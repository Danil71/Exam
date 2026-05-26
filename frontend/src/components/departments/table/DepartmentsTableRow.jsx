import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';

const DepartmentsTableRow = ({ index, department, onDelete, onEdit }) => {
    const handleClick = (e, action) => { e.preventDefault(); action(); };
    return (
        <tr data-testid={`row-${department.name}`}>
            <th scope="row">{index + 1}</th>
            <td>{department.name}</td>
            <td>{department.description}</td>
            <td><a href="#" onClick={(e) => handleClick(e, onEdit)} data-testid="edit-btn"><PencilFill /></a></td>
            <td><a href="#" onClick={(e) => handleClick(e, onDelete)} data-testid="delete-btn"><Trash3 /></a></td>
        </tr>
    );
};

DepartmentsTableRow.propTypes = {
    index: PropTypes.number,
    department: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
};

export default DepartmentsTableRow;
