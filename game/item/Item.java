package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;
import game.entity.*;
import game.block.Block;
import util.BmpRes;
import game.block.AirBlock;
import graphics.Canvas;
import util.SerializeUtil;
import game.world.StatMode.StatResult;

public abstract class Item implements java.io.Serializable,Cloneable,NormalAttacker{
	private static final long serialVersionUID=1844677L;
	
	//所有物品的基类
	//物品一般只能存在于某个SingleItem中
	
	public double getPrice(StatResult result){
		double s=result.getPrice0(this);
		if(this instanceof Tool){
			Tool t=(Tool)this;
			s*=max(0,min(1,1-t.damage*1./t.maxDamage()));
		}
		if(this instanceof DefaultItemContainer){
			for(SingleItem si:((DefaultItemContainer)this).getItems().toArray())s+=si.getPrice(result);
		}
		return s;
	}

	public double light(){return 0;}
	public int maxAmount(){return 99;}//最大叠加
	public int pickaxVal(){return 1;}//镐值
	public int shovelVal(){return 1;}//铲值
	public int axVal(){return 1;}//斧值
	public int swordVal(){return 1;}//剑值
	public int fuelVal(){return 0;}//燃料值
	public int foodVal(){return 0;}//食物值
	public int eatTime(){return 20;}//食用时间
	public void onDesBlock(Block b){}//被用于破坏方块
	public boolean onLongPress(Agent a,double tx,double ty){return false;}//被用于长按，返回true表示屏蔽默认事件
	public void onAttack(Entity a,Source src){}//被用于攻击生物
	public double hardness(){return game.entity.NormalAttacker.AGENT;}
	
	public boolean longable(){return false;}
	
	int energyCost(){return 1;}
	double mv2(){return EnergyTool.E0*energyCost();}
	
	public Entity asEnt(){return new ThrowedItem(0,0,this);}
	
	public final void onLaunch(Agent a,double slope,double mv2){
		onLaunchAtPos(a,a.dir,a.x+a.dir*(a.width()+0.05),a.y+0.2,slope,mv2);
	}//被发射
	
	public void onLaunchAtPos(Agent a,int dir,double x,double y,double slope,double mv2){
		a.throwEntAtPos(new ThrowedItem(x,y,this),dir,x,y,slope,mv2);
	}//被发射
	public String getAmountString(int cnt){
		//return "["+NormalAttacker.getHardnessName(hardness())+"]";
		if(cnt==1)return "";
		return Integer.toString(cnt);
	}
	public class OtherItem extends SingleItem{
		//private static final long serialVersionUID=1844677L;
		@Override
		protected boolean insertable(Item it){
			if(it.getClass()==Item.this.getClass())return false;
			return super.insertable(it);
		}
	}
	public class NonOverlapOtherItem extends OtherItem{
		//private static final long serialVersionUID=1844677L;
		@Override
		public int maxAmount(){return 1;}
	}
	
	//手持
	public void onCarried(game.entity.Agent a){
		if(!(a instanceof Player))return;
		Human h=(Human)a;
		Entity.predict(h);
		if(
		  this instanceof ThrowableItem
		||this instanceof RPGItem
		||this instanceof Bullet
		||this instanceof BasicBall
		||this instanceof game.block.ExplosiveBlock
		||this instanceof game.block.HDEnergyStoneBlock){
			Armor ar=h.armor.get();
			if(ar instanceof Tank){
				Entity.predict(((Tank)ar).test_shoot(h,((Tank)ar).a,this.clone()));
			}else if(ar instanceof Shilka){
				Entity.predict(((Shilka)ar).test_shoot(h,((Shilka)ar).a,this.clone()));
			}
		}
	}
	
	public boolean isCreative(){return false;}
	
	public void onVanish(double x,double y,Source src){}
	
	public boolean useInArmor(){return false;}
	
