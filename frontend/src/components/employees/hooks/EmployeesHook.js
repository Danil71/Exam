import { useEffect, useState } from 'react';
import EmployeesApiService from '../service/EmployeesApiService';

const useEmployees = ({page, size}) => {
    const [employees, setEmployees] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [employeesRefresh, setEmployeesRefresh] = useState(false);
    const handleEmployeesChange = () => setEmployeesRefresh(!employeesRefresh);

    const getEmployees = async () => {
        let expand = `?`;
        if (page) expand = `${expand}page=${page}`;
        if (size) expand = `${expand}size=${size}`;
        const data = await EmployeesApiService.getAll(expand);
        setEmployees(data.items ?? []);
        setTotalPages(data.totalPages ?? 0);
    };

    useEffect(() => {
        getEmployees();
    }, [employeesRefresh, page, size]);

    return {
        employees,
        totalPages,
        handleEmployeesChange
    };
};

export default useEmployees;
