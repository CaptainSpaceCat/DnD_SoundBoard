import java.io.*;
import java.lang.Math;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Dimension;

/*
 * SoundBoard - This is the UI for the soundboard. It will hold the buttons
 * for each sound and will handle the interactions between all the tracks.
 */
public class SoundBoard extends JFrame implements ActionListener{
	JPanel panel;
	
	private String resources = "C:\\Users\\Connor\\Desktop\\SoundBoard\\Resources";
	
	final int TYPE_UNKNOWN = -1;
	final int TYPE_LOOP = 0;
	final int TYPE_AMBIENT = 1;
	final int TYPE_CINEMATIC = 2;
	
	int BUTTON_WIDTH = 120;
	int BUTTON_HEIGHT = 30;
	int BUTTONS_PER_ROW = 10;
  
	private JTextArea initArea, battleArea, numArea;
	private JButton battleButton, incrementTurn, importButton, stopButton, saveButton;
	private JSoundBar soundBar;
	
	private JButton[] loopButtons;
	private JButton[] ambientButtons;
	private JButton[] cinematicButtons;
	
	private TrackLoader tl;
  
	public SoundBoard() {
		//initialize panel
		panel = new JPanel() {
		      public void paintComponent(Graphics g) {
		    	  super.paintComponent(g);
		      }
		};
	    panel.setLayout(null);
	
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("SoundBoard");
	    this.setSize(new Dimension(60 + BUTTONS_PER_ROW * BUTTON_WIDTH, 700));
	    
	    
	    stopButton = new JButton("Stop All Sounds");
	    stopButton.addActionListener(this);
	    panel.add(stopButton);
	    stopButton.setBounds(20, 150, 500, 30);
	    
	    //set up tracks and buttons
		tl = new TrackLoader();
		loopButtons = new JButton[tl.loopTracks.length];
		ambientButtons = new JButton[tl.ambientTracks.length];
		cinematicButtons = new JButton[tl.cinematicTracks.length];
		
		//create button objects and paste on their names
	    for (int i = 0; i < tl.loopTracks.length; i++) {
	    	loopButtons[i] = new JButton(tl.loopTracks[i].name);
	   		loopButtons[i].addActionListener(this);
	   		panel.add(loopButtons[i]);
	   		loopButtons[i].setBounds(20 + (BUTTON_WIDTH * i)%(BUTTONS_PER_ROW * BUTTON_WIDTH), 10 + BUTTON_HEIGHT * (i/BUTTONS_PER_ROW), BUTTON_WIDTH, BUTTON_HEIGHT);
	    }
	    
	    for (int i = 0; i < tl.ambientTracks.length; i++) {
	    	ambientButtons[i] = new JButton(tl.ambientTracks[i].name);
	    	ambientButtons[i].addActionListener(this);
	   		panel.add(ambientButtons[i]);
	   		ambientButtons[i].setBounds(20 + (BUTTON_WIDTH * i)%(BUTTONS_PER_ROW * BUTTON_WIDTH), 50 + BUTTON_HEIGHT * (i/BUTTONS_PER_ROW), BUTTON_WIDTH, BUTTON_HEIGHT);
	    }
	    
	    for (int i = 0; i < tl.cinematicTracks.length; i++) {
	    	cinematicButtons[i] = new JButton(tl.cinematicTracks[i].name);
	    	cinematicButtons[i].addActionListener(this);
	   		panel.add(cinematicButtons[i]);
	   		cinematicButtons[i].setBounds(20 + (BUTTON_WIDTH * i)%(BUTTONS_PER_ROW * BUTTON_WIDTH), 90 + BUTTON_HEIGHT * (i/BUTTONS_PER_ROW), BUTTON_WIDTH, BUTTON_HEIGHT);
	    }
	    
	    //create static buttons/other UI areas
	    initArea = new JTextArea();
	    initArea.setLineWrap(true);
	    initArea.setWrapStyleWord(true);
	    panel.add(initArea);
	    initArea.setBounds(20, 300, 300, 200);
	    refreshInitArea();
	    
	    numArea = new JTextArea();
	    numArea.setLineWrap(true);
	    numArea.setWrapStyleWord(true);
	    panel.add(numArea);
	    numArea.setBounds(330, 300, 190, 200);
	    
	    battleArea = new JTextArea();
	    battleArea.setLineWrap(true);
	    battleArea.setWrapStyleWord(true);
	    panel.add(battleArea);
	    battleArea.setBounds(620, 300, 500, 200);
	    
	    battleButton = new JButton("Generate Battle Order");
	    battleButton.addActionListener(this);
	    panel.add(battleButton);
	    battleButton.setBounds(20, 500, 500, 30);
	    
	    incrementTurn = new JButton("Increment Turn");
	    incrementTurn.addActionListener(this);
	    panel.add(incrementTurn);
	    incrementTurn.setBounds(620, 500, 500, 30);
	    
	    saveButton = new JButton("Save Current Team");
	    saveButton.addActionListener(this);
	    panel.add(saveButton);
	    saveButton.setBounds(20, 550, 500, 30);
	    
	    importButton = new JButton("Import New Tracks");
	    importButton.addActionListener(this);
	    panel.add(importButton);
	    importButton.setBounds(1050, 600, 150, 30);
	    
	    soundBar = new JSoundBar();
	    panel.add(soundBar);
	    soundBar.setBounds(20, 200, 1200, 30);
	    
	    //engage panel
	    this.add(panel);
	    this.setVisible(true);
	}
	
