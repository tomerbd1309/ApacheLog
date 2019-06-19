
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaboolaException extends Exception {
    public TaboolaException() {
        super();
    }

    public TaboolaException(String msg) {
        super(msg);
    }

    public TaboolaException(String msg, Throwable t) {
        super(msg, t);
    }

    public TaboolaException(String msg, Throwable t, Object... args) {
        super(String.format(msg, args), t);
    }

    public TaboolaException(String msg, Object... args) {
        super(String.format(msg, args));
    }

    public TaboolaException(Throwable t) {
        super(t);
    }
}
