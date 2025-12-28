import httpEmployee from "../http-common";

const getAllEmployees = () => {
    return httpEmployee.get('/v1/employee/');
}

const createEmployee = data => {
    return httpEmployee.post('/v1/employee/', data);
}

const getEmployeeById = id => {
    return httpEmployee.get(`/v1/employee/${id}`);
}

const updateEmployee = (id, data) => {
    return httpEmployee.put(`/v1/employee/${id}`, data);
}

const deleteEmployee = id => {
    return httpEmployee.delete(`/v1/employee/${id}`);
}

export default { getAllEmployees, createEmployee, getEmployeeById, updateEmployee, deleteEmployee };