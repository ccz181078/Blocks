package debug.script;

import java.util.*;
import java.lang.reflect.*;
import static debug.script.Script.*;

abstract class Expr implements indexable{
	int LEFT,RIGHT;
	Expr(int _l,int _r){LEFT=_l;RIGHT=_r;}
	public rvalue toRValue()throws ScriptException{
		throw new ScriptException(LEFT,RIGHT,"rvalue expected");
	}
	public lvalue toLValue()throws ScriptException{
		throw new ScriptException(LEFT,RIGHT,"lvalue expected");
	}
	public Class toClass()throws ScriptException{
		throw new ScriptException(LEFT,RIGHT,"type expected");
	}
	final void rethrow(Exception e)throws ScriptException{
		if(e!=null&&e instanceof ScriptException)throw (ScriptException)e;
		throw new ScriptException(LEFT,RIGHT,e);
	}
	final void rethrow(String e)throws ScriptException{
		throw new ScriptException(LEFT,RIGHT,e);
	}
	Expr dot(String s,int r)throws ScriptException{
		return new Dot(toRValue(),s,r);
	}
	public Expr get(rvalue[] args,int r) throws ScriptException{
		if(args.length!=1)rethrow("invalid array access");
		return new ArrayAccess(this,args[0],r);
	}
	protected void setType(int type){
		setType(LEFT,RIGHT,type);
	}
	protected static void setType(int L,int R,int type){
		int l=lrs.get(L*2),r=lrs.get(R*2+1)+1;
		setTextType(l,r,type);
	}
	//protected void noDefVar()throws ScriptException{}
}
abstract class lvalue extends rvalue{
	lvalue(int l,int r){super(l,r);}
	public lvalue toLValue() throws ScriptException{return this;}
	public abstract void setValue(Object val)throws ScriptException;
}

