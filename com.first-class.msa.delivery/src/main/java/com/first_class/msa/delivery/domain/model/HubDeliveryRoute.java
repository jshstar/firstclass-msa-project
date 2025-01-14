package com.first_class.msa.delivery.domain.model;

import java.time.LocalDateTime;

import com.first_class.msa.delivery.domain.common.HubDeliveryStatus;
import com.first_class.msa.delivery.domain.valueobject.Sequence;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_delivery_route")
public class HubDeliveryRoute extends BaseTime{
	@Id
	@Tsid
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", nullable = false)
	private Delivery delivery;

	@Column(name = "departure_hub_id", nullable = false)
	private Long departureHubId;

	@Column(name = "arrival_hub_id", nullable = false)
	private Long arrivalHubId;

	@Column(name = "expected_distance", nullable = false)
	private Double expectedDistance;

	@Column(name = "expected_time", nullable = false)
	private Long expectedTime;

	@Column(name = "actual_distance")
	private Double actualDistance;

	@Column(name = "actual_time")
	private Long actualTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "hub_status", nullable = false)
	private HubDeliveryStatus hubDeliveryStatus;

	@Embedded
	private Sequence sequence;

	@Column(name = "Hub_agent_id")
	private Long hubAgentId;

	public static HubDeliveryRoute createHubDeliveryRoute(
		Long departureHubId,
		Long arrivalHubId,
		Double expectedDistance,
		Long expectedTime,
		Sequence sequence,
		Long hubAgentId,
		Delivery delivery
	){
		return HubDeliveryRoute.builder()
			.departureHubId(departureHubId)
			.arrivalHubId(arrivalHubId)
			.expectedDistance(expectedDistance)
			.expectedTime(expectedTime)
			.sequence(sequence)
			.hubAgentId(hubAgentId)
			.hubDeliveryStatus(HubDeliveryStatus.WAITING_FOR_TRANSIT)
			.delivery(delivery)
			.build();
	}

	public void updateHubDeliveryStatus(HubDeliveryStatus newStatus) {
		validateStatusTransition(newStatus);
		this.hubDeliveryStatus = newStatus;
	}

	private void validateStatusTransition(HubDeliveryStatus newStatus) {
		if (this.hubDeliveryStatus == HubDeliveryStatus.WAITING_FOR_TRANSIT && newStatus != HubDeliveryStatus.IN_TRANSIT) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_HUB_STATUS));
		}
		if (this.hubDeliveryStatus == HubDeliveryStatus.IN_TRANSIT && newStatus != HubDeliveryStatus.ARRIVED_AT_HUB) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_HUB_STATUS));
		}
	}

	public void setDeleteHubDeliveryRoute(Long userId){
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
	}

	public void setCreateByAndUpdateBy(Long userId){
		this.setCreatedBy(userId);
		this.setUpdatedBy(userId);
	}

}
