package com.erp.controller.branch;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.erp.entity.Alarm;
import com.erp.entity.Reservation;
import com.erp.process.branch.AlarmProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.erp.dto.CustomerDto;
import com.erp.dto.MemberDto;
import com.erp.dto.ReservationDto;
import com.erp.dto.ServiceDto;
import com.erp.process.branch.ReservationProcess;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // React 서버 주소
public class ReservationController {
	@Autowired
	private ReservationProcess reservationProcess;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AlarmProcess alarmProcess;

    // 서비스 목록 조회
    @GetMapping("/reservation/service")
    public List<ServiceDto> getServiceList() {
        return reservationProcess.getServiceData();  // 서비스 목록을 반환
    }

    // 고객 목록 조회
    @GetMapping("/reservation/customer")
    public List<CustomerDto> getCustomerData() {
        return reservationProcess.getCustomerData();  // 고객 목록을 반환
    }

    // 직원 목록 조회
    @GetMapping("/reservation/member")
    public List<MemberDto> getMemberList() {
        return reservationProcess.getAllMembers();  // 직원 목록을 반환
    }

    // 예약 전체 목록 조회
    @GetMapping("/reservation")
    public Object getList() {
        return reservationProcess.getData();
    }

    //예약 대기 리스트 조회
    @GetMapping("/reservation/confirm")
//    public List<ReservationDto> getConfirmList() {
//        return reservationProcess.getReservationsWithStatusZero();
//    }
    public List<Reservation> getConfirmList() {
        return reservationProcess.getReservationsWithStatusZero();
    }

    //예약 완료 리스트 조회
    @GetMapping("/reservation/finish")
//    public List<ReservationDto> getFinishList() {
//        return reservationProcess.getReservationsWithStatusOne();
//    }
    public List<Reservation> getFinishList() {
        return reservationProcess.getReservationsWithStatusOne();
    }

    //예약 취소 리스트 조회
    @GetMapping("/reservation/cancel")
//    public List<ReservationDto> getCancelList() {
//        return reservationProcess.getReservationsWithStatusTwo();
//    }
    public List<Reservation> getCancelList() {
        return reservationProcess.getReservationsWithStatusTwo();
    }

    // 예약 추가
    @PostMapping("/reservation")
    public Map<String, Object> insertData(@RequestBody ReservationDto reservationDto) {
        // 예약 처리
        reservationProcess.insertReservation(reservationDto);

        // 고객명을 가져오기
        String customerName = reservationDto.getCustomerName();
        String memberName = reservationDto.getMemberName();
        String reservationDate = reservationDto.getReservationDate();
        String reservationTime = reservationDto.getReservationTime();

//        // 알림 메시지 생성
//        String alarmContent = customerName + "님의 예약이 등록되었습니다!";
        // 알림 메시지 생성
        String alarmContent = customerName + "님의 예약이 등록되었습니다!\n"
                + "담당 디자이너: " + memberName + "\n"
                + "예약 일시: " + reservationDate +" "+ reservationTime;
        // 알림 생성 및 DB 저장
        Alarm alarm = com.erp.entity.Alarm.builder()
                .content(alarmContent)
                .build();

        alarmProcess.saveAlarm(alarm);

        // WebSocket으로 알림 전송
        messagingTemplate.convertAndSend("/topic/reservations",
                alarmContent);

        // 응답 데이터 생성
        Map<String, Object> map = new HashMap<>();
        map.put("isSuccess", true);
        return map;
    }

    // 예약 삭제
//    @DeleteMapping("/reservation/{reservationNo}")
//    public Map<String, Object> deleteData(@PathVariable("reservationNo") int reservationNo) {
//        reservationProcess.delete(reservationNo);
//        return Map.of("isSuccess", true);
//    }

    // 예약 수정
    @PutMapping("/reservation/{reservationNo}")
    public Map<String, Object> updateData(@PathVariable("reservationNo") int reservationNo, @RequestBody ReservationDto reservationDto) {
        reservationDto.setReservationNo(reservationNo);
        reservationProcess.update(reservationDto);
        return Map.of("isSuccess", true);
    }


    // 예약 완료(확정) 상태 수정
//    @PutMapping("/reservation/finish/{reservationNo}")
//    public String reservationFinish(@PathVariable("reservationNo") int reservationNo) {
//        reservationProcess.reservationFinish(reservationNo);
//        return "isSuccess";
//    }
    @PutMapping("/reservation/finish/{reservationNo}")
    public Map<String, Boolean> reservationFinish(@PathVariable("reservationNo") int reservationNo) {
        reservationProcess.reservationFinish(reservationNo);

        // JSON 객체 형태로 반환
        Map<String, Boolean> response = new HashMap<>();
        response.put("isSuccess", true);
        return response;
    }

    // 예약 취소 상태 수정
//    @PutMapping("/reservation/cancel/{reservationNo}")
//    public String reservationCancel(@PathVariable("reservationNo") int reservationNo) {
//        reservationProcess.reservationCancel(reservationNo);
//        return "isSuccess";
//    }
    @PutMapping("/reservation/cancel/{reservationNo}")
    public Map<String, Boolean> reservationCancel(@PathVariable("reservationNo") int reservationNo) {
        reservationProcess.reservationCancel(reservationNo);

        // JSON 객체 형태로 반환
        Map<String, Boolean> response = new HashMap<>();
        response.put("isSuccess", true);
        return response;
    }

}
