package game.entity;

import game.block.Block;

public class SourceTool{
private static final long serialVersionUID=1844677L;
	public static Source item(game.item.Item it){
		return item(null,it);
	}
	public static Source item(Source src,game.item.Item it){
		if(src!=null)return make(src,"的"+it);
		return make(null,it.getName());
	}
	public static Source armor(Source src,game.item.Armor armor){
		return item(src,armor);
	}
	public static Source shoes(Source src,game.item.Shoes shoes){
		return item(src,shoes);
	}
	private static Source make(Source src,String verb){
		return new SourceInfo(src,verb);
	}
	public static Source useToLaunch(Source src,String name){
		return make(src,"使用"+name+"发射的");
	}
	public static Source launch(Source src){
		return make(src,"发射的");
	}
	public static Source launch(Source src,String name){
		return make(src,"发射的"+name);
	}
	public static Source blood(Source src){
		return make(src,"的吸血效果");
	}
	public static Source impact(Source src){
		return make(src,"的撞击");
	}
	public static Source set(Source src){
		return make(src,"设置的");
	}
	public static Source dropOnDead(Source src){
		return make(src,"死后掉落的");
	}
	public static Source hold(Source src){
		return make(src,"手持的");
	}
	public static Source carry(Source src){
		return make(src,"携带的");
	}
	public static Source place(Source src){
		return make(src,"放置的");
	}
	public static Source use(Source src,String name){
		return make(src,"使用"+name);
	}
	public static Source hitFromHand(Source src,String name){
		return make(src,"从"+name+"手中击飞的");
	}
	public static Source block(int x,int y,Block b){
		return block(x,y,b,"");
	}
	public static Source block(int x,int y,Block b,String verb){
		if(b.src!=null)return make(b.src,"放置的"+b.getName()+verb);
		return make(null,b.getName()+verb);
	}
	public static Source gen(Source src){
		return make(src,"产生的");
	}
	public static Source explode(Source src){
		if(src==null)debug.Log.printStackTrace();
		return make(src,"爆炸产生的");
	}
	public static final Source OUT=make(null,"出界");
	public static final Source NO_AIR=make(null,"窒息");
	public static final Source IMPACT=make(null,"撞击");
	public static final Source FIRE_WEATHER=make(null,"火天");
	public static final Source NORMAL=make(null,"自然衰减");
}
class SourceInfo implements Source,java.io.Serializable{
private static final long serialVersionUID=1844677L;
	Source src;
	String verb;
	SourceInfo(Source _src,String _verb){
		src=_src;
		verb=_verb;
	}
	public Agent getSrc(){
		if(src!=null)return src.getSrc();
		return null;
	}
	public String toString(){
		if(src!=null)return src+verb;
		return verb;
	}
}
