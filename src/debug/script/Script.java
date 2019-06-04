package debug.script;

import java.lang.reflect.*;
import java.util.*;
import static debug.script.Script.*;
import java.io.*;
import debug.ObjectNode;

public class Script{
	static HashMap<String,operator>ops=null;
	
	static ValStack vals;
	static Stack<DefVar>vars;
	static Stack<operator>opers,defs;
	static HashMap<String,Integer>name2id;
	
	//script runtime:
	static Object[] var_stack;
	static int script_type[];
	static char[] script_src;
	static ArrayList<Integer> lrs;

	
	static HashMap<Class,Class>obj2basic;
	static HashMap<Class,Integer>levels;
	static HashMap<String,Class>basic_types;
	
	static Class[] num_types={
		byte.class,
		short.class,
		int.class,
		long.class,
		float.class,
		double.class,
	},num_obj_types={
		Byte.class,
		Short.class,
		Integer.class,
		Long.class,
		Float.class,
		Double.class,
	};
	static{
		levels=new HashMap<>();
		obj2basic=new HashMap<>();
		for(int i=0;i<6;++i){
			levels.put(num_types[i],i);
			levels.put(num_obj_types[i],i);
			obj2basic.put(num_obj_types[i],num_types[i]);
		}
		obj2basic.put(Boolean.class,boolean.class);
		obj2basic.put(Character.class,char.class);
		obj2basic.put(Void.class,void.class);
		
		basic_types=new HashMap<>();
		basic_types.put("boolean",boolean.class);
		basic_types.put("char",char.class);
		basic_types.put("void",void.class);
		basic_types.put("byte",byte.class);
		basic_types.put("short",short.class);
		basic_types.put("int",int.class);
		basic_types.put("long",long.class);
		basic_types.put("float",float.class);
		basic_types.put("double",double.class);
		
		ops=new HashMap<>();
		ops.put(".",new DotOp());
		ops.put(":",new ColonOp());

		ops.put("+",new AddOp());
		ops.put("-",new SubOp());
		ops.put("*",new MulOp());
		ops.put("/",new DivOp());
		ops.put("%",new ModOp());

		ops.put("<",new LessOp());
		ops.put("<=",new LessEqualOp());
		ops.put(">",new GreaterOp());
		ops.put(">=",new GreaterEqualOp());

		ops.put("==",new EqualOp());
		ops.put("!=",new NotEqualOp());
		ops.put("is",new JEqualOp());
		ops.put("instanceof",new JInstanceOfOp());

		ops.put("&&",new AndOp());
		ops.put("||",new OrOp());
		ops.put("!",new NotOp());

		ops.put("=",new AssignOp());
		ops.put(":=",new DefAssignOp());
		ops.put("+=",new AddAssignOp());
		ops.put("-=",new SubAssignOp());
		ops.put("*=",new MulAssignOp());
		ops.put("/=",new DivAssignOp());
		ops.put("%=",new ModAssignOp());

		ops.put(",",new CommaOp());
		ops.put("(",new LeftParenthesis());
		ops.put(")",new RightParenthesis());
		ops.put("[",new LeftBracket());
		ops.put("]",new RightBracket());
		
		ops.put("while",new WhileOp());
		ops.put("try",new TryOp());
		ops.put("throw",new ThrowOp());
		ops.put("for",new ForOp());
	}
	
	
	static void init(){
		vals=new ValStack();
		vars=new Stack<>();
		opers=new Stack<>();
		defs=new Stack<>();
		name2id=new HashMap<>();
		var_stack=new Object[65536];
		parenthesis_depth=0;
	}
	static int
	NO_TYPE=0,
	ERROR_TYPE=1,
	OPERATOR_TYPE=2,
	TYPE_TYPE=3,
	CONST_TYPE=4,
	VAR_TYPE=5,
	FIELD_TYPE=6,
	METHOD_TYPE=7,
	NOTE_TYPE=8,
	PARENTHESIS_TYPE=9;
	
