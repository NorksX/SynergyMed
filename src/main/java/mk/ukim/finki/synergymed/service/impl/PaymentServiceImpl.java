package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.*;
import mk.ukim.finki.synergymed.repositories.*;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import mk.ukim.finki.synergymed.service.ClubCardService; // NEW
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ShoppingCartService shoppingCartService;
    private final ClientorderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final PaymentmethodRepository paymentmethodRepo;
    private final DeliverycompanyRepository deliveryRepo;
    private final InventoryBrandedmedicineRepository inventoryBrandedmedicineRepository;
    private final ClubCardService clubCardService; // NEW

    @Override
    public Clientorder checkout(Client client, Shoppingcart cart, Integer paymentMethodId, Integer deliveryCompanyId) {
        return checkout(client, cart, paymentMethodId, deliveryCompanyId, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, timeout = 30)
    public Clientorder checkout(Client client, Shoppingcart cart, Integer paymentMethodId, Integer deliveryCompanyId, boolean useCard) {
        BigDecimal total = shoppingCartService.getTotal(cart);

        int baseAmount = total.intValue();
        int discount = 0;
        if (useCard) {
            var cardOpt = clubCardService.getByClientId(client.getId());
            if (cardOpt.isPresent()) {
                Clubcard card = cardOpt.get();
                Integer pts = card.getPoints();
                int points = pts == null ? 0 : pts;
                discount = Math.min(points / 2, baseAmount);
                if (discount > 0) {
                    card.setPoints(0);
                }
            }
        }
        int finalAmount = Math.max(0, baseAmount - discount);

        Paymentmethod method = paymentmethodRepo.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));

        Payment payment = new Payment();
        payment.setClient(client);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(finalAmount);
        payment.setStatus("во тек");
        paymentRepo.save(payment);

        Deliverycompany deliveryCompany = deliveryRepo.findById(deliveryCompanyId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery company not found"));

        Clientorder order = new Clientorder();
        order.setClient(client);
        order.setDeliveryCompany(deliveryCompany);
        order.setPayment(payment);
        order.setOrderDate(LocalDate.now());
        order.setExpectedArrivalDate(LocalDate.now().plusDays(7));
        order.setStatus("во тек");
        order.setTotalPrice(finalAmount);

        shoppingCartService.getMedicinesInCart(cart).forEach((medicine, qty) -> {
            ClientorderBrandedmedicine line = new ClientorderBrandedmedicine();
            ClientorderBrandedmedicineId id = new ClientorderBrandedmedicineId();
            id.setBrandedMedicineId(medicine.getId());
            line.setId(id);
            line.setOrder(order);
            line.setBrandedMedicine(medicine);
            line.setQuantity(qty);
            order.getItems().add(line);
        });

        for (ClientorderBrandedmedicine line : order.getItems()) {
            int remaining = line.getQuantity();
            Integer bmId = line.getBrandedMedicine().getId();

            List<InventoryBrandedmedicine> facilities =
                    inventoryBrandedmedicineRepository.lockAllByMedicineInPharmacies(bmId);

            for (InventoryBrandedmedicine ibm : facilities) {
                if (remaining <= 0) break;
                int take = Math.min(ibm.getQuantity(), remaining);
                if (take <= 0) continue;

                ibm.setQuantity(ibm.getQuantity() - take);
                ibm.setLastChanged(LocalDate.now());
                inventoryBrandedmedicineRepository.save(ibm);

                remaining -= take;
            }

            if (remaining > 0) {
                throw new IllegalStateException("Insufficient stock for medicine id=" + bmId);
            }
        }
        order.setStatus("во тек");
        payment.setStatus("завршено");

        orderRepo.save(order);

        paymentRepo.save(payment);

        shoppingCartService.clearCart(cart);

        return order;
    }
}
