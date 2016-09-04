package LispInterpreter;

import java.io.*;
import java.util.*;

public class Interpreter {
	
	private static Frame InitialFrame = null;
	private static Environment global_env = null;
	private static Map<String, Data> PrimitiveMap = new TreeMap<>();
	/* 静态初始化，一个工程中最多只能拥有一个解释器 */
	static{
		PrimitiveMap.put("eq?"	, IsEq.Single());
		PrimitiveMap.put("="	, Equ.Single());
		PrimitiveMap.put("<"	, Less.Single());
		PrimitiveMap.put(">"	, Great.Single());
		PrimitiveMap.put("null?", isNull.Single());
		PrimitiveMap.put("pair?", IsPair.Single());
		PrimitiveMap.put("+"	, Add.Single());
		PrimitiveMap.put("-"	, Sub.Single());
		PrimitiveMap.put("*"	, Mul.Single());
		PrimitiveMap.put("/"	, Div.Single());
		PrimitiveMap.put("car"	, Car.Single());
		PrimitiveMap.put("cdr"	, Cdr.Single());
		PrimitiveMap.put("set-car!"	, SetCar.Single());
		PrimitiveMap.put("set-cdr!"	, SetCdr.Single());
		PrimitiveMap.put("remainder", Remainder.Single());
		PrimitiveMap.put("int"		, Integer.Single());
		PrimitiveMap.put("load-image"	, LoadImage.Single());
		PrimitiveMap.put("display-image", DisplayImage.Single());
		PrimitiveMap.put("save-image"	, SaveImage.Single());
		PrimitiveMap.put("cons"			, Cons.Single());
		PrimitiveMap.put("list"			, List.Single());
		PrimitiveMap.put("true"			, new BooleanData(true));
		PrimitiveMap.put("false"		, new BooleanData(false));
		PrimitiveMap.put("nil"			, NullData.Single());
		InitialFrame = new Frame(PrimitiveMap);
		global_env = new Environment(InitialFrame, null);		//初始化全局环境
	}
	
	/* 得到解释器全局环境 */
	public static final Environment GlobalEnv(){
		return global_env;
	} 
	/* 求值循环 */
	public static void DriverLoop(){
		Display.Welcome();
		
		/*读取定义式文件，并执行其中的各个表达式.*/
		Express DefinitionExpress = Display.LoadDefinition();
		if(DefinitionExpress != null){
			for(int i=0; i<DefinitionExpress.GetSubExps().size(); i++){
				Display.Show(Eval(new Express(DefinitionExpress.GetSubExps().get(i)), global_env));
			}
		}
		/*执行解释器*/
		while(true){
			Scanner stdin = new Scanner(System.in);
			String sExp = null;
			//*1). 输入有效字串
			do{
				System.out.print("Eval Input : ");
				sExp = stdin.nextLine();
			}while(sExp.equals(""));
			
			// 2). 求值，并显示
			Display.Show(Eval(new Express(sExp), global_env));
			System.out.println("");
		}
	}
	/*****************************eval-apply基本循环***************************************/
	public static Data Eval(Express exp, Environment env){
		switch(exp.Type()){
		case NUMBER:		return EvalSelf(exp, env);
		case VARIABLE:		return EvalVariable(exp, env);
		case QUOTED:		return EvalQuoted(exp, env);
		case ASSIGNMENT:	return EvalAssignment(exp, env);	/* operation without data and return null*/
		case DEFINITION:	return EvalDefinition(exp, env);	/* operation without data and return null*/
		case IF:			return EvalIf(exp, env);
		case OR:			return EvalOr(exp, env);
		case AND:			return EvalAnd(exp, env);
		case LAMBDA:		return EvalLambda(exp, env);
		case BEGIN:			return EvalBegin(exp, env);
		case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
		default:			return null;
		}
	}
	
