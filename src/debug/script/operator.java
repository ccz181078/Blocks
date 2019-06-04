package debug.script;

import java.util.*;
import java.lang.reflect.*;
import static debug.script.Script.*;

abstract class operator implements token,Cloneable{
	abstract int pushLevel();
	abstract int popLevel();
	int LEFT,RIGHT;
	boolean isL1(){return false;}
	public operator clone(int pos){
		try{
			operator o=(operator)super.clone();
			o.LEFT=o.RIGHT=pos;
			return o;
		}catch(Exception e){debug.Log.i(e);}
		return null;
	}
	protected void setType(int type){
		int l=lrs.get(LEFT*2),r=lrs.get(RIGHT*2+1)+1;
		setTextType(l,r,type);
	}
	abstract void pop()throws ScriptException;
	public void popStack(int push_level)throws ScriptException{
		while(!opers.empty()&&opers.peek().popLevel()<=push_level)opers.pop().pop();
	}
	public void push(int x)throws ScriptException{
		popStack(pushLevel());
		opers.push(this);
	}
	boolean l2r(){return true;}
	protected Expr[] a_op_b()throws ScriptException{
		if(vals.size()<2)throw new ScriptException(LEFT,RIGHT);
		Expr r=vals.pop();
		Expr l=vals.pop();
		if(l.RIGHT!=LEFT-1||r.LEFT!=RIGHT+1)throw new ScriptException(Math.min(LEFT,l.LEFT),Math.max(RIGHT,r.RIGHT));
		return new Expr[]{l,r};
	}
	protected Expr op_a()throws ScriptException{
		if(vals.empty())throw new ScriptException(LEFT,RIGHT);
		Expr v=vals.pop();
		if(v.LEFT!=RIGHT+1)throw new ScriptException(Math.min(LEFT,v.LEFT),Math.max(RIGHT,v.RIGHT));
		return v;
	}
	protected Expr a_op()throws ScriptException{
		if(vals.empty())throw new ScriptException(LEFT,RIGHT);
		Expr v=vals.pop();
		if(v.RIGHT!=LEFT+1)throw new ScriptException(Math.min(LEFT,v.LEFT),Math.max(RIGHT,v.RIGHT));
		return v;
	}
}
class DotOp extends operator{//.
	int pushLevel(){return 100;}
	int popLevel(){return 100;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		if(!(v[1] instanceof Name))v[1].rethrow("");
		vals.push(v[0].dot(((Name)v[1]).name,v[1].RIGHT));
	}
}
class ColonOp extends operator{//:
	int pushLevel(){return 200;}
	int popLevel(){return 200;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new ValueWithType(v[0],v[1]));
	}
}

class MulOp extends operator{
	int pushLevel(){return 240;}
	int popLevel(){return 240;}
	protected void str_op(rvalue[]v,int w)throws ScriptException{
		throw new ScriptException(LEFT,RIGHT);
	}
	void pop()throws ScriptException{
		Expr v0[]=a_op_b();
		rvalue v[]={v0[0].toRValue(),v0[1].toRValue()};
		for(int i=0;i<2;++i){
			Class tp=v[i].getType();
			if(tp==String.class){
				str_op(v,i);
				return;
			}
		}
		int mx=numberUpperCast(v);
		pop2(v[0],v[1],mx);
	}
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Mul(a,b,tp));
	}
}
class DivOp extends MulOp{
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Div(a,b,tp));
	}
}
class ModOp extends MulOp{
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Mod(a,b,tp));
	}
}
class AddOp extends SubOp{
	protected void str_op(rvalue[] v,int w) throws ScriptException{
		vals.push(new StringConcat(v[0],v[1],w==0));
	}
	void pop1(rvalue v)throws ScriptException{
		getNumberTypeId(v);
		vals.push(v);
	}
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Add(a,b,tp));
	}
}
class SubOp extends MulOp{
	int tp;
	int pushLevel(){return tp==1?150:250;}
	int popLevel(){return tp==1?151:250;}
	boolean isL1(){return tp==1;}
	public void push(int x) throws ScriptException{
		tp=(!opers.empty()&&opers.peek().RIGHT+1==LEFT)?1:2;
		super.push(x);
	}
	void pop1(rvalue v)throws ScriptException{
		int id=getNumberTypeId(v);
		vals.push(new Neg(v,id));
	}
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Sub(a,b,tp));
	}
	void pop()throws ScriptException{
		if(tp==1)pop1(op_a().toRValue());
		else super.pop();
	}
}

