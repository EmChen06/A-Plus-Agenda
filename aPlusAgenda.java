import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.TextStyle;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.time.temporal.ChronoUnit;
import java.awt.Color;
import java.util.regex.Pattern;

public class aPlusAgenda extends JFrame {

	public class Plan {
	    LocalTime startTime, endTime;
	    String name, type;
	    Color color;

	    Plan(LocalTime st, LocalTime et, String initName, String type, Color initColor) {
	        this.type = type;
	        this.startTime = st;
	        this.endTime = et;
	        this.name = initName;
	        this.color = initColor;
	    }

	    public double getDuration() { // in hours
	        return (double) startTime.until(endTime, ChronoUnit.MINUTES) / 60.0;
	    }

		public double getStartHour() {
			double hour = (double)(this.startTime.getHour());
			hour += (double)(this.startTime.getMinute()) / 60;
			return hour;
		}

		public double getEndHour() {
			double hour = (double)(this.endTime.getHour());
			hour += (double)(this.endTime.getMinute()) / 60;
			return hour;
		}

		public boolean overlaps(Plan e) {
			//double thisEnd = this.getEndHour() - 0.001;
			//double eEnd = e.getEndHour() - 0.001;
			boolean bad1 = e.getStartHour() <= this.getStartHour() && this.getStartHour() < e.getEndHour();
			boolean bad2 = this.getStartHour() <= e.getStartHour() && e.getStartHour() < this.getEndHour();
			return bad1 || bad2;
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
	final Color BackgroundColour = new Color(223, 231, 255);
    Font monthFont = new Font("Bahnschrift", Font.PLAIN, 56);
    Font dayFont = new Font("Bahnschrift", Font.BOLD, 24);
    Font timeFont = new Font("Bahnschrift", Font.PLAIN, 20);

	final int[] TestStudies = { -7, -4, -2, -1 };
	final int TestStudyLength = 120;
	final int[] QuizStudies = { -3, -1 };
	final int QuizStudyLength = 90;
	final int[] AssignmentStudies = { -14, -12, -10, -8, -6, -4, -3, -2, -1 };
	final int AssignmentStudyLength = 60;
	final int[] ExamStudies = { -28, -24, -21, -19, -17, -14, -13, -12, -10, -9, -7, -6, -5, -4, -3, -2, -1 };
	final int ExamStudyLength = 120;

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
			LocalTime start = LocalTime.of(hour, 0, 0);

			LocalDate date = sundayToDisplay.plusDays(day);

			ArrayList<Plan> currentEvents = events.get(date);
			if (currentEvents != null) {
				for (Plan e : events.get(date)) {
					if (e.getStartHour() <= hour && hour <= e.getEndHour()) return; // multiple events at same time
				}
			} else events.put(date, currentEvents = (new ArrayList<Plan>()));

			String name = JOptionPane.showInputDialog(that, "What is the name of this event?");
			if (name == null) return; // text input was cancelled
			if (name.equals("")) {
				JOptionPane.showMessageDialog(that, "Invalid name");
				return;
			}

			double closestHour = NumHours + StartingHour;
			for (Plan e : currentEvents) {
				if (hour < e.getStartHour() && e.getStartHour() < closestHour) {
					closestHour = e.getStartHour();
				}
			}

			//int hoursFree = (int)((double)closestHour - (double)hour);
			//String times[] = new String[hoursFree];
			//for (int i = 0; i < hoursFree; i++) times[i] = String.valueOf(i+1);
			//String timeStr = (String)JOptionPane.showInputDialog(that, "", "How many hours will this event take?", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);
			//if (timeStr == null) return; // time was cancelled
			//int length = Integer.parseInt(timeStr);

			JOptionPane.showMessageDialog(that, "When will this event end?");
			LocalTime end = getValidTime();

			Plan event = new Plan(start, end, name, "", Color.BLUE);
			if (18 <= end.getHour() || event.getEndHour() <= event.getStartHour()) {
				JOptionPane.showMessageDialog(that, "Error: invalid end time");
				return;
			}

			for (Plan e : currentEvents) {
				if (event.overlaps(e)) {
					JOptionPane.showMessageDialog(that, "Error: event overlaps with another event.");
					return;
				}
			}

			String eventTypes[] = {"Test", "Quiz", "Assignment", "Exam", "Other"}; //maybe add extracurricular later
			String type = (String)JOptionPane.showInputDialog(that, "", "What type of event is this?", JOptionPane.QUESTION_MESSAGE, null, eventTypes, eventTypes[0]);
			if (type == null) return; // event type was cancelled
			event.type = type;

			int[] extraSessions;
			int extraSessionLength = 0;
			String sessionName = "";
			switch (type) {
				case "Test":
					extraSessions = TestStudies;
					extraSessionLength = TestStudyLength;
					sessionName = "Test Study Session";
					break;
				case "Quiz":
					extraSessions = QuizStudies;
					extraSessionLength = QuizStudyLength;
					sessionName = "Test Study Session";
					break;
				case "Assignment":
					extraSessions = AssignmentStudies;
					extraSessionLength = AssignmentStudyLength;
					sessionName = "Assignment Work Session";
					break;
				case "Exam":
					extraSessions = ExamStudies;
					extraSessionLength = ExamStudyLength;
					sessionName = "Exam Study Session";
					break;
				default:
					extraSessions = new int[0];
					break;
			}

			for (int dayOffset : extraSessions) {
				LocalDate extraDate = date.plusDays(dayOffset);
				if (!events.containsKey(extraDate)) events.put(extraDate, new ArrayList<Plan>());
				boolean validTime = false;
				int tryTime = 15;
				Plan tempPlan = new Plan(LocalTime.of(tryTime, 0, 0), LocalTime.of(tryTime, 0, 0).plusMinutes(extraSessionLength), name, sessionName, Color.RED);
				while (!validTime && tryTime >= 8) {
					tempPlan.startTime = LocalTime.of(tryTime, 0, 0);
					tempPlan.endTime = LocalTime.of(tryTime, 0, 0).plusMinutes(extraSessionLength);
					validTime = true;
					for (Plan p : events.get(extraDate)) {
						if (tempPlan.overlaps(p)) validTime = false;
					}
					if (!validTime) tryTime--;
				}
				if (tryTime < 8) continue;
				events.get(extraDate).add(tempPlan);
			}

			currentEvents.add(event);
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

	
	class myKeyListener implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_LEFT) {
				sundayToDisplay = sundayToDisplay.minusDays(7);
				
			}
			else if (keyCode == KeyEvent.VK_RIGHT) {
				sundayToDisplay = sundayToDisplay.plusDays(7);
			}

