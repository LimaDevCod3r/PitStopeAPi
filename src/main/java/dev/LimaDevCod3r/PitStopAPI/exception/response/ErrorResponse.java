package dev.LimaDevCod3r.PitStopAPI.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private int status;
    private String path;
    private LocalDateTime timestamp;
    private List<FieldErrorResponse> fieldErrors;
}
