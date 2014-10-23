import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.awt.Color;

public class ImageManipulation {
	private BufferedImage img;
	private String bitsReceived = "";
	private int imgHeight;
	private int imgWidth;
	static int height= 0;
	static int width = -1;

	private BufferedWriter fileWriter;

	public ImageManipulation(BufferedImage image, int imageWidth, int imageHeight){
		img = image;
		imgWidth = imageWidth-1;
		imgHeight = imageHeight-1;
	}


	public void encode(ByteArrayInputStream inputStream){
		//If the bit you want to store is 0, make the corresponding byte even; 
		//if the bit is 1, make the byte odd;
		String currentByte;
        while (inputStream.available() > 0) {
            currentByte = String.format("%8s", Integer.toBinaryString(inputStream.read())).replace(' ', '0');
            for (int i = 0; i < currentByte.length(); i++) {
                String bitToSend = currentByte.substring(i, i+1);
                bitsReceived += bitToSend;
            	if (bitsReceived.length() == 3){
                	writeToimage();
                	bitsReceived = "";
           		}
            }
        }
        // End of Message notification
        bitsReceived = "000";
        writeToimage();
        writeToimage();
        writeToimage();
        writeToimage();
        bitsReceived = "";	
	}

	public void decode(){
		boolean eom = false;
		int position = 0;
		int index;
		int[] rgb;
		// go through pixels until eom == true
		while(position < (imgHeight*imgWidth) && eom == false){
			index = 0;
			getPixel();
			position++;
			rgb = get_RGB(width,height);
			// add bits to bitsReceived until = 8 or until index of RGB values = 3
			while(bitsReceived.length() < 8 && index < 3){
				bitsReceived += Integer.toString(getBit(rgb[index++]));
			}
			//write to file if bitsRecieved == 8
			if(bitsReceived.length() == 8){
				//check if End of message 
				if(bitsReceived.equals("00000000")){
					eom = true;
				} else {
				//write to file
					writeByteToFile();
					bitsReceived = "";
				}
			}
			//deal with left over bits 
			if(index < 3){
				while(index < 3){
					bitsReceived += Integer.toString(getBit(rgb[index++]));
				}
			}
		}
	}
		
	private int getBit(int color){
		if(color % 2 == 0)
			return 0;
		else return 1;
	}

	private void getPixel(){
		if(width == imgWidth){
			++height;
			width = 0;
		} else ++width;
	}

    private int[] get_RGB(int x, int y) {
	     int rgb[] = new int[3];
	     Color color = new Color(img.getRGB(x, y));
	     rgb[0] = color.getRed();
	     rgb[1] = color.getGreen();
	     rgb[2] = color.getBlue();
	     return rgb;
	}

	private void writeToimage(){
		// going through for height ...
		//					for width ...
		// get positon -> x,y
		getPixel(); 
		if(width <= imgWidth && height <= imgHeight){
			int[] rgb = get_RGB(width, height);
			int color;
			String bit;
			// set new rgb values to appropriate value based on bit value
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
				// convert rgb values to a color and write to image
				Color curr = new Color(rgb[0], rgb[1], rgb[2]);
				color = curr.getRGB();
				img.setRGB(width, height,color);
			}
		} else System.out.println("error: message too long");
	}


	public void writeByteToFile(){
        try {
            short a = Short.parseShort(bitsReceived, 2);
            ByteBuffer bytes = ByteBuffer.allocate(2).putShort(a);

            byte[] array = bytes.array();
            fileWriter.write(new String(array, "UTF-8"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void openFile(String filename) throws IOException{
        FileWriter fstream = new FileWriter(filename);
        fileWriter = new BufferedWriter(fstream);
    }

    public void closeFile(){
        try {
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}