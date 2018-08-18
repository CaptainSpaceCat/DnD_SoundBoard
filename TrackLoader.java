import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 * TrackLoader - utility class designed to load all available tracks on program start
 * will navigate to Resources folder, and load all the soundtracks within
 * based on structure defined in TrackImporter
 */
public class TrackLoader {
	//path variables, point to the folder with sound resources
	private String resources = "C:\\Users\\Inigo\\Desktop\\SoundBoard\\Resources";
	
	private String ambientPath = "\\Ambient";
	private String cinematicPath = "\\Cinematic";
	private String loopPath = "\\Loop";
	private String transitionPath = "\\Transition";
	
	//member variables containing all the processed tracks
	public AmbientTrack[] ambientTracks;
	public LoopTrack[] loopTracks;
	public CinematicTrack[] cinematicTracks;
	
	public SoundTrack[] tracks;
	
	//constructor, loads all tracks upon creation
	public TrackLoader() {
		
		File ambientDir = new File(resources + ambientPath);
		File cinematicDir = new File(resources + cinematicPath);
		File loopDir = new File(resources + loopPath);
		File transitionDir = new File(resources + transitionPath);
		
		ambientTracks = LoadAmbient(ambientDir);
		loopTracks = LoadLoop(loopDir);
		cinematicTracks = LoadCinematic(cinematicDir);
	}
	
	
	public int getNumTracks() {
		return ambientTracks.length + loopTracks.length;
	}
	
	//accepts a file, and returns that file's extension, or "" if none found
	private static String getFileExtension(File f) {
	    String fileName = f.getName();
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
	
	//accepts a directory object, and returns all .wav files in that directory
	private File[] getSoundFiles(File dir) {
		File[] flist = dir.listFiles();
		int num = 0;
		for (File f: flist) {
			if (f.isFile() && getFileExtension(f).equals("wav")) {
				num++;
			}
		}
		File[] result = new File[num];
		num = 0;
		for (File f: flist) {
			if (f.isFile() && getFileExtension(f).equals("wav")) {
				result[num] = f;
				num++;
			}
		}
		
		return result;
	}
	
	//accepts a directory object, and returns all sub-directories of that directory
	private File[] getDirectories(File dir) {
		File[] flist = dir.listFiles();
		int num = 0;
		for (File f: flist) {
			if (!f.isFile()) {
				num++;
			}
		}
		File[] result = new File[num];
		num = 0;
		for (File f: flist) {
			if (!f.isFile()) {
				result[num] = f;
				num++;
			}
		}
		
		return result;
	}
	
	//accepts a string filename, and returns a buffered clip object
	//containing the data for the sound file of that name
	private Clip loadSoundFile(String fileName) {
		try {
			InputStream stream = new BufferedInputStream(new FileInputStream(fileName));
	        AudioInputStream audio = AudioSystem.getAudioInputStream(stream);
	        Clip c = AudioSystem.getClip();
	        c.open(audio);
	        return c;
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Load failed.");
		}
		return null;
	}
	
	//accepts a directory object, an returns a buffered reader
	//reading from the info.txt file in that directory
	private BufferedReader getInfoReader(File dir) {
		try {
			//System.out.println(dir.getAbsolutePath());
			return new BufferedReader(new FileReader(dir.getAbsolutePath() + "\\_info.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: _info.txt not found.");
		}
		return null;
	}
	
	//this function loads all ambient tracks in the directory passed to it
	private AmbientTrack[] LoadAmbient(File trackDir) {
		BufferedReader infoReader = getInfoReader(trackDir);
		File[] soundFiles = getSoundFiles(trackDir);
		AmbientTrack[] output = new AmbientTrack[soundFiles.length];
		
		for (int i = 0; i < soundFiles.length; i++) {
			try {
				String name = infoReader.readLine();
				String fileName = soundFiles[i].getAbsolutePath();
				
		        output[i] = new AmbientTrack(name, loadSoundFile(fileName));
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return output;
	}
	
	//this function loads all loop tracks in the directory passed to it
	private LoopTrack[] LoadLoop(File trackDir) {
		BufferedReader infoReader = getInfoReader(trackDir);
		File[] soundDirs = getDirectories(trackDir);
		LoopTrack[] output = new LoopTrack[soundDirs.length];
		
		for (int i = 0; i < soundDirs.length; i++) {
			try {
				String name = infoReader.readLine();
				System.out.println(name);
				File[] soundFiles = getSoundFiles(soundDirs[i]);
				Clip[] soundClips = new Clip[soundFiles.length];
				for (int j = 0; j < soundFiles.length; j++) {
					soundClips[j] = loadSoundFile(soundFiles[j].getAbsolutePath());
					System.out.println(soundFiles[j].getAbsolutePath());
				}
				
		        output[i] = new LoopTrack(name, soundClips);
		        System.out.println("Type:" + output[i].type);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return output;
	}
	
	private CinematicTrack[] LoadCinematic(File trackDir) {
		BufferedReader infoReader = getInfoReader(trackDir);
		File[] soundFiles = getSoundFiles(trackDir);
		CinematicTrack[] output = new CinematicTrack[soundFiles.length];
		
		for (int i = 0; i < soundFiles.length; i++) {
			try {
				String line = infoReader.readLine();
				String fileName = soundFiles[i].getAbsolutePath();
				
				String name = getName(line);
				line = line.substring(line.indexOf('|') + 1);
				int[] frames = getFrames(line);
				line = line.substring(line.indexOf('|') + 1);
				boolean[] repeats = getRepeats(line);
				
		        output[i] = new CinematicTrack(name, loadSoundFile(fileName), frames, repeats);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return output;
	}
	
	
	//=================================================== UTILS ===================================================//
	
	private String getName(String line) {
		int i = line.indexOf('|');
		String output = line.substring(0, i);
		line = line.substring(i + 1);
		return output;
	}
	
	private int[] getFrames(String line) {
		int[] temp = new int[20];
		int index = 0;
		while (line.charAt(0) != '|') {
			int i = line.indexOf(',');
			temp[index] = Integer.parseInt(line.substring(0, i));
			index++;
			line = line.substring(i + 1);
		}
		int[] output = new int[index];
		for (int i = 0; i < index; i++) {
			output[i] = temp[i];
		}
		return output;
	}
	
	private boolean[] getRepeats(String line) {
		boolean[] temp = new boolean[20];
		int index = 0;
		while (line.length() > 0) {
			int i = line.indexOf(',');
			temp[index] = (line.substring(0, i).charAt(0) == '1') ? true : false;
			index++;
			line = line.substring(i + 1);
		}
		boolean[] output = new boolean[index];
		for (int i = 0; i < index; i++) {
			output[i] = temp[i];
		}
		return output;
	}
}
