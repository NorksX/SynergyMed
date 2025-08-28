package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.service.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            var userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generate(userDetails);

            // store jwt in session
            session.setAttribute("jwt_token", token);
            session.setAttribute("username", username);
            session.setAttribute("user", userDetails);

            // redirect to home after a successful login
            return "redirect:/home";

        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("username", username);
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login");
            model.addAttribute("username", username);
            return "login";
        }
    }
}