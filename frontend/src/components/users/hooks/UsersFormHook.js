import { useState } from 'react';
import toast from 'react-hot-toast';
import UsersApiService from '../service/UsersApiService';
import useUser from './UserByIdHook';

const useUsersForm = (id, usersChangeHandle) => {
    const { user, setUser } = useUser(id);
    const [validated, setValidated] = useState(false);

    const resetValidity = () => setValidated(false);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setUser({ ...user, [name]: value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (!event.currentTarget.checkValidity()) {
            setValidated(true);
            return false;
        }
        const body = {
            login: user.login,
            email: user.email,
            password: user.password || undefined,
            role: user.role,
            employeeId: user.employeeId ? Number(user.employeeId) : null,
        };
        try {
            if (id === undefined || id === null) {
                await UsersApiService.create(body);
            } else {
                await UsersApiService.update(id, body);
            }
            if (usersChangeHandle) usersChangeHandle();
            toast.success('Пользователь сохранён');
            return true;
        } catch (err) {
            toast.error(err?.message || 'Ошибка сохранения');
            return false;
        }
    };

    return { user, validated, handleSubmit, handleChange, resetValidity };
};

export default useUsersForm;
