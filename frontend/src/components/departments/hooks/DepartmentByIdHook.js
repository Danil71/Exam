import { useEffect, useState } from 'react';
import DepartmentsApiService from '../service/DepartmentsApiService';

const useDepartment = (id) => {
    const emptyDepartment = { id: '', name: '', description: '' };
    const [department, setDepartment] = useState({ ...emptyDepartment });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchDepartmentData = async (departmentId = null) => {
        setLoading(true);
        setError(null);
        try {
            if (departmentId && departmentId > 0) {
                const data = await DepartmentsApiService.get(departmentId);
                setDepartment(data);
            } else {
                setDepartment({ ...emptyDepartment });
            }
        } catch (err) {
            setError(err);
            setDepartment({ ...emptyDepartment });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDepartmentData(id);
    }, [id]);

    return { department, setDepartment, loading, error, refetchDepartmentData: fetchDepartmentData };
};

export default useDepartment;
