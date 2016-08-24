package LispInterpreter;

import java.util.*;

public class Interpreter {
	private static String[] PrimitiveVars = {"+", "-", "*", "/"};
	private static Data[] PrimitiveVals = {Add.Single(), Sub.Single(), Mul.Single(), Div.Single()};
	
	public static void DriverLoop(){
		Frame InitialFrame = 
				new Frame(	new ArrayList<String>(Arrays.asList(PrimitiveVars)),
							new ArrayList<Data>(Arrays.asList(PrimitiveVals)));
		
		Environment global_env =new Environment(InitialFrame, null);
		System.out.println(global_env);
		Data result = Eval(new Express("2"), global_env);
		Show(result);
		result = Eval(new Express("+"), global_env);
		Show(result);
		result = Eval(new Express("(+ 10 3)"), global_env);
		Show(result);
		result = Eval(new Express("(+ (- 1 20 4) 10 2 3)"), global_env);
		Show(result);
		result = Eval(new Express("(* (- 1 20 4) 10 2 3)"), global_env);
		Show(result);
		result = Eval(new Express("(/ (- 1 20 4) 10 2 3)"), global_env);
		Show(result);
		result = Eval(new Express("((lambda (x) (+ x 1)) 3)"), global_env);
		Show(result);
		result = Eval(new Express("((lambda (x y z) (* x y (+ x z))) 1 2 3)"), global_env);
		Show(result);
	}
	
	static Data Eval(Express exp, Environment env){
		Data result = null;
		
		if(exp.Type() == ExpressType.NUMBER){
			return new Data(Double.valueOf(exp.GetSubExps().get(0)));
		}
		else if (exp.Type() == ExpressType.VARIABLE){
			return env.lookup_variable_value(exp.GetSubExps().get(0));
		}
		else if(exp.Type() == ExpressType.LAMBDA){
			return new Procedure( 	Lambda.Variables(exp),
									Lambda.Body(exp),
									env);
		}
		else if (exp.Type() == ExpressType.APPLICATION){
			ArrayList<Data> vals = ListOfValues(operands( exp ),env);
			return Apply(Eval( operator(exp), env), vals);
		}
		
		return result;
	}
	
	static Data Apply(Data procedure, ArrayList<Data> args){
		if (procedure.Type() == DataType.PRIMITIVE){ /*基础过程*/
			return ((Primitive)procedure).Call(args);
		}
		else if (procedure.Type() == DataType.PROCEDURE){
			/* 在新环境下对procedure的body顺序求值 */
			return EvalSequence(((Procedure)procedure).Body(),  
								((Procedure)procedure).Env().extend_environment(
										((Procedure)procedure).Variables(),
										 args) );
		}
		else{
			System.out.println("error : Apply , procedure is not PROCEDURE");
			System.exit(0);
			return null;
		}
	}
	
	static void Show(Data data){
		String s = new String();
		
		s = "output value : ";
		if(data.Type() == DataType.NUMBER){
			s += data.GetNumber() + "\n";
		}
		else if(data.Type() == DataType.STRING){
			s += data.GetNumber() + "\n";
		}
		else if(data.Type() == DataType.SYMBOL){
			s += data.GetNumber() + "\n";
		}
		else if(data.Type() == DataType.PROCEDURE){
			s += "PROCEDURE\n";
		}
		else if(data.Type() == DataType.PRIMITIVE){
			s += "PROCEDURE\n";
		}
		else if(data.Type() == DataType.NULL){
			s += "error : Show , data display NULL\n";
		}
		else{
			s += "error : Show , data isn't data...\n";
		}
		
		System.out.print(s);
	}
	
	/*取出复合过程的操作符*/
	static Express operator(Express exp){
		return new Express( exp.GetSubExps().get(1) );
	}
	
	/*取出符合过程的操作数*/
	static ArrayList<Express> operands(Express exp){
		ArrayList<Express> exps = new ArrayList<Express>();
		
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		
		return exps;
	}
	
	/*对组合式的自表达式顺序求值*/
	static ArrayList<Data> ListOfValues(ArrayList<Express> exps, Environment env){
		ArrayList<Data> vals = new ArrayList<Data>();
		for(int i=0; i<exps.size(); i++){
			vals.add(Eval(exps.get(i), env));
		}
		return vals;
	}
	
	/*对复合式的body顺序求值*/
	static Data EvalSequence(ArrayList<Express> exps, Environment env){
		for(int i=0; i<exps.size()-1; i++){
			Eval(exps.get(i), env);
		}
		
		return Eval(exps.get(exps.size()-1), env);
	}
}