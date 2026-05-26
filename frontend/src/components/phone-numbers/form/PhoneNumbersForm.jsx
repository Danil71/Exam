import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import Input from '../../input/Input';
import { PHONE_PATTERN_STRING } from '../../utils/Constants';

const PhoneNumbersForm = ({ phone, handleChange }) => (
    <>
        <Input name="number" label="Номер" value={phone.number} onChange={handleChange} required pattern={PHONE_PATTERN_STRING} />
        <Form.Group className="mb-3">
            <Form.Label>Тип</Form.Label>
            <Form.Select name="type" value={phone.type} onChange={handleChange} required>
                <option value="WORK">Рабочий</option>
                <option value="MOBILE">Мобильный</option>
                <option value="FAX">Факс</option>
            </Form.Select>
        </Form.Group>
        <Input name="extension" label="Добавочный" type="number" value={phone.extension} onChange={handleChange} required min={0} />
    </>
);

PhoneNumbersForm.propTypes = {
    phone: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
};

export default PhoneNumbersForm;
