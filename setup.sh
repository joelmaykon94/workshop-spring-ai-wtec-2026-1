#!/bin/bash
echo "🚀 Iniciando automação de Setup do Workshop..."

# 1. Copiar arquivo .env
if [ ! -f .env ]; then
    echo "📄 Criando arquivo .env a partir do .env.example..."
    cp .env.example .env
else
    echo "✅ Arquivo .env já existe."
fi

# 2. Subir o Docker Compose
echo "🐳 Subindo infraestrutura no Docker (Postgres, MinIO, Kafka, Langfuse)..."
docker compose up -d

echo ""
echo "========================================================"
echo "🎉 Setup Base Concluído!"
echo "========================================================"
echo "⚠️  ATENÇÃO: Abra o arquivo '.env' e cole suas chaves do Google Gemini e do Langfuse."
echo "⏳ Os painéis estão disponíveis em:"
echo "   - Langfuse: http://localhost:3000"
echo "   - MinIO S3: http://localhost:9001 (minioadmin/minioadmin)"
echo ""
echo "▶️  Para iniciar a aplicação, rode: ./mvnw spring-boot:run"
echo "========================================================"
