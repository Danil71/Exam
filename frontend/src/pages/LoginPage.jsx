import { observer } from 'mobx-react-lite';
import { useContext, useState } from 'react';
import { Button, Card, Container, Form } from 'react-bootstrap';
import { Link, Navigate } from 'react-router-dom';
import { LOGIN_PATTERN_STRING, PASSWORD_PATTERN_STRING } from '../components/utils/Constants';
import StoreContext from '../components/users/StoreContext';

const LoginPage = observer(() => {
    const { store } = useContext(StoreContext);
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [validated, setValidated] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const loginError = store.loginError;

    if (store.isAuth) {
        return <Navigate to="/" replace />;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;
        if (!form.checkValidity()) {
            e.stopPropagation();
            setValidated(true);
            return;
        }
        setSubmitting(true);
        await store.login(login, password);
        setSubmitting(false);
    };

    return (
        <Container className="py-5" style={{ maxWidth: 420 }}>
            <Card>
                <Card.Body>
                    <Card.Title className="text-center mb-4">Вход в магазин</Card.Title>
                    {loginError && (
                        <div className="alert alert-danger py-2" role="alert" data-testid="login-error">
                            {loginError}
                        </div>
                    )}
                    <Form noValidate validated={validated} onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Логин</Form.Label>
                            <Form.Control
                                type="text"
                                value={login}
                                onChange={(e) => setLogin(e.target.value)}
                                required
                                pattern={LOGIN_PATTERN_STRING}
                                data-testid="login-input"
                            />
                            <Form.Control.Feedback type="invalid">Логин 3–30 символов (латиница, цифры, _)</Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Пароль</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                minLength={6}
                                pattern={PASSWORD_PATTERN_STRING}
                                data-testid="password-input"
                            />
                            <Form.Control.Feedback type="invalid">Пароль не менее 6 символов</Form.Control.Feedback>
                        </Form.Group>
                        <Button type="submit" variant="primary" className="w-100" disabled={submitting} data-testid="login-btn">
                            {submitting ? 'Вход…' : 'Войти'}
                        </Button>
                    </Form>
                    <p className="text-center mt-3 mb-0">
                        Нет аккаунта? <Link to="/register">Регистрация</Link>
                    </p>
                </Card.Body>
            </Card>
        </Container>
    );
});

export default LoginPage;
