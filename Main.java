import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        /**BinaryIn a = new BinaryIn("ur.jpg");
         //boolean currentBoolean;
         byte currentByte;
         BinaryOut d = new BinaryOut("midFile");
         int count = 0;
         while(!a.isEmpty()){
         currentByte = a.readByte();

         }**/

        /**while (!a.isEmpty()) {
         currentBoolean = a.readBoolean();
         //System.out.println(currentBoolean);
         d.write(currentBoolean);
         //String intString = String.format("%08d", Integer.parseInt(Integer.toBinaryString(currentByte)));
         //System.out.println(currentInt);
         //System.out.println("shit" );
         //d.write(intString);
         }**/
        FileInputStream fs = new FileInputStream("ur.jpg");
        byte[] bs= fs.readAllBytes();
        BinaryOut bo = new BinaryOut("outputFile.jpg");
        for(byte b : bs){
            bo.write(b);
        }


    }
}

