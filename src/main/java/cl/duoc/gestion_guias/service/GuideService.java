package cl.duoc.gestion_guias.service;

import cl.duoc.gestion_guias.dto.GuideRequestDTO;
import cl.duoc.gestion_guias.dto.GuideResponseDTO;
import cl.duoc.gestion_guias.exception.GuideNotFoundException;
import cl.duoc.gestion_guias.model.Guide;
import cl.duoc.gestion_guias.model.GuideStatus;
import cl.duoc.gestion_guias.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;
    private final S3Service s3Service;
    private final PdfService pdfService;
    private final GuideProducer guideProducer;

    public GuideResponseDTO create(GuideRequestDTO dto) {
        Guide guide = new Guide();
        guide.setGuideNumber(dto.getGuideNumber());
        guide.setCarrier(dto.getCarrier());
        guide.setDate(dto.getDate());
        guide.setRecipient(dto.getRecipient());
        guide.setDestinationAddress(dto.getDestinationAddress());
        guide.setCargoDescription(dto.getCargoDescription());
        guide.setStatus(GuideStatus.PENDING);

        Guide savedGuide = guideRepository.save(guide);
        GuideResponseDTO response = toDTO(savedGuide);
        guideProducer.sendGuide(response);
        return response;
    }

    public GuideResponseDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    public GuideResponseDTO update(Long id, GuideRequestDTO dto) {
        Guide guide = getOrThrow(id);
        guide.setGuideNumber(dto.getGuideNumber());
        guide.setCarrier(dto.getCarrier());
        guide.setDate(dto.getDate());
        guide.setRecipient(dto.getRecipient());
        guide.setDestinationAddress(dto.getDestinationAddress());
        guide.setCargoDescription(dto.getCargoDescription());
        return toDTO(guideRepository.save(guide));
    }

    public void delete(Long id) {
        Guide guide = getOrThrow(id);
        if (guide.getS3Key() != null) {
            s3Service.delete(guide.getS3Key());
            guide.setS3Key(null);
        }
        guide.setStatus(GuideStatus.DELETED);
        guideRepository.save(guide);
    }

    public GuideResponseDTO uploadToS3(Long id) {
        Guide guide = getOrThrow(id);

        byte[] pdf = pdfService.generate(guide);

        String s3Key = s3Service.upload(pdf, guide);
        guide.setS3Key(s3Key);
        guide.setStatus(GuideStatus.UPLOADED);

        return toDTO(guideRepository.save(guide));
    }

    public byte[] download(Long id, String requester) {
        Guide guide = getOrThrow(id);
        if (guide.getStatus() != GuideStatus.UPLOADED) {
            throw new IllegalStateException("Guide is not available in S3");
        }
        return s3Service.download(guide.getS3Key(), requester);
    }

    public List<GuideResponseDTO> findByCarrierAndDate(String carrier, LocalDate date) {
        return guideRepository.findByCarrierAndDate(carrier, date)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Guide getOrThrow(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() -> new GuideNotFoundException(id));
    }

    private GuideResponseDTO toDTO(Guide guide) {
        GuideResponseDTO dto = new GuideResponseDTO();
        dto.setId(guide.getId());
        dto.setGuideNumber(guide.getGuideNumber());
        dto.setCarrier(guide.getCarrier());
        dto.setDate(guide.getDate());
        dto.setRecipient(guide.getRecipient());
        dto.setDestinationAddress(guide.getDestinationAddress());
        dto.setCargoDescription(guide.getCargoDescription());
        dto.setStatus(guide.getStatus());
        dto.setS3Key(guide.getS3Key());
        return dto;
    }
}
