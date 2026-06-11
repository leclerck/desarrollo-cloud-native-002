package cl.duoc.transporte.service;

import cl.duoc.transporte.dto.GuiaRequestDTO;
import cl.duoc.transporte.dto.GuiaResponseDTO;
import cl.duoc.transporte.exception.GuiaNotFoundException;
import cl.duoc.transporte.model.Guia;
import cl.duoc.transporte.model.GuiaStatus;
import cl.duoc.transporte.repository.GuiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuiaService {

    private final GuiaRepository guiaRepository;
    private final S3Service s3Service;
    private final EfsService efsService;

    public GuiaResponseDTO create(GuiaRequestDTO dto) {
        Guia guia = new Guia();
        guia.setNumeroGuia(dto.getNumeroGuia());
        guia.setTransportista(dto.getTransportista());
        guia.setFecha(dto.getFecha());
        guia.setDestinatario(dto.getDestinatario());
        guia.setDireccionDestino(dto.getDireccionDestino());
        guia.setDescripcionCarga(dto.getDescripcionCarga());
        guia.setStatus(GuiaStatus.PENDING);
        return toDTO(guiaRepository.save(guia));
    }

    public GuiaResponseDTO findById(Long id) {
        return toDTO(guiaRepository.findById(id).orElseThrow(() -> new GuiaNotFoundException(id)));
    }

    public GuiaResponseDTO update(Long id, GuiaRequestDTO dto) {
        Guia guia = getOrThrow(id);
        guia.setNumeroGuia(dto.getNumeroGuia());
        guia.setTransportista(dto.getTransportista());
        guia.setFecha(dto.getFecha());
        guia.setDestinatario(dto.getDestinatario());
        guia.setDireccionDestino(dto.getDireccionDestino());
        guia.setDescripcionCarga(dto.getDescripcionCarga());
        return toDTO(guiaRepository.save(guia));
    }

    public void delete(Long id) {
        Guia guia = getOrThrow(id);
        guia.setStatus(GuiaStatus.DELETED);
        guiaRepository.save(guia);
    }

    public GuiaResponseDTO uploadToS3(Long id, MultipartFile file) {
        Guia guia = getOrThrow(id);

        String efsPath = efsService.saveTemporarily(file, guia);
        guia.setRutaEfs(efsPath);

        String s3Key = s3Service.upload(file, guia);
        guia.setS3Key(s3Key);
        guia.setStatus(GuiaStatus.UPLOADED);

        return toDTO(guiaRepository.save(guia));
    }

    public byte[] download(Long id, String requester) {
        Guia guia = getOrThrow(id);
        if (guia.getStatus() != GuiaStatus.UPLOADED) {
            throw new IllegalStateException("Guía no disponible en S3");
        }
        return s3Service.download(guia.getS3Key(), requester);
    }

    public List<GuiaResponseDTO> findByTransportistaAndFecha(String transportista, LocalDate fecha) {
        return guiaRepository.findByTransportistaAndFecha(transportista, fecha)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Guia getOrThrow(Long id) {
        return guiaRepository.findById(id)
                .orElseThrow(() -> new GuiaNotFoundException(id));
    }

    private GuiaResponseDTO toDTO(Guia guia) {
        GuiaResponseDTO dto = new GuiaResponseDTO();
        dto.setId(guia.getId());
        dto.setNumeroGuia(guia.getNumeroGuia());
        dto.setTransportista(guia.getTransportista());
        dto.setFecha(guia.getFecha());
        dto.setDestinatario(guia.getDestinatario());
        dto.setDireccionDestino(guia.getDireccionDestino());
        dto.setDescripcionCarga(guia.getDescripcionCarga());
        dto.setStatus(guia.getStatus());
        dto.setS3Key(guia.getS3Key());
        return dto;
    }
}
