package com.rydio.vehicle.service;

import com.rydio.vehicle.dto.VehicleDto;
import com.rydio.vehicle.dto.VehicleSearchDto;
import com.rydio.vehicle.entity.Vehicle;
import com.rydio.vehicle.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        if (vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new RuntimeException("Vehicle with this license plate already exists");
        }

        Vehicle vehicle = mapToEntity(vehicleDto);
        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(savedVehicle);
    }

    public VehicleDto updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        // Check if license plate is being changed and if it already exists
        if (!existingVehicle.getLicensePlate().equals(vehicleDto.getLicensePlate()) &&
            vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new RuntimeException("Vehicle with this license plate already exists");
        }

        updateEntityFromDto(existingVehicle, vehicleDto);
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return mapToDto(updatedVehicle);
    }

    public VehicleDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
        return mapToDto(vehicle);
    }

    public List<VehicleDto> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Page<VehicleDto> getVehiclesWithPagination(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return vehicleRepository.findAll(pageable).map(this::mapToDto);
    }

    public List<VehicleDto> searchVehicles(VehicleSearchDto searchDto) {
        Specification<Vehicle> spec = createSpecification(searchDto);
        return vehicleRepository.findAll(spec).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<VehicleDto> getAvailableVehicles() {
        return vehicleRepository.findByStatus(Vehicle.VehicleStatus.AVAILABLE).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<VehicleDto> getVehiclesByType(Vehicle.VehicleType type) {
        return vehicleRepository.findByVehicleType(type).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
        
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            throw new RuntimeException("Cannot delete a rented vehicle");
        }
        
        vehicleRepository.delete(vehicle);
    }

    public VehicleDto updateVehicleStatus(Long vehicleId, Vehicle.VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
        
        vehicle.setStatus(status);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(updatedVehicle);
    }

    public List<String> getAllBrands() {
        return vehicleRepository.findAllBrands();
    }

    public List<String> getModelsByBrand(String brand) {
        return vehicleRepository.findModelsByBrand(brand);
    }

    public List<String> getAllLocations() {
        return vehicleRepository.findAllLocations();
    }

    private Specification<Vehicle> createSpecification(VehicleSearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (searchDto.getVehicleType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleType"), searchDto.getVehicleType()));
            }
            if (searchDto.getBrand() != null && !searchDto.getBrand().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("brand")), 
                    "%" + searchDto.getBrand().toLowerCase() + "%"));
            }
            if (searchDto.getModel() != null && !searchDto.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("model")), 
                    "%" + searchDto.getModel().toLowerCase() + "%"));
            }
            if (searchDto.getMinYear() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), searchDto.getMinYear()));
            }
            if (searchDto.getMaxYear() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("year"), searchDto.getMaxYear()));
            }
            if (searchDto.getMinDailyRate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dailyRate"), searchDto.getMinDailyRate()));
            }
            if (searchDto.getMaxDailyRate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dailyRate"), searchDto.getMaxDailyRate()));
            }
            if (searchDto.getLocation() != null && !searchDto.getLocation().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")), 
                    "%" + searchDto.getLocation().toLowerCase() + "%"));
            }
            if (searchDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), searchDto.getStatus()));
            }
            if (searchDto.getMinSeatCount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seatCount"), searchDto.getMinSeatCount()));
            }
            if (searchDto.getMaxSeatCount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seatCount"), searchDto.getMaxSeatCount()));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private Vehicle mapToEntity(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setColor(dto.getColor());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setEngineCapacity(dto.getEngineCapacity());
        vehicle.setSeatCount(dto.getSeatCount());
        vehicle.setDailyRate(dto.getDailyRate());
        vehicle.setHourlyRate(dto.getHourlyRate());
        vehicle.setMileage(dto.getMileage());
        vehicle.setInsuranceNumber(dto.getInsuranceNumber());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setLocation(dto.getLocation());
        vehicle.setDescription(dto.getDescription());
        vehicle.setFeatures(dto.getFeatures());
        vehicle.setImageUrl(dto.getImageUrl());
        return vehicle;
    }

    private void updateEntityFromDto(Vehicle vehicle, VehicleDto dto) {
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setColor(dto.getColor());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setEngineCapacity(dto.getEngineCapacity());
        vehicle.setSeatCount(dto.getSeatCount());
        vehicle.setDailyRate(dto.getDailyRate());
        vehicle.setHourlyRate(dto.getHourlyRate());
        vehicle.setMileage(dto.getMileage());
        vehicle.setInsuranceNumber(dto.getInsuranceNumber());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setLocation(dto.getLocation());
        vehicle.setDescription(dto.getDescription());
        vehicle.setFeatures(dto.getFeatures());
        vehicle.setImageUrl(dto.getImageUrl());
        if (dto.getStatus() != null) {
            vehicle.setStatus(dto.getStatus());
        }
    }

    private VehicleDto mapToDto(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setColor(vehicle.getColor());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setFuelType(vehicle.getFuelType());
        dto.setEngineCapacity(vehicle.getEngineCapacity());
        dto.setSeatCount(vehicle.getSeatCount());
        dto.setDailyRate(vehicle.getDailyRate());
        dto.setHourlyRate(vehicle.getHourlyRate());
        dto.setMileage(vehicle.getMileage());
        dto.setInsuranceNumber(vehicle.getInsuranceNumber());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setStatus(vehicle.getStatus());
        dto.setLocation(vehicle.getLocation());
        dto.setDescription(vehicle.getDescription());
        dto.setFeatures(vehicle.getFeatures());
        dto.setImageUrl(vehicle.getImageUrl());
        return dto;
    }
}