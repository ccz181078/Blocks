package com.ccz.blocks;

import game.world.World;
import game.GlobalSetting;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.util.*;
import game.socket.Client;

public class Main{
	static final UpdateThread ut=new UpdateThread();
	public static int MS_PER_FRAME=33;
	static volatile boolean exited=false;
	public static void help(){
		System.out.println(util.AssetLoader.loadString(Main.class,"help"));
	}
	private static void startGui(){
		if(MainActivity._this==null){
			ut.run(new Runnable(){public void run(){
				World.cur.resetRootPlayer();
			}});
			new MainActivity();
		}
	}
	public static void main(String args[])throws Exception{
		if(args.length==4){
			MainActivity.main(args);
			return;
		}
		game.item.Craft.init();
		util.SerializeUtil.loadTexture();
		ut.start();
		Scanner sc=new Scanner(System.in);
		String s;
		help();
		debug.Log.init();
		for(;;){
			try{
				s=sc.next();
			}catch(NoSuchElementException nsee){
				break;
			}
			if(s.equals("no_more_input")){
				for(;;)Thread.sleep(1000);
			}else if(s.equals("start"))ut.run(new Runnable(){public void run(){ut.startGame();}});
			else if(s.equals("stop"))ut.run(new Runnable(){public void run(){ut.stopGame();}});
			else if(s.equals("replay")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.replay(name);}});
			}/*else if(s.equals("record")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.startRecord(name);}});
			}else if(s.equals("recordall")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.startRecordAll(name);}});
			}*/else if(s.equals("w")||s.equals("save"))ut.run(new Runnable(){public void run(){ut.saveGame();}});
			else if(s.equals("ren")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.saveGame(name);}});
			}else if(s.equals("bkup")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){
					String s=World.cur.save_path;
					ut.saveGame(name);
					World.cur.save_path=s;
				}});
			}else if(s.equals(".")||s.equals("show"))ut.run(new Runnable(){public void run(){ut.showGameState();}});
			else if(s.equals("ls"))ut.run(new Runnable(){public void run(){ut.ls();}});
			else if(s.equals("'")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){
					if(ut.restoreWorld(name))ut.startGame();
				}});
			}else if(s.equals("''")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){
					if(ut.restoreWorld(name))ut.startGame();
				}});
				startGui();
			}else if(s.equals("cli")||s.equals("client")){
				final String ip=sc.next();
				final String port=sc.next();
				final String un=sc.next();
				final String pw=sc.next();
				if(MainActivity._this==null)new MainActivity();
				ut.run(new Runnable(){public void run(){
					ut.startGame(ip,port,un,pw);
				}});
			}else if(s.equals("cli:main")){
				final String ip="47.108.129.66";
				final String port="18447";
				final String un=sc.next();
				final String pw=sc.next();
				if(MainActivity._this==null)new MainActivity();
				ut.run(new Runnable(){public void run(){
					ut.startGame(ip,port,un,pw);
				}});
			}else if(s.equals("cli:local")){
				final String ip="127.0.0.1";
				final String port="18447";
				final String un=sc.next();
				final String pw="qwq";
				if(MainActivity._this==null)new MainActivity();
				ut.run(new Runnable(){public void run(){
					ut.startGame(ip,port,un,pw);
				}});
			}else if(s.equals("ro")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){
					if(ut.restoreWorld(name)){
						ut.saveGame("__tmp__");
						ut.startGame();
					}
				}});
				startGui();
			}else if(s.equals("tp")||s.equals("teleport")){
				final String name=sc.next(),x=sc.next(),y=sc.next();
				ut.runInWorld(new Runnable(){public void run(){ut.tp(name,x,y);}});
			}else if(s.equals("undo")){
				final long t=sc.nextLong();
				ut.run(new Runnable(){public void run(){
					World.cur.setTime(World.cur.time-t);
				}});
			}else if(s.equals("time")){
				final long t=sc.nextLong();
				ut.run(new Runnable(){public void run(){
					World.cur.setTime(t);
				}});
			}else if(s.equals("run")){
				final String name=sc.next();
				final String ss[]=name.split(",");
				ut.run(new Runnable(){public void run(){
					ut.runInWorld(debug.script.Script.run(ss));
				}});
			}else if(s.equals("cm")||s.equals("changemode")){
				final String name=sc.next();
				ut.runInWorld(new Runnable(){public void run(){ut.changeGameMode(name);}});
			}else if(s.equals("cr")){
				ut.runInWorld(new Runnable(){public void run(){ut.changeGameMode("[root]");}});
			}else if(s.equals("cpw")||s.equals("changepassword")){
				final String un=sc.next(),pw=sc.next();
				ut.runInWorld(new Runnable(){public void run(){ut.cpw(un,pw);}});
			}else if(s.equals("init")){
				final String mode=sc.next();
				final String terrain=sc.next();
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.initWorld(mode,terrain,name);}});
			}else if(s.equals("r")||s.equals("restore")){
				final String name=sc.next();
				ut.run(new Runnable(){public void run(){ut.restoreWorld(name);}});
			}else if(s.equals("?")||s.equals("help"))help();
			else if(s.equals("x")||s.equals("exit"))break;
			else if(s.equals("xx")){
				System.exit(0);
			}else if(s.equals("setip")){
				GlobalSetting.getInstance().default_server_ip=sc.next();
				GlobalSetting.getInstance().save();
			}else if(s.equals("getip"))util.AddressGetter.showIpAddress();
			else if(s.equals("g")||s.equals("gui")){
				startGui();
			}else if(s.equals("say")){
				final String txt=sc.next();
				try{
					MainActivity._this.action.sendText(txt);
				}catch(Exception e){}
			}else{
				final String name=s;
				final String ss[]=name.split(",");
				ut.run(new Runnable(){public void run(){
					ut.runInWorld(debug.script.Script.run(ss));
				}});
				//System.out.println("无效指令: "+s);
			}
		}
		ut.run(new Runnable(){public void run(){ut.exitGame();}});
		while(!Main.exited){
			try{Thread.sleep(10);}catch(Exception e){}
		}
		debug.Log.close();
		System.out.println("Main.main() exited");
		System.exit(0);
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
	}
	public static void log(String e){
		debug.Log.i(e);
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
	public void ls(){
		try{
			File[] fs = new File(debug.Log.MAIN_DIR).listFiles();
			for(File f:fs)if(f.isDirectory()){
				System.out.println(f);
			}
		}catch(Exception e){
			log(e);
		}
	}
	public void tp(String name,String _x,String _y){
		try{
			int x=Integer.valueOf(_x);
			int y=Integer.valueOf(_y);
			game.entity.Player pl=World.cur.getPlayerByName(name);
			if(pl!=null){
				new game.entity.SetRelPos(pl,null,x,y);
			}
		}catch(Exception e){
			log("error");
		}
	}
	boolean local_world_running=false;
	public void runInWorld(Runnable task){
		if(local_world_running)World.cur.runOnUpdateThread(task);
		else run(task);
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
	public boolean restoreWorld(String name){
		if(started){
			stopGame();
			if(started)return false;
		}
		try{
			String path=debug.Log.MAIN_DIR+name+debug.Log.FILE_PATH_SEPARATOR;
			World.restore(path);
			cur=name;
			log("读取存档:"+name);
			return true;
		}catch(FileNotFoundException e){
			log("未找到存档:"+name);
		}catch(Exception e){
			log("读取存档失败");
			log(e);
		}
		return false;
	}
	public void initWorld(String mode,String terrain,String name){
		if(started){
			stopGame();
			if(started)return;
		}
		try{
			
			World.InitConfig config=new World.InitConfig();

			     if(mode.equals("c"))config.mode=World.Mode.CREATIVE;
			else if(mode.equals("s"))config.mode=World.Mode.SURVIVE;
			else if(mode.equals("z"))config.mode=World.Mode.ZOMBIE;
			else if(mode.equals("i"))config.mode=World.Mode.INFZOMBIE;
			else if(mode.equals("stat"))config.mode=World.Mode.STAT;
			else if(mode.equals("test"))config.mode=World.Mode.TEST;
			else if(mode.equals("p"))config.mode=World.Mode.PVP;
			else if(mode.equals("e"))config.mode=World.Mode.ECPVP;
			else if(mode.equals("level"))config.mode=World.Mode.LEVEL;
			else throw new Exception();

			     if(terrain.equals("n"))config.terrain=World.Terrain.NORMAL;
			else if(terrain.equals("i"))config.terrain=World.Terrain.ISLAND;
			else if(terrain.equals("f"))config.terrain=World.Terrain.FLAT;
			else if(terrain.equals("e"))config.terrain=World.Terrain.EMPTY;
			else if(terrain.equals("b"))config.terrain=World.Terrain.BUILDINGS;
			else throw new Exception();

			String path=debug.Log.MAIN_DIR+name+debug.Log.FILE_PATH_SEPARATOR;
			
			World.init(path,config);
			cur=name;
			log("创建存档:"+name);
		}catch(Exception e){
			log("创建存档失败");
			log(e);
		}
	}
	boolean client_on=false;
	String getDatetimeString(){
		return new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
	}
	public void startGame(String ip,String port,String un,String pw){
		if(started){
			stopGame();
			if(started)return;
		}
		try{
			game.world.World.cur=null;
			Client.cur=new Client(ip,Integer.valueOf(port),un,pw);
			Client.cur.connect();
			started=true;
			client_on=true;
			startRecord("record_"+un+"_"+ip+"_"+port+"_"+getDatetimeString());
			while(started&&client_on&&!exited&&MainActivity._this!=null){
				handleTasks();
				try{sleep(50);}catch(Exception e){return;}
			}
			started=false;
			client_on=false;
		}catch(Exception e){
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
			World.cur.restart();
			started=true;
			log("开始游戏");
			new game.socket.Server().start();
			startRecordAll("record_"+getDatetimeString());
			while(started&&!exited){
				if(World.cur.time%(30*60)==0){
					saveGame();
				}
				long t=System.currentTimeMillis();
				World.cur.update();
				local_world_running=true;
				handleTasks();
				local_world_running=false;
				try{sleep(Math.max(0,Main.MS_PER_FRAME-(System.currentTimeMillis()-t)));}catch(Exception e){return;}
			}
		}catch(Exception e){
			log(e);
			stopGame();
		}
		local_world_running=false;
	}
	boolean replaying=false;
	public void replay(String path){
		if(started){
			stopGame();
			if(started)return;
		}
		MainActivity.replaying=true;
		try{
			if(MainActivity._this==null){
				new MainActivity();
			}
			util.ScreenReader sr=new util.ScreenReader(path);
			started=true;
			replaying=true;
			log("开始播放: "+path);
			while(started&&!exited){
				handleTasks();
				try{sleep(Main.MS_PER_FRAME);}catch(Exception e){return;}
				byte ni[]=sr.read();
				if(ni==null)break;
				GameView.ni.offer(ni);
				MainActivity._this.runOnUiThread(new Runnable(){public void run(){
					try{
						MainActivity._this.game_view.invalidate();
					}catch(Exception e){debug.Log.i(e);}
				}});
			}
		}catch(Exception e){
			log(e);
		}
		MainActivity.replaying=false;
		replaying=false;
		started=false;
		log("播放结束");
	}
	public void startRecord(String path){
		try{
			MainActivity._this.game_view.ss=new util.ScreenSaver(path);
			log("开始录制: "+path);
		}catch(Exception e){
			log(e);
		}
	}
	public void startRecordAll(String path){
		if(!started){
			log("游戏未运行");
			return;
		}
		try{
			World.cur.startRecordAll(path);
		}catch(Exception e){
			log(e);
		}
	}
	public void changeGameMode(String name){
		if(!started){
			log("游戏未运行");
			return;
		}
		try{
			game.entity.Player pl=World.cur.getPlayerByName(name);
			pl.creative_mode=!pl.creative_mode;
		}catch(Exception e){}
	}
	public void cpw(String un,String pw){
		if(!started){
			log("游戏未运行");
			return;
		}
		World.cur.changePassword(un,pw);
	}
	public void saveGame(){
		try{
			World.save();
			log("已保存:"+World.cur.save_path);
		}catch(Exception e){
			log("保存失败");
			log(e);
		}
	}
	public void saveGame(String name){
		if(World.cur==null){
			log("游戏未运行");
			return;
		}
		World.cur.save_path=debug.Log.MAIN_DIR+name+debug.Log.FILE_PATH_SEPARATOR;
		saveGame();
	}
	public void stopGame(){
		if(!started){
			log("游戏未运行");
			return;
		}
		if(replaying){
			started=false;
			replaying=false;
			return;
		}
		if(client_on){
			started=false;
			client_on=false;
			if(MainActivity._this!=null)MainActivity._this.finish();
			return;
		}
		try{
			saveGame();
			World.cur.stop();
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
