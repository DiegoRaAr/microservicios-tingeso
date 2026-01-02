import React, {useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "../services/tool.service";
import '../App.css';

const Home = () => {

    // Temporalmente todos los usuarios tienen acceso de admin
    const isAdmin = true;
    const isEMPLOYEE = true;

    const [tools, setTools] = useState([]);
    const addToolNumber = (id, number) => {
        toolService.addToolNumber(id, number)
        window.location.reload()
            .then(response => {
                alert("Herramienta agregada con éxito");
            })
    }
    const navigate = useNavigate();

    useEffect(() => {
        toolService.getAllTools()
            .then(response => {
                setTools(response.data);
            })
            .catch(error => {
                console.log("Error al obtener herramientas", error);
            });
    }, []);

    return (
        

        <div className="container-fluid">
            <h1 className="text-start my-1 mb-4">Lista de herramientas</h1>
           
            <table className="table table-striped table-hover align-middle">
                <thead>
                    <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Herramienta</th>
                    <th scope="col">Categoría</th>
                    <th scope="col">Cargo diario</th>
                    <th scope="col">Stock</th>
                    </tr>
                </thead>
                <tbody className="table-group-divider">
                {tools
                    .filter(tool => tool.stateTool === "ACTIVA")
                    .map((tool) => (
                    <tr key={tool.idTool}>
                        <th scope="row">{tool.idTool}</th>
                        <td>{tool.nameTool}</td>
                        <td>{tool.categoryTool}</td>
                        <td>{tool.dailyCharge}</td>
                        <td>{tool.stockTool}</td>
                        <td>
                            <div className="d-grid gap-2 d-md-block">
                                
                                {isAdmin && (
                                <button
                                    className="btn btn-warning mx-2"
                                    type="button"
                                    onClick={() => navigate('/add-tool', { state: { tool } })}
                                    >
                                    Editar
                                </button>
                                )}

                                {isAdmin && (
                                <button className="btn btn-success mx-2"
                                    type="button" 
                                    onClick={() => {toolService.addTool(tool.idTool)
                                        .then(() => {
                                            alert("Herramienta agregada con éxito");
                                            window.location.reload();
                                        })
                                        .catch(() => alert("Error al agregar herramienta"));
                                    
                                    }}
                                    >Sumar herramienta
                                </button>
                                )}

                                {isAdmin && (
                                <button className="btn btn-danger mx-2" 
                                    type="button" 
                                    onClick={() => {
                                        if (tool.stockTool > 1){
                                            toolService.subtractTool(tool.idTool)
                                            .then(() => {
                                                alert("Herramienta quitada con éxito");
                                                window.location.reload();
                                            })
                                            .catch(() => alert("Error al quitar herramienta"));
                                        }else{
                                            if(confirm('¿Eliminar herramienta?')){
                                                toolService.subtractTool(tool.idTool)
                                                .then(() => {
                                                    alert("Herramienta eliminada con éxito");
                                                })
                                                .catch(() => alert("Error al eliminar herramienta"));
                                                window.location.reload()
                                            }
                                        }
                                    }}
                                    >Bajar herramienta
                                </button>
                                )}
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
                </table>

            <button className="btn btn-primary mx-2" type="button" onClick={() => navigate(`/add-tool`)}>Agregar herramienta</button>
            <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
            

        </div>
        
    );
};

export default Home;