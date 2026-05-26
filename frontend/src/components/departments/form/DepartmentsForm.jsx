import PropTypes from 'prop-types';
import Input from '../../input/Input';
import TextArea from '../../input/TextArea';

const DepartmentsForm = ({ department, handleChange }) => (
    <>
        <Input name="name" label="Название" value={department.name || ''} onChange={handleChange} type="text" required data-testid="dept-name" />
        <TextArea name="description" label="Описание" value={department.description || ''} onChange={handleChange} required data-testid="dept-description" />
    </>
);

DepartmentsForm.propTypes = {
    department: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
};

export default DepartmentsForm;
