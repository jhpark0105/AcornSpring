package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.dto.ServiceDto;
import com.erp.entity.Service;


public interface ServiceRepository extends JpaRepository<Service, String>{
	List<ServiceDto> findByServiceCode(String serviceCode);

	boolean existsById(String serviceCode);
	boolean existsByServiceName(String serviceName); // 서비스 이름으로 존재 여부 확인
}