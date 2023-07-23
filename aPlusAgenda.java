import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.HashMap;
import java.time.format.TextStyle;

public class aPlusAgenda extends JFrame {
	final int W = 1920; // width
	final int H = 1080; // height

	final int StartingHour = 8;
	final int NumHours = 9; // 8 am (StartingHour) to 4 pm
	final int CalendarTop = 150;
	final int CalendarBottom = 1000;
	final int CalendarLeft = 100;
	final int CalendarRight = W - 50;
	final int HourHeight = (CalendarBottom - CalendarTop) / NumHours;
	final int DayWidth = (CalendarRight - CalendarLeft) / 7;

	LocalDate sundayToDisplay; // for weekly view, the sunday of the week we're displaying

	//JFrame f;

	//HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

    // Color lineColor = Color(182, 190, 216, 104);
    // Color textColor = Color(23, 44, 112);
    // Font typeFont = Font("Montserrat", Font.BOLD, 24);
    // Font monthFont = Font("Montserrat", Font.PLAIN, 56);
    // Font timeFont = Font("Montserrat", Font.PLAIN, 20);

    public static void main(String[] args) {
		new aPlusAgenda();
    }

	LocalDate lastSunday(LocalDate ld) {
		DayOfWeek d = ld.getDayOfWeek();
		int daysSinceSunday = d.getValue() % 7;
		return ld.minusDays(daysSinceSunday);
	}

    aPlusAgenda() {
		sundayToDisplay = lastSunday(LocalDate.now());
    	setup();
    }

    void setup() {
		//f = new JFrame ("A+ Agenda");
		this.setTitle("A+ Agenda");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().setBackground(new Color(223, 231, 255));
		this.setSize(W, H);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//JPanel backPanel = new JPanel();
		//backPanel.setBackground(new Color(182, 190, 216));
		//backPanel.setBounds(0,0,W,500);
		//backPanel.setPreferredSize(new Dimension(100, 100));
		//f.add(backPanel, BorderLayout.NORTH);

    	// f.setColor(Color(182, 190, 216));
		// g.fillRect(0,0,W,100);

    
	JPanel linePanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(new Color(182, 190, 216));
			((Graphics2D) g).setStroke(new BasicStroke(2));

			weeklyView(g);
		}
	};
		this.add(linePanel, BorderLayout.CENTER);
	}
	

	public void drawCentredString(Graphics g, String text, int x, int y) {
	    FontMetrics fm = g.getFontMetrics(g.getFont());
	    int newX = x - fm.stringWidth(text)/2;
	    int newY = y + fm.getHeight()/2 - fm.getAscent()/2; // this doesn't work properly but looks centred enough to me
	    g.drawString(text, newX, newY);
	}

    void weeklyView(Graphics g) {
		drawLines(g);
    	//dates MAY CHANGE LATER BASED ON IMPORTED DATA??
    	drawCentredString(g, "June 2023", W/2, CalendarTop * 2/5);
		drawTimes(g);
		drawDates(g);
    }

	void drawLines(Graphics g) {
		for (int i = 0; i <= NumHours; i++) {
			int y = CalendarTop + i * HourHeight;
			g.drawLine(CalendarLeft, y, CalendarRight, y);
		}
		for (int i = 0; i <= 8; i++) {
			int x = CalendarLeft + i * DayWidth;
			g.drawLine(x, CalendarTop, x, CalendarBottom);
		}
	}

	void drawTimes(Graphics g) {
		for (int i = 0; i < NumHours; i++) {
			int twentyFourHour = i + StartingHour;
			String amOrPm = twentyFourHour <= 11 ? "am" : "pm";
			int twelveHour = (twentyFourHour - 1) % 12 + 1;
			drawCentredString(g, twelveHour + " " + amOrPm, CalendarLeft / 2, CalendarTop + i * HourHeight + HourHeight/2);
		}
	}

	void drawDates(Graphics g) {
		for (int i = 0; i < 7; i++) {
			LocalDate date = sundayToDisplay.plusDays(i);
			int day = date.getDayOfMonth();
			String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
			int x = CalendarLeft + i * DayWidth + DayWidth / 2;
			drawCentredString(g, dayName + ", " + day, x, CalendarTop * 4/5);
		}
	}
}
