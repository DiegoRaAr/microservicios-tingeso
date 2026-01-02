import axios from 'axios';

// Para Kubernetes: usar rutas relativas que pasen por el proxy nginx
// El proxy nginx redirige /client-service/, /loan-service/, etc. al gateway-service
const baseURL = '';

const api = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export default api;