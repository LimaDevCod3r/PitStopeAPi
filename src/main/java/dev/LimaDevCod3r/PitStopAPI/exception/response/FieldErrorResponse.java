package dev.LimaDevCod3r.PitStopAPI.exception.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class FieldErrorResponse {
    private String field;
    private String message;
}