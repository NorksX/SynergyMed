package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;

@org.springframework.web.bind.annotation.ControllerAdvice
public class GlobalModelAttributes {
    @org.springframework.web.bind.annotation.ModelAttribute
    public void addSessionUser(org.springframework.ui.Model model, HttpSession session) {
        Object u = session.getAttribute("user");
        Object name = session.getAttribute("username");
        if (name != null) model.addAttribute("username", name);
        if (u != null) model.addAttribute("user", u);
    }
}
