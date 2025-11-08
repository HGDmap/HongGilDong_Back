package hongik.map.honggildong.domain.image.converter;

import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;

import java.awt.*;
import java.net.URL;

public class ImageConverter {

    public static ImageResponseDTO.PresignedDTO toDTO(URL presignedURL, String imagePath) {
        return ImageResponseDTO.PresignedDTO.builder()
                .presignedURL(presignedURL.toString())
                .imageURL(imagePath)
                .build();
    }
}
