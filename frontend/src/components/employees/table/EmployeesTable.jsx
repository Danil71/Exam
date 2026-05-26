import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const EmployeesTable = ({ children }) => (
    <Table className="mt-2" striped responsive hover>
        <thead>
            <tr>
                <th>#</th>
                <th>ФИО</th>
                <th>Должность</th>
                <th>Отдел</th>
                <th />
                <th />
            </tr>
        </thead>
        <tbody>{children}</tbody>
    </Table>
);

EmployeesTable.propTypes = { children: PropTypes.node };

export default EmployeesTable;
