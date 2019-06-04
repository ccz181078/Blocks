package game;

import java.io.*;

public class GlobalSetting implements Serializable{
	private static final long serialVersionUID=1844677L;
	//游戏的全局设定，储存在/sdcard/Blocks/setting.dat
	
	public static boolean playing_screen_record=false;
	public boolean server_on=true,screen_record=false;
	public String default_server_ip="";
	private GameSetting gs=null;
	private static GlobalSetting instance=null;
	
	public static GlobalSetting getInstance(){
		if(instance==null)instance=read();
		return instance;
	}
	
	
	public static GameSetting getGameSetting(){
		getInstance();
		if(instance.gs==null)instance.gs=new GameSetting();
		return instance.gs;
	}
	
	
	public static GlobalSetting read(){
		GlobalSetting w=new GlobalSetting();
		try{
			FileInputStream fis=new FileInputStream(debug.Log.MAIN_DIR+"setting.dat");
			ObjectInputStream ois=new ObjectInputStream(fis);
			w=(GlobalSetting)ois.readObject();
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
			w.save();
		}
		return w;
	}
	
	
	public void save(){
		try{
			File f=new File(debug.Log.MAIN_DIR);
			if(!f.exists())f.mkdirs();
			FileOutputStream fos=new FileOutputStream(debug.Log.MAIN_DIR+"setting.dat");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			fos.close();
			instance=this;
		}catch(Exception e){e.printStackTrace();}
	}
}
