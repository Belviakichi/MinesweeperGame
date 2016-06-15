import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Time;

public class GUI implements ActionListener, MouseListener
{
	private static int screenHeight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	private JButton playAgain; //click if you want to play another board of same size
	private JButton[][] buttons;//grid of buttons forming game board
	private JFrame f;//frame for game board
	private JPanel panel;//panel for game board
	private JPanel kaboom;//panel for kaboom window
	private JPanel win;//panel for win window
	private JPanel ask;//panel for starting window asking user what size of board they want to play
	private Minesweeper game;//the actual game
	private GridBagConstraints cc;//layout for the panels
	private JButton current;//button the mouse is currently in (used for right clicks)
	private int currentr;//row location of current button
	private int currentc;//column location of current button
	private JButton go;//go button to start game on ask panel
	private JLabel boom;//"kaboom" label on kaboom panel
	private JLabel yay;//"you win" label on win panel
	private JLabel boardSize;//"what size?" label on ask panel
	private JLabel width;//"width: " label on ask panel
	private JLabel length;//"length: " label on ask panel
	private JTextField linput;//user input for length
	private JTextField winput;//user input for width
	private int l;//length of board (set to user input) (rows)
	private int w;//width of board(set to user input) (cols)
	private Random gen;//random number generator used to assign a random label to squares marked as mines
	private String[] labels;//string array for random labels
	private ImageIcon dog; //DON'T GET RID OF - sets the image to nothing because dog is blank
	private BufferedImage[][] pics;//buffered image two dimensional array for picture pieces from ImageSplit
	private ImageSplit choppitychop;//ImageSplit object for the image on top of the buttons
	private ImageIcon currentPic; //used in for loop when creating buttons
	private Date start; //date object instantiated in setUp() to begin timing
	private Date end; //date object instantiated once you win to end timing
	private long startTime; //gets time when you start the game (start.getTime())
	private long endTime; //gets time when you end the game (end.getTime())
	private JLabel time; //label on win panel that says how many seconds it took you to solve the game
	
