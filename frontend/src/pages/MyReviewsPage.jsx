import { useEffect, useState } from 'react';
import { Badge, Button, Form, Spinner, Table } from 'react-bootstrap';
import { Link, useSearchParams } from 'react-router-dom';
import toast from 'react-hot-toast';
import ReviewsApiService from '../components/api/ReviewsApiService';
import PaginationComponent from '../components/pagination/Pagination';

const MyReviewsPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [reviews, setReviews] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [form, setForm] = useState({
        search: searchParams.get('search') || '',
        rating: searchParams.get('rating') || '',
        dateFrom: searchParams.get('dateFrom') || '',
        dateTo: searchParams.get('dateTo') || '',
    });

    const page = searchParams.get('page') || '0';

    useEffect(() => {
        const load = async () => {
            setIsLoading(true);
            try {
                const params = new URLSearchParams(searchParams);
                params.set('size', '10');
                if (!params.get('page')) params.set('page', '0');
                const data = await ReviewsApiService.getMy(`?${params.toString()}`);
                setReviews(data.items ?? []);
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
        setForm({ search: '', rating: '', dateFrom: '', dateTo: '' });
        setSearchParams(new URLSearchParams());
    };

    const deleteReview = async (id) => {
        await ReviewsApiService.delete(id);
        toast.success('Отзыв удалён');
        const data = await ReviewsApiService.getMy(`?${searchParams.toString()}`);
        setReviews(data.items ?? []);
        setTotalPages(data.totalPages ?? 0);
    };

    return (
        <div>
            <h1 className="h3 mb-3">Мои отзывы</h1>
            <div className="row g-2 align-items-end mb-3">
                <div className="col-12 col-lg-3">
                    <Form.Label>Поиск</Form.Label>
                    <Form.Control value={form.search} onChange={(e) => setForm({ ...form, search: e.target.value })} placeholder="Товар, заголовок, текст" />
                </div>
                <div className="col-12 col-md-4 col-lg-3">
                    <Form.Label>Оценка</Form.Label>
                    <Form.Select value={form.rating} onChange={(e) => setForm({ ...form, rating: e.target.value })}>
                        <option value="">Любая</option>
                        {[1, 2, 3, 4, 5].map((rating) => <option key={rating} value={rating}>{rating}</option>)}
                    </Form.Select>
                </div>
                <div className="col-6 col-md-4 col-lg-3">
                    <Form.Label>Дата от</Form.Label>
                    <Form.Control type="date" value={form.dateFrom} onChange={(e) => setForm({ ...form, dateFrom: e.target.value })} />
                </div>
                <div className="col-6 col-md-4 col-lg-3">
                    <Form.Label>Дата до</Form.Label>
                    <Form.Control type="date" value={form.dateTo} onChange={(e) => setForm({ ...form, dateTo: e.target.value })} />
                </div>
                <div className="col-12 d-flex gap-2 flex-wrap">
                    <Button onClick={applyFilters}>Найти</Button>
                    <Button variant="outline-secondary" onClick={resetFilters}>Сбросить</Button>
                </div>
            </div>
            {isLoading ? (
                <div className="text-center py-5"><Spinner animation="border" /></div>
            ) : (
                <Table responsive striped hover>
                    <thead>
                        <tr>
                            <th>Товар</th>
                            <th>Заголовок</th>
                            <th>Оценка</th>
                            <th>Дата</th>
                            <th />
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {reviews.map((review) => (
                            <tr key={review.id}>
                                <td><Link to={`/products/${review.productId}`}>{review.productName}</Link></td>
                                <td>{review.title}</td>
                                <td><Badge bg="warning" text="dark">{review.ratingValue}/5</Badge></td>
                                <td>{new Date(review.createdAt).toLocaleDateString('ru-RU')}</td>
                                <td><Button as={Link} to={`/products/${review.productId}`} size="sm" variant="outline-primary">Изменить</Button></td>
                                <td><Button size="sm" variant="outline-danger" onClick={() => deleteReview(review.id)}>Удалить</Button></td>
                            </tr>
                        ))}
                        {!reviews.length && (
                            <tr><td colSpan="6" className="text-muted">Отзывы не найдены</td></tr>
                        )}
                    </tbody>
                </Table>
            )}
            <PaginationComponent totalPages={totalPages} currentPage={page} handlePageChange={(nextPage) => {
                const params = new URLSearchParams(searchParams);
                params.set('page', String(nextPage));
                setSearchParams(params);
            }} />
        </div>
    );
};

export default MyReviewsPage;
