package wise.server;

import wise.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server application for WISE
 * <p/>
 * Created by kevin on 03/02/14.
 */
public class Server implements Runnable{

	private Thread thread;
	private int port;
	private ServerSocket socket;

	public static void main(String args[]){
		if(args.length != 1){
			Logger.log(Logger.ERROR, "USAGE: java wise.server.Server <port>");
		}else{
			int port = Integer.parseInt(args[0]);
			new Server(port);
		}
	}

	public Server(int port){
		this.port = port;

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run(){
		startServer();
	}

	private void startServer(){
		try{
			socket = new ServerSocket(this.port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
