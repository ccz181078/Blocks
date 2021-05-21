package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import game.entity.*;
import game.world.StatMode.StatResult;
import game.item.*;
import util.SerializeUtil;

public class ECPvPMode extends GameMode{
	private static final long serialVersionUID=1844677L;
	public double cur_l,cur_r,nxt_l,nxt_r;
	int getWorldWidth(){return HALF_WORLD_WIDTH*2;}
	public double next_box_pos=0;
	public int next_box_state=0;
	@Override
	public boolean forceOnline(){return true;}
	int resourceRate(){
		return 0;
	}
	void genEnt(Chunk chunk){}
	static boolean le(int l,int x,int r){
		return l<=x&&x<=r;
	}
	void drop(Item items[],int x,int y){
		Item it=(Item)SerializeUtil.deepCopy(items[rndi(0,items.length-1)]);
		int cnt=1;
		if(it.isCreative())return;
		if(it instanceof EnergyCell){
			EnergyCell ec=(EnergyCell)it;
			ec.setEnergy(f2i(rnd_exp(200)));
		}
		if(it instanceof game.item.EnergyStoneArmor)return;
		if(it instanceof game.item.BlackHole)return;
		if(it instanceof game.block.ExplosiveBlock && rnd()>0.25)return;
		if(it instanceof Tool){
			int k=1000;
			Tool tool=(Tool)it;
			if(tool instanceof EnergyArmor)return;
			//if(tool instanceof AgentRef)return;
			if(tool instanceof EnergyLauncher)k=60;
			if(tool instanceof RPGLauncher)k=20;
			if(tool instanceof WarheadLauncher)k=300;
			if(tool instanceof EnergySwordType)k=30;
			if(tool instanceof DefendTool)k=1000;
			if(tool instanceof TeleportationStick)k=10;
			tool.damage=max(0,tool.maxDamage()-f2i(1+rnd_exp(k)));
			if(tool instanceof TeleportationSquare)tool.damage=0;
			if(tool instanceof Bucket)tool.damage=0;
		}
		if(it instanceof game.item.Bottle){
			if(it instanceof game.item.PureEnergyBottle||it instanceof game.item.BloodBottle)cnt+=f2i(rnd_exp(3));
			else cnt+=f2i(rnd_exp(12));
		}
		if(it.foodVal()>0)cnt+=f2i(rnd_exp(2));
		if(it instanceof game.item.Cube && rnd()>0.2)return;
		if(it instanceof game.item.Arrow)cnt+=f2i(rnd_exp(4));
		if(it instanceof game.item.Bullet){
			if(it instanceof game.item.DarkGuidedBullet ){
				return;
			}
			if(it instanceof game.item.SpecialBullet||it instanceof game.item.FireBullet||it instanceof game.item.FlakBullet||it instanceof game.item.PathBullet||it instanceof game.item.GuidedBullet){
				cnt+=f2i(rnd_exp(4));
			}
			else if(it instanceof game.item.APDSBullet){
				cnt+=f2i(rnd_exp(16));
			}
			else{
				cnt+=f2i(rnd_exp(32));
			}
			if(it instanceof game.item.SpecialBullet&&rnd()>0.2)return;
		}
		if(it instanceof game.item.StoneBall){
			if(it instanceof game.item.HeatBall&&rnd()>0.3)return;
			if(it instanceof game.item.DarkIronBall&&rnd()>0.1)return;
			cnt+=f2i(rnd_exp(1));
		}
		if(it instanceof game.item.RPGItem){
			if(rnd()>0.2)return;
			if(!(it instanceof game.item.RPG_Guided))cnt=1;
		}
		if(it instanceof game.item.Warhead){
			if(rnd()>0.2)return;
			cnt+=f2i(rnd_exp(1));
		}
		cnt=max(1,min(cnt,it.maxAmount()));
		it.drop(x+0.5,y+0.5,cnt);
	}
	void placeBox(int x,int y){
		if(World.cur.get(x,y).isCoverable()){
			if(rnd()<0.3){
				double r=rnd();
				Block b=null;
				if(r<0.3)b=new IronWorkBenchBlock();
				else if(r<0.6)b=new FurnaceBlock();
				else if(r<0.65)b=new BlockMakerBlock();
				else if(r<0.72)b=new CompresserBlock();
				else if(r<0.8)b=new PulverizerBlock();
				else if(r<0.9)b=new EnergyFurnaceBlock();
				else b=new RepairerBlock();
				World.cur.place(x,y,b);
				return;
			}
			//if(World.cur.get(x,y-1).rootBlock() instanceof WoodenType);
			World.cur.place(x,y,new IronBoxBlock());
			while(rnd()<0.05)drop(Craft._normal_armor,x,y);
			while(rnd()<0.05)drop(Craft._bow,x,y);
			while(rnd()<0.06)drop(Craft._bullet,x,y);
			while(rnd()<0.03)drop(Craft._energy_launcher,x,y);
			while(rnd()<0.6)drop(Craft._normal_tool,x,y);
			while(rnd()<0.6)drop(Craft._energy_tool,x,y);
			while(rnd()<0.5)drop(Craft._normal_item,x,y);
			while(rnd()<0.5)drop(Craft._throwable_item,x,y);
			if(rnd()<0.3)EnergyCell.full().drop(x+0.5,y+0.5);
			if(rnd()<0.008){
				RPGLauncher it=new RPGLauncher();
				it.damage=it.maxDamage()-20;
				it.drop(x+0.5,y+0.5);
			}
			if(rnd()<0.1)new Mine().drop(x+0.5,y+0.5);
			//if(rnd()<0.1)new WarheadLauncher().drop(x+0.5,y+0.5);
			
			if(rnd()<0.1)new EnergyStone().drop(x+0.5,y+0.5);
			if(rnd()<0.05)new game.item.FireBall().drop(x+0.5,y+0.5);
			if(rnd()<0.05)new game.item.DarkBall().drop(x+0.5,y+0.5);
			if(rnd()<0.08)new EnergyMotor().drop(x+0.5,y+0.5);
			if(rnd()<0.1)new game.item.Arrow().drop(x+0.5,y+0.5);
			
			//if(rnd()<0.3)new ItemSlot().drop(x+0.5,y+0.5,rndi(1,3));
			if(rnd()<0.003)new ExplosiveBlock().drop(x+0.5,y+0.5,rndi(1,3));
			if(rnd()<0.0003)new HDEnergyStoneBlock().drop(x+0.5,y+0.5);
		}
	}
	static void fillBlock(int x1,int x2,int y1,int y2,Block b){
		fillBlock(x1,x2,y1,y2,b,false);
	}
	static void fillBlock(int x1,int x2,int y1,int y2,Block b,boolean clone){
		for(int x=x1;x<=x2;++x){
			for(int y=y1;y<=y2;++y){
				if(clone)b=b.clone();
				World.cur.set(x,y,b);
			}
		}
	}
	
