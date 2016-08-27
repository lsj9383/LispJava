package Client;

import LispInterpreter.*;

public class Project {
	static void Run(){
		Interpreter.DriverLoop();
	}
	
	public static void main(String[] args){
		Run();
		System.out.println("project done!");
	}
}
