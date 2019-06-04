package game.ui;
import graphics.Canvas;
import game.entity.Player;
import game.world.World;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;

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
	public void showText(String text){
		tq.clear();
		addText(text);
	}
	public void addText(String text){
		tq.offer(new Texts(text));
	}
	protected void onDraw(Canvas cv){
		float hp=(float)(pl.hp/pl.maxHp());
		float xp=(float)(pl.xp/pl.maxXp());
		drawProgressBar(cv,0xffff0000,0xff770000,hp,0.1f,0.1f,1.9f,0.3f);
		drawProgressBar(cv,0xff7fffff,0xff3f7f7f,xp,0.1f,0.4f,1.9f,0.6f);
		final float tx_sz=cv.gs.text_size;
		float py=1+tx_sz;
		if(!tq.isEmpty()&&!tq.peek().exist())tq.poll();
		for(Texts t:tq)
		for(String ln:t){
			float[] ws=new float[ln.length()];
			graphics.MyCanvas.text_paint.getTextWidths(ln,ws);
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
class Texts implements Iterable{
	private final String ls[];
	private final long end_t;
	private int _pos=0;
	Texts(String s){
		ls=s.split("\\n");
		end_t=World._.time+300;
	}
	void pop(){
		if(_pos<ls.length)++_pos;
	}
	boolean exist(){
		if(World._.time>end_t)pop();
		return _pos<ls.length;
	}
	public Iterator iterator(){
		return new Iterator(){
			private int pos=_pos;
			public boolean hasNext(){return pos<ls.length;}
			public Object next(){return ls[pos++];}
			public void remove(){}
		};
	}
}
