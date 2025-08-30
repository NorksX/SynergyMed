package mk.ukim.finki.synergymed.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.*;
import mk.ukim.finki.synergymed.repositories.*;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final ShoppingCartService shoppingCartService;
    private final ClientorderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final PaymentmethodRepository paymentmethodRepo;
    private final DeliverycompanyRepository deliveryRepo;

    @Override
    public Clientorder checkout(Client client, Shoppingcart cart, Integer paymentMethodId, Integer deliveryCompanyId) {

        BigDecimal total = shoppingCartService.getTotal(cart);

        Paymentmethod method = paymentmethodRepo.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));

        Payment payment = new Payment();
        payment.setClient(client);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(total.intValue());
        payment.setStatus("PENDING");
        paymentRepo.save(payment);


        Deliverycompany deliveryCompany = deliveryRepo.findById(deliveryCompanyId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery company not found"));


        Clientorder order = new Clientorder();
        order.setClient(client);
        order.setDeliveryCompany(deliveryCompany);
        order.setPayment(payment);
        order.setOrderDate(LocalDate.now());
        order.setExpectedArrivalDate(LocalDate.now().plusDays(3));
        order.setStatus("PROCESSING");
        order.setTotalPrice(total.intValue());

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

        orderRepo.save(order);

        payment.setStatus("COMPLETED");
        paymentRepo.save(payment);

        shoppingCartService.clearCart(cart);

        return order;
    }
}