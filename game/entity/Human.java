package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import java.io.*;
import util.BmpRes;
import game.item.*;
import game.world.*;
import graphics.Canvas;
import game.block.Block;
import game.block.StaticBlock;
import game.block.AirBlock;
import game.entity.Airship_Flank;
import game.ui.UI_ItemList;
import game.world.StatMode.StatResult;

public abstract class Human extends Agent implements Crafter,DroppedItem.Picker{
	public static final double TOUCHABLE_XD=4,TOUCHABLE_YD=4;
	private static final long serialVersionUID=1844677L;
	public SelectableItemList items;
	public ItemList bag_items;
	public SpecialItem<Armor>armor;
	public SpecialItem<Shoes> shoes;
	public double walk_state=0,des_state=0;
	protected boolean attack_flag=false;
	public String name=null;
	protected int pickup_state=0;
	public double money=0;
	public int kill_cnt=0;
	public long last_eat_time;
	public Armor cur_armor=null;
	public boolean recover_item=false;
	@Override
	public boolean chkRigidBody(){
		Armor ar=armor.get();
		if(ar!=null)return ar.chkRigidBody();
		return true;
	}
	@Override
	public double fluidResistance(){
		double v=super.fluidResistance();
		Armor ar=armor.get();
		if(ar!=null)v*=ar.fluidResistance();
		return v;
	}
	@Override
	public BmpRes getCtrlBmp(){
		return new BmpRes("Entity/"+getClass().getSimpleName());
	}
	@Override
	public void initUI(game.ui.UI_MultiPage ui){
		ItemList ar=ItemList.create(armor,shoes);
		UI_ItemList il=new UI_ItemList(items,null);
		UI_ItemList il2=new UI_ItemList(ar,null);
		il.duel_ui_il=(il2);
		il2.duel_ui_il=(il);
		ui.addPage(new game.item.AgentMaker(getClass()),il);
		ui.addPage(new IronArmor(),il2);
	}
	public boolean isWeakToNormalAttack(){
		return !(armor.get() instanceof Vehicle);
	}
	
	@Override
	public String getName(){
		String name=super.getName();
		if(this.name!=null)name=this.name;
		Armor ar=armor.get();
		if(ar instanceof EnergyArmor){
			name+="(使用"+ar.getName()+")";
		}
		return name;
	}
	
	public double maxHp(){
		double v=World.cur.setting.human_max_hp;
		v+=World.cur.setting.human_max_hp_c1*kill_cnt;
		v+=World.cur.setting.human_max_hp_c2*kill_cnt*kill_cnt;
		return v;
	}
	public double maxXp(){return World.cur.setting.human_max_xp;}
	public double mass(){
		double m=1;
		Armor ar=armor.get();
		if(ar!=null)m+=ar.mass();
		Shoes sh=shoes.get();
		if(sh!=null)m+=sh.mass();
		return m;
	}
	@Override
	double frictionX(){
		double v=1;
		Shoes sh=shoes.get();
		if(sh!=null)v=sh.friction();
		Armor ar=armor.get();
		if(ar!=null)v*=ar.frictionXr();
		return v;
	}
	public double width(){
		Armor ar=armor.get();
		if(ar!=null)return ar.width();
		return 0.29;
	}
	public double height(){
		Armor ar=armor.get();
		if(ar!=null)return ar.height();
		return 0.95;
	}
	public double light(){
		double v=0.5;
		
		Item it=getCarriedItem().get();
		if(it!=null)v=max(v,it.light());
		
		Armor ar=armor.get();
		if(ar!=null)v=max(v,ar.light());
		
		Shoes sh=shoes.get();
		if(sh!=null)v=max(v,sh.light());
		
		return v;
	}
	
