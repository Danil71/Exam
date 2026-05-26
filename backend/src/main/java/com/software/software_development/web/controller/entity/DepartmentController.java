package com.software.software_development.web.controller.entity;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.web.dto.entity.DepartmentDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<DepartmentDto> getAll(@RequestParam(name = "name", defaultValue = "") String name) {
        return departmentService.getAll(name).stream()
                .map(e -> modelMapper.map(e, DepartmentDto.class))
                .toList();
    }

    @GetMapping("/{id}")
    public DepartmentDto get(@PathVariable Long id) {
        return modelMapper.map(departmentService.get(id), DepartmentDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto create(@RequestBody @Valid DepartmentDto dto) {
        DepartmentEntity entity = modelMapper.map(dto, DepartmentEntity.class);
        return modelMapper.map(departmentService.create(entity), DepartmentDto.class);
    }

    @PutMapping("/{id}")
    public DepartmentDto update(@PathVariable Long id, @RequestBody @Valid DepartmentDto dto) {
        DepartmentEntity entity = modelMapper.map(dto, DepartmentEntity.class);
        return modelMapper.map(departmentService.update(id, entity), DepartmentDto.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
