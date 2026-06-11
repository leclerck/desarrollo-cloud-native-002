package cl.duoc.transporte.service;

import cl.duoc.transporte.model.Guia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EfsService {

    @Value("${efs.path}")
    private String efsPath;

    public String saveTemporarily(MultipartFile file, Guia guia) {
        Path directory = Paths.get(efsPath, guia.getTransportista(), guia.getFecha().toString());
        try {
            Files.createDirectories(directory);
            Path destination = directory.resolve(guia.getNumeroGuia() + ".pdf");
            file.transferTo(destination.toFile());
            return destination.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file to EFS", e);
        }
    }
}