	public void onUse(Human a){//按下使用按钮
		if(foodVal()>0&&a.hp<a.maxHp()-0.1){
			if(!a.eat(this)){
				a.items.getSelected().insert(this);
			}
		}else{
			a.items.getSelected().insert(this);
			if(this instanceof DefaultItemContainer){
				if((a instanceof Player)){
					((Player)a).openDialog(new game.ui.UI_Item(this,((DefaultItemContainer)this).getItems()));
				}
			}
		}
	}
	public void using(UsingItem ent){
		if(foodVal()>0){
			((Human)ent.ent).addHp(foodVal()*1.0/eatTime());
		}
	}
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return foodVal()>0?eat_btn:this instanceof DefaultItemContainer?use_btn:empty_btn;
	}
	public boolean isBroken(){return false;}
	public void onBroken(double x,double y){//损坏
		Fragment.gen(x,y,0.3,0.3,4,4,8,getBmp());
		if(this instanceof ItemContainer)DroppedItem.dropItems((ItemContainer)this,x,y);
	}
	public void onBroken(double x,double y,Agent a){
		onBroken(x,y);
	}

	
	public boolean autoUse(final Human h,final Agent a){
		if(this instanceof Shield)return false;
		if(foodVal()>0){
			if(h.hp+foodVal()/2<h.maxHp()){
				h.items.getSelected().popItem().onUse(h);
				return true;
			}
			return false;
		}
		if(this instanceof LaunchableItem
		||this instanceof Warhead
		||this instanceof game.block.ExplosiveBlock
		||this instanceof game.block.HDEnergyStoneBlock){
			if(this instanceof Warhead){
				if(!((Warhead)this).explodeDirected())return false;
			}
			final Armor ar=h.armor.get();
			if(ar instanceof Airship){
				final boolean is_long=rnd()<0.3;
				final double x=(a.x-h.x)*(1+rnd_gaussion()*0.5),y=(a.y-h.y)*(1+rnd_gaussion()*0.5);
				
				OneDimFunc f=new OneDimFunc(){public double get(double z){
					double c=cos(z),s=sin(z);
					return Entity.predictHit(((Airship)ar).test_shoot(h,h.x+(x*c+y*s),h.y+(-x*s+y*c),Item.this.clone(),is_long),a);
				}};
				
				Pos p=Optimizer.optimize(f,0,is_long?0:1,0.01);
				if(p.y<0){
					double c=cos(p.x),s=sin(p.x);
					if((this instanceof StoneBall||this instanceof game.block.ExplosiveBlock)&&(-x*s+y*c)>0)return false;
					if(is_long)h.setDes(h.x+(x*c+y*s),h.y+(-x*s+y*c));
					else h.clickAt(h.x+(x*c+y*s),h.y+(-x*s+y*c));
					return true;
				}
			}else if(ar instanceof Tank){
				OneDimFunc f=new OneDimFunc(){public double get(double z){
					if(abs(z-PI/2)>PI/2+0.15)return 1e10;
					return Entity.predictHit(((Tank)ar).test_shoot(h,z,Item.this.clone()),a);
				}};
				
				if(f.get(((Tank)ar).a)<0){
					h.clickAt(h.x,h.y+3);
				}else{
					Pos p=Optimizer.optimize(f,((Tank)ar).a+rnd_gaussion()*0.1,1,0.01);
					if(p.y<f.get(((Tank)ar).a))h.setDes(h.x+cos(p.x)*3,h.y+sin(p.x)*3);
					if(f.get(((Tank)ar).a)<0)h.clickAt(h.x,h.y+3);
				}
				return true;
			}else if(ar instanceof Shilka){
				//World.showText(this.getName()+"");
				final Shilka sh=(Shilka)ar;
				if(sh.reload<sh.maxReload()*rnd(0.5,0.8))return true;
				OneDimFunc f=new OneDimFunc(){public double get(double z){
					if(abs(z-PI/2)>PI/2+0.15)return 1e10;
					return Entity.predictHit(sh.test_shoot(h,z,Item.this.clone()),a);
				}};
				
				Pos p=Optimizer.optimize(f,sh.a+rnd_gaussion()*0.1,1,0.01);
				if(p.y<f.get(sh.a))h.setDes(h.x+cos(p.x)*3,h.y+sin(p.x)*3);
				if(f.get(sh.a)<max(a.width(),a.height())+hypot(a.xv-h.xv,a.yv-h.yv)*8){
					if(sh.walk==0)h.clickAt(h.x,h.y);
					h.clickAt(h.x,h.y+3);
					return true;
				}
				if(sh.walk!=0)h.clickAt(h.x,h.y);
				return false;
			}else if(ar instanceof FastBall){
				//World.showText(this.getName()+"");
				final FastBall sh=(FastBall)ar;
				OneDimFunc f=new OneDimFunc(){public double get(double z){
					if(abs(z-PI/2)>PI/2+0.15)return 1e10;
					return Entity.predictHit(sh.test_shoot(h,z,Item.this.clone()),a);
				}};
				
				Pos p=Optimizer.optimize(f,sh.a+rnd_gaussion()*0.1,1,0.01);
				if(p.y<f.get(sh.a))h.setDes(h.x+cos(p.x)*3,h.y+sin(p.x)*3);
				if(f.get(sh.a)<max(a.width(),a.height())+hypot(a.xv-h.xv,a.yv-h.yv)*8){
					h.clickAt(h.x,h.y+3);
					return true;
				}
				return false;
			}
		}/*else{
			double xd=abs(a.x-h.x);
			if(xd>h.width()&&xd<32){
				h.clickAt(a.x+a.width()*rnd_gaussion()*0.5,a.y+a.height()*rnd_gaussion()*0.5);
			}
		}*/
		return false;
	}
	
	public void autoLaunch(final Human h,final Agent a){
		if(foodVal()>0)return;
		Item it=h.items.getSelected().get();
		if(!(it instanceof RPGLauncher))return;
		final RPGLauncher rl=(RPGLauncher)it;
		
		if(
		  this instanceof StoneBall
		||this instanceof RPGItem
		||this instanceof Bullet
		||this instanceof BasicBall
		||this instanceof game.block.Block){
			final double x=(a.x-h.x)*(1+rnd_gaussion()*0.5),y=(a.y-h.y)*(1+rnd_gaussion()*0.5);
			
			OneDimFunc f=new OneDimFunc(){public double get(double z){
				double c=cos(z),s=sin(z);
				return Entity.predictHit(rl.test_shoot(h,h.x+(x*c+y*s),h.y+(-x*s+y*c),Item.this.clone()),a);
			}};
			
			Pos p=Optimizer.optimize(f,0,1,0.01);
			if(p.y<0){
				double c=cos(p.x),s=sin(p.x);
				h.clickAt(h.x+(x*c+y*s),h.y+(-x*s+y*c));
			}
		}
		/*double xd=abs(a.x-h.x);
		if(xd>h.width()&&xd<32){
			h.clickAt(a.x+a.width()*rnd_gaussion()*0.5,a.y+a.height()*rnd_gaussion()*0.5);
		}*/
	}
	
	public void drawTip(Canvas cv,Player pl){}
	
	public String toString(){return getName();}
	public String getName(){return util.AssetLoader.loadString(getClass(),"name");}//获取名字
	public String getDoc(){return util.AssetLoader.loadString(getClass(),"doc");}//获取说明
	
	public static BmpRes
		eat_btn=new BmpRes("UI/eat"),
		empty_btn=new BmpRes("UI/empty"),
		talk_btn=new BmpRes("UI/talk"),
		use_btn=new BmpRes("UI/use"),
		field_btn=new BmpRes("UI/field"),
		armor_btn=new BmpRes("UI/armor"),
		rotate_btn=new BmpRes("UI/rotate");
	
	public int heatingTime(boolean in_furnace){return 1000000000;}//获取加热后发生变化所需的期望时间
	public Item heatingProduct(boolean in_furnace){return null;}//获取加热后得到的物品
	public Item clone(){//浅拷贝，可叠加的物品不应有复杂的内部结构
		Item i=null;
		try{
			i=(Item)super.clone();
		}catch(Exception e){e.printStackTrace();}
		return i;
	}
	
	public boolean cmpType(Item it){//比较是否可以合为一项
		return this.getClass()==it.getClass();
	}
	
	//被生物a用于点击世界中的x,y位置
	//return this or null, null means "this" is removed
	public Item click(double x,double y,Agent a){
		if(!a.clickable(x,y)){
			if(a instanceof Player)((Player)a).click_out_of_range=10;
			return this;
		}
		return clickAt(x,y,a);
	}
	public Item clickAt(double x,double y,Agent a){
		if(!World.cur.get(x,y).onClick(f2i(x),f2i(y),a)){
			for(Agent a1:World.cur.getNearby(x,y,0.1,0.1,false,false,true).agents){
				if(a1.onClick(x,y,a))return this;
			}
			if(a instanceof Human)((Human)a).attack();
		}
		return this;
	}
	public void onPressBlock(int x,int y,Agent a){
		World.cur.get(x,y).onPress(x,y,this);
	}
	public void onUpdate(ThrowedItem ent){}
	//public static final Item air(){return new AirBlock();}
	
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){
		if(this instanceof ItemContainer){
			int cnt=src.getAmount();
			ItemContainer ic=(ItemContainer)this;
			ic.insert(src);
			if(cnt>src.getAmount())return true;
			for(SingleItem si:ic.toArray()){
				if(!batch)continue;
				if(SingleItem.exchange(src,si)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean dominate(Item it){return false;}
	
	public BmpRes getBmp(){//获取默认贴图
		return new BmpRes("Item/"+getClass().getSimpleName());
	}
	public SingleItem setAmount(int amount){//转化为一个SingleItem
		amount=min(amount,maxAmount());//TO DEBUG
		return new SingleItem(this,amount);
	}
	
	//掉落指定数量
	public DroppedItem drop(int x,int y,int c){
		DroppedItem d=new DroppedItem(x,y,setAmount(c));
		d.add();
		return d;
	}
	public DroppedItem drop(double x,double y,int c){
		DroppedItem d=new DroppedItem(x,y,setAmount(c));
		d.add();
		return d;
	}
	//掉落一个
	public DroppedItem drop(int x,int y){
		return drop(x,y,1);
	}
	public DroppedItem drop(double x,double y){
		return drop(x,y,1);
	}
	
	public boolean xRev(){return true;}
	
	//绘制除默认贴图外的额外信息
	public void drawInfo(Canvas cv){}
	
};
interface OneDimFunc{
	public double get(double x);
}
class Pos{
	double x,y;
	Pos(double x,double y){
		this.x=x;
		this.y=y;
	}
}
class Optimizer{
	public static Pos optimize(OneDimFunc f,double x0,double r0,double eps){
		double x=x0,y=f.get(x);
		for(double r=r0;r>eps;r*=0.5){
			for(int s=-1;s<=1;s+=2){
				double a=r*s,x1=x+a;
				double y1=f.get(x1);
				if(y1<y){x=x1;y=y1;break;}
			}
			//System.err.println("Optimizer: "+x+","+y);
		}
		return new Pos(x,y);
	}
}
