package io.github.tasoula.service;

import io.github.tasoula.repository.interfaces.ImageRepository;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    @Value("${upload.images.dir}") // свойство в application.properties
    private String uploadDir;

    @Autowired
    private ServletContext servletContext;
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

    public String saveToDisc(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        if (uploadDir == null) {
            throw new IllegalStateException("Требуется задать каталог для сохранения изображений поста");
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. Сохранить файл на диск
        Path filePath = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), filePath);

        return filename;
    }

    public void updatePostImage(UUID postId, MultipartFile image) {
        if (image == null || image.isEmpty())
            return;

        try {
            deletePostImage(postId);
            String filename = saveToDisc(image);
            repository.updatePostImage(postId, filename); // Сохраняем путь

        } catch (IOException e) {
            log.error("Произошла ошибка: {}", e.getMessage(), e);
        }
    }

    public void deletePostImage(UUID postId) {
        //удалить картинку из хранилища
        String imagePath = repository.getPostImage(postId);
        if (imagePath == null)
            return;

        Path filePath = Paths.get(uploadDir, imagePath);

        if (!Files.exists(filePath))
            return;

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Произошла ошибка: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
