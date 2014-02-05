package wise.network;

import wise.Logger;
import wise.server.ClientHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * The protocol used by the server
 * <p/>
 * Created by kevin on 03/02/14.
 */
public class ServerProtocol {

    /* Constant for hello command 	- hello <version> */
    public static final String HELLO = "hello";
    /* Constant for type command 	- type <type> <hostname> */
    public static final String TYPE = "type";
    /* Constant for info command 	- info <type> */
    public static final String INFO = "info";
    /* Constant for data command	- data <data> */
    public static final String DATA = "data";
    /* Constant for part command 	- part */
    public static final String PART = "part";
    /* Constant for error command 	- error <code> */
    public static final String ERROR = "error";
    /* Constant for sendToWhatsapp debug command 	- sendToWhatsapp <msg> */
    public static final String SENDDEBUG = "sendToWhatsapp";

    private ClientHandler parentHandler = null;
    private HashMap<String, Integer> serverCommands = new HashMap<>();

    public ServerProtocol(ClientHandler c) {
        this.parentHandler = c;
        serverCommands.put(ServerProtocol.HELLO, 1);
        serverCommands.put(ServerProtocol.TYPE, 2);
        serverCommands.put(ServerProtocol.INFO, 1);
        serverCommands.put(ServerProtocol.DATA, -1);
        serverCommands.put(ServerProtocol.PART, 0);
        serverCommands.put(ServerProtocol.ERROR, 1);
        serverCommands.put(ServerProtocol.SENDDEBUG, -1);
    }

    public void receivedMessage(String msg) {
        Logger.log(Logger.INFO, "MSG: " + msg);
        if (isValidCommand(msg)) {
            Logger.log(Logger.DEBUG, "Valid command: " + msg);
            executeCommand(msg);
        } else {
            Logger.log(Logger.DEBUG, "Invalid command: " + msg);
            this.parentHandler.sendError(Errors.CLIENT_MALFORMED_COMMAND);
        }
    }

    private boolean isValidCommand(String msg) {
        String[] split = msg.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        String cmd = split[0];

        if (this.serverCommands.containsKey(cmd)) {
            int argCount = this.serverCommands.get(cmd);
            if (argCount == args.length || argCount == -1) {
                return true;
            }
        }
        return false;
    }

    private void executeCommand(String msg) {
        String[] split = msg.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        String cmd = split[0];
        Logger.log(Logger.CONSOLE, cmd);
        switch (cmd) {
            case ServerProtocol.HELLO:
                this.hello(args[0]);
                break;
            case ServerProtocol.TYPE:
                this.type(args[0], args[1]);
                break;
            case ServerProtocol.INFO:
                this.info(args[0]);
                break;
            case ServerProtocol.DATA:
                this.data(msg.substring(ServerProtocol.DATA.length() + 1)); // Message without "DATA " in front of it
                break;
            case ServerProtocol.PART:
                this.part();
                break;
            case ServerProtocol.ERROR:
                this.error(Integer.parseInt(args[0]));
                break;
            case ServerProtocol.SENDDEBUG:
                this.sendDebug(args);
                break;
        }
    }

}

    /**
     * Command that client sends to server to initiate the connection.
     * <p/>
     * Requires that the handshake is not done.
     *
     * @param version - Version string of client
     */
    private void hello(String version) {
        if (!this.parentHandler.getHandshake()) {
            this.parentHandler.setVersion(version);
            this.parentHandler.sendCommand(ClientProtocol.HELLO, new String[0]);
            this.parentHandler.setHandshake(true);
        } else if (this.parentHandler.getHandshake()) {
            this.parentHandler.sendError(Errors.CLIENT_ALREADY_HANDSHAKED);
        } else {
            this.parentHandler.sendError(Errors.CLIENT_GENERIC_ERROR);
        }
    }

    /**
     * Command that client sends to server to initiate the connection.
     * <p/>
     * Requires that the handshake is done.
     *
     * @param deviceID - Identifier for the device that the server knows
     * @param hostName - Hostname of the source
     */
    private void type(String deviceID, String hostName) {
        if (this.parentHandler.getHandshake()) {
            this.parentHandler.setDeviceID(deviceID);
            this.parentHandler.setHostName(hostName);
            this.parentHandler.sendCommand(ClientProtocol.TYPEOK, new String[0]);
        } else if (!this.parentHandler.getHandshake()) {
            this.parentHandler.sendError(Errors.CLIENT_HANDSHAKE_MISSING);
        } else {
            this.parentHandler.sendError(Errors.CLIENT_GENERIC_ERROR);
        }
    }

    /**
     * Command that client sends to server to initiate the connection.
     * <p/>
     * Requires that the handshake is done.
     * Requires that the source is known.
     *
     * @param infoName - Name for the information
     */
    private void info(String infoName) {
        if (this.parentHandler.getHandshake() && !this.parentHandler.getDeviceID().equals("")) {
            this.parentHandler.setInfoName(infoName);
            this.parentHandler.sendCommand(ClientProtocol.CONTINUE, new String[0]);
        } else if (!this.parentHandler.getHandshake()) {
            this.parentHandler.sendError(Errors.CLIENT_HANDSHAKE_MISSING);
        } else if (this.parentHandler.getDeviceID().equals("")) {
            this.parentHandler.sendError(Errors.CLIENT_UNEXPECTED_COMMAND);
        } else {
            this.parentHandler.sendError(Errors.CLIENT_GENERIC_ERROR);
        }
    }

    /**
     * Command that client sends to server to transfer data.
     * <p/>
     * Requires that the handshake is done.
     * Requires that the source is known.
     * Requires that the info type is known.
     *
     * @param data - The data
     */
    private void data(String data) {
        if (this.parentHandler.getHandshake() && !this.parentHandler.getDeviceID().equals("") && !this.parentHandler.getInfoName().equals("")) {
            this.parentHandler.setData(data);
            this.parentHandler.sendCommand(ClientProtocol.INFOOK, new String[0]);
        } else if (!this.parentHandler.getHandshake()) {
            this.parentHandler.sendError(Errors.CLIENT_HANDSHAKE_MISSING);
        } else if (this.parentHandler.getDeviceID().equals("")) {
            this.parentHandler.sendError(Errors.CLIENT_UNEXPECTED_COMMAND);
        } else if (this.parentHandler.getInfoName().equals("")) {
            this.parentHandler.sendError(Errors.CLIENT_UNEXPECTED_COMMAND);
        } else {
            this.parentHandler.sendError(Errors.CLIENT_GENERIC_ERROR);
        }
    }

    /**
     * Command that client sends to server to close the connection.
     * <p/>
     * Requires that the handshake is done.
     */
    private void part() {
        if (this.parentHandler.getHandshake()) {
            try {
                this.parentHandler.close();
            } catch (IOException e) {
                Logger.log(Logger.ERROR, "Client part failed [" + e.getMessage() + "]");
            }
        } else if (!this.parentHandler.getHandshake()) {
            this.parentHandler.sendError(Errors.CLIENT_HANDSHAKE_MISSING);
        } else {
            this.parentHandler.sendError(Errors.CLIENT_GENERIC_ERROR);
        }
    }

    public void sendDebug(String[] args) {

    }

    /**
     * Command that client sends to server to inform about an error.
     *
     * @param errorCode - The error code
     */
    private void error(int errorCode) {
        Logger.log(Logger.ERROR, Errors.getMessage(errorCode));
    }
}