abstract class rvalue extends Expr{
	rvalue(int l,int r){super(l,r);}
	public rvalue toRValue() throws ScriptException{return this;}
	public abstract Class getType()throws ScriptException;
	public abstract Object getValue()throws ScriptException;
}
interface callable{
	public rvalue call(rvalue[]args,int r)throws ScriptException;
}
interface indexable{
	public Expr get(rvalue[]args,int r)throws ScriptException;
}
class CommaList extends Expr{
	private ArrayList<Expr>list=new ArrayList<>();
	CommaList(int l){super(l,l-1);}
	public void add(Expr v){list.add(v);RIGHT=v.RIGHT;}
	public rvalue[] toRValues()throws ScriptException{
		rvalue args[]=new rvalue[list.size()];
		for(int i=0;i<args.length;++i)args[i]=list.get(i).toRValue();
		return args;
	}
}
class RunnableList extends rvalue{
	rvalue args[];
	RunnableList(CommaList cl)throws ScriptException{
		super(cl.LEFT-1,cl.RIGHT+1);
		args=cl.toRValues();
	}
	public Class getType() throws ScriptException{
		if(args.length==0)throw new ScriptException(LEFT,RIGHT,"type needed");
		return args[args.length-1].getType();
	}
	public Object getValue()throws ScriptException{
		Object ret=null;
		for(rvalue v:args)ret=v.getValue();
		return ret;
	}
}
class While extends rvalue{
	private final rvalue o;
	While(Expr v0,int l,int r)throws ScriptException{
		super(l,r);
		o=v0.toRValue();
		if(!sameType(o.getType(),boolean.class)){
			rethrow("boolean needed");
		}
	}
	public Class getType(){return void.class;}
	public Object getValue()throws ScriptException{
		try{
			while((boolean)o.getValue());
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Try extends rvalue{
	private final rvalue o;
	Try(Expr v0,int l,int r)throws ScriptException{
		super(l,r);
		o=v0.toRValue();
	}
	public Class getType(){return Exception.class;}
	public Object getValue()throws ScriptException{
		try{
			o.getValue();
		}catch(ScriptException e){
			if(e.e!=null)return e.e;
			throw e;
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Throw extends rvalue{
	rvalue a;
	Throw(Expr v0,int l,int r)throws ScriptException{
		super(l,r);
		a=v0.toRValue();
		if(!Exception.class.isAssignableFrom(a.getType()))rethrow("Exception expected");
	}
	public Class getType() throws ScriptException{return boolean.class;}

	public Object getValue() throws ScriptException{
		Object obj=a.getValue();
		if(a==null)rethrow(new NullPointerException("null exception"));
		try{
			rethrow((Exception)obj);
		}catch(Exception e){rethrow(e);}
		return true;
	}
}
class For extends rvalue{
	rvalue a,b,c,d;
	For(Expr v0,int l,int r)throws ScriptException{
		super(l,r);
		if(v0 instanceof RunnableList){
			RunnableList rl=(RunnableList)v0;
			rvalue vs[]=rl.args;
			if(vs.length==4||vs.length==3){
				a=vs[0];
				b=vs[1];
				c=vs[2];
				if(vs.length==4)d=vs[3];
				else d=null;
				return;
			}
		}
		rethrow("invalid for");
	}
	public Class getType(){return void.class;}
	public Object getValue() throws ScriptException{
		try{
			for(a.getValue();(boolean)b.getValue();c.getValue())if(d!=null)d.getValue();
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
/*class For extends rvalue{
	private final lvalue iter;
	private final rvalue list,body;
	private final boolean is_arr;
	For(Expr p0,Expr p1,Expr p2,int l,int r)throws ScriptException{
		super(l,r);
		iter=p0.toLValue();
		list=p1.toRValue();
		body=p2.toRValue();
		is_arr=false;
		Class tp=list.getType();
		if(tp.getComponentType()!=null){
			is_arr=true;
		}else if(!Iterable.class.isAssignableFrom(tp)){
			p1.rethrow("uniterable");
		}
	}
	public Class getType(){return void.class;}
	public Object getValue()throws ScriptException{
		Object obj=list.getValue();
		if(is_arr)obj=Arrays.asList(obj);
		for(Iterator it=((Iterable)obj).iterator();it.hasNext();){
			iter.setValue(it.next());
			body.getValue();
		}
		return null;
	}
}*/
class ArrayLength extends rvalue{
	private final rvalue a;
	ArrayLength(Expr v0,int r)throws ScriptException{
		super(v0.LEFT,r);
		a=v0.toRValue();
	}
	public Class getType(){return int.class;}
	public Object getValue() throws ScriptException{
		try{
			return Array.getLength(a.getValue());
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class ArrayAccess extends lvalue{
	private final rvalue a,b;
	private final Class tp;
	ArrayAccess(Expr v0,Expr v1,int r)throws ScriptException{
		super(v0.LEFT,r);
		a=v0.toRValue();
		b=v1.toRValue();
		tp=a.getType().getComponentType();
		if(tp==null)rethrow("not an array");
		if(!sameType(b.getType(),int.class))rethrow("index should be int");
	}
	public Class getType(){return tp;}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			return Array.get(x,y);
		}catch(Exception e){rethrow(e);}
		return null;
	}
	public void setValue(Object o) throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			Array.set(x,y,o);
		}catch(Exception e){rethrow(e);}
	}
}
class Not extends rvalue{
	private final rvalue v;
	Not(Expr _v)throws ScriptException{
		super(_v.LEFT-1,_v.RIGHT);
		v=_v.toRValue();
		if(!sameType(v.getType(),boolean.class))rethrow("boolean needed");
	}
	public Class getType(){return boolean.class;}
	public Object getValue()throws ScriptException{
		try{
			return !(boolean)v.getValue();
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class And extends rvalue{
	protected final rvalue a,b;
	And(Expr v0,Expr v1)throws ScriptException{
		super(v0.LEFT,v1.RIGHT);
		a=v0.toRValue();
		b=v1.toRValue();
		if(!sameType(a.getType(),boolean.class)||!sameType(b.getType(),boolean.class))
			rethrow("boolean needed");
	}
	public Class getType(){return boolean.class;}
	public Object getValue() throws ScriptException{
		try{
			if((boolean)a.getValue())return(boolean)b.getValue();
			return false;
		}catch(Exception e){rethrow(e);}
		return null;		
	}
}
class Or extends And{
	Or(Expr v0,Expr v1)throws ScriptException{super(v0,v1);}
	public Object getValue() throws ScriptException{
		try{
			if((boolean)a.getValue())return true;
			return(boolean)b.getValue();
		}catch(Exception e){rethrow(e);}
		return null;
	}
}

class Neg extends rvalue{
	private final rvalue v;
	private final int tp;
	Neg(Expr v0,int _tp)throws ScriptException{
		super(v0.LEFT-1,v0.RIGHT);
		v=v0.toRValue();
		tp=_tp;
	}
	public Class getType(){return num_types[tp];}
	public Object getValue() throws ScriptException{
		try{
			Object x=v.getValue();
			switch(tp){
				case 0:return -(byte)x;
				case 1:return -(short)x;
				case 2:return -(int)x;
				case 3:return -(long)x;
				case 4:return -(float)x;
				case 5:return -(double)x;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Add extends rvalue{
	protected final rvalue a,b;
	protected final int tp;
	Add(Expr v0,Expr v1,int _tp)throws ScriptException{
		super(v0.LEFT,v1.RIGHT);
		a=v0.toRValue();
		b=v1.toRValue();
		tp=_tp;
	}
	public Class getType(){return num_types[tp];}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			switch(tp){
				case 0:return (byte)x+(byte)y;
				case 1:return (short)x+(short)y;
				case 2:return (int)x+(int)y;
				case 3:return (long)x+(long)y;
				case 4:return (float)x+(float)y;
				case 5:return (double)x+(double)y;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Sub extends Add{
	Sub(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			switch(tp){
				case 0:return (byte)x-(byte)y;
				case 1:return (short)x-(short)y;
				case 2:return (int)x-(int)y;
				case 3:return (long)x-(long)y;
				case 4:return (float)x-(float)y;
				case 5:return (double)x-(double)y;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Mul extends Add{
	Mul(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			switch(tp){
				case 0:return (byte)x*(byte)y;
				case 1:return (short)x*(short)y;
				case 2:return (int)x*(int)y;
				case 3:return (long)x*(long)y;
				case 4:return (float)x*(float)y;
				case 5:return (double)x*(double)y;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Div extends Add{
	Div(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			switch(tp){
				case 0:return (byte)x/(byte)y;
				case 1:return (short)x/(short)y;
				case 2:return (int)x/(int)y;
				case 3:return (long)x/(long)y;
				case 4:return (float)x/(float)y;
				case 5:return (double)x/(double)y;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Mod extends Add{
	Mod(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			switch(tp){
				case 0:return (byte)x%(byte)y;
				case 1:return (short)x%(short)y;
				case 2:return (int)x%(int)y;
				case 3:return (long)x%(long)y;
				case 4:return (float)x%(float)y;
				case 5:return (double)x%(double)y;
			}
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Less extends Add{
	Less(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Class getType(){return boolean.class;}
	protected final boolean less(Object x,Object y){
		switch(tp){
			case 0:return (byte)x<(byte)y;
			case 1:return (short)x<(short)y;
			case 2:return (int)x<(int)y;
			case 3:return (long)x<(long)y;
			case 4:return (float)x<(float)y;
			case 5:return (double)x<(double)y;
		}
		return false;
	}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			return less(x,y);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class LessEqual extends Less{
	LessEqual(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			return !less(y,x);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Greater extends Less{
	Greater(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			return less(y,x);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class GreaterEqual extends Less{
	GreaterEqual(Expr v0,Expr v1,int _tp)throws ScriptException{super(v0,v1,_tp);}
	public Object getValue() throws ScriptException{
		try{
			Object x=a.getValue(),y=b.getValue();
			return !less(x,y);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Equal extends rvalue{
	protected final rvalue a,b;
	Equal(Expr v0,Expr v1)throws ScriptException{
		super(v0.LEFT,v1.RIGHT);
		a=v0.toRValue();
		b=v1.toRValue();
	}
	public Object getValue()throws ScriptException{
		Object x=a.getValue(),y=b.getValue();
		return cmp(x,y);
	}
	public Class getType(){return Boolean.class;}
	public Object cmp(Object x,Object y)throws ScriptException{
		try{
			return x.equals(y);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class NotEqual extends Equal{
	NotEqual(Expr v0,Expr v1)throws ScriptException{super(v0,v1);}
	public Object cmp(Object x,Object y)throws ScriptException{
		try{
			return !x.equals(y);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class JEqual extends Equal{
	JEqual(Expr v0,Expr v1)throws ScriptException{super(v0,v1);}
	public Object cmp(Object x,Object y){
		return x==y;
	}
}
class JInstanceOf extends rvalue{
	protected final rvalue a;
	protected final Class b;
	JInstanceOf(Expr v0,Expr v1)throws ScriptException{
		super(v0.LEFT,v1.RIGHT);
		a=v0.toRValue();
		b=v1.toClass();
	}
	public Class getType(){return Boolean.class;}
	public Object getValue()throws ScriptException{
		Object x=a.getValue();
		return x!=null&&b.isAssignableFrom(x.getClass());
	}
}



class TransType extends rvalue{
	final rvalue v;
	final Class tp;
	final Method me;
	TransType(Expr v0,int t0,int t1)throws ScriptException{
		super(v0.LEFT,v0.RIGHT);
		v=v0.toRValue();
		tp=num_types[t1];
		me=null;
		try{
			me=num_obj_types[t0].getMethod(tp.getName()+"Value",new Class[]{});
		}catch(Exception e){
			rethrow(e);
		}
	}
	public Class getType(){return tp;}
	public Object getValue() throws ScriptException{
		try{
			return me.invoke(v.getValue());
		}catch(Exception e){rethrow(e);}
		return null;
	}
	
}
class Dot extends Expr implements callable{
	final rvalue v;
	final String name;
	Dot(rvalue v0,String s,int r){
		super(v0.LEFT,r);
		v=v0;
		name=s;
	}
	public rvalue toRValue() throws ScriptException{
		setType(RIGHT,RIGHT,FIELD_TYPE);
		if(name.equals("length")){
			Class c=v.getType().getComponentType();
			if(c!=null)return new ArrayLength(v,RIGHT);
		}
		return new JField(v,name,RIGHT);
	}
	public lvalue toLValue() throws ScriptException{
		setType(RIGHT,RIGHT,FIELD_TYPE);
		return new JField(v,name,RIGHT);
	}
	public rvalue call(rvalue[] args,int r) throws ScriptException{
		setType(RIGHT,RIGHT,METHOD_TYPE);
		return new JMethod(v,name,args,LEFT,r);
	}
}
class ClassDot extends Expr implements callable{
	final Class c;
	final String name;
	ClassDot(Class c0,String s,int l,int r){
		super(l,r);
		c=c0;
		name=s;
	}
	public rvalue toRValue() throws ScriptException{
		setType(RIGHT,RIGHT,FIELD_TYPE);
		return new JStaticField(c,name,LEFT,RIGHT);
	}
	public lvalue toLValue() throws ScriptException{
		setType(RIGHT,RIGHT,FIELD_TYPE);
		return new JStaticField(c,name,LEFT,RIGHT);
	}
	public rvalue call(rvalue[] args,int r) throws ScriptException{
		setType(RIGHT,RIGHT,METHOD_TYPE);
		return new JStaticMethod(c,name,args,LEFT,r);
	}
}
class Name extends Expr implements callable{
	final String[] imported_packages={
		"game.block",
		"game.item",
		"game.entity",
		"game.world",
		"game.ui",
		"java.lang",
		"java.util",
	};
	final String name;
	Name(String s,int l,int r){super(l,r);name=s;}
	private Class toClass_noexcept(){
		Class c=basic_types.get(name);
		if(c==null)try{
			c=Class.forName(name);
		}catch(ClassNotFoundException e){}
		if(c==null)for(String s:imported_packages){
			try{
				Class w=Class.forName(s+'.'+name);
				if(c!=null)return null;
				if(c==null)c=w;
			}catch(ClassNotFoundException e){}
		}
		if(c!=null)setType(TYPE_TYPE);
		return c;
	}
	public Class toClass() throws ScriptException{
		Class c=toClass_noexcept();
		if(c==null)rethrow("no such type");
		return c;
	}
	private rvalue toRValue_noexcept(){
		Integer i=name2id.get(name);
		if(i!=null)return new Var(vars.get(i),LEFT,RIGHT);
		return null;
	}
	public rvalue toRValue() throws ScriptException{
		rvalue v=toRValue_noexcept();
		if(v!=null)return v;
		rethrow("invalid");
		return null;
	}

	public lvalue toLValue() throws ScriptException{
		return toRValue().toLValue();
	}
	

	Expr dot(String s,int r) throws ScriptException{
		Class c=toClass_noexcept();
		if(c!=null){
			if(s.equals("class"))return new JConst(c,Class.class,LEFT,r);
			return new ClassDot(c,s,LEFT,r);
		}
		rvalue v=toRValue_noexcept();
		if(v!=null)return new Dot(v,s,r);
		return new Name(name+"."+s,LEFT,r);
	}
	
	public rvalue call(rvalue[] args,int r) throws ScriptException{
		return new JConstructor(toClass(),args,LEFT,r);
	}
	
	public Expr get(rvalue[] args,int r) throws ScriptException{
		Class c=toClass_noexcept();
		if(c!=null){
			if(args.length==0)return new JClass(Array.newInstance(c,0).getClass(),LEFT,r);
			return new JArrayInit(c,args,LEFT,r);
		}
		return super.get(args,r);
	}
}
class JClass extends Expr implements callable{
	private final Class c;
	JClass(Class tp,int l,int r){super(l,r);c=tp;}
	public Class toClass(){
		setType(TYPE_TYPE);
		return c;
	}

	@Override
	public rvalue call(rvalue[] args,int r) throws ScriptException{
		if(c.getComponentType()!=null)return new JArrayConstructor(c,args,LEFT,r);
		return new JConstructor(c,args,LEFT,r);
	}

	
	public Expr get(rvalue[] args,int r) throws ScriptException{
		if(args.length==0)return new JClass(Array.newInstance(c,0).getClass(),LEFT,r);
		return new JArrayInit(c,args,LEFT,r);
	}
}
class JArrayInit extends rvalue{
	private final Class c,ca;
	private final rvalue args[];
	JArrayInit(Class tp,rvalue arg[],int l,int r)throws ScriptException{
		super(l,r);
		c=tp;
		args=arg;
		for(rvalue v:args)if(!sameType(v.getType(),int.class))v.rethrow("int expected");
		ca=c;
		for(int i=0;i<args.length;++i)ca=Array.newInstance(ca,0).getClass();
	}
	public Class getType(){return ca;}
	public Object getValue() throws ScriptException{
		int dim[]=new int[args.length];
		for(int i=0;i<args.length;++i)dim[i]=args[i].getValue();
		try{
			return Array.newInstance(c,dim);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class ValueWithType extends rvalue{
	private final rvalue v;
	private final Class c;
	private final boolean check;
	ValueWithType(Expr _v,Expr tp)throws ScriptException{
		super(_v.LEFT,tp.RIGHT);
		v=_v.toRValue();
		c=tp.toClass();
		check=false;
		if(v instanceof JNull)return;
		Class vt=v.getType();
		if(sameType(vt,c))return;
		Integer vi=levels.get(vt),ci=levels.get(c);
		if(vi!=null&&ci!=null){
			if((int)vi!=(int)ci)v=new TransType(v,vi,ci);
		}else{
			check=!c.isAssignableFrom(vt);
			if(check&&!vt.isAssignableFrom(c))rethrow(vt+" cannot be "+c);
		}
	}
	public Class getType(){return c;}
	public Object getValue() throws ScriptException{
		Object o=v.getValue();
		if(check)
		try{
			c.cast(o);
		}catch(ClassCastException e){rethrow(e);}
		return o;
	}
}
class StringConcat extends rvalue{
	private final rvalue l,r;
	private final boolean l_is_str;
	StringConcat(Expr _l,Expr _r,boolean _l_is_str)throws ScriptException{
		super(_l.LEFT,_r.RIGHT);
		l=_l.toRValue();
		r=_r.toRValue();
		l_is_str=_l_is_str;
	}
	public Object getValue()throws ScriptException{
		Object a=l.getValue();
		Object b=r.getValue();
		if(l_is_str)return (String)a+b;
		return a+(String)b;
	}
	public Class getType(){return String.class;}
}
class JArrayConstructor extends rvalue{
	Class tp;
	rvalue args[];
	JArrayConstructor(Class _tp,rvalue _args[],int l,int r)throws ScriptException{
		super(l,r);
		tp=_tp;
		args=_args;
		Class ctp=tp.getComponentType();
		for(int i=0;i<args.length;++i){
			rvalue v=Assign.cast(ctp,args[i]);
			if(v==null)args[i].rethrow("invalid type");
			args[i]=v;
		}
	}
	public Class getType(){
		return tp;
	}
	public Object getValue()throws ScriptException{
		Object arr=Array.newInstance(tp.getComponentType(),args.length);
		for(int i=0;i<args.length;++i){
			try{
				Array.set(arr,i,args[i].getValue());
			}catch(Exception e){
				args[i].rethrow(e);
			}
		}
		return arr;
	}
}
class JConstructor extends rvalue{
	private final Constructor c;
	private final rvalue args[];
	JConstructor(Class tp,rvalue _args[],int l,int r)throws ScriptException{
		super(l,r);
		args=_args;
		c=null;
		try{
			c=tp.getDeclaredConstructor(getTypes(_args));
		}catch(NoSuchMethodException e){
			rethrow(e);
		}
		c.setAccessible(true);
	}
	public Class getType(){
		return c.getDeclaringClass();
	}
	public Object getValue()throws ScriptException{
		try{
			return c.newInstance(getValues(args));
		}catch(Exception e){
			rethrow(e);
		}
		return null;
	}
}
class JField extends lvalue{
	private final Field f;
	private final rvalue o;
	JField(Expr v0,String name,int r)throws ScriptException{
		super(v0.LEFT,r);
		o=v0.toRValue();
		f=null;
		try{
			f=getField(o.getType(),name);
		}catch(Exception e){rethrow(e);}
	}
	static Field getField(Class c0,String name)throws Exception{
		if(c0.isInterface())throw new Exception("interface "+c0.getSimpleName()+" doesn't have field");
		for(Class c=c0;;){
			try{
				Field f=c.getDeclaredField(name);
				if(!(Modifier.isPrivate(f.getModifiers())&&c!=c0)){
					f.setAccessible(true);
					return f;
				}
			}catch(Exception e){}
			if((c=c.getSuperclass())==null)throw new Exception("no such field");
		}
	}
	public Class getType() throws ScriptException{
		return f.getType();
	}
	public Object getValue() throws ScriptException{
		try{
			return f.get(o.getValue());
		}catch(Exception e){rethrow(e);}
		return null;
	}
	public void setValue(Object val) throws ScriptException{
		try{
			f.set(o.getValue(),val);
		}catch(Exception e){rethrow(e);}
	}
}
class JStaticField extends lvalue{
	private final Field f;
	JStaticField(Class c,String name,int l,int r)throws ScriptException{
		super(l,r);
		f=null;
		try{
			f=JField.getField(c,name);
			if(!Modifier.isStatic(f.getModifiers()))rethrow("not a static field");
		}catch(Exception e){rethrow(e);}
	}
	public Class getType() throws ScriptException{
		return f.getType();
	}
	public Object getValue() throws ScriptException{
		try{
			return f.get(null);
		}catch(Exception e){rethrow(e);}
		return null;
	}
	public void setValue(Object val) throws ScriptException{
		try{
			f.set(null,val);
		}catch(Exception e){rethrow(e);}
	}
}
class JStaticMethod extends rvalue{
	private final Method m;
	private final rvalue args[];
	JStaticMethod(Class c0,String _m,rvalue arg[],int l,int r)throws ScriptException{
		super(l,r);
		args=arg;
		m=null;
		try{
			m=JMethod.getMethod(c0,_m,getTypes(arg));
			if(!Modifier.isStatic(m.getModifiers()))rethrow("not a static method");
		}catch(Exception e){rethrow(e);};
	}
	public Class getType() throws ScriptException{
		return m.getReturnType();
	}
	public Object getValue() throws ScriptException{
		try{
			return m.invoke(null,getValues(args));
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class JMethod extends rvalue{
	private final rvalue obj,args[];
	private final Method m;
	private final Class tps[];
	private final boolean isfinal;
	public static Method getMethod(Class c0,String name,Class tps[])throws Exception{
		HashSet<Class>cs=new HashSet<>();
		Stack<Class>stk=new Stack<>();
		stk.push(c0);
		cs.add(c0);
		while(!stk.empty()){
			Class c=stk.pop();
			try{
				Method m=c.getDeclaredMethod(name,tps);
				if(!(Modifier.isPrivate(m.getModifiers())&&c!=c0)){
					m.setAccessible(true);
					return m;
				}
			}catch(Exception e){}
			if(c.isInterface()){
				for(Class i:c.getInterfaces())if(!cs.contains(i)){
					stk.push(i);
					cs.add(i);
				}
			}else{
				c=c.getSuperclass();
				if(c!=null&&!cs.contains(c)){
					stk.push(c);
					cs.add(c);
				}
			}
		}
		throw new Exception("no such method:"+c0.getSimpleName()+"."+name);
	}
	JMethod(rvalue v0,String _m,rvalue arg[],int l,int r)throws ScriptException{
		super(l,r);
		obj=v0;
		args=arg;
		m=null;
		isfinal=false;
		Class c0=v0.getType();
		tps=getTypes(arg);
		try{
			m=getMethod(c0,_m,tps);
			int mo=m.getModifiers();
			if(Modifier.isPrivate(mo)||Modifier.isFinal(mo)||Modifier.isFinal(c0.getModifiers()))isfinal=true;
		}catch(Exception e){rethrow(e);}
	}
	public Class getType() throws ScriptException{
		return m.getReturnType();
	}
	public Object getValue() throws ScriptException{
		try{
			Object o=obj.getValue(),os[]=getValues(args);
			Class c=o.getClass();
			if(isfinal||c==m.getDeclaringClass())return m.invoke(o,os);
			return getMethod(c,m.getName(),tps).invoke(o,os);
		}catch(Exception e){rethrow(e);}
		return null;
	}
}
class Var extends lvalue{
	private final int id;
	private final DefVar def;
	Var(DefVar d,int l,int r){
		super(l,r);
		def=d;
		id=d.id;
		setType(VAR_TYPE);
	}
	Var(String name,Class tp,int l,int r)throws ScriptException{
		super(l,r);
		def=new DefVar(name,tp,l,r);
		id=def.id;
		setType(VAR_TYPE);
	}
	public Class getType(){
		return def.tp;
	}
	public Object getValue(){
		return var_stack[id];
	}
	public void setValue(Object val){
		var_stack[id]=val;
	}
}
class Assign extends rvalue{
	private final lvalue l;
	private final rvalue r;
	Assign(lvalue _l,rvalue _r)throws ScriptException{
		super(_l.LEFT,_r.RIGHT);
		l=_l;
		r=cast(l.getType(),_r);
		if(r==null)rethrow("invalid type");
	}
	static rvalue cast(Class lt,rvalue r)throws ScriptException{
		if(r.getClass()==JNull.class)return r;
		Class rt=r.getType();
		if(sameType(lt,rt))return r;
		Integer li=levels.get(lt),ri=levels.get(rt);
		if(li!=null&&ri!=null){
			if(li==ri)return r;
			if(li>ri)return new TransType(r,ri,li);
		}else{
			if(lt.isAssignableFrom(rt))return r;
		}
		return null;
	}
	public Class getType() throws ScriptException{
		return r.getType();
	}
	public Object getValue()throws ScriptException{
		Object o=r.getValue();
		l.setValue(o);
		return o;
	}
}
class JConst extends rvalue{
	private final Object v;
	private final Class tp;
	JConst(Object o,Class _tp,int p){this(o,_tp,p,p);}
	JConst(Object o,Class _tp,int l,int r){
		super(l,r);
		v=o;
		tp=_tp;
		setType(CONST_TYPE);
	}
	public Object getValue(){return v;}
	public Class getType(){
		return tp;
	}
}
class JNull extends rvalue{
	JNull(int l,int r){
		super(l,r);
		setType(CONST_TYPE);
	}
	public Class getType() throws ScriptException{
		throw new ScriptException(LEFT,RIGHT,"null doesn't have a type");
	}
	public Object getValue() throws ScriptException{
		return null;
	}
}


