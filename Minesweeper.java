import java.util.Random;
public class Minesweeper 
{
	private int[][] board;
	private Random gen; 
	private boolean[][] open;
	private int mines;
	
	public Minesweeper(int length, int width)
	{
		board = new int[length][width];
		gen=new Random();
		open=new boolean[board.length][board[0].length];
		mines=(length*width)/8;
		for (int i=0; i<mines; i++)//inserts mines in random locations in board
		{
			board[gen.nextInt(board.length)][gen.nextInt(board[0].length)]=1;//random row and random column
		}
	}
	
	public boolean[][] getOpen()
	{
		return open;
	}
	public int getMines()
	{
		int total=0;
		for (int r=0; r<board.length; r++)
			for (int c=0; c<board[0].length; c++)
			{
				total+=board[r][c];
			}
		return total;
	}
	public static void main(String[] args)
	{
	 Minesweeper b = new Minesweeper(9,9);
	 System.out.println(b.getMines(0, 0));
	 b.print();
	 //print(b.clear(0, 0));
	}
	public boolean isMine(int r, int c)
	{
		return (board[r][c]==1);
	}
	public int getMines(int r, int c)
	{
		int mc=0;
		for (int row=r-1; row<=r+1; row++)
			for (int col=c-1; col<=c+1; col++)
			{
				try
				{
					if (board[row][col]==1)
						mc++;
				}
				catch(ArrayIndexOutOfBoundsException n)
				{
					
				}
			}
		return mc;	
	}
	public void print()
	{
		for (int r=0; r<board.length; r++){
			for (int c=0; c<board[0].length; c++)
				System.out.print(board[r][c]+" ");
			System.out.println();
		}
	}
	public void clear(int r, int c)
	{
		if (isMine(r,c))
			System.out.println("KABOOM");
		else
		{
			if (getMines(r,c)==0)
			{
				open[r][c]=true;
				for (int row=r-1; row<=r+1; row++)
					for (int col=c-1; col<=c+1; col++)
					{
							try
							{
								if (!open[row][col])
								{
									//System.out.println("row: "+row+" col: "+col);
									clear(row, col);
								}
							}
							catch(ArrayIndexOutOfBoundsException n)
							{
							
							}
					}
			}
			else
			{
				open[r][c]=true;
			}
		}
	}
	public static void print(boolean[][] a)
	{
		for (int r=0; r<a.length; r++){
			for (int c=0; c<a[0].length; c++)
				System.out.print(a[r][c]+" ");
			System.out.println();
		}
	}
	
}
