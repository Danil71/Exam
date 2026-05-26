import PropTypes from 'prop-types';
import Input from '../../input/Input';
import Select from '../../input/Select';
import { LOGIN_PATTERN_STRING, PASSWORD_PATTERN_STRING } from '../../utils/Constants';

const UsersForm = ({ user, handleChange }) => {
    const ROLE_OPTIONS = [
        { id: 'USER', name: 'Пользователь' },
        { id: 'MANAGER', name: 'Администратор' },
    ];

    return (
        <>
            <Input name="login" label="Логин" value={user.login || ''} onChange={handleChange} required pattern={LOGIN_PATTERN_STRING} />
            <Input name="email" label="Email" value={user.email || ''} onChange={handleChange} type="email" required />
            <Input name="password" label="Пароль" value={user.password ?? ''} onChange={handleChange} type="password" pattern={PASSWORD_PATTERN_STRING} minLength={6} required={!user.id} />
            <Select name="role" label="Роль" values={ROLE_OPTIONS} value={user.role} onChange={handleChange} required />
        </>
    );
};

UsersForm.propTypes = {
    user: PropTypes.object,
    handleChange: PropTypes.func,
};

export default UsersForm;
