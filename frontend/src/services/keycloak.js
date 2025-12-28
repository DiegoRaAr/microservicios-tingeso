import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "/auth",  // Ruta relativa a través del proxy Nginx
  realm: "loan-realm",
  clientId: "loan-spa",
}); 

window.keycloak = keycloak;

export default keycloak;