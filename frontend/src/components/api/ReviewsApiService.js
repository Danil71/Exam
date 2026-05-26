import CrudApiService from './CrudApiService';
import { ApiClient } from './ApiClient';

class ReviewsApiService extends CrudApiService {
    constructor() {
        super('/reviews');
    }

    async getMy(query = '') {
        return ApiClient.get(`/reviews/my${query}`);
    }
}

export default new ReviewsApiService();
