package com.ccz.blocks;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.util.Log;
import game.ui.Action;
import game.world.World;
import java.util.Random;
import util.BmpRes;
import game.GlobalSetting;

public class MainActivity extends Activity{
	private static final long serialVersionUID=1844677L;
    /** Called when the activity is first created. */
	public GameView game_view;
	DirectionControlView dcv;
	public RelativeLayout rl;
	public volatile Action action;
	public static volatile MainActivity _this=null;
	public static volatile long rnd_id;
	static Random rnd_gen=new Random();
	
	public void onCreate(Bundle savedInstanceState){
		debug.Log.i("MainActivity.onCreate()");
		super.onCreate(savedInstanceState);
		getWindow().setFlags(1024,1024);
		requestWindowFeature(1);
		
		int W=getWindowManager().getDefaultDisplay().getWidth();
		int H=getWindowManager().getDefaultDisplay().getHeight();
		action=new Action(W,H);
		game_view=new GameView(this);
		dcv=new DirectionControlView(this,action,H/8f);
		rl=new RelativeLayout(this);
		rl.addView(game_view,W,H);
		rl.addView(dcv,H/2,H);
		setContentView(rl);
	}
	
	public static void showText(final String s){
		Log.i("showText",s);
		final MainActivity _=_this;
		if(_!=null)try{
			_.runOnUiThread(new Runnable(){public void run(){
				try{
					android.widget.Toast.makeText(_,s,5000).show();
				}catch(Exception e){e.printStackTrace();}
			}});
		}catch(Exception e){e.printStackTrace();}
	}

	@Override
	protected void onStart(){
		debug.Log.i("MainActivity.onStart()");
		super.onStart();
	}

	@Override
	protected void onResume(){
		debug.Log.i("MainActivity.onResume()");
		_this=this;
		rnd_id=rnd_gen.nextLong();
		BmpRes.init();
		GameView.ni=null;
		action.init();
		game_view.initScreenRecord();
		debug.Log.i("start game");
		if(GlobalSetting.playing_screen_record){
			World._=null;
			game_view.start();
		}else if(World._!=null){
			World._.restart();
			game_view.start();
			new game.socket.Server().start();
		}else{
			game.socket.Client.cur.connect();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		game_view.exitScreenRecord();
		debug.Log.i("stop game");
		rnd_id=rnd_gen.nextLong();
		if(GlobalSetting.playing_screen_record||World._!=null)game_view.stop();
		_this=null;
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		if(_this!=null){
			if(World._!=null)game_view.stop();			
		}
		super.onSaveInstanceState(outState);
	}
	
	

	@Override
	protected void onStop(){
		debug.Log.i("MainActivity.onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy(){
		debug.Log.i("MainActivity.onDestroy()");
		super.onDestroy();
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
			case KeyEvent.KEYCODE_A:action.l=true;action.r=false;break;
			case KeyEvent.KEYCODE_D:action.r=true;action.l=false;break;
			case KeyEvent.KEYCODE_W:action.u=true;action.d=false;break;
			case KeyEvent.KEYCODE_S:action.d=true;action.u=false;break;
		}
		return super.onKeyDown(keyCode,event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		switch(keyCode){
			case KeyEvent.KEYCODE_A:action.l=false;break;
			case KeyEvent.KEYCODE_D:action.r=false;break;
			case KeyEvent.KEYCODE_W:action.u=false;break;
			case KeyEvent.KEYCODE_S:action.d=false;break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
