import { ApiClient, FileApiClient } from './ApiClient';

class EmailApiService {
  constructor(url) {
    this.url = url;
  }

  async sendSimpleEmail(body) {
    return ApiClient.post(`${this.url}/email/simple`, body);
  }

  async sendEmailWithAttachments(emailRequest, attachments) {
    const formData = new FormData();
    formData.append('emailRequest', new Blob([JSON.stringify(emailRequest)], { type: 'application/json' }));

    attachments.forEach(file => {
      formData.append('attachments', file);
    });

    return FileApiClient.post(`${this.url}/email/attachments`, formData);
  }
}

export default EmailApiService;