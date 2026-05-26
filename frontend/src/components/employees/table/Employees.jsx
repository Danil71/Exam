import { Button } from 'react-bootstrap';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import PaginationComponent from '../../pagination/Pagination.jsx';
import usePagination from '../../pagination/PaginationHook.js';
import EmployeesForm from '../form/EmployeesForm.jsx';
import useEmployeesDeleteModal from '../hooks/EmployeesDeleteModalHook.js';
import useEmployeesFormModal from '../hooks/EmployeesFormModalHook.js';
import useEmployees from '../hooks/EmployeesHook.js';
import EmployeesTable from './EmployeesTable.jsx';
import EmployeesTableRow from './EmployeesTableRow.jsx';

const Employees = () => {
    const { currentPage, handlePageChange } = usePagination();
    const { employees, handleEmployeesChange, totalPages } = useEmployees({ page: currentPage });
    const { isDeleteModalShow, showDeleteModal, handleDeleteConfirm, handleDeleteCancel } =
        useEmployeesDeleteModal(handleEmployeesChange);
    const {
        isFormModalShow, isFormValidated, showFormModal, currentEmployee,
        handleEmployeeChange, handlePhoneIdsChange, handleFormSubmit, handleFormClose,
    } = useEmployeesFormModal(handleEmployeesChange);

    return (
        <>
            <EmployeesTable>
                {employees.map((employee, index) => (
                    <EmployeesTableRow
                        key={employee.id}
                        index={index}
                        employee={employee}
                        onDelete={() => showDeleteModal(employee.id)}
                        onEdit={() => showFormModal(employee.id)}
                    />
                ))}
            </EmployeesTable>
            <div className="d-flex justify-content-center">
                <Button variant="primary" className="fw-bold px-5 mb-5" onClick={() => showFormModal()} data-testid="emp-create-btn">
                    Добавить сотрудника
                </Button>
            </div>
            <PaginationComponent totalPages={totalPages} currentPage={currentPage} handlePageChange={handlePageChange} />
            <ModalConfirm show={isDeleteModalShow} onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel} title="Удаление" message="Удалить сотрудника?" />
            <ModalForm show={isFormModalShow} validated={isFormValidated} onSubmit={handleFormSubmit} onClose={handleFormClose} title="Сотрудник" saveBtnTestId="emp-save-btn">
                <EmployeesForm employee={currentEmployee} handleChange={handleEmployeeChange} handlePhoneIdsChange={handlePhoneIdsChange} />
            </ModalForm>
        </>
    );
};

export default Employees;
