package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Contactinformation;
import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.ContactinformationRepository;
import mk.ukim.finki.synergymed.repositories.FacilityRepository;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.ContactInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactInformationServiceImpl implements ContactInformationService {

    private final ContactinformationRepository repo;
    private final UserRepository userRepo;
    private final FacilityRepository facilityRepo;

    // User-scoped
    @Override @Transactional(readOnly = true)
    public List<Contactinformation> listForUser(Integer userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public Contactinformation createForUser(Integer userId, String phone, String address) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Contactinformation ci = new Contactinformation();
        ci.setUser(user);
        ci.setFacility(null);
        ci.setPhone(phone);
        ci.setAddress(address);
        return repo.save(ci);
    }

    @Override
    public Contactinformation updateForUser(Integer contactId, Integer userId, String phone, String address) {
        Contactinformation ci = repo.findByIdAndUser_Id(contactId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found for user"));
        ci.setPhone(phone);
        ci.setAddress(address);
        return repo.save(ci);
    }

    @Override
    public void deleteForUser(Integer contactId, Integer userId) {
        Contactinformation ci = repo.findByIdAndUser_Id(contactId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found for user"));
        repo.delete(ci);
    }

    // Facility-scoped
    @Override @Transactional(readOnly = true)
    public List<Contactinformation> listForFacility(Integer facilityId) {
        return repo.findByFacility_Id(facilityId);
    }

    @Override
    public Contactinformation createForFacility(Integer facilityId, String phone, String address) {
        Facility facility = facilityRepo.findById(facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
        Contactinformation ci = new Contactinformation();
        ci.setFacility(facility);
        ci.setUser(null);
        ci.setPhone(phone);
        ci.setAddress(address);
        return repo.save(ci);
    }

    @Override
    public Contactinformation updateForFacility(Integer contactId, Integer facilityId, String phone, String address) {
        Contactinformation ci = repo.findByIdAndFacility_Id(contactId, facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found for facility"));
        ci.setPhone(phone);
        ci.setAddress(address);
        return repo.save(ci);
    }

    @Override
    public void deleteForFacility(Integer contactId, Integer facilityId) {
        Contactinformation ci = repo.findByIdAndFacility_Id(contactId, facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found for facility"));
        repo.delete(ci);
    }
}
