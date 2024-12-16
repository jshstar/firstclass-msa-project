package com.first_class.msa.delivery.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first_class.msa.delivery.domain.model.Delivery;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {

	Optional<Delivery> findByIdAndDeletedByIsNotNull(Long deliveryId);
}