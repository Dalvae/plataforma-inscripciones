package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3StorageServiceTest {

    @Mock
    private S3Client s3Client;

    private S3StorageService service;

    @BeforeEach
    void setUp() {
        service = new S3StorageService(s3Client);
        ReflectionTestUtils.setField(service, "bucket", "mi-bucket");
    }

    @Test
    void subirEnviaPutObjectConBucketYKey() {
        service.subir("5/resumen-5.pdf", new byte[]{1, 2, 3}, "application/pdf");

        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture(), any(RequestBody.class));
        assertEquals("mi-bucket", captor.getValue().bucket());
        assertEquals("5/resumen-5.pdf", captor.getValue().key());
    }

    @Test
    void descargaDevuelveBytes() {
        ResponseBytes<GetObjectResponse> bytes = ResponseBytes.fromByteArray(
                GetObjectResponse.builder().build(), new byte[]{9, 8, 7});
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(bytes);

        byte[] result = service.descargar("5/resumen-5.pdf");
        assertArrayEquals(new byte[]{9, 8, 7}, result);
    }

    @Test
    void descargaLanzaNoSuchElementSiNoExiste() {
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class)))
                .thenThrow(NoSuchKeyException.builder().build());
        assertThrows(NoSuchElementException.class, () -> service.descargar("5/resumen-5.pdf"));
    }

    @Test
    void borrarEnviaDeleteObject() {
        service.borrar("5/resumen-5.pdf");
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void existeTrueCuandoHeadOk() {
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(null);
        assertTrue(service.existe("5/resumen-5.pdf"));
    }

    @Test
    void existeFalseCuandoNoSuchKey() {
        when(s3Client.headObject(any(HeadObjectRequest.class)))
                .thenThrow(NoSuchKeyException.builder().build());
        assertFalse(service.existe("5/resumen-5.pdf"));
    }
}
