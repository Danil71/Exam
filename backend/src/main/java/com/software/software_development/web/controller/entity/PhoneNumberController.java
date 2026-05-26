package com.software.software_development.web.controller.entity;

import java.util.List;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.model.entity.PhoneNumberEntity;
import com.software.software_development.service.entity.PhoneNumberService;
import com.software.software_development.web.dto.entity.PhoneNumberDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/phone-number")
@RequiredArgsConstructor
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<PhoneNumberDto> getAll() {
        return StreamSupport.stream(phoneNumberService.getAll().spliterator(), false)
                .map(e -> modelMapper.map(e, PhoneNumberDto.class))
                .toList();
    }

    @GetMapping("/{id}")
    public PhoneNumberDto get(@PathVariable Long id) {
        return modelMapper.map(phoneNumberService.get(id), PhoneNumberDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneNumberDto create(@RequestBody @Valid PhoneNumberDto dto) {
        PhoneNumberEntity entity = modelMapper.map(dto, PhoneNumberEntity.class);
        return modelMapper.map(phoneNumberService.create(entity), PhoneNumberDto.class);
    }

    @PutMapping("/{id}")
    public PhoneNumberDto update(@PathVariable Long id, @RequestBody @Valid PhoneNumberDto dto) {
        PhoneNumberEntity entity = modelMapper.map(dto, PhoneNumberEntity.class);
        return modelMapper.map(phoneNumberService.update(id, entity), PhoneNumberDto.class);
    }

    @DeleteMapping("/{id}")
    public PhoneNumberDto delete(@PathVariable Long id) {
        return modelMapper.map(phoneNumberService.delete(id), PhoneNumberDto.class);
    }
}
