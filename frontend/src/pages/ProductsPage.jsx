import { useEffect, useState } from 'react';
import { Badge, Button, Card, Col, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useSearchParams } from 'react-router-dom';
import CategoriesApiService from '../components/api/CategoriesApiService';
import ProductsApiService from '../components/api/ProductsApiService';
import PaginationComponent from '../components/pagination/Pagination';

const buildQuery = (filters) => {
    const params = new URLSearchParams();
    Object.entries(filters).forEach(([key, value]) => {
        if (value !== '' && value !== null && value !== undefined) params.set(key, value);
    });
    params.set('size', '9');
    return `?${params.toString()}`;
};

const ProductsPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [form, setForm] = useState({
        search: searchParams.get('search') || '',
        owner: searchParams.get('owner') || '',
        categoryId: searchParams.get('categoryId') || '',
        minRating: searchParams.get('minRating') || '',
    });

    const filters = {
        search: searchParams.get('search') || '',
        owner: searchParams.get('owner') || '',
        categoryId: searchParams.get('categoryId') || '',
        minRating: searchParams.get('minRating') || '',
        page: searchParams.get('page') || '0',
    };

    useEffect(() => {
        CategoriesApiService.getPublic().then(setCategories).catch(() => setCategories([]));
    }, []);

    useEffect(() => {
        const load = async () => {
            setIsLoading(true);
            try {
                const currentFilters = {
                    search: searchParams.get('search') || '',
                    owner: searchParams.get('owner') || '',
                    categoryId: searchParams.get('categoryId') || '',
                    minRating: searchParams.get('minRating') || '',
                    page: searchParams.get('page') || '0',
                };
                const data = await ProductsApiService.getPublic(buildQuery(currentFilters));
                setProducts(data.items ?? []);
                setTotalPages(data.totalPages ?? 0);
            } finally {
                setIsLoading(false);
            }
        };
        load();
    }, [searchParams]);

    const applyFilters = () => {
        const params = new URLSearchParams();
        Object.entries(form).forEach(([key, value]) => {
            if (value) params.set(key, value);
        });
        params.set('page', '0');
        setSearchParams(params);
    };

    const resetFilters = () => {
        setForm({ search: '', owner: '', categoryId: '', minRating: '' });
        setSearchParams(new URLSearchParams());
    };

    return (
        <div>
            <h1 className="h3 mb-3">Каталог товаров</h1>
            <Row className="g-2 align-items-end mb-3">
                <Col xs={12} lg={3}>
                    <Form.Label>Название</Form.Label>
                    <Form.Control value={form.search} onChange={(e) => setForm({ ...form, search: e.target.value })} placeholder="Поиск товара" />
                </Col>
                <Col xs={12} lg={3}>
                    <Form.Label>Продавец</Form.Label>
                    <Form.Control value={form.owner} onChange={(e) => setForm({ ...form, owner: e.target.value })} placeholder="Логин продавца" />
                </Col>
                <Col xs={12} md={6} lg={3}>
                    <Form.Label>Категория</Form.Label>
                    <Form.Select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })}>
                        <option value="">Все категории</option>
                        {categories.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}
                    </Form.Select>
                </Col>
                <Col xs={12} md={6} lg={3}>
                    <Form.Label>Средняя оценка от</Form.Label>
                    <Form.Select value={form.minRating} onChange={(e) => setForm({ ...form, minRating: e.target.value })}>
                        <option value="">Любая</option>
                        {[1, 2, 3, 4, 5].map((rating) => <option key={rating} value={rating}>{rating}</option>)}
                    </Form.Select>
                </Col>
                <Col xs={12} className="d-flex gap-2 flex-wrap">
                    <Button onClick={applyFilters}>Найти</Button>
                    <Button variant="outline-secondary" onClick={resetFilters}>Сбросить</Button>
                </Col>
            </Row>
            {isLoading ? (
                <div className="text-center py-5"><Spinner animation="border" /></div>
            ) : (
                <Row className="g-3">
                    {products.map((product) => (
                        <Col key={product.id} xs={12} md={6} xl={4}>
                            <Card className="h-100">
                                <Card.Body className="d-flex flex-column">
                                    <div className="d-flex justify-content-between gap-2">
                                        <Card.Title className="h5">{product.name}</Card.Title>
                                        <Badge bg="warning" text="dark">{product.averageRating || 0}/5</Badge>
                                    </div>
                                    <Card.Text className="text-muted flex-grow-1">{product.description}</Card.Text>
                                    <div className="mb-2">
                                        {product.categories?.map((category) => (
                                            <Badge bg="secondary" className="me-1" key={category.id}>{category.name}</Badge>
                                        ))}
                                    </div>
                                    <div className="d-flex justify-content-between align-items-center">
                                        <strong>{Number(product.price).toLocaleString('ru-RU')} ₽</strong>
                                        <Button as={Link} to={`/products/${product.id}`} variant="primary">Открыть</Button>
                                    </div>
                                    <small className="text-muted mt-2">Продавец: {product.ownerLogin}, отзывов: {product.reviewCount}</small>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                    {!products.length && <Col><p className="text-muted">Товары не найдены</p></Col>}
                </Row>
            )}
            <PaginationComponent totalPages={totalPages} currentPage={filters.page} handlePageChange={(page) => {
                const params = new URLSearchParams(searchParams);
                params.set('page', String(page));
                setSearchParams(params);
            }} />
        </div>
    );
};

export default ProductsPage;
