package LispInterpreter;

import java.util.ArrayList;

class Lambda {
	
	/* 提取lambda表达式中的变量符号 */
	static public ArrayList<String> Variables(Express exp){
		ArrayList<String> vars = new ArrayList<String>();
		Express exp_vars = new Express(exp.GetSubExps().get(2));
		for(int i=1; i<exp_vars.GetSubExps().size()-1; i++){
			vars.add(exp_vars.GetSubExps().get(i));
		}
		return vars;
	}
	
	/* 提取lambda表达式中的表达式集 */
	static public ArrayList<Express> Body(Express exp){
		ArrayList<Express> exps = new ArrayList<Express>();
	
		for(int i=3; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		return exps;
	}
	
	/* 组成lambda表达式 */
	static Express make(ArrayList<String> Vars, ArrayList<String> sExps){
		String s = "(lambda (";
		
		for(int i=0; i<Vars.size(); i++){
			s+=Vars.get(i)+" ";
		}
		s+=")";
		
		for(int i=0; i<sExps.size(); i++){
			s+=" "+sExps.get(i);
		}
		
		s+=")";
		return new Express(s);
	}
}
