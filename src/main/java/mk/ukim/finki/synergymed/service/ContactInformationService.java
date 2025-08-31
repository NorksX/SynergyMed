package mk.ukim.finki.synergymed.service;


import mk.ukim.finki.synergymed.models.Contactinformation;

import java.util.List;

public interface ContactInformationService {
    List<Contactinformation> listForUser(Integer userId);
    Contactinformation createForUser(Integer userId, String phone, String address);
    Contactinformation updateForUser(Integer contactId, Integer userId, String phone, String address);
    void deleteForUser(Integer contactId, Integer userId);

    // Facility-scoped
    List<Contactinformation> listForFacility(Integer facilityId);
    Contactinformation createForFacility(Integer facilityId, String phone, String address);
    Contactinformation updateForFacility(Integer contactId, Integer facilityId, String phone, String address);
    void deleteForFacility(Integer contactId, Integer facilityId);
}

