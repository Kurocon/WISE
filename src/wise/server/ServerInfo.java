package wise.server;

import wise.network.InputHandler;

/**
 * Information about the server
 * Created by kevin on 03/02/14.
 */
public class ServerInfo{
	private int port;
	private String version;
	private InputHandler inputHandler;


	public int getPort(){
		return port;
	}

	public void setPort(int port){
		this.port = port;
	}

	public String getVersion(){
		return version;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public InputHandler getInputHandler(){
		return inputHandler;
	}

	public void setInputHandler(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}
}
