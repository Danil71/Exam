import CrudApiService from './CrudApiService';
import { ApiClient } from './ApiClient';

class ProductsApiService extends CrudApiService {
    constructor() {
        super('/admin/products');
    }

    async getPublic(query = '') {
        return ApiClient.get(`/products/public${query}`);
    }

    async getProduct(id) {
        return ApiClient.get(`/products/${id}`);
    }

    async getReviews(productId, query = '') {
        return ApiClient.get(`/products/${productId}/reviews${query}`);
    }
}

export default new ProductsApiService();
