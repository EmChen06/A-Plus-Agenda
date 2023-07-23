import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.TextStyle;
import java.awt.event.*;

public class aPlusAgenda extends JFrame {
	class Plan {
		String name, type;
		int hour, length;
		Plan(String n, String t, int h, int l) {
			name = n;
			type = t;
			hour = h;
			length = l;
		}
	}

	final int W = 1920; // width
	final int H = 1080; // height

	final int StartingHour = 8;
	final int NumHours = 10; // 8 am (StartingHour) to 5 pm
	final int CalendarTop = 150;
	final int CalendarBottom = 1000;
	final int CalendarLeft = 100;
	final int CalendarRight = W - 50;
	final int HourHeight = (CalendarBottom - CalendarTop) / NumHours;
	final int DayWidth = (CalendarRight - CalendarLeft) / 7;
	ImageIcon imageBorder = new ImageIcon("agendaBorder.png");

	LocalDate sundayToDisplay; // for weekly view, the sunday of the week we're displaying

	HashMap<LocalDate, ArrayList<Plan>> events = new HashMap<LocalDate, ArrayList<Plan>>();

    Color lineColor = new Color(182, 190, 216, 104);
    Color textColor = new Color(23, 44, 112);
    Font monthFont = new Font("Bahnschrift", Font.PLAIN, 56);
    Font dayFont = new Font("Bahnschrift", Font.BOLD, 24);
    Font timeFont = new Font("Bahnschrift", Font.PLAIN, 20);

    public static void main(String[] args) {
		new aPlusAgenda();
    }

	JPanel graphics;
	JFrame that = this;

	class myMouseListener implements MouseListener {

	    @Override
	    public void mouseClicked(MouseEvent click) { 
			int x = click.getX();
			int y = click.getY();
			int day = (x - CalendarLeft) / DayWidth;
			if (day < 0 || 6 < day) return; // invalid click
			int hour = (y - CalendarTop) / HourHeight + StartingHour;
			if (hour < StartingHour || StartingHour + NumHours <= hour) return; // invalid click

			LocalDate date = sundayToDisplay.plusDays(day);

			ArrayList<Plan> currentEvents = events.get(date);
			if (currentEvents != null) {
				for (Plan p : events.get(date)) {
					if (p.hour <= hour && hour <= p.hour + p.length) return; // multiple events at same time
				}
			} else events.put(date, currentEvents = (new ArrayList<Plan>()));

			String name = JOptionPane.showInputDialog(that, "What is the name of this event?");
			if (name == null) return; // text input was cancelled
			if (name.equals("")) {
				JOptionPane.showMessageDialog(that, "Invalid name");
				return;
			}

			int closestHour = NumHours + StartingHour;
			for (Plan p : currentEvents) {
				if (hour < p.hour && p.hour < closestHour) {
					closestHour = p.hour;
				}
			}
			int hoursFree = closestHour - hour;
			String times[] = new String[hoursFree];
			for (int i = 0; i < hoursFree; i++) times[i] = String.valueOf(i+1);
			String timeStr = (String)JOptionPane.showInputDialog(that, "", "How many hours will this event take?", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);
			if (timeStr == null) return; // time was cancelled
			int length = Integer.parseInt(timeStr);

			String eventTypes[] = {"Test", "Quiz", "Project/Assignment", "Exam", "Other"}; //maybe add extracurricular later
			String type = (String)JOptionPane.showInputDialog(that, "", "What type of event is this?", JOptionPane.QUESTION_MESSAGE, null, eventTypes, eventTypes[0]);
			if (type == null) return; // event type was cancelled

			Plan plan = new Plan(name, type, hour, length);

			currentEvents.add(plan);
			graphics.repaint();
	    }

	    @Override
	    public void mouseEntered(MouseEvent click) { }

	    @Override
	    public void mouseExited(MouseEvent click) { }

	    @Override
	    public void mousePressed(MouseEvent click) { }

	    @Override
	    public void mouseReleased(MouseEvent click) { }
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
		this.setTitle("A+ Agenda");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().setBackground(new Color(223, 231, 255));

		//JPanel backPanel = new JPanel();
		//backPanel.setBackground(new Color(182, 190, 216));
		//backPanel.setBounds(0,0,W,500);
		//backPanel.setPreferredSize(new Dimension(100, 100));
		//f.add(backPanel, BorderLayout.NORTH);

		JPanel linePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(new Color(182, 190, 216));
				((Graphics2D) g).setStroke(new BasicStroke(2));

				weeklyView(g);
			}
		};
		linePanel.setPreferredSize(new Dimension(W, H));
		linePanel.setBounds(0, 0, W, H);
		linePanel.setOpaque(false);
		this.add(linePanel, BorderLayout.CENTER);
		graphics = linePanel;

		myMouseListener mml = new myMouseListener();
		linePanel.addMouseListener(mml);


		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	public void drawCentredString(Graphics g, String text, int x, int y) {
	    FontMetrics fm = g.getFontMetrics(g.getFont());
	    int newX = x - fm.stringWidth(text)/2;
	    int newY = y + fm.getHeight()/2 - fm.getAscent()/2; // this doesn't work properly but looks centred enough to me
	    g.drawString(text, newX, newY);
	}

    void weeklyView(Graphics g) {
		g.setColor(lineColor);
		drawLines(g);
		g.setColor(textColor);
    	//dates MAY CHANGE LATER BASED ON IMPORTED DATA??
		g.setFont(monthFont);
    	drawCentredString(g, "June 2023", W/2, CalendarTop * 2/5);
		drawTimes(g);
		drawDates(g);
		drawEvents(g);
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
			g.setFont(timeFont);
			drawCentredString(g, twelveHour + " " + amOrPm, CalendarLeft / 2, CalendarTop + i * HourHeight + 10);
		}
	}

	void drawDates(Graphics g) {
		for (int i = 0; i < 7; i++) {
			LocalDate date = sundayToDisplay.plusDays(i);
			int day = date.getDayOfMonth();
			String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
			int x = CalendarLeft + i * DayWidth + DayWidth / 2;
			g.setFont(dayFont);
			drawCentredString(g, dayName + ", " + day, x, CalendarTop * 4/5);
		}
	}

	void drawEvents(Graphics g) {
		for (int i = 0; i < 7; i++) {
			int x = CalendarLeft + i * DayWidth;
			LocalDate date = sundayToDisplay.plusDays(i);
			if (!events.containsKey(date)) continue;

			ArrayList<Plan> plans = events.get(date);
			for (Plan p : plans) {
				int y = CalendarTop + (p.hour - StartingHour) * HourHeight;
				g.setColor(lineColor);
				g.fillRect(x, y, DayWidth, HourHeight * p.length);
				g.setColor(Color.BLACK);
				drawCentredString(g, p.name, x + DayWidth/2, y + HourHeight * 1/3);
				g.setFont(timeFont);
				g.setColor(textColor);
				drawCentredString(g, p.type, x + DayWidth/2, y + HourHeight * 2/3);
			}
		}
	}
}
