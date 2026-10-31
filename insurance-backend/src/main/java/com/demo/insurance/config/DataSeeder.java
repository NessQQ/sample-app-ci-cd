package com.demo.insurance.config;

import com.demo.insurance.model.Owner;
import com.demo.insurance.model.Policy;
import com.demo.insurance.model.Truck;
import com.demo.insurance.controller.BackendInsuranceCRMController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BackendInsuranceCRMController insuranceCRMController;

    // We inject the controller directly so it routes through our premium calculator engine
    public DataSeeder(BackendInsuranceCRMController insuranceCRMController) {
        this.insuranceCRMController = insuranceCRMController;
    }

    @Override
    public void run(String... args) throws Exception {
        // --- CLIENT 1: Raul Martinez (Small Operator - 1 Truck) ---
        Owner owner1 = new Owner();
        owner1.setName("Raul Martinez");
        owner1.setEmail("raul@martineztrans.com");
        owner1.setPhoneNumber("555-0192");

        Policy policy1 = new Policy();
        policy1.setPolicyNumber("POL-99281");
        policy1.setExpirationDate(LocalDate.now().plusMonths(6)); // Compliant
        policy1.setStatus("ACTIVE");
        owner1.setInsurancePolicy(policy1);

        Truck truck1 = new Truck();
        truck1.setVin("1YV1Y58H7A5XXXXXX");
        truck1.setLicensePlate("7X89-H3");
        truck1.setModel("2022 Volvo VNL");

        owner1.setTrucks(new ArrayList<>());
        owner1.getTrucks().add(truck1);

        // Run through the controller to automatically calculate Raul's premium ($200 + $600 = $800)
        insuranceCRMController.createOwner(owner1);


        // --- CLIENT 2: Alpha Logistics (Medium Fleet - 3 Trucks) ---
        Owner owner2 = new Owner();
        owner2.setName("Alpha Logistics LLC");
        owner2.setEmail("dispatch@alphahauling.com");
        owner2.setPhoneNumber("555-8831");

        Policy policy2 = new Policy();
        policy2.setPolicyNumber("POL-44102");
        policy2.setExpirationDate(LocalDate.now().minusDays(5)); // Non-Compliant (Expired)
        policy2.setStatus("EXPIRED");
        owner2.setInsurancePolicy(policy2);

        Truck truck2 = new Truck();
        truck2.setVin("1FVACWDB8FHXXXXXX");
        truck2.setLicensePlate("3M45-A1");
        truck2.setModel("2020 Freightliner Cascadia");

        Truck truck3 = new Truck();
        truck3.setVin("1XP5D49X9DDXXXXXX");
        truck3.setLicensePlate("9P12-B8");
        truck3.setModel("2023 Peterbilt 579");

        Truck truck4 = new Truck();
        truck4.setVin("1NKHA4EX1FHXXXXXX");
        truck4.setLicensePlate("4K55-C2");
        truck4.setModel("2021 Kenworth T680");

        owner2.setTrucks(new ArrayList<>());
        owner2.getTrucks().add(truck2);
        owner2.getTrucks().add(truck3);
        owner2.getTrucks().add(truck4);

        // Run through the controller to automatically calculate Alpha's premium ($200 + ($500 * 3) = $1700)
        insuranceCRMController.createOwner(owner2);

        System.out.println(">> Database successfully seeded via Premium Calculation Engine!");
    }
}
