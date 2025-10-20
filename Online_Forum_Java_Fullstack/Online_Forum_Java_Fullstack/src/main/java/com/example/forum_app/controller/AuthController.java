package com.example.forum_app.controller;
import com.example.forum_app.model.User;
import com.example.forum_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;  // Added import
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {  // Added RedirectAttributes
        System.out.println("Registration attempt for: " + user.getUsername() + ", Email: " + user.getEmail());
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "register";
        }
        try {
            userService.registerUser(user);
            System.out.println("Registration successful, redirecting to login");
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");  // Flash attribute for redirect
            return "redirect:/login";  // Redirect to navigate to login page
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
