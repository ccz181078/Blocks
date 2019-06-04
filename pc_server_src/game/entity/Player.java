package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import game.ui.*;
import game.block.BlockAt;
import game.world.World;
import graphics.Canvas;
import util.BmpRes;
import game.world.Weather;
import game.block.AirBlock;
import game.block.AutoCraftBlock;

public class Player extends Human{
	private static final long serialVersionUID=1844677L;
	private double rs_x,rs_y;
	transient public Action action;
	public boolean batch_op=false;
	transient public int fail_to_place_block=0,click_out_of_range=0;
	transient public UI_ItemList il;
	transient private UI_Group ui;
	transient private UI_Info info;
	transient public boolean send_text=false;
	transient private UI dialog=null;
	transient public game.GameSetting game_setting=null;
	public boolean creative_mode=false,suspend_mode=false;
	public boolean isDead(){return removed;}
	static BmpRes
	body=new BmpRes("Entity/Player/body"),
	hand=new BmpRes("Entity/Player/hand"),
	leg=new BmpRes("Entity/Player/leg");

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	
	public boolean chkBlock(){return !(creative_mode&&suspend_mode);}
	public boolean chkRigidBody(){return !(creative_mode&&suspend_mode);}
	double gA(){return suspend_mode?0:super.gA();}
	
	public Player(double _x,double _y){
		super(_x,_y);
		resetUI();
		stopCraft();
	}

	public AttackFilter getAttackFilter(){
		return new AttackFilter(){public Attack transform(Attack a){
			if(a!=null)a=energy_filter.transform(a);
			return Player.super.getAttackFilter().transform(a);
		}};
	}
	

	private static BmpRes
		bag_btn=new BmpRes("UI/bag"),
		cancel_btn=new BmpRes("UI/cancel"),
		work_btn=new BmpRes("UI/work"),
		throw_btn=new BmpRes("UI/throw"),
		batch_btn[]=BmpRes.load("UI/batch_",2),
		suspend_btn[]=BmpRes.load("UI/suspend_",2);
		
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
				ui.addPage(new IronArmor(),new UI_ItemList(armor,il));
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
				if(closeDialog())return;
				if(creative_mode)openDialog(new UI_CreativeMode());
				else openDialog(new UI_Craft(Craft.getAll(0),-7));
			}
		});
		ui.addChild(new UI_Button(-3,6){
			protected BmpRes getBmp(){return throw_btn;}
			protected void onPress(){throwCarriedItem(batch_op);}
		});
		ui.addChild(new UI_Button(-3,5){
			protected BmpRes getBmp(){
				Item w=items.getSelected().get();
				if(w!=null)return w.getUseBmp();
				return Item.talk_btn;
			}
			protected void onPress(){
				Item it=items.popItem();
				if(it!=null)it.onUse(Player.this);
				else{
					send_text=true;
				}
			}
		});
		ui.addChild(new UI_Button(-3,4){
			protected BmpRes getBmp(){return batch_btn[batch_op?1:0];}
			protected void onPress(){batch_op=!batch_op;}
		});
		ui.addChild(new UI_Button(-3,3){
			protected BmpRes getBmp(){return suspend_btn[suspend_mode?1:0];}
			public boolean exist(){return creative_mode;}
			protected void onPress(){suspend_mode=!suspend_mode;}
		});
		info=new UI_Info(this);
		action=new Action(1,1);
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

	public void clickAt(double tx,double ty){
		if(abs(tx-x)<4&&abs(ty-y)<4)super.clickAt(tx,ty);
		else click_out_of_range=10;
	}
	
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
		if(send_text){
			cv.sendText();
			send_text=false;
		}
	}
	public void setCursorState(boolean on,float tx,float ty){
		action.on=on;
		action.tx=tx;
		action.ty=ty;
	}
	public void drawRightUI(Canvas cv){
		name=game_setting.player_name;
		ui.draw(cv);
		if(dialog!=null)dialog.draw(cv);
	}
	public boolean UI_pressAt(float x,float y){
		if(dialog!=null){
			if(!dialog.exist())closeDialog();
			else if(dialog.press(x,y))return true;
		}
		return ui.press(x,y);
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
	transient int press_t;
	public void drawTip(Canvas cv){
		if(cv.gs.tip_click_range&&click_out_of_range>0){
			cv.drawRect(-4,-4,4,4,0x2fffffff);
		}
		if(game_setting.show_cursor){
			if(action.on)press_t=255;
			else press_t=max(0,press_t-30);
			float tx=action.tx,ty=action.ty;
			cv.drawLines(new float[]{tx-0.3f,ty,tx+0.3f,ty,tx,ty-0.3f,tx,ty+0.3f},0xff|press_t<<24);
		}
	}

	public void action(){
		super.action();
		if(creative_mode&&suspend_mode){
			xv=xdir*0.2;
			yv=ydir*0.2;
			if(xdir!=0)dir=xdir;
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
	
	public void update(){
		UI.pl=this;
		super.update();
		fail_to_place_block=max(0,fail_to_place_block-1);
		click_out_of_range=max(0,click_out_of_range-1);
		if(World.cur.weather==Weather._fire)hp-=0.003;
		if(World.cur.weather==Weather._energystone)hp+=0.003;
		if(World.cur.weather==Weather._plant)hp+=0.01;
		if(World.cur.weather==Weather._blood)hp+=0.03;
		hp=min(hp,maxHp());
		if(dialog!=null){
			if(!dialog.exist()){
				dialog.close();
				dialog=null;
			}else dialog.update();
		}
		action.apply(this);
		if(creative_mode){
			if(hp<=0)hp=maxHp();
			xp=min(xp+0.5,maxXp());
		}
		if(hp<=0){
			stopCraft();
			closeDialog();
			dropItems();
			initItems();
			resetUI();
			x=rs_x;y=rs_y;
			xv=yv=0;
			hp=maxHp();
		}
	}
};
