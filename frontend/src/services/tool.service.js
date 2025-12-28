import httpTool from "../http-common";

const getAllTools = () => {
    return httpTool.get('/v1/tool/');
}

const createTool = data => {
    return httpTool.post('/v1/tool/', data);
}

const getToolById = id => {
    return httpTool.get(`/v1/tool/${id}`);
}

const updateTool = (data) => {
    return httpTool.put('/v1/tool/', data);
}

const deleteTool = id => {
    return httpTool.delete(`/v1/tool/${id}`);
}

const subtractTool = (id) => {
    return httpTool.put(`/v1/tool/subtract-tool/${id}`);
}

const addTool = (id) => {
    return httpTool.put(`/v1/tool/add-tool/${id}`);
}

const getBestToolsByRangeDate = (initDate, endDate) => {
    return httpTool.get(`/v1/tool/best-tools-by-range-date/${initDate}/${endDate}`);
}


export default { getAllTools, createTool, getToolById, updateTool, deleteTool, subtractTool, addTool, getBestToolsByRangeDate };