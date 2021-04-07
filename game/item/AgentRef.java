package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.ui.*;
import static util.MathUtil.*;

public class AgentRef extends EnergyTool implements CamaraController{
	private static final long serialVersionUID=1844677L;
	public Agent agent;
	public void onDesBlock(game.block.Block b){}
	public AgentRef(){
		this(null);
	}
	public AgentRef(Agent agent){
		this.agent=agent;
	}
	public String getName(){
		if(agent==null)return super.getName();
		return util.AssetLoader.loadString(agent.getClass(),"name");
	}
	public String getDoc(){
		if(agent==null)return super.getDoc();
		return "(x,y): ("+f2i(agent.x)+","+f2i(agent.y)+")\nhp: "+f2i(agent.hp)+"/"+f2i(agent.maxHp());
	}
	
	public void onUse(Human a){
		if(agent==null){
			super.onUse(a);
			return;
		}
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			UI_MultiPage ui=new UI_MultiPage(){
				private static final long serialVersionUID=1844677L;
				public boolean exists(){
					if(pl.getCarriedItem().get()==AgentRef.this)return AgentRef.this.agent!=null;
					return false;
				}
			};
			agent.initUI(ui);
			ui.addPage(new BlueCrystalEnergyCell(),new UI_ItemList(getItems(),((Player)a).il));
			((Player)a).openDialog(ui);
		}
	}
	
	public int maxDamage(){return 100;}

	@Override
	public void onCarried(Agent a){
		if(agent==null)return;
		if(agent.removed){
			agent=null;
			return;
		}
		/*if(!agent.is_ctrled){
			agent.is_ctrled=true;
			a.is_ctrled=true;
			//System.out.println(agent.xdir+","+agent.ydir+"   "a.xdir+","+a.ydir);
			agent.xdir=a.xdir;
			agent.ydir=a.ydir;
		}*/
		a.xdir=a.ydir=0;
		
		int c=rf2i((a.distL1(agent)*5+5+agent.hp)*0.0002);
		if(!hasEnergy(1+c))agent=null;
		loseEnergy(c);
	}
	
	@Override
	public boolean onLongPress(Agent a0,double x,double y){
		if(agent!=null){
			agent.setDes(agent.x+(x-a0.x),agent.y+(y-a0.y));
			return true;
		}
		return false;
	}
	@Override
	public boolean useInArmor(){return true;}
	@Override
	public Item clickAt(double x,double y,Agent a0){
		if(agent!=null){
			agent.clickAt(agent.x+(x-a0.x),agent.y+(y-a0.y));
			++damage;
			return this;
		}
		for(Agent a:World.cur.getNearby(x,y,0.2,0.2,false,false,true).agents){
			int c=rf2i(a0.distL1(a)*5+5+a.hp);
			if(a instanceof Player)continue;
			if(a instanceof Zombie)continue;
			if(hasEnergy(c)){
				loseEnergy(c);
				agent=a;
				++damage;
			}
			return this;
		}
		return this;
	}
	
	public void drawInfo(Canvas cv){
		if(agent==null)super.drawInfo(cv);
		else{
			game.ui.UI.drawProgressBar(cv,0xffff0000,0xff7f0000,(float)(agent.hp/agent.maxHp()),-0.4f,-0.4f,0.4f,-0.33f);
		}
	}

	public BmpRes getBmp(){
		if(agent==null)return super.getBmp();
		return new BmpRes("Entity/"+agent.getClass().getSimpleName());
	}
	
	@Override
	public double[] getCamaraPos(Agent a){
		if(agent==null)return new double[]{a.x,a.y};
		return new double[]{agent.x,agent.y};
	}
};
