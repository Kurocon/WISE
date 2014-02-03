package wise;

/**
 * WISE Logger
 * <p/>
 * Used to log messages to the console.
 * <p/>
 * Created by kevin on 03/02/14.
 */
public class Logger{

	public static final int LOGLEVEL = 0;

	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int ERROR = 2;

	public static void log(int level, String msg){
		String prefix = "INFO";
		switch(level){
			case DEBUG:
				prefix = "DEBUG";
				break;
			case INFO:
				prefix = "INFO";
				break;
			case ERROR:
				prefix = "ERROR";
				break;
		}
		if(level >= Logger.LOGLEVEL){
			System.out.println(prefix + ": " + msg);
		}
	}

}
