package com.software.software_development.web.controller.entity;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.web.dto.entity.DepartmentDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + "/departments")
@RequiredArgsConstructor
public class DepartmentPublicController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<DepartmentDto> getAll(@RequestParam(required = false, defaultValue = "") String name) {
        return departmentService.getAll(name).stream()
                .map(e -> modelMapper.map(e, DepartmentDto.class))
                .toList();
    }
}
