package com.software.software_development.web.helper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.PhoneNumberEntity;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.web.dto.entity.EmployeeDto;
import com.software.software_development.web.dto.entity.PhoneNumberDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeDtoMapper {

    private final ModelMapper modelMapper;
    private final DepartmentService departmentService;

    public EmployeeDto toDto(EmployeeEntity entity) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        if (entity.getDepartment() != null) {
            dto.setDepartmentId(entity.getDepartment().getId());
            dto.setDepartmentName(entity.getDepartment().getName());
        }
        List<PhoneNumberDto> phones = new ArrayList<>();
        List<Long> phoneIds = new ArrayList<>();
        for (PhoneNumberEntity phone : entity.getPhoneNumbers()) {
            phones.add(modelMapper.map(phone, PhoneNumberDto.class));
            phoneIds.add(phone.getId());
        }
        dto.setPhoneNumbers(phones);
        dto.setPhoneNumberIds(phoneIds);
        return dto;
    }

    public EmployeeEntity toEntity(EmployeeDto dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        if (dto.getDepartmentId() != null) {
            entity.setDepartment(departmentService.get(dto.getDepartmentId()));
        }
        return entity;
    }
}
