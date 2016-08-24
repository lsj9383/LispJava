package LispInterpreter;

import java.util.*;

/* 
 * 基本过程:
 * +,  add
 * -,  sub
 * *,  mul
 * \/, div
 * 
 *  */
interface Primitive{
	Data Call(ArrayList<Data> args);
}

/* + */
class Add extends Data implements Primitive{
	private static Add obj = null;
	
	private Add(){
		type = DataType.PRIMITIVE;
	}
	
	static Add Single(){
		if(obj == null){
			obj = new Add();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = 0;
		
		for(int i=0; i<args.size(); i++){
			dat += args.get(i).GetNumber();
		}
		
		return new Data(dat);
	}
}

/* - */
class Sub extends Data implements Primitive{
	private static Sub obj = null;
	
	private Sub(){
		type = DataType.PRIMITIVE;
	}
	
	static Sub Single(){
		if(obj == null){
			obj = new Sub();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = args.get(0).GetNumber();
		
		for(int i=1; i<args.size(); i++){
			dat -= args.get(i).GetNumber();
		}
		
		return new Data(dat);
	}
}

/* * */
class Mul extends Data implements Primitive{
	private static Mul obj = null;
	
	private Mul(){
		type = DataType.PRIMITIVE;
	}
	
	static Mul Single(){
		if(obj == null){
			obj = new Mul();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = 1;
		
		for(int i=0; i<args.size(); i++){
			dat *= args.get(i).GetNumber();
		}
		
		return new Data(dat);
	}
}

/* \/ */
class Div extends Data implements Primitive{
	private static Div obj = null;
	
	private Div(){
		type = DataType.PRIMITIVE;
	}
	
	static Div Single(){
		if(obj == null){
			obj = new Div();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = args.get(0).GetNumber();
		
		for(int i=1; i<args.size(); i++){
			dat /= args.get(i).GetNumber();
		}
		
		return new Data(dat);
	}
}