import { useSearchParams } from 'react-router-dom';

const usePagination = () => {
    const filterName = 'page';

    const [searchParams, setSearchParams] = useSearchParams();

    const handlePageChange = (page) => {
        if (page) {
            searchParams.set(filterName, page);
        } else {
            searchParams.delete(filterName);
        }
        setSearchParams(searchParams);
    };

    return {
        currentPage: searchParams.get(filterName) || '',
        handlePageChange,
    };
};

export default usePagination;
