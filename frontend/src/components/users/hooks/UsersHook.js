import { useEffect, useState } from 'react';
import { UserRoleMapping } from '../../utils/Constants';
import UsersApiService from '../service/UsersApiService';

const useUsers = ({ page, size, role}) => {
    const [users, setUsers] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [usersRefresh, setUsersRefresh] = useState(false);
    const handleUsersChange = () => setUsersRefresh(!usersRefresh);

    const getUsers = async () => {
        const data = await UsersApiService.getAll(`?page=${page}&size=${size}`);
        const mappedUsers = (data.items || []).map(user => ({
        ...user,
        role: UserRoleMapping[user.role] || user.role
        }));
        setUsers(mappedUsers);
        setTotalPages(data.totalPages || 1);
    };

    useEffect(() => {
        getUsers();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [usersRefresh, page, role]);

    return {
        users,
        handleUsersChange,
        totalPages
    };
};

export default useUsers;
