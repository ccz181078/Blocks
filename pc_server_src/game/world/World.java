package game.world;

import java.util.*;
import java.io.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import util.SerializeUtil;
import game.block.*;
import game.entity.*;
import game.item.*;
import util.Pointer;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class World implements Serializable{
	private static final long serialVersionUID=1844677L;
	//描述一个世界(一个存档)
	
	public static final int World_Height=128,log_Chunk_Width=5,Chunk_Width=1<<log_Chunk_Width;
	public static World cur;//当前世界

	public long time;//游戏中的时间
	
	private ArrayList<PlayerInfo> players=new ArrayList<>();
	private transient String save_path=debug.Log.MAIN_DIR+"test"+debug.Log.FILE_PATH_SEPARATOR;
	private Chunk[] chunks;
	private int min_chunk_id,max_chunk_id;
	private WorldGenerator gen_l,gen_r;
	private Block out_of_world;
	private ArrayList<BlockAt>chk_blocks=new ArrayList<>();
	private ArrayList<BlockAt>new_chk_blocks=new ArrayList<>();
	private ArrayList<Entity>new_ents=new ArrayList<>();
	private ArrayList<Agent>new_agents=new ArrayList<>();
	private transient ConcurrentLinkedQueue<Runnable> task_queue;
	public volatile transient long rnd_id;
	
	public boolean login(String un,String pw,Pointer<PlayerInfo> pis,game.GameSetting gs){
		debug.Log.i(un+":"+pw);
		for(PlayerInfo pi:players)if(pi.check(un,pw)){
			pi.player.game_setting=gs;
			(pis.obj=pi).add();
			showText(un+" 已登录");
			return true;
		}else if(pi.check0(un))return false;
		if(players.size()<100){
			PlayerInfo pi=regPlayer(un,pw,false);
			pi.player.game_setting=gs;
			(pis.obj=pi).add();
			showText(un+" 已登录 (new user)");
			return true;
		}
		return false;
	}
	
	public Player getRootPlayer(){
		return players.get(0).player;
	}
	
	public double RX(double x){
		return x-players.get(0).player.x;
	}
	public double RY(double y){
		return y-players.get(0).player.y;
	}
	
	//获取在线玩家列表
	public Player[] getPlayers(){
		ArrayList<Player>ps=new ArrayList<>();
		for(PlayerInfo pi:players)if(!pi.player.removed)ps.add(pi.player);
		return ps.toArray(new Player[ps.size()]);
	}
	
	//获取一个玩家能看到的附近区域
	public NearbyInfo getNearby(Player pl){
		NearbyInfo ni=getNearby(pl.x,pl.y,(NearbyInfo.BW/2/9*16),(NearbyInfo.BW/2),true,true,true);
		ni.setPlayer(pl);
		return ni;
	}
	
	
	public NearbyInfo getNearby(double mx,double my,double xd,double yd,boolean block,boolean ent,boolean agent){
		NearbyInfo ni=new NearbyInfo();
		ni.mx=mx;ni.my=my;ni.xd=xd;ni.yd=yd;
		if(block){
			int xl=(int)floor(mx-xd),xr=(int)floor(mx+xd);
			int yl=(int)floor(my-yd),yr=(int)floor(my+yd);
			ni.blocks=new Block[yr-yl+1][xr-xl+1];
			ni.xl=xl;ni.yl=yl;
			for(int y=yl;y<=yr;++y)
				for(int x=xl;x<=xr;++x)
					ni.blocks[y-yl][x-xl]=get(x,y);
		}
		int L=max(min_chunk_id,(int)floor(mx-xd-1)>>log_Chunk_Width);
		int R=min(max_chunk_id,(int)floor(mx+xd+1)>>log_Chunk_Width);
		if(ent){
			ni.ents=new ArrayList<>();
			for(int i=L;i<=R;++i)getChunk(i).getEnts(ni);
		}
		if(agent){
			ni.agents=new ArrayList<>();
			for(int i=L;i<=R;++i)getChunk(i).getAgents(ni);
		}
		return ni;
	}
	
	private void newWorld(boolean creative_mode){
		gen_l=new WorldGenerator();
		gen_r=new WorldGenerator();
		chunks=new Chunk[0];
		min_chunk_id=0;
		max_chunk_id=-1;
		addChunkL();
		addChunkR();
		time=1;
		out_of_world=new BedRockBlock();
		addPlayer(creative_mode);
		//for(int i=0;i<=9;++i)regPlayer("user"+i,"123456",false);
	}
	
	public void addPlayer(boolean creative_mode){
		PlayerInfo pi=new PlayerInfo();
		pi.player=new Player(1.5,getGroundY(1)+1);
		pi.player.name="root";
		pi.player.setRespawnPoint();
		pi.player.creative_mode=creative_mode;
		pi.player.add();
		players.add(pi);
	}
	public PlayerInfo regPlayer(String un,String pw,boolean creative_mode){
		RemotePlayerInfo pi=new RemotePlayerInfo(un,pw);
		pi.player=new Player(2.5,getGroundY(1)+4);
		pi.player.name=un;
		pi.player.setRespawnPoint();
		pi.player.creative_mode=creative_mode;
		pi.player.removed=true;
		players.add(pi);
		return pi;
	}
	
	//对所有玩家显示提示信息
	public static void showText(String info){
		System.out.println(info);
		for(PlayerInfo pi:cur.players)if(!pi.player.removed){
			pi.player.showText(info);
		}
	}
	
	//创建存档
	public static void init(String _save_path,boolean creative_mode)throws Exception{
		cur=new World();
		cur.save_path=_save_path;
		cur.newWorld(creative_mode);
		save();
	}
	
	//保存
	public static void save()throws Exception{
		long tt=System.currentTimeMillis();
		debug.Log.i("save: "+cur.save_path);
		File f=new File(cur.save_path);
		if(!f.exists())f.mkdirs();
		OutputStream os=new FileOutputStream(cur.save_path+"World.dat.tmp");
		BufferedOutputStream bos=new BufferedOutputStream(os,131072);
		ObjectOutputStream oos=new ObjectOutputStream(bos);
		oos.writeObject(cur);
		oos.close();
		bos.close();
		os.close();
		f=new File(cur.save_path+"World.dat");
		if(f.exists())f.delete();
		File fn=new File(cur.save_path+"World.dat.tmp");
		fn.renameTo(f);
		debug.Log.i(
			"successfully saved: "+
			(System.currentTimeMillis()-tt)+
			"ms "+
			(f.length()>>10)+
			"KB\n"+
			cur.toString()
		);
	}
	
	
	public void changeGameMode(String s){
		for(PlayerInfo pi:players){
			if(pi instanceof RemotePlayerInfo&&((RemotePlayerInfo)pi).username.equals(s)){
				pi.player.creative_mode=!pi.player.creative_mode;
				return;
			}
		}
	}
	
	public void changePassword(String un,String pw){
		for(PlayerInfo pi:players){
			if(pi instanceof RemotePlayerInfo&&((RemotePlayerInfo)pi).username.equals(un)){
				((RemotePlayerInfo)pi).password=pw;
				pi.remove();
				return;
			}
		}
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append(super.toString());
		sb.append("\n----------------------\n");
		sb.append("time: "+time+"\n");
		sb.append("x range: [");
		sb.append(getChunk(min_chunk_id).minX());
		sb.append(',');
		sb.append(getChunk(max_chunk_id).maxX());
		sb.append(")\n");
		int ent_c=0,agent_c=0;
		for(Chunk c:chunks)if(c!=null){
			ent_c+=c.ents.size();
			agent_c+=c.agents.size();
		}
		sb.append("entity: ");
		sb.append(ent_c);
		sb.append('\n');
		sb.append("agent: ");
		sb.append(agent_c);
		sb.append('\n');
		for(PlayerInfo pi:players){
			if(!pi.player.removed){
				sb.append(pi.toString()+"\n");
			}
		}
		sb.append("\n----------------------\n");
		return sb.toString();
	}
	
	//读取存档
	public static void restore(String _save_path)throws Exception{
		long tt=System.currentTimeMillis();
		debug.Log.i("restore: "+_save_path);
		File f=new File(_save_path+"World.dat");
		InputStream is=new FileInputStream(f);
		BufferedInputStream bis=new BufferedInputStream(is,131072);
		ObjectInputStream ois=new ObjectInputStream(bis);
		cur=(World)ois.readObject();
		cur.save_path=_save_path;
		is.close();
		debug.Log.i("successfully restored: "+(System.currentTimeMillis()-tt)+"ms "+(f.length()>>10)+"KB");
	}
	public void runOnUpdateThread(Runnable ru){
		task_queue.offer(ru);
	}
	
	//初始化，在读取/创建存档后，开始更新前调用
	public void restart(){
		task_queue=new ConcurrentLinkedQueue<>();
		for(PlayerInfo pi:players){
			pi.player.game_setting=game.GlobalSetting.getGameSetting();
			if(pi instanceof RemotePlayerInfo)pi.remove();
			else pi.add();
		}
	}
	/*void saveChunk(Chunk c)throws Exception{
		if(c==null)return;
		OutputStream os=new FileOutputStream(save_path+c.id);
		os.write(SerializeUtil.obj2bytes(c));
		os.close();
	}
	Chunk restoreChunk(int id){
		try{
			InputStream is=new FileInputStream(save_path+id);
			ObjectInputStream ois=new ObjectInputStream(is);
			Chunk c=(Chunk)ois.readObject();
			is.close();
			return c;
		}catch(Exception e){e.printStackTrace();}
		return null;
	}*/
	
	//获取给定列的从上到下第一个固体方块的上边界的y坐标
	public int getGroundY(int x){
		int y=World_Height-1;
		while(y>0&&!get(x,y).isSolid())--y;
		return y+1;
	}
	
	//生成植物
	void genPlant(int x){
		int y=getGroundY(x);
		if(get(x,y).rootBlock() instanceof WaterBlock){
			int py=y;
			while(get(x,py+1).rootBlock() instanceof WaterBlock)++py;
			if(py==y||rnd()<0.5){
				set(x,rndi(y,py),new AlgaeBlock());
			}else{
				set(x,y,new AquaticGrassBlock());
				if(rnd()<0.5)set(x,y+1,new AquaticGrassBlock());
			}
			return;
		}
		Class tp=get(x,y-1).rootBlock().getClass();
		if(tp==DirtBlock.class){
			int h=rndi(3,6);
			for(int i=0;i<h;++i)set(x,y+i,new TrunkBlock());
			for(int i=h-1;i<=h+1;++i){
				for(int j=-2;j<=2;++j)if(get(x+j,y+i).rootBlock().getClass()==AirBlock.class)set(x+j,y+i,new LeafBlock(0));
			}
		}else if(tp==SandBlock.class){
			int h=rndi(1,3);
			for(int i=0;i<h;++i)set(x,y+i,new CactusBlock());
		}else if(tp==DarkSandBlock.class){
			set(x,y,new DarkVineBlock(1));
			for(int i=-1;i<=1;++i){
				for(int j=-1;j<=1;++j)if(get(x+j,y+i).rootBlock().getClass()==AirBlock.class)set(x+j,y+i,new DarkVineBlock(0));
			}
		}
	}
	public void newEnt(Entity a){
		new_ents.add(a);
	}
	public void newAgent(Agent a){
		new_agents.add(a);
	}
	
	private Chunk getChunk(int x){
		int w=x-min_chunk_id;
		//if(chunks[w]==null)chunks[w]=restoreChunk(x);
		return chunks[w];
	}

	private void addChunkL(){
		Chunk[] cs=new Chunk[chunks.length+1];
		for(int i=0;i<chunks.length;++i)cs[i+1]=chunks[i];
		cs[0]=new Chunk();
		chunks=cs;
		--min_chunk_id;
		getChunk(min_chunk_id).init(min_chunk_id,gen_l,-1);
	}
	
	private void addChunkR(){
		Chunk[] cs=new Chunk[chunks.length+1];
		for(int i=0;i<chunks.length;++i)cs[i]=chunks[i];
		cs[chunks.length]=new Chunk();
		chunks=cs;
		++max_chunk_id;
		getChunk(max_chunk_id).init(max_chunk_id,gen_r,1);
	}
	
	public BlockAt[] get4(int x,int y){
		return new BlockAt[]{get1(x-1,y),get1(x+1,y),get1(x,y-1),get1(x,y+1)};
	}
	public Block[] get4Block(int x,int y){
		return new Block[]{get(x-1,y),get(x+1,y),get(x,y-1),get(x,y+1)};
	}
	
	public BlockAt get1(int x,int y){
		return new BlockAt(x,y,get(x,y));
	}

	//获取给定坐标上的方块
	public Block get(int x,int y){
		if(y<0||y>=World_Height)return out_of_world;
		int x1=x>>log_Chunk_Width,x2=x&((1<<log_Chunk_Width)-1);
		if(x1<min_chunk_id||x1>max_chunk_id)return out_of_world;
		return getChunk(x1).blocks[y<<log_Chunk_Width|x2];
	}
	
	public void set(int x,int y,Block block){
		if(y<0||y>=World_Height)return;
		int x1=x>>log_Chunk_Width,x2=x&(Chunk_Width-1);
		if(x1<min_chunk_id||x1>max_chunk_id)return;
		getChunk(x1).blocks[y<<log_Chunk_Width|x2]=block;
	}
	
	//在给定坐标放置方块，替换掉原有方块，并对原来的方块执行onDestroy
	public void place(int x,int y,Block block){//place block at x,y and destroy the original block
		if(y<0||y>=World_Height)return;
		int x1=x>>log_Chunk_Width,x2=x&(Chunk_Width-1);
		if(x1<min_chunk_id||x1>max_chunk_id)return;
		Block[] b=getChunk(x1).blocks;
		int w=y<<log_Chunk_Width|x2;
		b[w].onDestroy(x,y);
		b[w]=block;
		block.onPlace(x,y);
		check4(x,y);
	}
	
	public void setCircuit(int x,int y,CircuitBlock block){
		set(x,y,block);
		check4(x,y);
	}
	
	public boolean nextToBlock(int x,int y){
		if(get(x,y).isCoverable()){
			for(BlockAt ba:get4(x,y))if(!ba.block.isCoverable())return true;
		}
		return false;
	}
	
	//检查是否可以在x,y放置方块（与一个不可覆盖方块相邻），且不会卡住生物
	public boolean placeable(int x,int y){
		if(nextToBlock(x,y))return getNearby(x+0.5,y+0.5,0.5,0.5,false,false,true).agents.size()==0;
		return false;
	}
	
	//测试给定矩形内是否没有固体方块
	public boolean noBlock(double x,double y,double xd,double yd){
		for(Block[]bs:getNearby(x,y,xd,yd,true,false,false).blocks)
			for(Block b:bs)if(b.isSolid())return false;
		return true;
	}
	
	public boolean destroyable(int x,int y){
		for(Block b:get4Block(x,y))if(!b.isSolid())return true;
		return false;
	}
	
	//把给定坐标的方块改为空气，原方块不接收到事件
	public void setAir(int x,int y){
		set(x,y,new AirBlock());
		check4(x,y);
	}

	//给定浮点数坐标，得到这个坐标上的方块
	public Block get(double x,double y){
		return get(f2i(x),f2i(y));
	}
	
	//将当前方块和上下左右的方块加入下一帧的check队列
	public void check4(int x,int y){
		checkBlock(x,y);
		checkBlock(x-1,y);
		checkBlock(x+1,y);
		checkBlock(x,y-1);
		checkBlock(x,y+1);
	}
	
	//将当前方块加入下一帧的check队列，保证同个方块只能入队一次
	public void checkBlock(int x,int y){
		Block b=get(x,y);
		if(b instanceof StaticBlock)b=((StaticBlock)b).deStatic(x,y);
		if(b.last_chk_time<=time){
			b.last_chk_time=time+1;
			new_chk_blocks.add(new BlockAt(x,y,null));
		}
	}
	public static double ser_t=0;
	
	public int weather=0,sky_color=Weather.color_of_energy[0];
	public void updWeather(){
		if(time%2048==0&&rnd()<0.5){
			weather=Weather.randomWeather();
		}
		for(int t=0;t<10;++t){
			int col=Weather.color_of_energy[weather];
			if(col==sky_color)break;
			int[] col_dis=new int[3];
			double s=0;
			int col0=sky_color;
			for(int i=0;i<3;++i){
				int a=col_dis[i]=(col>>i*8&0xff)-(col0>>i*8&0xff);
				s+=a*a;
			}
			s=sqrt(s);
			sky_color=0xff000000;
			for(int i=0;i<3;++i){
				sky_color|=max(0,min((col0>>i*8&0xff)+rf2i(col_dis[i]/s),255))<<i*8;
			}
		}
		//showText(Weather.name_of_energy[weather]+": "+(sky_color>>16&0xff)+","+(sky_color>>8&0xff)+","+(sky_color&0xff));
	}
	static int stt=0,ttt=0,sbc=0;
	public int getChunkId(double x){
		int id=f2i(x)>>log_Chunk_Width;
		if(id<min_chunk_id)id=min_chunk_id;
		if(id>max_chunk_id)id=max_chunk_id;
		return id;
	}
	
	//进行一次世界更新
	//每帧进行一次
	public void update(){
		long tt=System.currentTimeMillis();
		
		++time;
		
		while(!task_queue.isEmpty())task_queue.poll().run();

		updWeather();
		
		out_of_world.last_chk_time=time+1;
		for(PlayerInfo pi:players)pi.readAction();
		//Log.i("update","time="+time+" new_ents:"+new_ents.size()+" new_agents:"+new_agents.size());
		
		
		for(Player p:getPlayers()){
			int id=getChunkId(p.x);
			int L=max(id-2,min_chunk_id),R=min(id+2,max_chunk_id);
			for(int i=L;i<=R;++i){
				Chunk c=getChunk(i);
				if(c!=null)c.gen_ent=true;
			}
		}
		
		for(Chunk c:chunks)if(c!=null)c.updateEnts0();
		for(Chunk c:chunks)if(c!=null)c.updateEnts();
		for(Chunk c:chunks)if(c!=null)c.removeDeadEnts();
		for(Chunk c:chunks)if(c!=null)c.updateAgents();
		for(Chunk c:chunks)if(c!=null)c.moveEnts();
		for(Chunk c:chunks)if(c!=null)c.randomUpdateBlocks();
		
		Collections.shuffle(chk_blocks);
		for(int i=0;i<chk_blocks.size();++i){
			BlockAt ba=chk_blocks.get(i);
			if(get(ba.x,ba.y).onCheck(ba.x,ba.y))check4(ba.x,ba.y);
		}
		chk_blocks.clear();
		ArrayList<BlockAt> tmp=chk_blocks;
		chk_blocks=new_chk_blocks;
		new_chk_blocks=tmp;
		
		for(Entity ent:new_ents){
			getChunk(getChunkId(ent.x)).ents.add(ent);
		}
		new_ents.clear();
		for(Agent agent:new_agents){
			getChunk(getChunkId(agent.x)).agents.add(agent);
		}
		new_agents.clear();
		for(Chunk c:chunks)if(c!=null)c.sortEnts();
		boolean l_end=false,r_end=false;
		
		StringBuffer sb=new StringBuffer();

		int cnt=0;
		for(PlayerInfo pi:players){
			if(!pi.player.removed){
				l_end|=pi.player.x<getChunk(min_chunk_id).minX()+100;
				r_end|=pi.player.x>getChunk(max_chunk_id).maxX()-100;
				pi.genNI();
				++cnt;
				if(pi instanceof RemotePlayerInfo){
					if(pi.skip_t>200)pi.remove();
					RemotePlayerInfo rpi=(RemotePlayerInfo)pi;
					sb.append(rpi.username+":"+pi.player.name+":"+f2i(pi.player.x)+","+f2i(pi.player.y)+":"+pi.skip_t+"\n");
				}else{
					sb.append("[root]:"+pi.player.name+":"+f2i(pi.player.x)+","+f2i(pi.player.y)+"\n");
				}
			}
		}
		if(cnt>1)getRootPlayer().showTextIfFree(sb.toString());
		
		if(max_chunk_id-min_chunk_id<4096/Chunk_Width){
			if(l_end)addChunkL();
			if(r_end)addChunkR();
		}

		
		/*if(com.ccz.blocks.MainActivity._this!=null)
		try{
			PlayerInfo pi=players.get(0);
			pi.setAction(com.ccz.blocks.MainActivity._this.action.getBytes());
			com.ccz.blocks.GameView.ni=pi.getNI();
		}catch(Exception e){e.printStackTrace();}*/

		tt=System.currentTimeMillis()-tt;
		stt+=tt;ttt+=1;
		sbc+=chk_blocks.size();
		if(ttt>=30*60){
			debug.Log.i("update time:"+stt*1./ttt+"upd block:"+sbc*1./ttt);
			stt=ttt=0;
			sbc=0;
		}
	}
};


