import httpEmployee from "../http-common";

// NOTA: No existe employee-service en el backend
// Este servicio está comentado hasta que se implemente

const getAllEmployees = () => {
    console.warn('employee-service no existe en el backend');
    return Promise.reject('employee-service no implementado');
}

const createEmployee = data => {
    console.warn('employee-service no existe en el backend');
    return Promise.reject('employee-service no implementado');
}

const getEmployeeById = id => {
    console.warn('employee-service no existe en el backend');
    return Promise.reject('employee-service no implementado');
}

const updateEmployee = (data) => {
    console.warn('employee-service no existe en el backend');
    return Promise.reject('employee-service no implementado');
}

const deleteEmployee = id => {
    console.warn('employee-service no existe en el backend');
    return Promise.reject('employee-service no implementado');
}

export default { getAllEmployees, createEmployee, getEmployeeById, updateEmployee, deleteEmployee };