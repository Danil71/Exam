import CrudApiService from './CrudApiService';

class PhoneNumbersApiService extends CrudApiService {
    constructor() {
        super('admin/phone-number');
    }
}

export default new PhoneNumbersApiService();
