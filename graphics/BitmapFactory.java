package graphics;

import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class BitmapFactory{
	public static Bitmap decodeByteArray(byte bytes[],int offset,int length)throws IOException{
		return decodeStream(new ByteArrayInputStream(bytes,offset,length));
	}
	static Bitmap transform(BufferedImage image){
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
	public static Bitmap decodeStream(InputStream is)throws IOException{
		BufferedImage image = javax.imageio.ImageIO.read(is);
		return transform(image);
	}
	public static Bitmap decodeMatrix(int rgbValue[][]){
		int height = rgbValue.length;
		int width = rgbValue[0].length;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int y=0; y< height; y++){
			for(int x=0; x< width; x++){
				bufferedImage.setRGB(x,y,rgbValue[y][x]);  
			}
		}
		return transform(bufferedImage);
	}
}
