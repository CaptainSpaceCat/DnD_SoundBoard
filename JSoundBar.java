import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class JSoundBar extends JComponent implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = -7412983006691512676L;

	private Timer animationTimer;
	
	private SoundTrack track;
	
	public JSoundBar() {
		animationTimer = new Timer(100, this);
		animationTimer.start();
		addMouseListener(this);
	}
	
	public void setTrack(SoundTrack st) {
		if (track == null || st == null || st.priority <= track.priority) {
			track = st;
		}
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		if (track == null) {
			graphics.setColor(Color.gray);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

		} else {
			track.drawSelf(this.getBounds(), graphics);
		}
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
		if (track != null) {
			int x = e.getX();
			float percent = ((float)x)/this.getWidth();
			track.setTrackPercent(percent);
		}
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
