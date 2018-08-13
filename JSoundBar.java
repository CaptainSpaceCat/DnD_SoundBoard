import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.Timer;

public class JSoundBar extends JComponent implements ActionListener, MouseListener {
	
	int PIN_WIDTH;
	
	final int TYPE_UNKNOWN = -1;
	final int TYPE_LOOP = 0;
	final int TYPE_AMBIENT = 1;
	final int TYPE_CINEMATIC = 2;
	
	private int type = TYPE_UNKNOWN;
	
	private Timer animationTimer;
	
	private LoopTrack loop;
	private AmbientTrack ambient;
	private CinematicTrack cinematic;
	
	public JSoundBar() {
		this(8);
	}
	
	public JSoundBar(int w) {
		animationTimer = new Timer(100, this);
		animationTimer.start();
		addMouseListener(this);
		PIN_WIDTH = w;
	}
	
	public void setTrackNull() {
		type = TYPE_UNKNOWN;
		repaint();
	}
	public void setTrack(LoopTrack track) {
		loop = track;
		type = TYPE_LOOP;
		repaint();
	}
	public void setTrack(AmbientTrack track) {
		if (type == TYPE_UNKNOWN) {
			ambient = track;
			type = TYPE_AMBIENT;
			repaint();
		}
	}
	public void setTrack(CinematicTrack track) {
		cinematic = track;
		type = TYPE_CINEMATIC;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		if (type == TYPE_LOOP) {
			graphics.setColor(Color.blue);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
				
			drawPin(loop.getTrackPosition(), loop.getTrackLength(), graphics);
		} else if (type == TYPE_AMBIENT) {
			graphics.setColor(Color.orange);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			drawPin(ambient.getTrackPosition(), ambient.getTrackLength(), graphics);
		} else if (type == TYPE_CINEMATIC) {
			graphics.setColor(Color.blue);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			int[] repeatedSegments = cinematic.getRepeatedSegments();
			for (int i = 0; i < repeatedSegments.length; i+=2) {
				if (i == cinematic.getCurrentRepeatingSegment() * 2) {
					graphics.setColor(Color.yellow);
				} else {
					graphics.setColor(Color.cyan);
				}
				int x = (int)((((float)repeatedSegments[i])/cinematic.getTrackLength()) * this.getWidth());
				int y = (int)((((float)repeatedSegments[i + 1])/cinematic.getTrackLength()) * this.getWidth());
				int width = y-x;
				graphics.fillRect(x, 0, width, this.getHeight());
			}
			
			int segment = cinematic.getCurrentSegment();
			//drawPin(, graphics);
		} else {
			graphics.setColor(Color.gray);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}
	
	private void drawPin(int framePos, int maxPos, Graphics graphics) {
		int pixelPos = (int)((((float)framePos)/maxPos) * this.getWidth());
		graphics.setColor(Color.black);
		graphics.fillRect(pixelPos, 0, PIN_WIDTH, this.getHeight());
	}
	
	private void drawPin(float percent, Graphics graphics) {
		int pixelPos = (int)(percent * this.getWidth());
		graphics.setColor(Color.black);
		graphics.fillRect(pixelPos, 0, PIN_WIDTH, this.getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == animationTimer) {
			repaint();
			animationTimer.restart();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		float percent = ((float)x)/this.getWidth();
		loop.setTrackPosition((int)(percent * loop.getTrackLength()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
