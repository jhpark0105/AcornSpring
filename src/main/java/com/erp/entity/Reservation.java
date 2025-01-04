package com.erp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
	@Id
	@Column(name="reservation_no")
	private int reservationNo;
	@Column(name="reservation_date")
	private String reservationDate;
	@Column(name="reservation_time")
	private String reservationTime;
	@Column(name="reservation_comm")
	private String reservationComm;
	@Column(name="reservation_status")
	private int reservationStatus;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerId")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "serviceCode")
	private Service service;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "memberId")
	private Member member;
}
