package LispInterpreter;

import java.io.*;
import java.util.*;

public class Interpreter {
	private static String[] PrimitiveVars = 
		{"=", "<", ">", "+", "-", "*", "/", "cons", "car", "cdr", "list", "null", "null?"};
	
	private static Data[]   PrimitiveVals = {
									Equ.Single(),
									Less.Single(),
									Great.Single(),
									Add.Single(), 
									Sub.Single(), 
									Mul.Single(), 
									Div.Single(),
									Cons.Single(),
									Car.Single(),
									Cdr.Single(),
									List.Single(),
									null,
									isNull.Single()};
	
	/* ��ֵѭ�� */
	public static void DriverLoop(){
		Frame InitialFrame = 
				new Frame(	new ArrayList<String>(Arrays.asList(PrimitiveVars)),
							new ArrayList<Data>(Arrays.asList(PrimitiveVals)));
		
		Environment global_env =new Environment(InitialFrame, null);
		
		Display.Welcome();
		while(true){
			Scanner stdin = new Scanner(System.in);
			String sExp = null;
			/*1). ������Ч�ִ�*/
			do{
				System.out.print("Eval Input : ");
				sExp = stdin.nextLine();
			}while(sExp.equals(""));
			
			/*��ֵ������ʾ*/
			Display.Show(Eval(new Express(sExp), global_env));
			System.out.println("");
		}
	}
	/*****************************eval-apply����ѭ��***************************************/
	static Data Eval(Express exp, Environment env){
		switch(exp.Type()){
		case NUMBER:		return new Data(Double.valueOf(exp.GetSubExps().get(0)));
		case VARIABLE:		return env.lookup_variable_value(exp.GetSubExps().get(0));
		case ASSIGNMENT:	EvalAssignment(exp, env);		return null;	/* operation without data */
		case DEFINITION:	EvalDefinition(exp, env);		return null;	/* operation without data */
		case IF:			return EvalIf(exp, env);
		case OR:			return EvalOr(exp, env);
		case AND:			return EvalAnd(exp, env);
		case LAMBDA:		return new Procedure(Lambda.Variables(exp), Lambda.Body(exp), env );
		case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
		default:			return null;
		}
	}
	
	static Data Apply(Data procedure, ArrayList<Data> args){
		if (procedure.Type() == DataType.PRIMITIVE){ /*��������*/
			return ((Primitive)procedure).Call(args);
		}
		else if (procedure.Type() == DataType.PROCEDURE){
			/* ���»����¶�procedure��body˳����ֵ */
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
	
	/*ȡ�����Ϲ��̵Ĳ�����*/
	static Express operator(Express exp){
		return new Express( exp.GetSubExps().get(1) );
	}
	
	/*ȡ�����Ϲ��̵Ĳ�����*/
	static ArrayList<Express> operands(Express exp){
		ArrayList<Express> exps = new ArrayList<Express>();
		
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			exps.add(new Express(exp.GetSubExps().get(i)));
		}
		
		return exps;
	}
	
	/*�����ʽ���Ա��ʽ˳����ֵ*/
	static ArrayList<Data> ListOfValues(ArrayList<Express> exps, Environment env){
		ArrayList<Data> vals = new ArrayList<Data>();
		for(int i=0; i<exps.size(); i++){
			vals.add(Eval(exps.get(i), env));
		}
		return vals;
	}
	
	/*�Ը���ʽ��body˳����ֵ*/
	static Data EvalSequence(ArrayList<Express> exps, Environment env){
		for(int i=0; i<exps.size()-1; i++){
			Eval(exps.get(i), env);
		}
		
		return Eval(exps.get(exps.size()-1), env);
	}
	
	/* �Զ�����ֵ��Ҳ�����ڻ��������Լ�� */
	static void EvalDefinition(Express exp, Environment env){
		env.define_variable(Definition.Variable(exp), Eval(Definition.Value(exp), env));
	}
	
	/* �Ը�ֵ��ֵ��Ҳ�����ڻ������޸�Լ�� */
	static void EvalAssignment(Express exp, Environment env){
		env.set_variable_value(	exp.GetSubExps().get(2),
								Eval(new Express(exp.GetSubExps().get(3)),env));
	}
	
	/* ���������ʽ��ֵ  */
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
	
	/* ��OR���ʽ��ֵ  */
	static Data EvalOr(Express exp, Environment env){
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			Data pred = Eval(new Express((exp.GetSubExps().get(i))), env);
			if(pred.Type()!=DataType.BOOLEAN){
				System.out.println("error : EvalOr , exps contain not true or false");
				System.exit(0);
				return null;
			}
			else{
				if( pred.GetBoolean() ){
					return new Data(true);
				}
			}
		}
		
		return new Data(false);
	}
	
	/* ��AND���ʽ��ֵ  */
	static Data EvalAnd(Express exp, Environment env){
		for(int i=2; i<exp.GetSubExps().size()-1; i++){
			Data pred = Eval(new Express((exp.GetSubExps().get(i))), env);
			if(pred.Type()!=DataType.BOOLEAN){
				System.out.println("error : EvalOr , exps contain not true or false");
				System.exit(0);
				return null;
			}
			else{
				if( !pred.GetBoolean() ){
					return new Data(false);
				}
			}
		}
		return new Data(true);
	}
}