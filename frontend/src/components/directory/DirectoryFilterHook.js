import { useSearchParams } from 'react-router-dom';

const setParam = (searchParams, setSearchParams, name, value) => {
    if (value !== null && value !== undefined && value !== '') {
        searchParams.set(name, value);
    } else {
        searchParams.delete(name);
    }
    setSearchParams(searchParams);
};

export const useDirectoryFilters = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const applyFilter = (name, value) => setParam(searchParams, setSearchParams, name, value);

    const resetFilters = () => setSearchParams(new URLSearchParams());

    return {
        search: searchParams.get('search') || '',
        departmentId: searchParams.get('departmentId') || '',
        position: searchParams.get('position') || '',
        phone: searchParams.get('phone') || '',
        owner: searchParams.get('owner') || '',
        extensionMin: searchParams.get('extensionMin') || '',
        extensionMax: searchParams.get('extensionMax') || '',
        page: searchParams.get('page') || '0',
        applyFilter,
        resetFilters,
        searchParams,
    };
};

export const buildQueryString = (filters, includeOwner = false, includeExtension = false) => {
    const params = new URLSearchParams();
    if (filters.search) params.set('search', filters.search);
    if (filters.departmentId) params.set('departmentId', filters.departmentId);
    if (filters.position) params.set('position', filters.position);
    if (filters.phone) params.set('phone', filters.phone);
    if (includeOwner && filters.owner) params.set('owner', filters.owner);
    if (includeExtension && filters.extensionMin) params.set('extensionMin', filters.extensionMin);
    if (includeExtension && filters.extensionMax) params.set('extensionMax', filters.extensionMax);
    params.set('page', filters.page || '0');
    params.set('size', '10');
    const qs = params.toString();
    return qs ? `?${qs}` : '?page=0&size=10';
};
