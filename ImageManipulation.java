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
        // End of Message notification
        bitsReceived = "000";
        writeToimage(position++);
        writeToimage(position++);
        writeToimage(position++);
        writeToimage(position++);


        bitsReceived = "";
        System.out.println("number of pixels manipulated " + position);	
	}

	public void decode(){
		bitsReceived = "";
		boolean eof = false;
		int position= 0;
		int index;
		int[] pixel;
		int[] rgb;
		while(position < (imgHeight*imgWidth) && eof == false){
			index = 0;
			pixel = getPixel(position++);
			rgb = get_RGB(pixel[0],pixel[1]);
			// System.out.println("bits length " +bitsReceived.length());
			while(bitsReceived.length() < 8 && index < 3){
				bitsReceived += Integer.toString(getBit(rgb[index++]));
			}
			//write to file if bitsRecieved is = 8
			if(bitsReceived.length() == 8){
				// System.out.println("bits write: " + bitsReceived);
				if(bitsReceived.equals("00000000")){
				//check if End of message 
					eof = true;
					// System.out.println("END");
				} else {
				//write to file
					// System.out.println("write");
					writeByteToFile();
					bitsReceived = "";
				}
			}
			//deal with left over bits 
			if(index < 3){
				// System.out.println("extra bits");
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

	private int[] getPixel(int position){
		int xy[] = new int[2];
		if(width == imgWidth){
			++height;
			width = 0;
		} else ++width;
		xy[0] = width;
		xy[1] = height;
		return xy;
	}

    private int[] get_RGB(int x, int y) {
	     int rgb[] = new int[3];
	     Color color = new Color(img.getRGB(x, y));
	     rgb[0] = color.getRed();
	     rgb[1] = color.getGreen();
	     rgb[2] = color.getBlue();
	     return rgb;
	}

	private void writeToimage(int position){
		// going through for height ...
		//					for width ...

		//get positon -> x,y
		System.out.println(bitsReceived);
		int[] pos = getPixel(position); 
		//actually writing to image
		if(pos[0] <= imgWidth && pos[1] <= imgHeight){
			int[] rgb = get_RGB(pos[0], pos[1]);
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