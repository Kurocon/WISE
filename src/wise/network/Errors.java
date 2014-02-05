package wise.network;

/**
 * Class defining error codes.
 * <p/>
 * Created by kevin on 04/02/14.
 */
public class Errors {

    public static final int CLIENT_GENERIC_ERROR = 40;
    public static final int CLIENT_HANDSHAKE_MISSING = 41;
    public static final int CLIENT_ALREADY_HANDSHAKE = 42;
    public static final int CLIENT_MALFORMED_COMMAND = 43;
    public static final int CLIENT_DEVICE_NOT_FOUND = 44;
    public static final int CLIENT_UNEXPECTED_COMMAND = 45;

    public static final int SERVER_HANDSHAKE_MISSING = 51;
    public static final int SERVER_MALFORMED_COMMAND = 53;
    public static final int SERVER_UNEXPECTED_COMMAND = 55;

    public static String getMessage(int code) {
        String msg = "Unknown error code";
        switch (code) {
            case Errors.CLIENT_GENERIC_ERROR:
                msg = "An error occurred";
                break;
            case Errors.CLIENT_HANDSHAKE_MISSING:
                msg = "Client handshake missing";
                break;
            case Errors.CLIENT_ALREADY_HANDSHAKE:
                msg = "Client handshake already complete";
                break;
            case Errors.CLIENT_MALFORMED_COMMAND:
                msg = "Client command was malformed";
                break;
            case Errors.CLIENT_DEVICE_NOT_FOUND:
                msg = "Client device id not found";
                break;
            case Errors.CLIENT_UNEXPECTED_COMMAND:
                msg = "Client command not expected at this time";
                break;


            case Errors.SERVER_HANDSHAKE_MISSING:
                msg = "Server handshake missing";
                break;
            case Errors.SERVER_MALFORMED_COMMAND:
                msg = "Server command was malformed";
                break;
            case Errors.SERVER_UNEXPECTED_COMMAND:
                msg = "Server command not expected at this time";
                break;
        }
        return msg;
    }
}