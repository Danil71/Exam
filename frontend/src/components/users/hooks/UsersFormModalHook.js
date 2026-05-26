import { useState } from 'react';
import useModal from '../../modal/ModalHook';
import useUsersForm from './UsersFormHook';



const useUsersFormModal = (usersChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(0);
    

    const {
        user,
        validated,
        handleSubmit,
        handleChange,
        handleEmployeeChange,
        initialEmployee,
        resetValidity,
    } = useUsersForm(currentId, usersChangeHandle);

    const showModalDialog = (id) => {
        setCurrentId(id);
        resetValidity();
        showModal();
    };

    const onClose = () => {
        setCurrentId(-1);
        hideModal();
    };

    const onSubmit = async (event) => {
        if (await handleSubmit(event)) {
            onClose();
        }
    };

    return {
        isFormModalShow: isModalShow,
        isFormValidated: validated,
        showFormModal: showModalDialog,
        currentUser: user,
        handleEmployeeChange,
        initialEmployee,
        handleUserChange: handleChange,
        handleFormSubmit: onSubmit,
        handleFormClose: onClose,
    };
};

export default useUsersFormModal;
