import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


public class Sound {

	public Sound() {
		// TODO Auto-generated constructor stub
	}
	
	public static void playSoundFile(File file) {//http://java.ittoolbox.com/groups/technical-functional/java-l/sound-in-an-application-90681

		try {
			//get an AudioInputStream
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			//get the AudioFormat for the AudioInputStream
			AudioFormat audioformat = ais.getFormat();
			
			/*System.out.println("Format: " + audioformat.toString());
			System.out.println("Encoding: " + audioformat.getEncoding());
			System.out.println("SampleRate:" + audioformat.getSampleRate());
			System.out.println("SampleSizeInBits: " + audioformat.getSampleSizeInBits());
			System.out.println("Channels: " + audioformat.getChannels());
			System.out.println("FrameSize: " + audioformat.getFrameSize());
			System.out.println("FrameRate: " + audioformat.getFrameRate());
			System.out.println("BigEndian: " + audioformat.isBigEndian());*/
			
			//ULAW format to PCM format conversion
			if ((audioformat.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (audioformat.getEncoding() == AudioFormat.Encoding.ALAW)) {
				AudioFormat newformat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						audioformat.getSampleRate(),
						audioformat.getSampleSizeInBits() * 2,
						audioformat.getChannels(),
						audioformat.getFrameSize() * 2,
						audioformat.getFrameRate(), true);
				ais = AudioSystem.getAudioInputStream(newformat, ais);
				audioformat = newformat;
			}

			//checking for a supported output line
			DataLine.Info datalineinfo = new DataLine.Info(SourceDataLine.class, audioformat);
			if (!AudioSystem.isLineSupported(datalineinfo)) {
				//System.out.println("Line matching " + datalineinfo + " is not supported.");
			} else {
				//System.out.println("Line matching " + datalineinfo + " is supported.");
				//opening the sound output line
				SourceDataLine sourcedataline = (SourceDataLine) AudioSystem.getLine(datalineinfo);
				sourcedataline.open(audioformat);
				sourcedataline.start();
				//Copy data from the input stream to the output data line
				int framesizeinbytes = audioformat.getFrameSize();
				int bufferlengthinframes = sourcedataline.getBufferSize() / 8;
				int bufferlengthinbytes = bufferlengthinframes * framesizeinbytes;
				byte[] sounddata = new byte[bufferlengthinbytes];
				int numberofbytesread = 0;
				while ((numberofbytesread = ais.read(sounddata)) != -1) {
					// int numberofbytesremaining = numberofbytesread;
					sourcedataline.write(sounddata, 0, numberofbytesread);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
