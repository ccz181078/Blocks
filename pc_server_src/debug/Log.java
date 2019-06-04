package debug;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public final class Log{
private static final long serialVersionUID=1844677L;
	private static FileOutputStream fos;
	private static PrintWriter pw;
	public static final String FILE_PATH_SEPARATOR="/",MAIN_DIR="."+FILE_PATH_SEPARATOR+"data"+FILE_PATH_SEPARATOR;
	private static boolean show_time=true;
	public static void showTime(boolean v){
		show_time=v;
	}
	public static void init(){
		try{
			File f=new File(MAIN_DIR);
			if(!f.exists()||!f.isDirectory())f.mkdirs();
			fos=new FileOutputStream(MAIN_DIR+"log.txt",true);
			pw=new PrintWriter(fos);
			pw.write("\n\n");
			pw.flush();
		}catch(Exception e){e.printStackTrace();}
	}
	public static void i(String str){
		try{
			if(show_time){
				Calendar c=Calendar.getInstance();
				pw.write(c.getTime().toLocaleString()+":");
			}
			pw.write(str+"\n");
			pw.flush();
			fos.flush();
		}catch(Exception e){e.printStackTrace();}
	}
	public static void i(Exception e){
		try{
			i(e.toString());
			e.printStackTrace(pw);
			pw.flush();
			fos.flush();
		}catch(Exception err){err.printStackTrace();}
	}
	public static void close(){
		try{
			pw.flush();
			pw.close();
			fos.flush();
			fos.close();
		}catch(Exception e){e.printStackTrace();}
	}
}
