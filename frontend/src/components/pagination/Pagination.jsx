import Pagination from 'react-bootstrap/Pagination';

const PaginationComponent = ({ totalPages, currentPage, handlePageChange }) => {
    const maxPage = 2;

    if(!currentPage) currentPage = 0;
    else currentPage = parseInt(currentPage);    

    const page = currentPage + 1;
    const seqFrom = Math.max(1, page - maxPage);
    const seqTo = Math.min(totalPages, page + maxPage);

    const goToPage = (pageNum) => {
        handlePageChange(pageNum);
    };


    const paginationItems = [];

    if (page > maxPage + 1) {
        paginationItems.push(
            <Pagination.First key="first" onClick={() => goToPage(0)} />,
            <Pagination.Ellipsis key="start-ellipsis" disabled />
        );
    }

    for (let i = seqFrom; i <= seqTo; i++) {
        paginationItems.push(
            <Pagination.Item
                key={i}
                active={i === page}
                onClick={() => goToPage(i - 1)}
            >
                {i}
            </Pagination.Item>
        );
    }

    if (page < totalPages - maxPage) {
        paginationItems.push(
            <Pagination.Ellipsis key="end-ellipsis" disabled />,
            <Pagination.Last key="last" onClick={() => goToPage(totalPages - 1)} />
        );
    }

    return (
        <Pagination className="justify-content-center">
            {paginationItems}
        </Pagination>
    );
};

export default PaginationComponent;