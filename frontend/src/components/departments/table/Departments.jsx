import { Button } from 'react-bootstrap';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import DepartmentsForm from '../form/DepartmentsForm.jsx';
import useDepartmentsDeleteModal from '../hooks/DepartmentsDeleteModalHook.js';
import useDepartmentsFormModal from '../hooks/DepartmentsFormModalHook.js';
import useDepartments from '../hooks/DepartmentsHook.js';
import DepartmentsTable from './DepartmentsTable.jsx';
import DepartmentsTableRow from './DepartmentsTableRow.jsx';

const Departments = () => {
    const { departments, handleDepartmentsChange } = useDepartments();
    const { isDeleteModalShow, showDeleteModal, handleDeleteConfirm, handleDeleteCancel } =
        useDepartmentsDeleteModal(handleDepartmentsChange);
    const {
        isFormModalShow, isFormValidated, showFormModal, currentDepartment,
        handleDepartmentChange, handleFormSubmit, handleFormClose,
    } = useDepartmentsFormModal(handleDepartmentsChange);

    return (
        <>
            <DepartmentsTable>
                {departments.map((dept, index) => (
                    <DepartmentsTableRow
                        key={dept.id}
                        index={index}
                        department={dept}
                        onDelete={() => showDeleteModal(dept.id)}
                        onEdit={() => showFormModal(dept.id)}
                    />
                ))}
            </DepartmentsTable>
            <div className="d-flex justify-content-center">
                <Button variant="primary" className="fw-bold px-5 mb-5" onClick={() => showFormModal()} data-testid="dept-create-btn">
                    Добавить отдел
                </Button>
            </div>
            <ModalConfirm show={isDeleteModalShow} onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel} title="Удаление" message="Удалить отдел?" />
            <ModalForm show={isFormModalShow} validated={isFormValidated} onSubmit={handleFormSubmit} onClose={handleFormClose} title="Отдел" saveBtnTestId="dept-save-btn">
                <DepartmentsForm department={currentDepartment} handleChange={handleDepartmentChange} />
            </ModalForm>
        </>
    );
};

export default Departments;
