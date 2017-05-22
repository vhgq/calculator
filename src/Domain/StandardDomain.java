package Domain;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Observer.Observer;

public class StandardDomain extends Domain implements ActionListener{
	
	final private int MARGIN = 1;//控件之间的距离
	
	public StandardDomain() {
		// TODO Auto-generated constructor stub
		
		init();
	}
	
	private void init() {
		
		//this.setMaximumSize(new Dimension(10, 50));
		//this.setMinimumSize(new Dimension(10, 50));
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		//网格包布局
		setLayout(gridbag);
		
		c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridheight = 1;
        c.gridwidth = 1;
		
		//第一行
        c.gridy = 0;
        makebutton("←", gridbag, c);
        makebutton("CE", gridbag, c);
        makebutton("C", gridbag, c);
        makebutton("±", gridbag, c);
        makebutton("√", gridbag, c);
        //第一行
        
        //第二行
        c.gridy = 1;
        makebutton("7", gridbag, c);
        makebutton("8", gridbag, c);
        makebutton("9", gridbag, c);
        makebutton("/", gridbag, c);
        makebutton("%", gridbag, c);
        //第二行

        //第三行  
        c.gridy = 2;
        makebutton("4", gridbag, c);
        makebutton("5", gridbag, c);
        makebutton("6", gridbag, c);
        makebutton("*", gridbag, c);
        makebutton("1/x", gridbag, c);
        //第三行
        
        //第四行
        c.gridy = 3;
        makebutton("1", gridbag, c);
        makebutton("2", gridbag, c);
        makebutton("3", gridbag, c);
        makebutton("-", gridbag, c);
        c.gridheight = 2 ;//跨两行
        makebutton("=", gridbag, c);
        //第四行
        
        //第五行
        c.gridy = 4;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        makebutton("0", gridbag, c);
        
        c.gridx = 2;
        c.gridwidth = 1;
        makebutton(".", gridbag, c);
        
        c.gridx = 3;
        makebutton("+", gridbag, c);
        //第五行

        
	}
	
	protected void makebutton(String name,
                              GridBagLayout gridbag,
                              GridBagConstraints c) {
        JButton button = new JButton(name); 
        
        //设置字体
        button.setFont(new Font("宋体", Font.BOLD, 10));
        
        //去掉按钮文字周围的焦点框
        button.setFocusPainted(false);
        
        //事件监听
        button.addActionListener(this);
        
        //设置此布局中指定组件的约束条件。
        //将button组件的约束条件设置为c
        gridbag.setConstraints(button, c);
        add(button);
    }

	
	@Override
	//当按钮点击时，执行此方法，给观察者发送通知
	public void notifyObservers(String value) {
		// TODO Auto-generated method stub
		for(int i = 0; i < observers.size(); i++)
		{
			Observer observer = (Observer)observers.get(i);
			observer.update(value);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		notifyObservers(btn.getText());
	}
	
}