	static int text_type_2_color[]={
		0xff000000,
		0xffff0000,//err
		0xff008080,//op
		0xff00a0a0,//type
		0xff00a000,//const
		0xff800080,//var
		0xff000080,//field
		0xff0040ff,//method
		0xff808080,//note
		0xff00c060,//parenthesis
		0xff009048,
		0xff006030,
		0xff003018,
	};
	static void setTextType(int l,int r,int type){
		for(int i=l;i<r;++i)script_type[i]=type;
	}
	public static token[] getTokens(char[] s)throws Exception{
		ArrayList<token>ss=new ArrayList<>();
		script_src=s;
		script_type=new int[s.length];
		lrs=new ArrayList<>();
		for(int p=0,p0=0;p<s.length;)try{
			for(;p<s.length&&Character.isWhitespace(s[p]);++p);
			if(p==s.length)break;
			p0=p;
			if(s[p]=='/'){
				if(s[p+1]=='/'){
					//debug.Log.i("//...");
					for(p+=2;s[p]!='\n';++p);
					setTextType(p0,p,NOTE_TYPE);
					continue;
				}else if(s[p+1]=='*'){
					//debug.Log.i("/*...*/");
					int c=1;
					for(p+=2;;++p){
						if(p+1>=s.length){
							throw new Exception("\"*/\" not found");
						}
						if(s[p]=='*'&&s[p+1]=='/'){
							++p;
							if(0==--c)break;
						}
						if(s[p]=='/'&&s[p+1]=='*'){
							++p;
							++c;
						}
					}
					++p;
					setTextType(p0,p,NOTE_TYPE);
					continue;
				}
			}
			if(s[p]=='_'||s[p]=='$'||s[p]>='a'&&s[p]<='z'||s[p]>='A'&&s[p]<='Z'){
				for(++p;s[p]=='_'||s[p]=='$'||s[p]>='a'&&s[p]<='z'||s[p]>='A'&&s[p]<='Z'||s[p]>='0'&&s[p]<='9';++p);
				ss.add(NameToken.make(new String(s,p0,p-p0)));	
			}else if(s[p]>='0'&&s[p]<='9'){
				boolean isInt=true;
				for(++p;s[p]>='0'&&s[p]<='9';++p);
				if(s[p]=='.'){
					isInt=false;
					for(++p;s[p]>='0'&&s[p]<='9';++p);
				}
				if(s[p]=='e'){
					isInt=false;
					++p;
					if(s[p]=='+'||s[p]=='-')++p;
					if(!(s[p]>='0'&&s[p]<='9'))throw new Exception();
					for(;s[p]>='0'&&s[p]<='9';++p);
				}
				String s1=new String(s,p0,p-p0);
				if(isInt)ss.add(new ValueToken(Integer.valueOf(s1),int.class));
				else ss.add(new ValueToken(Double.valueOf(s1),double.class));
			}else if(s[p]=='"'){
				StringBuffer sb=new StringBuffer();
				for(++p;s[p]!='"';++p){
					//if(s[p]=='\n')throw new Exception("invalid newline in string");
					if(s[p]=='\\'){
						++p;
						if(s[p]!='\n')sb.append(s[p]=='n'?'\n':s[p]);
					}else sb.append(s[p]);
				}
				ss.add(new ValueToken(sb.toString(),String.class));
				++p;
			}else{
				String s1=new String(s,p0,1);
				String s2=new String(s,p0,2);
				if(ops.containsKey(s2)){
					p+=2;
					ss.add(ops.get(s2).clone(ss.size()));
				}else if(ops.containsKey(s1)){
					p+=1;
					ss.add(ops.get(s1).clone(ss.size()));
				}else throw new Exception("invalid operator:"+s1);
				setTextType(p0,p,OPERATOR_TYPE);
			}
			lrs.add(p0);lrs.add(p-1);
			//debug.Log.i(ss.size()-1+":{"+new String(s,p0,p-p0)+"}");
		}catch(Exception e){
			throw new ScriptException(p0,p,0);
		}
		token[] sss=new token[ss.size()];
		return ss.toArray(sss);
	}
	static ArrayList<Integer>token_pos;
	public static void printCompileStack(){
		debug.Log.i("\nCompile Stack--------------------------\n");
		debug.Log.i("vals:");
		for(Expr v:vals){
			int l=lrs.get(v.LEFT*2);
			int r=lrs.get(v.RIGHT*2+1);
			debug.Log.i(v.getClass().getSimpleName()+":["+v.LEFT+","+v.RIGHT+"]{"+new String(script_src,l,r-l+1)+"}");
		}
		debug.Log.i("\nopers:");
		for(operator o:opers){
			int l=lrs.get(o.LEFT*2);
			int r=lrs.get(o.RIGHT*2+1);
			debug.Log.i(o.getClass().getSimpleName()+":["+o.LEFT+","+o.RIGHT+"]{"+new String(script_src,l,r-l+1)+"}");
		}
		debug.Log.i("\ndefs:");
		for(operator o:defs){
			int l=lrs.get(o.LEFT*2);
			int r=lrs.get(o.RIGHT*2+1);
			debug.Log.i(o.getClass().getSimpleName()+":["+o.LEFT+","+o.RIGHT+"]{"+new String(script_src,l,r-l+1)+"}");
		}
		debug.Log.i("\n--------------------------\n");
	}
	public static void printRunStack(){
		for(int i=0;i<10;++i){
			debug.Log.i(i+":"+var_stack[i]);
		}
	}
	public static void run(){
		rvalue r=null;
		boolean finished=false;
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("(\n");
			util.SerializeUtil.readStringFromFile(sb,debug.Log.log_path+"script.txt");
			sb.append("\n)\n");
			debug.Log.showTime(false);
			r=compile(sb.toString());
			//debug.Log.i(debug.ObjectInfo.obj2str_deep(r));
			//new ObjectNode(r,r.getClass()).show();
			init();
			r.getValue();
			finished=true;
		}catch(ScriptException e){
			debug.Log.i(e.info);
			if(e.e!=null)debug.Log.i(e.e);
			debug.Log.i(e);
			if(r==null)printCompileStack();
			else printRunStack();
			debug.Log.i("\nsource:");
			debug.Log.i(new String(script_src,0,e.L)+"###{"+new String(script_src,e.L,e.R-e.L+1)+"}###"+new String(script_src,e.R+1,script_src.length-e.R-1));
		}catch(Exception e){
			debug.Log.i(e);
			if(r==null)printCompileStack();
			else printRunStack();
		}
		if(!finished)com.ccz.blocks.MainActivity.showText(r==null?"Compile Error":"Runtime Error");
		debug.Log.showTime(true);
	}
	static int parenthesis_depth;
	public static rvalue compile(String s0)throws Exception{
		init();
		//debug.Log.i(s0);
		token[] ts=getTokens(s0.toCharArray());
		for(int i=0;i<ts.length;++i){
			ts[i].push(i);
			//printCompileStack();
		}
		if(!opers.empty()){
			operator o=opers.peek();
			throw new ScriptException(o.LEFT,o.RIGHT);
		}
		if(vals.empty())throw new ScriptException(0,ts.length-1);
		if(vals.size()>1){
			Expr v=vals.get(0);
			throw new ScriptException(v.RIGHT,v.RIGHT);
		}
		return vals.get(0).toRValue();
	}
	static Class[] getTypes(rvalue args[])throws ScriptException{
		Class tps[]=new Class[args.length];
		for(int i=0;i<tps.length;++i){
			tps[i]=args[i].getType();
		}
		return tps;
	}
	static Object[] getValues(rvalue args[])throws ScriptException{
		Object objs[]=new Object[args.length];
		for(int i=0;i<objs.length;++i)objs[i]=args[i].getValue();
		return objs;
	}
	static int getNumberTypeId(rvalue v)throws ScriptException{
		Class t=v.getType();
		Integer id=levels.get(t);
		if(id==null)throw new ScriptException(v.LEFT,v.RIGHT,"invalid type for arithmetic operation");
		return id;
	}
	static boolean sameType(Class a,Class b){
		Class a1=obj2basic.get(a),b1=obj2basic.get(b);
		if(a1!=null)a=a1;
		if(b1!=null)b=b1;
		return a==b;
	}
	static int numberUpperCast(rvalue v[])throws ScriptException{
		int lv[]=new int[2],mx;
		for(int i=0;i<2;++i)lv[i]=getNumberTypeId(v[i]);
		mx=Math.max(lv[0],lv[1]);
		for(int i=0;i<2;++i)if(lv[i]<mx){
			v[i]=new TransType(v[i],lv[i],mx);
		}
		return mx;
	}
}


