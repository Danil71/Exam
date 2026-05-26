import { ApiClient } from './ApiClient';

class EmployeeDirectoryApiService {
    async getMy(expand = '') {
        return ApiClient.get(`/employees/my${expand}`);
    }

    async getPublic(expand = '') {
        return ApiClient.get(`/employees/public${expand}`);
    }

    async getById(id) {
        return ApiClient.get(`/employees/${id}`);
    }
}

export default new EmployeeDirectoryApiService();
