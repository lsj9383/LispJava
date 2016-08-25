package LispInterpreter;

import java.util.*;

public class Interpreter {
	private static String[] PrimitiveVars = {"=", "<", "+", "-", "*", "/"};
	private static Data[] PrimitiveVals = {	Equ.Single(),
											Less.Single(),
											Add.Single(), 
											Sub.Single(), 
											Mul.Single(), 
											Div.Single()};
	
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
		
		result = Eval(new Express("(define a 3)"), global_env);
		Show(result);
		result = Eval(new Express("(define b (* 3 (+ 2 2) 0.5))"), global_env);
		Show(result);
		result = Eval(new Express("a"), global_env);
		Show(result);
		result = Eval(new Express("b"), global_env);
		Show(result);
		result = Eval(new Express("(+ a b)"), global_env);
		Show(result);
		result = Eval(new Express("(define (fun a b) (+ a b))"), global_env);
		Show(result);
		result = Eval(new Express("(fun 4 3)"), global_env);
		Show(result);
		
		result = Eval(new Express("(= 1 2)"), global_env);
		Show(result);
		result = Eval(new Express("(= 1 1 1 1)"), global_env);
		Show(result);
		result = Eval(new Express("(if (= 3 3) (+ 2 3) (- 2 3))"), global_env);
		Show(result);
		result = Eval(new Express("(< 10 8)"), global_env);
		Show(result);
		result = Eval(new Express("(< 7 8 9)"), global_env);
		Show(result);
		
		result = Eval(new Express("(define (abs x) (if (< x 0) (- 0 x) x))"), global_env);
		Show(result);
		result = Eval(new Express("(abs 10)"), global_env);
		Show(result);
		result = Eval(new Express("(abs 10)"), global_env);
		Show(result);
		result = Eval(new Express("(define one_num 10)"), global_env);
		Show(result);
		result = Eval(new Express("(set! one_num 20)"), global_env);
		Show(result);
		result = Eval(new Express("(+ 1 one_num)"), global_env);
		Show(result);
		result = Eval(new Express("(define withdraw ((lambda (balance) (lambda (x) (set! balance (- balance x)) balance)) 100))"), global_env);
		Show(result);
		result = Eval(new Express("(withdraw 10)"), global_env);
		Show(result);
		result = Eval(new Express("(withdraw 10)"), global_env);
		Show(result);
		result = Eval(new Express("(withdraw 30)"), global_env);
		Show(result);
	}
	
	static Data Eval(Express exp, Environment env){
		switch(exp.Type()){
		case NUMBER:		return new Data(Double.valueOf(exp.GetSubExps().get(0)));
		case VARIABLE:		return env.lookup_variable_value(exp.GetSubExps().get(0));
		case ASSIGNMENT:	EvalAssignment(exp, env);		return null;
		case DEFINITION:	EvalDefinition(exp, env);		return null;
		case IF:			return EvalIf(exp, env);
		case LAMBDA:		return new Procedure(Lambda.Variables(exp), Lambda.Body(exp), env );
		case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
		default:			return null;
		}
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
		if(data == null){
			s += "OK\n";
		}
		else{
			if(data.Type() == DataType.NUMBER){
				s += data.GetNumber() + "\n";
			}
			else if(data.Type() == DataType.STRING){
				s += data.GetNumber() + "\n";
			}
			else if(data.Type() == DataType.SYMBOL){
				s += data.GetNumber() + "\n";
			}
			else if(data.Type() == DataType.BOOLEAN){
				s += "#"+data.GetBoolean() + "\n";
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
	
	/* 对定义求值，也就是在环境中添加约束 */
	static void EvalDefinition(Express exp, Environment env){
		env.define_variable(Definition.Variable(exp), Eval(Definition.Value(exp), env));
	}
	
	/* 对赋值求值，也就是在环境中修改约束 */
	static void EvalAssignment(Express exp, Environment env){
		env.set_variable_value(	exp.GetSubExps().get(2),
								Eval(new Express(exp.GetSubExps().get(3)),env));
	}
	
	/* 对条件表达式求值  */
	static Data EvalIf(Express exp, Environment env){
		Data pred = Eval(new Express(exp.GetSubExps().get(2)), env);
		
		if(pred.Type() == DataType.BOOLEAN){
			if(pred.GetBoolean()){
				return Eval(new Express(exp.GetSubExps().get(3)), env);
			}
			else{
				return Eval(new Express(exp.GetSubExps().get(4)), env);
			}
		}
		else{
			System.out.println("error : EvalIf, pred is not boolean express");
			System.exit(0);
			return null;
		}
	}
}