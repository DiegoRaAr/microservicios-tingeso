import React, {use, useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import clientService from '../services/client.service';
import loanService from '../services/loan.service';
import '../App.css';

const AdminClient = () => {

  const navigate = useNavigate();

  const [clients, setClients] = React.useState([]);

  useEffect(() => {
    clientService.getAllClients()
      .then(response => {
        setClients(response.data);
      })
      .catch(error => {
        console.log("Error al obtener clientes", error);
      });
  }, []);

  return (
    <div>
      <h2 className='text-start my-1 mb-4'>Lista de clientes</h2>
      <div className="d-flex gap-2 mb-3">
        <input className="form-control" type="search" placeholder="Search" aria-label="Search" />
        <button className="btn btn-outline-success" type="submit">Search</button>
      </div>
      <table className="table table-striped table-hover align-middle">
                <thead>
                    <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Contacto</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    {clients.map((client) => (
                        <tr key={client.id}>
                            <th scope="row">{client.idClient}</th>
                            <td>{client.nameClient}</td>
                            <td>{client.stateClient}</td>
                            <td>{client.phoneNumberClient}</td>
                            <div class="d-grid gap-2 d-md-block">

                            <button 
                              className="btn btn-danger mx-2"
                              type="button"
                              onClick={() => {
                                clientService.changeStateClient(client.idClient)
                                .then(() => {
                                  navigate('/admin-client');
                                  window.location.reload();
                                })
                                .catch(error => {
                                  console.log("Error al cambiar estado del cliente", error);
                                });
                              }
                              }                              
                            >
                              Cambiar estado
                              </button>

                            <button
                              className="btn btn-warning mx-2"
                              type="button"
                              onClick={() => navigate('/add-client', { state: { client } })}
                            >
                              Editar
                            </button>

                            <button 
                              class="btn btn-info mx-2" 
                              type="button" 
                              onClick= {() => {
                                loanService.updatePenalty(client.idClient)
                                .then(() =>{
                                navigate(`/loans-by-rut/${client.rutClient}`)}
                                )}
                              }
                            >
                              Ver prestamos
                              </button>

                            <button 
                              class="btn btn-success mx-2" 
                              type="button" 
                              onClick= {() => {
                                loanService.updatePenalty(client.idClient)
                                .then(() =>{
                                navigate(`/make-loan/${client.rutClient}`, {state: {client}})}
                                )}
                              }
                            >
                              Iniciar prestamo
                              </button>

                            </div>
                        </tr>
                    ))}
                </tbody>
                </table>

            <button class="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/add-client`)}>Agregar cliente</button>
            <button class="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
      
    </div>
  );
};

export default AdminClient;