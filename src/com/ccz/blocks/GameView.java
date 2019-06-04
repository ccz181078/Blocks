package com.ccz.blocks;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import game.world.World;
import game.world.NearbyInfo;
import android.os.*;
import java.io.*;
import static util.MathUtil.*;
import game.GlobalSetting;
import util.*;

public class GameView extends android.view.SurfaceView{
	private static final long serialVersionUID=1844677L;
	public static volatile byte[] ni;
	ScreenReader sr=null;
	ScreenSaver ss=null;
	//public static volatile byte[] action;
	GameView(Context con){
		super(con);
		setBackgroundColor(0);
		setKeepScreenOn(true);
		util.AssetLoader.setAssetManager(con.getAssets());
	}
	boolean stopped=false;
	//double upd_t=0;
	void update(){
		if(stopped){
			stopped=false;
			return;
		}
		long t=System.currentTimeMillis();
		if(World._!=null)
		try{
			World._.update();
			//upd_t+=0.05*(System.currentTimeMillis()-t-upd_t);
		}catch(Exception e){
			e.printStackTrace();
			World.showText(e.toString());
		}
		invalidate();
		new Handler().postDelayed(new Runnable(){
			public void run(){update();}
		},Math.max(5,33-(System.currentTimeMillis()-t)));
	}
	void start(){
		update();
	}
	void initScreenRecord(){
		sr=null;ss=null;
		if(GlobalSetting.playing_screen_record)sr=new ScreenReader();
		else if(GlobalSetting.getInstance().screen_record)ss=new ScreenSaver();		
	}
	void exitScreenRecord(){
		if(sr!=null)sr.close();
		if(ss!=null)ss.close();
	}
	void stop(){
		stopped=true;
		if(World._!=null)
		try{
			World.save();
		}catch(Exception e){
			debug.Log.i(e);
		}
	}
	long t0=0;
	double ft=33;
	int stt=0,ttt=0;
	
	protected void onDraw(Canvas cv){
		long tt=System.currentTimeMillis();
		int W=getWidth(),H=getHeight();
		try{
			if(GlobalSetting.playing_screen_record&&sr!=null){
				ni=sr.read();
				if(ni==null&&MainActivity._this!=null)MainActivity._this.finish();
			}
			if(ni!=null){
				NearbyInfo.draw(cv,ni,W,H);
				if(ss!=null)ss.write(ni);
			
				long t1=System.currentTimeMillis();
				ft+=0.01*(Math.max(1,Math.min(1000,t1-t0))-ft);
				if(rnd(30*60)<1)debug.Log.i("fps:"+1000/ft);
				t0=t1;
				
			}
		}catch(Exception e){e.printStackTrace();}
		
		stt+=System.currentTimeMillis()-tt;
		ttt+=1;
		if(ttt>=30*60){
			debug.Log.i("draw time:"+stt*1./ttt);
			stt=ttt=0;
		}
	}
	public boolean onTouchEvent(MotionEvent event){
		if(GlobalSetting.playing_screen_record)return false;
		if(MainActivity._this!=null){
			MainActivity._this.action.onTouch(event.getX(),event.getY(),event.getAction(),System.currentTimeMillis());
			return true;
		}
		return false;
	}
}

class ScreenSaver{
	CompressedOutputStream cos;
	FileOutputStream fos;
	ScreenSaver(){
		try{
			fos=new FileOutputStream("/sdcard/Blocks/screen_record.tmp");
			cos=new CompressedOutputStream(fos);
		}catch(Exception e){e.printStackTrace();}
	}
	void write(byte[]b){
		try{
			cos.write(b);
		}catch(Exception e){e.printStackTrace();}
	}
	void close(){
		try{
			cos.close();
			fos.close();
		}catch(Exception e){e.printStackTrace();}
	}
}

class ScreenReader{
	CompressedInputStream cis;
	FileInputStream fis;
	ScreenReader(){
		try{
			fis=new FileInputStream("/sdcard/Blocks/screen_record.tmp");
			cis=new CompressedInputStream(fis);
		}catch(Exception e){e.printStackTrace();}
	}
	byte[] read(){
		try{
			return cis.read();
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	void close(){
		try{
			cis.close();
			fis.close();
		}catch(Exception e){e.printStackTrace();}
	}
}

