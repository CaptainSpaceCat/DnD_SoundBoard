import javax.sound.sampled.Clip;

public class CinematicTrack extends SoundTrack {
	
	public String name;
	private Clip track;
	private int[] dividers;
	private boolean[] repeats;
	private int activeSegment = -1;
	
	public CinematicTrack(String n, Clip c, int[] d, boolean[] r) {
		name = n;
		track = c;
		dividers = d;
		repeats = r;
		type = TYPE_CINEMATIC;
	}
	
	public void activate() {
		activeSegment++;
		while (activeSegment < repeats.length && !repeats[activeSegment]) {
			activeSegment++;
		}
		track.stop();
		if (activeSegment == 0) {
			track.setLoopPoints(0, dividers[0]);
		} else if (activeSegment == dividers.length) {
			track.setLoopPoints(dividers[dividers.length], -1);
		} else if (activeSegment < dividers.length) {
			track.setLoopPoints(dividers[activeSegment - 1], dividers[activeSegment]);
		}
		
		track.start();
		
		if (activeSegment <= dividers.length) {
			track.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			track.loop(0);
		}
		
	}
	
	public void stop() {
		track.setFramePosition(0);
		track.stop();
		activeSegment = -1;
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
	
	public int getCurrentSegment() {
		return activeSegment;
	}
	
	public int getCurrentRepeatingSegment() {
		int segment = 0;
		for (int i = 0; i < activeSegment; i++) {
			if (i < repeats.length && repeats[i]) {
				segment++;
			}
		}
		return segment;
	}
	
	public int[] getRepeatedSegments() {
		int count = 0;
		for (int i = 0; i < repeats.length; i++) {
			if (repeats[i]) {
				count++;
			}
		}
		
		int[] result = new int[count*2];
		count = 0;
		for (int i = 0; i < repeats.length; i++) {
			if (repeats[i]) {
				if (i == 0) {
					result[count] = 0;
				} else {
					result[count] = dividers[i - 1];
				}
				if (i == dividers.length) {
					result[count+1] = track.getFrameLength();
				} else {
					result[count+1] = dividers[i];
				}
				count+=2;
			}
		}
		return result;
	}
}
