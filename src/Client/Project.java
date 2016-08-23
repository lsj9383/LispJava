package Client;

import LispInterpreter.*;

public class Project {
	public static void main(String[] args){
		Express exp = new Express("(* (+ 2 3) (- 3 4) (f))");
		new Express("(f)");
		new Express("(if (= a 0) (- 3 4) (f))");
		new Express("\"3\"");
		new Express("3");
		new Express("a");

		System.out.println("project done!");
	}
}
