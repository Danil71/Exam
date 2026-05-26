import { ApiClient } from './ApiClient';

class PurchasesApiService {
    async getMy(query = '') {
        return ApiClient.get(`/purchases/my${query}`);
    }

    async isPurchased(productId) {
        return ApiClient.get(`/purchases/my/products/${productId}`);
    }

    async buy(productId) {
        return ApiClient.post('/purchases', { productId });
    }
}

export default new PurchasesApiService();
