import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import '../App.css';
import kardexService from "../services/kardex.service";
import toolService from "../services/tool.service";

const Kardex = () => {
    const navigate = useNavigate();

    const [kardexes, setKardexes] = useState([]);
    const [tools, setTools] = useState([]);
    const [selectedTool, setSelectedTool] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");

    useEffect(() => {
        kardexService.getAllKardex()
            .then(response => setKardexes(response.data))
            .catch(error => console.log("Error al obtener kardex", error));
        toolService.getAllTools()
            .then(response => setTools(response.data))
            .catch(error => console.log("Error al obtener herramientas", error));
    }, []);

    const filteredKardexes = kardexes.filter(kardex => {
        // Filter for tool
        const toolMatch = selectedTool ? kardex.idTool === Number(selectedTool) : true;
        // filter for date range
        const kardexDate = new Date(kardex.dateKardex);
        const startMatch = startDate ? kardexDate >= new Date(startDate) : true;
        const endMatch = endDate ? kardexDate <= new Date(endDate) : true;
        return toolMatch && startMatch && endMatch;
    });

    return (
        <div>
            <h2>Kardex</h2>
            {/* Filtros */}
            <div className="mb-3 d-flex gap-3">
                <select
                    className="form-select"
                    value={selectedTool}
                    onChange={e => setSelectedTool(e.target.value)}
                >
                    <option value="">Todas las herramientas</option>
                    {tools.map(tool => (
                        <option key={tool.idTool} value={tool.idTool}>
                            {tool.nameTool}
                        </option>
                    ))}
                </select>
                <input
                    type="date"
                    className="form-control"
                    value={startDate}
                    onChange={e => setStartDate(e.target.value)}
                />
                <input
                    type="date"
                    className="form-control"
                    value={endDate}
                    onChange={e => setEndDate(e.target.value)}
                />
                <button
                    className="btn btn-secondary"
                    type="button"
                    onClick={() => {
                        setSelectedTool("");
                        setStartDate("");
                        setEndDate("");
                    }}
                >
                    Limpiar filtros
                </button>
            </div>
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th scope="col">ID Kardex</th>
                        <th scope="col">Fecha</th>
                        <th scope="col">ID Herramienta</th>
                        <th scope="col">Nombre Herramienta</th>
                        <th scope="col">Estado Herramienta</th>
                    </tr>
                </thead>
                <tbody className="table-group-divider">
                    {filteredKardexes.map((kardex) => (
                        <tr key={kardex.idKardex}>
                            <td>{kardex.idKardex}</td>
                            <td>{new Date(kardex.dateKardex).toLocaleDateString()}</td>
                            <td>{kardex.idTool}</td>
                            <td>{kardex.nameTool}</td>
                            <td>{kardex.stateTool}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
        </div>
    );
};

export default Kardex;