import javax.sound.sampled.*;

/**
 * Class for managing the music being played in the background
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 *  @author Stella Li
 */
public class Track extends Thread {
    private AudioInputStream stream;
    private Clip clip;

    public Track(String file) {
        try {
            stream = AudioSystem.getAudioInputStream(new java.io.File(file));
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,stream.getFormat()));
            clip.open(stream);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pause() {
        try {
            clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run(){
        try {
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}