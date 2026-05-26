import Departments from "../components/departments/table/Departments";

const DepartmentsPage = () => {
    return (
        <>
            <div className="container-lg table-responsive">
                <h3>Отделы</h3>
                <Departments />
            </div>
        </>
    );
};

export default DepartmentsPage;