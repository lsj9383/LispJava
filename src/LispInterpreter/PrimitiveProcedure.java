package LispInterpreter;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/* 
 * 基本过程:
 * =,  equ
 * +,  add
 * -,  sub
 * *,  mul
 * \/, div
 * 
 *  */
interface Primitive{
	Data Call(ArrayList<Data> args);
}

/* = */
class Equ extends Data implements Primitive{
	private static Equ obj = null;
	
	private Equ(){
		type = DataType.PRIMITIVE;
	}
	
	static Equ Single(){
		if(obj == null){
			obj = new Equ();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				double n1 = (Double)args.get(i).GetContent();
				double n2 = (Double)args.get(i-1).GetContent(); 
				if(n1 != n2){
					return new BooleanData(false);
				}
			}
		}
		return new BooleanData(true);
	}
}

/* < */
class Less extends Data implements Primitive{
	private static Less obj = null;
	
	private Less(){
		type = DataType.PRIMITIVE;
	}
	
	static Less Single(){
		if(obj == null){
			obj = new Less();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				double n1 = (Double)args.get(i).GetContent();
				double n2 = (Double)args.get(i-1).GetContent(); 
				if(n1<=n2){
					return new BooleanData(false);
				}
			}
		}
		return new BooleanData(true);
	}
}

/* > */
class Great extends Data implements Primitive{
	private static Great obj = null;
	
	private Great(){
		type = DataType.PRIMITIVE;
	}
	
	static Great Single(){
		if(obj == null){
			obj = new Great();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		for(int i=1; i<args.size(); i++){
			if(args.get(i).Type() != DataType.NUMBER){
				System.out.println("error : Equ Call , Data is not number");
				System.exit(0);
			}
			else{
				double n1 = (Double)args.get(i).GetContent();
				double n2 = (Double)args.get(i-1).GetContent(); 
				if(n1>=n2){
					return new BooleanData(false);
				}
			}
		}
		return new BooleanData(true);
	}
}

/* + */
class Add extends Data implements Primitive{
	private static Add obj = null;
	
	private Add(){
		type = DataType.PRIMITIVE;
	}
	
	static Add Single(){
		if(obj == null){
			obj = new Add();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = 0;
		
		for(int i=0; i<args.size(); i++){
			dat += (Double)args.get(i).GetContent();
		}
		
		return new NumberData(dat);
	}
}

/* - */
class Sub extends Data implements Primitive{
	private static Sub obj = null;
	
	private Sub(){
		type = DataType.PRIMITIVE;
	}
	
	static Sub Single(){
		if(obj == null){
			obj = new Sub();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = (Double)args.get(0).GetContent();
		
		for(int i=1; i<args.size(); i++){
			dat -= (Double)args.get(i).GetContent();
		}
		
		return new NumberData(dat);
	}
}

/* * */
class Mul extends Data implements Primitive{
	private static Mul obj = null;
	
	private Mul(){
		type = DataType.PRIMITIVE;
	}
	
	static Mul Single(){
		if(obj == null){
			obj = new Mul();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = 1;
		
		for(int i=0; i<args.size(); i++){
			dat *= (Double)args.get(i).GetContent();
		}
		
		return new NumberData(dat);
	}
}

/* \/ */
class Div extends Data implements Primitive{
	private static Div obj = null;
	
	private Div(){
		type = DataType.PRIMITIVE;
	}
	
	static Div Single(){
		if(obj == null){
			obj = new Div();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		double dat = (Double)args.get(0).GetContent();
		
		for(int i=1; i<args.size(); i++){
			dat /= (Double)args.get(i).GetContent();
		}
		
		return new NumberData(dat);
	}
}

/* cons */
class Cons extends Data implements Primitive{
	private static Cons obj = null;
	
	private Cons(){
		type = DataType.PRIMITIVE;
	}
	
	static Cons Single(){
		if(obj == null){
			obj = new Cons();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=2){
			System.out.println("error : Cons , size of args is not two");
		}
		
		return new ConsData(args.get(0), args.get(1));
	}
}

/* Car */
class Car extends Data implements Primitive{
	private static Car obj = null;
	
	private Car(){
		type = DataType.PRIMITIVE;
	}
	
	static Car Single(){
		if(obj == null){
			obj = new Car();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		if(args.get(0) == null){
			System.out.println("error : Car , args is null");
		}
		
		return ((Pair)args.get(0).GetContent()).First();
	}
}

/* cons */
class Cdr extends Data implements Primitive{
	private static Cdr obj = null;
	
