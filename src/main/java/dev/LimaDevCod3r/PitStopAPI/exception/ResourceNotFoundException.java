package dev.LimaDevCod3r.PitStopAPI.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " não encontrado com o código informado.");

    }
}