class EqualOp extends operator{
	int pushLevel(){return 270;}
	int popLevel(){return 270;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new Equal(v[0],v[1]));
	}
}
class NotEqualOp extends EqualOp{
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new NotEqual(v[0],v[1]));
	}
}
class JEqualOp extends EqualOp{
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new JEqual(v[0],v[1]));
	}
}
class JInstanceOfOp extends EqualOp{
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new JInstanceOf(v[0],v[1]));
	}
}

class LessOp extends MulOp{
	int pushLevel(){return 260;}
	int popLevel(){return 260;}
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Less(a,b,tp));
	}
}
class LessEqualOp extends LessOp{
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new LessEqual(a,b,tp));
	}
}
class GreaterOp extends LessOp{
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new Greater(a,b,tp));
	}
}
class GreaterEqualOp extends LessOp{
	void pop2(rvalue a,rvalue b,int tp)throws ScriptException{
		vals.push(new GreaterEqual(a,b,tp));
	}
}

class AndOp extends operator{
	int pushLevel(){return 275;}
	int popLevel(){return 275;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new And(v[0],v[1]));
	}
}
class OrOp extends operator{
	int pushLevel(){return 280;}
	int popLevel(){return 280;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new Or(v[0],v[1]));
	}
}
class NotOp extends operator{
	boolean isL1(){return true;}
	int pushLevel(){return 150;}
	int popLevel(){return 151;}
	void pop()throws ScriptException{
		Expr v=op_a();
		vals.push(new Not(v));
	}
}
class WhileOp extends NotOp{
	void pop()throws ScriptException{
		Expr v=op_a();
		vals.push(new While(v,LEFT,v.RIGHT));
	}
}
class ForOp extends NotOp{
	void pop()throws ScriptException{
		Expr v=op_a();
		vals.push(new For(v,LEFT,v.RIGHT));
	}
}
class ThrowOp extends NotOp{
	void pop()throws ScriptException{
		Expr v=op_a();
		vals.push(new Throw(v,LEFT,v.RIGHT));
	}
}
class TryOp extends NotOp{
	void pop()throws ScriptException{
		Expr v=op_a();
		vals.push(new Try(v,LEFT,v.RIGHT));
	}
}
class AssignOp extends operator{//=
	int pushLevel(){return 300;}
	int popLevel(){return 301;}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		vals.push(new Assign(v[0].toLValue(),v[1].toRValue()));
	}
}
class DefAssignOp extends AssignOp{//:=
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		if(!(v[0] instanceof Name))throw new ScriptException(v[0].LEFT,RIGHT,"invalid var name");
		rvalue v1=v[1].toRValue();
		Var var=new Var(((Name)v[0]).name,v1.getType(),v[0].LEFT,v[0].RIGHT);
		vals.push(new Assign(var,v1));
	}
}
class AddAssignOp extends AssignOp{
	rvalue calc(rvalue l,rvalue r,int tp)throws ScriptException{
		return new Add(l,r,tp);
	}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		lvalue l=v[0].toLValue();
		rvalue r=v[1].toRValue();
		if(l.getType()==String.class&&getClass()==AddAssignOp.class){
			vals.push(new Assign(l,new StringConcat(l,r,true)));
			return;
		}
		rvalue vs[]={l,r};
		int mx=numberUpperCast(vs);
		vals.push(new Assign(l,calc(vs[0],vs[1],mx)));
	}
}
class SubAssignOp extends AddAssignOp{
	rvalue calc(rvalue l,rvalue r,int tp)throws ScriptException{
		return new Sub(l,r,tp);
	}
}
class MulAssignOp extends AddAssignOp{
	rvalue calc(rvalue l,rvalue r,int tp)throws ScriptException{
		return new Mul(l,r,tp);
	}
}
class DivAssignOp extends AddAssignOp{
	rvalue calc(rvalue l,rvalue r,int tp)throws ScriptException{
		return new Div(l,r,tp);
	}
}
class ModAssignOp extends AddAssignOp{
	rvalue calc(rvalue l,rvalue r,int tp)throws ScriptException{
		return new Mod(l,r,tp);
	}
}

