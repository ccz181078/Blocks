package game.block;

import util.BmpRes;
import game.item.Item;
import game.ui.*;

public class PaperBlock extends WoodenType implements BlockWithUI{
	static BmpRes bmp=new BmpRes("Block/PaperBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 20;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public String text=null;
	public int fuelVal(){return 50;}
	public void onDestroy(int x,int y){}
	public void onDestroyByFire(int x,int y){}
	
	@Override
	protected int crackType(){return 1;}
	
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){
			@Override
			protected void onDraw(graphics.Canvas cv){
				super.onDraw(cv);
				if(text==null)return;
				final float tx_sz=cv.gs.text_size;
				float py=0.5f+tx_sz;
				for(String ln:text.split("\\n")){
					float[] ws=new float[ln.length()];
					for(int i=0;i<ws.length;++i)ws[i]=32;
					//graphics.MyCanvas.text_paint.getTextWidths(ln,ws);/*android*/
					for(int l=0,r;l<ws.length;l=r){
						float w=tx_sz;
						for(r=l;r<ws.length&&w+ws[r]/32*tx_sz<4;++r)w+=ws[r]/32*tx_sz;
						cv.drawText(ln.substring(l,r),tx_sz,py,tx_sz,-1);
						py+=tx_sz*1.25f;
						if(py>=3.5f)return;
					}
				}
			}
		}.setBlock(ba);
	}
};
