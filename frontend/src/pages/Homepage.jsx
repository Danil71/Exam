import { Link } from 'react-router-dom';
import { Button, Card, Col, Row } from 'react-bootstrap';

const Homepage = () => (
    <div className="py-4">
        <h1>Интернет-магазин</h1>
        <p className="lead">
            Каталог товаров с отзывами, оценками и серверной фильтрацией.
        </p>
        <Row className="g-3 mt-2">
            <Col xs={12} md={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>Каталог товаров</Card.Title>
                        <Card.Text>Поиск по названию, продавцу, категории и средней оценке.</Card.Text>
                        <Button as={Link} to="/products" variant="primary">Открыть</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col xs={12} md={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>Мои покупки</Card.Title>
                        <Card.Text>Купленные товары, по которым можно перейти к деталям и оставить отзыв.</Card.Text>
                        <Button as={Link} to="/my-purchases" variant="primary">Открыть</Button>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </div>
);

export default Homepage;
