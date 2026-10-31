package com.demo.insurance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "policies")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, updatable = false)
    private String policyNumber;
    
    private Double premiumAmount;
    private LocalDate expirationDate;
    private String status;

    public Policy() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPolicyNumber() { return policyNumber; }
    
    // Safety check: allow setting only on initial creation
    public void setPolicyNumber(String policyNumber) {
        if (this.policyNumber != null) {
            throw new IllegalStateException("Policy number is immutable and cannot be changed once set.");
        }
        this.policyNumber = policyNumber;
    }
    
    public Double getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(Double premiumAmount) { this.premiumAmount = premiumAmount; }
    
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