	private Cdr(){
		type = DataType.PRIMITIVE;
	}
	
	static Cdr Single(){
		if(obj == null){
			obj = new Cdr();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		
		return ((Pair)args.get(0).GetContent()).Second();
	}
}

/* List */
class List extends Data implements Primitive{
	private static List obj = null;
	
	private List(){
		type = DataType.PRIMITIVE;
	}
	
	static List Single(){
		if(obj == null){
			obj = new List();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		return Iter(args, 0);
	}
	
	private Data Iter(ArrayList<Data> args, int cnt){
		if(cnt==args.size()){
			return null;
		}
		else{
			return new ConsData(args.get(cnt), Iter(args, cnt+1));
		}
	}
}

/* null? */
class isNull extends Data implements Primitive{
	private static isNull obj = null;
	
	private isNull(){
		type = DataType.PRIMITIVE;
	}
	
	static isNull Single(){
		if(obj == null){
			obj = new isNull();
			return obj;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		if(args.size()!=1){
			System.out.println("error : Car , args is not 'one' cons");
			System.exit(0);
		}
		
		if (args.get(0) == null){
			return new BooleanData(true);
		}
		else{
			return new BooleanData(false);
		}
	}
}

/* set-car! */
class SetCar extends Data implements Primitive{
	private static SetCar obj = null;
	
	private SetCar(){
		type = DataType.PRIMITIVE;
	}
	
	static SetCar Single(){
		if(obj == null){
			obj = new SetCar();
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.get(0).type!=DataType.CONS){
			System.out.println("error : SetCar , args(0) is not cons data");
			System.exit(0);
		}
		((Pair)args.get(0).GetContent()).SetFirst(args.get(1));
		
		return null;
	}
}

/* set-cdr! */
class SetCdr extends Data implements Primitive{
	private static SetCdr obj = null;
	
	private SetCdr(){
		type = DataType.PRIMITIVE;
	}
	
	static SetCdr Single(){
		if(obj == null){
			obj = new SetCdr();
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.get(0).type!=DataType.CONS){
			System.out.println("error : SetCdr , args(0) is not cons data");
			System.exit(0);
		}
		((Pair)args.get(0).GetContent()).SetSecond(args.get(1));
		
		return null;
	}
}

/* remainder */
class Remainder extends Data implements Primitive{
	private static Remainder obj = null;
	
	private Remainder(){
		type = DataType.PRIMITIVE;
	}
	
	static Remainder Single(){
		if(obj == null){
			obj = new Remainder();
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=2){
			System.out.println("error : remainder , operands should be two numbers.");
		}
		if(args.get(0).Type()!=DataType.NUMBER || args.get(1).Type()!=DataType.NUMBER){
			System.out.println("error : remainder , operands is not NUMBER.");
		}
		
		double first = (double)args.get(0).GetContent();
		double second = (double)args.get(1).GetContent();
		
		return new NumberData(first%second);
	}
}

/* int */
class Integer extends Data implements Primitive{
	private static Integer obj = null;
	
	private Integer(){
		type = DataType.PRIMITIVE;
	}
	
	static Integer Single(){
		if(obj == null){
			obj = new Integer();
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : remainder , operand should be one number.");
			System.exit(0);
		}
		if(args.get(0).Type()!=DataType.NUMBER){
			System.out.println("error : remainder , operands is not NUMBER.");
			System.exit(0);
		}
		
		double first = (double)args.get(0).GetContent();
		
		return new NumberData((int)first);
	}
}

/* load-image */
class LoadImage extends Data implements Primitive{
	private static LoadImage obj = null;
	
	private LoadImage(){
		type = DataType.PRIMITIVE;
	}
	
	static LoadImage Single(){
		if(obj == null){
			obj = new LoadImage();
		}
		return obj;
	}
	
	private Data ColorData(int[] rgb){
		return new ConsData(new NumberData(rgb[0]), 
							new ConsData(new NumberData(rgb[1]), 
										 new ConsData(new NumberData(rgb[2]), null)));
	} 
	
