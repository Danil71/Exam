import { useState } from 'react';
import useDepartmentsForm from './DepartmentsFormHook';

const useDepartmentsFormModal = (departmentsChangeHandle) => {
    const [isFormModalShow, setIsFormModalShow] = useState(false);
    const [departmentIdForModal, setDepartmentIdForModal] = useState(null);

    const { department, validated, handleSubmit, handleChange, resetValidity } =
        useDepartmentsForm(departmentIdForModal, departmentsChangeHandle);

    const showFormModal = (id = null) => {
        setDepartmentIdForModal(id);
        resetValidity();
        setIsFormModalShow(true);
    };

    const handleFormClose = () => {
        setIsFormModalShow(false);
        setDepartmentIdForModal(null);
    };

    const handleFormSubmit = async (event) => {
        if (await handleSubmit(event)) {
            handleFormClose();
        }
    };

    return {
        isFormModalShow,
        isFormValidated: validated,
        showFormModal,
        currentDepartment: department,
        handleDepartmentChange: handleChange,
        handleFormSubmit,
        handleFormClose,
    };
};

export default useDepartmentsFormModal;
