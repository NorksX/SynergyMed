package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.User;

public interface UserService {
    User findUserById(Integer id);
    User findUserByUsername(String username);
}
