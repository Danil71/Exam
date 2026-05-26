import { ApiClient } from './ApiClient';

class AuthApiService {
    async register(body) {
        return ApiClient.post('/auth/register', body);
    }

    async login(body) {
        return ApiClient.post('/auth/login', body);
    }

    async refreshToken() {
        return ApiClient.put('/auth/refresh-token');
    }

    async logout(body) {
        return ApiClient.post('/auth/logout', body);
    }
}

export default AuthApiService;
