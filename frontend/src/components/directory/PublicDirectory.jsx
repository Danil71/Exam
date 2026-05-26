import { useCallback, useEffect, useState } from 'react';
import { Button, Col, Form, Row } from 'react-bootstrap';
import EmployeeDirectoryApiService from '../api/EmployeeDirectoryApiService';
import DepartmentsPublicApiService from '../api/DepartmentsPublicApiService';
import PaginationComponent from '../pagination/Pagination';
import EmployeeDirectoryTable from './EmployeeDirectoryTable';
import { buildQueryString, useDirectoryFilters } from './DirectoryFilterHook';

const PublicDirectory = () => {
    const filters = useDirectoryFilters();
    const [employees, setEmployees] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [departments, setDepartments] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [searchInput, setSearchInput] = useState(filters.search);
    const [positionInput, setPositionInput] = useState(filters.position);
    const [phoneInput, setPhoneInput] = useState(filters.phone);
    const [ownerInput, setOwnerInput] = useState(filters.owner);

    useEffect(() => {
        DepartmentsPublicApiService.getAll().then(setDepartments).catch(() => setDepartments([]));
    }, []);

    const load = useCallback(async () => {
        setIsLoading(true);
        try {
            const data = await EmployeeDirectoryApiService.getPublic(buildQueryString(filters, true, false));
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
        filters.applyFilter('owner', ownerInput);
        filters.applyFilter('page', '0');
    };

    return (
        <div>
            <h2 className="mb-3">Телефонный справочник</h2>
            <Row className="g-2">
                <Col xs={12} md={6}>
                    <Form.Control placeholder="Поиск по ФИО" value={searchInput} onChange={(e) => setSearchInput(e.target.value)} data-testid="pub-search-input" />
                </Col>
                <Col xs={12} md={6}>
                    <Form.Select value={filters.departmentId} onChange={(e) => filters.applyFilter('departmentId', e.target.value)}>
                        <option value="">Все отделы</option>
                        {departments.map((d) => (
                            <option key={d.id} value={d.id}>{d.name}</option>
                        ))}
                    </Form.Select>
                </Col>
                <Col xs={12} md={4}>
                    <Form.Control placeholder="Должность" value={positionInput} onChange={(e) => setPositionInput(e.target.value)} />
                </Col>
                <Col xs={12} md={4}>
                    <Form.Control placeholder="Номер телефона" value={phoneInput} onChange={(e) => setPhoneInput(e.target.value)} data-testid="pub-phone-filter" />
                </Col>
                <Col xs={12} md={4}>
                    <Form.Control placeholder="Сотрудник (ФИО)" value={ownerInput} onChange={(e) => setOwnerInput(e.target.value)} data-testid="pub-owner-filter" />
                </Col>
                <Col xs={12} className="d-flex gap-2">
                    <Button variant="primary" onClick={handleSearch} data-testid="pub-search-btn">Найти</Button>
                    <Button variant="outline-secondary" onClick={filters.resetFilters}>Сбросить</Button>
                </Col>
            </Row>
            <EmployeeDirectoryTable employees={employees} isLoading={isLoading} />
            <PaginationComponent currentPage={Number(filters.page)} totalPages={totalPages} onPageChange={(p) => filters.applyFilter('page', String(p))} />
        </div>
    );
};

export default PublicDirectory;
