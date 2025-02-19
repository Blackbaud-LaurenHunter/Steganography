import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;


public class Steganography {


	public static void main (String []args) {
		//Steganography -E image.png my-message - "Encode"
		//Steganography -D image-steg.png my-message-out - "Decode"
		ImageManipulation message;
		BufferedImage img = null;

		Path path = Paths.get(args[2]);
		String new_image = args[1].replaceAll("\\..*","");

        try {	
            img = ImageIO.read(new File(args[1]));
            message = new ImageManipulation(img, img.getWidth(), img.getHeight());
			if(args[0].equals("-E")){
                // ENCODING
				byte[] bytes = Files.readAllBytes(path);
            	ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            	message.encode(byteStream);
            	File outputfile = new File(new_image + "-steg.png");
        		ImageIO.write(img, "png", outputfile);

			} else if(args[0].equals("-D")){
                // DECODING
				message.openFile(args[2]);
				message.decode();
				message.closeFile();
			} else System.out.println("Wrong arguments");
		
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    	int height = img.getHeight();
    	int width = img.getWidth();
    	int amountPixel = height * width;
		// This prints the image height and width and amount and name. 
    	System.out.println("picture name: " +args[1]  + "  image height: " + height  + "  image width: " +  width + " number of pixels: " + amountPixel);
    }
}

