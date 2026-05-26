package com.software.software_development.web.controller.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.web.dto.entity.EmployeeDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.EmployeeDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeDtoMapper employeeDtoMapper;

    @GetMapping
    public PageDto<EmployeeDto> getAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(employeeService.getAll(name, page, size), employeeDtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public EmployeeDto get(@PathVariable Long id) {
        return employeeDtoMapper.toDto(employeeService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@RequestBody @Valid EmployeeDto dto) {
        EmployeeEntity entity = employeeService.bindDepartment(employeeDtoMapper.toEntity(dto), dto.getDepartmentId());
        return employeeDtoMapper.toDto(employeeService.create(entity, dto.getPhoneNumberIds()));
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody @Valid EmployeeDto dto) {
        EmployeeEntity entity = employeeService.bindDepartment(employeeDtoMapper.toEntity(dto), dto.getDepartmentId());
        return employeeDtoMapper.toDto(employeeService.update(id, entity, dto.getPhoneNumberIds()));
    }

    @DeleteMapping("/{id}")
    public EmployeeDto delete(@PathVariable Long id) {
        return employeeDtoMapper.toDto(employeeService.delete(id));
    }
}