	public void refreshInitArea() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\Connor\\Desktop\\SoundBoard\\Resources\\_settings.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				initArea.append(line + "\n");
			}
		} catch (Exception e) {
			System.err.println("Error: Failed to pull data from _settings.txt");
			//System.err.println(e);
		}
	}
	
	public void saveInitArea() {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("C:\\Users\\Connor\\Desktop\\SoundBoard\\Resources\\_settings.txt"));
			bw.write(initArea.getText());
			bw.close();
		} catch (Exception e) {
			System.err.println("Error: Failed to pull data from _settings.txt");
			//System.err.println(e);
		}
	}
	
	public void stopAllLoopTracks() {
		for (int i = 0; i < loopButtons.length; i++) {
			tl.loopTracks[i].stop();
		}
	}
	
	public void stopAllAmbientTracks() {
		for (int i = 0; i < ambientButtons.length; i++) {
			tl.ambientTracks[i].stop();
		}
	}

	public void stopAllCinematicTracks() {
		for (int i = 0; i < cinematicButtons.length; i++) {
			tl.cinematicTracks[i].stop();
		}
	}
	
	public void stopAllTracks() {
		stopAllLoopTracks();
		stopAllAmbientTracks();
		stopAllCinematicTracks();
		
		//TODO: play transition
	}
	
	//handle button presses
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == importButton) {
			new TrackImporter(tl);
		} else if (e.getSource() == incrementTurn) {
			
		} else if (e.getSource() == battleButton) {
			
		} else if (e.getSource() == saveButton) {
			saveInitArea();
		} else if (e.getSource() == stopButton) {
			soundBar.setTrackNull();
			stopAllTracks();
		} else {
			
			int type = getTrackType(e);
			if (type == TYPE_LOOP) {
				
				stopAllCinematicTracks();
				//turn off all loop tracks except for the clicked one
				for (int i = 0; i < loopButtons.length; i++) {
					if (e.getSource() == loopButtons[i]) {
						tl.loopTracks[i].play();
						soundBar.setTrack(tl.loopTracks[i]);
					} else {
						tl.loopTracks[i].stop();
					}
				}
			} else if (type == TYPE_AMBIENT) {
			
				//turn off all ambient tracks, toggle the clicked one
				for (int i = 0; i < ambientButtons.length; i++) {
					if (e.getSource() == ambientButtons[i]) {
						tl.ambientTracks[i].toggle();
						soundBar.setTrack(tl.ambientTracks[i]);
					} else {
						tl.ambientTracks[i].stop();
					}
				}
			} else if (type == TYPE_CINEMATIC) {
				
				stopAllLoopTracks();
				//turn off all cinematic tracks except for the clicked one
				for (int i = 0; i < cinematicButtons.length; i++) {
					if (e.getSource() == cinematicButtons[i]) {
						tl.cinematicTracks[i].nextSegment();
						soundBar.setTrack(tl.cinematicTracks[i]);
					} else {
						tl.cinematicTracks[i].stop();
					}
				}
			} else if (type == TYPE_UNKNOWN) {
				soundBar.setTrackNull();
				System.err.println("Error: type unknown");
			}
		}
	}
  
	private int getTrackType(ActionEvent e) {
		for (int i = 0; i < loopButtons.length; i++) {
			if (e.getSource() == loopButtons[i]) {
				return TYPE_LOOP;
			}
		}
		for (int i = 0; i < ambientButtons.length; i++) {
			if (e.getSource() == ambientButtons[i]) {
				return TYPE_AMBIENT;
			}
		}
		for (int i = 0; i < cinematicButtons.length; i++) {
			if (e.getSource() == cinematicButtons[i]) {
				return TYPE_CINEMATIC;
			}
		}
		return TYPE_UNKNOWN;
	}
	
	//start up program
	public static void main(String[] args) {
		new SoundBoard();
	}
}