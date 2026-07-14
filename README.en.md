<div align="center">

<img src="arquitetura.jpg" alt="Workshop Spring AI Banner" width="100%" />

<br/>

[![Follow on LinkedIn](https://img.shields.io/badge/Follow%20on-LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/joelmaykon)
[![Follow on Instagram](https://img.shields.io/badge/Follow%20on-Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/joelmaykon94)

[Português](README.md) | [English](README.en.md) | [Español](README.es.md)

<br/>

# 🌟 Workshop Spring AI - WTEC 2026

**100+ AI Agents & RAG apps you can actually run — clone, customize, ship.** <br>
*Fraud Detection with LLMs, RAG, Computer Vision, and Autonomous Agents in Spring Boot.*

[![Stars](https://img.shields.io/github/stars/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FDD835&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/stargazers)
[![Forks](https://img.shields.io/github/forks/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00BFFF&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/network/members)
[![Contributors](https://img.shields.io/github/contributors/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=00FF7F&logo=github&logoColor=black)](https://github.com/joelmaykon94/workshop-spring-ai/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache--2.0-8A2BE2?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)
[![Last Commit](https://img.shields.io/github/last-commit/joelmaykon94/workshop-spring-ai?style=for-the-badge&color=FF4500)](#)

<br/>

[![🚀 Quick Start](https://img.shields.io/badge/🚀_Quick_Start-f3f4f6?style=flat-square)](#-how-to-start-the-practice-step-by-step)
[![📁 Browse Templates](https://img.shields.io/badge/📁_Browse_Templates-f3f4f6?style=flat-square)](#-the-hands-on-challenge)
[![📚 Step-by-Step Tutorials](https://img.shields.io/badge/📚_Step--by--Step_Tutorials-f3f4f6?style=flat-square)](#-the-hands-on-challenge)

<br/>

[![GitHub Trending](https://img.shields.io/badge/🏆_GITHUB_TRENDING-%231_Repository_Of_The_Day-f3f4f6?style=for-the-badge&color=white&labelColor=black)]()

</div>

---

## 💡 Why does this project exist?

You shouldn't have to rebuild the same RAG pipeline, agent loop, or tool integration from scratch for every new enterprise LLM project.

**The Spring AI - WTEC 2026 Workshop** is a ready-to-use "cookbook" of advanced architectures - starter code you can fork, customize, and ship to production. The goal is to demonstrate the transition from traditional CRUD APIs to **Multimodal Cognitive Agents** capable of correlating metadata, receipt photos, and voice authorizations.

* 🛠️ **Hand-built, not generated** - Each agent flow is original and reflects a real, end-to-end tested use case.
* 🚀 **Runs in a few commands** - No "figure it out yourself" infrastructure setup. Everything is automated with Dev Containers and Docker Compose.
* 🧠 **Covers the modern AI stack** - Cognitive Agents, AI Tools/Functions, Multimodal Vision, Audio, RAG (Vector Stores), and Observability with Langfuse.

---

## 🚀 Environment (Docker / Dev Containers)

This project has been optimized to run locally using Docker Compose or VS Code Dev Containers. The infrastructure will **automatically** start the database and messaging services (PostgreSQL, Kafka, MinIO, and Langfuse).

1. **How to check if the containers are up:**
   Open your local terminal and run:
   ```bash
   docker ps
   ```

2. **Configuring the AI API (Google Gemini 1.5 Flash):**
   To keep the development environment extremely fast and lightweight, the project is configured to use the free Google Gemini API instead of processing local models on the GPU.
   - Create a free API key at [Google AI Studio](https://aistudio.google.com/app/apikey).
   
3. **Getting the Langfuse keys:**
   - Access the Langfuse dashboard at `http://localhost:3000`.
   - Create an initial test account to login.
   - In the onboarding flow, create an organization (e.g., `org-test`) and then a project (e.g., `project-test`).
   - The "Setup Tracing" start screen will be displayed with the option to copy the **Public Key**, **Secret Key**, and **Host**.

4. **Configuring the Environment Variables:**
   - At the root of the project, duplicate the `.env.example` file and rename it to `.env`.
   - Open the `.env` file and fill in the environment variables with the obtained keys (`OPENAI_API_KEY` from Google AI Studio, and the `LANGFUSE_PUBLIC_KEY` and `LANGFUSE_SECRET_KEY` keys from Langfuse).

5. **Running the Application:**
   - With the `.env` file configured and saved, simply start the application via the terminal:
   ```bash
   ./mvnw spring-boot:run
   ```

*(The project is already configured to automatically point to `localhost` to connect to Postgres, Kafka, and MinIO transparently!)*

---

## 📊 Web Dashboards

With the containers active, access the dashboards in your browser:
*   **Langfuse (http://localhost:3000):** Observability Dashboard for the AI Prompts. Create a local account and generate your keys in Settings > API Keys.
*   **MinIO (http://localhost:9001):** Our local "S3". Login: `minioadmin / minioadmin`.

---

## 🚀 How to Start the Practice (Step-by-Step)

To ensure you can code and save your changes without permission issues, follow the order below:

### Step 1: Fork the Repository (Required)
1. While on this page, click the **Fork** button in the top right corner of GitHub.
2. Make sure to **uncheck** the *"Copy the main branch only"* option, as we will need both the `main` branch and the `solucao` branch during the practice.
3. This will create an identical copy of this project in your personal GitHub account (e.g., `github.com/your-username/workshop-spring-ai-wtec-2026-1`).

### Step 2: Initialize the Local Environment
1. Clone the repository to your local machine (e.g., `git clone https://github.com/your-username/workshop-spring-ai-wtec-2026-1.git`).
2. Open the project folder in **Visual Studio Code**.
3. If you have the **Dev Containers** extension installed, a prompt will appear: **"Reopen in Container"**. Click it. This will configure Java 21 and the extensions in VS Code without needing to install anything on your physical machine!

---

## 🛠️ Automatic Orchestration

To bring up the database and messaging infrastructure, run the following command at the root of the project:
```bash
docker compose up -d
```
This will start:
- **PostgreSQL** with the `pgvector` extension (For RAG).
- **MinIO** (S3 Storage for receipts).
- **Kafka** (Asynchronous messaging for Saga orchestration).
- **Langfuse** (Web dashboard for AI observability).

---

## 🧪 Testing the API (cURL)

To facilitate practical tests during the workshop, we have prepared a `test-data/` folder containing different transaction scenarios and real receipts (anonymized).

### MinIO Setup (Required)
Before running the tests, you must upload the images to the `fraud-images` bucket in MinIO (to simulate an upload from an app). Run the following commands in a terminal:
```bash
docker cp test-data/itau_uni.png fraud-minio:/tmp/
docker exec fraud-minio mc cp /tmp/itau_uni.png myminio/fraud-images/itau_uni.png

docker cp test-data/receipt_b_dot.png fraud-minio:/tmp/
docker exec fraud-minio mc cp /tmp/receipt_b_dot.png myminio/fraud-images/receipt_b_dot.png
```

### 1. Scenario: Legitimate Transaction
Tests a normal transaction, referencing the `itau_uni.png` receipt. The AI Agent should deduce it is not a fraud.
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/payload-itau-legitimo.json http://localhost:8080/api/fraud/analyze
```

### 2. Scenario: Fraudulent Transaction
Tests a suspicious transaction, referencing `receipt_b_dot.png`. The AI Agent should block it (isFraud: true) and trigger the Compensation Saga.
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/payload-receipt-falso.json http://localhost:8080/api/fraud/analyze
```

### 3. Seeding Data in RAG (Vector Store)
To populate the application's knowledge history with a transaction:
```bash
curl -X POST -H "Content-Type: application/json" -d @test-data/test-payload.json http://localhost:8080/api/fraud/seed
```

---

## 📝 The Hands-on Challenge
The repository on the `main` branch contains the ready-made controllers and models structure, but the vector search rules and multimodal injection are empty. Your goal during the crash course is to:
1. Implement the semantic search in **`VectorSearchService.java`**.
2. Configure the **`ChatClient`** with memory/RAG Advisors and Tools in **`FraudDetectionAgent.java`**.
3. Build the multimodal processing flow for receipt images and audio in the agent.

If you need help or get stuck, check the complete code in the **`solucao`** branch.

---

## 📚 References and Documentation

For deeper learning during and after the workshop, check the official documentation of the tools we used:

*   **Spring AI**
    *   [Spring AI Overview](https://spring.io/projects/spring-ai#overview)
    *   [ChatClient API (Prompts and Calls)](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
    *   [Vector Databases (PGVector)](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
    *   [Multimodality (Vision and Audio)](https://docs.spring.io/spring-ai/reference/api/multimodality.html)
*   **Artificial Intelligence**
    *   [Google AI Studio (Gemini API)](https://aistudio.google.com/)
*   **Infrastructure and Observability**
    *   [Langfuse (LLM Observability)](https://langfuse.com/docs)
    *   [Apache Kafka (Saga Pattern)](https://kafka.apache.org/documentation/)
    *   [MinIO (Object Storage S3)](https://min.io/docs/minio/linux/index.html)
