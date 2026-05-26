import { useCallback, useContext, useEffect, useState } from 'react';
import { Badge, Button, Card, Col, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useNavigate, useParams } from 'react-router-dom';
import toast from 'react-hot-toast';
import ProductsApiService from '../components/api/ProductsApiService';
import PurchasesApiService from '../components/api/PurchasesApiService';
import ReviewsApiService from '../components/api/ReviewsApiService';
import StoreContext from '../components/users/StoreContext';

const emptyReview = { title: '', text: '', ratingValue: 5 };

const ProductDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { store } = useContext(StoreContext);
    const currentLogin = store.user?.login;
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [myReview, setMyReview] = useState(null);
    const [isPurchased, setIsPurchased] = useState(false);
    const [form, setForm] = useState(emptyReview);
    const [isLoading, setIsLoading] = useState(false);
    const [isSaving, setIsSaving] = useState(false);
    const [isBuying, setIsBuying] = useState(false);
    const [validated, setValidated] = useState(false);

    const load = useCallback(async () => {
        setIsLoading(true);
        try {
            const [productData, reviewsData, purchaseData] = await Promise.all([
                ProductsApiService.getProduct(id),
                ProductsApiService.getReviews(id, '?page=0&size=20'),
                PurchasesApiService.isPurchased(id),
            ]);
            const items = reviewsData.items ?? [];
            const own = items.find((review) => review.authorLogin === currentLogin) || null;
            setProduct(productData);
            setReviews(items);
            setMyReview(own);
            setIsPurchased(Boolean(purchaseData.purchased));
            setForm(own ? { title: own.title, text: own.text, ratingValue: own.ratingValue } : emptyReview);
        } finally {
            setIsLoading(false);
        }
    }, [id, currentLogin]);

    useEffect(() => {
        load();
    }, [load]);

    const submitReview = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (!event.currentTarget.checkValidity()) {
            setValidated(true);
            return;
        }
        setIsSaving(true);
        try {
            const payload = { ...form, productId: Number(id), ratingValue: Number(form.ratingValue) };
            if (myReview) {
                await ReviewsApiService.update(myReview.id, payload);
                toast.success('Отзыв обновлён');
            } else {
                await ReviewsApiService.create(payload);
                toast.success('Отзыв добавлен');
            }
            setValidated(false);
            await load();
        } finally {
            setIsSaving(false);
        }
    };

    const deleteReview = async () => {
        if (!myReview) return;
        await ReviewsApiService.delete(myReview.id);
        toast.success('Отзыв удалён');
        await load();
    };

    const buyProduct = async () => {
        setIsBuying(true);
        try {
            await PurchasesApiService.buy(Number(id));
            toast.success('Товар добавлен в мои покупки');
            navigate('/my-purchases');
        } finally {
            setIsBuying(false);
        }
    };

    if (isLoading && !product) {
        return <div className="text-center py-5"><Spinner animation="border" /></div>;
    }

    if (!product) {
        return <p className="text-muted">Товар не найден</p>;
    }

    return (
        <div>
            <Button as={Link} to="/products" variant="link" className="px-0 mb-2">Назад к каталогу</Button>
            <Row className="g-4">
                <Col xs={12} lg={7}>
                    <h1 className="h3">{product.name}</h1>
                    <div className="d-flex gap-2 flex-wrap mb-3">
                        <Badge bg="warning" text="dark">Средняя оценка {product.averageRating || 0}/5</Badge>
                        <Badge bg="secondary">Отзывы: {product.reviewCount}</Badge>
                        <Badge bg={product.stock > 0 ? 'success' : 'danger'}>Остаток: {product.stock}</Badge>
                    </div>
                    <p>{product.description}</p>
                    <p className="h4">{Number(product.price).toLocaleString('ru-RU')} ₽</p>
                    <p className="text-muted">Продавец: {product.ownerLogin}</p>
                    <div>
                        {product.categories?.map((category) => (
                            <Badge bg="secondary" className="me-1" key={category.id}>{category.name}</Badge>
                        ))}
                    </div>
                    {!isPurchased && (
                        <Button className="mt-3" onClick={buyProduct} disabled={isBuying || product.stock <= 0}>
                            {isBuying ? 'Покупка...' : 'Купить'}
                        </Button>
                    )}
                </Col>
                <Col xs={12} lg={5}>
                    <Card>
                        <Card.Body>
                            {isPurchased ? (
                                <>
                                    <Card.Title>{myReview ? 'Ваш отзыв' : 'Оставить отзыв'}</Card.Title>
                                    <Form noValidate validated={validated} onSubmit={submitReview}>
                                        <Form.Group className="mb-2">
                                            <Form.Label>Оценка</Form.Label>
                                            <Form.Select required value={form.ratingValue} onChange={(e) => setForm({ ...form, ratingValue: e.target.value })}>
                                                {[5, 4, 3, 2, 1].map((rating) => <option key={rating} value={rating}>{rating}</option>)}
                                            </Form.Select>
                                        </Form.Group>
                                        <Form.Group className="mb-2">
                                            <Form.Label>Заголовок</Form.Label>
                                            <Form.Control required minLength={2} maxLength={100} value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
                                        </Form.Group>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Текст</Form.Label>
                                            <Form.Control as="textarea" required minLength={5} maxLength={1000} rows={4} value={form.text} onChange={(e) => setForm({ ...form, text: e.target.value })} />
                                        </Form.Group>
                                        <div className="d-flex gap-2">
                                            <Button type="submit" disabled={isSaving}>{isSaving ? 'Сохранение...' : 'Сохранить'}</Button>
                                            {myReview && <Button type="button" variant="outline-danger" onClick={deleteReview}>Удалить</Button>}
                                        </div>
                                    </Form>
                                </>
                            ) : (
                                <>
                                    <Card.Title>Отзывы доступны после покупки</Card.Title>
                                    <Card.Text className="text-muted">
                                        Нажмите «Купить», товар появится в разделе «Мои покупки», после этого можно будет оставить отзыв.
                                    </Card.Text>
                                </>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <h2 className="h4 mt-4">Отзывы</h2>
            <Row className="g-3">
                {reviews.map((review) => (
                    <Col xs={12} md={6} key={review.id}>
                        <Card>
                            <Card.Body>
                                <div className="d-flex justify-content-between gap-2">
                                    <Card.Title className="h6">{review.title}</Card.Title>
                                    <Badge bg="warning" text="dark">{review.ratingValue}/5</Badge>
                                </div>
                                <Card.Text>{review.text}</Card.Text>
                                <small className="text-muted">{review.authorLogin} · {new Date(review.createdAt).toLocaleDateString('ru-RU')}</small>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
                {!reviews.length && <Col><p className="text-muted">Отзывов пока нет</p></Col>}
            </Row>
        </div>
    );
};

export default ProductDetailPage;
