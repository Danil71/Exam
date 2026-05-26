package com.software.software_development.web.controller.entity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.utility.SecurityUtils;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.service.entity.EmployeeFilterParams;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.service.entity.UserService;
import com.software.software_development.web.dto.entity.EmployeeDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.EmployeeDtoMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.EMPLOYEES_URL)
@RequiredArgsConstructor
public class EmployeeDirectoryController {

    private final EmployeeService employeeService;
    private final UserService userService;
    private final EmployeeDtoMapper employeeDtoMapper;

    @GetMapping("/my")
    public PageDto<EmployeeDto> getMyDepartment(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer extensionMin,
            @RequestParam(required = false) Integer extensionMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        UserEntity user = userService.get(SecurityUtils.getCurrentUserId());
        EmployeeFilterParams params = EmployeeFilterParams.builder()
                .search(search)
                .departmentId(departmentId)
                .position(position)
                .phone(phone)
                .extensionMin(extensionMin)
                .extensionMax(extensionMax)
                .build();
        return PageDtoMapper.toDto(employeeService.getMyDepartment(user, params, page, size), employeeDtoMapper::toDto);
    }

    @GetMapping("/public")
    public PageDto<EmployeeDto> getPublicDirectory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String owner,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        EmployeeFilterParams params = EmployeeFilterParams.builder()
                .search(search)
                .departmentId(departmentId)
                .position(position)
                .phone(phone)
                .owner(owner)
                .build();
        return PageDtoMapper.toDto(employeeService.getPublicDirectory(params, page, size), employeeDtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public EmployeeDto get(@PathVariable Long id) {
        return employeeDtoMapper.toDto(employeeService.get(id));
    }
}
