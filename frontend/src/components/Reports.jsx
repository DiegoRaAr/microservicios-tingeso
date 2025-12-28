import React, { useEffect, useState } from "react";
import clientService from "../services/client.service";
import loanService from "../services/loan.service";
import toolService from "../services/tool.service";

const Reports = () => {
    const [restrictedClients, setRestrictedClients] = useState([]);
    const [initDate, setInitDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [loans, setLoans] = useState([]);
    const [ranking, setRanking] = useState([]);

    useEffect(() => {
        clientService.getRestrictedClients()
            .then(res => setRestrictedClients(res.data))
            .catch(() => setRestrictedClients([]));
    }, []);

    const handleSearch = () => {
        if (!initDate || !endDate) {
            alert("Debes ingresar ambas fechas");
            return;
        }
        loanService.getLoanByRangeDate(initDate, endDate)
            .then(res => setLoans(res.data))
            .catch(() => setLoans([]));
        toolService.getBestToolsByRangeDate(initDate, endDate)
            .then(res => setRanking(res.data))
            .catch(() => setRanking([]));
    };

    return (
        <div>
            <h1 className='text-start my-1 mb-4'>Reportes</h1>

            <h2>Clientes Restringidos</h2>
            <ul className="list-group mb-4">
                {restrictedClients.length === 0 && <li className="list-group-item text-muted">No hay clientes restringidos</li>}
                {restrictedClients.map(client => (
                    <li key={client.idClient} className="list-group-item">
                        {client.nameClient} ({client.rutClient})
                    </li>
                ))}
            </ul>

            <div className="mb-4">
                <h4>Buscar por rango de fechas</h4>
                <div className="d-flex gap-2 align-items-center">
                    <input
                        type="date"
                        className="form-control"
                        value={initDate}
                        onChange={e => setInitDate(e.target.value)}
                    />
                    <span>a</span>
                    <input
                        type="date"
                        className="form-control"
                        value={endDate}
                        onChange={e => setEndDate(e.target.value)}
                    />
                    <button className="btn btn-primary" onClick={handleSearch}>Buscar</button>
                </div>
            </div>

            <div className="row">
                    <h4>Préstamos entre fechas</h4>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Cliente</th>
                                <th>Fecha inicio</th>
                                <th>Fecha fin</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            {loans.length === 0 && (
                                <tr>
                                    <td colSpan="5" className="text-muted">No hay préstamos en ese rango</td>
                                </tr>
                            )}
                            {loans.map(loan => (
                                <tr key={loan.idLoan}>
                                    <td>{loan.idLoan}</td>
                                    <td>{loan.idClient?.nameClient}</td>
                                    <td>{loan.initDate ? new Date(loan.initDate).toLocaleDateString() : ""}</td>
                                    <td>{loan.endDate ? new Date(loan.endDate).toLocaleDateString() : ""}</td>
                                    <td>{loan.stateLoan}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="row mt-4">
                    <h4>Ranking herramientas más prestadas</h4>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Herramienta</th>
                                <th>Categoría</th>
                            </tr>
                        </thead>
                        <tbody>
                            {ranking.length === 0 && (
                                <tr>
                                    <td colSpan="3" className="text-muted">No hay datos para ese rango</td>
                                </tr>
                            )}
                            {ranking.slice(0,3).map((tool, idx) => (
                                <tr key={tool.idTool}>
                                    <td>{idx + 1}</td>
                                    <td>{tool.nameTool}</td>
                                    <td>{tool.categoryTool}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <button className="btn btn-info mt-3" onClick={() => {
                    print();
                }}>Guardar Reporte</button>

        </div>
    );
};

export default Reports;