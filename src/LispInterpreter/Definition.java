package LispInterpreter;

import java.util.ArrayList;

/*
 * 1). (define <var> <exp>)
 * 
 * 2). (define (<var> <var-1>...<var-2>) <body>)
 * 
 * */

class Definition {
	static String Variable(Express exp){
		String buffer = exp.GetSubExps().get(2);
		
		if(buffer.charAt(0) != '('){	/*非函数定义*/
			return buffer;	
		}
		else{
			Express xp = new Express(buffer);
			return xp.GetSubExps().get(1);
		}
	}
	
	static Express Value(Express exp){
		
		if(exp.GetSubExps().get(2).charAt(0) != '('){	/*非函数定义*/
			return new Express(exp.GetSubExps().get(3));	
		}
		else{
			Express xp = new Express(exp.GetSubExps().get(2));
			ArrayList<String> Vars = new ArrayList<String>();
			ArrayList<String> sExps= new ArrayList<String>();
			
			for(int i=2; i<xp.GetSubExps().size()-1; i++){
				Vars.add(xp.GetSubExps().get(i));
			}
			
			for(int i=3; i<exp.GetSubExps().size()-1; i++){
				sExps.add(exp.GetSubExps().get(i));
			}
			
			return Lambda.make(Vars, sExps);
		}
	}
}
