# Despliegue del Frontend en Kubernetes

## Paso 1: Construir la imagen Docker

Desde la carpeta del frontend:

```bash
cd frontend
docker build -t diegoraar/frontend-tingeso:latest .
```

## Paso 2: Subir la imagen a Docker Hub

```bash
docker push diegoraar/frontend-tingeso:latest
```

## Paso 3: Aplicar ConfigMap

```bash
kubectl apply -f kubernetes/frontend/frontend-configmap.yml
```

## Paso 4: Desplegar el Frontend

```bash
kubectl apply -f kubernetes/frontend/frontend-deployment.yml
```

## Paso 5: Verificar el despliegue

```bash
# Ver el pod
kubectl get pods -l app=frontend

# Ver el servicio
kubectl get svc frontend

# Ver logs
kubectl logs -l app=frontend
```

## Paso 6: Acceder al Frontend

### Opción 1: Port-forward (desarrollo)
```bash
kubectl port-forward svc/frontend 3000:80
```
Luego accede a: http://localhost:3000

### Opción 2: Minikube service (desarrollo)
```bash
minikube service frontend --url
```

### Opción 3: NodePort (acceso directo)
El servicio está configurado como LoadBalancer. En Minikube quedará como NodePort.
```bash
minikube service frontend
```

## Configuración de Variables de Entorno

Las variables están definidas en el ConfigMap:
- VITE_BACKEND_SERVER: gateway-service
- VITE_BACKEND_PORT: 8080
- VITE_KEYCLOAK_URL: http://keycloak:8080
- VITE_KEYCLOAK_REALM: tingeso-realm
- VITE_KEYCLOAK_CLIENT_ID: frontend-app

## Notas importantes:

1. **Nginx Proxy**: El frontend tiene configurado un proxy para:
   - Servicios backend: `/client-service/`, `/loan-service/`, etc. → `gateway-service:8080`
   - Keycloak: `/auth/` → `keycloak:8080`

2. **Actualizar imagen**: Si haces cambios en el código:
   ```bash
   cd frontend
   docker build -t diegoraar/frontend-tingeso:latest --no-cache .
   docker push diegoraar/frontend-tingeso:latest
   kubectl rollout restart deployment/frontend
   ```

3. **Ver logs en tiempo real**:
   ```bash
   kubectl logs -f -l app=frontend
   ```
