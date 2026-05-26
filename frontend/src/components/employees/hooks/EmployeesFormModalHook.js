import { useState } from 'react';
import useModal from '../../modal/ModalHook';
import useEmployeesForm from './EmployeesFormHook';

const useEmployeesFormModal = (employeesChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(-1);

    const { employee, validated, handleSubmit, handleChange, handlePhoneIdsChange, resetValidity } =
        useEmployeesForm(currentId >= 0 ? currentId : undefined, employeesChangeHandle);

    const showFormModal = (id) => {
        setCurrentId(id ?? -1);
        resetValidity();
        showModal();
    };

    const onClose = () => {
        setCurrentId(-1);
        hideModal();
    };

    const onSubmit = async (event) => {
        if (await handleSubmit(event)) onClose();
    };

    return {
        isFormModalShow: isModalShow,
        isFormValidated: validated,
        showFormModal,
        currentEmployee: employee,
        handleEmployeeChange: handleChange,
        handlePhoneIdsChange,
        handleFormSubmit: onSubmit,
        handleFormClose: onClose,
    };
};

export default useEmployeesFormModal;