	public GUI()
	{
		dog=new ImageIcon(); //no image so shows background color and text
		labels=new String[]{"meow", "quack","boom","woof","poof","brrt", "moo", "dork", "click","zoom","oof","beep","boop","thud","plop"};//labels for mines
		gen=new Random();
		f=new JFrame();
		ask = new JPanel();
		cc=new GridBagConstraints();
		go=new JButton("Go");
		go.addActionListener(this);
		boardSize=new JLabel("What size?");
		width=new JLabel("Width: ");
		length=new JLabel("Length: ");
		linput=new JTextField(1);
		winput=new JTextField(1);
		cc.weightx=cc.weighty=1;//when you expand the screen, everything expands properly, and all the columns are distributed evenly
		cc.fill=GridBagConstraints.BOTH;//everything fills its space entirely
		ask.setLayout(new GridBagLayout());//make ask panel use grid bag layout
		cc.gridx=0;//x coordinate thingy on ask panel for board size label = 0 (column 0) 
		cc.gridy=0;//row 0 for board size label
		cc.gridwidth=4;//number of columns - label spans all four columns
		boardSize.setHorizontalAlignment(JLabel.CENTER);//centers label
		ask.add(boardSize,cc);//add label with constraints to the panel
		cc.gridy=1;//changes row 
		cc.gridwidth=1;//limits next label to one column
		ask.add(width, cc);//adds width label to panel
		cc.gridx=1;
		ask.add(winput, cc);
		cc.gridx=2;
		ask.add(length, cc);
		cc.gridx=3;
		ask.add(linput, cc);
		cc.gridy=2;
		cc.gridx=0;
		cc.gridwidth=4;
		ask.add(go, cc);
		f.setContentPane(ask);//ask what size of board the user wants to play
		f.pack();//necessary for things to show up
		f.setVisible(true);//makes frame visible
		f.setSize(400,100);//sets size of frame in pixels(length, height)
	}
	public void setUp() throws IOException//sets up minesweeper game board
	{
		choppitychop = new ImageSplit(l,w,screenHeight);
		pics = choppitychop.chop();//splits image split object and assigns pieces to pics
		panel = new JPanel();
		kaboom = new JPanel();
		win=new JPanel();
		panel.setLayout(new GridBagLayout());//sets layout of main game panel to grid bag stuff
		buttons = new JButton[l][w];
		current=new JButton();
		game = new Minesweeper(l,w);
		playAgain = new JButton("Play again?");
		playAgain.addActionListener(this);
		boom=new JLabel("kaboom");
		yay=new JLabel("You Didn't Explode. ");
		JButton button;//button used for current button when setting up the game board
		cc.weightx=cc.weighty=1;
		cc.fill=GridBagConstraints.BOTH;
		cc.gridx=0;
		//cc.gridy=buttons.length+1;
		cc.gridwidth=buttons.length;
		cc.gridy=0;
		win.add(yay);
		kaboom.add(boom);
		kaboom.add(playAgain);
		cc.gridwidth=1;
		for (int r=0; r<buttons.length; r++)//add picture and interactive stuff to buttons
			for (int c=0; c<buttons[0].length; c++)
			{
				button=new JButton(){
					public Dimension getPreferredSize(){
						return new Dimension(screenHeight/2/buttons[0].length,screenHeight/2/buttons.length);
					}
				};
				button.setBackground(Color.BLACK);//DO NOT REMOVE (extremely necessary for later)
				currentPic = new ImageIcon(pics[r][c]);
				button.setIcon(currentPic);//sets picture on button to the corresponding piece of the image split thing
				button.addActionListener(this);//allows button to do stuff on click
				button.addMouseListener(this);//enables right click to be used
				cc.gridx=c;
				cc.gridy=r;
				panel.add(button, cc);//adds button to panel
				buttons[r][c]=button;
			}
		f.setContentPane(panel);//makes the content of the frame the panel
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ends GUI when window is closed 
		f.pack();
		f.setVisible(true);
		f.setSize(screenHeight,screenHeight); //width, height
		start = new Date();
		startTime = start.getTime();
	}
	public void actionPerformed(ActionEvent e)//click
	{
		if (e.getSource()==go)//if you click on the go button, the board is created with specified number of squares from user input
		{
			w=Integer.parseInt(winput.getText());//reads user width input to variables to set size
			l=Integer.parseInt(linput.getText());//reads user length input
			try {
				setUp();
			} catch (IOException e1) {//if picture file not found
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == playAgain){ //set up another board of same size
			try {
				setUp();
			} catch (IOException e1) {//picture stuff
				e1.printStackTrace();
			}
		}
		else //click on square, you either blow up or clear one/many squares
		{
			for (int r=0; r<buttons.length; r++)
				for (int c=0; c<buttons[0].length; c++)
				{
					if (e.getSource()==buttons[r][c])
					{
						if (game.isMine(r,c))//if user clicks on a mine, a window pops up saying kaboom
						{
							f.setContentPane(kaboom);
							f.pack();
							f.setVisible(true);
							f.setSize(500,500);
						}
						else//clears open squares
						{
							game.clear(r, c);
							for (int row=0; row<game.getOpen().length; row++)
								for (int col=0; col<game.getOpen()[0].length; col++)
								{
									if (game.getOpen()[row][col])
									{
										buttons[row][col].setIcon(dog);
										buttons[row][col].setBackground(Color.white);
										buttons[row][col].setText("");
										if (game.getMines(row, col)!=0)///number stuff(number of mines touching the selected square
										{
											buttons[row][col].setText(Integer.toString(game.getMines(row, col)));
											if(l>=15 || w>=15)
												buttons[row][col].setFont(new Font("Sans Serif", Font.PLAIN, 10));
												
										}
									}
								}
						}
					}
				}
		}
		if (checkWin())//if all non-mine squares are cleared, a window comes up saying you win
		{
			end = new Date();
			endTime = end.getTime();
			time = new JLabel("It took you " + (endTime-startTime)/1000 + " seconds.");
			win.add(time);
			win.add(playAgain);
			f.setContentPane(win);
			f.pack();
			f.setVisible(true);
			f.setSize(500,500);
		}
		
	}
	
	public boolean checkWin()//check if all non mine squares have been cleared
	{
		int fc=0;
		for (int r=0; r<game.getOpen().length; r++)
			for (int c=0; c<game.getOpen()[0].length; c++)
			{
				if (!game.getOpen()[r][c])
				{
					fc++;
				}
			}
		return fc==game.getMines();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) //required to use right clicky stuff in the mousePressed method
	{
		for (int r=0; r<buttons.length; r++)
			for (int c=0; c<buttons[0].length; c++)
			{
				if (arg0.getSource()==buttons[r][c])
				{
					current=buttons[r][c];
					currentr = r;
					currentc = c;
				}
			}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if (arg0.getButton()==MouseEvent.BUTTON3)//right clicky stuff
		{ 
			if (current.getBackground().equals(Color.white))//can't flag already open square as a mine
				current.setBackground(Color.white);
			else if (current.getBackground().equals(Color.RED))//returns flagged squares to normal
			{
				currentPic = new ImageIcon(pics[currentr][currentc]);
				current.setIcon(currentPic);
				current.setBackground(Color.black);
			}
			
			else
			{
				current.setIcon(dog);
				current.setBackground(Color.RED);//flags a square as a mine upon right click
				int i=gen.nextInt(labels.length);//random number used for selecting a random label
				current.setText(labels[i]);//sets text to random label
				if(l>10 || w>10)
					current.setFont(new Font("Sans Serif", Font.PLAIN, 8));
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args)
	{
		GUI fun = new GUI();
	}
	
	
}