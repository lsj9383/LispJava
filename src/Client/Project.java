package Client;

import LispInterpreter.*;

public class Project {
	static void TestExpress(){
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
		System.out.println(new Express("(or (= 3 4) (= 3 2) (= 3 1))"));
	}
	
	
	static void Run(){
		Interpreter.DriverLoop();
	}
	
	public static void main(String[] args){
//		TestExpress();
		Run();
		System.out.println("project done!");
	}
}
