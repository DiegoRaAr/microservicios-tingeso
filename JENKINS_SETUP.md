# Configuración de Jenkins para Microservicios

Este documento explica cómo configurar Jenkins para trabajar con los Jenkinsfiles individuales de cada microservicio.

## Tabla de Contenidos

1. [Resumen](#resumen)
2. [Prerequisitos](#prerequisitos)
3. [Configuración de Credenciales](#configuración-de-credenciales)
4. [Opción 1: Multibranch Pipeline](#opción-1-multibranch-pipeline)
5. [Opción 2: Pipeline Individual por Servicio](#opción-2-pipeline-individual-por-servicio)
6. [Configuración de Webhooks](#configuración-de-webhooks)
7. [Solución de Problemas](#solución-de-problemas)

## Resumen

Cada microservicio ahora tiene su propio `Jenkinsfile` que automatiza:
- ✅ Compilación con Maven
- ✅ Construcción de imagen Docker
- ✅ Push a Docker Hub con tags `latest` y número de build
- ✅ Limpieza de imágenes locales

### Estructura de Archivos

```
microservicios-tingeso/
├── Jenkinsfile                    # Pipeline que construye todos los servicios
├── client-service/
│   └── Jenkinsfile               # Pipeline individual para client-service
├── config-service/
│   └── Jenkinsfile               # Pipeline individual para config-service
├── eureka-service/
│   └── Jenkinsfile               # Pipeline individual para eureka-service
├── gateway-service/
│   └── Jenkinsfile               # Pipeline individual para gateway-service
├── kardex-service/
│   └── Jenkinsfile               # Pipeline individual para kardex-service
├── loan-service/
│   └── Jenkinsfile               # Pipeline individual para loan-service
├── price-service/
│   └── Jenkinsfile               # Pipeline individual para price-service
├── report-service/
│   └── Jenkinsfile               # Pipeline individual para report-service
└── tool-service/
    └── Jenkinsfile               # Pipeline individual para tool-service
```

## Prerequisitos

Antes de configurar Jenkins, asegúrate de tener:

- ✅ Jenkins instalado y funcionando
- ✅ Plugin de Git instalado
- ✅ Plugin de Docker Pipeline instalado (`docker-pipeline`)
- ✅ Plugin de Credentials Binding instalado
- ✅ Docker instalado en el agente de Jenkins
- ✅ Cuenta de Docker Hub
- ✅ Repositorio Git (GitHub, GitLab, Bitbucket, etc.)

### Verificar Plugins Necesarios

1. Ve a **Manage Jenkins** → **Manage Plugins**
2. En la pestaña **Installed**, verifica que estén instalados:
   - Git plugin
   - Docker Pipeline
   - Credentials Binding Plugin
   - (Opcional) Multibranch Scan Webhook Trigger - para triggers más rápidos

## Configuración de Credenciales

### Configurar Credenciales de Docker Hub

1. En Jenkins, ve a **Manage Jenkins** → **Manage Credentials**
2. Selecciona el dominio apropiado (generalmente `(global)`)
3. Click en **Add Credentials**
4. Configura los siguientes campos:
   - **Kind**: Username with password
   - **Scope**: Global
   - **Username**: Tu usuario de Docker Hub (ej: `diegoraar`)
   - **Password**: Tu contraseña o token de Docker Hub
   - **ID**: `docker-credentials` ⚠️ **Este ID debe coincidir exactamente**
   - **Description**: Docker Hub Credentials
5. Click **OK**

> [!WARNING]
> El ID `docker-credentials` está hardcodeado en los Jenkinsfiles. Si usas otro ID, deberás modificar todos los Jenkinsfiles.

## Opción 1: Multibranch Pipeline

Esta es la opción **RECOMENDADA** si quieres que Jenkins detecte automáticamente cambios en servicios específicos y construya solo lo que cambió.

### Paso 1: Crear Multibranch Pipeline

1. En Jenkins, click en **New Item**
2. Ingresa un nombre: `microservicios-tingeso`
3. Selecciona **Multibranch Pipeline**
4. Click **OK**

### Paso 2: Configurar el Repositorio

1. En **Branch Sources**, click **Add source** → **Git**
2. Configura:
   - **Project Repository**: URL de tu repositorio (ej: `https://github.com/DiegoRaAr/microservicios-tingeso.git`)
   - **Credentials**: Selecciona tus credenciales de Git (si es privado)
3. En **Behaviors**, asegúrate de tener:
   - **Discover branches**
   - **Discover tags** (opcional)

### Paso 3: Configurar Build Configuration

1. En **Build Configuration**:
   - **Mode**: by Jenkinsfile
   - **Script Path**: `Jenkinsfile` (esto es el default)

### Paso 4: Configurar Scan Triggers

1. En **Scan Multibranch Pipeline Triggers**:
   - Marca **Periodically if not otherwise run**
   - Intervalo: `1 hour` (o el que prefieras)

### Paso 5: Guardar y Ejecutar

1. Click **Save**
2. Jenkins escaneará automáticamente el repositorio
3. Encontrará las ramas y ejecutará los Jenkinsfiles

> [!NOTE]
> Con esta configuración, Jenkins usará el Jenkinsfile en la raíz que construye todos los servicios. Para construcción selectiva, necesitarás configurar pipelines individuales (ver Opción 2) o modificar el Jenkinsfile raíz para detectar cambios.

## Opción 2: Pipeline Individual por Servicio

Esta opción te permite crear un job de Jenkins separado para cada microservicio, dándote control granular.

### Para Cada Microservicio:

#### Ejemplo: client-service

1. En Jenkins, click **New Item**
2. Nombre: `client-service-pipeline`
3. Selecciona **Pipeline**
4. Click **OK**

5. En **Build Triggers**, selecciona:
   - **Poll SCM**: `H/5 * * * *` (revisa cada 5 minutos)
   - O configura **GitHub hook trigger** si usas webhooks

6. En **Pipeline**:
   - **Definition**: Pipeline script from SCM
   - **SCM**: Git
   - **Repository URL**: URL de tu repositorio
   - **Credentials**: Tus credenciales de Git
   - **Branch Specifier**: `*/main` (o la rama que uses)
   - **Script Path**: `client-service/Jenkinsfile` ⚠️ **Importante: apunta al Jenkinsfile del servicio específico**

7. Click **Save**

8. Repite estos pasos para cada uno de los 9 microservicios:
   - `config-service-pipeline` → Script Path: `config-service/Jenkinsfile`
   - `eureka-service-pipeline` → Script Path: `eureka-service/Jenkinsfile`
   - `gateway-service-pipeline` → Script Path: `gateway-service/Jenkinsfile`
   - `kardex-service-pipeline` → Script Path: `kardex-service/Jenkinsfile`
   - `loan-service-pipeline` → Script Path: `loan-service/Jenkinsfile`
   - `price-service-pipeline` → Script Path: `price-service/Jenkinsfile`
   - `report-service-pipeline` → Script Path: `report-service/Jenkinsfile`
   - `tool-service-pipeline` → Script Path: `tool-service/Jenkinsfile`

### Ventaja de esta Opción

- ✅ Cada servicio tiene su propio historial de builds
- ✅ Puedes ejecutar builds individuales manualmente
- ✅ Más control sobre cuando construir cada servicio
- ✅ Fácil de entender qué servicio está fallando

### Desventaja

- ❌ Más configuración inicial (9 pipelines)
- ❌ Todos los servicios se construirán en cada push (a menos que configures path filtering)

## Configuración de Webhooks

Para builds instantáneos cuando haces push al repositorio:

### GitHub

1. Ve a tu repositorio en GitHub
2. **Settings** → **Webhooks** → **Add webhook**
3. Configura:
   - **Payload URL**: `http://TU_JENKINS_URL/github-webhook/`
   - **Content type**: application/json
   - **Which events**: Just the push event
4. Click **Add webhook**

### GitLab

1. Ve a tu repositorio en GitLab
2. **Settings** → **Webhooks**
3. Configura:
   - **URL**: `http://TU_JENKINS_URL/project/TU_JOB_NAME`
   - **Trigger**: Push events
4. Click **Add webhook**

> [!TIP]
> Si Jenkins está detrás de un firewall, considera usar herramientas como ngrok para exponerlo temporalmente o configurar polling SCM.

## Solución de Problemas

### Problema: "docker: command not found"

**Causa**: Docker no está instalado en el agente de Jenkins o no está en el PATH.

**Solución**:
```bash
# Verifica si Docker está instalado
docker --version

# Si no está instalado, instálalo
sudo apt-get update
sudo apt-get install docker.io

# Agrega el usuario de Jenkins al grupo docker
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### Problema: "Permission denied while trying to connect to Docker daemon"

**Causa**: El usuario de Jenkins no tiene permisos para usar Docker.

**Solución**:
```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### Problema: "Credentials not found: docker-credentials"

**Causa**: Las credenciales no están configuradas o tienen un ID diferente.

**Solución**:
1. Verifica que las credenciales existan en **Manage Jenkins** → **Manage Credentials**
2. El **ID** debe ser exactamente `docker-credentials`
3. Si usas otro ID, actualiza la variable `DOCKERHUB_CREDENTIALS` en cada Jenkinsfile

### Problema: Build falla en "mvn clean package"

**Causa**: Maven no está instalado o Java version incorrecta.

**Solución**:
```bash
# Verifica versiones
mvn --version
java --version

# Asegúrate de tener Java 17
sudo apt-get install openjdk-17-jdk
```

### Problema: "No such image" al hacer push

**Causa**: La imagen no se construyó correctamente.

**Solución**:
1. Revisa los logs del stage "Build Docker Image"
2. Verifica que el Dockerfile existe en el directorio del servicio
3. Ejecuta manualmente:
   ```bash
   cd client-service
   docker build -t test:latest .
   ```

### Problema: No se detectan cambios automáticamente

**Causa**: Webhooks no configurados o polling deshabilitado.

**Solución**:
- Configura webhooks (ver sección de Webhooks)
- O habilita **Poll SCM** con: `H/5 * * * *`

## Verificación Final

Para verificar que todo funciona:

1. **Haz un cambio en un servicio**:
   ```bash
   cd client-service/src/main/java/com/example/clientservice
   # Agrega un comentario en cualquier archivo .java
   git add .
   git commit -m "test: trigger Jenkins build"
   git push
   ```

2. **Observa Jenkins**:
   - El job correspondiente debería iniciarse automáticamente
   - Deberías ver las etapas: Checkout → Build Maven → Build Docker → Login → Push → Cleanup

3. **Verifica Docker Hub**:
   - Ve a https://hub.docker.com/u/diegoraar
   - Verifica que apareció una nueva versión de `client-service`
   - Deberías ver los tags: `latest` y el número de build (ej: `42`)

## Recursos Adicionales

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Docker Pipeline Plugin](https://plugins.jenkins.io/docker-workflow/)
- [Jenkins Credentials](https://www.jenkins.io/doc/book/using/using-credentials/)

---

**¡Felicitaciones!** 🎉 Tu sistema de CI/CD está configurado y listo para automatizar el proceso de construcción y despliegue de tus microservicios.
