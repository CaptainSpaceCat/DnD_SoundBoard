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
	
	public LoopTrack(String n, Clip c){
		tracks = new Clip[1];
		tracks[0] = c;
		name = n;
		type = TYPE_LOOP;
	}
	
	public LoopTrack(String n, Clip[] c) {
		System.out.println(c.length);
		tracks = new Clip[c.length];
		for (int i = 0; i < c.length; i++) {
			tracks[i] = c[i];
		}
		name = n;
	}
	
	public void activate() {
		if (this.isPlaying()) {
			if (tracks.length > 1) {
				int currentPos = tracks[activeTrack].getFramePosition();
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
	
	public int getTrackLength() {
		return tracks[activeTrack].getFrameLength();
	}
	
	public int getTrackPosition() {
		return tracks[activeTrack].getFramePosition() % this.getTrackLength();
	}
	
	public void setTrackPosition(int pos) {
		tracks[activeTrack].setFramePosition(pos);
	}
	
	public boolean isPlaying() {
		return tracks[activeTrack].isActive();
	}
}
