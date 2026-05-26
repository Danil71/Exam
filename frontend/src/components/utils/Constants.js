export const PASSWORD_PATTERN_STRING = '^.{6,60}$';
export const LOGIN_PATTERN_STRING = '^[a-zA-Z0-9_]{3,30}$';
export const PHONE_PATTERN_STRING = '^((8|\\+7)[\\- ]?)?\\(?\\d{3,5}\\)?[\\- ]?\\d{1}([\\- ]?\\d{1}){4,8}$';

export const UserRoleMapping = {
    MANAGER: 'Администратор',
    USER: 'Пользователь',
};
