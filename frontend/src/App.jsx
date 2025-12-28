import React, { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import Navbar from "./components/Navbar";
import AddTool from "./components/AddTool";
import Start from "./components/Start";
import AdminClient from "./components/AdminClient";
import AddClient from "./components/AddClient";
import MakeLoan from "./components/MakeLoan";
import FinishLoan from "./components/FinishLoan";
import Kardex from "./components/Kardex";
import Reports from "./components/Reports";

import ViewLoanClient from "./components/ViewLoanClient";
import { BrowserRouter as Router } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import keycloak from "./services/keycloak";


function App() {

  const {keycloak, initialized} = useKeycloak();

  if (!initialized) return <div>Cargando...</div>;

  const isLoggedIn = keycloak.authenticated;
  const roles = keycloak.tokenParsed?.realm_access?.roles || [];

  const privateRoute = ({element, rolesAllowed}) => {
    if(!isLoggedIn) {
      keycloak.login();
      return null;
    }
    if (rolesAllowed && !rolesAllowed.some(r => roles.includes(r))) {
      return <h2>No tienes permiso para ver esta página</h2>;
    }
    return element;
  };

  if (!isLoggedIn) {
    keycloak.login();
    return null;
  }


  return (
    <Router>
      <Navbar />
      <div className="container bg-white p-4 p-md-5 my-5 rounded-5 shadow-sm">
        <Routes>
          <Route path="/" element={<Start />} />
          <Route path="/home" element={<Home />} />
          <Route path="/add-tool" element={<AddTool />} />
          <Route path="/start" element={<Start />} />
          <Route path="/admin-client" element={<AdminClient />} />
          <Route path="/add-client" element={<AddClient />} />
          <Route path="/make-loan/:rut" element={<MakeLoan />} />
          <Route path="/finish-loan/:id" element={<FinishLoan />} />
          <Route path="/kardex" element={<Kardex />} />
          <Route path="/reports" element={<Reports />} />
          <Route path="/loans-by-rut/:rut" element={<ViewLoanClient />} />
          
        </Routes>
      </div>
    </Router>
  );
}

export default App;