package LispInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Display {
	/* ��ӡ���� */
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
	
	/* ��ӭ���� */
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
	
	/* ���벢ִ�ж���ʽ�ļ��еı��ʽ */
	static Express LoadDefinition(){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("./liva-start/Definition.txt")));
			String tmp;
			String s="";
			
			/*���s*/
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
