package game.ui;
import graphics.Canvas;
import game.entity.Player;
import game.world.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;
import game.item.Armor;
import game.item.Shoes;
import game.item.Tank;
import game.item.Airship;
import game.item.Shilka;
import game.world.StatMode.StatResult;
import game.GlobalSetting;

public class UI_Info extends UI{
	private static final long serialVersionUID=1844677L;
	//左侧界面，包括玩家hp、xp，提示文本
	//原点在屏幕左上角
	
	Player pl;
	Queue<Texts>tq;
	public UI_Info(Player _pl){
		x=y=0;
		pl=_pl;
		tq=new LinkedList<>();
	}
	public void showTextIfFree(String text){
		if(tq.isEmpty())showText(text);
	}
	public void showText(String text){
		tq.clear();
		addText(text);
	}
	public void addText(String text){
		for(Texts t:tq){
			if(t.s.equals(text)){
				t.end_t=World.cur.time+300;
				return;
			}
		}
		tq.offer(new Texts(text));
	}
	protected void onDraw(Canvas cv){
		float hp=(float)(pl.hp/pl.maxHp());
		float xp=(float)(pl.xp/pl.maxXp());
		drawProgressBar(cv,0xffff0000,0xff770000,hp,0.1f,0.1f,1.9f,0.3f);
		drawProgressBar(cv,0xff7fffff,0xff3f7f7f,xp,0.1f,0.4f,1.9f,0.6f);
		drawProgressBar(cv,0xff0000ff,0xffffffff,pl.air_rate*0.01f,0.1f,0.7f,1.9f,0.9f);
		
		if(World.cur.getMode() instanceof PvPMode){
			PvPMode mode=(PvPMode)World.cur.getMode();
			float sz=Math.min(GlobalSetting.getGameSetting().text_size,0.7f);
			cv.drawText("$"+StatResult.getPriceString(pl.money)+"/$"+StatResult.getPriceString(pl.getPrice(mode.getStat())),sz*15f,1.2f,sz,1);
		}
		float sz=Math.min(GlobalSetting.getGameSetting().text_size,0.7f);
		cv.drawText(((int)pl.hp)+"/"+(int)pl.maxHp(),1f,0.3f,sz,0);
		if(World.cur.getMode() instanceof ECPvPMode){
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			cv.drawText("剩余"+mode.restPlayerCount()+"人,你击杀了"+pl.kill_cnt+"人",sz*20f,1.2f,sz,1);
			cv.save();
			cv.translate(2.1f,0.7f);
			cv.drawRect(0,0,1.8f,0.2f,0xff804040);
			float L=World.cur.getRelX(mode.cur_l);
			float R=World.cur.getRelX(mode.cur_r);
			cv.drawRect(L*1.8f,0,R*1.8f,0.2f,0xff808080);
			L=World.cur.getRelX(mode.nxt_l);
			R=World.cur.getRelX(mode.nxt_r);
			cv.drawRect(L*1.8f,0,R*1.8f,0.2f,0xff80b080);
			float w=World.cur.getRelX(mode.next_box_pos);
			cv.drawRect(w*1.7f,0,w*1.9f,0.2f,mode.next_box_state==0?0x80ff00ff:0x80ffff00);
			double xy[]=pl.getCamaraPos();
			float x[]=new float[]{World.cur.getRelX(xy[0]),1-World.cur.getRelY(xy[1])};
			cv.drawRect(x[0]*1.8f-0.05f,x[1]*0.2f-0.05f,x[0]*1.8f+0.05f,x[1]*0.2f+0.05f,0xff0000ff);
			if(!mode.airship.isRemoved()){
				x=new float[]{World.cur.getRelX(mode.airship.x),1-World.cur.getRelY(mode.airship.y)};
				cv.drawRect(x[0]*1.8f-0.05f,x[1]*0.2f-0.05f,x[0]*1.8f+0.05f,x[1]*0.2f+0.05f,0xff00ffff);
			}
			cv.restore();
		}
		Armor ar=pl.armor.get();
		if(ar!=null)ar.drawLeftInfo(cv);
		Shoes sh=pl.shoes.get();
		if(sh!=null)sh.drawLeftInfo(cv);
		/*if(ar instanceof Tank){
			Tank tank=(Tank)ar;
			float damage=1-tank.damage*1f/tank.maxDamage();
			float reload=tank.reload/tank.maxReload();
			drawProgressBar(cv,0xff00ff00,0xff3f7f7f,damage,2.1f,0.1f,3.9f,0.3f);
			drawProgressBar(cv,0x80ffffff,0xff3f7f7f,reload,2.1f,0.3f,3.9f,0.4f);
		}
		if(ar instanceof Airship){
			Airship ship=(Airship)ar;
			float damage=1-ship.damage*1f/ship.maxDamage();
			float reload=ship.reload/ship.maxReload();
			drawProgressBar(cv,0xff00ff00,0xff3f7f7f,damage,2.1f,0.1f,3.9f,0.3f);
			drawProgressBar(cv,0x80ffffff,0xff3f7f7f,reload,2.1f,0.3f,3.9f,0.4f);
		}
		if(ar instanceof Shilka){
			Shilka tank=(Shilka)ar;
			float damage=1-tank.damage*1f/tank.maxDamage();
			float reload=tank.reload/tank.maxReload();
			drawProgressBar(cv,0xff00ff00,0xff3f7f7f,damage,2.1f,0.1f,3.9f,0.3f);
			drawProgressBar(cv,0x80ffffff,0xff3f7f7f,reload,2.1f,0.3f,3.9f,0.4f);
		}*/
		final float tx_sz=cv.gs.text_size;
		float py=1.3f+tx_sz;
		if(!tq.isEmpty()&&!tq.peek().exist())tq.poll();
		for(Texts t:tq)
		for(String ln:t){
			float[] ws=new float[ln.length()];
			for(int i=0;i<ws.length;++i)ws[i]=32;
			//graphics.MyCanvas.text_paint.getTextWidths(ln,ws);/*android*/
			for(int l=0,r;l<ws.length;l=r){
				float w=tx_sz;
				for(r=l;r<ws.length&&w+ws[r]/32*tx_sz<8*(1f/H_div_W-1);++r)w+=ws[r]/32*tx_sz;
				cv.drawText(ln.substring(l,r),tx_sz,py,tx_sz,-1);
				py+=tx_sz*1.25f;
				if(py>7){
					tq.peek().pop();
					return;
				}
			}
		}
	}
}
class Texts implements Iterable<String>{
	private final String ls[];
	long end_t;
	private int _pos=0;
	String s;
	Texts(String s){
		this.s=s;
		ls=s.split("\\n");
		end_t=World.cur.time+300;
	}
	void pop(){
		if(_pos<ls.length)++_pos;
	}
	boolean exist(){
		if(World.cur.time>end_t)pop();
		return _pos<ls.length;
	}
	public Iterator<String> iterator(){
		return new Iterator<String>(){
			private int pos=_pos;
			public boolean hasNext(){return pos<ls.length;}
			public String next(){return ls[pos++];}
			public void remove(){}
		};
	}
}
