package game.item;

import util.BmpRes;
import game.entity.Agent;
import game.world.World;
import game.block.Block;

public class AgentMaker extends Item{
	private static final long serialVersionUID=1844677L;
	Class type;
	public AgentMaker(Class _type){
		type=_type;
	}
	public String getName(){
		return util.AssetLoader.loadString(type,"name");
	}
	public String getDoc(){
		return util.AssetLoader.loadString(type,"doc");
	}

	@Override
	boolean cmpType(Item it){
		if(it.getClass()==getClass())return type==((AgentMaker)it).type;
		return false;
	}
	
	
	public Item clickAt(double x,double y,Agent a){
		try{
			Agent agent=(Agent)type.getConstructor(double.class,double.class).newInstance(x,y);
			if(agent.cadd())return null;
		}catch(Exception e){e.printStackTrace();}
		return this;
	}
	
	public BmpRes getBmp(){
		return new BmpRes("Entity/"+type.getSimpleName());
	}
};
