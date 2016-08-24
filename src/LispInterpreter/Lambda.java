package LispInterpreter;

import java.util.ArrayList;

class Lambda {
	
	static public ArrayList<String> Variables(Express exp){
		ArrayList<String> vars = new ArrayList<String>();
		Express exp_vars = new Express(exp.GetSubExps().get(2));
		for(int i=1; i<exp_vars.GetSubExps().size()-1; i++){
			vars.add(exp_vars.GetSubExps().get(i));
		}
		return vars;
	}
	
	static public ArrayList<Express> Body(Express exp){
		ArrayList<Express> exps = new ArrayList<Express>();
	
		for(int i=3; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		return exps;
	}
}
