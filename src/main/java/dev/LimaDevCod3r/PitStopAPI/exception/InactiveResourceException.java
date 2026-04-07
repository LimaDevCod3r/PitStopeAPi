package dev.LimaDevCod3r.PitStopAPI.exception;

public class InactiveResourceException extends RuntimeException {
    public InactiveResourceException(String resource, Long id) {
        super(resource + " inativo com o código " + id);
    }
}
