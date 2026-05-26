import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { makeAutoObservable } from 'mobx';
import toast from 'react-hot-toast';
import { API_URL } from '../components/api/ApiClient';
import AuthApiService from '../components/api/AuthApiService';

export default class Store {
    user = {};
    isAuth = false;
    isLoading = false;
    loginError = '';

    constructor() {
        makeAutoObservable(this);
        this.authApiService = new AuthApiService();
    }

    setAuth(bool) {
        this.isAuth = bool;
    }

    setUser(user) {
        this.user = user;
    }

    setLoading(bool) {
        this.isLoading = bool;
    }

    setLoginError(message) {
        this.loginError = message;
    }

    get isAdmin() {
        return this.user?.role === 'MANAGER';
    }

    resetAuthState() {
        this.setUser({});
        this.setAuth(false);
        localStorage.removeItem('token');
    }

    applyLoginSuccess(data) {
        if (!data?.accessToken) {
            throw new Error('No access token received');
        }
        localStorage.setItem('token', data.accessToken);
        const decoded = jwtDecode(data.accessToken);
        this.setUser({
            id: decoded.user_id,
            login: data.login,
            email: data.email,
            role: data.role,
            employeeId: data.employeeId,
        });
        this.setAuth(true);
        this.setLoginError('');
    }

    async login(login, password) {
        this.setLoginError('');
        try {
            const data = await this.authApiService.login({ login, password });
            this.applyLoginSuccess(data);
            toast.success('Вход выполнен');
        } catch (e) {
            const message = e?.message || 'Неверный логин или пароль';
            this.setLoginError(message);
            toast.error(message);
            this.resetAuthState();
        }
    }

    async register(login, email, password) {
        try {
            const data = await this.authApiService.register({ login, email, password });
            this.applyLoginSuccess(data);
            toast.success('Регистрация успешна');
        } catch (e) {
            toast.error(e?.message || 'Ошибка регистрации');
        }
    }

    async logout() {
        try {
            const login = this.user?.login;
            if (login) {
                await this.authApiService.logout({ login });
            }
        } catch (e) {
            toast.error(e?.message || 'Ошибка выхода');
        } finally {
            this.resetAuthState();
        }
    }

    async checkAuth() {
        this.setLoading(true);
        try {
            const response = await axios.put(`${API_URL}/auth/refresh-token`, null, {
                withCredentials: true,
                headers: { Accept: 'application/json' },
            });
            const data = response.data;
            localStorage.setItem('token', data.accessToken);
            const decoded = jwtDecode(data.accessToken);
            this.setUser({
                id: decoded.user_id,
                login: data.login,
                email: data.email,
                role: data.role,
                employeeId: data.employeeId,
            });
            this.setAuth(true);
        } catch {
            this.resetAuthState();
        } finally {
            this.setLoading(false);
        }
    }
}
