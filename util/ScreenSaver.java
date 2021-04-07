
package util;

import game.world.World;
import game.GlobalSetting;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.util.*;

public class ScreenSaver{
	CompressedOutputStream cos;
	FileOutputStream fos;
	public ScreenSaver(String fn){
		try{
			fos=new FileOutputStream(debug.Log.MAIN_DIR+fn+".tmp",true);
			cos=new CompressedOutputStream(fos);
		}catch(Exception e){e.printStackTrace();}
	}
	public void write(byte[]b){
		if(b==null)return;
		try{
			cos.write(b);
		}catch(Exception e){e.printStackTrace();}
	}
	public void close(){
		try{
			cos.close();
			fos.close();
		}catch(Exception e){e.printStackTrace();}
	}
}