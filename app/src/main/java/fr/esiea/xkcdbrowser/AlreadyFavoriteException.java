package fr.esiea.xkcdbrowser;

public class AlreadyFavoriteException extends Exception {
    public AlreadyFavoriteException() {
        super();
    }

    public AlreadyFavoriteException(String message) {
        super(message);
    }

    public AlreadyFavoriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyFavoriteException(Throwable cause) {
        super(cause);
    }
}
