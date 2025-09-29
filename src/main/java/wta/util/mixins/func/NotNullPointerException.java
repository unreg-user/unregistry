package wta.util.mixins.func;

public class NotNullPointerException extends NullPointerException {
	public NotNullPointerException(String message) {
		super(message);
	}

    public NotNullPointerException() {
        super();
    }
}
