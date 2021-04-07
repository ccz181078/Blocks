
package util;

import game.world.World;
import game.GlobalSetting;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.util.*;

public class ScreenReader{
	CompressedInputStream cis;
	FileInputStream fis;
	public ScreenReader(String fn){
		try{
			fis=new FileInputStream(debug.Log.MAIN_DIR+fn+".tmp");
			cis=new CompressedInputStream(fis);
		}catch(Exception e){e.printStackTrace();}
	}
	public byte[] read(){
		try{
			return cis.read();
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	public void close(){
		try{
			cis.close();
			fis.close();
		}catch(Exception e){e.printStackTrace();}
	}
}
