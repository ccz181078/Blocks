package util;

public final class IntArrayList{
	public int a[];
	public int n,p;
	IntArrayList(){
		a=new int[1];
		n=p=0;
	}
	public final int next(){
		return a[p++];
	}
	public final void push(int x){
		if(n==a.length){
			int[] b=new int[n<<1];
			System.arraycopy(a,0,b,0,n);
			a=b;
		}
		a[n++]=x;
	}
}
