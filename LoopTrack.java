import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import javax.sound.sampled.*;


/*
 * LoopTrack - data structure to hold and handle a looping soundtrack
 * this type of soundtrack will play when its button is pressed, and if it has
 * any coterminal tracks, will switch to and from them as the button is pressed
 * again. If it has no coterminal tracks, pressing the button will do nothing.
 * Can only be stopped with main "Stop" button. Useful for battle themes, especially
 * ones with two different coterminal tracks, like the XCOM music
 * Also useful for basic looping themes, like the open road or forest music
 */
public class LoopTrack extends SoundTrack {

	public String name;
	private Clip[] tracks;
	private int activeTrack = 0;
	
	public final int PIN_WIDTH = 8;
	
	public LoopTrack(String n, Clip c){
		this(n, new Clip[] {c});
	}
	
	public LoopTrack(String n, Clip[] c) {
		System.out.println(c.length);
		tracks = new Clip[c.length];
		for (int i = 0; i < c.length; i++) {
			tracks[i] = c[i];
		}
		name = n;
		type = TYPE_LOOP;
		priority = 0;
	}
	
	public void activate() {
		if (this.isPlaying()) {
			if (tracks.length > 1) {
				int currentPos = getTrackPosition();
				tracks[activeTrack].stop();
				activeTrack = (activeTrack + 1) % tracks.length;
				tracks[activeTrack].setFramePosition(currentPos);
				tracks[activeTrack].start();
				tracks[activeTrack].loop(Clip.LOOP_CONTINUOUSLY);
			}
		} else {
			tracks[activeTrack].start();
			tracks[activeTrack].loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stop() {
		tracks[activeTrack].setFramePosition(0);
		tracks[activeTrack].stop();
		activeTrack = 0;
	}
	
	public void drawSelf(Rectangle bounds, Graphics graphics) {
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, bounds.width, bounds.height);
		
		int pixelPos = (int)(getTrackPercent() * bounds.width);
		graphics.setColor(Color.black);
		graphics.fillRect(pixelPos, 0, PIN_WIDTH, bounds.height);
	}
	
	public int getTrackLength() {
		return tracks[activeTrack].getFrameLength();
	}
	
	public int getTrackPosition() {
		return tracks[activeTrack].getFramePosition() % getTrackLength();
	}
	
	public float getTrackPercent() {
		return ((float)getTrackPosition()) / getTrackLength();
	}
	
	public void setTrackPercent(float percent) {
		tracks[activeTrack].setFramePosition((int)(percent * getTrackLength()));
	}
	
	public boolean isPlaying() {
		return tracks[activeTrack].isActive();
	}
}