	void initNextRange(){
		double d=cur_r-cur_l;
		nxt_l=cur_l+d*rnd(0.3);
		nxt_r=nxt_l+d*0.7;
	}
	public static int HALF_WORLD_WIDTH=900,NUM_ZOMBIE=49,BUILDING_NUM=30,DROP_CNT=500;
	public static double V0=0.06;//0.067;
	void newWorld(World world){
		cur_l=-HALF_WORLD_WIDTH;
		cur_r=HALF_WORLD_WIDTH;
		initNextRange();
		for(int ii=0;ii<BUILDING_NUM;++ii){
			int width=rndi(6,10);
			if(rnd()<0.3)width=10+(int)rnd_exp(10);
			int x1=rndi(-HALF_WORLD_WIDTH,HALF_WORLD_WIDTH-16),d=min(3,rndi(1,4)),x2=x1+width+d*2;
			int y1=World.cur.getGroundY(x1-1);
			int y2=World.cur.getGroundY(x2+1);
			if(abs(y1-y2)>=4)continue;
			double r=rnd();
			Block b_window=null,b,door,ladder,scaffold,air=new AirBlock();
			door=new IronDoorBlock();
			ladder=new IronLadderBlock();
			scaffold=new IronScaffoldBlock();
			if(r<0.3){
				if(rnd()<0.8)b=new StoneBlock();
				else b=new ImprovedStoneBlock();
				b_window=new GlassBlock();
			}else if(r<0.55){
				if(abs(y1-y2)>=1)continue;
				if(rnd()<0.7)b=new WoodenBlock();
				else b=new CoalBlock();
				door=new WoodenDoorBlock();
				ladder=new LadderBlock();
				scaffold=new WoodenScaffoldBlock();
				b_window=new GlassBlock();
			}else if(r<0.64)b=new CrystalBlock();
			else if(r<0.7)b=new DiamondBlock();
			else if(r<0.85)b=new GlassBlock();
			else if(r<0.9){
				b=new IronNailBlock();
				b_window=new GlassBlock();
			}else if(r<0.96)b=new QuartzBlock();
			else b=new ImprovedQuartzBlock();
			b=new StaticBlock(b);
			if(b_window!=null)b_window=new StaticBlock(b_window);
			else b_window=b;
			ladder=new StaticBlock(ladder);
			scaffold=new StaticBlock(scaffold);
			
			int y=min(y1,y2);
			int h=rndi(10,40);
			if(y+h>120)continue;
			int ground=-1,last_ground=-2;
			int ladder_x=rndi(x1+d,x2-d);
			for(int i=-1;i<=h;++i){
				if(i==ground){
					last_ground=ground;
					fillBlock(x1,x2,y+i,y+i,b);//ground
					fillBlock(x1+d+width/2-1,x2-(d+width/2-1),y+i,y+i,b_window);//ground window
					ground=max(ground+rndi(3,6),max(y1,y2)-y+2);
					if(h-ground<3)ground=h;
					if(rnd()<0.4&&i!=h)World.cur.set(ladder_x,y+i,door.clone());//ground door
					else World.cur.set(ladder_x,y+i,ladder);
				}else{
					fillBlock(x1,x2,y+i,y+i,b);
					fillBlock(x1+d,x2-d,y+i,y+i,air);
					World.cur.set(ladder_x,y+i,ladder);
					if(d<=2&&rnd()<0.4&&i>5&&i==last_ground+2){
						fillBlock(x1,x1+d-1,y+i-1,y+i,door,true);
						fillBlock(x2-d+1,x2,y+i-1,y+i,door,true);
					}else if(i==last_ground+1&&i>2){
						double c=0.5;
						for(int t=0;t<3&&rnd()<c;++t)placeBox(x1+d+t,y+i);
						for(int t=0;t<3&&rnd()<c;++t)placeBox(x2-d-t,y+i);
					}
				}
			}
			boolean use_ladder=(rnd()<0.5);
			boolean glue_in_wall=(use_ladder&&d==3&&rnd()<0.4);
			boolean reaction_in_wall=(!glue_in_wall&&use_ladder&&d==3&&rnd()<0.4);
			if(r<0.3){
				if(glue_in_wall){
					++h;
					World.cur.set(x1,y+h,b);
					World.cur.set(x2,y+h,b);
					fillBlock(x1+1,x2-1,y-2,y-2,new GlueBlock(),true);//ground
					fillBlock(x1+1,x2-1,y+h,y+h,new GlueBlock(),true);//ground
					fillBlock(x1+d+width/2-1,x2-(d+width/2-1),y+h,y+h,b_window);//ground window
					World.cur.set(ladder_x,y+h,ladder);
				}
				++h;
				fillBlock(x1,x2,y+h,y+h,new StaticBlock(new StonePowderBlock()));//ground
				fillBlock(x1+d+width/2-1,x2-(d+width/2-1),y+h,y+h,b_window);//ground window
				World.cur.set(ladder_x,y+h,ladder);
			}
			int door_d=min(d,2);//door
			fillBlock(x1,x1+d-1,y1,y1+1,air);
			fillBlock(x2-d+1,x2,y2,y2+1,air);
			fillBlock(x1,x1+door_d-1,y1,y1+1,door,true);
			fillBlock(x2-door_d+1,x2,y2,y2+1,door,true);
			
			//ladders under door
			fillBlock(x1+door_d,x1+door_d,y,y1-1,ladder);
			fillBlock(x2-door_d,x2-door_d,y,y2-1,ladder);
			
			//ladder out of building
			if(use_ladder){
				if(rnd()<0.3)fillBlock(x1-1,x1-1,y,y+h,ladder);
				if(rnd()<0.3)fillBlock(x2+1,x2+1,y,y+h,ladder);
				if(glue_in_wall){//glue in wall
					fillBlock(x1+1,x1+1,y1+2,y+h-1,new GlueBlock(),true);
					fillBlock(x2-1,x2-1,y2+2,y+h-1,new GlueBlock(),true);
				}
				if(reaction_in_wall){//reaction in wall
					fillBlock(x1+1,x1+1,y1+2,y+h-1,new ReactionBlock());
					fillBlock(x2-1,x2-1,y2+2,y+h-1,new ReactionBlock());
				}
			}else{
				if(rnd()<0.2)fillBlock(x1,x1,y1+2,y+h,scaffold);
				if(rnd()<0.2)fillBlock(x2,x2,y2+2,y+h,scaffold);
			}
		}
		Airship a=new IronAirship();
		a.ec.insert(InfiniteEnergyCell.full());
		a.af1.insert(new FastAirshipFlank());
		a.af2.insert(new FastAirshipFlank());
		airship=new ZombieRobot(0,0);
		airship.name="[qwq]";
		airship.goal=airship.new ECAirship();
		airship.armor.insert(a);
		airship.initPos(-HALF_WORLD_WIDTH,125,0.5,0,null).add();
		for(int i=0;i<NUM_ZOMBIE;++i){
			initHuman(Zombie.gen(rndi(-HALF_WORLD_WIDTH,HALF_WORLD_WIDTH),i+1));
		}
	}
	ArrayList<Human> pls=new ArrayList<>(),dead_pls=new ArrayList<>();
	public Zombie airship=null;
	public int restPlayerCount(){
		int cnt=0;
		for(Human hu:pls){
			if(!hu.isRemoved())++cnt;
		}
		return cnt;
	}
	long time=-1;
	int wait=30*60;
	boolean finished=false;
	int rest_drop=DROP_CNT;
	void update(Chunk chunk){
		if(World.cur.time>time){
			time=World.cur.time;
			if(!airship.dead){
				for(int i=0;i<dead_pls.size();++i){
					Player pl=(Player)dead_pls.get(i);
					if(--pl.respawn_time<=0){
						dead_pls.remove(i--);
						initPlayer(pl);
					}
				}
			}
			for(Human hu:pls){
				if((hu.xdir!=0||hu.ydir!=0||hu instanceof Player && ((Player)hu).creative_mode)&&hu.locked){
					World.showText(">>> 一个毒瘤跳伞了");
					EscapeDevice ed=new EscapeDevice();
					ed.damage=ed.maxDamage()-10;
					ed.state=0;
					hu.shoes.insert(ed);
					hu.locked=false;
					hu.initPos(airship.x,airship.y-2.1,airship.xv,airship.yv-0.05,null);
					if(hu instanceof Player && ((Player)hu).creative_mode){
						hu.items.lockAll();
						hu.items.unlock(31);
						for(Human w:pls){
							AgentRefReadonly it=new AgentRefReadonly();
							it.insert(InfiniteEnergyCell.full().setAmount(1));
							it.agent=w;
							it.drop(hu.x,hu.y,1);
						}
						hu.xv=hu.yv=0;
						for(int i=1;i<=4;++i){
							World.cur.place((int)(hu.x-1),(int)hu.y-i,new IronBoxBlock());
							World.cur.place((int)(hu.x),(int)hu.y-i,new IronBoxBlock());
						}
						for(int j=1;j>=-80;--j)World.cur.place((int)(hu.x)+j,(int)hu.y-5,new BedRockBlock());
					}
					break;
				}
			}
			if(time>=10&&restPlayerCount()<=1&&!finished){
				finished=true;
				cur_l=-HALF_WORLD_WIDTH;
				cur_r=HALF_WORLD_WIDTH;
				World.showText("游戏结束！");
			}
			if(!finished){
				if(rest_drop>0){
					int x=(int)(cur_l+(rnd(cur_r-cur_l)+rnd(cur_r-cur_l))*0.5);
					--rest_drop;
					if(rnd()<0.5)drop(Craft._normal_tool,x,127);
					else drop(Craft._throwable_item,x,127);
				}
				//if(rnd()<0.01)new ItemSlot().drop(rnd(cur_l,cur_r),127);
				if(rnd()<0.002)new PlantEssence().drop(rnd(cur_l,cur_r),127);
				if(wait>0)--wait;
				else{
					cur_l=min(nxt_l,cur_l+V0);
					cur_r=max(nxt_r,cur_r-V0);
					if(cur_l>nxt_l-0.1&&cur_r<nxt_r+0.1){
						initNextRange();
						wait=30*30;
					}
				}
			}
		}
		if(World.cur.time%10==0){
			for(Agent a:chunk.agents){
				if(!a.active())continue;
				if(a.left<cur_l||a.right>cur_r||a.top>World.World_Height||a.bottom<0){
					BloodBall.drop(a,0.5,SourceTool.OUT);
				}
			}
		}
	}
	
