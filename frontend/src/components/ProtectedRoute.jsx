import { observer } from 'mobx-react-lite';
import { useContext, useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import StoreContext from './users/StoreContext';

const ProtectedRoute = observer(() => {
    const { store } = useContext(StoreContext);

    useEffect(() => {
        if (localStorage.getItem('token') && !store.isAuth) {
            store.checkAuth();
        }
    }, [store]);

    if (store.isLoading) {
        return <div className="text-center py-5">Загрузка…</div>;
    }

    if (!store.isAuth) {
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
});

export default ProtectedRoute;
