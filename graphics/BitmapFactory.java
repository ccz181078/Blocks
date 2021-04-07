package graphics;

import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class BitmapFactory{
	public static Bitmap decodeByteArray(byte bytes[],int offset,int length)throws IOException{
		return decodeStream(new ByteArrayInputStream(bytes,offset,length));
	}
	public static Bitmap decodeStream(InputStream is)throws IOException{
		BufferedImage image = javax.imageio.ImageIO.read(is);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		GraphicsDevice gd = ge.getDefaultScreenDevice ();
		GraphicsConfiguration gc = gd.getDefaultConfiguration ();
		BufferedImage convertedImage = gc.createCompatibleImage (image.getWidth (), 
												   image.getHeight (), 
												   Transparency.TRANSLUCENT );
		Graphics2D g2d = convertedImage.createGraphics ();
		g2d.drawImage ( image, 0, 0, image.getWidth (), image.getHeight (), null );
		g2d.dispose();
		return new Bitmap(convertedImage);
	}
}
