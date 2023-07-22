import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.HashMap;

public class APlusAgenda{
	final int W = 1920; // width
	final int H = 1080; // height
	final int HourHeight = 100;

	JFrame f;

	//HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

    // Color lineColor = Color(182, 190, 216, 104);
    // Color textColor = Color(23, 44, 112);
    // Font typeFont = Font("Montserrat", Font.BOLD, 24);
    // Font monthFont = Font("Montserrat", Font.PLAIN, 56);
    // Font timeFont = Font("Montserrat", Font.PLAIN, 20);

    public static void main(String[] args) {
		new APlusAgenda();
    }

    APlusAgenda() {
    	setup();
    }

    void setup() {
		f = new JFrame ("A+ Agenda");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		f.getContentPane().setBackground(new Color(223, 231, 255));
		f.setSize(W,H);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		JPanel backPanel = new JPanel();
		backPanel.setBackground(new Color(182, 190, 216));
		backPanel.setBounds(0,0,W,500);
		backPanel.setPreferredSize(new Dimension(100, 100));

		f.add(backPanel, BorderLayout.NORTH);

		JPanel linePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(new Color(182, 190, 216));
				((Graphics2D) g).setStroke(new BasicStroke(2));

				weeklyView(g);
			}
		};

		f.add(linePanel, BorderLayout.CENTER);


    	// f.setColor(Color(182, 190, 216));
		// g.fillRect(0,0,W,100);

    }

    void weeklyView(Graphics g) {
		drawLines(g);
    	//dates MAY CHANGE LATER BASED ON IMPORTED DATA??
    	g.drawString("June 2023", 830, 70);
		for (int i = 0; i < 9; i++) {
			// times are 8 am to 4 pm
			g.drawString((i + 8) + "am", 0, 155 + i * HourHeight);
		}
    }

	void drawLines(Graphics g) {
		for (int y = 150; y <= 1050; y += HourHeight) {
			g.drawLine(50, y, W, y);
		}
		for (int x = 50; x <= 1670; x += 270) {
			g.drawLine(x, 100, x, H);
		}
	}
}
