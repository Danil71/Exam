import { useCallback, useEffect, useState } from 'react';
import { Button, Form, Modal, Spinner, Table } from 'react-bootstrap';
import toast from 'react-hot-toast';
import CategoriesApiService from '../components/api/CategoriesApiService';
import PaginationComponent from '../components/pagination/Pagination';

const emptyCategory = { name: '', description: '' };

const CategoriesPage = () => {
    const [categories, setCategories] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [validated, setValidated] = useState(false);
    const [current, setCurrent] = useState(emptyCategory);

    const load = useCallback(async () => {
        setIsLoading(true);
        try {
            const data = await CategoriesApiService.getAll(`?page=${page}&size=10`);
            setCategories(data.items ?? []);
            setTotalPages(data.totalPages ?? 0);
        } finally {
            setIsLoading(false);
        }
    }, [page]);

    useEffect(() => {
        load();
    }, [load]);

    const openModal = (category = null) => {
        setCurrent(category ?? emptyCategory);
        setValidated(false);
        setShowModal(true);
    };

    const submit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (!event.currentTarget.checkValidity()) {
            setValidated(true);
            return;
        }
        if (current.id) {
            await CategoriesApiService.update(current.id, current);
            toast.success('Категория обновлена');
        } else {
            await CategoriesApiService.create(current);
            toast.success('Категория создана');
        }
        setShowModal(false);
        await load();
    };

    const remove = async (id) => {
        await CategoriesApiService.delete(id);
        toast.success('Категория удалена');
        await load();
    };

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center gap-2 mb-3">
                <h1 className="h3 mb-0">Категории</h1>
                <Button onClick={() => openModal()}>Добавить категорию</Button>
            </div>
            {isLoading ? (
                <div className="text-center py-5"><Spinner animation="border" /></div>
            ) : (
                <Table responsive striped hover>
                    <thead>
                        <tr>
                            <th>Название</th>
                            <th>Описание</th>
                            <th />
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map((category) => (
                            <tr key={category.id}>
                                <td>{category.name}</td>
                                <td>{category.description}</td>
                                <td><Button size="sm" variant="outline-primary" onClick={() => openModal(category)}>Изменить</Button></td>
                                <td><Button size="sm" variant="outline-danger" onClick={() => remove(category.id)}>Удалить</Button></td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
            <PaginationComponent totalPages={totalPages} currentPage={page} handlePageChange={setPage} />
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Form noValidate validated={validated} onSubmit={submit}>
                    <Modal.Header closeButton>
                        <Modal.Title>Категория</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Название</Form.Label>
                            <Form.Control required maxLength={60} value={current.name} onChange={(e) => setCurrent({ ...current, name: e.target.value })} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Описание</Form.Label>
                            <Form.Control required as="textarea" rows={3} maxLength={300} value={current.description} onChange={(e) => setCurrent({ ...current, description: e.target.value })} />
                        </Form.Group>
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

export default CategoriesPage;
