import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class GUI implements ActionListener, MouseListener
{
	private JButton[][] buttons;
	private JFrame f;
	private JPanel panel;
	private JPanel kaboom;
	private JPanel win;
	private JPanel ask;
	private Minesweeper game;
	private GridBagConstraints cc;
	private JButton current;
	private int currentr;
	private int currentc;
	private JButton go;
	private JLabel mineNum;
	private JLabel boom;
	private JLabel yay;
	private JLabel boardSize;
	private JLabel width;
	private JLabel length;
	private JTextField linput;
	private JTextField winput;
	private int l;
	private int w;
	private Random gen;
	private String[] labels;
	private ImageIcon cat;
	private ImageIcon dog;
	private BufferedImage[][] pics;
	private ImageSplit choppitychop;
	private ImageIcon currentPic;
	
	public GUI()
	{
		dog=new ImageIcon();
		cat=new ImageIcon("src/Eye.png");
		labels=new String[]{"meow", "quack","boom","woof","poof","brrt", "moo", "dork", "click","zoom","oof","beep","boop","thud","plop"};
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
		cc.weightx=cc.weighty=1;
		cc.fill=GridBagConstraints.BOTH;
		ask.setLayout(new GridBagLayout());
		cc.gridx=0;
		cc.gridy=0;
		cc.gridwidth=4;
		boardSize.setHorizontalAlignment(JLabel.CENTER);
		ask.add(boardSize,cc);
		cc.gridy=1;
		cc.gridwidth=1;
		ask.add(width, cc);
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
		f.setContentPane(ask);
		f.pack();
		f.setVisible(true);
		f.setSize(400,100);
	}
	public void setUp() throws IOException
	{
		choppitychop = new ImageSplit(w,l);
		pics = choppitychop.chop();
		panel = new JPanel();
		kaboom = new JPanel();
		win=new JPanel();
		panel.setLayout(new GridBagLayout());
		buttons = new JButton[l][w];
		current=new JButton();
		game = new Minesweeper(l,w);
		mineNum=new JLabel("Mines: "+game.getMines()){
			public Dimension getPreferredSize(){
				return new Dimension(750,50);
			}
		};
		boom=new JLabel("kaboom");
		yay=new JLabel("You Win");
		JButton button;
		cc.weightx=cc.weighty=1;
		cc.fill=GridBagConstraints.BOTH;
		cc.gridx=0;
		cc.gridy=buttons.length+1;
		cc.gridwidth=buttons.length;
		mineNum.setHorizontalAlignment(JLabel.CENTER);
		//panel.add(mineNum, cc);
		win.add(yay);
		kaboom.add(boom);
		cc.gridwidth=1;
		for (int r=0; r<buttons.length; r++)
			for (int c=0; c<buttons[0].length; c++)
			{
				button=new JButton(){
					public Dimension getPreferredSize(){
						return new Dimension(375/buttons[0].length,375/buttons.length);
					}
				};
				button.setBackground(Color.BLACK);
				currentPic = new ImageIcon(pics[r][c]);
				button.setIcon(currentPic);
				button.addActionListener(this);
				button.addMouseListener(this);
				cc.gridx=c;
				cc.gridy=r;
				panel.add(button, cc);
				buttons[r][c]=button;
			}
		f.setContentPane(panel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
		f.setSize(750,750); //width, height
	}
	public void actionPerformed(ActionEvent e)//click
	{
		if (e.getSource()==go)
		{
			w=Integer.parseInt(winput.getText());
			l=Integer.parseInt(linput.getText());
			try {
				setUp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else 
		{
			for (int r=0; r<buttons.length; r++)
				for (int c=0; c<buttons[0].length; c++)
				{
					if (e.getSource()==buttons[r][c])
					{
						if (game.isMine(r,c))
						{
							f.setContentPane(kaboom);
							f.pack();
							f.setVisible(true);
							f.setSize(500,500);
						}
						else
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
										if (game.getMines(row, col)!=0)
										{
											buttons[row][col].setText(Integer.toString(game.getMines(row, col)));
										}
									}
								}
						}
					}
				}
		}
		if (checkWin())
		{
			f.setContentPane(win);
			f.pack();
			f.setVisible(true);
			f.setSize(500,500);
		}
		
	}
	public static void main(String[] args)
	{
		GUI fun = new GUI();
	}
	public boolean checkWin()
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
	public void mouseEntered(MouseEvent arg0) 
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
				int i=gen.nextInt(labels.length);
				current.setText(labels[i]);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