			graphics.repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) { }

		@Override
		public void keyTyped(KeyEvent e) { }
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

	LocalTime getValidTime() {
		boolean allowed = true;
		String timeStr;
        do {
			String message = "Please enter a time (hh:mm)";
			if (!allowed) message = "Invalid time. Massive L. " + message;
            timeStr = (String)(JOptionPane.showInputDialog(message));
            allowed = Pattern.matches("\\d\\d?:\\d\\d", timeStr);
        } while (!allowed);

		String[] parts = timeStr.split(":");
		int endHour = Integer.parseInt(parts[0]);
		if (endHour < 8) endHour += 12; // pm times
		int endMinute = Integer.parseInt(parts[1]);
		LocalTime time = LocalTime.of(endHour, endMinute, 0);
		return time;
	}

    void setup() {
		this.setTitle("A+ Agenda");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().setBackground(BackgroundColour);

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

		myKeyListener mkl = new myKeyListener();
		this.addKeyListener(mkl);

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
		drawLines(g);
		g.setColor(textColor);
		g.setFont(monthFont);
		String month = sundayToDisplay.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		int year = sundayToDisplay.getYear();
    	drawCentredString(g, month + " " + year, W/2, CalendarTop * 2/5);
		drawTimes(g);
		drawDates(g);
		drawEvents(g);
    }

	void drawLines(Graphics g) {
		g.setColor(lineColor);
		for (int i = 0; i < 8; i++) {
			int x = CalendarLeft + i * DayWidth;
			if (sundayToDisplay.plusDays(i).equals(LocalDate.now())) {
				g.setColor(new Color(203, 213, 242));
				g.fillRect(x, CalendarTop, DayWidth, CalendarBottom - CalendarTop);
				g.setColor(lineColor);
			}
		}
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
			for (Plan e : plans) {
				int y = CalendarTop + (int)(e.getStartHour() - StartingHour) * HourHeight;
				g.setColor(new Color(e.color.getRed(), e.color.getBlue(), e.color.getGreen(), 128));
				g.fillRoundRect(x, y, DayWidth, (int)((double)HourHeight * e.getDuration()), 20, 20);
				g.setColor(new Color(0, 0, 0, 50));
				g.setFont(dayFont);
				drawCentredString(g, e.name, x + DayWidth/2, y + HourHeight * 1/3);
				g.setFont(timeFont);
				g.setColor(textColor);
				drawCentredString(g, e.type, x + DayWidth/2, y + HourHeight * 2/3);
			}
		}
	}
}
