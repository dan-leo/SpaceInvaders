import java.io.File;
import java.util.TimerTask;

public class Music extends TimerTask {

		
	
	public Music() {
		
	}

	@Override
	public void run() {
		// PLAY AUDIO CODE
    	File[] file = new File[2];
    	file[0] = new File("Chupathingy.wav");
    	file[1  ] = new File("Piano.wav");
		Sound.playSoundFile(file[0]);
		Sound.playSoundFile(file[1]);
		
		
	}

}



