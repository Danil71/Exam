import { useEffect, useState } from 'react';
import { Card, Spinner } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import EmployeeDirectoryApiService from '../components/api/EmployeeDirectoryApiService';

const EmployeeDetailPage = () => {
    const { id } = useParams();
    const [employee, setEmployee] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        EmployeeDirectoryApiService.getById(id)
            .then(setEmployee)
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) return <Spinner animation="border" />;
    if (!employee) return <p>Сотрудник не найден</p>;

    return (
        <Card>
            <Card.Body>
                <Card.Title>{employee.name}</Card.Title>
                <Card.Text><strong>Должность:</strong> {employee.position}</Card.Text>
                <Card.Text><strong>Отдел:</strong> {employee.departmentName}</Card.Text>
                <Card.Text><strong>Телефоны:</strong></Card.Text>
                <ul>
                    {employee.phoneNumbers?.map((p) => (
                        <li key={p.id}>{p.number} — {p.type}{p.extension ? `, доб. ${p.extension}` : ''}</li>
                    ))}
                </ul>
            </Card.Body>
        </Card>
    );
};

export default EmployeeDetailPage;
