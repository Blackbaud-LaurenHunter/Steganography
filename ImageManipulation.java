import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.awt.Color;

public class ImageManipulation {
	private BufferedImage img;
	private String bitsReceived = "";
	private int imgHeight;
	private int imgWidth;
	static int height= 0;
	static int width = -1;

	public ImageManipulation(BufferedImage image, int imageWidth, int imageHeight){
		img = image;
		imgWidth = imageWidth-1;
		imgHeight = imageHeight-1;
	}


	public void encode(ByteArrayInputStream inputStream){
		//If the bit you want to store is 0, make the corresponding byte even; 
		//if the bit is 1, make the byte odd;
		String currentByte;
		int position=0;
        while (inputStream.available() > 0) {
            currentByte = String.format("%8s", Integer.toBinaryString(inputStream.read())).replace(' ', '0');
            for (int i = 0; i < currentByte.length(); i++) {
                String bitToSend = currentByte.substring(i, i+1);
                bitsReceived += bitToSend;
            	if (bitsReceived.length() == 3){
                	writeToimage(position);
                	position++;
                	bitsReceived = "";
            	}
            }

        }
        System.out.println("number of pixels manipulated " + position);
		
	}

	public void decode(){

	}

    private int[] get_RGB(int x, int y) {
	     int rgb[] = new int[3];
	     Color color = new Color(img.getRGB(x, y));
	     rgb[0] = color.getRed();
	     rgb[1] = color.getGreen();
	     rgb[2] = color.getBlue();
	     // System.out.println("rgb: " + rgb[0] + " " + rgb[1] + " " + rgb[2]);
	     return rgb;
	}

	private void writeToimage(int position){
		// going through for height ...
		//					for width ...

		//get positon -> may break this up into another method
		if(position > -1){
			if(width == imgWidth){
				++height;
				width = 0;
			} else ++width;
		}
		//actually writing to image
		// System.out.println("Hegith " +height + " width " + width);
		if(width <= imgWidth && height <= imgHeight){
			int[] rgb = get_RGB(width, height);
			int color;
			String bit;
			for(int i = 0; i < rgb.length; i++){
				color = rgb[i];
				bit = bitsReceived.substring(i, i+1); 
				if(bit.equals("0")){
					if(!(color % 2 == 0)){
						rgb[i] -= 1;
					}
				} else if(bit.equals("1")){
					if((color % 2 == 0)){
						rgb[i] += 1;
					}
				} else System.out.println("OH snap, something went wrong"); 

				// DEBUG
				// rgb[0] = 255;
				// rgb[1] = 255;
				// rgb[2] = 255;
				// write to pixel
				Color curr = new Color(rgb[0], rgb[1], rgb[2]);
				color=curr.getRGB();
				img.setRGB(width, height,color);
			}
		}
	}

}