	protected Human(double _x,double _y){
		super(_x,_y);
		initItems();
	}
	protected int[] getItemListSize(){
		return new int[]{15,16};
	}
	public void initItems(){
		int[] item_list_size=getItemListSize();
		items=ItemList.emptySelectableList(item_list_size[0]);
		bag_items=ItemList.emptyList(item_list_size[1]);
		armor=new SpecialItem<Armor>(Armor.class);
		shoes=new SpecialItem<Shoes>(Shoes.class);
	}
	public void dropItems(){
		dropArmor();
		DroppedItem.dropItems(items,x,y);
		DroppedItem.dropItems(bag_items,x,y);
	}
	public void dropArmor(){
		DroppedItem.dropItems(getCarriedItem().pop(),x,y);
		if(!armor.isEmpty()){
			if(armor.get().isClosed())new VehiclePlaceHolder(x,y,armor.popItem()).initPos(x,y,xv,yv,SourceTool.dropOnDead(this)).add();
			else DroppedItem.dropItems(armor,x,y);
		}
		DroppedItem.dropItems(shoes,x,y);
	}
	public double getPrice(StatResult result){
		double s=money;
		for(SingleItem si:items.toArray())s+=si.getPrice(result,false);
		for(SingleItem si:bag_items.toArray())s+=si.getPrice(result,false);
		s+=armor.getPrice(result,false);
		s+=shoes.getPrice(result,false);
		return s;
	}
	public void forceSell(StatResult result){
		for(SingleItem si:items.toArray()){
			if(money>=0)return;
			money+=si.getPrice(result,false);
			si.clear();
		}
		for(SingleItem si:bag_items.toArray()){
			if(money>=0)return;
			money+=si.getPrice(result,false);
			si.clear();
		}
	}
	protected double getArmorRate(){
		double r=1;
		Armor ar=armor.get();
		if(ar!=null)r*=ar.armorRate();
		Shoes sh=shoes.get();
		if(sh!=null)r*=sh.armorRate();
		return r;
	}
	
	@Override
	void touchAgent(Agent a){
		Armor ar=armor.get();
		if(ar!=null)ar.touchAgent(this,a);
		Shoes sh=shoes.get();
		if(sh!=null)sh.touchAgent(this,a);
		super.touchAgent(a);
	}
	
