package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.ui.*;
import static util.MathUtil.*;

public class AgentRefReadonly extends BaseAgentRef{
	private static final long serialVersionUID=1844677L;
	public long agentref;
	protected Agent getAgent(){
		return World.getAgentRef(agentref);
	}
	protected void setAgent(Agent a){
		agentref=World.getAgentRef(a);
	}
	public AgentRefReadonly(){
		this(null);
	}
	public AgentRefReadonly(Agent agent){
		if(agent!=null)setAgent(agent);
	}
	
	public int maxDamage(){return 200;}

	@Override
	public boolean useInArmor(){return true;}
	@Override
	public Item clickAt(double x,double y,Agent a0){
		Agent agent=getAgent();
		for(Agent a:World.cur.getNearby(x,y,0.2,0.2,false,false,true).agents){
			int c=rf2i(a0.distL1(a)*5+5+a.hp);
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

