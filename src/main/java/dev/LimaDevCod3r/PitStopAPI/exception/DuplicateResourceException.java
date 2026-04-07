package dev.LimaDevCod3r.PitStopAPI.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String field, String value) {
        super(field + " '" + value + "' is already in use");
    }
}
