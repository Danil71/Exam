import { useEffect, useState } from 'react';
import EmployeesApiService from '../service/EmployeesApiService';

const useEmployee = (id) => {
    const emptyEmployee = { id: '', name: '', position: '', departmentId: '', phoneNumberIds: [] };

    const [employee, setEmployee] = useState({ ...emptyEmployee });

    useEffect(() => {
        const load = async () => {
            if (id && id > 0) {
                const data = await EmployeesApiService.get(id);
                setEmployee({
                    ...data,
                    phoneNumberIds: data.phoneNumberIds || data.phoneNumbers?.map((p) => p.id) || [],
                });
            } else {
                setEmployee({ ...emptyEmployee });
            }
        };
        load();
    }, [id]);

    return { employee, setEmployee };
};

export default useEmployee;