	@Override
	public Data Call(ArrayList<Data> args){
		
		if(args.size()!=1){
			System.out.println("error : LoadImage , operands should be one");
			System.exit(0);
		}
		if(args.get(0).Type() != DataType.QUOTED){
			System.out.println("error : LoadImage , operands should be a quoted");
			System.exit(0);
		}
		
		//image
		try {
			BufferedImage image = ImageIO.read(new File((String)args.get(0).GetContent()));
			WritableRaster raster = image.getRaster();
		
			ConsData header = new ConsData(	
							new ConsData(	new QuotedData("#IMAGE#"),
											new ConsData(	new NumberData(image.getWidth()),					//width 
															new ConsData( 	new NumberData(image.getHeight()),	//height
																			null))), 
							null );
			ConsData ROW = header;
			
			for(int row=0; row<raster.getHeight(); row++){
				//获得新行
				((Pair)ROW.GetContent()).SetSecond(new ConsData(null, null));
				ROW = (ConsData)((Pair)ROW.GetContent()).Second();
				ConsData COL = ROW;
				
				for(int col=0; col<raster.getWidth(); col++){
					//获得新列
					if(col==0){
						((Pair)COL.GetContent()).SetFirst(new ConsData(null, null));
						COL = (ConsData)((Pair)COL.GetContent()).First();
					}else{
						((Pair)COL.GetContent()).SetSecond(new ConsData(null, null));
						COL = (ConsData)((Pair)COL.GetContent()).Second();
					}
					
					int[] pix = new int[3];
					raster.getPixel(row, col, pix);			//读取像素
					Data color_data = ColorData(pix);		//
					
					((Pair)COL.GetContent()).SetFirst(color_data);
				}
			}
			
			return header;
			
		} catch (Exception e) {
			return null;
		} 
	}
}

/* display-image */
class DisplayImage extends Data implements Primitive{
	private static DisplayImage obj = null;
	
	private DisplayImage(){
		type = DataType.PRIMITIVE;
	}
	
	static DisplayImage Single(){
		if(obj == null){
			obj = new DisplayImage();
		}
		return obj;
	}
	
	private int getWidth(Data header){
		ConsData headerInfo = (ConsData)((Pair)header.GetContent()).First();
		ConsData Info = (ConsData)((Pair)headerInfo.GetContent()).Second();
		double width = (double)((Pair)Info.GetContent()).First().GetContent();
		return (int)width;
	}
	
	private int getHeight(Data header){
		ConsData headerInfo = (ConsData)((Pair)header.GetContent()).First();
		ConsData Info = (ConsData)((Pair)headerInfo.GetContent()).Second();
		double height = (double)((Pair)((Pair)Info.GetContent()).Second().GetContent()).First().GetContent();
		return (int)height;
	}
	
	//将color data转换为数组
	private void colorInt(int[] color, ConsData color_data){
		ConsData FirstPair = color_data;
		ConsData SecondPair = (ConsData)((Pair)color_data.GetContent()).Second();
		ConsData ThirdPair = (ConsData)((Pair)SecondPair.GetContent()).Second();
		
		double first 	= (double)((Pair)FirstPair.GetContent()).First().GetContent();
		double second 	= (double)((Pair)SecondPair.GetContent()).First().GetContent();
		double third 	= (double)((Pair)ThirdPair.GetContent()).First().GetContent();
		
		color[0] = (int)first;
		color[1] = (int)second;
		color[2] = (int)third;
	}
	
	private void updateBufferedImage(BufferedImage image, ConsData header){
		WritableRaster raster = image.getRaster();
		int width = image.getWidth();
		int height = image.getHeight();
		ConsData ROW = header;
		
		for(int row=0; row<height; row++){
			ROW = (ConsData)((Pair)ROW.GetContent()).Second();
			ConsData COL = ROW;
			for(int col=0; col<width; col++){
				if(col==0){
					COL = (ConsData)((Pair)COL.GetContent()).First();
				}
				else{
					COL = (ConsData)((Pair)COL.GetContent()).Second();
				}
				int[] color = new int[3];
				colorInt(color, (ConsData)((Pair)COL.GetContent()).First());
				raster.setPixel(row, col, color);
			}
		}
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		//检查
		if(args.size()!=1){
			System.out.println("error : DisplayImage , operands is not one");
			System.exit(0);
		}
		//
//		if()
		//打开显示图像的进程
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ConsData header = (ConsData)args.get(0);
				int width = getWidth(header);
				int height = getHeight(header);		//获得高度
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);	//获得缓冲图像
				
				updateBufferedImage(image, header);		//更新
				
				JFrame frame = new JFrame();
				frame.add(new ImageComponent(image));
				frame.setTitle("ImageTest");
				frame.setSize(width, height);
				frame.setVisible(true);
			}
			
			/* 显示图像的组件 */
			class ImageComponent extends JComponent{
				private BufferedImage image;
				public ImageComponent(BufferedImage image){
					this.image = image;
				}
				public void paintComponent(Graphics g){
					g.drawImage(image, 0, 0, null);
				}
			}
			
		});
		return null;
	}
}