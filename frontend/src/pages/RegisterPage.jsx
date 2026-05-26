import { observer } from 'mobx-react-lite';
import { useContext, useState } from 'react';
import { Button, Card, Container, Form } from 'react-bootstrap';
import { Link, Navigate } from 'react-router-dom';
import { LOGIN_PATTERN_STRING, PASSWORD_PATTERN_STRING } from '../components/utils/Constants';
import StoreContext from '../components/users/StoreContext';

const RegisterPage = observer(() => {
    const { store } = useContext(StoreContext);
    const [login, setLogin] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirm, setConfirm] = useState('');
    const [validated, setValidated] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    if (store.isAuth) {
        return <Navigate to="/" replace />;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;
        if (password !== confirm) {
            setValidated(true);
            return;
        }
        if (!form.checkValidity()) {
            e.stopPropagation();
            setValidated(true);
            return;
        }
        setSubmitting(true);
        await store.register(login, email, password);
        setSubmitting(false);
    };

    return (
        <Container className="py-5" style={{ maxWidth: 420 }}>
            <Card>
                <Card.Body>
                    <Card.Title className="text-center mb-4">Регистрация</Card.Title>
                    <Form noValidate validated={validated} onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Логин</Form.Label>
                            <Form.Control value={login} onChange={(e) => setLogin(e.target.value)} required pattern={LOGIN_PATTERN_STRING} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Пароль</Form.Label>
                            <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} required minLength={6} pattern={PASSWORD_PATTERN_STRING} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Подтверждение пароля</Form.Label>
                            <Form.Control
                                type="password"
                                value={confirm}
                                onChange={(e) => setConfirm(e.target.value)}
                                required
                                isInvalid={validated && password !== confirm}
                            />
                            <Form.Control.Feedback type="invalid">Пароли не совпадают</Form.Control.Feedback>
                        </Form.Group>
                        <Button type="submit" variant="primary" className="w-100" disabled={submitting}>
                            {submitting ? 'Регистрация…' : 'Зарегистрироваться'}
                        </Button>
                    </Form>
                    <p className="text-center mt-3 mb-0">
                        Уже есть аккаунт? <Link to="/login">Войти</Link>
                    </p>
                </Card.Body>
            </Card>
        </Container>
    );
});

export default RegisterPage;
