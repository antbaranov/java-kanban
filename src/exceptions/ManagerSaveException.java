package exceptions;

// Метод собственное непроверяемое исключение
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
    }
}
