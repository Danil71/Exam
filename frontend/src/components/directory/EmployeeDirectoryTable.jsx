import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const EmployeeDirectoryTable = ({ employees, isLoading }) => {
    if (isLoading) {
        return <div className="text-center py-4">Загрузка списка…</div>;
    }

    if (!employees?.length) {
        return <p className="text-muted">Сотрудники не найдены</p>;
    }

    return (
        <Table responsive striped hover className="mt-3">
            <thead>
                <tr>
                    <th>ФИО</th>
                    <th>Должность</th>
                    <th>Отдел</th>
                    <th>Телефоны</th>
                </tr>
            </thead>
            <tbody>
                {employees.map((emp) => (
                    <tr key={emp.id}>
                        <td>
                            <Link to={`/employees/${emp.id}`}>{emp.name}</Link>
                        </td>
                        <td>{emp.position}</td>
                        <td>{emp.departmentName}</td>
                        <td>
                            {emp.phoneNumbers?.map((p) => (
                                <div key={p.id}>
                                    {p.number} {p.extension ? `(доб. ${p.extension})` : ''} — {p.type}
                                </div>
                            ))}
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );
};

EmployeeDirectoryTable.propTypes = {
    employees: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
};

export default EmployeeDirectoryTable;
