import 'bootstrap/dist/css/bootstrap.min.css';
import ReactDOM from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import { StoreProvider } from './components/users/StoreContext.jsx';
import './index.css';
import AdminProductsPage from './pages/AdminProductsPage.jsx';
import CategoriesPage from './pages/CategoriesPage.jsx';
import ErrorPage from './pages/ErrorPage.jsx';
import Homepage from './pages/Homepage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import MyPurchasesPage from './pages/MyPurchasesPage.jsx';
import ProductDetailPage from './pages/ProductDetailPage.jsx';
import ProductsPage from './pages/ProductsPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import UsersPage from './pages/UsersPage.jsx';

const router = createBrowserRouter([
    { path: '/login', element: <LoginPage /> },
    { path: '/register', element: <RegisterPage /> },
    {
        path: '/',
        element: <App />,
        errorElement: <ErrorPage />,
        children: [
            {
                element: <ProtectedRoute />,
                children: [
                    { index: true, element: <Homepage /> },
                    { path: 'products', element: <ProductsPage /> },
                    { path: 'products/:id', element: <ProductDetailPage /> },
                    { path: 'my-purchases', element: <MyPurchasesPage /> },
                    { path: 'admin/products', element: <AdminProductsPage /> },
                    { path: 'admin/categories', element: <CategoriesPage /> },
                    { path: 'admin/users', element: <UsersPage /> },
                ],
            },
        ],
    },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <StoreProvider>
        <RouterProvider router={router} />
    </StoreProvider>
);
