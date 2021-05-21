package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.entity.Entity.*;
import game.item.*;
import game.ui.*;
import game.block.BlockAt;
import game.world.*;
import graphics.Canvas;
import util.BmpRes;
import game.block.*;

public class Zombie extends Human{
	private static final long serialVersionUID=1844677L;

	static BmpRes
	body=new BmpRes("Entity/Zombie/body"),
	hand=new BmpRes("Entity/Zombie/hand"),
	leg=new BmpRes("Entity/Zombie/leg");

	Enemy[] es;
	ZombieBase base;

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	Group group(){return Group.ZOMBIE;}

	public Goal goal=new KillPlayer();

	public Zombie(double _x,double _y){
		super(_x,_y);
		es=new Enemy[4];
	}
	
	public static Zombie gen(int x,int id){
		Zombie z;
		if(rnd()<1)z=new Zombie(0,0);
		else z=new ZombieRobot(0,0);
		z.goal=z.new KillHuman();
		z.name="第"+id+"个毒瘤";
		z.add();
		return z;
	}
	
	@Override
	public void clickAt(double x,double y){
		//Line.gen(x,y,0.1);
		//Line.gen(x,y,this.x,this.y);
		super.clickAt(x,y);
	}
	
	@Override
	protected int[] getItemListSize(){
		return new int[]{31,0};
	}

	@Override
	public void update(){
		super.update();
		if(base!=null)base.zombie_cnt+=1;
	}
	
	void chkShield(){
		SingleItem[] si=items.toArray();
		for(int x=0;x<si.length;++x){
			if(!si[x].isEmpty()){
				Item i=si[x].get();
				if(i instanceof Shield){
					items.select(si[x]);
					if(i instanceof EnergyStoneShield)break;
				}
			}
		}
	}
	
	@Override
	public void ai(){
		if(World.cur.getMode() instanceof LevelMode){
			if(World.cur.getRootPlayer().creative_mode)return;
		}
		Enemy.check(es);
		goal=goal.update();
		
		boolean ent_near=false;
		double xs=0,ys=0;
		boolean place_block=false,af_flag=false;
		if(armor.get() instanceof Vehicle)place_block=true;
		for(Agent e:World.cur.getNearby(x,y,6,6,false,false,true).agents){
			if(e instanceof BigShield){
				ys+=6;
				BigShield bs=(BigShield)e;
				if(!"[qwq]".equals(name))
				if(distLinf(bs)<4&&bs.shield instanceof BoxShield){
					BoxShield box=(BoxShield)bs.shield;
					
					for(SingleItem si:items.toArray()){
						if(si.isEmpty())continue;
						if(!useful(si.get()))box.insert(si);
					}
					
					for(SingleItem si:box.toArray()){
						if(useful(si.get())){
							items.insert(si);
						}
					}
				}
			}
			if(e instanceof Airship_Flank){
				if(abs(e.x-x)<1&&abs(y-2-e.y)<1)af_flag=true;
			}
		}
		for(Entity e:World.cur.getNearby(x,y,6,6,false,true,false).ents){
			if(e.harmless()){
				continue;
			}
			double xd=x-e.x,yd=y-e.y;
			double rxv=xv-e.xv,ryv=yv-e.yv;
			if(e instanceof FieldBuff||e instanceof ExplosiveBall||e instanceof EnergyExplodeBall||e instanceof ShockWave){
				ent_near=true;
				xs+=xd*2;
				ys+=yd*2;
				continue;
			}
			double s=xd*rxv+yd*ryv;
			if(s<0){
				if(e instanceof DarkCube && hitTest(e))continue;
				ent_near=true;
				double c=xd*ryv-yd*rxv;
				xs-=c*yd;
				ys+=c*xd;
				if(!place_block&&rnd()<0.2&&!(e instanceof FallingBlock||e instanceof DarkBall)&&distLinf(e)<4&&selectSolidBlockX()){
					place_block=true;
					for(int i=-2;i<=1;++i)clickAt(e.x+e.xv,e.y+e.yv+i);
				}
			}
		}
		double a=max(0,hp)-last_hp;
		if(a>2||ent_near){
			chkShield();
			if(abs(xs)*3>abs(ys)+1e-8)xdir=(xs>0?1:-1);
			if(abs(ys)*3>abs(xs)+1e-8)ydir=(ys>0?1:-1);
			for(SingleItem si:items.toArray()){
				Item w=si.get();
				if(w==null)continue;
				if(
					w instanceof TeleportationStick&&rnd()<0.1||
					w instanceof TeleportationSquare&&rnd()<0.1||
					w instanceof FieldGen||
					w instanceof game.item.ProtectionZone){
					items.select(si);
					w.autoUse(this,null);
				}
			}
		}
		
		if(armor.get() instanceof JetPack){
			if(abs(xv)>0.3)xdir=(xv>0?-1:1);
			if(abs(yv)>0.3)ydir=(yv>0?-1:1);
		}
		if(!(shoes.get() instanceof SpringShoes)&&!(armor.get() instanceof EnergyArmor)){
			if(abs(xv)>0.01){
				int d1=0,d2=0;
				for(int d0=-1;d0<=1;++d0){
					int m=(int)World.cur.setting.BW/2;
					int d=m;
					double x0=x+d0;
					if(xv*d0>0)x0+=d0*min(0.2,abs(xv))*24;
					for(int i=0;i<m;++i)if(!World.cur.get(x0,y-i).isCoverable()){
						d=i;
						break;
					}
					if(d>=max(m,6)){
						d1+=d0;
						d2+=1;
					}
				}
				if(d2>0){
					xdir=-d1;
					if(ydir>0)ydir=0;
				}
			}
			if(!af_flag&&yv<-0.5){
				if(World.cur.get(x,y-5).isSolid())
				for(SingleItem si:items.toArray()){
					Item w=si.get();
					if(w instanceof AirshipFlank){
						items.select(si);
						clickAt(x+xv,y-1-height());
						break;
					}
				}
			}
		}
	}

