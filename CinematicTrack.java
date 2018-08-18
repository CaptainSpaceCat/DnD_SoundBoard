import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import javax.sound.sampled.*;

public class CinematicTrack extends SoundTrack {
	
	public String name;
	private Clip track;
	private int[] dividers;
	private boolean[] repeats;
	private int activeSegment = -1;
	
	public final int PIN_WIDTH = 8;
	
	public CinematicTrack(String n, Clip c, int[] d, boolean[] r) {
		name = n;
		track = c;
		dividers = d;
		repeats = r;
		type = TYPE_CINEMATIC;
		priority = 0;
	}
	
	//used to correctly display the pin when drawing the track to the soundbar
	private int offset = 0;
	private int start = 0;
	private int low = 0;
	private int high = 0;
	
	public void activate() {
		System.out.println(getTrackPosition());
		offset = getTrackPosition();
		activeSegment++;
		while (activeSegment < repeats.length && !repeats[activeSegment]) {
			activeSegment++;
		}
		track.stop();
		if (activeSegment == 0) {
			track.setLoopPoints(0, dividers[0]);
			low = 0;
			high = dividers[0];
		} else if (activeSegment == dividers.length) {
			track.setLoopPoints(dividers[dividers.length], -1);
			low = dividers[dividers.length];
			high = -1;
		} else if (activeSegment < dividers.length) {
			track.setLoopPoints(dividers[activeSegment - 1], dividers[activeSegment]);
			low = dividers[activeSegment - 1];
			high = dividers[activeSegment];
		}
		
		start = track.getFramePosition();
		track.start();
		
		if (activeSegment <= dividers.length) {
			track.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			track.loop(0);
			low = 0;
			high = getTrackLength();
		}
		
	}
	
	public void stop() {
		track.setFramePosition(0);
		track.stop();
		activeSegment = -1;
	}
	
	public void drawSelf(Rectangle bounds, Graphics graphics) {
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, bounds.width, bounds.height);
		
		int[] repeatedSegments = getRepeatedSegments();
		for (int i = 0; i < repeatedSegments.length; i+=2) {
			if (i == getCurrentRepeatingSegment() * 2) {
				graphics.setColor(Color.yellow);
			} else {
				graphics.setColor(Color.cyan);
			}
			int x = (int)((((float)repeatedSegments[i])/getTrackLength()) * bounds.width);
			int y = (int)((((float)repeatedSegments[i + 1])/getTrackLength()) * bounds.width);
			int width = y-x;
			graphics.fillRect(x, 0, width, bounds.height);
		}
		
		int pixelPos = (int)(getTrackPercent() * bounds.width);
		graphics.setColor(Color.black);
		graphics.fillRect(pixelPos, 0, PIN_WIDTH, bounds.height);
	}
	
	public int getTrackLength() {
		return track.getFrameLength();
	}
	
	public int getTrackPosition() {
		int pos = track.getFramePosition() - start + offset;
		while (pos > high) {
			pos -= (high - low);
		}
		return pos;
	}
	
	public float getTrackPercent() {
		return ((float)getTrackPosition()) / getTrackLength();
	}
	
	public void setTrackPercent(float percent) {
		if (percent * getTrackLength() < high)
		track.setFramePosition((int)(percent * getTrackLength()));
		start = track.getFramePosition();
		offset = track.getFramePosition();
	}
	
	public boolean isPlaying() {
		return track.isActive();
	}
	
	public int getCurrentSegment() {
		return activeSegment;
	}
	//5818134
	//6383000
	
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
