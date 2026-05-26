import Employees from "../components/employees/table/Employees";

const EmployeesPage = () => {
    return (
        <>
            <div className="container-lg table-responsive">
                <h3>Работники</h3>
                <Employees />
            </div>
        </>
    );
};

export default EmployeesPage;