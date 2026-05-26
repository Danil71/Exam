import Users from "../components/users/table/Users";

const UsersPage = () => {
    return (
        <>
            <div className="container-lg table-responsive">
                <h3>Пользователи</h3>
                <Users />
            </div>
        </>
    );
};

export default UsersPage;