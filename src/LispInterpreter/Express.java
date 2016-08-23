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
	ArrayList<String> subexps = new ArrayList<String>();
	ExpressType type = ExpressType.NULL;
	
	public Express(String s){
		Split(s);		//将s按规则分割, 每一个子表达式都添加到datas中
		UpdateType();	//根据得到的表达式设置该表达式类型
		//check
		System.out.println(type);
		System.out.println(subexps);
	}
	private void UpdateType(){
		String sub = subexps.get(0);

		if(subexps.size()==1){
			if(sub.charAt(0)=='\"'){
				type = ExpressType.STRING;
			}
			else if(sub.charAt(0)=='('){
				type = ExpressType.APPLICATION;
			}
			else if(sub.matches(("\\d+\\.{0,1}\\d*"))){
				type = ExpressType.NUMBER;
			}
			else{
				type = ExpressType.VARIABLE;
			}
		}
		else{
			switch(sub.substring(1))
			{
			case "quoted":
				type=ExpressType.QUOTED;
				break;
			case "set!":
				type=ExpressType.ASSIGNMENT;
				break;
			case "define":
				type=ExpressType.DEFINITION;
				break;
			case "if":
				type=ExpressType.IF;
				break;
			case "lambda":
				type=ExpressType.LAMBDA;
				break;
			case "begin":
				type=ExpressType.BEGIN;
				break;
			case "cond":
				type=ExpressType.COND;
				break;
			default:
				type=ExpressType.APPLICATION;
				break;
			}
		}
	}
	
	private void Split(String s){
		Scanner stdin;
		if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
			stdin = new Scanner(s.substring(1, s.length()-1));
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
			subexps.set(0, "("+subexps.get(0));
			subexps.set(subexps.size()-1, subexps.get(subexps.size()-1)+")");
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