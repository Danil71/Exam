import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import useDepartments from '../../departments/hooks/DepartmentsHook';
import PhoneNumbersApiService from '../../api/PhoneNumbersApiService';
import Input from '../../input/Input';
import MultiSelect from '../../input/MultiSelect';
import Select from '../../input/Select';

const EmployeesForm = ({ employee, handleChange, handlePhoneIdsChange }) => {
    const { departments } = useDepartments();
    const [phoneOptions, setPhoneOptions] = useState([]);

    useEffect(() => {
        PhoneNumbersApiService.getAll().then((phones) => {
            setPhoneOptions(phones.map((p) => ({
                id: p.id,
                description: `${p.number} (${p.type}, доб. ${p.extension})`,
            })));
        });
    }, []);

    const selectedPhones = (employee.phoneNumberIds || []).map((id) => ({
        value: id,
        label: String(id),
    }));

    return (
        <>
            <Input name="name" label="ФИО" value={employee.name || ''} onChange={handleChange} required data-testid="emp-name" />
            <Input name="position" label="Должность" value={employee.position || ''} onChange={handleChange} required data-testid="emp-position" />
            <Select values={departments} name="departmentId" label="Отдел" value={employee.departmentId} onChange={handleChange} required />
            <MultiSelect
                label="Телефоны"
                options={phoneOptions}
                selectedValues={selectedPhones}
                onChange={(ids) => handlePhoneIdsChange(ids.map(Number))}
            />
        </>
    );
};

EmployeesForm.propTypes = {
    employee: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
    handlePhoneIdsChange: PropTypes.func.isRequired,
};

export default EmployeesForm;
