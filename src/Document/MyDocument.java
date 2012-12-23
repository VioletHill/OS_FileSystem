package Document;

import java.text.SimpleDateFormat;
import java.util.Date;



public abstract class  MyDocument
{	
	ContentPanel contentPanel;
	ContentPanel fatherContentPanel;
	String whoAmI;
	String fatherAddress;
	String name;
	String createTime;
	String visitTime;
	String modifiTime;
	Block block;
	boolean isHide=false;
	
	
	public abstract void create();
	public abstract void resetName();
	public abstract	boolean delete(boolean isRootPanel);
	public abstract void open();

	public void setFatherAddress(String str)
	{
		fatherAddress=str;
	}
	
	public String getSystemTime()
	{
		 Date currentTime = new Date();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return  formatter.format(currentTime);
	}
	
	public void setCreateTime()
	{
		createTime=getSystemTime();
		visitTime=createTime;
		modifiTime=createTime;
	
	}
	
	public void setVisitTime()
	{
		visitTime=getSystemTime();

	}
	
	public void setModifiTime()
	{
		modifiTime=getSystemTime();
		
	}
	
	public void showProperty()
	{
		block.showProperty();
	}
	
	public void getProperty(Block block)
	{
		this.block=block;
		
		String str=block.property;
		int begin;
		int end;
		
		begin=str.indexOf("文件名:");
		str=str.substring(begin+4);
		end=str.indexOf('\n');
		name=str.substring(0, end);
		str=str.substring(end+1);
		
		begin=str.indexOf("地址:");
		str=str.substring(begin+3);
		end=str.indexOf('\n');
		fatherAddress=str.substring(0,end);
		str=str.substring(end+1);
		
		begin=str.indexOf("创建时间:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		createTime=str.substring(0,end);
		str=str.substring(end+1);
		
		begin=str.indexOf("最近访问:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		visitTime=str.substring(0,end);
		str=str.substring(end+1);
		
		begin=str.indexOf("最近修改:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		modifiTime=str.substring(0,end);
		str=str.substring(end+1);
		
		begin=str.indexOf("是否为隐藏文件:");
		str=str.substring(begin+8);	
		if (str.equals("false"))	isHide=false;
		else isHide=true;
	}
	
}
