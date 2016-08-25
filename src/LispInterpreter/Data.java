package LispInterpreter;

import java.util.ArrayList;

enum DataType{
	NULL,
	NUMBER,
	STRING,
	SYMBOL,
	BOOLEAN,
	PRIMITIVE,
	PROCEDURE
}

public class Data {
	protected DataType type = DataType.NULL;
	protected String context_symbol = null;
	protected double context_number = 0;
	
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
