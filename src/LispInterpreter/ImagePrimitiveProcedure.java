package LispInterpreter;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

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
										 new ConsData(new NumberData(rgb[2]), NullData.Single())));
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
																			NullData.Single()))), 
							null );
			ConsData ROW = header;
			
			for(int row=0; row<raster.getHeight(); row++){
				//获得新行
				((Pair)ROW.GetContent()).SetSecond(new ConsData(NullData.Single(), NullData.Single()));
				ROW = (ConsData)((Pair)ROW.GetContent()).Second();
				ConsData COL = ROW;
				
				for(int col=0; col<raster.getWidth(); col++){
					//获得新列
					if(col==0){
						((Pair)COL.GetContent()).SetFirst(new ConsData(NullData.Single(), NullData.Single()));
						COL = (ConsData)((Pair)COL.GetContent()).First();
					}else{
						((Pair)COL.GetContent()).SetSecond(new ConsData(NullData.Single(), NullData.Single()));
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

/* save-image */
class SaveImage extends Data implements Primitive{
	private static SaveImage obj = null;
	
	private SaveImage(){
		type = DataType.PRIMITIVE;
	}
	
	static SaveImage Single(){
		if(obj == null){
			obj = new SaveImage();
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
		if(args.size()!=2){
			System.out.println("error : SaveImage , operands is not two");
			System.exit(0);
		}
		
		String path = (String) args.get(1).GetContent();
		if(!path.substring(path.length()-4, path.length()).equals(".bmp")){
			System.out.println("error : SaveImage , image type is not bmp(now support BMP only)");
			System.exit(0);
		}

		ConsData header = (ConsData)args.get(0);
		int width = getWidth(header);
		int height = getHeight(header);			//获得高度
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);	//获得缓冲图像
		updateBufferedImage(image, header);		//更新

		try {
			ImageIO.write(image, "BMP", new File(path));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}