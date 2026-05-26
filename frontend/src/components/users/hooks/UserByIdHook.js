import { useEffect, useState } from 'react';
import UsersApiService from '../service/UsersApiService';

const useUser = (id) => {
    const emptyUser = {
        id: '',
        login: '',
        email: '',
        password: '',
        role: 'USER',
        employeeId: '',
        employeeName: '',
    };

    const [user, setUser] = useState({ ...emptyUser });

    useEffect(() => {
        const load = async () => {
            if (id && id > 0) {
                const data = await UsersApiService.get(id);
                setUser({ ...data, password: '' });
            } else {
                setUser({ ...emptyUser });
            }
        };
        load();
    }, [id]);

    return { user, setUser };
};

export default useUser;
