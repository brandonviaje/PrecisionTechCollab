package com.precisiontech.moviecatalog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SaveImage {

    /**
     * Saves the cover image of the movie to the project structure
     *
     * @param poster    the image being saved
     * @return the file path of the image
     */
    public static String saveImage(MultipartFile poster) {
        try {
            String imageName = poster.getOriginalFilename();
            Path path = Paths.get("src", "main", "resources", "static", "userimg", imageName);
            Files.createDirectories(path.getParent());
            poster.transferTo(path);
            return "/userimg/" + imageName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage());
        }
    }

}
