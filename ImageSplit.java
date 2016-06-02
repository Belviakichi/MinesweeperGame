import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.util.Random;

public class ImageSplit {
	private int rows; //You should decide the values for rows and cols variables
    private int cols;
    private int chunks;
    private String[] files;
    private Random gen;
	
	
	public ImageSplit(int r, int c){
		rows = r;
		cols = c;
		chunks = rows*cols;
		files = new String[]{"src/kittySized.png", "src/kittyReach.png", "src/look.png", "src/yellowKitty.png","src/poo.png", "src/sadKitty.png","src/happyKitty.png",
				"src/cuteKitty.png","src/doubleKitty.png", "src/kiki.png","src/helloKitty.png"};
		gen=new Random();
	}
	
	public BufferedImage[][] chop() throws IOException{
		File file = new File(files[gen.nextInt(files.length)]); 
	    FileInputStream fis = new FileInputStream(file);
	    BufferedImage image = ImageIO.read(fis); //reading the image file

	    

	    int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
	    int chunkHeight = image.getHeight() / rows;
	    int count = 0;
	    BufferedImage imgs[][] = new BufferedImage[rows][cols]; //Image array to hold image chunks
	    for (int x = 0; x < rows; x++) {
	        for (int y = 0; y < cols; y++) {
	            //Initialize the image array with image chunks
	            imgs[x][y] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

	            // draws the image chunk
	            Graphics2D gr = imgs[x][y].createGraphics();
	            gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
	            gr.dispose();
	        }
	    }
	    return imgs;
	}
	
    /*System.out.println("Splitting done");

    //writing mini images into image files
    for (int i = 0; i < imgs.length; i++) {
        ImageIO.write(imgs[i], "jpg", new File("img" + i + ".jpg"));
    }
    System.out.println("Mini images created");*/
}
