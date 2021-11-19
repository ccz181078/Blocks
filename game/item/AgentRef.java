package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.ui.*;
import static util.MathUtil.*;

abstract class BaseAgentRef extends EnergyTool implements CamaraController{
	protected abstract Agent getAgent();
	protected abstract void setAgent(Agent a);
	public boolean disableRecover(){return true;}
	public String getName(){
		Agent agent=getAgent();
		if(agent==null)return super.getName();
		return util.AssetLoader.loadString(agent.getClass(),"name");
	}
	public String getDoc(){
		Agent agent=getAgent();
		if(agent==null)return super.getDoc();
		return "(x,y): ("+f2i(agent.x)+","+f2i(agent.y)+")\nhp: "+f2i(agent.hp)+"/"+f2i(agent.maxHp());
	}
	public void drawInfo(Canvas cv){
		Agent agent=getAgent();
		if(agent==null)super.drawInfo(cv);
		else{
			game.ui.UI.drawProgressBar(cv,0xffff0000,0xff7f0000,(float)(agent.hp/agent.maxHp()),-0.4f,-0.4f,0.4f,-0.33f);
		}
	}
	@Override
	public void onCarried(Agent a){
		Agent agent=getAgent();
		if(agent==null)return;
		if(!agent.active()){
			setAgent(null);
			return;
		}
		
		int c=rf2i((a.distL1(agent)*5+5+agent.hp)*0.0001);
		if(!hasEnergy(1+c))setAgent(null);
		loseEnergy(c);
	}


	public BmpRes getBmp(){
		Agent agent=getAgent();
		if(agent==null)return super.getBmp();
		return agent.getCtrlBmp();//new BmpRes("Entity/"+agent.getClass().getSimpleName());
	}
	
	@Override
	public double[] getCamaraPos(Agent a){
		Agent agent=getAgent();
		if(agent==null)return new double[]{a.x,a.y};
		return new double[]{agent.x,agent.y};
	}
	public void onDesBlock(game.block.Block b){}
}

public class AgentRef extends BaseAgentRef{
	public long agentref;
	transient long last_press_time=0;
	protected Agent getAgent(){
		return World.getAgentRef(agentref);
	}
	protected void setAgent(Agent a){
		agentref=World.getAgentRef(a);
	}
	public AgentRef(){
		this(null);
	}
	public AgentRef(Agent agent){
		if(agent!=null)setAgent(agent);
	}
	
	@Override
	public void onUse(Human a){
		Agent agent=getAgent();
		if(agent==null){
			super.onUse(a);
			return;
		}
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			UI_MultiPage ui=new UI_MultiPage(){
				private static final long serialVersionUID=1844677L;
				public boolean exists(){
					if(pl.getCarriedItem().get()==AgentRef.this)return getAgent()!=null;
					return false;
				}
			};
			agent.initUI(ui);
			ui.addPage(new BlueCrystalEnergyCell(),new UI_ItemList(getItems(),((Player)a).il));
			((Player)a).openDialog(ui);
		}
	}
	public BmpRes getUseBmp(){return Item.use_btn;}
	
	public int maxDamage(){return 100;}
	@Override
	public boolean onLongPress(Agent a0,double x,double y){
		Agent agent=getAgent();
		if(agent!=null){
			last_press_time=World.cur.time;
			agent.setDes(agent.x+(x-a0.x),agent.y+(y-a0.y));
			return true;
		}
		return false;
	}
	@Override
	public void onCarried(Agent a){
		super.onCarried(a);
		Agent agent=getAgent();
		if(agent!=null&&last_press_time<World.cur.time-1){
			agent.cancelDes();
		}
	}
	@Override
	public boolean useInArmor(){return true;}
	@Override
	public Item clickAt(double x,double y,Agent a0){
		Agent agent=getAgent();
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
				setAgent(a);
				++damage;
			}
			return this;
		}
		return this;
	}
	
};
