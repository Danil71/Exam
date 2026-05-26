import { useEffect, useState } from 'react';
import DepartmentsApiService from '../service/DepartmentsApiService';

const useDepartments = () => {
    const [departments, setDepartments] = useState([]);
    const [departmentsRefresh, setDepartmentsRefresh] = useState(false);
    const handleDepartmentsChange = () => setDepartmentsRefresh(!departmentsRefresh);

    const getDepartments = async () => {
        const data = await DepartmentsApiService.getAll();
        setDepartments(data ?? []);

    };

    useEffect(() => {
        getDepartments();
    }, [departmentsRefresh]);

    return {
        departments,
        handleDepartmentsChange
    };
};

export default useDepartments;
