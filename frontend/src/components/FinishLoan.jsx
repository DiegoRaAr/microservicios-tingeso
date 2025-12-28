import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import loanService from '../services/loan.service';
import '../App.css';

const FinishLoan = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [tools, setTools] = useState([]);
  const [loan, setLoan] = useState({});
  const [toolStates, setToolStates] = useState({});
  const [showPreview, setShowPreview] = useState(false);
  const [previewValues, setPreviewValues] = useState({
    valorReal: 0,
    valorAtraso: 0,
    valorDesperfecto: 0,
    total: 0
  });

  useEffect(() => {
    loanService.getLoanById(id)
      .then(response => {
        setTools(response.data.tool || []);
        setLoan(response.data);
      })
      .catch(error => {
        console.log("Error al obtener prestamo", error);
      });
  }, [id]);


  const handleToolStateChange = (toolId, value) => {
    setToolStates(prev => ({
      ...prev,
      [toolId]: value
    }));
  };


  const handlePreview = () => {
   
    const valorReal = loan.totalLoan || 0;
   
    const valorAtraso = loan.penaltyLoan || 0;

    const allSelected = tools.every(tool => toolStates[tool.idTool]);
    if (!allSelected) {
      alert("Debes seleccionar el estado de todas las herramientas.");
      return;
    }
   
    let valorDesperfecto = 0;
    tools.forEach(tool => {
      const estado = toolStates[tool.idTool];
      if (estado === "2") {
        valorDesperfecto += tool.repairCharge || 0;
      } else if (estado === "3") {
        valorDesperfecto += tool.totalValue || 0;
      }
      
    });

    const total = valorReal + valorAtraso + valorDesperfecto;

    setPreviewValues({
      valorReal,
      valorAtraso,
      valorDesperfecto,
      total
    });
    setShowPreview(true);
  };

  return (
    <div>
      <h1 className="text-start my-1 mb-4">Finalizar prestamo</h1>
      <table className="table">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Herramienta</th>
            <th scope="col">Estado</th>
          </tr>
        </thead>
        <tbody>
          {tools.map((tool) => (
            <tr key={tool.idTool}>
              <th scope="row">{tool.idTool}</th>
              <td>{tool.nameTool}</td>
              <td>
                <select
                  className="form-select"
                  aria-label="Estado herramienta"
                  value={toolStates[tool.idTool] || ""}
                  onChange={e => handleToolStateChange(tool.idTool, e.target.value)}
                  required
                >
                  <option value="">Estado herramienta</option>
                  <option value="1">Buenas condiciones</option>
                  <option value="2">Daños leves</option>
                  <option value="3">Inutilizable</option>
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button className="btn btn-primary mx-2" type="button" onClick={handlePreview}>
        Vista previa
      </button>
      <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/admin-client`)}>
        Volver al inicio
      </button>

      {showPreview && (
        <div className="alert alert-info mt-4">
          <h5>Vista previa del préstamo</h5>
          <p><strong>Valor real:</strong> ${previewValues.valorReal}</p>
          <p><strong>Valor por atraso:</strong> ${previewValues.valorAtraso}</p>
          <p><strong>Valor por desperfecto:</strong> ${previewValues.valorDesperfecto}</p>
          <hr />
          <p><strong>Total a pagar:</strong> ${previewValues.total}</p>
        </div>
      )}

      <button
        className="btn btn-success mx-2 my-4"
        type="button"
        disabled={!showPreview}
        onClick={() => {
          loanService.finishLoan(id, previewValues.total)
            .then(() => {
              alert("Préstamo finalizado con éxito");
              navigate('/admin-client');
            })
            .catch(error => {
              console.log("Error al finalizar préstamo", error);
              alert("Error al finalizar préstamo. Por favor, intente de nuevo.");
            });
        }}
      >
        Finalizar préstamo
      </button>
    </div>
  );
};

export default FinishLoan;