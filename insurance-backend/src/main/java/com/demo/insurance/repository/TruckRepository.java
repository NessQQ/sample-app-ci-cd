package com.demo.insurance.repository;

import com.demo.insurance.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
    // Allows looking up a truck instantly by its unique license plate
    Optional<Truck> findByLicensePlate(String licensePlate);
}
