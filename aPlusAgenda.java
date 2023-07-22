import java.util.*;
import javax.swing.*;
import java.awt.*;

public class APlusAgenda extends JFrame {
	final int W = 1920;//width
	final int H = 1080;//height
	JFrame f;

    // Color lineColor = new Color(182, 190, 216, 104);
    // Color textColor = new Color(23, 44, 112);
    // Font typeFont = new Font("Montserrat", Font.BOLD, 24);
    // Font monthFont = new Font("Montserrat", Font.PLAIN, 56);
    // Font timeFont = new Font("Montserrat", Font.PLAIN, 20);
	
    public static void main(String[] args) {
		new APlusAgenda();
    }

    APlusAgenda() {
    	setup();
        // weeklyView();
    }
    
    void setup() {
		f = new JFrame("A+ Agenda");
		f.setSize(W,H);
		frame.setLayout(new GridBagLayout());
		f.setLocationRelativeTo(null);
		f.setVisible(true);

    	// gc.setBackgroundColor(new Color(223, 231, 255));
    	// gc.setColor(new Color(182, 190, 216));
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