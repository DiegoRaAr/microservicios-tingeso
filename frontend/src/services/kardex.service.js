import httpKardex from "../http-common";

const getAllKardex = () => {
    return httpKardex.get('/kardex-service/kardex/');
}           

const createKardex = data => {
    return httpKardex.post('/kardex-service/kardex/', data);
}

const getKardexById = id => {
    return httpKardex.get(`/kardex-service/kardex/${id}`);
}

const updateKardex = (data) => {
    return httpKardex.put('/kardex-service/kardex/', data);
}

const deleteKardex = id => {
    return httpKardex.delete(`/kardex-service/kardex/${id}`);
}

const getKardexByDate = (startDate, endDate) => {
    return httpKardex.get(`/kardex-service/kardex/byDate/${startDate}/${endDate}`);
}

export default { getAllKardex, createKardex, getKardexById, updateKardex, deleteKardex, getKardexByDate };