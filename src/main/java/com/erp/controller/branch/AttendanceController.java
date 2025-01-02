package com.erp.controller.branch;

import com.erp.entity.Member;
import com.erp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.erp.dto.AttendanceDto;
import com.erp.process.branch.AttendanceProcess;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}) // React 서버 주소
public class AttendanceController {

    @Autowired
    private AttendanceProcess attendanceProcess;
    @Autowired
    private MemberRepository memberRepository;

    // 전체 근태 목록 조회
    @GetMapping("/all")
    public ResponseEntity<?> getAllAttendance() {
        try {
            List<AttendanceDto> attendanceList = attendanceProcess.getAllList();
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 예외 출력
            return ResponseEntity.status(500).body("데이터 조회 중 오류 발생 : "+ e.getMessage());
        }
    }

    // 특정 날짜 근태 조회
    @GetMapping("/by-date")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByDate(
            @RequestParam String memberId,
            @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(
                attendanceProcess.getAttendanceByDate(memberId, localDate)
                        .stream()
                        .map(AttendanceDto::fromEntity)
                        .toList()
        );
    }

    // 출근 처리
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(
            @RequestParam String memberId,
            @RequestParam String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다."));

        if (!member.getMemberPassword().equals(password)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        attendanceProcess.checkIn(memberId);
        return ResponseEntity.ok("출근 처리 완료");
    }

    // 퇴근 처리
    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@RequestParam String memberId) {
        attendanceProcess.checkOut(memberId);
        return ResponseEntity.ok("퇴근 처리 완료");
    }

    // 근태 추가
    @PostMapping("/add")
    public ResponseEntity<?> addAttendance(@RequestBody AttendanceDto attendanceDto) {
        return ResponseEntity.ok(attendanceProcess.insert(attendanceDto));
    }

    // 근태 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateAttendance(@RequestBody AttendanceDto attendanceDto) {
        try {
            String result = attendanceProcess.update(attendanceDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("수정 작업 오류: " + e.getMessage());
        }
    }

    // 근태 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Integer id) {
        return ResponseEntity.ok(attendanceProcess.delete(id));
    }
}
