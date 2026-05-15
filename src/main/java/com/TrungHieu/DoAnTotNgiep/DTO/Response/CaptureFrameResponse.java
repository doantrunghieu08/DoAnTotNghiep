package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaptureFrameResponse {

    String imageUrl;


    String publicId;


    Long size;
}
