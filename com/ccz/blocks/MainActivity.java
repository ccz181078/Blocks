package com.ccz.blocks;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JFrame;

import game.ui.Action;
import game.world.World;
import java.util.Random;
import util.BmpRes;
import game.GlobalSetting;
import game.socket.Client;

import javax.swing.*;

public class MainActivity extends JFrame implements MouseMotionListener,MouseListener,KeyListener{
	public static void main(String args[]){
		debug.Log.init();
		try{
			new MainActivity(args);
		}catch(Exception e){debug.Log.i(e);}
	}
	
	public GameView game_view;
	public volatile Action action;
	public static volatile MainActivity _this=null;
	public static volatile long rnd_id;
	static Random rnd_gen=new Random();
	boolean is_local;
	
	public MainActivity(){
		java.awt.Dimension dim=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int mw=(int)dim.getWidth();
		int mh=(int)dim.getHeight();
		mw=Math.min(mw,mh*2);
		mh=Math.min(mh,mw/2);
		System.out.println(mw+"x"+mh);
		action=new Action(mw,mh);
		rnd_id=rnd_gen.nextLong();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
            public void windowClosing(WindowEvent e){
				finish();
			}
		});
		_this=this;
		game_view=new GameView(this);
		addKeyListener(this);
		game_view.addMouseListener(this);
		game_view.addMouseMotionListener(this);
		enableInputMethods(false); 
		add(game_view,BorderLayout.CENTER);
		//setUndecorated(true);
		//setResizable(false);
		//setUndecorated(true);
		setVisible(true);
		setSize(mw,mh);
		
		GameView.ni.clear();
		action.init();
		BmpRes.init();
		GlobalSetting.playing_screen_record=false;
		is_local=true;
	}

	public MainActivity(String args[])throws Exception{
		this();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		is_local=false;
//		game_view.initScreenRecord();
		debug.Log.i("start game");
		/*if(GlobalSetting.playing_screen_record){
			World.cur=null;
			game_view.start();
		}else if(World.cur!=null){
			World.cur.restart();
			game_view.start();
			new game.socket.Server().start();
		}else{
			game.socket.Client.cur.connect();
		}*/
		
		game.world.World.cur=null;
		Client.cur=new Client(args[0],Integer.valueOf(args[1]),args[2],args[3]);
		Client.cur.connect();
	}
	
	boolean send_text_flag=false;
	
	public static void sendText(){
		try{
			if(MainActivity._this.send_text_flag)return;
			MainActivity._this.send_text_flag=true;
			new Thread(()->{
				try{
					String msg = JOptionPane.showInputDialog(MainActivity._this.game_view, "输入消息", "发送消息",JOptionPane.PLAIN_MESSAGE);
					if(msg!=null){
						MainActivity._this.runOnUiThread(()->{
							MainActivity._this.action.sendText(msg);
							MainActivity._this.send_text_flag=false;
						});
					}
				}catch(Exception e){}
			}).start();
		}catch(Exception e){}
	}
	
	public static void showText(String text){
		System.out.println(text);
	}
	
	public void runOnUiThread(Runnable task){
		javax.swing.SwingUtilities.invokeLater(task);
	}
	
	public void finish(){
		if(game_view!=null){
			if(game_view.ss!=null){
				game_view.ss.close();
				System.out.println("录制结束");
			}
		}
		if(is_local){
			System.out.println("gui closed");
			_this=null;
			this.dispose();
		}else{
			showText("finished");
			System.exit(0);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		int W=action.width,H=action.height;
		switch(e.getKeyCode()){
			case KeyEvent.VK_ESCAPE:
			if(!is_local)System.exit(0);
			else{
				finish();
			}
			break;
			
			case KeyEvent.VK_A:
			action.l=true;
			break;
			
			case KeyEvent.VK_D:
			action.r=true;
			break;
			
			case KeyEvent.VK_W:
			action.u=true;
			break;
			
			case KeyEvent.VK_S:
			action.d=true;
			break;

			case KeyEvent.VK_SPACE:
				action.u = true;
				break;

			case KeyEvent.VK_Q:
			action.onKey('Q');
			break;
			
			case KeyEvent.VK_E:
			action.onKey('E');
			break;
			
			case KeyEvent.VK_Z:
			action.onKey('Z');
			break;
			
			case KeyEvent.VK_X:
			action.onKey('X');
			break;
			
			case KeyEvent.VK_C:
			action.onKey('C');
			break;
			
			case KeyEvent.VK_V:
			action.onKey('V');
			break;
			
			case KeyEvent.VK_1:
			action.item_sel_group=1;
			break;
			
			case KeyEvent.VK_2:
			action.item_sel_group=2;
			break;
			
			case KeyEvent.VK_3:
			action.item_sel_group=3;
			break;
			
			case KeyEvent.VK_4:
			action.item_sel_group=4;
			break;
			
			case KeyEvent.VK_R:
			action.item_sel_index=1;
			break;
			
			case KeyEvent.VK_T:
			action.item_sel_index=2;
			break;
			
			case KeyEvent.VK_F:
			action.item_sel_index=3;
			break;
			
			case KeyEvent.VK_G:
			action.item_sel_index=4;
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		int W=action.width,H=action.height;
		switch(e.getKeyCode()){
			case KeyEvent.VK_A:
			action.l=false;
			break;
			
			case KeyEvent.VK_D:
			action.r=false;
			break;
			
			case KeyEvent.VK_W:
			action.u=false;
			break;
			
			case KeyEvent.VK_S:
			action.d=false;
			break;

			case KeyEvent.VK_SPACE:
				action.u = false;
				break;

			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
			action.item_sel_group=0;
			break;
			
			case KeyEvent.VK_R:
			case KeyEvent.VK_T:
			case KeyEvent.VK_F:
			case KeyEvent.VK_G:
			action.item_sel_index=0;
			break;
			
			case KeyEvent.VK_Q:
			action.onTouch(W-H*5/16,H*9/16,1,1);
			break;
			
			case KeyEvent.VK_E:
			action.onTouch(W-H*5/16,H*11/16,1,1);
			break;
			
			case KeyEvent.VK_Z:
			action.onTouch(W-H*5/16,H*15/16,1,1);
			break;
			
			case KeyEvent.VK_X:
			action.onTouch(W-H*5/16,H*13/16,1,1);
			break;
			
			case KeyEvent.VK_C:
			action.onTouch(W-H*5/16,H*7/16,1,1);
			break;
			
			case KeyEvent.VK_V:
			action.onTouch(W-H*5/16,H*5/16,1,1);
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent e){}
 
	@Override
	public void mouseClicked(MouseEvent e) {
	}
 
	@Override
	public void mousePressed(MouseEvent e) {
		action.onTouch(e.getX(),e.getY(),0,(e.getModifiers()&InputEvent.BUTTON1_MASK));
	}
 
	@Override
	public void mouseReleased(MouseEvent e) {
		action.onTouch(e.getX(),e.getY(),1,(e.getModifiers()&InputEvent.BUTTON1_MASK));
	}
 
	@Override
	public void mouseEntered(MouseEvent e) {
	}
 
	@Override
	public void mouseExited(MouseEvent e) {
	}
 
	@Override
	public void mouseDragged(MouseEvent e) {
		action.onTouch(e.getX(),e.getY(),2,(e.getModifiers()&InputEvent.BUTTON1_MASK));
	}
 
	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
