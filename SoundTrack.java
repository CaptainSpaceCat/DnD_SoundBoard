import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class SoundTrack {
	
	final int TYPE_UNKNOWN = -1;
	final int TYPE_LOOP = 0;
	final int TYPE_AMBIENT = 1;
	final int TYPE_CINEMATIC = 2;
	
	public int priority;
	public int type = TYPE_UNKNOWN;
	
	public abstract void activate(); //run when button is pressed for that track
	public abstract void stop(); //run when this track needs to be stopped
	public abstract void drawSelf(Rectangle bounds, Graphics graphics); //draws a graphical representation of itself to the passed graphics object
	
	public abstract void setTrackPercent(float percent); //sets the track to play starting from a certain percent completion

	
}
