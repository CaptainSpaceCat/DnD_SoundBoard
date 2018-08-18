import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import javax.sound.sampled.*;


/*
 * AmbientTrack - data structure that holds and handles an ambient soundtrack
 * this type of track starts when you press its button, and stops when you press
 * it again. It operates independently of other tracks, meaning it will overlay
 * them. It can therefore be used to add ambient noise to a scene without
 * sacrificing music, like adding wind noises over a battle sequence.
 */
public class AmbientTrack extends SoundTrack {
	
	public String name;
	private Clip track;
	public final int PIN_WIDTH = 8;
	
	public AmbientTrack(String n, Clip c) {
		name = n;
		track = c;
		type = TYPE_AMBIENT;
		priority = 1;
	}
	
	public void activate() {
		if (this.isPlaying()) {
			//position = track.getFramePosition();
			track.stop();
		} else {
			//track.setFramePosition(position);
			track.start();
			track.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stop() {
		track.setFramePosition(0);
		track.stop();
	}
	
	public void drawSelf(Rectangle bounds, Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, bounds.width, bounds.height);
		
		int pixelPos = (int)(getTrackPercent() * bounds.width);
		graphics.setColor(Color.black);
		graphics.fillRect(pixelPos, 0, PIN_WIDTH, bounds.height);
	}
	
	public int getTrackLength() {
		return track.getFrameLength();
	}
	
	public int getTrackPosition() {
		return track.getFramePosition() % getTrackLength();
	}
	
	public float getTrackPercent() {
		return ((float)getTrackPosition()) / getTrackLength();
	}
	
	public void setTrackPercent(float percent) {
		track.setFramePosition((int)(percent * getTrackLength()));
	}
	
	public boolean isPlaying() {
		return track.isActive();
	}
}
