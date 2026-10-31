package com.demo.insurance.controller;

import com.demo.insurance.model.Owner;
import com.demo.insurance.model.Policy;
import com.demo.insurance.model.Truck;
import com.demo.insurance.repository.OwnerRepository;
import com.demo.insurance.repository.TruckRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BackendInsuranceCRMController {

    private final OwnerRepository ownerRepository;
    private final TruckRepository truckRepository;

    public BackendInsuranceCRMController(OwnerRepository ownerRepository, TruckRepository truckRepository) {
        this.ownerRepository = ownerRepository;
        this.truckRepository = truckRepository;
    }

    /**
     * INTERNAL PREMIUM CALCULATOR ENGINE
     * Dynamically calculates premium based on commercial fleet size brackets.
     */
    private double calculateFleetPremium(int truckCount) {
        if (truckCount == 0) {
            return 200.00; // Base processing fee for an empty fleet wrapper
        }
        
        double baseFee = 200.00;
        double costPerTruck;

        // Apply basic range logic
        if (truckCount <= 2) {
            costPerTruck = 600.00; // Small operators pay standard rate
        } else if (truckCount <= 5) {
            costPerTruck = 500.00; // Medium fleet discount
        } else {
            costPerTruck = 400.00; // Large fleet volume discount
        }

        return baseFee + (truckCount * costPerTruck);
    }

    // 1. Get all owners, their computed policies, and their trucks
    @GetMapping("/owners")
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    // 2. Initial Onboarding: Creates Owner + Policy + Trucks, calculating premium dynamically
    @PostMapping("/owners")
    public Owner createOwner(@RequestBody Owner owner) {
        int startingTruckCount = 0;
        
        if (owner.getTrucks() != null) {
            startingTruckCount = owner.getTrucks().size();
            for (Truck truck : owner.getTrucks()) {
                truck.setOwner(owner);
            }
        }

        // Automatically generate and attach the calculated policy
        Policy policy = owner.getInsurancePolicy();
        if (policy != null) {
            double dynamicPremium = calculateFleetPremium(startingTruckCount);
            policy.setPremiumAmount(dynamicPremium);
        }

        return ownerRepository.save(owner);
    }

    // 3. Add a truck later: Automatically scales fleet count and updates premium based on ranges
    @PostMapping("/owners/{ownerId}/trucks")
    public Truck addTruckToOwner(@PathVariable Long ownerId, @RequestBody Truck newTruck) {
        // Find existing owner
        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + ownerId));
        
        // Link new asset
        newTruck.setOwner(existingOwner);
        existingOwner.getTrucks().add(newTruck);

        // Recalculate full policy premium based on the updated fleet size
        Policy policy = existingOwner.getInsurancePolicy();
        if (policy != null) {
            int updatedTruckCount = existingOwner.getTrucks().size();
            double newPremium = calculateFleetPremium(updatedTruckCount);
            policy.setPremiumAmount(newPremium);
            
            System.out.println(">> Fleet size changed to " + updatedTruckCount 
                    + ". Policy " + policy.getPolicyNumber() + " automatically re-rated to $" + newPremium);
        }
        
        // Save asset (cascades and updates premium in the database)
        return truckRepository.save(newTruck);
    }

    // 4. Compliance Gateway: Quick lookup by license plate
    @GetMapping("/compliance/{licensePlate}")
    public Truck getTruckCompliance(@PathVariable String licensePlate) {
        return truckRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new RuntimeException("Truck not found with plate: " + licensePlate));
    }
}
