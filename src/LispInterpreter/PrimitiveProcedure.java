package LispInterpreter;

import java.util.*;

/* 
 * ��������:
 * =,  equ
 * +,  add
 * -,  sub
 * *,  mul
 * \/, div
 * 
 *  */
interface Primitive{
	Data Call(ArrayList<Data> args);
}

/* = */
class Equ extends Data implements Primitive{
	private static Equ obj = null;
	
	private Equ(){
		type = DataType.PRIMITIVE;
	}
	
	static Equ Single(){
		if(obj == null){
			obj = new Equ();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				if(args.get(i).GetNumber()!=args.get(i-1).GetNumber()){
					return new Data(false);
				}
			}
		}
		return new Data(true);
	}
}

/* < */
class Less extends Data implements Primitive{
	private static Less obj = null;
	
	private Less(){
		type = DataType.PRIMITIVE;
	}
	
	static Less Single(){
		if(obj == null){
			obj = new Less();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				if(args.get(i).GetNumber()<=args.get(i-1).GetNumber()){
					return new Data(false);
				}
			}
		}
		return new Data(true);
	}
}

/* > */
class Great extends Data implements Primitive{
	private static Great obj = null;
	
	private Great(){
		type = DataType.PRIMITIVE;
	}
	
	static Great Single(){
		if(obj == null){
			obj = new Great();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				if(args.get(i).GetNumber()>=args.get(i-1).GetNumber()){
					return new Data(false);
				}
			}
		}
		return new Data(true);
	}
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

/* cons */
class Cons extends Data implements Primitive{
	private static Cons obj = null;
	
	private Cons(){
		type = DataType.PRIMITIVE;
	}
	
	static Cons Single(){
		if(obj == null){
			obj = new Cons();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=2){
			System.out.println("error : Cons , size of args is not two");
		}
		
		return new Data(args.get(0), args.get(1));
	}
}

/* cons */
class Car extends Data implements Primitive{
	private static Car obj = null;
	
	private Car(){
		type = DataType.PRIMITIVE;
	}
	
	static Car Single(){
		if(obj == null){
			obj = new Car();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		
		return args.get(0).GetFirst();
	}
}

/* cons */
class Cdr extends Data implements Primitive{
	private static Cdr obj = null;
	
	private Cdr(){
		type = DataType.PRIMITIVE;
	}
	
	static Cdr Single(){
		if(obj == null){
			obj = new Cdr();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		
		return args.get(0).GetSecond();
	}
}

/* List */
class List extends Data implements Primitive{
	private static List obj = null;
	
	private List(){
		type = DataType.PRIMITIVE;
	}
	
	static List Single(){
		if(obj == null){
			obj = new List();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		return Iter(args, 0);
	}
	
	private Data Iter(ArrayList<Data> args, int cnt){
		if(cnt==args.size()){
			return null;
		}
		else{
			return new Data(args.get(cnt), Iter(args, cnt+1));
		}
	}
}

/* isNull */
class isNull extends Data implements Primitive{
	private static isNull obj = null;
	
	private isNull(){
		type = DataType.PRIMITIVE;
	}
	
	static isNull Single(){
		if(obj == null){
			obj = new isNull();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		
		if (args.get(0) == null){
			return new Data(true);
		}
		else{
			return new Data(false);
		}
	}
}