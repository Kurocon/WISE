package wise.server;

import wise.Logger;
import wise.network.ClientProtocol;
import wise.network.ServerProtocol;

import java.io.*;
import java.net.Socket;

/**
 * Class to handle client connections
 * Created by kevin on 03/02/14.
 */
public class ClientHandler implements Runnable{

	private Server parentServer;
	private Socket socket;
	private Thread thread;
    private ServerProtocol protocol = new ServerProtocol(this);
    private String msg = "";
    private boolean finished = false;
    private InputStream inStream = null;
    private BufferedReader inReader = null;
    private OutputStream outStream = null;
    private BufferedWriter outWriter = null;

    public ClientInformation clientInfo = null;

    public ClientHandler(Server server, Socket client){
		this.parentServer = server;
		this.socket = client;

		this.thread = new Thread(this);
		this.thread.start();
	}

	public void run(){
        Logger.log(Logger.DEBUG, "Opening channel...");
        try {
            this.inStream = this.socket.getInputStream();
            this.outStream = this.socket.getOutputStream();
            this.inReader = new BufferedReader(new InputStreamReader(this.inStream));
            this.outWriter = new BufferedWriter(new OutputStreamWriter(this.outStream));
        } catch (IOException e) {
            Logger.log(Logger.ERROR, "Could not open channel [" + e.getMessage() + "]");
        }
        Logger.log(Logger.INFO, "Channel opened");
        this.clientInfo = new ClientInformation();
        while (!finished) {
            try {
                this.msg = this.inReader.readLine();
                this.protocol.receivedMessage(this.msg);
            } catch (IOException e) {
                if (!this.finished) {
                    Logger.log(Logger.ERROR, "Closing channel [" + e.getMessage() + "]");
                    try {
                        this.close();
                    } catch (IOException f) {
                        Logger.log(Logger.ERROR, "Error while closing channel [" + f.getMessage() + "]");
                    }
                }
            }
        }
    }

    public void send(String msg) {
        try {
            this.outWriter.write(msg + "\r\n");
            this.outWriter.flush();
        } catch (IOException e) {
            Logger.log(Logger.ERROR, "Could not send message: " + msg + " [" + e.getMessage() + "]");
            if (!this.finished) {
                Logger.log(Logger.ERROR, "Closing channel [" + e.getMessage() + "]");
                try {
                    this.close();
                } catch (IOException f) {
                    Logger.log(Logger.ERROR, "Error while closing channel [" + f.getMessage() + "]");
                }
            }
        }
    }

    public void sendCommand(String cmd, String[] args) {
        String command = cmd;
        for (String arg : args) {
            command = command + " " + arg;
        }
        Logger.log(Logger.INFO, msg);
        send(command);
    }

    public void sendError(int errorCode) {
        String error = ClientProtocol.ERROR + " " + errorCode;
        Logger.log(Logger.ERROR, error);
        send(error);
    }

    public void close() throws IOException {
        this.finished = true;
        this.parentServer.removeClient(this);
        this.outWriter.close();
        this.inReader.close();
        this.outStream.close();
        this.inStream.close();
        this.socket.close();
    }

    public boolean getHandshake() {
        return clientInfo.handshake;
    }

    public String getVersion() {
        return clientInfo.version;
    }

    public String getHostName() {
        return clientInfo.hostName;
    }

    public String getDeviceID() {
        return clientInfo.deviceID;
    }

    public String getInfoName() {
        return clientInfo.infoName;
    }

    public String getData() {
        return clientInfo.data;
    }

    public void setHandshake(boolean handshaked) {
        clientInfo.handshake = handshaked;
    }

    public void setHostName(String hostName) {
        clientInfo.hostName = hostName;
    }

    public void setVersion(String version) {
        clientInfo.version = version;
    }

    public void setDeviceID(String deviceID) {
        clientInfo.deviceID = deviceID;
    }

    public void setInfoName(String infoName) {
        clientInfo.infoName = infoName;
    }

    public void setData(String data) {
        clientInfo.data = data;
    }
}
