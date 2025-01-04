package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	//예약 리스트 조회 (0:대기 , 1:완료, 2:취소)
    //예약 대기 리스트 조회 ( reservation_status = 0)
    @Query("SELECT r FROM Reservation r JOIN FETCH r.customer c JOIN FETCH r.service s " +
            "JOIN FETCH r.member m WHERE r.reservationStatus = 0")
    List<Reservation> findReservationsWithStatusZero();

    //예약 완료 리스트 조회 ( reservation_status = 1)
    @Query("SELECT r FROM Reservation r JOIN FETCH r.customer c JOIN FETCH r.service s " +
            "JOIN FETCH r.member m WHERE r.reservationStatus = 1")
    List<Reservation> findReservationsWithStatusNoOne();

    //예약 취소 리스트 조회 ( reservation_status = 2)
    @Query("SELECT r FROM Reservation r JOIN FETCH r.customer c JOIN FETCH r.service s " +
            "JOIN FETCH r.member m WHERE r.reservationStatus = 2")
    List<Reservation> findReservationsWithStatusTwo();


    //고객명 -> 고객 ID 조회
    @Query("SELECT c.customerId FROM Customer c WHERE c.customerName = :customerName")
    Integer findCustomerIdByName(@Param("customerName") String customerName);

    //서비스명 -> 서비스 코드 조회
    @Query("SELECT s.serviceCode FROM Service s WHERE s.serviceName = :serviceName")
    String findServiceCodeByName(@Param("serviceName") String serviceName);
    
    //멤버명 -> 멤버 ID 조회
    @Query("SELECT m.memberId FROM Member m WHERE m.memberName = :memberName")
    String findMemberIdByName(@Param("memberName") String memberName);
    
    //서비스 코드 -> 서비스 가격 조회
    @Query("SELECT s.servicePrice FROM Service s WHERE s.serviceCode = :serviceCode")
    int findServicePriceByCode(@Param("serviceCode") String serviceCode);
    
    // 예약 코드 -> 고객 ID 조회
    @Query("SELECT r.customer.customerId FROM Reservation r WHERE r.reservationNo = :reservationNo")
    int findCustomerIdByReservationNo(@Param("reservationNo") int reservationNo);

    // 예약 코드 -> 서비스 코드 조회
    @Query("SELECT r.service.serviceCode FROM Reservation r WHERE r.reservationNo = :reservationNo")
    String findServiceCodeByReservationNo(@Param("reservationNo") int reservationNo);

    // 예약 코드 -> 멤버 ID 조회
    @Query("SELECT r.member.memberId FROM Reservation r WHERE r.reservationNo = :reservationNo")
    String findMemberIdByReservationNo(@Param("reservationNo") int reservationNo);

    // 예약 코드 -> 예약 status 조회
    @Query("SELECT r.reservationStatus FROM Reservation r WHERE r.reservationNo = :reservationNo")
    int findReservationStatusByReservationNo(@Param("reservationNo") int reservationNo);

    //예약 상태 변경(완료(확정))
    @Modifying
    @Query("UPDATE Reservation r SET r.reservationStatus = 1 WHERE r.reservationNo = :reservationNo")
    int updateReservationStatusZero(@Param("reservationNo") int reservationNo);

    //예약 상태 변경(취소)
    @Modifying
    @Query("UPDATE Reservation r SET r.reservationStatus = 2 WHERE r.reservationNo = :reservationNo")
    int updateReservationStatusTwo(@Param("reservationNo") int reservationNo);

    // 서비스 이용 횟수 증가
    @Modifying
    @Query("UPDATE Service s SET s.serviceCnt = s.serviceCnt + 1 WHERE s.serviceCode = :serviceCode")
    void incrementServiceCount(@Param("serviceCode") String serviceCode);

    // 멤버 이용 횟수 증가
    @Modifying
    @Query("UPDATE Member m SET m.memberCnt = m.memberCnt + 1 WHERE m.memberId = :memberId")
    void incrementMemberCount(@Param("memberId") String memberId);

    // 고객 총 결제 금액 증가
    @Modifying
    @Query("UPDATE Customer c SET c.customerTotal = c.customerTotal + :servicePrice WHERE c.customerId = :customerId")
    void incrementCustomerTotal(@Param("customerId") int customerId, @Param("servicePrice") int servicePrice);
    
    // 서비스 이용 횟수 감소
    @Modifying
    @Query("UPDATE Service s SET s.serviceCnt = s.serviceCnt - 1 WHERE s.serviceCode = :serviceCode")
    void decrementServiceCount(@Param("serviceCode") String serviceCode);

    // 멤버 이용 횟수 감소
    @Modifying
    @Query("UPDATE Member m SET m.memberCnt = m.memberCnt - 1 WHERE m.memberId = :memberId")
    void decrementMemberCount(@Param("memberId") String memberId);

    // 고객 총 결제 금액 감소
    @Modifying
    @Query("UPDATE Customer c SET c.customerTotal = c.customerTotal - :servicePrice WHERE c.customerId = :customerId")
    void decrementCustomerTotal(@Param("customerId"
	) int customerId, @Param("servicePrice") int servicePrice);

}
