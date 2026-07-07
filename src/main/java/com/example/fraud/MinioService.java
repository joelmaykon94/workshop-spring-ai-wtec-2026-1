package com.example.fraud;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey) {
        
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * Recupera a imagem do comprovante ou documento do S3/MinIO.
     * @param objectName O nome do arquivo salvo no bucket.
     * @return Os bytes da imagem para passar ao Llama 3.2-Vision.
     */
    public byte[] getImageBytes(String objectName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {
            
            return stream.readAllBytes();
        } catch (Exception e) {
            // Em produção deve haver um log e tratamento de erro melhor
            System.err.println("Erro ao baixar imagem do MinIO: " + e.getMessage());
            return null;
        }
    }
}
