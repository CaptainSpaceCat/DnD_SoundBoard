import javax.sound.sampled.Clip;

/*
 * Track - quick structure to hold button name and sound file name in one
 */
public class Track {
	
	public String name;
	public Clip track;
	
	public Track(String n, Clip c) {
		name = n;
		track = c;
	}
}
