package com.demo.insurance.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Controller
public class FrontendController {

    // URL pointing directly to our running backend microservice
    private final String BACKEND_URL = "http://localhost:8080/api/v1";
    private final RestTemplate restTemplate = new RestTemplate();

    // 1. The Main Dashboard Layout: Fetches all clients and checks compliance
    @GetMapping("/")
    public String viewDashboard(Model model) {
        // Fetch raw fleet list from backend api
        List<Map<String, Object>> owners = restTemplate.getForObject(BACKEND_URL + "/owners", List.class);
        
        model.addAttribute("owners", owners);
        return "dashboard"; // Points to dashboard.html
    }

    // 2. Form Submission Action: Handles adding a new truck to an existing fleet later
    @PostMapping("/add-truck")
    public String addTruck(@RequestParam Long ownerId,
                           @RequestParam String vin,
                           @RequestParam String licensePlate,
                           @RequestParam String modelName) {
        
        // Construct a clean dictionary map matching our Truck JSON footprint
        Map<String, String> requestPayload = Map.of(
            "vin", vin,
            "licensePlate", licensePlate,
            "model", modelName
        );

        // POST directly to our backend's truck appending endpoint
        restTemplate.postForObject(
            BACKEND_URL + "/owners/" + ownerId + "/trucks", 
            requestPayload, 
            Map.class
        );

        // Refresh back to dashboard view to instantly show the updated fleet count & new premium!
        return "redirect:/";
    }
}
