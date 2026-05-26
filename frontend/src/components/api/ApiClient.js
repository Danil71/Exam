import axios from 'axios';
import toast from 'react-hot-toast';

export const API_URL = 'http://localhost:8080/api/v1';

function isAuthRequest(url = '') {
    return url.includes('/auth/login') || url.includes('/auth/register');
}

function formatApiError(apiError) {
    const code = apiError?.error || apiError?.errorCode;
    const message = apiError?.message || 'Ошибка запроса';
    if (code === 'BAD_CREDENTIALS' || code === 'AUTHENTICATION_FAILED') {
        return 'Неверный логин или пароль';
    }
    if (code === 'ACCESS_DENIED') return 'Доступ запрещён';
    if (code === 'RESOURCE_NOT_FOUND') return 'Данные не найдены';
    if (code === 'DTO_VALIDATION_FAILED') return 'Проверьте правильность заполнения полей';
    if (code === 'INVALID_ARGUMENT' && message) return message;
    return message;
}

function responseHandler(response) {
    const { status, data } = response;
    if ([200, 201, 202, 206].includes(status)) {
        return data;
    }
    if (status === 204) {
        return;
    }
    throw { message: `Некорректный ответ сервера: ${status}`, status };
}

function responseErrorHandler(error) {
    const apiError = error.response?.data || {};
    const url = error.config?.url || '';
    apiError.status = error.response?.status;
    apiError.message = formatApiError(apiError);

    if (!isAuthRequest(url)) {
        if (apiError.status === 401) {
            toast.error('Требуется авторизация');
        } else if (apiError.status === 403) {
            toast.error(apiError.message || 'Доступ запрещён');
        } else if (apiError.status === 404) {
            toast.error('Не найдено');
        } else if (apiError.status >= 500) {
            toast.error('Ошибка сервера');
        } else if (apiError.status === 400) {
            toast.error(apiError.message);
        }
    }

    return Promise.reject(apiError);
}

function addAuthToken(config) {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}

async function responseAuthErrorInterceptor(error) {
    const originalRequest = error.config;
    const url = originalRequest?.url || '';

    if (error.response?.status === 401 && !originalRequest._isRetry) {
        const errorCode = error.response.data?.error;
        if (errorCode === 'BAD_CREDENTIALS' || errorCode === 'AUTHENTICATION_FAILED' || isAuthRequest(url)) {
            return Promise.reject(error.response.data);
        }
        if (url.includes('/refresh-token') || url.includes('/auth/login')) {
            return Promise.reject(error.response?.data || error);
        }
        originalRequest._isRetry = true;
        try {
            const response = await axios.put(`${API_URL}/auth/refresh-token`, {}, {
                withCredentials: true,
                headers: { Accept: 'application/json' },
            });
            localStorage.setItem('token', response.data.accessToken);
            return ApiClient.request(originalRequest);
        } catch (refreshError) {
            return Promise.reject(refreshError.response?.data || refreshError);
        }
    }
    return null;
}

export const ApiClient = axios.create({
    baseURL: API_URL,
    withCredentials: true,
    timeout: 30000,
    headers: { Accept: 'application/json' },
});

ApiClient.interceptors.request.use(addAuthToken);
ApiClient.interceptors.response.use(
    responseHandler,
    async (error) => {
        const retry = await responseAuthErrorInterceptor(error);
        if (retry) return retry;
        return responseErrorHandler(error);
    }
);
