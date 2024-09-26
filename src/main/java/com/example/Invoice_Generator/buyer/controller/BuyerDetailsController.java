package com.example.Invoice_Generator.buyer.controller;

import com.example.Invoice_Generator.buyer.service.BuyerDetailsService;
import com.example.Invoice_Generator.domain.BuyerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/buyers")
public class BuyerDetailsController {

    @Autowired
    private BuyerDetailsService buyerDetailsService;

    // Display all buyers
    @GetMapping("/list")
    public String getAllBuyer(Model model) {
        List<BuyerDetails> buyers = buyerDetailsService.getAllBuyers();
        model.addAttribute("buyers", buyers);
        return "buyer_list";
    }

    // Show form to create a new buyer
    @GetMapping("/new")
    public String showCreateBuyerForm(Model model) {
        BuyerDetails buyerDetails = new BuyerDetails();
        model.addAttribute("buyerDetails", buyerDetails);
        return "buyer_form";
    }

    // Save a new or edited buyer
    @PostMapping
    public String saveBuyer(@ModelAttribute("buyerDetails") BuyerDetails buyerDetails) {
        buyerDetailsService.saveOrUpdateBuyer(buyerDetails);
        return "redirect:/buyers/list";
    }

    // Show form to update an existing buyer
    @GetMapping("/edit/{id}")
    public String showUpdateBuyerForm(@PathVariable int id, Model model) {
        Optional<BuyerDetails> buyerDetails = buyerDetailsService.getBuyerById(id);
        if (buyerDetails.isPresent()) {
            model.addAttribute("buyerDetails", buyerDetails.get());
            return "buyer_form";
        } else {
            return "redirect:/buyers/list";
        }
    }

    // Delete a buyer
    @GetMapping("/delete/{id}")
    public String deleteBuyer(@PathVariable int id) {
        buyerDetailsService.deleteBuyer(id);
        return "redirect:/buyers/list";
    }
}
