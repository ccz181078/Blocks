package com.ccz.blocks;

import game.world.World;
import game.GlobalSetting;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.util.*;

public class Main{
	static final UpdateThread ut=new UpdateThread();
	static volatile boolean exited=false;
	public static void help(){
		System.out.println("显示帮助: help");
		System.out.println("显示运行状态: show");
		System.out.println("开始游戏: start");
		System.out.println("保存游戏: save");
		System.out.println("保存并退出游戏: stop");
		System.out.println("完全退出: exit");
		System.out.println("读取存档: restore 存档名");
		System.out.println("创建存档: init 存档名");
		System.out.println("修改游戏模式: changemode 用户名");
		System.out.println("获取本机ip: getip");
		System.out.println("设置ip: setip ip地址");
	}
	public static void main(String args[])throws Exception{
		ut.start();
		Scanner sc=new Scanner(System.in);
		String s;
		help();
		debug.Log.init();
		for(;;){
			s=sc.next();
			if(s.equals("start"))ut.run(new Runnable(){public void run(){ut.startGame();}});
			else if(s.equals("stop"))ut.run(new Runnable(){public void run(){ut.stopGame();}});
			else if(s.equals("save"))ut.run(new Runnable(){public void run(){ut.saveGame();}});
			else if(s.equals("show"))ut.run(new Runnable(){public void run(){ut.showGameState();}});
			else if(s.equals("changemode")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.changeGameMode(name);}});
			}else if(s.equals("cpw")){
				final String un=sc.next(),pw=sc.next();
				ut.run(new Runnable(){public void run(){ut.cpw(un,pw);}});
			}else if(s.equals("init")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.initWorld(name);}});
			}else if(s.equals("restore")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.restoreWorld(name);}});
			}else if(s.equals("help"))help();
			else if(s.equals("exit"))break;
			else if(s.equals("setip")){
				GlobalSetting.getInstance().default_server_ip=sc.next();
				GlobalSetting.getInstance().save();
			}else if(s.equals("getip"))util.AddressGetter.showIpAddress();
			else{
				System.out.println("无效指令: "+s);
			}
		}
		ut.run(new Runnable(){public void run(){ut.exitGame();}});
		while(!Main.exited){
			try{Thread.sleep(10);}catch(Exception e){}
		}
		debug.Log.close();
		System.out.println("Main.main() exited");
	}
}
class UpdateThread extends Thread{
	private Random rnd_gen=new Random();
	private ConcurrentLinkedQueue<Runnable> task_queue=new ConcurrentLinkedQueue<>();
	private boolean started=false;
	private volatile boolean exited=false;
	private String cur=null;
	public static void log(Exception e){
		debug.Log.i(e);
		e.printStackTrace(System.out);
	}
	public static void log(String e){
		debug.Log.i(e);
		System.out.println(e);
	}
	public void run(){
		while(!exited){
			handleTasks();
			try{sleep(3);}catch(Exception e){}
		}
		log("UpdateThread exited");
		Main.exited=true;
	}
	public void exitGame(){
		if(started)stopGame();
		log("exit");
		exited=true;
	}
	public void handleTasks(){
		while(!task_queue.isEmpty()&&!exited){
			try{
				task_queue.poll().run();
			}catch(Exception e){
				log(e);
			}
		}
	}
	public void run(Runnable ru){
		task_queue.offer(ru);
	}
	public void showGameState(){
		if(started){
			log("游戏正在运行");
		}else{
			log("游戏未运行");
		}
		if(cur!=null){
			log("当前存档: "+cur+"\n"+World.cur);
		}else log("当前未打开存档");
	}
	public void restoreWorld(String name){
		if(started){
			stopGame();
			if(started)return;
		}
		try{
			String path=debug.Log.MAIN_DIR+name+debug.Log.FILE_PATH_SEPARATOR;
			World.restore(path);
			cur=name;
			log("读取存档:"+name);
		}catch(Exception e){
			log("读取存档失败");
			log(e);
		}
	}
	public void initWorld(String name){
		if(started){
			stopGame();
			if(started)return;
		}
		try{
			String path=debug.Log.MAIN_DIR+name+debug.Log.FILE_PATH_SEPARATOR;
			World.init(path,true);
			cur=name;
			log("创建存档:"+name);
		}catch(Exception e){
			log("创建存档失败");
			log(e);
		}
	}
	public void startGame(){
		if(started){
			log("游戏正在运行");
			return;
		}
		try{
			World.cur.rnd_id=rnd_gen.nextLong();
			util.BmpRes.init();
			World.cur.restart();
			started=true;
			log("开始游戏");
			new game.socket.Server().start();
			while(started&&!exited){
				long t=System.currentTimeMillis();
				World.cur.update();
				handleTasks();
				try{sleep(Math.max(5,33-(System.currentTimeMillis()-t)));}catch(Exception e){return;}
			}
		}catch(Exception e){
			log(e);
			stopGame();
		}
	}
	public void changeGameMode(String name){
		if(!started){
			log("游戏未运行");
			return;
		}
		World.cur.changeGameMode(name);
	}
	public void cpw(String un,String pw){
		if(!started){
			log("游戏未运行");
			return;
		}
		World.cur.changePassword(un,pw);
	}
	public void saveGame(){
		if(!started){
			log("游戏未运行");
			return;
		}
		try{
			World.save();
			log("已保存");
		}catch(Exception e){
			log("保存失败");
			log(e);
		}
	}
	public void stopGame(){
		if(!started){
			log("游戏未运行");
			return;
		}
		try{
			saveGame();
			started=false;
			World.cur.rnd_id=rnd_gen.nextLong();
			game.socket.Server.close();
			log("结束游戏");
		}catch(Exception e){
			log("结束失败");
			log(e);
		}
	}
}