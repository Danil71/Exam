import { useCallback, useEffect, useState } from 'react';
import { Button, Form, Modal, Spinner, Table } from 'react-bootstrap';
import toast from 'react-hot-toast';
import CategoriesApiService from '../components/api/CategoriesApiService';
import ProductsApiService from '../components/api/ProductsApiService';
import UsersApiService from '../components/users/service/UsersApiService';
import PaginationComponent from '../components/pagination/Pagination';

const emptyProduct = { name: '', description: '', price: '', stock: 0, ownerId: '', categoryIds: [] };

const AdminProductsPage = () => {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [validated, setValidated] = useState(false);
    const [current, setCurrent] = useState(emptyProduct);

    const load = useCallback(async () => {
        setIsLoading(true);
        try {
            const [productData, categoryData, userData] = await Promise.all([
                ProductsApiService.getAll(`?page=${page}&size=10`),
                CategoriesApiService.getPublic(),
                UsersApiService.getAll('?page=0&size=50'),
            ]);
            setProducts(productData.items ?? []);
            setTotalPages(productData.totalPages ?? 0);
            setCategories(categoryData ?? []);
            setUsers(userData.items ?? []);
        } finally {
            setIsLoading(false);
        }
    }, [page]);

    useEffect(() => {
        load();
    }, [load]);

    const openModal = (product = null) => {
        setCurrent(product ? {
            ...product,
            price: product.price,
            ownerId: product.ownerId,
            categoryIds: product.categoryIds ?? [],
        } : emptyProduct);
        setValidated(false);
        setShowModal(true);
    };

    const handleCategoryChange = (categoryId, checked) => {
        const id = Number(categoryId);
        setCurrent((prev) => ({
            ...prev,
            categoryIds: checked
                ? [...(prev.categoryIds ?? []), id]
                : (prev.categoryIds ?? []).filter((value) => value !== id),
        }));
    };

    const submit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (!event.currentTarget.checkValidity()) {
            setValidated(true);
            return;
        }
        const payload = {
            name: current.name,
            description: current.description,
            price: Number(current.price),
            stock: Number(current.stock),
            ownerId: Number(current.ownerId),
            categoryIds: current.categoryIds ?? [],
        };
        if (current.id) {
            await ProductsApiService.update(current.id, payload);
            toast.success('Товар обновлён');
        } else {
            await ProductsApiService.create(payload);
            toast.success('Товар создан');
        }
        setShowModal(false);
        await load();
    };

    const remove = async (id) => {
        await ProductsApiService.delete(id);
        toast.success('Товар удалён');
        await load();
    };

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center gap-2 mb-3">
                <h1 className="h3 mb-0">Товары</h1>
                <Button onClick={() => openModal()}>Добавить товар</Button>
            </div>
            {isLoading ? (
                <div className="text-center py-5"><Spinner animation="border" /></div>
            ) : (
                <Table responsive striped hover>
                    <thead>
                        <tr>
                            <th>Название</th>
                            <th>Цена</th>
                            <th>Остаток</th>
                            <th>Продавец</th>
                            <th>Оценка</th>
                            <th />
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {products.map((product) => (
                            <tr key={product.id}>
                                <td>{product.name}</td>
                                <td>{Number(product.price).toLocaleString('ru-RU')} ₽</td>
                                <td>{product.stock}</td>
                                <td>{product.ownerLogin}</td>
                                <td>{product.averageRating || 0}</td>
                                <td><Button size="sm" variant="outline-primary" onClick={() => openModal(product)}>Изменить</Button></td>
                                <td><Button size="sm" variant="outline-danger" onClick={() => remove(product.id)}>Удалить</Button></td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
            <PaginationComponent totalPages={totalPages} currentPage={page} handlePageChange={setPage} />
            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Form noValidate validated={validated} onSubmit={submit}>
                    <Modal.Header closeButton>
                        <Modal.Title>Товар</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="row g-3">
                            <div className="col-12 col-md-6">
                                <Form.Label>Название</Form.Label>
                                <Form.Control required maxLength={100} value={current.name} onChange={(e) => setCurrent({ ...current, name: e.target.value })} />
                            </div>
                            <div className="col-12 col-md-3">
                                <Form.Label>Цена</Form.Label>
                                <Form.Control required type="number" min="0.01" step="0.01" value={current.price} onChange={(e) => setCurrent({ ...current, price: e.target.value })} />
                            </div>
                            <div className="col-12 col-md-3">
                                <Form.Label>Остаток</Form.Label>
                                <Form.Control required type="number" min="0" value={current.stock} onChange={(e) => setCurrent({ ...current, stock: e.target.value })} />
                            </div>
                            <div className="col-12">
                                <Form.Label>Описание</Form.Label>
                                <Form.Control required as="textarea" rows={3} maxLength={1000} value={current.description} onChange={(e) => setCurrent({ ...current, description: e.target.value })} />
                            </div>
                            <div className="col-12 col-md-6">
                                <Form.Label>Продавец</Form.Label>
                                <Form.Select required value={current.ownerId} onChange={(e) => setCurrent({ ...current, ownerId: e.target.value })}>
                                    <option value="">Выберите пользователя</option>
                                    {users.map((user) => <option key={user.id} value={user.id}>{user.login}</option>)}
                                </Form.Select>
                            </div>
                            <div className="col-12 col-md-6">
                                <Form.Label>Категории</Form.Label>
                                <div className="border rounded p-2">
                                    {categories.map((category) => (
                                        <Form.Check
                                            key={category.id}
                                            label={category.name}
                                            checked={(current.categoryIds ?? []).includes(category.id)}
                                            onChange={(e) => handleCategoryChange(category.id, e.target.checked)}
                                        />
                                    ))}
                                </div>
                            </div>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="outline-secondary" onClick={() => setShowModal(false)}>Отмена</Button>
                        <Button type="submit">Сохранить</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </div>
    );
};

export default AdminProductsPage;
