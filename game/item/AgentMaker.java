package game.item;

import util.BmpRes;
import game.world.World;
import game.block.Block;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

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
		return "点击可生成"+getName();
		//return util.AssetLoader.loadString(type,"doc");
	}
	public boolean useInArmor(){return true;}

	@Override
	public boolean cmpType(Item it){
		if(it.getClass()==getClass())return type==((AgentMaker)it).type;
		return false;
	}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		a.throwEntAtPos(asEnt(),dir,x,y,slope,mv2);
	}
	@Override
	public boolean autoUse(final Human h,final Agent a){
		if(max(abs(h.x-a.x),abs(h.y-a.y))<4){
			h.clickAt(a.x+rnd_gaussion()*0.1,a.y+rnd_gaussion()*0.1);
			return true;
		}
		return false;
	}
	@Override
	public Agent asEnt(){
		try{
			return (Agent)type.getConstructor(double.class,double.class).newInstance(0,0);
		}catch(Exception e){}
		return null;
	}
	
	public Item clickAt(double x,double y,Agent a){
		Agent agent=asEnt();
		agent.initPos(x,y,0,0,SourceTool.place(a));
		if(agent.cadd())return null;
		return this;
	}
	
	public BmpRes getBmp(){
		return new BmpRes("Entity/"+type.getSimpleName());
	}
};