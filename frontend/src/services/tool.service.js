import httpTool from "../http-common";

const getAllTools = () => {
    return httpTool.get('/tool-service/tool/');
}

const createTool = data => {
    return httpTool.post('/tool-service/tool/', data);
}

const getToolById = id => {
    return httpTool.get(`/tool-service/tool/${id}`);
}

const updateTool = (data) => {
    return httpTool.put('/tool-service/tool/', data);
}

const deleteTool = id => {
    return httpTool.delete(`/tool-service/tool/${id}`);
}

const subtractTool = (id) => {
    return httpTool.put(`/tool-service/tool/subtract-tool/${id}`);
}

const addTool = (id) => {
    return httpTool.put(`/tool-service/tool/add-tool/${id}`);
}

const getBestToolsByRangeDate = (initDate, endDate) => {
    return httpTool.get(`/tool-service/tool/best-tools-by-range-date/${initDate}/${endDate}`);
}


export default { getAllTools, createTool, getToolById, updateTool, deleteTool, subtractTool, addTool, getBestToolsByRangeDate };