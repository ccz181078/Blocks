package util;

import java.io.*;
import java.util.HashMap;

/*android*/
public class AssetLoader{
	private static final long serialVersionUID=1844677L;
	private static HashMap<String,String>strs=null;
	
	//传入类和键名，加载doc.txt中对应的字符串值
	public static String loadString(Class c,String key){
		if(strs==null){
			strs=new HashMap<>();
			try{
				InputStreamReader isr=new InputStreamReader(new FileInputStream(debug.Log.MAIN_DIR+"doc.txt"),"UTF-8");
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
		if(s==null)return c.getCanonicalName()+"."+key;
		//else s=c.getSimpleName()+"."+key+"="+s;
		return s;
	}
	
}

