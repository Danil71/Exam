import { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import PhoneNumbersApiService from '../../api/PhoneNumbersApiService';
import ModalConfirm from '../../modal/ModalConfirm';
import ModalForm from '../../modal/ModalForm';
import PhoneNumbersForm from '../form/PhoneNumbersForm';

const emptyPhone = { number: '', type: 'WORK', extension: 0 };

const PhoneNumbers = () => {
    const [phones, setPhones] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const [showDelete, setShowDelete] = useState(false);
    const [current, setCurrent] = useState(emptyPhone);
    const [validated, setValidated] = useState(false);

    const load = async () => {
        setLoading(true);
        try {
            setPhones(await PhoneNumbersApiService.getAll());
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { load(); }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCurrent((prev) => ({ ...prev, [name]: name === 'extension' ? Number(value) : value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!e.currentTarget.checkValidity()) {
            setValidated(true);
            return;
        }
        if (current.id) {
            await PhoneNumbersApiService.update(current.id, current);
        } else {
            await PhoneNumbersApiService.create(current);
        }
        setShowForm(false);
        setCurrent(emptyPhone);
        load();
    };

    const handleDelete = async () => {
        await PhoneNumbersApiService.delete(current.id);
        setShowDelete(false);
        load();
    };

    return (
        <div className="table-responsive">
            <h2>Телефонные номера</h2>
            <Button className="mb-3" onClick={() => { setCurrent(emptyPhone); setShowForm(true); }}>Добавить</Button>
            {loading ? <p>Загрузка…</p> : (
                <Table responsive striped>
                    <thead><tr><th>Номер</th><th>Тип</th><th>Добавочный</th><th></th></tr></thead>
                    <tbody>
                        {phones.map((p) => (
                            <tr key={p.id}>
                                <td>{p.number}</td>
                                <td>{p.type}</td>
                                <td>{p.extension}</td>
                                <td>
                                    <Button size="sm" variant="warning" className="me-1" onClick={() => { setCurrent(p); setShowForm(true); }}>Изменить</Button>
                                    <Button size="sm" variant="danger" onClick={() => { setCurrent(p); setShowDelete(true); }}>Удалить</Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
            <ModalForm show={showForm} onClose={() => setShowForm(false)} onSubmit={handleSubmit} title={current.id ? 'Редактирование' : 'Новый номер'} validated={validated}>
                <PhoneNumbersForm phone={current} handleChange={handleChange} />
            </ModalForm>
            <ModalConfirm show={showDelete} onHide={() => setShowDelete(false)} onConfirm={handleDelete} message="Удалить номер?" />
        </div>
    );
};

export default PhoneNumbers;
