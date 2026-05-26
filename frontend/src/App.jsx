import { Container } from 'react-bootstrap';
import { Toaster } from 'react-hot-toast';
import { Outlet } from 'react-router-dom';
import Footer from './components/Footer';
import Navigation from './components/Navigation';

const App = () => (
    <>
        <Navigation />
        <Container className="d-flex flex-column flex-grow-1 my-3" as="main" fluid="md">
            <Outlet />
        </Container>
        <Footer />
        <Toaster position="top-center" toastOptions={{ duration: 4000 }} />
    </>
);

export default App;
