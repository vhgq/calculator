package Screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.PopupMenu;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class StandardScreen extends Screen{
	
	private final int EDIT_HEIGHT = 30; //"edit"标签的高度
	private final int TIP_HEIGHT = 20;  //"tip"标签的高度
	private final int EDIT_FONT_SIZE = 24; //"edit"标签字体大小
	private final int EDIT_FONT_SIZE_SMALL = 18; //长度过长时，弄小一号字体
	private final int TIP_FONT_SIZE = 12;//"tip"标签字体大小
	
	private final int editChangeSizeLength = 18; //如果editValue大于18位，则字体变小
	
	//private final int MARGIN = 1; //margin
	JLabel tipLabel = new JLabel("",SwingConstants.RIGHT);//
	JLabel editLabel = new JLabel("",SwingConstants.RIGHT);//
	
	public StandardScreen(){
		
		init();
	}
	
	private void init()
	{
		
		
		//JLabel默认的大小是根据文本长度和大小来决定的
		//使用setMaximumSize和setMinimumSize来设置最大大小和最小大小无效
		//可以通过设置标签的最佳大小setPreferredSize方法来实现
		tipLabel.setPreferredSize(new Dimension(getWidth(), TIP_HEIGHT));
		editLabel.setPreferredSize(new Dimension(getWidth(), EDIT_HEIGHT));
	
		//设置字体大小
		tipLabel.setFont(new Font("Serif", Font.PLAIN, TIP_FONT_SIZE));
		editLabel.setFont(new Font("Serif", Font.BOLD, EDIT_FONT_SIZE));
		
		BorderLayout border	= new BorderLayout();//边界布局
		//border.setHgap(MARGIN);//margin设置
		//border.setVgap(MARGIN);//margin设置
		this.setBorder(BorderFactory.createLineBorder(new Color(102, 102, 102)));//设置边框颜色和样式
		this.setLayout(border);//设置布局
		this.setBackground(new Color(238, 238, 238));//背景色

		//设置margin
		//this.add(new Panel(),"North");
		//this.add(new Panel(),"South");
		this.add(new Panel(),"East");
		this.add(new Panel(),"West");
		
		//设置content
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBackground(new Color(238, 238, 238));
		content.add(tipLabel, "North");
		content.add(editLabel , "Center");
		this.add(content,"Center");
		
		
	}

	@Override
	public String getTipValue() {
		// TODO Auto-generated method stub
		return tipLabel.getText();
	}

	@Override
	public Boolean setTipValue(String value) {
		// TODO Auto-generated method stub
		tipLabel.setText(value);
		return Boolean.TRUE;
	}

	@Override
	public String getEditValue() {
		// TODO Auto-generated method stub
		return editLabel.getText();
	}

	@Override
	public Boolean setEditValue(String value) {
		
		/*
		//通过正则表达式去掉小数点后面多余的0
		if(value.indexOf(".") > 0){
	        value = value.replaceAll("0+?$", "");//去掉多余的0
	        value = value.replaceAll("[.]$", "");//如最后一位是.则去掉
	    }
	    */
		
		// TODO Auto-generated method stub
		if(value.length() > editChangeSizeLength)
			editLabel.setFont(new Font("Serif", Font.BOLD, EDIT_FONT_SIZE_SMALL));
		else
			editLabel.setFont(new Font("Serif", Font.BOLD, EDIT_FONT_SIZE));
			
		editLabel.setText(value);
		return Boolean.TRUE;
	}
}