	public void action(){
		super.action();
	}

	public void onAttacked(game.entity.Attack a){
		super.onAttacked(a);
		if(a.src!=null)Enemy.ins(es,new Enemy(a.val,a.src,this));
	}

	public boolean chkRemove(long t){
		//if(src!=null&&src instanceof ZombieBase)return false;
		//return t>30*600;
		return false;
	}

	void onKill(){
		if(base!=null){
			Spark.explode(x,y,xv,yv,4,0.1,0.2,this);
			initItems();
		}else{
			World.cur.getMode().onZombieDead(this);
			//dropItems();
			//new ZombieCrystal().drop(x,y,2);
		}
		super.onKill();
	}

	@Override
	void touchBlock(int x,int y,Block b){
		if(goal instanceof PickUpItem){
			if(b.isSolid()&&rnd()<0.01)goal=new DesBlock(new BlockAt(x,y));
		}
		super.touchBlock(x,y,b);
	}
	
	

	void upgrade(){
		Zombie z=new ZombieRobot(x,y);
		z.dir=dir;

		z.add();
		kill();
	}

	@Override
	public void pick(DroppedItem item){
		super.pick(item);
		checkArmor();
	}
	
	void protectBase(){
		goal=new ProtectBase();
	}
	
	void pickUpItem(){
		goal=new PickUpItem();
	}
	double getArmorValue(Armor ar){
		if(ar==null)return -1;
		if(ar instanceof Vehicle)return 1e6-ar.damage*0.01;
		if(ar instanceof EnergyStoneArmor)return 1e5-ar.damage*0.01;
		return ar.maxDamage()-ar.damage*0.01;
	}
	void checkArmor(){
		Armor ar=armor.get();
		double v=getArmorValue(ar);
		
		SingleItem[] si=items.toArray();
		for(int x=0;x<si.length;++x){
			if(!si[x].isEmpty()){
				items.select(si[x]);
				Item i=si[x].get();
				if(i instanceof Armor){
					double v1=getArmorValue((Armor)i);
					if(v<v1){
						v=v1;
						SingleItem.exchange(si[x],armor);
					}
				}else if(i instanceof SpringShoes){
					SingleItem.exchange(si[x],shoes);
				}
			}
		}
	}

	abstract class Goal implements java.io.Serializable{
		private static final long serialVersionUID=1844677L;
		Goal prev;
		abstract public Goal update();
		Goal(){
			prev=Zombie.this.goal;
		}
		Goal popCurGoal(){
			prev=Zombie.this.goal.prev;
			return this;
		}
	}

