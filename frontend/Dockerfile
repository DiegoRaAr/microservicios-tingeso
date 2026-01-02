# Etapa 1: Construcción con Node (Vite)
FROM node:20 AS build
WORKDIR /app

# Copiamos dependencias y código
COPY package*.json ./
RUN npm install
COPY . .

# Construimos el proyecto para producción
RUN npm run build

# Etapa 2: Servir con Nginx
FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html

# Borramos contenido por defecto
RUN rm -rf ./*

# Copiamos el build de Vite
COPY --from=build /app/dist ./

# Configuramos Nginx para SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
