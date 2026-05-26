package com.software.software_development.web.controller.entity;

import org.modelmapper.ModelMapper;
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
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.service.entity.UserService;
import com.software.software_development.web.dto.entity.UserDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public UserDto toDto(UserEntity entity) {
        UserDto dto = modelMapper.map(entity, UserDto.class);
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setEmployeeName(entity.getEmployee().getName());
        }
        dto.setPassword(null);
        return dto;
    }

    public UserEntity toEntity(UserDto dto) {
        UserEntity entity = modelMapper.map(dto, UserEntity.class);
        if (dto.getEmployeeId() != null) {
            EmployeeEntity employee = employeeService.get(dto.getEmployeeId());
            entity.setEmployee(employee);
        }
        return entity;
    }

    @GetMapping
    public PageDto<UserDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(userService.getAll(page, size), this::toDto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return toDto(userService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto dto) {
        return toDto(userService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody @Valid UserDto dto) {
        return toDto(userService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable Long id) {
        return toDto(userService.delete(id));
    }
}
