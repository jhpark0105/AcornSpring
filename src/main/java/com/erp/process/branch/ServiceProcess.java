package com.erp.process.branch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.erp.dto.ServiceDto;
import com.erp.entity.Service;
import com.erp.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceProcess {
    @Autowired
    private ServiceRepository serviceRepository;

    // service 모두 읽기
    public List<ServiceDto> getDataAll() {
        List<Service> services = serviceRepository.findAll();
        return services.stream()
                .map(service -> new ServiceDto(service.getServiceCode(), service.getServiceName(), service.getServicePrice()))
                .collect(Collectors.toList());
    }

    // service 등록
    public String insert(ServiceDto serviceDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 빈 칸 검사
            if (isAnyFieldEmpty(serviceDto)) {
                response.put("isSuccess", false);
                response.put("message", "모든 필드를 채워주세요.");
                return response.toString();
            }

            // 서비스 코드 중복 확인
            if (serviceRepository.existsById(serviceDto.getServiceCode())) {
                response.put("isSuccess", false);
                response.put("message", "이미 존재하는 서비스 코드입니다.");
                return response.toString();
            }

            // 서비스 이름 중복 확인
            if (serviceRepository.existsByServiceName(serviceDto.getServiceName())) {
                response.put("isSuccess", false);
                response.put("message", "이미 존재하는 서비스 이름입니다.");
                return response.toString();
            }

            // 서비스 저장
            Service service = new Service(serviceDto.getServiceCode(), serviceDto.getServiceName(), serviceDto.getServicePrice());
            serviceRepository.save(service);

            response.put("isSuccess", true);
            response.put("message", "서비스 등록 성공!");
            return response.toString();
        } catch (Exception e) {
            response.put("isSuccess", false);
            response.put("message", "입력 자료 오류입니다. " + e.getMessage());
            return response.toString();
        }
    }

    // service 수정 & 삭제를 위한 레코드 읽기
    public List<ServiceDto> getData(String serviceCode) {
        List<ServiceDto> services = serviceRepository.findByServiceCode(serviceCode);
        return services.stream()
                .map(service -> new ServiceDto(service.getServiceCode(), service.getServiceName(), service.getServicePrice()))
                .collect(Collectors.toList());
    }

    // service 수정
    public String update(ServiceDto serviceDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 빈 칸 검사
            if (isAnyFieldEmpty(serviceDto)) {
                response.put("isSuccess", false);
                response.put("message", "모든 필드를 채워주세요.");
                return response.toString();
            }

            // 서비스 이름 중복 확인
            Service existingService = serviceRepository.findById(serviceDto.getServiceCode()).orElse(null);
            if (existingService != null && !existingService.getServiceName().equals(serviceDto.getServiceName())
                    && serviceRepository.existsByServiceName(serviceDto.getServiceName())) {
                response.put("isSuccess", false);
                response.put("message", "이미 존재하는 서비스 이름입니다.");
                return response.toString();
            }

            // DTO -> 엔티티
            Service service = new Service(serviceDto.getServiceCode(), serviceDto.getServiceName(), serviceDto.getServicePrice());
            serviceRepository.save(service);

            response.put("isSuccess", true);
            response.put("message", "서비스 수정 성공!");
            return response.toString();
        } catch (Exception e) {
            response.put("isSuccess", false);
            response.put("message", "수정 작업 오류: " + e.getMessage());
            return response.toString();
        }
    }

    // service 삭제
    public String delete(String serviceCode) {
        try {
            serviceRepository.deleteById(serviceCode);
            return "isSuccess";
        } catch (Exception e) {
            return "삭제 작업 오류 : " + e.getMessage();
        }
    }

    // 모든 필드가 비어있는지 검사
    private boolean isAnyFieldEmpty(ServiceDto serviceDto) {
        return serviceDto.getServiceCode() == null || serviceDto.getServiceCode().isEmpty()
                || serviceDto.getServiceName() == null || serviceDto.getServiceName().isEmpty()
                || serviceDto.getServicePrice() <= 0; // 0 이하인 경우 유효하지 않은 값으로 간주
    }
}