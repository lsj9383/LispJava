package Client;

import LispInterpreter.*;

public class Project {
	static void TestExpress(){
		Express exp = new Express("(* (+ 2 3) (- 3 4) (f))");
		new Express("(f)");
		new Express("(if (= a 0) (- 3 4) (f))");
		new Express("\"3\"");
		System.out.println(new Express("3"));
		System.out.println(new Express("a"));
		System.out.println(new Express("((lambda (x) (+ x 1)) 3)"));
		System.out.println(new Express("(lambda (x) (+ x 1))"));
		System.out.println(new Express("(lambda (a b ) (+ x 1 ))"));
		System.out.println(new Express("+10"));
		System.out.println(new Express("-10"));
		System.out.println(new Express("10"));
		System.out.println(new Express("(set! a 3)"));
	}
	
	static void TestEnvironment(){
		Interpreter.DriverLoop();
	}
	
	static void TestEval(){
		Interpreter.DriverLoop();
	}
	
	public static void main(String[] args){
//		TestExpress();
//		TestEnvironment();
		TestEval();
		System.out.println("project done!");
	}
}
