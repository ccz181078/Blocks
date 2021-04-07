package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.*;

public abstract class LambdaNode extends Item{
	public abstract BmpRes getBmp();
	
	@Override
	public int maxAmount(){
		return 1;
	}
	public String getAmountString(int cnt){
		return valid()?"":"x";
	}
	protected static String doc(LambdaNode w){
		if(w==null)return "?";
		return w.getDoc();
	}
	protected static boolean valid(LambdaNode w){
		if(w==null)return false;
		return w.valid();
	}
	protected boolean valid(){return true;}
	protected LambdaNode compute(){return this;}
	protected abstract LambdaNode subst(Variable x,LambdaNode x_);
	@Override
	public Item clickAt(double x,double y,Agent a){
		if(valid())return compute();
		return this;
	}
	
	public abstract String getDoc();

	@Override
	public boolean isCreative(){return true;}
	public static class Lambda extends LambdaNode implements DefaultItemContainer{
		static BmpRes bmp=new BmpRes("Item/Lambda/Lambda");
		public BmpRes getBmp(){return bmp;}
		SpecialItem<Variable> x=new SpecialItem<Variable>(Variable.class);
		SpecialItem<LambdaNode> fx=new SpecialItem<LambdaNode>(LambdaNode.class);
		public ShowableItemContainer getItems(){return ItemList.create(x,fx);}
		public String getDoc(){
			return "(λ"+doc(x.get())+"."+doc(fx.get())+")";
		}
		protected boolean valid(){
			return valid(x.get())&&valid(fx.get());
		}
		protected LambdaNode compute(){
			LambdaNode fx0=fx.get();
			LambdaNode fx1=fx0.compute();
			if(fx0==fx1)return this;
			Lambda v=new Lambda();
			v.x.insert(x.get());
			v.fx.insert(fx1);
			return v;
		}
		protected LambdaNode subst(Variable x0,LambdaNode x1){
			if(x0==x.get())return this;
			Lambda v=new Lambda();
			v.x.insert(x.get());
			v.fx.insert(fx.get().subst(x0,x1));
			return v;
		}
		LambdaNode apply(LambdaNode x_){
			return fx.get().subst(x.get(),x_);
		}
	}
	public static class Apply extends LambdaNode implements DefaultItemContainer{
		static BmpRes bmp=new BmpRes("Item/Lambda/Apply");
		public BmpRes getBmp(){return bmp;}
		SpecialItem<LambdaNode> f=new SpecialItem<LambdaNode>(LambdaNode.class);
		SpecialItem<LambdaNode> x=new SpecialItem<LambdaNode>(LambdaNode.class);
		public ShowableItemContainer getItems(){return ItemList.create(f,x);}
		public String getDoc(){
			return doc(f.get())+"("+doc(x.get())+")";
		}
		protected boolean valid(){
			return valid(f.get())&&valid(x.get());
		}
		protected LambdaNode compute(){
			LambdaNode x0=x.get();
			LambdaNode x1=x0.compute();
			if(x0!=x1){
				Apply v=new Apply();
				v.f.insert(f.get());
				v.x.insert(x1);
				return v;
			}
			LambdaNode f0=f.get();
			LambdaNode f1=f0.compute();
			if(f0!=f1){
				Apply v=new Apply();
				v.f.insert(f1);
				v.x.insert(x.get());
				return v;
			}
			if(f1 instanceof Lambda){
				return ((Lambda)f1).apply(x1);
			}
			return this;
		}
		protected LambdaNode subst(Variable x0,LambdaNode x1){
			Apply v=new Apply();
			v.f.insert(f.get().subst(x0,x1));
			v.x.insert(x.get().subst(x0,x1));
			return v;
		}
	}
	public static class Variable extends LambdaNode{
		static BmpRes bmp=new BmpRes("Item/Lambda/Variable");
		public BmpRes getBmp(){return bmp;}
		public String getDoc(){
			return "v"+hashCode()%100;
		}
		protected LambdaNode subst(Variable x0,LambdaNode x1){
			if(this==x0)return x1;
			return this;
		}
		@Override
		public String getAmountString(int cnt){
			return getDoc();
		}
		@Override
		public Item clickAt(double x,double y,Agent a){
			drop(x,y,1);
			return this;
		}
	}
}

class Copy extends Item{
	static BmpRes bmp=new BmpRes("Item/Copy");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int maxAmount(){return 1;}
	@Override
	public boolean isCreative(){return true;}
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){
		if(src.isEmpty())return false;
		if(dst.getAmount()!=1)return false;
		Item w=(Item)util.SerializeUtil.deepCopy(src.get());
		dst.pop();
		dst.insert(w.setAmount(1));
		return true;
	}
}