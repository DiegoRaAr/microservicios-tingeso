import Keycloak from "keycloak-js";

// Para Kubernetes: usar ruta relativa que pase por el proxy nginx
const keycloak = new Keycloak({
  url: "/auth",
  realm: import.meta.env.VITE_KEYCLOAK_REALM || "tingeso-realm",
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || "frontend-app",
}); 

export default keycloak;