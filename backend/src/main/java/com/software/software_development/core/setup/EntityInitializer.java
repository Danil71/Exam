package com.software.software_development.core.setup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.log.Loggable;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.PhoneNumberEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.PhoneType;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.service.entity.PhoneNumberService;
import com.software.software_development.service.entity.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityInitializer {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final PhoneNumberService phoneNumberService;
    private final UserService userService;

    @Loggable
    @Transactional
    public void initializeAll() {
        List<DepartmentEntity> departments = createDepartments();
        List<PhoneNumberEntity> phones = createPhones();
        List<EmployeeEntity> employees = createEmployees(departments, phones);
        createUsers(employees);
    }

    private List<DepartmentEntity> createDepartments() {
        List<DepartmentEntity> departments = new ArrayList<>();
        departments.add(departmentService.create(new DepartmentEntity("IT-отдел", "Разработка и поддержка ПО")));
        departments.add(departmentService.create(new DepartmentEntity("HR", "Управление персоналом")));
        departments.add(departmentService.create(new DepartmentEntity("Бухгалтерия", "Финансовый учёт")));
        departments.add(departmentService.create(new DepartmentEntity("Продажи", "Работа с клиентами")));
        departments.add(departmentService.create(new DepartmentEntity("Юридический", "Правовое сопровождение")));
        return departments;
    }

    private List<PhoneNumberEntity> createPhones() {
        List<PhoneNumberEntity> phones = new ArrayList<>();
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74951234567", PhoneType.WORK, 101)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74951234568", PhoneType.WORK, 102)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74957654321", PhoneType.MOBILE, 0)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74957654322", PhoneType.MOBILE, 0)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74951112233", PhoneType.FAX, 200)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74959876543", PhoneType.WORK, 301)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74959876544", PhoneType.WORK, 302)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74953334455", PhoneType.MOBILE, 0)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74954445566", PhoneType.WORK, 401)));
        phones.add(phoneNumberService.create(new PhoneNumberEntity("+74955556677", PhoneType.WORK, 105)));
        return phones;
    }

    private List<EmployeeEntity> createEmployees(List<DepartmentEntity> departments, List<PhoneNumberEntity> phones) {
        List<EmployeeEntity> employees = new ArrayList<>();

        employees.add(employeeService.create(
                new EmployeeEntity("Иванов Иван", "Ведущий разработчик", departments.get(0)),
                List.of(phones.get(0).getId(), phones.get(2).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Петрова Анна", "HR-менеджер", departments.get(1)),
                List.of(phones.get(1).getId(), phones.get(3).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Сидоров Пётр", "Бухгалтер", departments.get(2)),
                List.of(phones.get(5).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Козлова Мария", "Менеджер по продажам", departments.get(3)),
                List.of(phones.get(6).getId(), phones.get(7).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Смирнов Олег", "Юрист", departments.get(4)),
                List.of(phones.get(8).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Федорова Елена", "Системный администратор", departments.get(0)),
                List.of(phones.get(9).getId(), phones.get(0).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Никитин Дмитрий", "Разработчик", departments.get(0)),
                List.of(phones.get(2).getId())));
        employees.add(employeeService.create(
                new EmployeeEntity("Волкова Светлана", "Кадровик", departments.get(1)),
                List.of(phones.get(4).getId(), phones.get(1).getId())));

        return employees;
    }

    private void createUsers(List<EmployeeEntity> employees) {
        userService.create(new UserEntity(
                "admin",
                "admin@company.ru",
                "Admin123",
                UserRole.MANAGER,
                employees.get(5)
        ));
        userService.create(new UserEntity(
                "ivanov",
                "ivanov@company.ru",
                "User1234",
                UserRole.USER,
                employees.get(0)
        ));
        userService.create(new UserEntity(
                "petrova",
                "petrova@company.ru",
                "User1234",
                UserRole.USER,
                employees.get(1)
        ));
    }
}
