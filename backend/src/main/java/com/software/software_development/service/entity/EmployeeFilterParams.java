package com.software.software_development.service.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeFilterParams {
    private final String search;
    private final Long departmentId;
    private final String position;
    private final String phone;
    private final String owner;
    private final Integer extensionMin;
    private final Integer extensionMax;
    private final Long restrictToDepartmentId;
}
