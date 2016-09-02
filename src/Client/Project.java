package Client;

import LispInterpreter.*;

public class Project {
	static void Run(){
		Interpreter.DriverLoop();
//		System.out.println(10.4%3);
	}
	
	public static void main(String[] args){
		Run();
		System.out.println("project done!");
	}
}