	public AttackFilter getAttackFilter(){
		return new AttackFilter(){public Attack transform(Attack a){
			if(a!=null){
				Item it=items.popItem();
				if(it!=null&&it instanceof Shield)a=((Shield)it).transform(a);
				items.getSelected().insert(it);
			}
			if(a!=null){
				Armor ar=armor.get();
				if(ar!=null){
					ar.onAttacked(Human.this,a);
					a=ar.transform(a);
				}
			}
			if(a!=null){
				Shoes sh=shoes.get();
				if(sh!=null)a=sh.transform(a);
			}
			return a;
		}};
	}
	
	
	public void useCarriedItem(){
		Item it=getCarriedItem().popItem();
		if(it!=null)it.onUse(this);
	}
	@Override
	public SingleItem getCarriedItem(){
		return items.getSelected();
	}
	public void desBlock(){
		Item w=items.getSelected().popItem();
		if(w==null){
			super.desBlock();
		}else{
			//World.cur.get(des_x,des_y).onPress(des_x,des_y,w==null?new AirBlock():w);
			w.onPressBlock(des_x,des_y,this);
		}
		items.getSelected().insert(w);
	}
	public class RecoverItem{
		Item old=null,it=null;
		int cnt=0;
		GameMode mode=World.cur.getMode();
		double cost=0;
		public RecoverItem(Item it){
			this.it=it;
			if(recover_item&&it!=null&&!it.disableRecover()){
				old=util.SerializeUtil.deepCopy(it);
				cnt=getCarriedItem().getAmount()+1;
				if(mode instanceof PvPMode){
					cost+=old.getPrice(((PvPMode)mode).getStat(),false)*cnt;
				}
			}
		}
		public void end(){
			if(recover_item&&old!=null){
				if(it instanceof EnergyLauncher){
					((EnergyLauncher)old).copyCd((EnergyLauncher)it);
				}
				if(mode instanceof PvPMode){
					cost-=getCarriedItem().getPrice(((PvPMode)mode).getStat(),false);
					if(cost>=0&&cost<=money){
						money-=cost;
						getCarriedItem().set(old,cnt);
					}
				}else{
					getCarriedItem().set(old,cnt);
				}
			}
		}
	}
	public void setDes(double tx,double ty){
		teleporting=false;
		Item it=items.getSelected().popItem();
		RecoverItem ri=new RecoverItem(it);
		
		if(it!=null&&it.useInArmor()){
			if(it.onLongPress(this,tx,ty)){
				items.getSelected().insert(it);
				return;
			}
		}
		items.getSelected().insert(it);
		
		Armor ar=armor.get();
		if(ar!=null&&ar.onArmorLongPress(this,tx,ty));
		else{
			it=items.getSelected().popItem();
			if(it!=null&&it.onLongPress(this,tx,ty)){
				items.getSelected().insert(it);
			}else{
				items.getSelected().insert(it);
				dir=(tx>x?1:-1);
				super.setDes(tx,ty);
			}
		}
		ri.end();
	}
	public void clickAt(double tx,double ty){
		dir=(tx>x?1:-1);
		teleporting=false;
		//if(abs(tx-x)<TOUCHABLE_XD&&abs(ty-y)<TOUCHABLE_YD){
			int px=f2i(tx),py=f2i(ty);
			Block b=World.cur.get(px,py);
			if(b instanceof StaticBlock)((StaticBlock)b).deStatic(px,py);
			
			
			Item it=items.getSelected().popItem();
			RecoverItem ri=new RecoverItem(it);
			
			if(it!=null&&it.useInArmor()){
				it=it.clickAt(tx,ty,this);
				items.getSelected().insert(it);
			}else{
				items.getSelected().insert(it);
				
				Armor ar=armor.get();
				if(ar!=null&&ar.onArmorClick(this,tx,ty));
				else{
					it=items.getSelected().popItem();
					if(it==null)new AirBlock().click(tx,ty,this);
					else it=it.click(tx,ty,this);
					items.getSelected().insert(it);
				}
			}
			
			ri.end();
	}
	public boolean eat(Item it){
		if(last_eat_time>=World.cur.time)return false;
		//hp=min(maxHp(),hp+it.foodVal());
		int t=it.eatTime();
		last_eat_time=World.cur.time+t;
		UsingItem.gen(0.3,0.3,t,it,this);
		return true;
	}
	@Override
	public boolean rotByVelDir(){
		Armor ar=armor.get();
		if(ar!=null)return ar.rotByVelDir();
		return false;
	}
	@Override
	public float getRotation(){
		if(rotByVelDir())return (float)(a*180/PI);
		return 0;
	}
	public void attack(){
		attack_flag=true;
	}
	@Override
	public double onImpact(double v){
		Armor ar=armor.get();
		if(ar!=null)v=ar.onImpact(this,v);
		Shoes sh=shoes.get();
		if(sh!=null)v=sh.onImpact(this,v);
		//Item item=items.getSelected().get();
		//if(item instanceof DefendTool)v=((DefendTool)item).onImpact(this,v);
		return v;
	}
	@Override
	public double getJumpAcc(){
		double v=super.getJumpAcc();
		Shoes sh=shoes.get();
		if(sh!=null)v=sh.getJumpAcc(this,v);
		double v0=v;
		Armor ar=armor.get();
		if(ar!=null)v=ar.getJumpAcc(this,v);
		return max(min(v0,v),v/mass());
	}
	@Override
	public void update(){
		if(isRemoved())return;
		super.update();
		if(hp>0){
			if(World.cur.weather==Weather._energystone)addHp(0.003);
			if(World.cur.weather==Weather._plant)addHp(0.01);
			if(World.cur.weather==Weather._blood)addHp(0.03);
		}
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
		if(cur_craft!=null&&World.cur.time>=craft_ft){
			if(World.cur.getMode() instanceof ECPvPMode)World.showText("***"+getName()+"合成了"+cur_craft.out[0].get().getName());
			cur_craft.finish(this);
			cur_craft=null;
		}

		if(des_flag||attack_flag){
			Item w0=items.popItem();
			Item w=w0==null?new AirBlock():w0;
			
			Source info=SourceTool.hold(this);
			double xd=(width()+0.32),yd=0;
			if(w instanceof LongItem){
				xd+=0.3;
				yd+=0.3;
			}
			ThrowedItem e=new ThrowedItem(0,0,w);
			e.initPos(x+dir*xd,y+yd,xv,yv,info);
			e.des_flag=false;
			e.attacking=attack_flag|des_flag;
			e.no_kill=true;
			e.update0();
			e.update();
			e.anti_g=1;
			e.move();
			impulseMerge(e);
			if(w0!=null&&!e.shadowed)shadowed=false;
			/*final double w1=0.3f,h1=0.5f,x1=x+dir*(width()+w1);
			NearbyInfo ni=World.cur.getNearby(x1,y,w1,h1,false,true,true);
			for(Entity a:ni.ents){
				if(a.harmless())continue;
				if(abs(a.x-x1)<w1+a.width()&&abs(a.y-y)<h1+a.height()){
					a.onAttacked(w.swordVal()*(0.3+v2rel(a)*3),info);
					//a.xa+=dir*0.02/a.mass();
					w.onAttack(a,info);
				}
			}
			for(Agent a:ni.agents){
				if(a==this)continue;
				if(abs(a.x-x1)<w1+a.width()&&abs(a.y-y)<h1+a.height()){
					//if(a.group()==group()&&group()==Group.ZOMBIE)continue;
					if(a instanceof Human){
						Human h=(Human)a;
						Armor ar=h.armor.get();
						if(!(h.des_flag||h.attack_flag)&&rnd()<0.05&&!(ar!=null&&ar.isClosed()))h.throwCarriedItem(false,0);
					}
					a.onAttacked(w.swordVal()*(0.3+v2rel(a)*3),info);
					a.xa+=dir*0.02/a.mass();
					a.ya+=0.04*max(-1,min(1,(a.y+0.4-y)*2))/a.mass();
					w.onAttack(a,info);
				}
			}*/
			items.getSelected().insert(w0);
		}
		Item w=items.popItem();
		if(w!=null){
			w.onCarried(this);
			if(w.isBroken()){
				w.onBroken(x+dir*width(),y,this);
				w=null;
			}
		}
		items.getSelected().insert(w);
		
		Armor ar=armor.get();
		if(ar!=null){
			ar.onUpdate(this);
			if(ar.isBroken()){
				armor.clear();
				ar.onBroken(x,y,this);
			}
		}
		Shoes sh=shoes.popItem();
		if(sh!=null){
			shoes.insert(sh.update(this));
		}
		
		if(armor.get()!=cur_armor){
			cur_armor=armor.get();
			if(cur_armor!=null){
				int cost=cur_armor.wearEnergyCost();
				if(hasEnergy(cost)){
					loseEnergy(cost);
				}else{
					DroppedItem.dropItems(armor,x,y);
					cur_armor=null;
				}
			}
		}
		
	}
	abstract BmpRes bodyBmp();
	abstract BmpRes handBmp();
	abstract BmpRes legBmp();
	public void defaultDrawHuman(Canvas cv){
		float a=(float)sin(walk_state)*25f;
		float w=(float)width(),h=(float)height();
		cv.save();{
			if(dir==-1)cv.scale(-1,1);
			
			Shoes sh=shoes.get();
			
			cv.save();{
				cv.rotate(a);
				legBmp().drawR(cv,-w,-h,w,0);
				if(sh!=null&&sh.getCnt()==2)sh.getBmp().drawR(cv,-w,-h,w,0);
				cv.rotate(-a*2);
				legBmp().drawR(cv,-w,-h,w,0);
				if(sh!=null&&sh.getCnt()==2)sh.getBmp().drawR(cv,-w,-h,w,0);
			}cv.restore();
			
			float b=(float)des_state,c=30-b/9;
			b+=c;
			
			cv.save();{
				cv.rotate(c,w*0f,h*0.62f);
				handBmp().drawR(cv,w*-0.55f,-h*0.1f,w*-0.05f,h*0.55f);
				cv.rotate(-b,-0.05f,0);
				Item i=items.get();
				if(i!=null){
					cv.save();
					cv.translate(0.1f,0.15f);
					if(!i.xRev()&&dir==-1)cv.scale(-1,1);
					if(i instanceof LongItem){
						LongItem li=(LongItem)i;
						li.item2.getBmp().draw(cv,0.3f,0.3f,0.3f,0.3f);
						li.item1.getBmp().draw(cv,0,0,0.3f,0.3f);
					}else{
						i.getBmp().draw(cv,0,0,0.3f,0.3f);
					}
					cv.restore();
				}
			}cv.restore();

			bodyBmp().drawR(cv,-w,-h*0.1f,w,h);
			Armor ar=armor.get();
			if(ar!=null){
				BmpRes bmp=ar.getBmp();
				if(bmp!=null)bmp.drawR(cv,-w,-h*0.1f,w*1.1f,h*0.9f);
			}
			
			if(sh!=null){
				if(sh.getCnt()==1){
					float w1=(float)sh.width();
					float h1=(float)sh.height();
					sh.getBmp().drawR(cv,-w1,-h,w1,h1*2-h);
				}
			}
		}cv.restore();
	}
	public void draw(Canvas cv){
		float w=(float)width(),h=(float)height();
		
		Armor ar=armor.get();
		if(ar!=null)ar.draw(cv,this);
		else defaultDrawHuman(cv);
		
		super.draw(cv);
		if(name!=null&&(World.cur.setting.show_human_name)){
			cv.save();{
				float k=World.cur.setting.BW/12f;
				cv.translate(0,h+0.4f*k);
				cv.scale(k,-k);
				cv.drawText(name+":"+kill_cnt,0,0,0.4f,0);
			}cv.restore();
		}
	}

