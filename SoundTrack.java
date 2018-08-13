
public abstract class SoundTrack {
	
	final int TYPE_UNKNOWN = -1;
	final int TYPE_LOOP = 0;
	final int TYPE_AMBIENT = 1;
	final int TYPE_CINEMATIC = 2;
	
	public int type;
	
	public abstract void activate(); //run when button is pressed for that track
	public abstract void stop(); //run when this track needs to be stopped
	
	
}
