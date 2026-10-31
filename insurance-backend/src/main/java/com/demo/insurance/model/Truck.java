package com.demo.insurance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, updatable = false)
    private String vin;
    
    @Column(nullable = false, unique = true, updatable = false)
    private String licensePlate;
    
    private String model;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Truck() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getVin() { return vin; }
    
    // Safety check: allow setting only on initial creation
    public void setVin(String vin) {
        if (this.vin != null) {
            throw new IllegalStateException("VIN is immutable and cannot be changed once set.");
        }
        this.vin = vin;
    }
    
    public String getLicensePlate() { return licensePlate; }
    
    // Safety check: allow setting only on initial creation
    public void setLicensePlate(String licensePlate) {
        if (this.licensePlate != null) {
            throw new IllegalStateException("License plate is immutable and cannot be changed once set.");
        }
        this.licensePlate = licensePlate;
    }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }
}