class CommaOp extends operator{//,
	int pushLevel(){return 400;}
	int popLevel(){return 400;}
	CommaOp(){}
	CommaOp(int l,int r){LEFT=l;RIGHT=r;}
	static void merge(Expr l,Expr r)throws ScriptException{
		if(!(l instanceof CommaList)){
			CommaList cl=new CommaList(l.LEFT);
			cl.add(l);
			cl.add(r);
			vals.push(cl);
		}else{
			((CommaList)l).add(r);
			vals.push(l);
		}
	}
	void pop()throws ScriptException{
		Expr v[]=a_op_b();
		merge(v[0],v[1]);
	}
}
class DefVar extends operator{
	int pushLevel(){return 0;}
	int popLevel(){return 1000;}
	final String str;
	final int old_id,id;
	final Class tp;
	DefVar(String name,Class _tp,int l,int r)throws ScriptException{
		if(name.indexOf('.')>0)throw new ScriptException(l,r,"invalid var name");
		LEFT=l;RIGHT=r;
		str=name;
		tp=_tp;
		if(name2id.containsKey(name))old_id=name2id.get(name);
		else old_id=-1;
		id=vars.size();
		name2id.put(str,id);
		defs.push(this);
		vars.push(this);
		//debug.Log.i("var "+str+":"+id);
	}
	void pop(){
		vars.pop();
		if(old_id!=-1)name2id.put(str,old_id);
		else name2id.remove(str);
	}
}
class LeftParenthesis extends operator{
	int pushLevel(){return 0;}
	int popLevel(){return 1000000;}
	boolean isL1(){return true;}
	void pop(){}
	public void push(int x) throws ScriptException{
		super.push(x);
		setType(++parenthesis_depth%4+PARENTHESIS_TYPE);
		defs.push(this);
	}
}
class RightParenthesis extends operator{
	int pushLevel(){return 10000;}
	int popLevel(){return 0;}
	String getLeftOp(){return "'('";}
	Class getLeftClass(){return LeftParenthesis.class;}
	protected void popDefs()throws ScriptException{
		while(!defs.empty()&&defs.peek().getClass()!=getLeftClass())defs.pop().pop();
		if(defs.empty())throw new ScriptException(LEFT,RIGHT);
		defs.pop();
	}
	public void push(int x)throws ScriptException{
		boolean comma=false;
		if(!opers.empty()){
			operator o=opers.peek();
			if(o.getClass()==CommaOp.class&&o.RIGHT+1==LEFT){
				opers.pop();
				comma=true;
			}
		}
		popStack(10000);
		if(!opers.empty()){
			operator o=opers.pop();
			if(o.getClass()==getLeftClass()){
				CommaList cl;
				if(o.RIGHT+1==LEFT){
					cl=new CommaList(o.RIGHT+1);
				}else if(!vals.empty()){
					Expr v=vals.pop();
					if(v.LEFT<=o.RIGHT||v.RIGHT>=LEFT)throw new ScriptException(Math.min(v.LEFT,o.RIGHT),Math.max(v.RIGHT,LEFT));
					if(v instanceof CommaList)cl=(CommaList)v;
					else{
						cl=new CommaList(o.RIGHT+1);
						cl.add(v);
					}
				}else throw new ScriptException(o.LEFT,RIGHT);
				if(comma&&cl.RIGHT+2==LEFT)++cl.RIGHT;
				popStack(110);
				setType(parenthesis_depth--%4+PARENTHESIS_TYPE);
				if(!vals.empty()){
					Expr v=vals.peek();
					if(v.RIGHT==o.LEFT-1){
						vals.pop();
						aLbR(v,cl);
						return;
					}
				}
				LaR(cl);
				return;
			}
		}
		throw new ScriptException(x,x,new Exception("failed to match"+getLeftOp()));
	}
	void aLbR(Expr v,CommaList cl)throws ScriptException{
		if(!(v instanceof callable))throw new ScriptException(v.LEFT,v.RIGHT,"invalid [ ]");
		vals.push(((callable)v).call(cl.toRValues(),RIGHT));
		popDefs();
	}
	void LaR(CommaList cl)throws ScriptException{
		vals.push(new RunnableList(cl));
		popDefs();
	}
	void pop(){}
}
class LeftBracket extends LeftParenthesis{
	
}
class RightBracket extends RightParenthesis{
	Class getLeftClass(){return LeftBracket.class;}
	String getLeftOp(){return "'['";}
	void aLbR(Expr v,CommaList cl)throws ScriptException{
		if(!(v instanceof indexable))throw new ScriptException(v.LEFT,v.RIGHT,"invalid [ ]");
		vals.push(((indexable)v).get(cl.toRValues(),RIGHT));
		popDefs();
	}
	void LaR(CommaList cl)throws ScriptException{
		popDefs();
		throw new ScriptException(cl.LEFT,cl.RIGHT,"invalid [ ]");
	}
}


