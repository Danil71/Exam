package com.software.software_development.web.controller.entity;

import java.util.List;

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
import com.software.software_development.service.entity.CategoryService;
import com.software.software_development.web.dto.entity.CategoryDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.ShopDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ShopDtoMapper mapper;

    @GetMapping(Constants.API_URL + Constants.CATEGORIES_URL)
    public List<CategoryDto> getPublic() {
        return categoryService.getAll().stream().map(mapper::toCategoryDto).toList();
    }

    @GetMapping(Constants.API_URL + Constants.ADMIN_PREFIX + Constants.CATEGORIES_URL)
    public PageDto<CategoryDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(categoryService.getAll(page, size), mapper::toCategoryDto);
    }

    @GetMapping(Constants.API_URL + Constants.ADMIN_PREFIX + Constants.CATEGORIES_URL + "/{id}")
    public CategoryDto get(@PathVariable Long id) {
        return mapper.toCategoryDto(categoryService.get(id));
    }

    @PostMapping(Constants.API_URL + Constants.ADMIN_PREFIX + Constants.CATEGORIES_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryDto dto) {
        return mapper.toCategoryDto(categoryService.create(mapper.toCategoryEntity(dto)));
    }

    @PutMapping(Constants.API_URL + Constants.ADMIN_PREFIX + Constants.CATEGORIES_URL + "/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid CategoryDto dto) {
        return mapper.toCategoryDto(categoryService.update(id, mapper.toCategoryEntity(dto)));
    }

    @DeleteMapping(Constants.API_URL + Constants.ADMIN_PREFIX + Constants.CATEGORIES_URL + "/{id}")
    public CategoryDto delete(@PathVariable Long id) {
        return mapper.toCategoryDto(categoryService.delete(id));
    }
}
