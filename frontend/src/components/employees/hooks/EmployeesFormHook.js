import { useState } from 'react';
import toast from 'react-hot-toast';
import EmployeesApiService from '../service/EmployeesApiService';
import useEmployee from './EmployeeByIdHook';

const useEmployeesForm = (id, employeesChangeHandle) => {
    const { employee, setEmployee } = useEmployee(id);
    const [validated, setValidated] = useState(false);

    const resetValidity = () => setValidated(false);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setEmployee({ ...employee, [name]: value });
    };

    const handlePhoneIdsChange = (ids) => {
        setEmployee({ ...employee, phoneNumberIds: ids });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (!event.currentTarget.checkValidity()) {
            setValidated(true);
            return false;
        }
        const body = {
            name: employee.name,
            position: employee.position,
            departmentId: Number(employee.departmentId),
            phoneNumberIds: employee.phoneNumberIds || [],
        };
        try {
            if (id === undefined || id === null || id < 0) {
                await EmployeesApiService.create(body);
            } else {
                await EmployeesApiService.update(id, body);
            }
            if (employeesChangeHandle) employeesChangeHandle();
            toast.success('Сотрудник сохранён');
            return true;
        } catch (err) {
            toast.error(err?.message || 'Ошибка сохранения');
            return false;
        }
    };

    return { employee, validated, handleSubmit, handleChange, handlePhoneIdsChange, resetValidity };
};

export default useEmployeesForm;
