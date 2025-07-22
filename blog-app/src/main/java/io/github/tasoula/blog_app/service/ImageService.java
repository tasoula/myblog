package io.github.tasoula.blog_app.service;

import io.github.tasoula.blog_app.repository.interfaces.ImageRepository;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Value("${upload.images.dir}")
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

    @Transactional //так же, как и в TagService, оставлю на случай изменений
    public void updatePostImage(UUID postId, MultipartFile image) {
        if (image == null || image.isEmpty())
            return;

        String oldImagePath = repository.getPostImage(postId);

        String imagePath = UUID.randomUUID() + "_" + image.getOriginalFilename();
        repository.updatePostImage(postId, imagePath);

        saveToDisc(image, imagePath);
        deleteImageFromDisk(oldImagePath);
    }

    public String getImagePath(UUID postId){
        return repository.getPostImage(postId);
    }

    public void saveToDisc(MultipartFile file, String imagePath) {
        if (uploadDir == null) {
            throw new IllegalStateException("Требуется задать каталог для сохранения изображений поста");
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. Сохранить файл на диск
        Path filePath = Paths.get(uploadDir, imagePath);
        try {
            Files.copy(file.getInputStream(), filePath);
        }
        catch (IOException e){
            log.error("Не удалось загрузить изобраение: {}", e.getMessage(), e);
        }
    }

    public void deleteImageFromDisk(String imagePath){
        Path filePath = Paths.get(uploadDir, imagePath);

        if (!Files.exists(filePath))
            return;

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Не удалось удалить файл: {}", e.getMessage(), e);
        }
    }
}
