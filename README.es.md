<div align="center">

<img src="arquitetura.jpg" alt="Workshop Spring AI Banner" width="100%" />

<br/>

[![Follow on LinkedIn](https://img.shields.io/badge/Follow%20on-LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/joelmaykon)
[![Follow on Instagram](https://img.shields.io/badge/Follow%20on-Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/joelmaykon94)

[Português](README.md) | [English](README.en.md) | [Español](README.es.md)

<br/>

# 🌟 Workshop Spring AI - WTEC 2026

**100+ AI Agents & RAG apps you can actually run — clone, customize, ship.** <br>
*Detección de Fraudes con LLMs, RAG, Visión Computacional y Agentes Autónomos en Spring Boot.*

[![Stars](https://img.shields.io/github/stars/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FDD835&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/stargazers)
[![Forks](https://img.shields.io/github/forks/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00BFFF&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/network/members)
[![Contributors](https://img.shields.io/github/contributors/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00FF7F&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache--2.0-8A2BE2?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)
[![Last Commit](https://img.shields.io/github/last-commit/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FF4500)](#)

<br/>

[![🚀 Quick Start](https://img.shields.io/badge/🚀_Quick_Start-f3f4f6?style=flat-square)](#-cómo-iniciar-la-práctica-paso-a-paso)
[![📁 Browse Templates](https://img.shields.io/badge/📁_Browse_Templates-f3f4f6?style=flat-square)](#-el-desafío-hands-on)
[![📚 Step-by-Step Tutorials](https://img.shields.io/badge/📚_Step--by--Step_Tutorials-f3f4f6?style=flat-square)](#-el-desafío-hands-on)

<br/>

[![GitHub Trending](https://img.shields.io/badge/🏆_GITHUB_TRENDING-%231_Repository_Of_The_Day-f3f4f6?style=for-the-badge&color=white&labelColor=black)]()

</div>

---

## 💡 ¿Por qué existe este proyecto?

No deberías tener que reconstruir el mismo pipeline RAG, ciclo de agente o integración con herramientas desde cero para cada nuevo proyecto corporativo con LLMs.

**El Workshop Spring AI - WTEC 2026** es un "libro de cocina" (cookbook) de arquitecturas avanzadas listo para usar: código base que puedes clonar, personalizar y aplicar en producción. El objetivo es demostrar la transición de las API CRUD tradicionales a **Agentes Cognitivos Multimodales** capaces de correlacionar metadatos, fotos de recibos fiscales y audios de autorización.

* 🛠️ **Construido a mano, no generado** - Cada flujo de agente es original y refleja un caso de uso real probado de principio a fin.
* 🚀 **Se ejecuta en pocos comandos** - Sin tener que adivinar cómo levantar la infraestructura. Todo está automatizado con Dev Containers y Docker Compose.
* 🧠 **Cubre el stack moderno de IA** - Agentes Cognitivos, Herramientas de IA (Tools/Functions), Visión Multimodal, Audio, RAG (Vector Stores) y Observabilidad con Langfuse.

---

## 🚀 Entorno (Docker / Dev Containers)

Este proyecto ha sido optimizado para ejecutarse localmente utilizando Docker Compose o VS Code Dev Containers. La infraestructura levantará **automáticamente** los servicios de base de datos y mensajería (PostgreSQL, Kafka, MinIO y Langfuse).

1. **Cómo verificar si los contenedores se levantaron:**
   Abre tu terminal local y ejecuta:
   ```bash
   docker ps
   ```

2. **Configurando la API de IA (Google Gemini 1.5 Flash):**
   Para mantener el entorno de desarrollo extremadamente rápido y liviano, el proyecto está configurado para usar la API gratuita de Google Gemini en lugar del procesamiento de modelos locales en GPU.
   - Crea una clave de API gratuita en [Google AI Studio](https://aistudio.google.com/app/apikey).
   
3. **Obteniendo las claves de Langfuse:**
   - Accede al panel de Langfuse en `http://localhost:3000`.
   - Crea una cuenta de prueba inicial para iniciar sesión.
   - En el flujo de bienvenida, crea una organización (ej: `org-test`) y luego un proyecto (ej: `proyecto-test`).
   - Se mostrará la pantalla inicial de "Setup Tracing" con la opción de copiar la **Public Key**, **Secret Key** y **Host**.

4. **Configurando las Variables de Entorno:**
   - En la raíz del proyecto, duplica el archivo `.env.example` y renómbralo a `.env`.
   - Abre el archivo `.env` y completa las variables de entorno con las claves obtenidas (`OPENAI_API_KEY` de Google AI Studio, y las claves `LANGFUSE_PUBLIC_KEY` y `LANGFUSE_SECRET_KEY` de Langfuse).

5. **Ejecutando la Aplicación:**
   - Con el archivo `.env` configurado y guardado, simplemente inicia la aplicación desde la terminal:
   ```bash
   ./mvnw spring-boot:run
   ```

*(¡El proyecto ya está configurado para apuntar automáticamente a `localhost` para conectarse a Postgres, Kafka y MinIO de forma transparente!)*

---

## 📊 Dashboards Web

Con los contenedores activos, accede a los paneles en tu navegador:
*   **Langfuse (http://localhost:3000):** Panel de Observabilidad de los Prompts de la IA. Crea una cuenta local y genera tus claves en Settings > API Keys.
*   **MinIO (http://localhost:9001):** Nuestro "S3" local. Login: `minioadmin / minioadmin`.

---

## 🚀 Cómo Iniciar la Práctica (Paso a Paso)

Para asegurar que puedas programar y guardar tus cambios sin problemas de permisos, sigue el orden a continuación:

### Paso 1: Hacer Fork del Repositorio (Obligatorio)
1. Estando en esta página, haz clic en el botón **Fork** en la esquina superior derecha de GitHub.
2. Asegúrate de **desmarcar** la opción *"Copy the main branch only"* (Copiar solo la rama main), ya que necesitaremos tanto la rama `main` como la rama `solucao` durante la práctica.
3. Esto creará una copia idéntica de este proyecto en tu cuenta personal de GitHub (ej: `github.com/tu-usuario/workshop-spring-ai-wtec-2026-1`).

### Paso 2: Inicializar el Entorno Local
1. Clona el repositorio a tu máquina local (ej: `git clone https://github.com/tu-usuario/workshop-spring-ai-wtec-2026-1.git`).
2. Abre la carpeta del proyecto en **Visual Studio Code**.
3. Si tienes la extensión **Dev Containers** instalada, aparecerá un aviso: **"Reopen in Container"**. Haz clic en él. ¡Esto configurará Java 21 y las extensiones en VS Code sin necesidad de instalar nada en tu máquina física!

---

## 🛠️ Orquestación Automática

Para levantar la infraestructura de base de datos y mensajería, ejecuta el siguiente comando en la raíz del proyecto:
```bash
docker compose up -d
```
Esto iniciará:
- **PostgreSQL** con la extensión `pgvector` (Para RAG).
- **MinIO** (Almacenamiento S3 para recibos).
- **Kafka** (Mensajería asíncrona para la orquestación de la Saga).
- **Langfuse** (Panel web para observabilidad de la Inteligencia Artificial).

---

## 🧪 Probando la API (cURL)

Para facilitar las pruebas prácticas durante el taller, preparamos una carpeta `test-data/` que contiene diferentes escenarios de transacciones y recibos reales (anonimizados).

### Setup de MinIO (Obligatorio)
Antes de ejecutar las pruebas, es necesario enviar las imágenes al bucket `fraud-images` en MinIO (para simular la subida desde una app). Ejecuta los comandos a continuación en una terminal:
```bash
docker cp test-data/itau_uni.png fraud-minio:/tmp/
docker exec fraud-minio mc cp /tmp/itau_uni.png myminio/fraud-images/itau_uni.png

docker cp test-data/receipt_b_dot.png fraud-minio:/tmp/
docker exec fraud-minio mc cp /tmp/receipt_b_dot.png myminio/fraud-images/receipt_b_dot.png
```

### 1. Escenario: Transacción Legítima
Prueba una transacción normal, haciendo referencia al recibo `itau_uni.png`. El Agente IA debe deducir que no es un fraude.
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/payload-itau-legitimo.json http://localhost:8080/api/fraud/analyze
```

### 2. Escenario: Transacción Fraudulenta
Prueba una transacción sospechosa, haciendo referencia a `receipt_b_dot.png`. El Agente IA debe bloquearla (isFraud: true) y activar la Saga de Compensación.
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/payload-receipt-falso.json http://localhost:8080/api/fraud/analyze
```

### 3. Sembrando Datos en el RAG (Vector Store)
Para popular el historial de conocimiento de la aplicación con una transacción:
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/test-payload.json http://localhost:8080/api/fraud/seed
```

---

## 📝 El Desafío Hands-on
El repositorio en la rama `main` contiene la estructura de controladores y modelos lista, pero las reglas de búsqueda vectorial y la inyección multimodal están vacías. Tu objetivo durante el mini-curso es:
1. Implementar la búsqueda semántica en **`VectorSearchService.java`**.
2. Configurar el **`ChatClient`** con Advisors de memoria/RAG y Herramientas (Tools) en **`FraudDetectionAgent.java`**.
3. Construir el flujo multimodal de procesamiento de imágenes de recibos y audio en el agente.

Si necesitas ayuda o te quedas atascado, consulta el código completo en la rama **`solucao`**.

---

## 📚 Referencias y Documentaciones

Para profundizar durante y después del taller, consulta la documentación oficial de las herramientas que utilizamos:

*   **Spring AI**
    *   [Visión General de Spring AI](https://spring.io/projects/spring-ai#overview)
    *   [API de ChatClient (Prompts y Llamadas)](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
    *   [Bases de Datos Vectoriales (PGVector)](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
    *   [Multimodalidad (Visión y Audio)](https://docs.spring.io/spring-ai/reference/api/multimodality.html)
*   **Inteligencia Artificial**
    *   [Google AI Studio (Gemini API)](https://aistudio.google.com/)
*   **Infraestructura y Observabilidad**
    *   [Langfuse (LLM Observability)](https://langfuse.com/docs)
    *   [Apache Kafka (Saga Pattern)](https://kafka.apache.org/documentation/)
    *   [MinIO (Object Storage S3)](https://min.io/docs/minio/linux/index.html)
