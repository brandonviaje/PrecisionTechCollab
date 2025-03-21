package com.precisiontech.moviecatalog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveImageTest {

    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        mockMultipartFile = mock(MultipartFile.class);
    }

    @Test
    void testSaveImageSuccessfully() throws IOException {
        String originalFileName = "poster.jpg";
        String expectedPath = "/userimg/" + originalFileName;

        // Mock MultipartFile behavior
        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFileName);

        // Mock file transfer behavior
        Path path = Paths.get("src", "main", "resources", "static", "userimg", originalFileName);
        Files.createDirectories(path.getParent());

        String result = SaveImage.saveImage(mockMultipartFile);

        // Assert
        assertEquals(expectedPath, result);
        verify(mockMultipartFile, times(1)).transferTo(any(Path.class));
    }

    @Test
    void testSaveImageThrowsIOException() throws IOException {
        String originalFileName = "poster.jpg";
        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFileName);

        // Simulate IOException during file transfer
        doThrow(new IOException("Error saving file")).when(mockMultipartFile).transferTo(any(Path.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> SaveImage.saveImage(mockMultipartFile));  // Accessing via class name
        assertTrue(thrown.getMessage().contains("Error saving image"));
    }
}
