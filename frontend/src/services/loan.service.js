import httpLoan from '../http-common';

const getAllLoans = () => {
    return httpLoan.get('/v1/loan/');
}

const createLoan = data => {
    return httpLoan.post('/v1/loan/', data);
}

const getLoanById = id => {
    return httpLoan.get(`/v1/loan/${id}`);
}

const updateLoan = (id, data) => {
    return httpLoan.put(`/v1/loan/${id}`, data);
}

const deleteLoan = id => {
    return httpLoan.delete(`/v1/loan/${id}`);
}

const getLoansByRut = rut => {
    return httpLoan.get(`/v1/loan/by-rut/${rut}`);
}

const getToolsByLoanId = id => {
    return httpLoan.get(`/v1/loan/tools-by-loan/${id}`);
}

const updatePenalty = (id) => {
    return httpLoan.put(`/v1/loan/update-penalty/${id}`);
}

const finishLoan = (id,total) => {
    return httpLoan.put(`/v1/loan/finish-loan/${id}/${total}`);
}

const getLoanByRangeDate = (startDate, endDate) => {
    return httpLoan.get(`/v1/loan/loans-by-range-date/${startDate}/${endDate}`);
}

export default { getAllLoans, createLoan, getLoanById, updateLoan, deleteLoan , getLoansByRut, getToolsByLoanId, updatePenalty, finishLoan, getLoanByRangeDate };