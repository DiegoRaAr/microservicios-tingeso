# Etapa 1: Construcción del proyecto
FROM node:22 AS build

WORKDIR /app

COPY package*.json ./

RUN npm install --include=dev

COPY . .

# Build-time args para VITE_*
ARG VITE_BACKEND_SERVER=localhost
ARG VITE_BACKEND_PORT=8090
ENV VITE_BACKEND_SERVER=$VITE_BACKEND_SERVER
ENV VITE_BACKEND_PORT=$VITE_BACKEND_PORT

# Compilar el frontend (genera carpeta "dist")
RUN npm run build


# Etapa 2: Servir con Nginx
FROM nginx:alpine

# Copiar los archivos compilados a la carpeta de Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Exponer el puerto 80
EXPOSE 80

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
