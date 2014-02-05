package wise.network;

import wise.Logger;

import java.util.HashMap;

/**
 * The protocol used by the client.
 * <p/>
 * Created by kevin on 04/02/14.
 */
public class ClientProtocol {

    /* Constant for hello command 	 - hello <version> */
    public static final String HELLO = "hello";
    /* Constant for typeOk command 	 - typeOk */
    public static final String TYPEOK = "typeOk";
    /* Constant for infoOk command 	 - infoOk */
    public static final String INFOOK = "infoOk";
    /* Constant for continue command - continue */
    public static final String CONTINUE = "continue";
    /* Constant for error command 	 - error <code> */
    public static final String ERROR = "error";

    private ServerHandler parentHandler = null;
    private HashMap<String, Integer> clientCommands = new HashMap<String, Integer>();

    public ClientProtocol(ServerHandler c) {
        this.parentHandler = c;
        clientCommands.put(ClientProtocol.HELLO, 1);
        clientCommands.put(ClientProtocol.TYPEOK, 0);
        clientCommands.put(ClientProtocol.INFOOK, 0);
        clientCommands.put(ClientProtocol.CONTINUE, 0);
        clientCommands.put(ClientProtocol.ERROR, 1);
    }

    public void receivedMessage(String msg) {
        Logger.log(Logger.INFO, "MSG: " + msg);
        if (isValidCommand(msg)) {
            executeCommand(msg);
        } else {
            this.parentHandler.sendError(Errors.SERVER_MALFORMED_COMMAND);
        }
    }

    private boolean isValidCommand(String msg) {
        String[] split = msg.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        String cmd = split[0];

        if (this.clientCommands.containsKey(cmd)) {
            int argCount = this.clientCommands.get(cmd);
            if (argCount == args.length) {
                return true;
            }
        }
        return false;
    }

    private void executeCommand(String msg) {
        String[] split = msg.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        String cmd = split[0].substring(1);

        switch (cmd) {
            case ClientProtocol.HELLO:
                this.hello(args[0]);
                break;
            case ClientProtocol.TYPEOK:
                this.typeok();
                break;
            case ClientProtocol.INFOOK:
                this.infook();
                break;
            case ClientProtocol.CONTINUE:
                this.acknowledge();
                break;
            case ClientProtocol.ERROR:
                this.error(Integer.parseInt(args[0]));
                break;
        }
    }

    /**
     * Command that server sends to client to acknowledge the connection.
     * <p/>
     * Requires that the handshake is not done.
     *
     * @param version - Version string of server
     */
    private void hello(String version) {

    }

    /**
     * Command that server sends to client to acknowledge the type
     * <p/>
     * Requires that the handshake is done.
     */
    private void typeok() {

    }

    /**
     * Command that server sends to client to acknowledge the info type
     * <p/>
     * Requires that the handshake is done.
     * Requires that the type is sent.
     */
    private void infook() {

    }

    /**
     * Command that server sends to client to acknowledge the info was received
     * <p/>
     * Requires that the handshake is done.
     * Requires that the type is sent.
     * Requires that the info is sent.
     */
    private void acknowledge() {

    }

    /**
     * Command that server sends to client to inform about an error made
     *
     * @param code - The error code.
     */
    private void error(int code) {

    }

}
