package com.erp.dto;

import com.erp.entity.Customer;
import com.erp.entity.Member;
import com.erp.entity.Reservation;
import com.erp.entity.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
	
	private int reservationNo;
	private String reservationDate;
	private String reservationTime;
	private String reservationComm;
	private int reservationStatus;
	private int customerId;
	private String customerName;
	private String memberId;
	private String memberName;
	private String serviceCode;
	private String serviceName;
	private int servicePrice;
	private Customer customer;
	private Service service;
	private Member member;
	
	
	//DTO -> Entity
	public Reservation toEntity() {
	    Reservation reservation = new Reservation();
	    reservation.setReservationNo(reservationNo);
	    reservation.setReservationDate(reservationDate);
	    reservation.setReservationTime(reservationTime);
	    reservation.setReservationComm(reservationComm);
		reservation.setReservationStatus(reservationStatus);
	    reservation.setCustomer(customer);
	    reservation.setService(service);
	    reservation.setMember(member);
	    return reservation;
	}
	
	//Entity -> DTO
	public static ReservationDto fromEntity(Reservation reservation) {
    	return ReservationDto.builder()
    			.reservationNo(reservation.getReservationNo())
    			.reservationDate(reservation.getReservationDate())
    			.reservationTime(reservation.getReservationTime())
    			.reservationComm(reservation.getReservationComm())
				.reservationStatus(reservation.getReservationStatus())
    			.customerId(reservation.getCustomer().getCustomerId())
    			.customerName(reservation.getCustomer().getCustomerName())
    			.serviceCode(reservation.getService().getServiceCode())
    			.serviceName(reservation.getService().getServiceName())
    			.servicePrice(reservation.getService().getServicePrice())
    			.memberId(reservation.getMember().getMemberId())
    			.memberName(reservation.getMember().getMemberName())
    			.build();
	}
}
