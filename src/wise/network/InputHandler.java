package wise.network;

import com.sun.security.ntlm.Client;
import wise.Logger;
import wise.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * InputHandler to listen to console input and relay it to the parent server/client
 * Created by kevin on 03/02/14.
 */
public class InputHandler implements Runnable{

	private Object parent;
	private Thread thread;
	private String msg = "";
	private InputStream inStream = System.in;
	private BufferedReader inReader = new BufferedReader(new InputStreamReader(inStream));

	private boolean finished = false;

	//@requires parent instanceof Server || parent instanceof Client
	public InputHandler(Object parent){
		this.parent = parent;

		// Start listening
		this.thread = new Thread(this);
		this.thread.start();
	}

	public void run(){
		Logger.log(Logger.INFO, "Console commands are now available. Type /help for a list.");
		while(!finished){
			try{
				this.msg = this.inReader.readLine();
				Logger.log(Logger.CONSOLE, this.msg);
				if(this.parent instanceof Server){
					((Server) this.parent).consoleCommand(this.msg);
				}else if(this.parent instanceof Client){
					((Client) this.parent).consoleCommand(this.msg);
				}else{
					Logger.log(Logger.ERROR, "I do not know what to do with this input, the parent is not a client or a server.");
				}
			}catch(IOException e){
				Logger.log(Logger.ERROR, "Could not read from console [" + e.getMessage() + "]");
			}
		}
	}


	public void setFinished(boolean finished){
		this.finished = finished;
	}
}
