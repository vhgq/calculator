package Screen;

import javax.swing.JPanel;

/**
 * 
 * @author LolitaC
 */
public abstract class Screen extends JPanel{
	
	 //tipValue
	//protected String tipValue = null;
	public abstract String getTipValue();
	public abstract Boolean setTipValue(String value);
	
	//editValue
	//protected String editValue = "0";
	public abstract String getEditValue();
	public abstract Boolean setEditValue(String value);
	
}
