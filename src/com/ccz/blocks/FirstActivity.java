package com.ccz.blocks;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import util.BmpRes;

public class FirstActivity extends Activity{
	private static final long serialVersionUID=1844677L;
	WorldPicker wp;
	FileOutputStream fos;
	PrintWriter pr;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		debug.Log.init();
		debug.Log.i("FirstActivity.onCreate()");
		getWindow().setFlags(1024,1024);
		requestWindowFeature(1);
		wp=new WorldPicker(this);
		setContentView(wp);
		/*try{
			fos=new FileOutputStream("/sdcard/windows/BstSharedFolder/Blocks/assets/doc.txt");
			pr=new PrintWriter(new OutputStreamWriter(fos));
			File f=new File("/sdcard/windows/BstSharedFolder/Blocks/src/");
			F(f);
			pr.flush();
			pr.close();
			fos.close();
		}catch(Exception e){e.printStackTrace();}*/
	}
	/*void F(File f){
		if(f.isDirectory()){
			for(File g:f.listFiles())F(g);
		}else{
			String s=f.getAbsolutePath();
			if(s.endsWith(".java")){
				String str=(s.split("src/")[1].split(".java")[0]);
				pr.write(str.replace('/','.')+"\nname=\ndoc=\n");
			}
		}
	}
	void more(){
		Toast.makeText(this,"no more",1000);
	}*/
	void startgame(){
		Intent i=new Intent();
		i.setClassName("com.ccz.blocks","com.ccz.blocks.MainActivity");
		startActivity(i);
	}
	@Override
	protected void onDestroy(){
		if(MainActivity._this!=null){
			if(game.world.World._!=null)MainActivity._this.game_view.stop();
		}
		debug.Log.i("FirstActivity.onDestroy()");
		debug.Log.close();
		super.onDestroy();
	}
}

