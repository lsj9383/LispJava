package LispInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Display {
	/* 打印数据 */
	static void Show(Data data){
		String s = new String();
		
		s = "output value : ";
		if(data == null){
			s += "OK\n";
		}
		else{
			s = data.toString()+"\n";
		}
		
		System.out.print(s);
	}
	
	/* 欢迎界面 */
	static void Welcome(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("welcome.txt")));
			String tmp;
			try {
				tmp = br.readLine();
				while(tmp != null){
					System.out.println(tmp);
					tmp = br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
