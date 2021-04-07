package graphics;

import java.awt.image.VolatileImage;
import java.awt.image.BufferedImage;

public class Bitmap{
	int w,h;
	BufferedImage img;
	Bitmap(BufferedImage img){this.img=img;}
	int getWidth(){return img.getWidth();}
	int getHeight(){return img.getHeight();}
}
