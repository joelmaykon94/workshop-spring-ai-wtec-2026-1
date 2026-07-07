@echo off
echo ========================================================
echo 🚀 Iniciando automacao de Setup do Workshop...
echo ========================================================

:: 1. Copiar arquivo .env
if not exist ".env" (
    echo 📄 Criando arquivo .env a partir do .env.example...
    copy .env.example .env >nul
) else (
    echo ✅ Arquivo .env ja existe.
)

:: 2. Subir o Docker Compose
echo 🐳 Subindo infraestrutura no Docker (Postgres, MinIO, Kafka, Langfuse)...
docker compose up -d

echo.
echo ========================================================
echo 🎉 Setup Base Concluido!
echo ========================================================
echo ⚠️  ATENCAO: Abra o arquivo '.env' e cole suas chaves do Google Gemini e do Langfuse.
echo ⏳ Os paineis estao disponiveis em:
echo    - Langfuse: http://localhost:3000
echo    - MinIO S3: http://localhost:9001 (minioadmin/minioadmin)
echo.
echo ▶️  Para iniciar a aplicacao, rode: .\mvnw.cmd spring-boot:run
echo ========================================================
pause
