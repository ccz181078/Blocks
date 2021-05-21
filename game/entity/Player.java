package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;

import game.item.*;
import game.ui.*;
import game.ui.Action;
import game.world.World;
import game.world.ECPvPMode;
import graphics.Canvas;
import util.BmpRes;
import game.world.Weather;
import game.block.*;

public class Player extends Human{
	private static final long serialVersionUID=1844677L;
	private double rs_x,rs_y,dead_x,dead_y;
	public int death_cnt=0;
	public int air_rate=100;
	transient public Action action=null;
	public boolean batch_op=true,online=true;
	transient public int fail_to_place_block=0,click_out_of_range=0;
	transient public UI_ItemList il;
	transient private UI_Group ui;
	transient private UI_Info info;
	transient private UI dialog=null;
	transient public UI last_pressed_ui=null;
	transient public game.GameSetting game_setting=null;
	public boolean creative_mode=false,suspend_mode=false;
	public boolean isDead(){return removed;}
	long last_active_time=0;
	public boolean active(){
		if(super.active()&&(online||World.cur.getMode().forceOnline()&&last_active_time+30*60>World.cur.time)){
			if(online)last_active_time=World.cur.time;
			return true;
		}
		return false;
	}
	static BmpRes
	body=new BmpRes("Entity/Player/body"),
	hand=new BmpRes("Entity/Player/hand"),
	leg=new BmpRes("Entity/Player/leg");

	BmpRes bodyBmp(){return World.cur.getMode() instanceof ECPvPMode?Zombie.body:body;}
	BmpRes handBmp(){return World.cur.getMode() instanceof ECPvPMode?Zombie.hand:hand;}
	BmpRes legBmp(){return World.cur.getMode() instanceof ECPvPMode?Zombie.leg:leg;}
	Group group(){return Group.PLAYER;}
	
	public boolean chkBlock(){return !(creative_mode&&suspend_mode);}
	public boolean chkRigidBody(){return !(creative_mode&&suspend_mode);}
	public double gA(){return suspend_mode?0:super.gA();}
	
	public float getRotation(){
		if(armor.get()!=null)return armor.get().getRotation();
		return 0;
	}

	@Override
	public boolean clickable(double tx,double ty){
		if(creative_mode)return true;
		return super.clickable(tx,ty);
	}
	/*@Override
	public boolean isRemoved(){
		return !chkEnt();
	}
		if(respawn_time>0){
			respawn_time-=1;
			xv=yv=0;f=1;
			hp=maxHp();
			xp=maxXp();
			if(respawn_time<=0){
				stopCraft();
				closeDialog();
				dropItems();
				initItems();
				resetUI();
				x=rs_x;y=rs_y;
				air_rate=100;
				World.cur.getMode().onPlayerRespawn(this);
			}
			return true;
		}
		return super.isRemoved();
	}*/
	transient Source atk_tip_src=null;
	double atk_tip_sum=0;
	public void onAttackedTip(Source src,double weight){
		atk_tip_sum+=weight;
		if(rnd(atk_tip_sum)<weight)atk_tip_src=src;
	}
	
	public Player(double _x,double _y){
		super(_x,_y);
		resetUI();
		stopCraft();
	}

	public AttackFilter getAttackFilter(){
		return new AttackFilter(){public Attack transform(Attack a){
			if(!chkEnt())return null;
			if(a!=null)a=Player.super.getAttackFilter().transform(a);
			if(a!=null)a=energy_filter.transform(a);
			return a;
		}};
	}
	

	public static BmpRes
		bag_btn=new BmpRes("UI/bag"),
		cancel_btn=new BmpRes("UI/cancel"),
		work_btn=new BmpRes("UI/work"),
		throw_btn=new BmpRes("UI/throw"),
		pick_btn[]=BmpRes.load("UI/pick_",3),
		batch_btn[]=BmpRes.load("UI/batch_",2),
		suspend_btn[]=BmpRes.load("UI/suspend_",2);
		
