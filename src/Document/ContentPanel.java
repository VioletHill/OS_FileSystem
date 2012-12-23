package Document;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import Document.Disk.DiskPanel;

public class ContentPanel extends JPanel
{
	Vector<Folder> folderList;
	Vector<MyFile> fileList;
	ContentPanel fatherContentPanel;
	static boolean isShowAll=false;
	static ContentPanel runningPanel=null;

	ContentPanel(ContentPanel father)
	{
		fatherContentPanel=father;
		setBackground(Color.white);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		addMouseListener(contentMouseListener);
		folderList=new Vector();
		fileList=new Vector();
		
	}
	

	Timer refreshTime;
	void deleteTime()
	{
		if (refreshTime==null) return ;
		refreshTime.cancel();
		refreshTime=null;
	}
	
	public void refresh()
	{
		refreshTime=new Timer();	
		class RefreshTask extends TimerTask
		{
			public void run()
			{
				for (int i=0; i<folderList.size(); i++)
				{
					if (isShowAll || !folderList.get(i).isHide)	add(folderList.get(i).folderView);
				}
				for (int i=0; i<fileList.size(); i++)
				{
					if (isShowAll || !fileList.get(i).isHide)	add(fileList.get(i).fileView);	
				}	
				repaint();
				updateUI();
				
				deleteTime();
			}
		}
	
		removeAll();
		updateUI();
		refreshTime.schedule(new RefreshTask(), 50);
		
		Folder folder=getFolder();
		if (folder==null)	FolderToolBar.getToolBar().setAddress("�ҵ�Q��/");
		else	FolderToolBar.getToolBar().setAddress(folder.getAddress());
	}
	
	public Folder getFolder()
	{
		if (this==Disk.contentPanel) return null;
		for (int i=0; i<fatherContentPanel.folderList.size(); i++)
		{
			if (fatherContentPanel.folderList.get(i).contentPanel==this) return fatherContentPanel.folderList.get(i);
		}
		return null;
	}

	
	Block getContentBlock()
	{	
		Folder fatherFolder=getFolder();
		if (fatherFolder==null) return Disk.block[0];
		else return fatherFolder.block;
	}
	
	public void createFolder()
	{
		Block block=Disk.fat.getBlock();
		if (block==null)
		{
			JOptionPane.showMessageDialog(null, "�ռ��������޷��½��ļ�");
			return ;
		}
		
		Folder folder=new Folder(this);
		folder.block=block;
		folderList.add(folder);
		
		Folder fatherFolder=folder.fatherContentPanel.getFolder();
		if (fatherFolder==null)
			folder.setFatherAddress("�ҵ�Q��/");
		else	
			folder.setFatherAddress(fatherFolder.getAddress());
		
		folder.block.setProperty(folder);
		Disk.fat.setUseBlock(folder.block.index);
		
		getContentBlock().setData(this);
		refresh();
	}
	
	public void createFile()
	{
		Block block=Disk.fat.getBlock();
		if (block==null)
		{
			JOptionPane.showMessageDialog(null, "�ռ��������޷��½��ļ�");
			return ;
		}
		
		MyFile file=new MyFile(this);
		file.block=block;
		fileList.add(file);
		
		if (file.fatherContentPanel.getFolder()==null)
			file.setFatherAddress("�ҵ�Q��/");
		else	
			file.setFatherAddress(file.fatherContentPanel.getFolder().getAddress());
		
		file.block.setProperty(file);
		Disk.fat.setUseBlock(file.block.index);
		
		getContentBlock().setData(this);
		refresh();
	}
	
	public boolean delete(MyFile file)
	{
		if (fileList.remove(file))
		{
			refresh();
			return true;
		}
		else return false;
	}
	
	public void addKeyStringDocument(String key,JPanel panel)
	{
		for (int i=0; i<folderList.size(); i++)
		{
			if (folderList.get(i).name.indexOf(key)!=-1) panel.add(folderList.get(i).folderView);
			folderList.get(i).contentPanel.addKeyStringDocument(key, panel);
		}
		for (int i=0; i<fileList.size(); i++)
		{
			if (fileList.get(i).name.indexOf(key)!=-1) panel.add(fileList.get(i).fileView);
		}
	}
	
	MouseListener contentMouseListener = new MouseListener()
	{
		public void mouseClicked(MouseEvent e) 
		{
			if (e.getButton()==MouseEvent.BUTTON3)
			{
				JPopupMenu menu=new JPopupMenu();
				JMenuItem refreshMenu=new JMenuItem("ˢ��");
				refreshMenu.addActionListener(refreshMenuListener);
				menu.add(refreshMenu);
				
				JMenuItem newFile=new JMenuItem("�½��ļ�");
				newFile.addActionListener(newFileMenuListener);
				menu.add(newFile);
				
				JMenuItem newFolder=new JMenuItem("�½��ļ���");
				newFolder.addActionListener(newFolderMenuListener);
				menu.add(newFolder);
				
				if (!isShowAll)
				{
					JMenuItem showAll=new JMenuItem("��ʾ�����ļ�");
					showAll.addActionListener(showAllMenuListener);
					menu.add(showAll);
				}
				else
				{
					JMenuItem hideAll=new JMenuItem("����ʾ�����ļ�");
					hideAll.addActionListener(hideAllMenuListener);
					menu.add(hideAll);
				}
				menu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	
		ActionListener refreshMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				refresh();
			}
		};
		
		ActionListener newFileMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createFile();
			}
		};
		
		ActionListener newFolderMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createFolder();
			}
		};
		
		ActionListener showAllMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				isShowAll=true;
				refresh();
			}
		};
		
		ActionListener hideAllMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				isShowAll=false;
				refresh();
			}
		};
	};
	
	static ContentPanel getRunningPanel()
	{
		return runningPanel;
	}
	static void switchPanel(ContentPanel contentPanel)
	{
		runningPanel=contentPanel;
		Disk.getMainPanel().removeAll();
		Disk.getMainPanel().add(contentPanel);
		ContentPanel.getRunningPanel().refresh();
	}
	static void switchPanel(DiskPanel panel)
	{
		runningPanel=null;
		Disk.getMainPanel().removeAll();
		Disk.getMainPanel().add(panel);
		Disk.getMainPanel().repaint();
		Disk.getMainPanel().updateUI();
	}
}
