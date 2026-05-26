import { useCallback, useEffect, useState } from 'react';
import { Button, Col, Form, Row } from 'react-bootstrap';
import EmployeeDirectoryApiService from '../api/EmployeeDirectoryApiService';
import DepartmentsPublicApiService from '../api/DepartmentsPublicApiService';
import PaginationComponent from '../pagination/Pagination';
import EmployeeDirectoryTable from './EmployeeDirectoryTable';
import { buildQueryString, useDirectoryFilters } from './DirectoryFilterHook';

const MyEmployees = () => {
    const filters = useDirectoryFilters();
    const [employees, setEmployees] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [departments, setDepartments] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [searchInput, setSearchInput] = useState(filters.search);
    const [positionInput, setPositionInput] = useState(filters.position);
    const [phoneInput, setPhoneInput] = useState(filters.phone);
    const [extMin, setExtMin] = useState(filters.extensionMin);
    const [extMax, setExtMax] = useState(filters.extensionMax);

    useEffect(() => {
        DepartmentsPublicApiService.getAll().then(setDepartments).catch(() => setDepartments([]));
    }, []);

    const load = useCallback(async () => {
        setIsLoading(true);
        try {
            const data = await EmployeeDirectoryApiService.getMy(buildQueryString(filters, false, true));
            setEmployees(data.items ?? []);
            setTotalPages(data.totalPages ?? 0);
        } finally {
            setIsLoading(false);
        }
    }, [filters.searchParams.toString()]);

    useEffect(() => {
        load();
    }, [load]);

    const handleSearch = () => {
        filters.applyFilter('search', searchInput);
        filters.applyFilter('position', positionInput);
        filters.applyFilter('phone', phoneInput);
        filters.applyFilter('extensionMin', extMin);
        filters.applyFilter('extensionMax', extMax);
        filters.applyFilter('page', '0');
    };

    return (
        <div>
            <h2 className="mb-3">Мой отдел</h2>
            <Row className="g-2">
                <Col xs={12} md={6}>
                    <Form.Control placeholder="Поиск по ФИО" value={searchInput} onChange={(e) => setSearchInput(e.target.value)} data-testid="my-search-input" />
                </Col>
                <Col xs={12} md={6}>
                    <Form.Select value={filters.departmentId} onChange={(e) => filters.applyFilter('departmentId', e.target.value)} data-testid="my-dept-filter">
                        <option value="">Все отделы (в рамках доступа)</option>
                        {departments.map((d) => (
                            <option key={d.id} value={d.id}>{d.name}</option>
                        ))}
                    </Form.Select>
                </Col>
                <Col xs={12} md={4}>
                    <Form.Control placeholder="Должность" value={positionInput} onChange={(e) => setPositionInput(e.target.value)} />
                </Col>
                <Col xs={12} md={4}>
                    <Form.Control placeholder="Номер телефона" value={phoneInput} onChange={(e) => setPhoneInput(e.target.value)} data-testid="my-phone-filter" />
                </Col>
                <Col xs={6} md={2}>
                    <Form.Control type="number" placeholder="Доб. от" value={extMin} onChange={(e) => setExtMin(e.target.value)} />
                </Col>
                <Col xs={6} md={2}>
                    <Form.Control type="number" placeholder="Доб. до" value={extMax} onChange={(e) => setExtMax(e.target.value)} />
                </Col>
                <Col xs={12} className="d-flex gap-2 flex-wrap">
                    <Button variant="primary" onClick={handleSearch} data-testid="my-search-btn">Найти</Button>
                    <Button variant="outline-secondary" onClick={filters.resetFilters}>Сбросить</Button>
                </Col>
            </Row>
            <EmployeeDirectoryTable employees={employees} isLoading={isLoading} />
            <PaginationComponent currentPage={Number(filters.page)} totalPages={totalPages} onPageChange={(p) => filters.applyFilter('page', String(p))} />
        </div>
    );
};

export default MyEmployees;
