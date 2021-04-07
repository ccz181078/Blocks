package com.ccz.blocks;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

import graphics.MyCanvas;
import graphics.Canvas1;

import game.world.World;
import game.world.NearbyInfo;
import java.io.*;
import static util.MathUtil.*;
import game.GlobalSetting;
import util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameView extends JComponent {
	public static ConcurrentLinkedQueue<byte[]> ni=new ConcurrentLinkedQueue<>();
	ScreenReader sr=null;
	ScreenSaver ss=null;
	MainActivity ctx=null;
	
	public GameView(MainActivity ctx) {
		ni.clear();
		this.ctx=ctx;
		setDoubleBuffered(true);
	}
	
	public void invalidate(){
		repaint();
	}
	
	byte last_ni[]=null;
	
	int cnt_m=0;
	
	@Override
	public void paint(Graphics cv) {
		//	System.err.println("paint!");
		try{
		super.paint(cv);
		ctx.action.width=getWidth();
		ctx.action.height=getHeight();
		

		int cnt=ni.size();
		/*if(cnt<=cnt_m/2)++cnt_m;
		if(cnt>cnt_m){
			if(cnt>1)--cnt;
		}
		System.err.println(cnt+"/"+cnt_m);*/
		
		if(!ni.isEmpty()){
			int mi=1;
			if(cnt>1)cnt=1;//--cnt;
			while(ni.size()>cnt){
				last_ni=ni.poll();
				draw(cv,last_ni,getWidth(),getHeight(),true);
				if(ss!=null)ss.write(last_ni);
			}
			last_ni=ni.poll();
			draw(cv,last_ni,getWidth(),getHeight(),false);
			if(ss!=null)ss.write(last_ni);
			//System.err.println("draw frame!");
		}else if(last_ni!=null){
			draw(cv,last_ni,getWidth(),getHeight(),false);
			//System.err.println("skip frame!");
		}

		}catch(Exception e){e.printStackTrace();debug.Log.i(e);}
	}
	static void draw(java.awt.Graphics g,byte[] bytes,int W,int H,boolean skip){
		graphics.Canvas1 cv=new graphics.Canvas1((java.awt.Graphics2D)g,W,H);
		graphics.MyCanvas mc=new graphics.MyCanvas(cv,bytes,BmpRes.int2bmp);
		cv.skip=skip;
		try{
			cv.save();
			cv.translate(W/2f,H/2f);
			float C=NearbyInfo.BW*Math.min(1,(H*2f)/W);
			cv.scale(H/C,-H/C);
			mc.draw();
			cv.restore();

			cv.save();
			cv.scale(H/8f,H/8f);
			mc.draw();
			cv.translate(W*8f/H,0);
			mc.draw();
			cv.restore();
		}catch(Exception e){debug.Log.i(e);}
	}
}


