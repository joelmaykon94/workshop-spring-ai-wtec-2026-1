<div align="center">

<img src="arquitetura.jpg" alt="Workshop Spring AI Banner" width="100%" />

<br/>

[![Follow on LinkedIn](https://img.shields.io/badge/Follow%20on-LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/joelmaykon)
[![Follow on Instagram](https://img.shields.io/badge/Follow%20on-Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/joelmaykon94)

[Português](#-português) | [English](#-english) | [Español](#-español)

</div>

---

<h2 id="-português">🇧🇷 Português</h2>

# 🌟 Workshop Spring AI - WTEC 2026

**Agentes de Inteligência Artificial para Detecção de Fraudes — Clone, personalize, execute.** <br>
*Detecção de Fraudes com LLMs, RAG, Visão Computacional e Agentes Autônomos em Spring Boot.*

<div align="center">

[![Stars](https://img.shields.io/github/stars/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FDD835&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/stargazers)
[![Forks](https://img.shields.io/github/forks/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00BFFF&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/network/members)
[![Contributors](https://img.shields.io/github/contributors/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00FF7F&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache--2.0-8A2BE2?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

<br/>

[![🚀 Quick Start](https://img.shields.io/badge/🚀_Quick_Start-f3f4f6?style=flat-square)](#-como-iniciar-a-prática-passo-a-passo)
[![📁 Browse Templates](https://img.shields.io/badge/📁_Browse_Templates-f3f4f6?style=flat-square)](#-o-desafio-hands-on)
[![📚 Step-by-Step Tutorials](https://img.shields.io/badge/📚_Step--by--Step_Tutorials-f3f4f6?style=flat-square)](#-o-desafio-hands-on)

<br/>

[![GitHub Trending](https://img.shields.io/badge/🏆_GITHUB_TRENDING-%231_Repository_Of_The_Day-f3f4f6?style=for-the-badge&color=white&labelColor=black)]()

</div>

## 💡 Por que este projeto existe?
Você não precisaria ter que reconstruir o mesmo pipeline RAG, loop de agente ou integração com ferramentas do zero a cada novo projeto corporativo com LLMs.
**O Workshop Spring AI - WTEC 2026** é um "cookbook" de arquiteturas avançadas pronto para uso - código base que você pode clonar, personalizar e aplicar em produção. O objetivo é demonstrar a transição de APIs CRUD tradicionais para **Agentes Cognitivos Multimodais** capazes de correlacionar metadados, fotos de cupons fiscais e áudios de autorização.
* 🛠️ **Construído à mão, não gerado** - Cada fluxo de agente é original e reflete um caso de uso real testado de ponta a ponta.
* 🚀 **Roda em poucos comandos** - Sem "se vira aí" para subir a infraestrutura. Tudo automatizado no Dev Containers e Docker Compose.
* 🧠 **Cobre a stack de IA moderna** - Agentes Cognitivos, Ferramentas de IA (Tools/Functions), Visão Multimodal, Áudio, RAG (Vector Stores) e Observabilidade com Langfuse.

## 🚀 Ambiente (Docker / Dev Containers)
Este projeto foi otimizado para rodar localmente utilizando Docker Compose ou VS Code Dev Containers. A infraestrutura subirá **automaticamente** os serviços de banco e mensageria (PostgreSQL, Kafka, MinIO e Langfuse).
1. **Como verificar se os containers subiram:** `docker ps`
2. **Configurando a API da IA (Google Gemini 1.5 Flash):** Crie uma chave de API grátis no [Google AI Studio](https://aistudio.google.com/app/apikey).
3. **Obtendo as chaves do Langfuse:** Acesse `http://localhost:3000` e crie seu projeto.
4. **Configurando Variáveis:** Copie `.env.example` para `.env` e preencha as chaves.
5. **Executando:** `./mvnw spring-boot:run`

---
<br/>

<h2 id="-english">🇺🇸 English</h2>

# 🌟 Workshop Spring AI - WTEC 2026

**100+ AI Agents & RAG apps you can actually run — clone, customize, ship.** <br>
*Fraud Detection with LLMs, RAG, Computer Vision, and Autonomous Agents in Spring Boot.*

<div align="center">

[![Stars](https://img.shields.io/github/stars/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FDD835&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/stargazers)
[![Forks](https://img.shields.io/github/forks/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00BFFF&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/network/members)
[![Contributors](https://img.shields.io/github/contributors/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00FF7F&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache--2.0-8A2BE2?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

</div>

## 💡 Why does this project exist?
You shouldn't have to rebuild the same RAG pipeline, agent loop, or tool integration from scratch for every new enterprise LLM project.
**The Spring AI - WTEC 2026 Workshop** is a ready-to-use "cookbook" of advanced architectures - starter code you can fork, customize, and ship to production. The goal is to demonstrate the transition from traditional CRUD APIs to **Multimodal Cognitive Agents** capable of correlating metadata, receipt photos, and voice authorizations.
* 🛠️ **Hand-built, not generated** - Each agent flow is original and reflects a real, end-to-end tested use case.
* 🚀 **Runs in a few commands** - No "figure it out yourself" infrastructure setup. Everything is automated with Dev Containers and Docker Compose.
* 🧠 **Covers the modern AI stack** - Cognitive Agents, AI Tools/Functions, Multimodal Vision, Audio, RAG (Vector Stores), and Observability with Langfuse.

## 🚀 Environment (Docker / Dev Containers)
This project has been optimized to run locally using Docker Compose or VS Code Dev Containers. The infrastructure will **automatically** start the database and messaging services (PostgreSQL, Kafka, MinIO, and Langfuse).
1. **How to check if containers are up:** `docker ps`
2. **Configuring AI API (Google Gemini 1.5 Flash):** Create a free API key at [Google AI Studio](https://aistudio.google.com/app/apikey).
3. **Getting Langfuse keys:** Access `http://localhost:3000` and create your project.
4. **Environment Variables:** Copy `.env.example` to `.env` and fill the keys.
5. **Running:** `./mvnw spring-boot:run`

---
<br/>

<h2 id="-español">🇪🇸 Español</h2>

# 🌟 Workshop Spring AI - WTEC 2026

**100+ AI Agents & RAG apps you can actually run — clone, customize, ship.** <br>
*Detección de Fraudes con LLMs, RAG, Visión Computacional y Agentes Autónomos en Spring Boot.*

<div align="center">

[![Stars](https://img.shields.io/github/stars/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FDD835&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/stargazers)
[![Forks](https://img.shields.io/github/forks/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00BFFF&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/network/members)
[![Contributors](https://img.shields.io/github/contributors/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00FF7F&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache--2.0-8A2BE2?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

</div>

## 💡 ¿Por qué existe este proyecto?
No deberías tener que reconstruir el mismo pipeline RAG, ciclo de agente o integración con herramientas desde cero para cada nuevo proyecto corporativo con LLMs.
**El Workshop Spring AI - WTEC 2026** es un "libro de cocina" (cookbook) de arquitecturas avanzadas listo para usar: código base que puedes clonar, personalizar y aplicar en producción. El objetivo es demostrar la transición de las API CRUD tradicionales a **Agentes Cognitivos Multimodales** capaces de correlacionar metadatos, fotos de recibos fiscales y audios de autorización.
* 🛠️ **Construido a mano, no generado** - Cada flujo de agente es original y refleja un caso de uso real probado de principio a fin.
* 🚀 **Se ejecuta en pocos comandos** - Sin tener que adivinar cómo levantar la infraestructura. Todo está automatizado con Dev Containers y Docker Compose.
* 🧠 **Cubre el stack moderno de IA** - Agentes Cognitivos, Herramientas de IA (Tools/Functions), Visión Multimodal, Audio, RAG (Vector Stores) y Observabilidad con Langfuse.

## 🚀 Entorno (Docker / Dev Containers)
Este proyecto ha sido optimizado para ejecutarse localmente utilizando Docker Compose o VS Code Dev Containers. La infraestructura levantará **automáticamente** los servicios de base de datos y mensajería (PostgreSQL, Kafka, MinIO y Langfuse).
1. **Cómo verificar si los contenedores se levantaron:** `docker ps`
2. **Configurando la API de IA (Google Gemini 1.5 Flash):** Crea una clave de API gratuita en [Google AI Studio](https://aistudio.google.com/app/apikey).
3. **Obteniendo las claves de Langfuse:** Accede a `http://localhost:3000` y crea tu proyecto.
4. **Variables de Entorno:** Copia `.env.example` a `.env` y completa las claves.
5. **Ejecutando:** `./mvnw spring-boot:run`