	class KillAgent extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent a;
		KillAgent(Agent _a){a=_a;}
		public Goal update(){
			if(a==null){
				return prev;
			}
			if(a.isRemoved()){
				return prev;
			}
			if(a.camp==camp)return prev;
			if(hp<maxHp()*0.8&&rnd()<0.2){
				return new EatFood();
			}
			if(hp<maxHp()*0.3&&rnd()<0.05){
				return prev;
			}
			if(rnd()<0.9&&distLinf(a)<World.cur.setting.BW)(new RangedAttack(a)).update();
			if(rnd()<0.5){
				return new MoveToAgent(a);
			}
			return new Attack(a);
		}
	}

	class PickUpItem extends Goal{
		private static final long serialVersionUID=1844677L;
		PickUpItem(){
			xdir=rnd()<0.5?-1:1;
			ydir=0;
		}
		public Goal update(){
			if(rnd()<0.01)xdir=rnd()<0.5?-1:1;
			ydir=0;
			climbable=false;
			if(rnd()<0.001)return prev;
			if(rnd()<0.05){
				SingleItem si[]=items.toArray();
				if(si[rndi(0,si.length-1)].getAmount()>0)
					return prev;
				randomPlaceBlock();
			}
			if(rnd()<0.05&&es[rndi(0,es.length-1)]!=null){
				return prev;
			}
			return this;
		}
	}

	class ProtectBase extends Goal{
		private static final long serialVersionUID=1844677L;
		ProtectBase(){
			checkArmor();
		}
		public Goal update(){
			if(base==null||base.isRemoved())return prev;
			if(distL1(base)>6){
				return new MoveToAgent(base);
			}
			if(hp>=maxHp()*0.3){
				Agent a=base.getMainTarget();
				if(a!=null){
					return new KillAgent(a);
				}
			}
			xdir=x<base.x?1:-1;
			ydir=y<base.y?1:-1;
			if(rnd()<0.1)randomPlaceBlock();
			if(distL1(base)<1&&rnd()<0.01){
				base.addHp(hp);
				kill();
			}
			return this;
		}
	}
	
	void randomPlaceBlock(){
		//Text.gen(x,y,"RPB",this);
		SingleItem si[]=items.toArray();
		for(int i=0;i<5;++i){
			int w=rndi(0,si.length-1);
			Item it=si[w].get();
			if(it!=null&&it instanceof Block){
				items.select(si[w]);
				for(int j=0;j<5;++j){
					double x1=x+rnd_gaussion()*2,y1=y+rnd_gaussion()*2;
					clickAt(x1,y1);
					//Text.gen(x1,y1,"click",this);
				}
				break;
			}
		}
	}
	
	void craft(){
		
	}
	
	public void setTarget(Agent pl){
		KillPlayer k=new KillPlayer();
		k.pl=pl;
		goal=k;
	}

	class KillPlayer extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent pl=null;
		KillPlayer(){}
		private final void findPlayer(){
			if(rnd()<0.5){
				findHuman();
				return;
			}
			double md=30;
			if(rnd()<0.05)md=1e8;
			for(Player p:World.cur.getPlayers()){
				double d=distL1(p);
				if(d<md){
					md=d;
					pl=p;
				}
			}
		}
		private final void findHuman(){
			double md=300;
			for(Agent a:World.cur.getNearby(x,y,World.cur.setting.BW,World.cur.setting.BW/2,false,false,true).agents){
				if(a==Zombie.this)continue;
				if(a instanceof Human){
					double d=distL2(a);
					if(a instanceof Zombie){
						if(((Zombie)a).camp==camp)continue;
						d*=5;
					}
					if(d<md){
						pl=(Human)a;
						md=d;
					}
				}
			}
		}
		public Goal update(){
			if(pl==null){
				if(rnd()<0.05)findPlayer();
				return this;
			}
			if(pl.isRemoved()){
				pl=null;
				return this;
			}
			if(rnd()<0.2&&hp<maxHp()*0.8){
				return new EatFood();
			}
			if(rnd()<0.05)checkArmor();
			if(rnd()<0.9&&distLinf(pl)<World.cur.setting.BW)(new RangedAttack(pl)).update();
			if(rnd()<0.1){
				Agent a=Enemy.weightedSelect(es);
				if(a!=null){
					if(abs(a.x-x)+abs(a.y-y)<(abs(pl.x-x)+abs(pl.y-y))*0.8-2)
						return new KillAgent(a);
				}
			}
			if(rnd()<0.2){
				(new MoveToAgent(pl)).update();
			}
			if(rnd()<0.5)(new PhysicsAttack(pl)).update();
			return this;
		}
	}
	static class WorldMap implements java.io.Serializable{
		private static final long serialVersionUID=1844677L;
		int bmp[]=new int[1024];
		boolean get(int x,int y){
			x=(x&4095)>>2;
			y=(y&127)>>2;
			return (bmp[x]>>y&1)!=0;
		}
		void set(int x,int y){
			x=(x&4095)>>2;
			y=(y&127)>>2;
			bmp[x]|=1<<y;
		}
		void reset(int x,int y){
			x=(x&4095)>>2;
			y=(y&127)>>2;
			bmp[x]&=~(1<<y);
		}
	};
	private boolean selectSolidBlock(){
		for(SingleItem si:items.toArray()){
			Item it=si.get();
			if(it instanceof Block){
				if(!((Block)it).isSolid())continue;
				if(it instanceof ExplosiveBlock)continue;
				items.select(si);
				return true;
			}
		}
		return false;
	}
	private boolean selectSolidBlockX(){
		for(SingleItem si:items.toArray()){
			Item it=si.get();
			if(rnd()<0.2)
			if(it instanceof game.item.BigShield||it instanceof AirshipFlank){
				items.select(si);
				return true;
			}
			if(it instanceof Block){
				if(!((Block)it).isSolid())continue;
				if(it instanceof ExplosiveBlock)continue;
				items.select(si);
				return true;
			}
		}
		return false;
	}
	public int getCraftType(){
		return CraftInfo._energy;
	}
	boolean useful(Item it){
		if(it==null)return false;
		if(it instanceof EnergyCell){
			EnergyCell ec=(EnergyCell)it;
			if(ec.getEnergy()<=30)return false;
		}
		for(SingleItem si:items.toArray()){
			Item it2=si.get();
			if(it2!=null){
				if(it2.dominate(it))return false;
			}
		}
		return it instanceof Tool
		||it instanceof DirtType
		||it instanceof Iron
		||it instanceof EnergyMotor
		||it instanceof ExplosiveBlock
		||it instanceof Mine
		||it instanceof LaunchableItem
		||it instanceof FieldGen
		||it instanceof game.item.BigShield
		||it instanceof AirshipFlank
		||it instanceof game.item.Warhead
		||it instanceof EnergyCell
		||it.foodVal()>0;
	}
	static Craft ec=Craft.get(EnergyCell.full())[0];
	static Craft skb=Craft.get(new SkateBoard())[0];
	static Craft esa=Craft.get(new EnergyStoneArmor())[0];
	static Craft tel=Craft.get(new TeleportationStick())[0];
	static Craft ira=Craft.get(new IronArmor())[0];
	static Craft dia=Craft.get(new DiamondArmor())[0];
	static Craft esg=Craft.get(new EnergySubmachineGun())[0];
	class KillHuman extends Goal{
		private static final long serialVersionUID=1844677L;
		static final double E_ATK=1;//6
		double xl,xr,yl,yr;
		int matk0=0;
		int xdir0=1;
		int time=0;
		Human pl=null;
		double e_atk=E_ATK;
		WorldMap mp=new WorldMap();
		int ladder_x;
		int state=0;//0:find ladder  1:walk on floor
		double x0;
		KillHuman(){
			xl=xr=x;
			yl=yr=y;
			time=0;
			for(;;){
				x0=rnd(3);
				if(rnd(3)<min(2*x0+1,4-x0))break;
			}
			x0=-ECPvPMode.HALF_WORLD_WIDTH+x0/3*(ECPvPMode.HALF_WORLD_WIDTH*2);
		}
		private final void findHuman(){
			double md=300;
			for(Agent a:World.cur.getNearby(x,y,World.cur.setting.BW,World.cur.setting.BW/2,false,false,true).agents){
				if(a==Zombie.this)continue;
				//if(a instanceof Zombie)continue;//test
				if(a instanceof Human){
					double d=distL2(a);
					if(d<md){
						pl=(Human)a;
						md=d;
					}
				}
			}
		}
		public Goal update(){
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			if(locked){
				if(Zombie.this instanceof ZombieRobot&&mode.airship.xv>0)return this;
				if(x0<mode.cur_l||x0>mode.cur_r||abs(mode.airship.x-x0)<2)ydir=-1;
				return this;
			}
			xl=min(x,xl);xr=max(x,xr);
			yl=min(y,yl);yr=max(y,yr);
			++time;
			//if(World.cur.time%10==0)Text.gen(x,y,"M:"+matk0,Zombie.this);
			if(time>16&&rnd()<0.09){
				if(max(xr-xl,(yr-yl)/3.)<1)matk0+=1;
				if(max(xr-xl,yr-yl)<1&&(xdir!=0||ydir!=0)){
					for(int t=0;t<5;++t){
						BlockAt ba=World.cur.get1(f2i(rnd(xl-0.2,xr+0.2)),f2i(rnd(yl,yr)));
						if(!ba.block.isCoverable()){
							new DesBlock(ba).update();
							break;
						}
					}
				}
				xl=xr=x;
				yl=yr=y;
				time=0;
			}
			if(matk0>0&&rnd()<1./300)matk0=max(0,matk0-5);
			if(rnd()<0.2&&hp<maxHp()*0.8){
				new EatFood().update();
			}
			if(rnd()<0.05)checkArmor();
			if(true){
				double c=rnd(1,4);
				int x1=f2i(x+rnd(-c,c));
				int y1=f2i(y+rnd(-c,c));
				Block b=World.cur.get(x1,y1);
				if(b instanceof IronBoxBlock){
					IronBoxBlock box=(IronBoxBlock)b;
					for(SingleItem si:items.toArray()){
						if(si.isEmpty())continue;
						if(!useful(si.get()))box.insert(si);
					}
					
					for(SingleItem si:box.getItems()){
						if(useful(si.get())){
							items.insert(si);
							//if(si.isEmpty())System.out.printf("%g,%g: %s\n",x,y,it.getClass().toString());
						}
					}
				}
				if(b instanceof IronWorkBenchBlock){
					esa.start(Zombie.this);
					if(!selectItem(TeleportationStick.class,true))tel.start(Zombie.this);
					if(shoes.isEmpty()&&!selectItem(SkateBoard.class,true))skb.start(Zombie.this);
					if(rnd()<0.5)ec.start(Zombie.this);
					//ira.start(Zombie.this);
					if(rnd()<0.3)dia.start(Zombie.this);
					if(rnd()<0.7)esg.start(Zombie.this);
				}else if(b instanceof IronDoorBlock){
					if(((IronDoorBlock)b).tp==0){
						b.onClick(x1,y1,Zombie.this);
					}
				}else if(b instanceof WoodenDoorBlock){
					if(((WoodenDoorBlock)b).tp==0){
						b.onClick(x1,y1,Zombie.this);
					}
				}
			}
			if(rnd()<0.05)findHuman();
			if(pl==null){
				if(true){
					int px=f2i(x),py=f2i(y);
					//if(rnd()<0.03)Text.gen(x,y,state+"",Zombie.this);
					
					mp.set(px,py);
					
					
					if(state==1){
						Block b=World.cur.get(x+xdir,y+rnd(-0.7,0.7)).rootBlock();
						if(xdep!=0||b.isSolid()||b instanceof WoodenDoorBlock||b instanceof IronDoorBlock||rnd()<0.015)state=2;
					}
					if(state==2){
						if(px==ladder_x)state=0;
						else xdir=(px>ladder_x?-1:1);
					}
					if(state==0){
						/*if(World.cur.get(x,y-2).rootBlock() instanceof DirtType){
							xdir=(x>0?-1:1);ydir=0;
						}*/
						
						
						double fc=min(50,(mode.cur_r-mode.cur_l)/3);
						boolean near_out=(x<mode.cur_l+fc||x>mode.cur_r-fc);
						if((x<mode.cur_l+5||x>mode.cur_r-5)&&rnd()<1./8)matk0+=1;
						if(rnd()<0.0001)xdir=(rnd()<0.5?-1:1);
						Block b=World.cur.get(px,py).rootBlock();
						Block b1=World.cur.get(px,py-1).rootBlock();
						if(b instanceof IronLadderBlock||b instanceof LadderBlock
						||b instanceof IronDoorBlock&&b1 instanceof IronLadderBlock
						||b instanceof WoodenDoorBlock&&b1 instanceof LadderBlock
						){
							if(World.cur.get(px,py+4).isCoverable())mp.set(px,py+4);
							if(!mp.get(px,py+4)&&!near_out){
								double xd=x-(px+0.5);
								ydir=1;
								xdir=(xd>0?-1:1);
							}else if(mp.get(px,py-4)){
								double xd=x-(px+0.5);
								if(rnd()<0.1)ydir=-1;
								xdir=(xd>0?-1:1);
							}else{
								ydir=0;
								if(near_out){
									if(x<mode.cur_l+fc){xdir=1;state=0;}
									if(x>mode.cur_r-fc){xdir=-1;state=0;}
								}
							}
							if(rnd()<0.1&&ydep==0){
								int xd=(rnd()<0.5?-1:1);
								if(World.cur.get(x,y-3).rootBlock() instanceof DirtType){
									if(abs(x-mode.next_box_pos)>20)xdir0=(x<mode.next_box_pos?1:-1);//TO DEBUG
									xdir=xdir0;
								}else if(!mp.get(px+xd*4,py)&&World.cur.get(x+xd*rnd(1,10),y-rnd(1,3)).isSolid()){
									state=1;
									ladder_x=px;
									xdir=xd;
								}
							}
						}else{
							//if(xdir!=0&&ydep==0&&!in_wall&&!climbable&&rnd()<0.1)ydir=-1;
							if(xdir!=0&&ydep!=0&&ydir==-1)ydir=0;
							if(near_out){
								if(x<mode.cur_l+fc){xdir=1;state=0;}
								if(x>mode.cur_r-fc){xdir=-1;state=0;}
							}
						}
					}
					desTouchedBlock();
				}
				if(cur_craft!=null)xdir=ydir=0;
				if(xdir!=0&&in_wall)ydir=1;
			}else if(pl.isRemoved()||abs(x-pl.x)>World.cur.setting.BW+pl.width()||abs(y-pl.y)>World.cur.setting.BW/2+pl.height()){
				pl=null;
				e_atk=E_ATK;
			}else{
				dir=(x<pl.x?1:-1);
				/*if(rnd()<0.1){
					Agent a=Enemy.weightedSelect(es);
					if(a!=null){
						if(abs(a.x-x)+abs(a.y-y)<(abs(pl.x-x)+abs(pl.y-y))*0.8-2)
							return new KillAgent(a);
					}
				}*/
				if(rnd()<0.2){
					int cnt=0;
					double atk=0,food=0;
					//if(pl instanceof Zombie)e_atk*=10;//?
					double ar=getArmorRate(),e_ar=pl.getArmorRate();
					{
						Item it=pl.getCarriedItem().get();
						if(it!=null)e_atk=max(e_atk,it.swordVal());
					}
					for(SingleItem si:items.toArray()){
						Item it=si.get();
						if(it==null)continue;
						atk=max(atk,it.swordVal());
						food+=it.foodVal()*si.getAmount();
					}
					double e_atkx=e_atk;
					if(pl instanceof Zombie)e_atkx+=10;
					else e_atkx=max(0,e_atkx-matk0*3);
					if((hp+food)/(e_atkx*ar)<=(pl.hp+5)/(atk*e_ar)){
						xdir=x<pl.x?-1:1;
						state=0;
						if(distLinf(pl)<4)
						if(selectSolidBlock()){
							for(int t=0;t<3;++t)clickAt(pl.x+xdir*rnd(0,1),pl.y+rnd(-1,1));
						}
					}else{
						new MoveToAgent(pl).update();
						
						if(pl.xdir*(pl.x-x)>0&&distLinf(pl)<4)
						if(selectSolidBlock()){
							for(int t=0;t<3;++t)clickAt(pl.x+pl.xdir*rnd(0,1),pl.y+rnd(-1,1));
						}
					}
					
					if(x<mode.cur_l+2){xdir=1;state=0;}
					if(x>mode.cur_r-2){xdir=-1;state=0;}
				}
				dir=(x<pl.x?1:-1);
				final double w1=0.3f,h1=0.5f,x1=x+dir*(width()+w1);
				if(abs(pl.x-x1)<w1+pl.width()+abs(xv)+abs(pl.xv)&&abs(pl.y-y)<h1+pl.height()){
					attack();
					new PhysicsAttack(pl).update();
				}
				if(rnd()<(pl instanceof Zombie ? 0.005 : 1))(new RangedAttack(pl)).update();
			}
			if(World.cur.get(x,y-1).isSolid()){
				if(xdir!=0&&ydir>=0){
					if(World.cur.get(x+xdir,y-0.1).isSolid()
						||!World.cur.get(x+xdir,y-1.1).isSolid()
						&& World.cur.get(x+xdir*2,y-1.1).isSolid())ydir=1;
					else if(rnd()<0.1)ydir=0;
				}
			}else if(ydir>0&&rnd()<0.1)ydir=0;
			if(xdir==0)xdir=xdir0;
			else if(state==0)xdir0=xdir;
			if(ydir<=0&&rnd()<0.1){
				if(selectItem(SkateBoard.class,true))useCarriedItem();
			}
			if((ydir>0||xdep!=0)&&shoes.get() instanceof SkateBoard){
				items.insert(shoes);
				checkArmor();
			}
			if(ball_cd>6)cancelDes();
			if(rnd()<0.2){
				if(des_flag)new DesBlock(World.cur.get1(des_x,des_y)).update();
				if(!des_flag){
					int px=f2i(x+xdir),py=f2i(y-0.7);
					BlockAt ba=World.cur.get1(px,py);
					if(ba.block instanceof SingleBlockPlantType)new DesBlock(ba).update();
					else if(!selectSolidBlock()&&pl==null){
						desTouchedBlock();
					}
				}
			}
			//Text.gen(x,y,state+":"+xdir+","+ydir,Zombie.this);
			return this;
		}
	}

	class EatFood extends Goal{
		private static final long serialVersionUID=1844677L;
		EatFood(){}
		public Goal update(){
			int mv=0;
			for(SingleItem s:items.toArray())if(!s.isEmpty()){
					Item it=s.get();
					int v=it.foodVal();
					if(v>mv&&v<(maxHp()-hp)*1.5){
						mv=v;
						items.select(s);
					}
				}
			if(mv>0)eat(items.popItem());
			return prev;
		}
	}

	class Attack extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent a;
		Attack(Agent _a){a=_a;}
		public Goal update(){
			final double w1=0.3f,h1=0.5f,x1=x+dir*(width()+w1);
			if(abs(a.x-x1)<w1+a.width()&&abs(a.y-y)<h1+a.height()){
				attack();
				return new PhysicsAttack(a).popCurGoal();
			}
			double xd=abs(a.x-x);
			if(xd<32){
				return new RangedAttack(a).popCurGoal();
			}
			return prev;
		}
	}
	
	public class ECAirship extends Goal{
		private static final long serialVersionUID=1844677L;
		boolean first=true;
		boolean first_upd=true;
		int round=0;
		SingleItem tmp=new SingleItem();
		public ECAirship(){}
		void nextBoxPos(){
			if(round>8)return;
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			double m=(mode.nxt_l+mode.nxt_r)/2;
			double d=(mode.nxt_r-mode.nxt_l)/2;
			mode.next_box_pos=m+rnd(-d/2,d/2)+rnd(-d/2,d/2);
			mode.next_box_state=0;
		}
		void genBox(){
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			mode.next_box_state=1;
			BoxShield box=new BoxShield();
			if(rnd()<0.3){
				box.insert(new EnergyStoneArmor().setAmount(1));
			}else if(rnd()<0.5){
				box.insert(new RecoillessGun().setAmount(1));
				box.insert(FuelEnergyCell.full().setAmount(1));
				if(rnd()<0.3)box.insert(new game.item.RPG_HEAT().setAmount(3));
				if(rnd()<0.3)box.insert(new game.item.RPG_HE().setAmount(3));
				if(rnd()<0.3)box.insert(new game.item.RPG_Empty().setAmount(3));
			}else{
				if(rnd()<0.3)box.insert(new EnergyMachineGun().setAmount(1));
				box.insert(new EnergySubmachineGun().setAmount(1));
				box.insert(FuelEnergyCell.full().setAmount(1));
				if(rnd()<0.2)box.insert(BlueCrystalEnergyCell.full().setAmount(1));
				if(rnd()<0.5)box.insert(new SpecialBullet(rndi(1,5)).setAmount(1+rf2i(rnd_exp(32))));
				box.insert(new game.item.Bullet().setAmount(99));
				if(rnd()<0.5)box.insert(new game.item.DarkGuidedBullet().setAmount(1+rf2i(rnd_exp(1))));
			}
			
			if(rnd()<0.8)box.insert(new QuartzArmor().setAmount(1));
			
			if(rnd()<0.3)box.insert(new game.item.IronBall_HD().setAmount(1+rf2i(rnd_exp(1))));
			if(rnd()<0.15)box.insert(new game.item.HeatBall().setAmount(1+rf2i(rnd_exp(1))));
			if(rnd()<0.15)box.insert(new game.item.DarkIronBall().setAmount(1+rf2i(rnd_exp(1))));
			
			
			if(rnd()<0.5)box.insert(new EnergyStone().setAmount(1+rf2i(rnd_exp(5))));
			if(rnd()<0.5)box.insert(new Iron().setAmount(1+rf2i(rnd_exp(5))));
			
			box.ent.initPos(x,y-2,xv,yv,SourceTool.make(Zombie.this,"发射的")).add();
		}
		public Goal update(){
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			SingleItem si=items.getSelected();
			si.clear();
			if(first_upd){
				first_upd=false;
				nextBoxPos();
			}
			if(xv<0)first=false;
			/*if(!first&&rnd()<0.003){
				Item it[]=Craft._throwable_item;
				Item w=it[rndi(0,it.length-1)].clone();
				if(!(w instanceof game.item.BlackHole)){
					tmp.clear();
					tmp.insert(w.setAmount(5));
					World.showText(">>> 飞行箱将使用"+w.getName()+"进行轰炸");
				}
			}*/
			if(!tmp.isEmpty()&&World.cur.time%10==0){
				si.insert(tmp.popItem());
				setDes(x,y-2);
			}
			xdir=ydir=0;
			double c=(125-y-yv*10)*5;
			if(c>rnd())ydir=1;
			else if(-c>rnd())ydir=-1;
			//if(y>126||yv>+0.1)ydir=-1;
			//if(y<124||yv<-0.1)ydir=1;
			if(round%2==0){
				if(xv<0.4)xdir=1;
				if(xv>0.6)xdir=-1;
				if(x>mode.next_box_pos&&mode.next_box_state==0){
					genBox();
				}
				if(x>mode.cur_r-10){
					++round;
					nextBoxPos();
				}
			}else{
				if(-xv<0.4)xdir=-1;
				if(-xv>0.6)xdir=1;
				if(x<mode.next_box_pos&&mode.next_box_state==0){
					genBox();
				}
				if(x<mode.cur_l+10){
					++round;
					nextBoxPos();
				}
			}
			
			if(rnd()<0.2&&hp<maxHp()*0.8){
				si.insert(new PlantEssence());
				new EatFood().update();
			}
			return this;
		}
	}

	int ra_cache[]=null;
	int ball_cd=0,rpg_cd=0,rpg_cd_tot=0;
	class RangedAttack extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent a;
		RangedAttack(Agent _a){a=_a;}
		public Goal update(){
			if(ball_cd>0)--ball_cd;
			if(rpg_cd>0)--rpg_cd;
			if(rpg_cd_tot>0)--rpg_cd_tot;
			SingleItem sel=items.getSelected();
			SingleItem[] si=items.toArray();
			if(ra_cache==null){
				ra_cache=new int[4];
			}
			//if(rnd()<0.1||items.getSelected().isEmpty())
			for(int t=0;t<15;++t){
				int x=rndi(0,si.length-1);
				if(t<8){
					x=ra_cache[x%4];
				}
				if(!si[x].isEmpty()){
					//System.err.println("RangedAttack: "+si[x].get());
					boolean is_ball=(si[x].get() instanceof game.item.ThrowableItem);
					boolean is_rpg=(si[x].get() instanceof game.item.RPGLauncher);
					if(is_ball && ball_cd>0)continue;
					if(is_rpg && (rpg_cd>0||rpg_cd_tot>160))continue;
					
					items.select(si[x]);
					if(si[x].get().autoUse(Zombie.this,a)){
						ra_cache[rndi(0,3)]=x;
						if(is_ball)ball_cd=10;
						if(is_rpg){
							rpg_cd=32;
							rpg_cd_tot+=60;
						}
						//System.err.println(World.cur.time+" RangedAttack success: "+si[x].get());
						return prev;
					}
					//clickAt(a.x+rnd(-a.width(),a.width()),a.y+rnd(-a.height(),a.height()));
					//break;
				}
			}
			items.select(sel);
			//System.err.println(World.cur.time+" RangedAttack failed");
			/*Item it=items.getSelected().get();
			if(it!=null){
				it.autoUse(Zombie.this,a);
				if(rnd()<0.9)return this;
			}*/
			return prev;
		}
	}

	class PhysicsAttack extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent a;
		PhysicsAttack(Agent a){this.a=a;}
		public Goal update(){
			double mv=0;
			for(SingleItem s:items.toArray())if(!s.isEmpty()){
				Item it=s.get();
				double v=it.swordVal()*it.getWeight(a);
				if(v>mv){
					mv=v;
					items.select(s);
				}
			}
			return prev;
		}
	}

	class DesBlock extends Goal{
		private static final long serialVersionUID=1844677L;
		BlockAt ba;
		public DesBlock(BlockAt _ba){ba=_ba;}
		public Goal update(){
			setDes(ba.x,ba.y);
			double mv=-100;
			if(ba.exist()){
				if(World.cur.destroyable(ba.x,ba.y))
					for(SingleItem s:items.toArray())if(!s.isEmpty()){
						Item it=s.get();
						double v=1;
						if(ba.block instanceof DirtType)v=it.shovelVal();
						if(ba.block instanceof StoneType)v=it.pickaxVal();
						if(ba.block instanceof WoodenType)v=it.axVal();
						v*=it.getWeight(ba.block);
						if(it instanceof Tool){
							Tool tool=(Tool)it;
							v-=10./max(1,tool.maxDamage()-tool.damage);
						}
						if(v>mv){
							mv=v;
							items.select(s);
						}
					}
			}
			return prev;
		}
	}
	
	void desTouchedBlock(){
		if(rnd()<0.02)cancelDes();
		
		if(in_wall&&rnd()<0.5){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y+rnd(-height(),height())));
			if(ba.block.isSolid()){
				new DesBlock(ba).update();
				return;
			}
		}

		if(xdir!=0&&xdep!=0&&rnd()<0.5){
			if(rnd()<0.3)xdir=ydir=0;
			else ydir=1;
			BlockAt ba=World.cur.get1(f2i(x+(width()+0.1)*xdir*rnd()),f2i(y+height()*rnd(0.2,1.1)));
			if(ba.block.isSolid()){
				new DesBlock(ba).update();
				return;
			}
				
		}

		if(ydep>0&&rnd()<0.7){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y+height()*rnd(0.5,1.1)));
			if(ba.block.isSolid()){
				new DesBlock(ba).update();
				return;
			}
		}

		if(ydir<0&&ydep<0&&rnd()<0.5){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y-height()));
			if(ba.block.isSolid()){
				new DesBlock(ba).update();
				return;
			}
		}
	}

	class MoveToAgent extends Goal{
		private static final long serialVersionUID=1844677L;
		Agent a;
		MoveToAgent(Agent _a){a=_a;}
		public Goal update(){
			if(a.isRemoved()){
				xdir=ydir=0;
				return prev;
			}

			if(abs(x-a.x)<width()+a.width()&&(a.x-x)*xdir>0)xdir=rndi(-1,1);
			else if(x<a.x)xdir=1;
			else xdir=-1;
			
			if(abs(x-a.x)>6||abs(y-a.y)<height()+a.height())ydir=rndi(-1,1);
			else if(y<a.y)ydir=1;
			else ydir=-1;
			
			dir=(x<a.x?1:-1);
			
			if(armor.get() instanceof EnergyArmor&&rnd()<0.2){
				EnergyArmor ar=(EnergyArmor)armor.get();
				if(!ar.hasEnergy(10)){
					for(SingleItem si:items.toArray()){
						if(si.get() instanceof EnergyCell){
							EnergyCell ec=(EnergyCell)si.get();
							if(ec.getEnergy()>=10){
								armor.insert(si);
								break;
							}
						}
					}
				}
			}
			
			if(armor.get() instanceof JetPack){
				if(!a.isWeakToNormalAttack()){
					if(abs(x-a.x)<16)xdir=(x>a.x?1:-1);
					if(abs(y-a.y)<8)ydir=1;
					if(y>126)ydir=-1;
				}
			}

			if(armor.get() instanceof Airship){
				if(abs(y-a.y)<16)ydir=1;
				if(y>126)ydir=-1;
				if(abs(x-a.x)<32)xdir=(x>a.x?1:-1);
				if(abs(xv)>0.4)xdir=(xv>0?-1:1);
				if(yv>0.4||yv<-0.2)ydir=(yv>0?-1:1);
			}
			
			if((armor.get() instanceof Tank
			||armor.get() instanceof Shilka)
			&&!a.isWeakToNormalAttack()){
				if(abs(x-a.x)<30)xdir=0;
				if(abs(x-a.x)<20)xdir=(x>a.x?1:-1);
				if(a.y<y)ydir=1;
			}

			if(rnd()<0.02)cancelDes();

			desTouchedBlock();

			return prev;
		}
	}
};
