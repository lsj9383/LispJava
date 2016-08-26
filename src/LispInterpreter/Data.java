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

public class Data {
	protected DataType type = DataType.NULL;
	protected String context_symbol = null;
	protected double context_number = 0;
	protected Data first=null;
	protected Data second=null;
	
	@Override
	public String toString(){
		String s = "";
		switch(type){
		case NULL:
			s += "error : Data-toString , data display NULL";
			break;
		case NUMBER:
			s+=context_number;
			break;
		case SYMBOL:
		case STRING:
			s+=context_symbol;
			break;
		case PROCEDURE:
			s+="PROCEDURE";
			break;
		case PRIMITIVE:
			s+="PRIMITIVE";
			break;
		case BOOLEAN:
			s+=GetBoolean();
			break;
		case CONS:
			s+="{";
			if(first==null){
				s+="()";
			}
			else{
				s+=first.toString();
			}
			s+=", ";
			
			if(second==null){
				s+="()";
			}
			else{
				s+=second.toString();
			}
			s+="}";
			break;
		default:
			s += "error : Data-toString, data type is wrong";	
		}
		return s;
	}
	
	public Data(){}
	public Data(int context){
		context_number = context;
		type = DataType.NUMBER;
	}
	
	public Data(double context){
		context_number = context;
		type = DataType.NUMBER;
	}
	
	public Data(boolean t){
		context_number = t ? 1 : 0;
		type = DataType.BOOLEAN;
	}
	
	public Data(String context){
		if(context.charAt(0) == '"' && context.charAt(context.length()-1) == '"'){
			type = DataType.STRING;
		}
		else{
			type = DataType.SYMBOL;
		}
		context_symbol = context;
	}
	
	public Data(Data aFirst, Data aSecond){
		type = DataType.CONS;
		first = aFirst;
		second = aSecond;
	}
	
	public double GetNumber(){
		return this.context_number;
	}
	
	public String GetString(){
		return this.context_symbol;
	}
	
	public String GetSymbol(){
		return this.context_symbol;
	}
	
	public boolean GetBoolean(){
		return (context_number != 0);
	}
	
	public Data GetFirst(){
		return first;
	}
	
	public Data GetSecond(){
		return second;
	}
	
	
	public DataType Type(){return type;}
}

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
}
