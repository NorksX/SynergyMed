package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.User;

import java.time.LocalDate;

public interface UserService {
    User findUserById(Integer id);
    User findUserByUsername(String username);
    User register(String firstName,
                  String lastName,
                  String username,
                  String password,
                  String repeatPassword,
                  String email,
                  String gender,
                  LocalDate dateOfBirth);
}
