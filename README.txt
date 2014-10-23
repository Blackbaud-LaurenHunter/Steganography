UTEID: lmh2742;krm965;

FIRSTNAME: Lauren; Kyle;

LASTNAME: Hunter; Martinez;

CSACCOUNT: hunter7;kmart;

EMAIL: hunter7@cs.utexas.edu;kylemartinez10@yahoo.com;

[Program 3]
[Description]
There are 2 files: Steganography.java, ImageManipulation.java

Steganography.java: This file takes in the arguments and decides to do encoding or decoding depending on the first argument. We use BufferedImage and ImageIO to read in the image.
If the first argument is -E, then we read in from the file given and store all the bytes into a ByteArrayInputStream, then pass that to encode, where the bytes will be converted into a message within the image given.
If the first argument is -D, then we open the file given and call decode, where the message is then found from the pixel rgb values and writen to the file. And finally, once the ending character is found, we close the file. 
Then We print the image stats(image height, image width, number of pixels and name of image) after both calls to encode and decode. 

ImageManipulation.java: This class deals with encoding messages and decoding messages from a particular image. The class contains a BufferedImage, a string containig bits for encoding/decoding the message, the image height and width. This class gets called either to encode a message or decode a message. 
In the encode method, it takes in a ByteArrayInputStream and it goes through it byte by byte and adds each bit to the string bitsRecieved until the lenght of bitsRecieved has exactly 3 bits, then wirteToImage is called. In the writeToImage method, getPixel is called and returns an int[] with x and y values. How getPixel works: we go through the image by height then width, so [0][1], [0][1], [0][2],... we have these static counters height and width that get incremented everytime the method is called. once we get the position we get the RGB values by get_RGB. How get_RGB works: takes in the x,y(width, height) positions, then uses .getRGB(x,y) and sets the int rgb[] values and returns it. So now we have the right pixel and its RGB values, so now we loop through the rbg array and the bitsRecieved string and set the colors accordingly. Then once we have set all the values to their new respective colors we set that pixel by .setRGB(width, height, new color). At the end we add "000000000000" to the image to indicate the end of message.
In the decode method, we go through each pixel until our eom(end of message) is found. How we go through each pixel and write to file: so we get a pixel(using the same getPixel() method), and get its RGB values(using the same get_RGB() method) then we use a while loop to loop through the rgb values and decode its values(using getBit, which just return a 0 if the color is even or a 1 if the color is odd) and add it to our bitsRecieved string until either bitsReceived has a length of 8(signifing a full byte) or index(the index of the rgb values) is > 3. If bitsRecieved lenght = 8, then we check if the string is equal to the end of message, else we write the byte to the file using writeByteToFile. Then we have to deal with the possible left over bits in the RGB values, if index < 3 indicates that, and we just add the bits to bitsRecieved and then carry on.

HOW to run the program:
To compile: javac *.java
To execute: java Steganography -flag image message

[Finish]
We finished all of the assignment except for the extra credit. 

[Answer of Questions]
[Question 1]
Comparing your original and modified images carefully, can you detect *any* difference visually (that is, in the appearance of the image)?

[Answer 1]
No, I tried it on different images and I couldnt detect any difference in any of them.

[Question 2]
Can you think of other ways you might hide the message in image files (or in other types of files)?

[Answer 2]


[Question 3]
Can you invent ways to increase the bandwidth of the channel?

[Answer 3]
Larger images can contain larger messages. 

[Question 4]
Suppose you were tasked to build an "image firewall" that would block images containing hidden messages. Could you do it? How might you approach this problem?

[Answer 4]
Rather than blocking images, you could put each image through a scrambler of some sort. So you could take each pixel and randomly increment or decrement each color. so if a message was encoded in the image, it would now be scrambled.

[Question 5]
Does this fit our definition of a covert channel? Explain your answer.

[Answer 5]
Yes, it is sending information through a channel not meant for communitication. Two subjects could have access to a certain image and send information this way. 