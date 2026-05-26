import { ApiClient } from './ApiClient';

class DepartmentsPublicApiService {
    async getAll() {
        return ApiClient.get('/departments');
    }
}

export default new DepartmentsPublicApiService();