	public void onKey(char c){
		switch(c){
			case 'Q':{
				Agent a=getControlledAgent();
				if(a!=Player.this&&dialog==null)a=Player.this;
				if(a instanceof Human)((Human)a).useCarriedItem();
				return;
			}
			case 'E':{
				getControlledAgent().throwCarriedItem(batch_op,0);
				return;
			}
			case 'Z':{
				if(closeDialog())return;
				if(creative_mode)openDialog(new UI_CreativeMode());
				else if(World.cur.getMode() instanceof game.world.PvPMode)openDialog(new UI_PvPMode());
				else openDialog(new UI_Craft(Craft.getAll(0),-7));
				return;
			}
			case 'X':{
				pickup_state=(pickup_state+1)%3;
				return;
			}
			case 'C':{
				batch_op=!batch_op;
				return;
			}
			case 'V':{
				if(creative_mode)suspend_mode=!suspend_mode;
				return;
			}
		}
	}
	
	public void resetUI(){
		closeDialog();
		il=new UI_ItemList(-2,0,2,8,items,null);
		ui=new UI_Group(0,0);
		
		ui.addChild(il);
		ui.addChild(new UI_Button(-1,7){
			protected BmpRes getBmp(){return bag_btn;}
			protected void onPress(){
				UI_MultiPage ui=new UI_MultiPage();
				ui.setBgColor(0xffaaaaaa);
				ui.addPage(new AirBlock(),new UI_ItemList(bag_items,il));
				ui.addPage(new IronArmor(),new UI_ItemList(ItemList.create(armor,shoes),il));
				openDialog(ui);
			}
			protected void onDraw(Canvas cv){
				cv.save();
				cv.translate(.5f,.5f);
				cv.drawItemFrame(false);
				cv.restore();
				super.onDraw(cv);
			}
		});
		ui.addChild(new UI_Button(-3,7){
			protected BmpRes getBmp(){return dialog==null?work_btn:cancel_btn;}
			protected void onPress(){
				onKey('Z');
			}
		});
		ui.addChild(new UI_Button(-3,0){
			protected BmpRes getBmp(){return pick_btn[pickup_state];}
			protected void onPress(){
				onKey('X');
			}
		});
		ui.addChild(new UI_Button(-3,1){
			boolean pressed=false;
			protected BmpRes getBmp(){return Item.talk_btn;}
			@Override
			protected void onPress() {
				pressed=true;
			}
			@Override
			protected void onDraw(Canvas cv){
				if(pressed){
					pressed=false;
					cv.sendText();
				}
				super.onDraw(cv);
			}
		});
		ui.addChild(new UI_Button(-3,6){
			long t0;
			protected BmpRes getBmp(){return throw_btn;}
			protected void onPress(){
				t0=World.cur.time;
			}
			protected void onRelease(){
				if(pl.last_pressed_ui!=this)return;
				long t=World.cur.time-t0;
				getControlledAgent().throwCarriedItem(batch_op,t);
			}
		});
		ui.addChild(new UI_Button(-3,5){
			protected BmpRes getBmp(){
				Agent a=getControlledAgent();
				if(a!=Player.this&&dialog==null)a=Player.this;
				if(a instanceof Human){
					Item w=((Human)a).getCarriedItem().get();
					if(w!=null)return w.getUseBmp();
				}
				return Item.empty_btn;
			}
			protected void onPress(){
				onKey('Q');
			}
		});
		ui.addChild(new UI_Button(-3,4){
			protected BmpRes getBmp(){return batch_btn[batch_op?1:0];}
			protected void onPress(){onKey('C');}
		});
		ui.addChild(new UI_Button(-3,3){
			protected BmpRes getBmp(){return suspend_btn[suspend_mode?1:0];}
			public boolean exist(){return creative_mode;}
			protected void onPress(){onKey('V');}
		});
		info=new UI_Info(this);
		action=new Action(1,1);
		last_pressed_ui=null;
	}

	
	public boolean check(BlockAt ba){
		return ba.exist()
		&&abs(ba.x+0.5-x)<4
		&&abs(ba.y+0.5-y)<4;
	}

	public boolean destroyable(int dx,int dy){
		if(creative_mode&&suspend_mode)return true;
		return super.destroyable(dx,dy);
	}

