package com.example.Invoice_Generator.user.controller;

import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import com.example.Invoice_Generator.domain.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


    @Controller
    @RequestMapping("/users") // Base URL for user-related operations
    public class UserController {

        @Autowired
        private UserDetailsRepo userDetailsRepository;

        @GetMapping("/list")
        public String getAllUsers(Model model) {
            model.addAttribute("users", userDetailsRepository.findAll());
            return "user_list"; // Thymeleaf template for displaying users
        }

        @GetMapping("/create")
        public String showCreateUserForm(Model model) {
            model.addAttribute("user", new UserDetails());
            return "user_form"; // Thymeleaf template for creating a new user
        }

        @PostMapping("/save")
        public String saveUser(@ModelAttribute UserDetails user) {
            userDetailsRepository.save(user);
            return "redirect:/users/list"; // Redirect to user list after saving
        }

        @GetMapping("/edit/{userId}") // Dynamic path for editing specific user
        public String showEditUserForm(@PathVariable int userId, Model model) {
            UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
            model.addAttribute("user", user);
            return "user_form"; // Reuse same template for editing
        }

        @PostMapping("/update/{userId}") // Dynamic path for updating specific user
        public String updateUser(@PathVariable Long userId, @ModelAttribute UserDetails user) {
            user.setUserId(userId); // Ensure ID remains unchanged
            userDetailsRepository.save(user);
            return "redirect:/users/list"; // Redirect to user list after updating
        }

        @GetMapping("/delete/{userId}") // Dynamic path for deleting specific user
        public String deleteUser(@PathVariable int userId) {
            userDetailsRepository.deleteById(userId);
            return "redirect:/users/list"; // Redirect to user list after deleting
        }
    }
