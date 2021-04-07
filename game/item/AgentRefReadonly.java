package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.ui.*;
import static util.MathUtil.*;

public class AgentRefReadonly extends EnergyTool implements CamaraController{
	private static final long serialVersionUID=1844677L;
	public Agent agent;
	public void onDesBlock(game.block.Block b){}
	public AgentRefReadonly(){
		this(null);
	}
	public AgentRefReadonly(Agent agent){
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
	
	public int maxDamage(){return 200;}

	@Override
	public void onCarried(Agent a){
		if(agent==null)return;
		if(!agent.active()){
			agent=null;
			return;
		}
		
		int c=rf2i((a.distL1(agent)*5+5+agent.hp)*0.0001);
		if(!hasEnergy(1+c))agent=null;
		loseEnergy(c);
	}

	@Override
	public boolean useInArmor(){return true;}
	@Override
	public Item clickAt(double x,double y,Agent a0){
		for(Agent a:World.cur.getNearby(x,y,0.2,0.2,false,false,true).agents){
			int c=rf2i(a0.distL1(a)*5+5+a.hp);
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
