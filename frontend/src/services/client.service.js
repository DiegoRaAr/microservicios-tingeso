import httpClient from "../http-common";

const getAllClients = () => {
    return httpClient.get('/client-service/client/');
}

const createClient = data => {
    return httpClient.post('/client-service/client/', data);
}

const getClientById = id => {
    return httpClient.get(`/client-service/client/${id}`);
}

const getClientByRut = rut => {
    return httpClient.get(`/client-service/client/by-rut/${rut}`);
}

const updateClient = (data) => {
    return httpClient.put('/client-service/client/', data);
}

const deleteClient = id => {
    return httpClient.delete(`/client-service/client/${id}`);
}

const changeStateClient = (id) => {
    return httpClient.put(`/client-service/client/change-state/${id}`);
}

const getRestrictedClients = () => {
    return httpClient.get('/report-service/report/restricted-clients');
}

export default { getAllClients, createClient, getClientById, updateClient, deleteClient, getClientByRut, changeStateClient, getRestrictedClients };