	/*public void clickAt(double tx,double ty){
		if(abs(tx-x)<4&&abs(ty-y)<4)super.clickAt(tx,ty);
		else click_out_of_range=10;
	}*/
	
	public boolean closeDialog(){
		if(dialog!=null){
			dialog.close();
			dialog=null;
			return true;
		}
		return false;
	}
	public void openDialog(UI ui){
		closeDialog();
		dialog=ui;
		dialog.open();
	}
	public void drawLeftUI(Canvas cv){
		info.draw(cv);
	}
	public void setCursorState(boolean on,float tx,float ty,long press_time){
		action.on=on;
		action.tx=tx;
		action.ty=ty;
		Item cur=getCarriedItem().get();
		if(cur!=null)cur.setCursorState(this,on,x+tx,y+ty,press_time);
		if(on&&press_time>12)setDes(x+tx,y+ty);
		else cancelDes();
	}
	public void drawRightUI(Canvas cv){
		name=game_setting.player_name;
		ui.draw(cv);
		if(dialog!=null)dialog.draw(cv);
	}
	public boolean UI_pressAt(float x,float y,int tp){
		if(dialog!=null){
			if(!dialog.exist())closeDialog();
			else if(dialog.touch(x,y,tp)){
				if(tp==1)last_pressed_ui=null;
				return true;
			}
		}
		if(ui.touch(x,y,tp)){
			if(tp==1)last_pressed_ui=null;
			return true;
		}
		if(last_pressed_ui!=null&&tp==2)return true;
		last_pressed_ui=null;
		return false;
	}
	public void setRespawnPoint(){
		rs_x=x;
		rs_y=y;
	}
	public void showTextIfFree(String str){
		info.showTextIfFree(str);
	}
	public void showText(String str){
		info.showText(str);
	}
	public void addText(String str){
		info.addText(str);
	}
	public transient int press_t;
	public void drawTip(Canvas cv){
		if(cv.gs.tip_click_range&&click_out_of_range>0){
			cv.drawRect(-4,-4,4,4,0x2fffffff);
		}
		if(game_setting.show_cursor){
			if(action.on)press_t=255;
			else press_t=max(0,press_t-30);
			float tx=action.tx,ty=action.ty;
			cv.drawLines(new float[]{tx-0.3f,ty,tx+0.3f,ty,tx,ty-0.3f,tx,ty+0.3f},0xff|press_t<<24);
			
			Item w=items.popItem();
			if(w!=null)w.drawTip(cv,this);
			items.getSelected().insert(w);
			
		}
	}
	public void kill(Source src){
		if(!removed){
			loseHp(hp,src);
		}
	}
	public double suspend_speed=1;
	public void action(){
		super.action();
		if(creative_mode&&suspend_mode){
			xv=xdir*(1-suspend_speed);
			yv=ydir*(1-suspend_speed);
			xa=ya=0;
			if(xdir==0&&ydir==0)suspend_speed=1;
			else suspend_speed*=0.95;
			if(xdir!=0)dir=xdir;
		}else{
			suspend_mode=false;
		}
	}
	
	public float getCraftProcess(){
		if(dialog!=null){
			CraftHelper ch=dialog.getCraftHelper();
			if(ch!=null)return ch.getCraftProcess();
		}
		return super.getCraftProcess();
	}

	public int getCraftType(){
		if(dialog!=null)return dialog.getCraftType();
		return super.getCraftType();
	}
	public int getEnergy(){
		if(dialog!=null){
			CraftHelper ch=dialog.getCraftHelper();
			if(ch!=null)return ch.getEnergy();
		}
		return super.getEnergy();
	}
	public void loseEnergy(int v){
		if(dialog!=null){
			CraftHelper ch=dialog.getCraftHelper();
			if(ch!=null){
				ch.loseEnergy(v);
				return;
			}
		}
		super.loseEnergy(v);
	}
	public boolean startCraft(Craft c){
		if(dialog!=null){
			CraftHelper ch=dialog.getCraftHelper();
			if(ch!=null&&ch.startCraft(c)){
				AutoCraftBlock.check(dialog.getBlock(),c);
				return true;
			}
		}
		if(super.startCraft(c)){
			AutoCraftBlock.check(dialog.getBlock(),c);
			return true;
		}
		return false;
	}

