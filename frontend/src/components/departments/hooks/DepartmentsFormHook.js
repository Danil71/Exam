import { useCallback, useState } from 'react';
import toast from 'react-hot-toast';
import DepartmentsApiService from '../service/DepartmentsApiService';
import useDepartment from './DepartmentByIdHook';

const useDepartmentsForm = (id, departmentsChangeHandle) => {
    const { department, setDepartment, loading, error } = useDepartment(id);
    const [validated, setValidated] = useState(false);

    const resetValidity = useCallback(() => setValidated(false), []);

    const handleChange = useCallback((event) => {
        const { name, value } = event.target;
        setDepartment((prev) => ({ ...prev, [name]: value }));
    }, [setDepartment]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        setValidated(true);
        if (!event.currentTarget.checkValidity()) {
            toast.error('Заполните обязательные поля');
            return false;
        }
        const payload = { name: department.name, description: department.description };
        try {
            if (department.id) {
                await DepartmentsApiService.update(department.id, payload);
            } else {
                await DepartmentsApiService.create(payload);
            }
            toast.success('Отдел сохранён');
            if (departmentsChangeHandle) departmentsChangeHandle();
            return true;
        } catch (err) {
            toast.error(err?.message || 'Ошибка сохранения');
            return false;
        }
    };

    return { department, validated, handleSubmit, handleChange, loading, error, resetValidity };
};

export default useDepartmentsForm;
