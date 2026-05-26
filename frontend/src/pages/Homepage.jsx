import { Link } from 'react-router-dom';
import { Button, Card, Col, Row } from 'react-bootstrap';

const Homepage = () => (
    <div className="py-4">
        <h1>Телефонный справочник организации</h1>
        <p className="lead">
            Веб-приложение для просмотра сотрудников, отделов и телефонных номеров с поиском и фильтрацией на сервере.
        </p>
        <Row className="g-3 mt-2">
            <Col xs={12} md={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>Мой отдел</Card.Title>
                        <Card.Text>Сотрудники вашего отдела: поиск по ФИО, фильтр по отделу, должности, номеру и добавочному.</Card.Text>
                        <Button as={Link} to="/my-employees" variant="primary">Открыть</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col xs={12} md={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>Общий справочник</Card.Title>
                        <Card.Text>Все сотрудники организации: поиск, отдел, должность, телефон, ФИО сотрудника.</Card.Text>
                        <Button as={Link} to="/directory" variant="primary">Открыть</Button>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </div>
);

export default Homepage;