	public Craft cur_craft=null;
	public long craft_ft=0;

	public float getCraftProcess(){
		if(cur_craft==null)return -1;
		return 1-(craft_ft-World.cur.time)*1f/cur_craft.cost.time;
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
		if(World.cur.getMode() instanceof ECPvPMode)World.showText("***"+getName()+"开始合成"+c.out[0].get().getName());
		cur_craft=c;
		craft_ft=World.cur.time+c.cost.time;
		return true;
	}
	public void stopCraft(){
		if(cur_craft!=null){
			cur_craft.interrupt(this);
			cur_craft=null;
		}
	}
	
	@Override
	public double gA(){
		return super.gA();
	}

	@Override
	public void pick(DroppedItem item){
		if(pickup_state==0){
			getItems().insert(item.item);
			if(!item.item.isEmpty()){
				for(SingleItem si:bag_items.toArray()){
					if(!si.isEmpty())si.insert(item.item);
				}
			}
		}
		if(pickup_state==1)items.getSelected().insert(item.item);
	}

	@Override
	public void onKilled(Source src){
		Armor ar=armor.get();
		if(ar instanceof EnergyArmor||name!=null){
			World.showText(">>> "+getName()+"死于"+src);
			Agent a=src.getSrc();
			if(a instanceof Human&&a!=this){
				Human w=(Human)a;
				World.showText(">>> "+w.getName()+"击杀了"+(++w.kill_cnt)+"人");
			}
		}
		super.onKilled(src);
	}
	public boolean selectItem(Class type,boolean subclass){
		for(SingleItem si:items.toArray()){
			Item it=si.get();
			if(it==null)continue;
			Class c=it.getClass();
			if(subclass?(type.isAssignableFrom(c)):(type==c)){
				items.select(si);
				return true;
			}
		}
		return false;
	}
	
	protected void onGenFragment(){
		double w=0.29,h=0.95;
		Fragment.gen(x+dir*w*0.3,y+h*0.325,w*0.25,h*0.325,1,2,2,handBmp());
		Fragment.gen(x,y+h*0.45,w,h*0.55,2,3,6,bodyBmp());
		Fragment.gen(x,y-h*0.5,w,h*0.5,1,2,2,legBmp());
	}

	@Override
	void onKill(){
		onGenFragment();
		super.onKill();
	}
	
	private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException{ 
		in.defaultReadObject();
		if(shoes==null)shoes=new SpecialItem<Shoes>(Shoes.class);
	}
	@Override
	public double maxv(){
		double v=super.maxv();
		Shoes sh=shoes.get();
		if(sh!=null)v*=sh.maxvr();
		Armor ar=armor.get();
		if(ar!=null)v*=ar.maxvr();
		return v;
	}
}

