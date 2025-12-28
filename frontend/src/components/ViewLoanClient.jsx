import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import loanService from '../services/loan.service';
import clientService from '../services/client.service';

import '../App.css';

const ViewLoanClient = () => {

  const [loans, setLoans] = useState([]);
  const [client, setClient] = useState({
    rutClient: "",
    nameClient: ""
  });

  const { rut } = useParams();
  const navigate = useNavigate();
  const [tools, setTools] = useState([]);
  const [showTools, setShowTools] = useState({});
  
  const toggleTools = (loanId) => {
    setShowTools(prev => ({
      ...prev,
      [loanId]: !prev[loanId]
    }));
  }

  useEffect(() => {
    clientService.getClientByRut(rut)
      .then(response => {
        setClient(response.data);
        setClient({
          ...client,
          nameClient: response.data.nameClient
        });
      })
      .catch(error => {
        console.log("Error al obtener cliente", error);
      });
    loanService.getLoansByRut(rut)
      .then(response => {
        setLoans(response.data);
      })
      .catch(error => {
        console.log("Error al obtener prestamos", error);
      });
    }, [rut]);

  return (
    <div>
      <h2>Prestamos de {client.nameClient}</h2>
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Fecha de inicio</th>
            <th>Fecha de fin</th>
            <th>Estado</th>
            <th>Multa</th>
            <th>Valor real</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {loans.map(loan => (
            <React.Fragment key={loan.idLoan}>
              <tr>
                <td>{loan.idLoan}</td>
                <td>{loan.initDate ? new Date(loan.initDate).toLocaleDateString() : ""}</td>
                <td>{loan.endDate ? new Date(loan.endDate).toLocaleDateString() : ""}</td>
                <td>{loan.stateLoan}</td>
                <td>${loan.penaltyLoan.toLocaleString()}</td>
                <td>${loan.totalLoan ? loan.totalLoan.toLocaleString() : '0'}</td>
                <td>
                  {loan.stateLoan === "ACTIVO" && (
                    <button 
                      className="btn btn-danger mx-2" 
                      type="button"
                      onClick={ () => { navigate(`/finish-loan/${loan.idLoan}`) } }
                    >
                      Finalizar prestamo
                    </button>
                  )}
                  <button 
                    className="btn btn-info mx-2" 
                    type="button"
                    onClick={() => toggleTools(loan.idLoan)}
                    >
                    <i className={`bi bi-chevron-${showTools[loan.idLoan] ? 'up' : 'down'}`}></i>
                    {showTools[loan.idLoan] ? 'Ocultar' : 'Ver'} herramientas
                    </button>
                </td>
              </tr>
              {showTools[loan.idLoan] && (
                <tr>
                  <td colSpan="6">
                    <div className="card card-body">
                      <h6>Herramientas del préstamo:</h6>
                      {loan.tool && loan.tool.length > 0 ? (
                    <ul className="list-group">
                      {loan.tool.map(tool => (
                        <li key={tool.idTool} className="list-group-item">
                          {tool.nameTool} - {tool.categoryTool}
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-muted">No hay herramientas asociadas</p>
                  )}
                    </div>
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))}
          
        </tbody>
      </table>

      <button 
        className="btn btn-primary mx-2" 
        type="button"
        onClick={() => navigate(`/admin-client`)}
        >
        Volver
      </button>
    </div>
  );
};

export default ViewLoanClient;