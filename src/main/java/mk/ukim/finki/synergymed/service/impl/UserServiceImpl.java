package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.exceptions.*;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public User register(String firstName, String lastName, String username, String password, String repeatPassword, String email, String gender, LocalDate dateOfBirth) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidInputException();
        }
        if (!password.equals(repeatPassword)) {
            throw new PasswordsMismatchException("Passwords do not match.");
        }
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        if(this.userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException("Email address already exists.");
        }


        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);

        User savedUser = userRepository.save(user);

        Client client = new Client();
        client.setUsers(savedUser);
        client.setIsVerified(false);

        clientRepository.save(client);

        return savedUser;
    }
}
