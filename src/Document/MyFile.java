package Document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyFile extends MyDocument
{
	Vector<String> bufferString=new Vector(10);
	FileViewPanel fileView=new FileViewPanel();
	
	int bufferIndex;
	int fontSize;
	String text="";
	
	JMenuBar menuBar;
	JFrame frame;
	JPanel panel;
	JTextArea textArea;
	
	JFrame replaceFrame;
	JMenuItem undo;
	JMenuItem redo;
	
	
	class FileViewPanel extends JPanel
	{
		void setViewPanel()
		{
			viewPanel.setBackground(Color.white);
			viewPanel.setBounds(15, 5, 70, 70);
			viewPanel.addMouseListener(fileMouseListener);
			viewImg = new ImageIcon("resource/file.png");
			JLabel imgLabel = new JLabel(viewImg);
			viewPanel.add(imgLabel);
			add(viewPanel);
		}
		
		void setNamePanel()
		{
			nameField.setHorizontalAlignment(JTextField.CENTER);
			nameField.setEditable(false);
			nameField.setBackground(Color.white);
			nameField.setBounds(10, 80, 80, 20);
			add(nameField);
		}
		FileViewPanel()
		{
			setLayout(null);
			setBackground(Color.white);
			setPreferredSize(new Dimension(100,100));
			setViewPanel();
			setNamePanel();
		}
		
		MouseListener fileMouseListener=new MouseListener()
		{
		
			public void mouseClicked(MouseEvent e) 
			{
			
				if (e.getButton()==MouseEvent.BUTTON3)
				{
					JPopupMenu menu=new JPopupMenu();
					JMenuItem openMenu=new JMenuItem("��");
					openMenu.addActionListener(openMenuListener);
					menu.add(openMenu);
					
					JMenuItem editMenu=new JMenuItem("�༭");
					editMenu.addActionListener(openMenuListener);
					menu.add(editMenu);
					
					JMenuItem deleteMenu=new JMenuItem("ɾ��");
					deleteMenu.addActionListener(deleteMenuListener);
					menu.add(deleteMenu);
					
					JMenuItem resetNameMenu=new JMenuItem("������");
					resetNameMenu.addActionListener(resetNameMenuListener);
					menu.add(resetNameMenu);
					if (!isHide)
					{
						JMenuItem hideMenu=new JMenuItem("�����ļ�");
						hideMenu.addActionListener(hideMenuListener);
						menu.add(hideMenu);
					}
					else
					{
						JMenuItem showMenu=new JMenuItem("��ʾ�ļ�");
						showMenu.addActionListener(showMenuListener);
						menu.add(showMenu);
					}
					
					JMenuItem propertyMenu=new JMenuItem("����");
					propertyMenu.addActionListener(propertyMenuListener);
					menu.add(propertyMenu);
					
					menu.show(e.getComponent(),e.getX(),e.getY());
				}
				if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2)
				{
					viewPanel.setBackground(Color.white);
					open();
				}
			}

			public void mouseEntered(MouseEvent arg0) 
			{
				viewPanel.setBackground(Color.blue);
			}

			public void mouseExited(MouseEvent arg0) 
			{
				viewPanel.setBackground(Color.white);
			}

			public void mousePressed(MouseEvent arg0) 
			{			
			}

			public void mouseReleased(MouseEvent e) 
			{		
			}
			
			ActionListener openMenuListener = new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					open();
				}				
			}; 
			ActionListener deleteMenuListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					delete(true);
				}
			};
			
			ActionListener hideMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					hideFile(true);
				}
			};
			
			ActionListener showMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					hideFile(false);
				}
			};
			
			ActionListener resetNameMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					resetName();
				}
			};
			
			ActionListener propertyMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperty();
				}
			};
		};
	}
	
	void setMenuBar()
	{
		menuBar=new JMenuBar();
		menuBar.setBounds(0, 0, 500, 20);
		
		JMenu file=new JMenu("�ļ�");
		JMenuItem save=new JMenuItem("�����ļ�");
		save.addActionListener(saveButtonListener);
		JMenuItem exit=new JMenuItem("�˳�");
		exit.addActionListener(exitButtonListener);
		file.add(exit);
		file.add(save);
		
		JMenu edit=new JMenu("�༭");
		undo=new JMenuItem("����");
		undo.addActionListener(undoButtonListener);
		
		redo=new JMenuItem("����");
		redo.addActionListener(redoButtonListener);
		
		JMenuItem replace=new JMenuItem("�滻");
		replace.addActionListener(replaceButtonListener);
		edit.add(replace);
		edit.add(undo);
		edit.add(redo);
		
		JMenu setting=new JMenu("����");	
		JMenuItem font=new JMenuItem("��������");
		font.addActionListener(fontButtonListener);
		setting.add(font);
		menuBar.add(file);
		menuBar.add(setting);	
		menuBar.add(edit);
		panel.add(menuBar);
	}
	
	void setTextArea()
	{
		textArea=new JTextArea();
		textArea.setBackground(Color.white);
		textArea.addKeyListener(setTextListener);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll);
	}
	
	//�������±�
	public void createTxt()
	{
		setMenuBar();
		setTextArea();
	
		frame=new JFrame();
		frame.setJMenuBar(menuBar);
		frame.add(panel);
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(closeListener); 
	}
	
	public MyFile(ContentPanel father)
	{
		fatherContentPanel=father;
		panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(1,1));
		
		createTxt();
		
		create();
	}
	
	public MyFile(Block block,ContentPanel father)
	{
		fatherContentPanel=father;
		fatherContentPanel.fileList.add(this);
		
		panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(1,1));
		
		createTxt();
		getProperty(block);
		nameField.setText(name);
		text=block.data;
		if (text==null) text="";
		whoAmI="�ļ�";
	}
	
	public void create()
	{
		setCreateTime();
		whoAmI="�ļ�";
		nameField.setText("�½��ļ�");
		resetName(true);
	}
	

	
	/////////��
	public void open()
	{
		if (frame.isShowing())
		{
			frame.requestFocus();
			return ;
		}
	
		setVisitTime();
		block.setProperty(this);
		frame.show();
		frame.setTitle("���±�����"+name);
		textArea.setText(text);
		
		undo.setEnabled(false);
		redo.setEnabled(false);
		bufferString.clear();
		bufferIndex=0;
		bufferString.add(text);
	}
	
	//////////ɾ��
	public boolean delete(boolean isRootPanel)
	{
		if (frame.isShowing())
		{
			frame.requestFocus();
			JOptionPane.showMessageDialog(null, "�ļ����ڱ�ʹ��", "ɾ������", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		else
		{
			if (isRootPanel)
			{
				int option = JOptionPane.showConfirmDialog(
					null, "ɾ���ļ���",
					"�Ƿ�ɾ���ļ�", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null);  
				if (option==JOptionPane.NO_OPTION)	return false;
			}
			
			Disk.fat.deleteBlock(this);
			fatherContentPanel.delete(this);
			
			Block fatherBlock;
			if (fatherContentPanel.getFolder()==null) fatherBlock=Disk.block[0];
			else fatherBlock=fatherContentPanel.getFolder().block;
			fatherBlock.setData(fatherContentPanel);
	
					
			return true;
		}
	}
	
	//////////��������
	void setTextAreaFontSize(int selectSize)
	{
		fontSize=selectSize;
		textArea.setFont(new Font(textArea.getFont().getFontName(),textArea.getFont().getStyle(),fontSize));
	}
	
	/////////�滻
	void replaceString(String search, String replace)
	{
		String str=textArea.getText();
		str=str.replaceAll(search, replace);
		textArea.setText(str);
		addBuffer();
	}
	
	//////����뻺�壬������ʹ��
	void addBuffer()
	{
		
		if (bufferString.get(bufferIndex).equals(textArea.getText())) return ;
		for (int i=bufferString.size()-1; i>bufferIndex; i--)
		{
			bufferString.remove(i);
		}
		if (bufferString.size()<10)
		{
			bufferIndex++;
			bufferString.add(textArea.getText());
		}
		else
		{
			for (int i=0; i<9; i++)
			{
				bufferString.set(i, bufferString.get(i+1));
			}
			bufferString.set(9, textArea.getText());
		}
		if (bufferIndex!=0) undo.setEnabled(true);
		else undo.setEnabled(false);
		if (bufferIndex!=bufferString.size()-1) redo.setEnabled(true);
		else redo.setEnabled(false);
	}
	
	////////�˳�
	void fileExit()
	{
		if (replaceFrame!=null) replaceFrame.dispose();
		if (!text.equals(textArea.getText()))
		{
			int option = JOptionPane.showConfirmDialog(
				null, "�ļ����޸ģ��Ƿ񱣴棿",
				"�Ƿ񱣴��ļ�", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null);  
			switch (option)
			{
			case JOptionPane.YES_OPTION:
				text=textArea.getText();
				block.setData(text);
				setModifiTime();
				frame.dispose();
				break;
			case JOptionPane.NO_OPTION:
				frame.dispose();
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		}
		else frame.dispose();
		
		block.setProperty(this);
	}
	
	///////����
	void hideFile(boolean hide)
	{
		isHide=hide;
		fatherContentPanel.refresh();
		block.setProperty(this);
	}
	
	//////�����Ƿ����ظ�����
	boolean checkName(String str)
	{
		for (int i=0; i<fatherContentPanel.fileList.size(); i++)
		{
			if (fatherContentPanel.fileList.get(i)!=this && fatherContentPanel.fileList.get(i).name.equals(str))	return false;
		}
		return true;
	}
	
	////�Զ�����������
	void resetName(boolean isAutomic)
	{
		String newName=nameField.getText();
		while (!newName.isEmpty() && newName.charAt(newName.length()-1)==' ') newName=newName.substring(0, newName.length()-1);
		while (!newName.isEmpty() && newName.charAt(0)==' ') newName=newName.substring(1);
		
		if (newName.length()>10) newName=newName.substring(0,10);
		
		if (newName.length()==0)
		{
			nameField.setText(name);
			return ;
		}
		
		if (!checkName(newName))
		{
			int i=0;
			while (!checkName(newName+"("+new Integer(++i).toString()+')'));
			if (!isAutomic)
			{
				JOptionPane.showMessageDialog(null, "ϵͳ�Ѱ����޸�������", "����������", JOptionPane.YES_OPTION, null);
			}
			newName=newName+"("+new Integer(i).toString()+')';
		}
		name=newName;
		nameField.setText(name);
		
		if (!isAutomic)
		{
			setModifiTime();
			block.setProperty(this);
		}
	}
	
	public void resetName()
	{
		nameField.setEditable(true);
		nameField.addFocusListener(nameFieldFocusListener);
		nameField.addActionListener(nameFieldActionListener);
	}

	///��ȡ·��
	public String getAddress()
	{
		return fatherAddress+name;
	}
	
	
	//////////////////////////////������///////////////////////////
	
	/////////////////////////////����////////////////////////////
	FocusListener nameFieldFocusListener=new FocusListener()
	{
		public void focusGained(FocusEvent arg0) 
		{
		}
		public void focusLost(FocusEvent e) 
		{
			nameField.setEditable(false);
			resetName(false);
		}
	};
	
	ActionListener nameFieldActionListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			fatherContentPanel.requestFocus();
		}
	};
	
	//////////////////////////////�ر�///////////////////////////////
	WindowAdapter closeListener=new WindowAdapter() 
	{ 
	       public void windowClosing(WindowEvent   e) 
	       {     
	    	   fileExit();
	       } 
	 };
	 
	//////////////////////////////����//////////////////// 
	ActionListener saveButtonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			text=textArea.getText();
			block.setData(text);
		}				
	};

	//////////////////////////////����/////////////////////////
	ActionListener undoButtonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			textArea.setText(bufferString.get(--bufferIndex));
			if (bufferIndex!=0) undo.setEnabled(true);
			else undo.setEnabled(false);
			if (bufferIndex!=bufferString.size()-1) redo.setEnabled(true);
			else redo.setEnabled(false);
		}				
	};
	
	//////////////////////////////����//////////////////////////
	ActionListener redoButtonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			textArea.setText(bufferString.get(++bufferIndex));
			if (bufferIndex!=0) undo.setEnabled(true);
			else undo.setEnabled(false);
			if (bufferIndex!=bufferString.size()-1) redo.setEnabled(true);
			else redo.setEnabled(false);
		}				
	};
	
	/////////////////////////////�滻///////////////////////////
	ActionListener replaceButtonListener = new ActionListener() 
	{

		JTextField searchString;
		JTextField replaceString;
		public void actionPerformed(ActionEvent e) 
		{
			if (replaceFrame!=null && replaceFrame.isShowing())
			{
				replaceFrame.requestFocus();
				return ;
			}
			replaceFrame=new JFrame("�滻");
			searchString=new JTextField("");
			replaceString=new JTextField("");
			
			replaceFrame.setSize(200, 100);
			replaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			replaceFrame.setLayout(new GridLayout(3,3));
			replaceFrame.setVisible(true);
			
			JLabel searchLabel=new JLabel("��������",JLabel.RIGHT);			
			JLabel replaceLabel=new JLabel("�滻����",JLabel.RIGHT);
			JButton yes=new JButton("ȷ���滻");
			yes.addActionListener(yesReplaceListener);
			JButton no=new JButton("ȡ��");
			no.addActionListener(noReplaceListener);
			
			replaceFrame.add(searchLabel);
			replaceFrame.add(searchString);
			replaceFrame.add(replaceLabel);
			replaceFrame.add(replaceString);
			replaceFrame.add(yes);
			replaceFrame.add(no);
		}				
		
		ActionListener yesReplaceListener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (replaceString.getText().equals("") || searchString.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "�滻ʧ��,��������");
					return ;
				}
				replaceString(searchString.getText(),replaceString.getText());
				replaceFrame.dispose();
				JOptionPane.showMessageDialog(null, "�滻���");
			}				
		};
		ActionListener noReplaceListener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{		
				replaceFrame.dispose();
			}				
		};
	};
	
	/////////////////////////////�˳�////////////////////////////
	ActionListener exitButtonListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			fileExit();
		}
	};
	
	/////////////////////////////����/////////////////////////////
	ActionListener fontButtonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{		
			int oldFontSize=textArea.getFont().getSize();
			Integer []newFontSize=new Integer[26];
			for (int i=0; i<16; i++)
			{
				newFontSize[i]=new Integer(i+10);
			}
			Object selectSize=JOptionPane.showInputDialog(null, "ѡ�������С", "��������", 
					JOptionPane.INFORMATION_MESSAGE,null,newFontSize, newFontSize[oldFontSize-10]);
			if (selectSize!=null)	setTextAreaFontSize((Integer) selectSize);
		}				
	};
	
	///////////////////////////�޸��ĵ�//////////////////////
	KeyListener setTextListener=new KeyListener()
	{
		public void keyPressed(KeyEvent arg0) 
		{					
		}

		public void keyReleased(KeyEvent arg0) 
		{	
			addBuffer();
		}

		public void keyTyped(KeyEvent arg0) 
		{			
		}
		
	};
}
