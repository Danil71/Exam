import CrudApiService from './CrudApiService';
import { ApiClient } from './ApiClient';

class CategoriesApiService extends CrudApiService {
    constructor() {
        super('/admin/categories');
    }

    async getPublic() {
        return ApiClient.get('/categories');
    }
}

export default new CategoriesApiService();
