package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.Shoppingcart;

public interface PaymentService {
    Clientorder checkout(Client client, Shoppingcart cart,
                         Integer paymentMethodId, Integer deliveryCompanyId,
                         boolean useCard);
    Clientorder checkout(Client client, Shoppingcart cart, Integer paymentMethodId, Integer deliveryCompanyId);
}
