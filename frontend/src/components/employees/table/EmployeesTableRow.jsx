import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';

const EmployeesTableRow = ({ index, employee, onDelete, onEdit }) => {
    const handleClick = (e, action) => { e.preventDefault(); action(); };
    return (
        <tr data-testid={`row-${employee.name}`}>
            <th scope="row">{index + 1}</th>
            <td>{employee.name}</td>
            <td>{employee.position}</td>
            <td>{employee.departmentName || '-'}</td>
            <td><a href="#" onClick={(e) => handleClick(e, onEdit)} data-testid="edit-btn"><PencilFill /></a></td>
            <td><a href="#" onClick={(e) => handleClick(e, onDelete)} data-testid="delete-btn"><Trash3 /></a></td>
        </tr>
    );
};

EmployeesTableRow.propTypes = {
    index: PropTypes.number,
    employee: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
};

export default EmployeesTableRow;
