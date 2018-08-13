import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
/*
 * TrackImporter - UI class handling adding or removing data from resources
 * This class will allow the user to add new tracks to the main soundboard UI
 * The user will specify which type of track they want, and provide any
 * additional information needed to properly structure the data
 * 
 * Structure:
 * class points to Resources folder. in this folder, we have these sub-folders:
 * 		Ambient - contains sound files, and a file called info.txt which contains
 * 				the button names for each sound file in order
 * 		Cinematic - contains folders, and one file called info.txt along with them.
 * 				within folders, ordered list of sound files (labeled 1 though n).
 * 				each line of info.txt contains the button name for one of the folders
 * 				followed by a sequence of 0's and 1's, indicating which sections
 * 				of the songs should loop or not.
 * 		Loop - contains folders, and one file called info.txt along. within folders,
 * 				any number of sound files are added, all assumed to be coterminal.
 * 				like before, info.txt contains button names in order
 * 		Transition - contains sound files with transition types as filenames. only
 * 					a certain, specified number of these will ever exist here, and
 * 					additions are not possible, only replacements
 */
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TrackImporter extends JFrame implements ActionListener {
	JPanel panel;
	private JButton importButton, ambientButton, cinematicButton, loopButton;
	private JTextArea fileArea;
	
	//TODO:
	//-accept trackloader here, update the files in it 
	//-also, make separate importer for each track type
	//-make transition handler for each import, choose in and out transitions
	//-make the button creation in an updateButtons() function in soundboard
	//so we can update without restarting program
	
	public TrackImporter(TrackLoader tl) {
		panel = new JPanel() {
		      public void paintComponent(Graphics g) {
		    	  super.paintComponent(g);
		      }
		};
		panel.setLayout(null);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //maybe use EXIT_ON_CLOSE to force a reload
		this.setTitle("Import New Tracks");
		this.setSize(new Dimension(480, 400));
		
		ambientButton = new JButton("Ambient");
		ambientButton.addActionListener(this);
	    panel.add(ambientButton);
	    ambientButton.setBounds(10, 10, 140, 30);
	    
	    loopButton = new JButton("Loop");
	    loopButton.addActionListener(this);
	    panel.add(loopButton);
	    loopButton.setBounds(160, 10, 140, 30);
	    
	    cinematicButton = new JButton("Cinematic");
	    cinematicButton.addActionListener(this);
	    panel.add(cinematicButton);
	    cinematicButton.setBounds(310, 10, 140, 30);
	    
	    fileArea = new JTextArea();
	    fileArea.setLineWrap(true);
	    fileArea.setWrapStyleWord(true);
	    panel.add(fileArea);
	    fileArea.setBounds(10, 50, 440, 250);
	    fileArea.setEditable(false);
		
		importButton = new JButton("Import");
	    importButton.addActionListener(this);
	    panel.add(importButton);
	    importButton.setBounds(10, 310, 440, 30);
	    importButton.setEnabled(false);
		
		this.add(panel);
	    this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
}
