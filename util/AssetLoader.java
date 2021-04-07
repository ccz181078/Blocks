package util;

import java.io.*;
import java.util.HashMap;
import graphics.*;

/*android*/
public class AssetLoader{
	private static final long serialVersionUID=1844677L;
	private static HashMap<String,String>strs=null;
	
	//传入类和键名，加载doc.txt中对应的字符串值
	public static String loadString(Class c,String key){
		if(strs==null){
			strs=new HashMap<>();
			try{
				InputStreamReader isr=new InputStreamReader(AssetLoader.class.getResource("/assets/doc.txt").openStream(),"UTF-8");
				BufferedReader br=new BufferedReader(isr);
				String cur="";
				for(String s;(s=br.readLine())!=null;){
					int p=s.indexOf('=');
					if(p<0)cur=s+' ';
					else{
						String text=s.substring(p+1,s.length());
						if(text.equals("'''")){
							StringBuffer sb=new StringBuffer();
							while((text=br.readLine())!=null){
								if(text.equals("'''"))break;
								sb.append(text);
								sb.append('\n');
							}
							text=sb.toString();
						}
						strs.put(cur+s.substring(0,p),text);
					}
				}
				br.close();
				isr.close();
			}catch(Exception e){e.printStackTrace();}
		}
		String s=strs.get(c.getName()+' '+key);
		if(s==null)return c.getCanonicalName()+"."+key;
		//else s=c.getSimpleName()+"."+key+"="+s;
		return s;
	}
	
	/*private static HashMap<String,Bitmap> bmps=new HashMap<>();
	static{
		loadAllTexture(new File(debug.Log.ASSETS_DIR),"");
	}
	private static void loadAllTexture(File f0,String s){
		if(f0.isDirectory()){
			for(File f:f0.listFiles()){
				loadAllTexture(f,s+f.getName()+"/");
			}
		}else{
			String fn=f0.getName();
			if(fn.endsWith(".png")){
				System.err.println(s+fn);
				try{
					Bitmap bmp=BitmapFactory.decodeStream(new FileInputStream(f0));
					bmps.put(s+fn,bmp);
				}catch(Exception e){}
			}
		}
	}*/
	public static Bitmap loadBmp(String path){
		path+=".png";
		//if(bmps.containsKey(path))return bmps.get(path);
		//else System.err.println(path);
		Bitmap bmp=null;
		try{
			bmp=SerializeUtil.texture.get(path);
			//return BitmapFactory.decodeStream(AssetLoader.class.getResource("/assets/"+path).openStream());
		}catch(Exception e){}
		return bmp;
	}
	
	public static InputStream load(String path)throws IOException{
		try{
			return AssetLoader.class.getResource("/assets/"+path).openStream();
		}catch(Exception e){}
		return new FileInputStream(debug.Log.ASSETS_DIR+path);
	}

}

