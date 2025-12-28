import React, { useState, useEffect } from 'react';
import toolService from '../services/tool.service';
import loanService from '../services/loan.service';
import { useLocation, useNavigate } from "react-router-dom";

const MakeLoan = () => {
  const location = useLocation();
  const client = location.state?.client;

  const navigate = useNavigate();

  const [previewPrice, setPreviewPrice] = useState(null);

  const [tools, setTools] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const [endDate, setEndDate] = useState('');

  useEffect(() => {
    toolService.getAllTools().then(r => setTools(r.data));
  }, []);

  const handleToolSelect = (toolId) => {
    setSelectedTools(prev =>
      prev.includes(toolId)
        ? prev.filter(id => id !== toolId)
        : [...prev, toolId]
    );
  };

  const handlePreview = () => {
    if (!endDate || selectedTools.length === 0) {
      alert("Selecciona herramientas y fecha de devolución");
      return;
    }
    
    const days =
      Math.ceil(
        (new Date(endDate).getTime() - new Date().getTime()) /
        (1000 * 60 * 60 * 24)
      ) || 1;

    const total = tools
      .filter(tool => selectedTools.includes(tool.idTool))
      .reduce((sum, tool) => sum + tool.dailyCharge * days, 0);

    setPreviewPrice(total);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const loanData = {
      initDate: new Date(),
      endDate: new Date(endDate),
      stateLoan: 'ACTIVO',
      penaltyLoan: 0,
      tool: selectedTools.map(id => ({ idTool: id })),
      idClient: { idClient: client.idClient }
    };

    try {
      await loanService.createLoan(loanData);
      alert('Préstamo creado exitosamente');
      setSelectedTools([]);
      setEndDate('');
      navigate(`/loans-by-rut/${client.rutClient}`);
    } catch (error) {
      console.error('Error:', error);
      alert('Error al crear préstamo');
    }
  };

  return (
    <div>
      <h1 className="text-start my-1 mb-4">Realizar prestamo</h1>
      <form onSubmit={handleSubmit}>
        {/* Mostrar Cliente por defecto */}
        <div className="mb-3">
          <label className="form-label fw-bold fs-4">Cliente</label>
          <div className="form-control" readOnly>
            {client.nameClient} - {client.rutClient}
          </div>
        </div>

        {/* Lista de Herramientas con Checkbox */}
        <div className="mb-3">
          <label className="form-label fw-bold fs-4">Herramientas</label>
          <ul className="list-group">
            {tools
              .filter(tool => tool.stockTool >= 1 && tool.stateTool === "ACTIVA")
              
              .map(tool => (
                <li key={tool.idTool} className="list-group-item">
                  <input
                    className="form-check-input me-2"
                    type="checkbox"
                    id={`tool-${tool.idTool}`}
                    checked={selectedTools.includes(tool.idTool)}
                    onChange={() => handleToolSelect(tool.idTool)}
                  />
                  <label className="form-check-label" htmlFor={`tool-${tool.idTool}`}>
                    {tool.nameTool} - Stock: {tool.stockTool}
                  </label>
                </li>
              ))}
          </ul>
        </div>

        {/* Fecha de Devolución */}
        <div className="mb-3">
          <label className="form-label fw-bold fs-4">Fecha de Devolución</label>
          <input
            type="date"
            className="form-control"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            required
          />
        </div>

        <button 
          type="submit" 
          className="btn btn-success mx-2"
          >
          Crear Préstamo
        </button>

        <button
          type="button"
          className="btn btn-info mx-2"
          onClick={handlePreview}
        >
          Vista previa precio
        </button>

        {previewPrice !== null && (
          <div className="alert alert-primary mt-3">
            <strong>Precio estimado del préstamo:</strong> ${previewPrice}
          </div>
        )}

        <button 
          type="button" 
          className="btn btn-warning mx-2"
          onClick={() => navigate(-1)}
          >
          Cancelar
        </button>


      </form>
    </div>
  );
};

export default MakeLoan;