	@Override
	void initPlayer(Player player){
		super.initPlayer(player);
		
		//player.items.lockAll();
		//player.bag_items.lockAll();
		//player.items.unlock(10);
		
		player.initPos(0,0,0,0,null);
		initHuman(player);
	}
	void initHuman(Human hu){
		hu.items.insert(new EnergyStone().setAmount(1));
		hu.locked=true;
		hu.dead=false;
		hu.kill_cnt=0;
		pls.add(hu);
		//if(false){
			//hu.items.insert(new BloodSword().setAmount(1));
			//hu.items.insert(new BloodNailShield().setAmount(1));
			//hu.items.insert(new BloodArmor().setAmount(1));
			//hu.items.insert(new EnergyStoneArmor().setAmount(1));
			//hu.armor.insert(new EnergyStoneArmor().setAmount(1));
			//JetPack w=new JetPack();
			//w.ec.insert(BlueCrystalEnergyCell.full().setAmount(1));
			//hu.armor.insert(w.setAmount(1));
			//hu.items.insert(new PlantEssence().setAmount(10));
			//hu.items.insert(new EnergyStoneShield().setAmount(1));
			//hu.items.insert(new EnergyStoneShield().setAmount(1));
			//hu.items.insert(new Mine().setAmount(4));
			//hu.items.insert(new game.item.HandGrenade().setAmount(16));
			
			//hu.items.insert(new game.item.GuidedBullet().setAmount(99));
			//hu.items.insert(new SpecialBullet(4).setAmount(99));
			//hu.items.insert(new SpecialBullet(4).setAmount(99));
			//hu.items.insert(new game.item.FlakBullet().setAmount(99));
			//hu.items.insert(new game.item.FlakBullet().setAmount(99));
			//hu.items.insert(new game.item.FlakBullet().setAmount(99));
			//hu.items.insert(new game.item.FireBullet().setAmount(99));
			//hu.items.insert(new game.item.FireBullet().setAmount(99));
			//hu.items.insert(new game.item.FireBullet().setAmount(99));
			//hu.items.insert(new game.item.FireBullet().setAmount(99));
			//hu.items.insert(new game.item.GuidedBullet().setAmount(99));
			//hu.items.insert(new EnergyMachineGun().setAmount(1));
			//hu.items.insert(new EnergyMachineGun().setAmount(1));
			//hu.items.insert(new EnergyMachineGun().setAmount(1));
			//hu.items.insert(new EnergySubmachineGun().setAmount(1));
			//hu.items.insert(new EnergySubmachineGun().setAmount(1));
			//hu.items.insert(new EnergySubmachineGun().setAmount(1));
			//hu.items.insert(new game.item.Bullet().setAmount(99));
			//hu.items.insert(new game.item.Bullet().setAmount(99));
			//hu.items.insert(new game.item.Bullet().setAmount(99));
			//hu.items.insert(new HighEnergyShockWaveLauncher().setAmount(1));
			//hu.items.insert(new HighEnergyFireLauncher().setAmount(1));
			
			//hu.items.insert(new game.item.ExplosiveBall().setAmount(99));
			//hu.items.insert(new game.item.FireBottle().setAmount(99));
			//hu.items.insert(new game.item.Warhead_HEAT().setAmount(16));
			//hu.items.insert(new game.item.Warhead_HE().setAmount(16));
			//hu.items.insert(new game.item.Warhead_WideRangeSpark().setAmount(16));
			//hu.items.insert(new game.item.RPG_HE().setAmount(16));
			//hu.items.insert(new game.item.RPG_HEAT().setAmount(16));
			//hu.items.insert(new game.item.RPG_EnergyBall().setAmount(2));
			//hu.items.insert(new RPGLauncher().setAmount(1));
			//hu.items.insert(new game.item.RPG_EnergyBall().setAmount(2));
			//hu.items.insert(new game.item.HeatBall().setAmount(99));
			//hu.items.insert(new game.item.ExplosiveIronNailBall().setAmount(99));
			//hu.items.insert(BlueCrystalEnergyCell.full().setAmount(1));
			//hu.items.insert(BlueCrystalEnergyCell.full().setAmount(1));
			//hu.items.insert(BlueCrystalEnergyCell.full().setAmount(1));
		//}
		if(airship.dead)hu.hp=0;
	}
	public void onHumanDead(Human w){
		int px=f2i(w.x-0.5),py=max(0,min(127,f2i(w.y-1)));
		BoxShield box=new BoxShield();
		boolean cnt=false;
		for(SingleItem si:w.items.toArray())box.insert(si);
		for(SingleItem si:w.bag_items.toArray())box.insert(si);
		new ZombieCrystal().drop(w.x,w.y,2);
		w.dropItems();
		pls.remove(w);
		box.ent.initPos(w.x,w.y,w.xv,w.yv,SourceTool.make(w,"死后生成的")).add();
	}
	public void onZombieDead(Zombie w){
		onHumanDead(w);
	}
	public void onPlayerDead(Player player){
		onHumanDead(player);
		player.respawn_time=300;
		player.locked=true;
		dead_pls.add(player);
	}
	public void onPlayerRespawn(Player player){
		new SetRelPos(player,null,0,-5);
	}
}
