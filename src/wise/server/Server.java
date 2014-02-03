package wise.server;

import wise.Logger;
import wise.network.InputHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Server application for WISE
 * <p/>
 * Created by kevin on 03/02/14.
 */
public class Server implements Runnable{

	private Thread thread;
	private ServerInfo serverInfo;
	private ServerSocket socket;
	private HashMap<String, int[]> consoleCommands = new HashMap<String, int[]>();
	private boolean finished = false;
	private ArrayList<ClientHandler> connectedClients = new ArrayList<ClientHandler>();

	public static void main(String args[]){
		if(args.length != 1){
			Logger.log(Logger.ERROR, "USAGE: java wise.server.Server <port>");
		}else{
			int port = Integer.parseInt(args[0]);
			new Server(port);
		}
	}

	public Server(int port){
		this.serverInfo = new ServerInfo();

		serverInfo.setPort(port);

		addConsoleCommand("help", new int[]{0});

		thread = new Thread(this);
		thread.start();
	}

	private void addConsoleCommand(String cmd, int[] args){
		if(!this.consoleCommands.containsKey(cmd)){
			// Command not yet added for these argument counts
			this.consoleCommands.put(cmd, args);
		}
	}

	@Override
	public void run(){
		startServer();
	}

	private void startServer(){
		Logger.log(Logger.INFO, "Starting server on port " + this.serverInfo.getPort());
		try{
			// Start server
			this.socket = new ServerSocket(this.serverInfo.getPort());
			this.socket.setReuseAddress(true);
			Logger.log(Logger.DEBUG, "Socket created on port " + this.serverInfo.getPort());

			// Listen for serverside-commands
			this.serverInfo.setInputHandler(new InputHandler(this));

			// Listen for clients
			while(!finished){
				Socket client = socket.accept();
				Logger.log(Logger.INFO, "Client connected from " + client.getInetAddress() + ":" + client.getPort());
				ClientHandler handler = new ClientHandler(this, client);
				connectedClients.add(handler);
			}
		}catch(IOException e){
			Logger.log(Logger.ERROR, "Failed to start server [" + e.getMessage() + "]");
		}
	}

	public void consoleCommand(String msg){
		// Received console command
		if(isValidConsoleCommand(msg)){
			executeConsoleCommand(msg);
		}
	}

	private void executeConsoleCommand(String msg){
		String[] split = msg.split(" ");
		String[] args = new String[split.length - 1];
		System.arraycopy(split, 1, args, 0, split.length - 1);
		String cmd = split[0];

		switch(cmd){
			case "help":
				this.help();
				break;
		}
	}

	private void help(){
		Logger.log(Logger.HELP, "Supported server commands:");
		Logger.log(Logger.HELP, "/help - Displays this help");
	}

	private boolean isValidConsoleCommand(String msg){
		String[] split = msg.split(" ");
		String[] args = new String[split.length - 1];
		System.arraycopy(split, 1, args, 0, split.length - 1);
		String cmd = split[0];
		String argString = "";
		for(String arg : args){
			if(argString.equals("")){
				argString = arg;
			}else{
				argString = " " + arg;
			}
		}

		if(this.consoleCommands.containsKey(cmd)){
			int[] pArgCount = this.consoleCommands.get(cmd);
			for(int arg : pArgCount){
				if(arg == args.length){
					return true;
				}
			}
		}
		return false;
	}
}
