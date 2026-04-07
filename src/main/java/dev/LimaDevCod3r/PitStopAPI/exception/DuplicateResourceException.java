package dev.LimaDevCod3r.PitStopAPI.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String field, String value) {
        super(String.format("O campo %s informado já está em uso.", field));
    }
}
