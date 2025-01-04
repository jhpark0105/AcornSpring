package com.erp.controller.branch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.erp.entity.Service;
import com.erp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.ServiceDto;
import com.erp.process.branch.ServiceProcess;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // React 서버 주소
public class ServiceController {
    @Autowired
    private ServiceProcess serviceProcess;
    @Autowired
    private ServiceRepository serviceRepository;

    // List 불러오기
    @GetMapping("service")
    public List<ServiceDto> listProcess() {
    	
        return serviceProcess.getDataAll();
    }

    // insertProcess
//    @PostMapping("service")
//    public Map<String, Object> insertProcess(@RequestBody ServiceDto serviceDto) {
//    	serviceProcess.insert(serviceDto);
//
//        return Map.of("isSuccess", true);
//    }

    @PostMapping("/service")
    public Map<String, Object> insertProcess(@RequestBody ServiceDto serviceDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 서비스 코드 중복 확인
            if (serviceRepository.existsById(serviceDto.getServiceCode())) {
                response.put("isSuccess", false);
                response.put("message", "이미 존재하는 서비스입니다.");
                return response; // 중복된 경우
            }

            // 서비스 저장
            Service service = new Service(serviceDto.getServiceCode(), serviceDto.getServiceName(), serviceDto.getServicePrice());
            serviceRepository.save(service);

            response.put("isSuccess", true);
            response.put("message", "서비스 등록 성공!");
            return response; // 성공적으로 저장된 경우
        } catch (Exception e) {
            // 예외 발생 시 상세 오류 메시지 반환
            response.put("isSuccess", false);
            response.put("message", "모든 필드를 채워주세요." );
            return response; // 오류 메시지 반환
        }
    }


//    @PutMapping("/service/{serviceCode}")
//    public Map<String, Object> updateProcess(@PathVariable("serviceCode") String serviceCode, @RequestBody ServiceDto serviceDto) {
//        serviceService.update(serviceCode, serviceDto);  // 서비스 수정 로직
//        return Map.of("isSuccess", true);
//    }
    
    // updateProcess
    @PutMapping("/service/{serviceCode}")
    public Map<String, Object> updateProcess(@PathVariable("serviceCode") String serviceCode, @RequestBody ServiceDto serviceDto) {
        serviceDto.setServiceCode(serviceCode); 
        String result = serviceProcess.update(serviceDto);

        return Map.of("isSuccess", true);
    }
    
    // deleteProcess
    @DeleteMapping("/service/{serviceCode}")
    public Map<String, Object> deleteProcess(@PathVariable("serviceCode") String serviceCode) {
    	serviceProcess.delete(serviceCode);
        
        
        return Map.of("isSuccess", true);
    }
}