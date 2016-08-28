package LispInterpreter;

import java.util.ArrayList;

enum DataType{
	NULL,
	NUMBER,
	STRING,
	SYMBOL,
	BOOLEAN,
	CONS,
	PRIMITIVE,
	PROCEDURE
}

class Pair{
	private Data first=null;
	private Data second=null;
	
	public Pair(Data aFirst, Data aSecond){
		first = aFirst;
		second = aSecond;
	}
	
	public Data First(){
		return first;
	}
	
	public Data Second(){
		return second;
	}
	
	public void SetFirst(Data aFirst){
		first = aFirst;
	}
	
	public void SetSecond(Data aSecond){
		second = aSecond;
	}
}

/* 抽象数据类 */
abstract public class Data {
	protected DataType type = DataType.NULL;
	
	public Data(){}
	public DataType Type(){return type;}
	public Object GetContent(){return "PRIMITIVE";};
}

/***********number*************/
class NumberData extends Data{
	Double content;
	
	public NumberData(double num){
		content = num;
		type = DataType.NUMBER;
	}
	
	@Override
	public Object GetContent(){
		return content;
	}
	
	@Override
	public String toString(){
		return new String()+content;
	}
}

/***********Bool*************/
class BooleanData extends Data{
	Boolean content;
	
	public BooleanData(boolean bo){
		content = bo;
		type = DataType.BOOLEAN;
	}
	
	@Override
	public Object GetContent(){
		return content;
	}
	
	@Override
	public String toString(){
		return ""+content;
	}
}

/***********Cons*************/
class ConsData extends Data{
	Pair content;
	
	public ConsData(Data aFirst, Data aSecond){
		content = new Pair(aFirst, aSecond);
		type = DataType.CONS;
	}
	
	@Override
	public Object GetContent(){
		return content;
	}
	
	@Override
	public String toString(){
		String s="";
		s+="{";
		if(content.First()==null){
			s+="()";
		}
		else{
			s+=content.First().toString();
		}
		s+=", ";
		
		if(content.Second()==null){
			s+="()";
		}
		else{
			s+=content.Second().toString();
		}
		s+="}";
		return s;
	}
}


/*********procedure********/
class Procedure extends Data{
	ArrayList<String> vars = null;
	private ArrayList<Express> body = null;
	private Environment env = null;
	
	public Procedure(ArrayList<String> aVars, ArrayList<Express> aBody, Environment aEnv){
		type = DataType.PROCEDURE;
		vars = aVars;
		body = aBody;
		env = aEnv;
	}
	
	public ArrayList<String> Variables(){
		return vars;
	}
	
	public ArrayList<Express> Body(){
		return body;
	}
	
	public Environment Env(){
		return env;
	}
	
	@Override
	public Object GetContent(){
		return null;
	}
	
	@Override
	public String toString(){
		return "PROCODURE";
	}
}
