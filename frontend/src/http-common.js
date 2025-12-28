import axios from 'axios';
import { configs } from 'eslint-plugin-react-refresh';
import keycloak from './services/keycloak';

// Siempre usar URL relativa para que funcione con el proxy de nginx
const baseURL = '/api';

const api = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(async (config) => {
    if (keycloak.authenticated) {
        try {
            // Intentar renovar el token si es necesario
            const refreshed = await keycloak.updateToken(30);
            if (refreshed) {
                console.log('Token renovado automáticamente');
            }
            console.log('Token actual:', keycloak.token ? 'Presente' : 'Ausente');
            config.headers.Authorization = `Bearer ${keycloak.token}`;
        } catch (error) {
            console.error('Error al renovar token:', error);
            // Si hay error al renovar, redirigir al login
            keycloak.login();
            return Promise.reject(error);
        }
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;