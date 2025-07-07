package io.github.tasoula.service;

import io.github.tasoula.repository.interfaces.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    @Value("${upload.images.dir}") // свойство в application.properties
    private String uploadDir;
    private final ImageRepository repository;

    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    public Optional<Resource> getImage(UUID postId) {
        String fileName = repository.getPostImage(postId);
        if (fileName == null) {
            return Optional.empty();
        }

        Path filePath = Paths.get(uploadDir, fileName);
        return Optional.of(new FileSystemResource(filePath.toFile()));
    }
}
