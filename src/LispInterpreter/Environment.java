package LispInterpreter;

import java.util.*;

public class Environment {
	private Frame frame=null;
	private Environment out_env=null;

	@Override
	public String toString(){
		String s = new String();
		s += "Frame : \n";
		final ArrayList<String> vars = frame.frame_variables();
		final ArrayList<Data> vals = frame.frame_values();
		
		for(int i=0; i<vars.size(); i++){
			s += vars.get(i) + " : " + vals.get(i) + "\n";
		}
		
		if(out_env != null){
			s += out_env.toString();
		}
		return s;
	}
	//将当前环境做扩充
	public Environment extend_environment(ArrayList<String> vars, 
										ArrayList<Data> vals){
		if(vars.size() != vals.size()){
			System.out.println("vars and vals is not match with size.");
			System.exit(0);
		}
		return new Environment(new Frame(vars, vals), this);
	}
	
	//在当前环境下寻找约束
	public Data lookup_variable_value(String var){
		final ArrayList<String> vars = frame.frame_variables();
		final ArrayList<Data> vals = frame.frame_values();
		
		if (vars != null && vals != null){
			for(int i=0; i<vars.size(); i++){
				if(var.equals(vars.get(i))){
					return vals.get(i);
				}
			}
		}
		
		if(out_env == null){ /* error because no keywords */
			System.out.println("error : lookup_variable_values, no match --- " + var);
			System.exit(0);
			return null;
		}
		else{
			return out_env.lookup_variable_value(var);
		}
	}
	
	//在当前环境下重置约束
	public void set_variable_value(String var, Data new_val){
		ArrayList<String> vars = frame.frame_variables();
		ArrayList<Data> vals = frame.frame_values();
		
		if (vars != null && vals != null){
			for(int i=0; i<vars.size(); i++){
				if(var.equals(vars.get(i))){
					vals.set(i, new_val);
					return ;
				}
			}
		}
		
		if(out_env == null){ /* error because no keywords */
			System.out.println("error : set_variable_value, no match --- " + var);
			System.exit(0);
		}
		else{
			out_env.set_variable_value(var, new_val);
		}
	}
	
	//在当前环境下添加约束
	public void define_variable(String new_var, Data new_val){
		ArrayList<String> vars = frame.frame_variables();
		ArrayList<Data> vals = frame.frame_values();
		
		if (vars != null && vals != null){
			for(int i=0; i<vars.size(); i++){
				if(new_var.equals(vars.get(i))){
					vals.set(i, new_val);
					return ;
				}
			}
		}
		
		vars.add(new_var);
		vals.add(new_val);
	}
	
	public Environment(Frame aFrame, Environment aOutEnv){
		frame = aFrame;
		out_env = aOutEnv;
	}
	
	private Environment OutEnv(){
		return out_env;
	}
	
	private Frame FirstFrame(){
		return frame;
	}
}

class Frame{
	private ArrayList<String> vars;
	private ArrayList<Data> vals;
	
	public Frame(ArrayList<String> aVars, ArrayList<Data> aVals){
		if(aVars.size() != aVals.size()){
			System.out.println("error : Frame ctor, vars and vals haven't same size");
			System.exit(0);
		}
		vars = aVars;
		vals = aVals;
	}
	
	public ArrayList<String> frame_variables(){
		return vars;
	}
	
	public ArrayList<Data> frame_values(){
		return vals;
	}
}