	public boolean chkRemove(long t){return false;}
	
	public int respawn_time=0;
	
	public void update(){
		UI.pl=this;
		super.update();
		
		if(xdir!=0)dir=xdir;
		
		if((atk_tip_sum>10||rnd()<0.03)&&atk_tip_src!=null){
			if(atk_tip_sum>1){
				String tip="|-- 你受到 "+atk_tip_src+" 的伤害";
				addText(tip);
				System.out.println(getName()+" "+tip);
			}
			atk_tip_sum=0;
			atk_tip_src=null;
		}
		
		fail_to_place_block=max(0,fail_to_place_block-1);
		click_out_of_range=max(0,click_out_of_range-1);
		if(World.cur.weather==Weather._fire)loseHp(0.003,SourceTool.FIRE_WEATHER);
		if(hp>0&&chkEnt()){
			Block b=World.cur.get(x,y+height()-0.1).rootBlock();
			double c=0.9-b.transparency();
			if(b.isSolid()||b instanceof LiquidType)c=min(c,-0.4);
			air_rate=max(0,min(100,air_rate+rf2i(c*0.15)));
			if(air_rate<20){
				loseHp((20-air_rate)*0.05,SourceTool.NO_AIR);
			}
		}
		hp=min(hp,maxHp());
		if(dialog!=null){
			if(!dialog.exist()){
				dialog.close();
				dialog=null;
			}else dialog.update();
		}
		if(creative_mode){
			if(hp<=0){
				last_hp+=maxHp()-hp;
				hp=maxHp();
			}
			xp=min(xp+0.5,maxXp());
		}
		if(respawn_time>0)--respawn_time;
		if(hp<=0&&respawn_time<=0){
			stopCraft();
			closeDialog();
			dead_x=x;dead_y=y;
			respawn_time=150;
			xdir=ydir=0;
			hp=maxHp();
			xp=maxXp();
			air_rate=100;
			onGenFragment();
			World.cur.getMode().onPlayerDead(this);
			xv=yv=0;f=1;
			
			new SetRelPos(this,null,rs_x,rs_y);
			resetUI();
			World.cur.getMode().onPlayerRespawn(this);
		}
	}
	
	public void ai(){
		if(!online)return;
		if(respawn_time<=0&&action!=null)action.apply(this);
	}
	
	public double[] getCamaraPos(){
		if(respawn_time>0)return new double[]{dead_x,dead_y};
		if(locked){
			game.world.ECPvPMode mode=(game.world.ECPvPMode)World.cur.getMode();
			return new double[]{mode.airship.x,mode.airship.y};
		}
		Item cur=items.getSelected().get();
		if(cur instanceof CamaraController){
			return ((CamaraController)cur).getCamaraPos(this);
		}
		return new double[]{x,y};
	}

	@Override
	public void onKilled(Source src){
		if(!creative_mode)super.onKilled(src);
	}

	/*@Override
	public void onKillAgent(Agent a){
		super.onKillAgent(a);
		if(a instanceof Player){
			if(((Player)a).creative_mode)return;
			World.showText(">>> "+name+" has killed "+(++kill_cnt)+" player(s)");
		}
	}*/
	
	@Override
	public void draw(Canvas cv){
		if(respawn_time>0&&respawn_time%8<4)return;
		super.draw(cv);
	}

	@Override
	public boolean chkEnt(){return respawn_time<=0&&!suspend_mode;}
	@Override
	public boolean chkAgent(){return chkEnt();}
	@Override
	public double RPG_ExplodeProb(){return chkEnt()?super.RPG_ExplodeProb():0;}
	
	public Agent getControlledAgent(){
		Item item=items.getSelected().get();
		if(item instanceof AgentRef){
			AgentRef ref=(AgentRef)item;
			if(ref.agent!=null)return ref.agent;
		}
		return this;
	}
};
