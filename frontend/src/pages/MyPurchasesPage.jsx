import { useEffect, useState } from 'react';
import { Badge, Button, Spinner, Table } from 'react-bootstrap';
import { Link, useSearchParams } from 'react-router-dom';
import PurchasesApiService from '../components/api/PurchasesApiService';
import PaginationComponent from '../components/pagination/Pagination';

const MyPurchasesPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [purchases, setPurchases] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const page = searchParams.get('page') || '0';

    useEffect(() => {
        const load = async () => {
            setIsLoading(true);
            try {
                const params = new URLSearchParams(searchParams);
                params.set('size', '10');
                if (!params.get('page')) params.set('page', '0');
                const data = await PurchasesApiService.getMy(`?${params.toString()}`);
                setPurchases(data.items ?? []);
                setTotalPages(data.totalPages ?? 0);
            } finally {
                setIsLoading(false);
            }
        };
        load();
    }, [searchParams]);

    return (
        <div>
            <h1 className="h3 mb-3">Мои покупки</h1>
            {isLoading ? (
                <div className="text-center py-5"><Spinner animation="border" /></div>
            ) : (
                <Table responsive striped hover>
                    <thead>
                        <tr>
                            <th>Товар</th>
                            <th>Категории</th>
                            <th>Цена</th>
                            <th>Дата покупки</th>
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {purchases.map((purchase) => (
                            <tr key={purchase.id}>
                                <td>
                                    <Link to={`/products/${purchase.productId}`}>{purchase.product?.name}</Link>
                                </td>
                                <td>
                                    {purchase.product?.categories?.map((category) => (
                                        <Badge key={category.id} bg="secondary" className="me-1">{category.name}</Badge>
                                    ))}
                                </td>
                                <td>{Number(purchase.product?.price ?? 0).toLocaleString('ru-RU')} ₽</td>
                                <td>{new Date(purchase.purchasedAt).toLocaleDateString('ru-RU')}</td>
                                <td>
                                    <Button as={Link} to={`/products/${purchase.productId}`} size="sm" variant="outline-primary">
                                        Открыть и оставить отзыв
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {!purchases.length && (
                            <tr><td colSpan="5" className="text-muted">Покупок пока нет</td></tr>
                        )}
                    </tbody>
                </Table>
            )}
            <PaginationComponent totalPages={totalPages} currentPage={page} handlePageChange={(nextPage) => {
                const params = new URLSearchParams(searchParams);
                params.set('page', String(nextPage));
                setSearchParams(params);
            }} />
        </div>
    );
};

export default MyPurchasesPage;
