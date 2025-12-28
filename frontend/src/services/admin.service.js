import httpAdmin from "../http-common";

const getAllAdmins = () => {
    return httpAdmin.get('/v1/admin/');
}

const createAdmin = data => {
    return httpAdmin.post('/v1/admin/', data);
}

const getAdminById = id => {
    return httpAdmin.get(`/v1/admin/${id}`);
}

const updateAdmin = (id, data) => {
    return httpAdmin.put(`/v1/admin/${id}`, data);
}

const deleteAdmin = id => {
    return httpAdmin.delete(`/v1/admin/${id}`);
}

export default { getAllAdmins, createAdmin, getAdminById, updateAdmin, deleteAdmin };
