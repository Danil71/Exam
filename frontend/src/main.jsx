import 'bootstrap/dist/css/bootstrap.min.css';
import ReactDOM from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import { StoreProvider } from './components/users/StoreContext.jsx';
import './index.css';
import DepartmentsPage from './pages/DepartmentsPage.jsx';
import DirectoryPage from './pages/DirectoryPage.jsx';
import EmployeeDetailPage from './pages/EmployeeDetailPage.jsx';
import EmployeesPage from './pages/EmployeesPage.jsx';
import ErrorPage from './pages/ErrorPage.jsx';
import Homepage from './pages/Homepage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import MyEmployeesPage from './pages/MyEmployeesPage.jsx';
import PhoneNumbersPage from './pages/PhoneNumbersPage.jsx';
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
                    { path: 'my-employees', element: <MyEmployeesPage /> },
                    { path: 'directory', element: <DirectoryPage /> },
                    { path: 'employees/:id', element: <EmployeeDetailPage /> },
                    { path: 'admin/department', element: <DepartmentsPage /> },
                    { path: 'admin/employee', element: <EmployeesPage /> },
                    { path: 'admin/user', element: <UsersPage /> },
                    { path: 'admin/phone-number', element: <PhoneNumbersPage /> },
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
