package wise.server;

import java.net.Socket;

/**
 * Class to handle client connections
 * Created by kevin on 03/02/14.
 */
public class ClientHandler implements Runnable{

	private Server parentServer;
	private Socket socket;
	private Thread thread;

	public ClientHandler(Server server, Socket client){
		this.parentServer = server;
		this.socket = client;

		this.thread = new Thread(this);
		this.thread.start();
	}

	public void run(){

	}
}
