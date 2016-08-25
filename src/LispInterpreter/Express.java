package LispInterpreter;

import java.util.*;

enum ExpressType{
	NULL,
	NUMBER,
	STRING,
	VARIABLE,
	QUOTED,
	ASSIGNMENT,
	DEFINITION,
	IF,
	LAMBDA,
	BEGIN,
	COND,
	APPLICATION}

public class Express {
	private ArrayList<String> subexps = new ArrayList<String>();
	private ExpressType type = ExpressType.NULL;
	
	@Override
	public String toString(){
		String s = "";
		s += type + "\n";
		s += subexps;
		return s;
	}
	public Express(String s){
		Split(s);		//将s按规则分割, 每一个子表达式都添加到datas中
		UpdateType();	//根据得到的表达式设置该表达式类型
		//check
//		System.out.println(type);
//		System.out.println(subexps);
	}
	public ArrayList<String> GetSubExps(){
		return subexps;
	}
	
	public ExpressType Type(){
		return type;
	}
	
	private void UpdateType(){
		String sub = subexps.get(0);
		
		if(subexps.size()==1){
			if(sub.charAt(0)=='\"'){
				type = ExpressType.STRING;
			}
			else if(sub.matches(("^[+-]?\\d+\\.{0,1}\\d*"))){
				type = ExpressType.NUMBER;
			}
			else{
				type = ExpressType.VARIABLE;
			}
		}
		else{
			String FirstWord = subexps.get(1);
			if(FirstWord.equals("quoted")){
				type=ExpressType.QUOTED;
			}
			else if(FirstWord.equals("set!")){
				type=ExpressType.ASSIGNMENT;
			}
			else if(FirstWord.equals("define")){
				type=ExpressType.DEFINITION;
			}
			else if(FirstWord.equals("if")){
				type=ExpressType.IF;
			}
			else if(FirstWord.equals("lambda")){
				type=ExpressType.LAMBDA;
			}
			else if(FirstWord.equals("begin")){
				type=ExpressType.BEGIN;
			}
			else if(FirstWord.equals("cond")){
				type=ExpressType.COND;
			}
			else{
				type=ExpressType.APPLICATION;
			}
		}
	}
	
	private void Split(String s){
		Scanner stdin;
		if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
			stdin = new Scanner(s.substring(1, s.length()-1));
			subexps.add("(");
		}
		else{
			stdin = new Scanner(s);
		}
		
		while(stdin.hasNext())
		{
			String new_s = stdin.next();
			int bs = LeftBracketSize(new_s);
			while(bs!=0)
			{
				new_s += " "+stdin.next();
				bs = LeftBracketSize(new_s);
			}
			subexps.add(new_s);
		}
		
		if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
			subexps.add(")");
		}
	}
	
	private int LeftBracketSize(String s){
		int res = 0;
		
		for(int i=0; i<s.length(); i++){
			if( s.charAt(i) == '('){
				res++;
			}
			else if(s.charAt(i) == ')'){
				res--;
			}
		}
		
		return res;
	}
}