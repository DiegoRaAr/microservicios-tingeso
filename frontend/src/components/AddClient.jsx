import { useEffect, useState } from "react";
import clientService from "../services/client.service";
import { useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";

const AddClient = () => {

  const location = useLocation();
  const clientToEdit = location.state?.client;

  if (clientToEdit) {
    console.log("Editar cliente:", clientToEdit);
  }

  const navigate = useNavigate();

  const [client, setClient] = useState({
    rutClient: "",
    nameClient: "",
    stateClient: "ACTIVO",
    emailClient: "",
    phoneNumberClient: ""
  });

  useEffect(() => {
    if (clientToEdit) {
      setClient(clientToEdit);
    }
  }, [clientToEdit]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setClient({
      ...client,
      [name]: type === 'checkbox' ? checked : value 
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (clientToEdit) {
      clientService.updateClient(client)
        .then(response => {
          alert("Cliente actualizado con éxito");
          navigate('/admin-client');
        })
        .catch(error => {
          console.log("Error al actualizar cliente", error);
        });
      return;
    } else {
        clientService.createClient(client)
          .then(response => {
            alert("Cliente añadido con éxito");
            setClient({
              rutClient: "",
              nameClient: "",
              stateClient: "ACTIVO",
              emailClient: "",
              phoneNumberClient: ""
            });
          })
          .catch(error => {
            console.log("Error al añadir cliente", error);
          });
      }
  };


  return (
    <div>
      <h1 className="text-start my-1 mb-4">
        {clientToEdit ? "Editar cliente" : "Añadir un nuevo cliente"}</h1>
      <h5 className="text-start my-3 mb-4">Datos del cliente:</h5>

      
      <form onSubmit={handleSubmit}>
                <div className="mb-4 text-start">
                    <label htmlFor="rutClient" className="form-label">Rut Cliente</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        id="rutClient" 
                        name="rutClient"
                        value={client.rutClient}
                        onChange={handleChange}
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="nameclient" className="form-label">Nombre Cliente</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        id="nameClient"
                        name="nameClient"
                        value={client.nameClient}
                        onChange={handleChange} 
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="emailClient" className="form-label">Correo electronico Cliente</label>
                    <input 
                        type="email" 
                        className="form-control" 
                        id="emailClient" 
                        name="emailClient"
                        value={client.emailClient}
                        onChange={handleChange}
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="phoneNumberClient" className="form-label">Numero de telefono</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        id="phoneNumbreClient"
                        name="phoneNumberClient"
                        value={client.phoneNumberClient}
                        onChange={handleChange} 
                    />
                </div>
                <button 
                  type="submit" 
                  className="btn btn-primary">
                    {clientToEdit ? "Actualizar cliente" : "Añadir Cliente"}</button>
            </form>

      <button class="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/admin-client`)}>Volver</button>
    </div>
  );
};

export default AddClient;

