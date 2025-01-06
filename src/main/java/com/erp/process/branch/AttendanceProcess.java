package com.erp.process.branch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.erp.entity.Member;
import com.erp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.erp.dto.AttendanceDto;
import com.erp.entity.Attendance;
import com.erp.repository.AttendanceRepository;

@Repository
public class AttendanceProcess {

    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private MemberRepository memberRepository; // MemberRepository 추가

    
    // 전체 근태 목록 조회
    public List<AttendanceDto> getAllList() {
        try {
            List<Attendance> attendanceList = repository.findAll();
            if (attendanceList == null || attendanceList.isEmpty()) {
                return Collections.emptyList(); // 데이터가 없으면 빈 리스트 반환
            }
            return attendanceList.stream()
                    .map(AttendanceDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("근태 데이터를 가져오는 중 오류 발생: " + e.getMessage());
        }
    }

    // 근태 기록 추가
    @Transactional
    public Map<String, Object> insert(AttendanceDto dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            repository.findById(dto.getAttendanceId()).ifPresent(record -> {
                throw new RuntimeException("이미 등록된 근태 기록입니다.");
            });

            // Member 객체를 MemberRepository에서 조회
            Member member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("해당 직원 정보를 찾을 수 없습니다."));

            Attendance newData = Attendance.fromDto(dto, member); // Member 객체 주입
            repository.save(newData);

            response.put("Success", true);
            response.put("message", "근태 기록을 등록하였습니다.");
        } catch (Exception e) {
            response.put("Success", false);
            response.put("message", "입력 자료 오류입니다: " + e.getMessage());
        }
        return response;
    }

    // 근태 기록 조회
    public Attendance getData(Integer attendanceId) {
        return repository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("근태 기록을 찾을 수 없습니다."));
    }

    // 출근
    public void checkIn(String memberId) {
        // 해당 직원의 출근 기록 확인
        boolean alreadyCheckedIn = repository.findByMemberMemberIdAndAttendanceDate(memberId, LocalDate.now())
                .stream()
                .anyMatch(attendance -> attendance.getCheckIn() != null);

        if (alreadyCheckedIn) {
            throw new AlreadyCheckedInException("이미 오늘 출근 기록이 있습니다.");
        }

        // 직원 정보 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다."));

        Attendance attendance = new Attendance();
        attendance.setMember(member); // member 객체 설정
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.now());
        attendance.setAttendanceStatus("출근");
        repository.save(attendance);
    }

    // 퇴근 처리
    public void checkOut(String memberId) {
        Attendance attendance = repository.findByMemberMemberIdAndAttendanceDate(memberId, LocalDate.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("출근 기록을 찾을 수 없습니다."));

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("이미 퇴근 기록이 있습니다.");
        }

        attendance.setCheckOut(LocalTime.now());
        attendance.setAttendanceStatus("퇴근");
        repository.save(attendance);
    }

    // 근태 기록 수정
    @Transactional
    public Map<String, Object> update(AttendanceDto dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 기존 데이터 조회
            Attendance existingAttendance = repository.findById(dto.getAttendanceId())
                    .orElseThrow(() -> new RuntimeException("근태 기록을 찾을 수 없습니다."));

            Member member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("해당 직원 정보를 찾을 수 없습니다."));

            // 필드 업데이트
            existingAttendance.setCheckIn(dto.getCheckIn());
            existingAttendance.setCheckOut(dto.getCheckOut());
            existingAttendance.setAttendanceDate(dto.getAttendanceDate());
            existingAttendance.setAttendanceStatus(dto.getAttendanceStatus());
            existingAttendance.setMember(member); // 수정 시 Member도 매핑

            // 저장
            repository.save(existingAttendance);
            response.put("Success", true);
            response.put("message", "근태 정보가 수정되었습니다.");
        } catch (Exception e) {
            response.put("Success", false);
            response.put("message", "근태 수정 중 오류 발생: " + e.getMessage());
        }
        return response;
    }

    // 근태 기록 삭제
    @Transactional
    public String delete(Integer attendanceId) {
        try {
            repository.deleteById(attendanceId);
            return "Success";
        } catch (Exception e) {
            return "삭제 작업 오류입니다: " + e.getMessage();
        }
    }

    // 특정 날짜의 근태 상태 조회
    public List<Attendance> getAttendanceByDate(String memberId, LocalDate date) {
        return repository.findByMemberMemberIdAndAttendanceDate(memberId, date);
    }

    // 근무 시간 계산
    public Duration calculateWorkHours(Integer attendanceId) {
        Attendance attendance = repository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("근태 기록을 찾을 수 없습니다."));
        return Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
    }

    // 지각 및 조퇴 판단
    public String determineStatus(LocalTime checkInTime) {
        LocalTime startOfWork = LocalTime.of(9, 0); // 예: 오전 9시 기준
        if (checkInTime.isAfter(startOfWork)) {
            return "지각";
        }
        return "정상";
    }

    // 전체 근태 기록 조회
    public List<Attendance> getAllAttendances() {
        return repository.findAll();
    }

    // 커스텀 예외 클래스
    public static class AlreadyCheckedInException extends RuntimeException {
        public AlreadyCheckedInException(String message) {
            super(message);
        }
    }

}
