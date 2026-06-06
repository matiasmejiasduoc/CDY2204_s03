package cl.duoc.gestion_guias.service;

import cl.duoc.gestion_guias.model.Guide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EfsService {

    @Value("${efs.mount.path}")
    private String efsMountPath;

    public String saveTemporarily(MultipartFile file, Guide guide) {
        Path directory = Paths.get(efsMountPath, guide.getCarrier(), guide.getDate().toString());
        try {
            Files.createDirectories(directory);
            Path destination = directory.resolve(guide.getGuideNumber() + ".pdf");
            file.transferTo(destination.toFile());
            return destination.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file to EFS", e);
        }
    }
}
