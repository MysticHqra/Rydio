package com.rydio.vehicle.service;

import com.rydio.vehicle.dto.CreateVehicleRequest;
import com.rydio.vehicle.dto.VehicleResponse;
import com.rydio.vehicle.entity.Vehicle;
import com.rydio.vehicle.repository.VehicleRepository;
import com.rydio.user.entity.User;
import com.rydio.user.repository.UserRepository;
import com.rydio.common.exception.ResourceNotFoundException;
import com.rydio.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<VehicleResponse> getAvailableVehicles() {
        return vehicleRepository.findByStatus(Vehicle.VehicleStatus.AVAILABLE)
                .stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
    
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return new VehicleResponse(vehicle);
    }
    
    public List<VehicleResponse> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId)
                .stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
    
    public VehicleResponse createVehicle(CreateVehicleRequest request, Long ownerId) {
        // Validate owner exists
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));
        
        // Check if license plate already exists
        if (vehicleRepository.findByLicensePlate(request.getLicensePlate()).isPresent()) {
            throw new BadRequestException("Vehicle with license plate " + request.getLicensePlate() + " already exists");
        }
        
        // Check if registration number already exists (if provided)
        if (request.getRegistrationNumber() != null && !request.getRegistrationNumber().isEmpty()) {
            if (vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber()).isPresent()) {
                throw new BadRequestException("Vehicle with registration number " + request.getRegistrationNumber() + " already exists");
            }
        }
        
        // Create new vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setEngineCapacity(request.getEngineCapacity());
        vehicle.setSeatCount(request.getSeatCount());
        vehicle.setDailyRate(request.getDailyRate());
        vehicle.setHourlyRate(request.getHourlyRate());
        vehicle.setMileage(request.getMileage());
        vehicle.setInsuranceNumber(request.getInsuranceNumber());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setLocation(request.getLocation());
        vehicle.setDescription(request.getDescription());
        vehicle.setFeatures(request.getFeatures());
        vehicle.setImageUrl(request.getImageUrl());
        vehicle.setOwner(owner);
        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(savedVehicle);
    }
    
    public VehicleResponse updateVehicle(Long id, CreateVehicleRequest request, Long ownerId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        
        // Check if the current user is the owner or admin
        if (!vehicle.getOwner().getId().equals(ownerId)) {
            User user = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!"ADMIN".equals(user.getRole().toString())) {
                throw new BadRequestException("You can only update your own vehicles");
            }
        }
        
        // Update vehicle details
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setEngineCapacity(request.getEngineCapacity());
        vehicle.setSeatCount(request.getSeatCount());
        vehicle.setDailyRate(request.getDailyRate());
        vehicle.setHourlyRate(request.getHourlyRate());
        vehicle.setMileage(request.getMileage());
        vehicle.setInsuranceNumber(request.getInsuranceNumber());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setLocation(request.getLocation());
        vehicle.setDescription(request.getDescription());
        vehicle.setFeatures(request.getFeatures());
        vehicle.setImageUrl(request.getImageUrl());
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(updatedVehicle);
    }
    
    public void deleteVehicle(Long id, Long ownerId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        
        // Check if the current user is the owner or admin
        if (!vehicle.getOwner().getId().equals(ownerId)) {
            User user = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!"ADMIN".equals(user.getRole().toString())) {
                throw new BadRequestException("You can only delete your own vehicles");
            }
        }
        
        vehicleRepository.delete(vehicle);
    }
    
    public VehicleResponse updateVehicleStatus(Long id, Vehicle.VehicleStatus status, Long ownerId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        
        // Check if the current user is the owner or admin
        if (!vehicle.getOwner().getId().equals(ownerId)) {
            User user = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!"ADMIN".equals(user.getRole().toString())) {
                throw new BadRequestException("You can only update your own vehicles");
            }
        }
        
        vehicle.setStatus(status);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(updatedVehicle);
    }
    
    public List<VehicleResponse> searchVehicles(Vehicle.VehicleType vehicleType, String location, 
                                              Double minPrice, Double maxPrice) {
        return vehicleRepository.findVehiclesWithFilters(vehicleType, location, minPrice, maxPrice)
                .stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
}