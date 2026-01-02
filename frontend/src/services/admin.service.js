import httpAdmin from "../http-common";

// NOTA: No existe admin-service en el backend
// Este servicio está comentado hasta que se implemente

const getAllAdmins = () => {
    console.warn('admin-service no existe en el backend');
    return Promise.reject('admin-service no implementado');
}

const createAdmin = data => {
    console.warn('admin-service no existe en el backend');
    return Promise.reject('admin-service no implementado');
}

const getAdminById = id => {
    console.warn('admin-service no existe en el backend');
    return Promise.reject('admin-service no implementado');
}

const updateAdmin = (data) => {
    console.warn('admin-service no existe en el backend');
    return Promise.reject('admin-service no implementado');
}

const deleteAdmin = id => {
    console.warn('admin-service no existe en el backend');
    return Promise.reject('admin-service no implementado');
}

export default { getAllAdmins, createAdmin, getAdminById, updateAdmin, deleteAdmin };