class ValStack implements Iterable{
	Stack<Expr> stk=new Stack<>();
	public Expr pop(){
		return stk.pop();
	}
	public Expr peek(){
		return stk.peek();
	}
	public boolean empty(){return stk.empty();}
	public int size(){return stk.size();}
	public Expr get(int x){return stk.get(x);}
	public void push(Expr e)throws ScriptException{
		if(!stk.empty()&&stk.peek().RIGHT+1==e.LEFT&&(opers.empty()||opers.peek().RIGHT+1!=e.LEFT)){
			new CommaOp(e.LEFT,e.LEFT-1).push(e.LEFT);
		}
		stk.push(e);
	}
	public Iterator iterator(){
		return stk.iterator();
	}
}
class ScriptException extends Exception{
	final Exception e;
	final int L,R;
	final String info;
	ScriptException(int _L,int _R,int _){L=_L;R=_R;e=null;info="";}
	ScriptException(int _L,int _R){this(lrs.get(_L*2),lrs.get(_R*2+1),0);e=null;}
	ScriptException(int _L,int _R,Exception _e){this(_L,_R);e=_e;}
	ScriptException(int _L,int _R,String _s){this(_L,_R);info=_s;}
}


interface token{
	public void push(int pos)throws Exception;
}
class NameToken implements token{
	String s;
	public static token make(String s){
		if(s.equals("true"))return new ValueToken(true,boolean.class);
		if(s.equals("false"))return new ValueToken(false,boolean.class);
		if(s.equals("null"))return new ValueToken(null,null);
		NameToken a=new NameToken();
		a.s=s;
		return a;
	}
	public void push(int p)throws ScriptException{
		operator o=ops.get(s);
		if(o!=null){
			if(o.isL1()){
				vals.push(new Name(s,p,p));
				vals.pop();
			}
			opers.push(o.clone(p));
			setTextType(lrs.get(p*2),lrs.get(p*2+1)+1,OPERATOR_TYPE);
		}else vals.push(new Name(s,p,p));
	}
}
class ValueToken implements token{
	final Object val;
	final Class tp;
	ValueToken(Object obj,Class _tp){
		val=obj;
		tp=_tp;
	}
	public void push(int p)throws ScriptException{
		if(val==null&&tp==null)vals.push(new JNull(p,p));
		else vals.push(new JConst(val,tp,p));
	}
}