	private static Data Apply(Data procedure, ArrayList<Data> args){
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
	/*************************************************************************************/
	
	/*取出复合过程的操作符*/
	private static Express operator(Express exp){
		return new Express( exp.GetSubExps().get(1) );
	}
	
	/*取出符合过程的操作数*/
	private static ArrayList<Express> operands(Express exp){
		ArrayList<Express> exps = new ArrayList<Express>();
		
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		
		return exps;
	}
	
	/*对组合式的自表达式顺序求值*/
	private static ArrayList<Data> ListOfValues(ArrayList<Express> exps, Environment env){
		ArrayList<Data> vals = new ArrayList<Data>();
		for(int i=0; i<exps.size(); i++){
			vals.add(Eval(exps.get(i), env));
		}
		return vals;
	}
	
	/*对复合式的body顺序求值*/
	private static Data EvalSequence(ArrayList<Express> exps, Environment env){
		/* 最后一个表达式因为要返回值，所以单独处理 */
		for(int i=0; i<exps.size()-1; i++){
			Eval(exps.get(i), env);
		}
		
		return Eval(exps.get(exps.size()-1), env);		//返回最后一个表达式的结果
	}
	
	/* 对自求值数据进行求值 */
	private static Data EvalSelf(Express exp, Environment env){
		return new NumberData(Double.valueOf(exp.GetSubExps().get(0)));
	}
	
	/* 对符号数据进行求值 */
	private static Data EvalVariable(Express exp, Environment env){
		return env.lookup_variable_value(exp.GetSubExps().get(0));
	}
	
	/* 对引号数据进行求值 */
	private static Data EvalQuoted(Express exp, Environment env){
		return new QuotedData(exp.GetSubExps().get(2));
	}
	
	/* 对定义求值，也就是在环境中添加约束 */
	private static Data EvalDefinition(Express exp, Environment env){
		env.define_variable(Definition.Variable(exp), Eval(Definition.Value(exp), env));
		return null;
	}
	
	/* 对赋值求值，也就是在环境中修改约束 */
	private static Data EvalAssignment(Express exp, Environment env){
		env.set_variable_value(	exp.GetSubExps().get(2),
								Eval(new Express(exp.GetSubExps().get(3)),env));
		return null;
	}
	
	/* 对条件表达式求值  */
	private static Data EvalIf(Express exp, Environment env){
		Data pred = Eval(new Express(exp.GetSubExps().get(2)), env);
		
		if(pred.Type() == DataType.BOOLEAN){
			if((Boolean)pred.GetContent()){
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
	
	/* 对OR表达式求值  */
	private static Data EvalOr(Express exp, Environment env){
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			Data pred = Eval(new Express((exp.GetSubExps().get(i))), env);
			if(pred.Type()!=DataType.BOOLEAN){
				System.out.println("error : EvalOr , exps contain not true or false");
				System.exit(0);
				return null;
			}
			else{
				if( (Boolean)pred.GetContent() ){
					return new BooleanData(true);
				}
			}
		}
		
		return new BooleanData(false);
	}
	
	/* 对AND表达式求值  */
	private static Data EvalAnd(Express exp, Environment env){
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			Data pred = Eval(new Express((exp.GetSubExps().get(i))), env);
			if(pred.Type()!=DataType.BOOLEAN){
				System.out.println("error : EvalOr , exps contain not true or false");
				System.exit(0);
				return null;
			}
			else{
				if( !(Boolean)pred.GetContent() ){
					return new BooleanData(false);
				}
			}
		}
		return new BooleanData(true);
	}
	
	/* 对lambda表达式求值 */
	private static Data EvalLambda(Express exp, Environment env){
		return new Procedure(Lambda.Variables(exp), Lambda.Body(exp), env );
	}
	
	/* 对begin表达式求值 */
	private static Data EvalBegin(Express exp, Environment env){
		ArrayList<Express> exps = new ArrayList<>();
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		
		return EvalSequence(exps, env);
	}
}