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
	//private int position = 0;
	
	public AmbientTrack(String n, Clip c) {
		name = n;
		track = c;
		type = TYPE_AMBIENT;
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
	
	public int getTrackLength() {
		return track.getFrameLength();
	}
	
	public int getTrackPosition() {
		return track.getFramePosition() % this.getTrackLength();
	}
	
	public boolean isPlaying() {
		return track.isActive();
	}
}
