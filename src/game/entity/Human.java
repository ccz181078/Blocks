package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.item.*;
import game.world.World;
import graphics.Canvas;
import game.block.Block;
import game.block.StaticBlock;

public abstract class Human extends Agent implements Crafter,Launcher{
	public static final double TOUCHABLE_XD=4,TOUCHABLE_YD=4;
	private static final long serialVersionUID=1844677L;
	public SelectableItemList items;
	protected ItemList bag_items;
	protected SpecialItem<Armor>armor;
	private double walk_state=0,des_state=0;
	protected boolean attack_flag=false;
	public String name=null;
	public double maxHp(){return 40;}
	public double maxXp(){return 40;}
	public double width(){return 0.29;}
	public double height(){return 0.95;}
	protected Human(double _x,double _y){
		super(_x,_y);
		initItems();
	}
	protected void initItems(){
		items=ItemList.emptySelectableList(15);
		bag_items=ItemList.emptyList(16);
		armor=new SpecialItem<Armor>(Armor.class);
	}
	protected void dropItems(){
		DroppedItem.dropItems(items,x,y);
		DroppedItem.dropItems(bag_items,x,y);
		DroppedItem.dropItems(armor,x,y);
	}
	public void launch(Entity e,double s,double v){
		throwEnt(e,s,v);
	}
	public void throwCarriedItem(boolean all){
		SingleItem s;
		if(all){
			s=new SingleItem();
			s.insert(items.getSelected());
		}else s=items.getSelected().pop();
		if(!s.isEmpty())throwEnt(new DroppedItem(x,y,s),rnd(-0.3,0.3),rnd(0.1,0.15));
	}
	public void desBlock(){
		Item w=items.getSelected().popItem();
		World._.get(des_x,des_y).onPress(des_x,des_y,w==null?Item.air():w);
		items.getSelected().insert(w);
	}
	public void clickAt(double tx,double ty){
		if(abs(tx-x)<TOUCHABLE_XD&&abs(ty-y)<TOUCHABLE_YD){
			int px=f2i(tx),py=f2i(ty);
			Block b=World._.get(px,py);
			if(b instanceof StaticBlock)((StaticBlock)b).deStatic(px,py);
			Item it=items.getSelected().popItem();
			if(it==null)Item.air().clickAt(tx,ty,this);
			else it=it.clickAt(tx,ty,this);
			items.getSelected().insert(it);
		}
	}
	public void eat(Item it){
		hp=min(maxHp(),hp+it.foodVal());
	}
	
	public void attack(){
		attack_flag=true;
	}
	public void update(){
		super.update();
		if(xdir!=0)walk_state=(walk_state+0.2)%PI;
		else{
			if(walk_state>PI/2)walk_state=(float)PI-walk_state;
			walk_state=max(0,walk_state-0.2f);
		}
		if(des_flag||attack_flag){
			des_state+=14;
			if(des_state>=90){
				des_state%=90f;
				attack_flag=false;
			}
		}else{
			des_state=max(0,des_state-14);
		}
		if(cur_craft!=null&&World._.time>=craft_ft){
			cur_craft.finish(this);
			cur_craft=null;
		}

		if(des_flag||attack_flag){
			Item w0=items.popItem();
			Item w=w0==null?Item.air():w0;
			final double w1=0.3f,h1=0.5f,x1=x+dir*(width()+w1);
			for(Agent a:World._.getNearby(x1,y,w1,h1,false,false,true).agents){
				if(a==this)continue;
				if(abs(a.x-x1)<w1+a.width()&&abs(a.y-y)<h1+a.height()){
					if(a instanceof Human){
						if(a instanceof Zombie && this instanceof Zombie)continue;
						Human h=(Human)a;
						if(!(h.des_flag||h.attack_flag)&&rnd()<0.05)h.throwCarriedItem(false);
					}
					a.onAttacked(w.swordVal()*0.3,this);
					a.xa+=dir*0.02;
					a.ya+=0.045;
					w.onAttack(a);
				}
			}
			items.getSelected().insert(w0);
		}
		Item w=items.popItem();
		if(w instanceof Tool){
			Tool u=(Tool)w;
			if(u.isBroken()){
				u.onBroken(x+dir*width(),y);
				w=null;
			}
		}
		items.getSelected().insert(w);
		
		Armor ar=armor.get();
		if(ar!=null&&ar.isBroken()){
			armor.clear();
			ar.onBroken(x,y);
		}
	}
	abstract BmpRes bodyBmp();
	abstract BmpRes handBmp();
	abstract BmpRes legBmp();
	public void draw(Canvas cv){
		float a=(float)sin(walk_state)*25f;
		float w=(float)width(),h=(float)height();
		cv.save();{
			cv.scale(dir,1);
			
			cv.save();{
				cv.rotate(a);
				legBmp().drawR(cv,-w,-h,w,0);
				cv.rotate(-a*2);
				legBmp().drawR(cv,-w,-h,w,0);
			}cv.restore();
			
			float b=(float)des_state,c=30-b/9;
			b+=c;
			
			cv.save();{
				cv.rotate(c,w*0f,h*0.62f);
				handBmp().drawR(cv,w*-0.55f,-h*0.1f,w*-0.05f,h*0.55f);
				cv.rotate(-b,-0.05f,0);
				Item i=items.get();
				if(i!=null)i.getBmp().draw(cv,0.1f,0.15f,0.3f,0.3f);
			}cv.restore();

			bodyBmp().drawR(cv,-w,-h*0.1f,w,h);
			Armor ar=armor.get();
			if(ar!=null)ar.getArmorBmp().drawR(cv,-w,-h*0.1f,w,h);
			
		}cv.restore();
		super.draw(cv);
		if(name!=null){
			cv.save();{
				cv.scale(1,-1);
				cv.drawText(name,0,-h-0.4f,0.4f,0);
			}cv.restore();
		}
	}

	public Craft cur_craft=null;
	public long craft_ft=0;

	public float getCraftProcess(){
		if(cur_craft==null)return -1;
		return 1-(craft_ft-World._.time)*1f/cur_craft.cost.time;
	}
	public int getCraftType(){
		return 0;
	}
	public ItemContainer getItems(){
		return items;
	}
	public int getEnergy(){
		return f2i(xp);
	}
	public void loseEnergy(int v){
		xp-=v;
	}
	public void lose(CraftInfo cost){
		xp-=cost.energy;
	}
	public void gain(SingleItem item){
		items.insert(item);
		if(!item.isEmpty())new DroppedItem(x+dir*(width()+0.2f),y,item).add();
	}
	public boolean startCraft(Craft c){
		if(cur_craft!=null)return false;
		cur_craft=c;
		craft_ft=World._.time+c.cost.time;
		return true;
	}
	public void stopCraft(){
		if(cur_craft!=null){
			cur_craft.interrupt(this);
			cur_craft=null;
		}
	}
}

