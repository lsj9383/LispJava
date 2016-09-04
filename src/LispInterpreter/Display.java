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
			br = new BufferedReader(new FileReader(new File("./liva-start/Welcome.txt")));
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
	
	/* 载入并执行定义式文件中的表达式 */
	static Express LoadDefinition(){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("./liva-start/Definition.txt")));
			String tmp;
			String s="";
			
			/*获得s*/
			tmp = br.readLine();
			while(tmp != null){
				s+=tmp+"\n";
				tmp = br.readLine();
			}
			if(s.length()==0){
				return null;
			}
			else{
				return new Express(s);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
