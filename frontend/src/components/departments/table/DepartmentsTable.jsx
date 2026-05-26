import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const DepartmentsTable = ({ children }) => (
    <Table className="mt-2" striped responsive hover>
        <thead>
            <tr>
                <th>#</th>
                <th>Название</th>
                <th>Описание</th>
                <th />
                <th />
            </tr>
        </thead>
        <tbody>{children}</tbody>
    </Table>
);

DepartmentsTable.propTypes = { children: PropTypes.node };

export default DepartmentsTable;
