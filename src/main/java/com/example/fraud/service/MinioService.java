package com.example.fraud.service;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

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

    public void uploadImage(String objectName, byte[] content, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            System.err.println("Erro ao fazer upload para o MinIO: " + e.getMessage());
        }
    }
}
