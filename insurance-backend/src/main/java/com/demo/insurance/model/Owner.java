package com.demo.insurance.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "policy_id", referencedColumnName = "id")
    private Policy insurancePolicy;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Truck> trucks;

    public Owner() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Policy getInsurancePolicy() { return insurancePolicy; }
    public void setInsurancePolicy(Policy insurancePolicy) { this.insurancePolicy = insurancePolicy; }
    
    public List<Truck> getTrucks() { return trucks; }
    public void setTrucks(List<Truck> trucks) { this.trucks = trucks; }
}
