package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String getSignupPage(){
        return "register";
    }

    @PostMapping
    public String signup(Model model,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String repeatPassword,
                         @RequestParam String email,
                         @RequestParam String gender,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth){
        try {
            userService.register(firstName, lastName, username, password, repeatPassword, email, gender, dateOfBirth);
            return "register-success";

        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());

            return "register";
        }
    }
}

