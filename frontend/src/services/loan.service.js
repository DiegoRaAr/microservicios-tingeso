import httpLoan from '../http-common';

const getAllLoans = () => {
    return httpLoan.get('/loan-service/loan/');
}

const createLoan = data => {
    return httpLoan.post('/loan-service/loan/', data);
}

const getLoanById = id => {
    return httpLoan.get(`/loan-service/loan/${id}`);
}

const updateLoan = (data) => {
    return httpLoan.put('/loan-service/loan/', data);
}

const deleteLoan = id => {
    return httpLoan.delete(`/loan-service/loan/${id}`);
}

const getLoansByRut = rut => {
    return httpLoan.get(`/loan-service/loan/loan-by-rut/${rut}`);
}

const getToolsByLoanId = id => {
    return httpLoan.get(`/loan-service/loan/tools/${id}`);
}

const updatePenalty = (id) => {
    return httpLoan.put(`/loan-service/loan/update-penalty/${id}`);
}

const finishLoan = (id,total) => {
    return httpLoan.put(`/loan-service/loan/finish-loan/${id}/${total}`);
}

const getLoanByRangeDate = (startDate, endDate) => {
    return httpLoan.get(`/loan-service/loan/loans-by-range-date/${startDate}/${endDate}`);
}

export default { getAllLoans, createLoan, getLoanById, updateLoan, deleteLoan , getLoansByRut, getToolsByLoanId, updatePenalty, finishLoan, getLoanByRangeDate };