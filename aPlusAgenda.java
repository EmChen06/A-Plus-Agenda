import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.HashMap;

public class APlusAgenda{
	final int W = 1920;//width
	final int H = 1080;//height
	JFrame f;

	HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

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
        // weeklyView();
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

				g.drawLine(50, 150, W, 150);
				g.drawLine(50, 250, W, 250); 
				g.drawLine(50, 350, W, 350); 
				g.drawLine(50, 450, W, 450); 
				g.drawLine(50, 550, W, 550); 
				g.drawLine(50, 650, W, 650); 
				g.drawLine(50, 750, W, 750); 
				g.drawLine(50, 850, W, 850); 
				g.drawLine(50, 950, W, 950); 
				g.drawLine(50, 1050, W, 1050);
				g.drawLine(50, 100, 50, H);
				g.drawLine(320, 100, 320, H);
				g.drawLine(590, 100, 590, H);
				g.drawLine(860, 100, 860, H);
				g.drawLine(1130, 100, 1130, H);
				g.drawLine(1400, 100, 1400, H);
				g.drawLine(1670, 100, 1670, H);

			}
		};

		f.add(linePanel, BorderLayout.CENTER);

		
    	// f.setColor(Color(182, 190, 216));
		// gc.fillRect(0,0,W,100);

    }
    
    void weeklyView() {

    	// //times
    	// gc.drawLine(50, 150, W, 150);
    	// gc.drawLine(50, 250, W, 250); 
    	// gc.drawLine(50, 350, W, 350); 
    	// gc.drawLine(50, 450, W, 450); 
    	// gc.drawLine(50, 550, W, 550); 
    	// gc.drawLine(50, 650, W, 650); 
    	// gc.drawLine(50, 750, W, 750); 
    	// gc.drawLine(50, 850, W, 850); 
    	// gc.drawLine(50, 950, W, 950); 
    	// gc.drawLine(50, 1050, W, 1050);
    	// gc.drawLine(50, 100, 50, H);
    	// gc.drawLine(320, 100, 320, H);
    	// gc.drawLine(590, 100, 590, H);
    	// gc.drawLine(860, 100, 860, H);
    	// gc.drawLine(1130, 100, 1130, H);
    	// gc.drawLine(1400, 100, 1400, H);
    	// gc.drawLine(1670, 100, 1670, H);
    	// //dates MAY CHANGE LATER BASED ON IMPORTED DATA??
    	// gc.drawString("June 2023", 830, 70);
    	// gc.drawString("8am", 0, 155);
    	// gc.drawString("9am", 0, 255);
    	// gc.drawString("10am", 0, 355);
    	// gc.drawString("11am", 0, 455);
    	// gc.drawString("12pm", 0, 555);
    	// gc.drawString("1pm", 0, 655);
    	// gc.drawString("2pm", 0, 755);
    	// gc.drawString("3pm", 0, 855);
    	// gc.drawString("4pm", 0, 955);
    }
}