package util;

import java.io.*;
import android.graphics.*;
import android.content.*;
import android.content.res.*;
import android.util.*;
import java.util.HashMap;


public class AssetLoader{
	private static final long serialVersionUID=1844677L;
	private static AssetManager asset_manager;
	private static HashMap<String,String>strs=null;
	public static void setAssetManager(AssetManager am){
		asset_manager=am;
	}
	
	//传入类和键名，加载doc.txt中对应的字符串值
	public static String loadString(Class c,String key){
		if(strs==null){
			strs=new HashMap<>();
			try{
				InputStreamReader isr=new InputStreamReader(asset_manager.open("doc.txt"),"UTF-8");
				BufferedReader br=new BufferedReader(isr);
				String cur="";
				for(String s;(s=br.readLine())!=null;){
					int p=s.indexOf('=');
					if(p<0)cur=s+' ';
					else{
						strs.put(cur+s.substring(0,p),s.substring(p+1,s.length()));
					}
				}
				br.close();
				isr.close();
			}catch(Exception e){e.printStackTrace();}
		}
		String s=strs.get(c.getName()+' '+key);
		if(s==null)return c.getCanonicalName()+"  "+c.getSimpleName()+"."+key;
		//else s=c.getSimpleName()+"."+key+"="+s;
		return s;
	}
	
	
	//加载一张png图片，优先在/sdcard/Blocks/assets中找，其次在apk的assets中找，找不到则用默认的UI/cancel.png
	public static Bitmap loadBmp(String path){
		Bitmap bmp=null;
		try{
			return BitmapFactory.decodeStream(new FileInputStream("/sdcard/Blocks/assets/"+path+".png"));
		}catch(Exception e){}
		try{
			bmp=BitmapFactory.decodeStream(asset_manager.open(path+".png"));
		}catch(IOException err){
			Log.e("AssetLoader","Fail to load: "+path+".png");
			try{
				bmp=BitmapFactory.decodeStream(asset_manager.open("UI/cancel.png"));
			}catch(Exception e){}
		}
		return bmp;
	}
}

