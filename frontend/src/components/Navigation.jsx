import { observer } from 'mobx-react-lite';
import { useContext } from 'react';
import { Container, Nav, Navbar, NavDropdown, Offcanvas } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import StoreContext from './users/StoreContext.jsx';

const Navigation = observer(() => {
    const navigate = useNavigate();
    const { store } = useContext(StoreContext);

    const handleLogout = async () => {
        await store.logout();
        navigate('/login', { replace: true });
    };

    return (
        <header>
            <Navbar bg="dark" data-bs-theme="dark" expand="lg">
                <Container fluid>
                    <Navbar.Brand as={Link} to="/">Интернет-магазин</Navbar.Brand>
                    <Navbar.Toggle aria-controls="main-nav" />
                    <Navbar.Offcanvas id="main-nav" placement="end">
                        <Offcanvas.Header closeButton>
                            <Offcanvas.Title>Меню</Offcanvas.Title>
                        </Offcanvas.Header>
                        <Offcanvas.Body>
                            <Nav className="me-auto">
                                <Nav.Link as={Link} to="/products">Каталог</Nav.Link>
                                <Nav.Link as={Link} to="/my-purchases">Мои покупки</Nav.Link>
                                {store.isAdmin && (
                                    <NavDropdown title="Администрирование" data-testid="nav-management-dropdown">
                                        <NavDropdown.Item as={Link} to="/admin/products">Товары</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/admin/categories">Категории</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/admin/users">Пользователи</NavDropdown.Item>
                                    </NavDropdown>
                                )}
                            </Nav>
                            <Nav>
                                <NavDropdown title={store.user?.login || 'Аккаунт'} align="end">
                                    <NavDropdown.Item onClick={handleLogout} data-testid="logout-btn">
                                        Выйти
                                    </NavDropdown.Item>
                                </NavDropdown>
                            </Nav>
                        </Offcanvas.Body>
                    </Navbar.Offcanvas>
                </Container>
            </Navbar>
        </header>
    );
});

export default Navigation;
