package Document;

import javax.swing.JOptionPane;

public class Block
{
	String property;
	String data;
	int index;

	void setProperty(MyDocument fcb)
	{
		property="�ļ�����:"+fcb.whoAmI+'\n';
		property=property+"�ļ���:"+fcb.name+'\n';
		property=property+"��ַ:"+fcb.fatherAddress+'\n';
		property=property+"����ʱ��:"+fcb.createTime+'\n';
		property=property+"�������:"+fcb.visitTime+'\n';
		property=property+"����޸�:"+fcb.modifiTime+'\n';
		property=property+"���:"+new Integer(index).toString()+'\n';
		property=property+"�Ƿ�Ϊ�����ļ�:"+new Boolean(fcb.isHide).toString();
	}
	
	void setData(String str)
	{
		data=str;
	}
	
	void setData(ContentPanel content)
	{
		data="";
		for (int i=0; i<content.folderList.size(); i++)
		{
			data=data+content.folderList.get(i).block.index+'\n';
		}
		data=data+"NULL\n";
		
		for (int i=0; i<content.fileList.size(); i++)
		{
			data=data+content.fileList.get(i).block.index+'\n';
		}
	}
}

