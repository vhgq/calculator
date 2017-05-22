import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;

import org.omg.CORBA.portable.ValueBase;

import Domain.*;
import Observer.Observer;
import Screen.*;

//观察者
public class Calculator extends Frame implements Observer{

	final private int CAL_WIDTH = 257;//计算器宽度
	final private int CAL_HEIGHT = 323;//计算器高度
	private Domain domain;//功能区
	private Screen screen;//显示区
	
	//计算器  运算相关属性
	private String space = new String(" ");//表达式数值与运算符之间的分隔符
	private String editNum = new String();//当前键入的数值
	private String editNumCache = new String("0");//键入值缓存区，当无键入值时，该值等价于键入值
	private String calResult = new String(); //计算结果显示， 只显示一次(如果计算出错，也用该值来反馈错误
	private String exp = new String();//表达式，当键入“=”时，计算该表达式
	private String expCache = new String(); //表达式缓存，缓存上一次计算的表达式（ 除去第一个数值）
	private String op = new String(); //运算符
	
	public static void main(String[] args) {
		
		Calculator cal = new Calculator();
		
		
	}
	
	public Calculator() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init() {
		
		//窗口关闭事件
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				 System.exit(0);
			}
		});
		
		domain = new StandardDomain();
		screen = new StandardScreen();
		domain.registerObserver(this);//注册为观察者，接收来自domain的推送
		
		BorderLayout border = new BorderLayout();
		this.setLayout(border);//边界布局
		this.add("North", screen);
		this.add("Center",  (Component) domain);
		
		this.setSize(CAL_WIDTH,CAL_HEIGHT);//计算器size
		this.setLocationRelativeTo(null);//窗口居中显示
		setResult(); // 初始化计算窗口
		this.setVisible(true);
	}

	
	@Override
	//获取主题(domain)推送的消息,（运算器的按键点击的键值）
	public void update(String value) {
		
		//运算
		Calculate(value);
		
		//set the result to the screen interface
		setResult();
	}
	
	//set the result to the screen interface
	private void setResult(){
		
		//显示计算结果
		if (!calResult.isEmpty()) {
			screen.setEditValue(calResult);
			calResult = "";
		}
		//Screen的EditValue显示数值
	    else if(editNum.isEmpty()){//如果有键入值，显示键入值。  否则显示键入缓存区的值
			
			screen.setEditValue( editNumCache );
		}
		else{
			screen.setEditValue( editNum );
		}
		
		//Screen的TipValue显示数值
		screen.setTipValue( exp + space + op);
	}
	
	//计算
	private void Calculate(String value)
	{
		//得到的数据是数字或小数点
		if(value.equals("0") || value.equals("1") || value.equals("2") || value.equals("3")
				|| value.equals("4") || value.equals("5")  || value.equals("6")  || value.equals("7") 
				|| value.equals("8")  || value.equals("9")  || value.equals(".") )
		{
			_Num(value);
		}
		
		//得到的数据是“删除”一类的操作
		if(value.equals("←")  || value.equals("C")  || value.equals("CE") )
		{
			_Delete(value);
		}
		
		//得到的数据是“+、-、*、/”一类的标准运算符
		if(value.equals("+")  || value.equals("-")  || value.equals("*")  || value.equals("/") )
		{
			_StdOP(value);
		}
		
		//得到的数据是“=”，对表达式进行运算
		if(value.equals("="))
		{
			_Cal();
		}
		
		//得到的数据是“±、√、%、1/x”
		if(value.equals("±") || value.equals("√") || value.equals("%") || value.equals("1/x"))
		{
			_selfOP(value);
		}
	}
	
	//数据处理部分-对获得的数据进行处理
	
	//得到的数据是数字或小数点
	private void _Num(String value) {
		
		if(!editNum.isEmpty())
		{
			
			//将before转换为after的数据，如果合法的话。
			//before:  editNum:1     value:2
			//after：   editNum：12
			
			//如果当前editNum的数值为0，则不允许再输入0
			if(editNum.equals("0"))
			{
				if(value.equals("."))
				    editNum = editNum + value;
				else
					editNum = value;
				return;
			}
				
			//如果当前editNum已经有小数点了，则不允许再输入小数点
			if(value.equals(".") && editNum.indexOf(".")!=-1)
			    return;
			
			//除了以上各种情况，其他都输入合法输入
			editNum = editNum + value ;
				
			
				
		}
		else
		{
			//这是当editNum为空的时候 输入数据的情况
			
			//如果输入小数点，那么默认在前面补0
			if(value.equals("."))
				editNum = new String( "0" + value );
			else
			    editNum = new String(value);
		}
	}
	
	//得到的数据是“删除”一类的操作
	private void _Delete(String value)
	{
		//对← 、 C 、 CE 数据的处理
		
		//删除当前键入值的最后一个字符，相当于撤销操作
		if(value.equals("←") )
		{
			//如果当前键入值为空，不进行操作
			if(editNum.isEmpty())
				return ; 
			
			editNum = editNum.substring(0, editNum.length()-1);
			//当进行了撤销操作后，当前输入值为空，则初始化输入缓存区的值
			if(editNum.isEmpty())
				editNumCache = "0";
				
		}
		
		//清空当前键入值
		if(value.equals("CE") )
		{
			editNum = "";
			editNumCache = "0";
		}
		
		//清空所有
		if(value.equals("C") )
		{
			editNum = "";
			editNumCache = "0";
			op = "";
			exp = "";
			expCache = "";
		}
	}
	
	//得到的数据是+、-、*、/等标准运算符
	private void _StdOP(String value)
	{
		//如果运算符不为空（或者判断表达式不为空）说明不是第一次点击
		if(!op.isEmpty() )
		{
			//如果输入框有数据，则将该数值写入表达式，否则只改变运算符
			if(!editNum.isEmpty())
			{
				String curValue = new String();
				
				if(editNum.indexOf(".") == -1)//输入框为整数
					curValue = editNum;
				else                           //输入框为浮点数
				    curValue = new Double(editNum).toString();
				
				editNumCache = curValue; //写入缓存区
				exp = exp + space + op + space + curValue;//写入表达式
			}
			
			//改变运算符   在该函数的最后一句，因为无论执行什么操作，都要改变运算符
		}
		else   //运算符为空
		{
			String curValue = new String();//
			//如果输入框有数据，那么获取输入框的数据，否则获取输入缓存区的数据（editNumCache）
			if(editNum.isEmpty())
			{
				curValue = editNumCache;
				
			}
			else
			{
				if(editNum.indexOf(".") == -1)//输入框为整数
					curValue = editNum;
				else                           //输入框为浮点数
				    curValue = new Double(editNum).toString();
				
				
			}
			
			exp = curValue; //表达式为该值
			editNumCache = curValue; //缓存区的值默认为该值，
		}
		
		editNum = "";	
		op = value;//改变运算符
		
	}
	
	//表达式计算
	private void _Cal() 
	{
		String curValue;
		if(editNum.isEmpty())
		{
			curValue = editNumCache;
		}
		else
		{
			if(editNum.indexOf(".") == -1)//输入框为整数
				curValue = editNum;
			else                           //输入框为浮点数
			    curValue = new Double(editNum).toString();
		}
		
		editNum = "";
		
		String result;
		
		if(exp.isEmpty() && !expCache.isEmpty())  //表达式为空，表达式缓存不为0
		{
			//System.out.println(curValue + expCache);
			result = getCalResult(curValue + expCache);
			
		}
		else
		{
			//如果运算符为空
			if(op.isEmpty())
				exp = exp + curValue;
			else
				exp = exp + space + op + space + curValue ;
			
			op = ""; 
			result = getCalResult(exp);
		}
		
		
		//一次运算结束，清空表达式和输入框
		exp = "";
		
		
		if(result != null)//如果结果为空，说明运算出错
	   	    editNumCache = result;
	}
	
	//数据处理部分-对获得的数据进行处理
	
	
	//表达式计算部分
	
	/**
	 * @description getCalResult 对表达式进行求值
	 * @param exp 需要进行运算的表达式
	 * @return 运算结果
	 */
	private String getCalResult(String exp)
	{
		String pExp = new String(); //后缀表达式
		String result = new String();//计算结果
		
		//将表达式转换为后缀表达式
		pExp = getPExp(exp);
		
		//计算后缀表达式
		result = getResult(pExp);
		
		if(result != null)
		{
			//通过正则表达式去掉小数点后面多余的0
			if(result.indexOf(".") > 0 && result.indexOf("E") == -1){
		        result = result.replaceAll("0+?$", "");//去掉多余的0
		        result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
		    }
		}
		
		//返回运算结果
		return result;
	}
	
	/**
	 * @description getPExp 将该表达式转换为后缀表达式
	 * @param exp 需要进行转换的表达式
	 * @return 转换后的后缀表达式
	 */
	private String getPExp(String exp)
	{
		String elements[] = exp.split(" ");//exp里面数值跟运算符是用 “ ”隔开，
		
		String pExp = new String();//后缀表达式
		
		Stack<String> stack = new Stack<String>(); //创建一个栈，临时存储运算符
		
		//遍历表达式
		for (int i = 0; i < elements.length; i++) {
			
			//除了第一个数值，其他的存入表达式缓存（expCache）
			if(i==0)
				expCache = "";//先清空
			else
				expCache = expCache + space + elements[i];
			
			if(elements[i].equals("("))                            //如果该元素是“（”
			{
				//直接存入堆栈
				stack.push(elements[i]); 
			}
			else if(elements[i].equals(")"))                           //如果该元素是“）”
			{
				String e;
				//退栈，若栈顶元素不为“（”，则存入后缀表达式，否则丢弃
				while(!(e = stack.pop()).equals("(") )
				{
					pExp = pExp + space + e;
				}
				
			}
			else if(elements[i].equals("+") || elements[i].equals("-"))   //如果该元素是“+”或“-”
			{
				//栈空，直接存入栈中
				if(stack.isEmpty())
				{
					stack.push(elements[i]);
				}
				else    //栈不空，   
				{
					//栈不空
					while(!stack.isEmpty())
					{
						if(!stack.peek().equals("(") )        //栈顶元素不为“（”，弹出栈顶元素写入后缀表达式，
						    pExp = pExp + space + stack.pop();
						else
							break;
					}
					
					//将该符号入栈
					stack.push(elements[i]);
				}
				
			}
			else if (elements[i].equals("*") || elements[i].equals("/"))   //如果该元素是“*”或“/”
			{  
				//栈空，直接存入栈中
				if(stack.isEmpty())
				{
					stack.push(elements[i]);
				}
				else    //栈不空，   
				{
					//栈不空
					while(!stack.isEmpty())
					{
						if(!stack.peek().equals("(") && !stack.peek().equals("+") && !stack.peek().equals("-"))       //栈顶元素不为“（”、“+”、“-”，弹出栈顶元素写入后缀表达式，
						    pExp = pExp + space + stack.pop();
						else
							break;
					}
					
					//将该符号入栈
					stack.push(elements[i]);
				}
				
			}
			else {                                                      //如果元素是数值
				
				//后缀表达式的第一个元素肯定是数值,第一个元素前面不需要分隔符
				if(pExp.isEmpty())
				{
					pExp = elements[i];
				}
				else
					pExp = pExp + space + elements[i];
			}
			
		}
		
		while (!stack.isEmpty()) {
			pExp = pExp + space + stack.pop();
			
		}
		
		return pExp;
	}
	
	/**
	 * @description getResult 获取后缀表达式的计算结果
	 * @param pExp 需要进行计算的后缀表达式
	 * @return 计算结果
	 */
	private String getResult(String pExp)
	{
		String elements[] = pExp.split(" ");  //pExp里面数值跟运算符是用 “ ”隔开，
		Stack<String> result = new Stack<String>();    //存放待计算的数值 , 计算到最后剩下的最后一个数值就是计算结果
		
		String num1; //第一个运算数
		String num2; //第二个运算数
		
		//遍历后缀表达式
		for (int i = 0; i < elements.length; i++) {
			
			
			
			//后缀表达式运算，  先弹出的是第二个运算数，然后才是第一个
			if(elements[i].equals("+"))
			{
				num2 = result.pop();
				num1 = result.pop();
				
				//如果num1和num2都是整数，那么进行整型运算，否则进行浮点数运算
				if(num1.indexOf(".")==-1 && num2.indexOf(".")==-1)
				{
					Integer value = Integer.parseInt(num1) + Integer.parseInt(num2);
					result.push( value.toString() ) ;
				}
				else
				{
					Double value = Double.parseDouble(num1) + Double.parseDouble(num2);
					result.push( value.toString() ) ;
				}
				
			}
			else if (elements[i].equals("-")) {
				
				num2 = result.pop();
				num1 = result.pop();
				
				//如果num1和num2都是整数，那么进行整型运算，否则进行浮点数运算
				if(num1.indexOf(".")==-1 && num2.indexOf(".")==-1)
				{
					Integer value = Integer.parseInt(num1) - Integer.parseInt(num2);
					result.push( value.toString() ) ;
				}
				else
				{
					Double value = Double.parseDouble(num1) - Double.parseDouble(num2);
					result.push( value.toString() ) ;
				}
			}
            else if (elements[i].equals("*")) {
				
				num2 = result.pop();
				num1 = result.pop();
				
				//如果num1和num2都是整数，那么进行整型运算，否则进行浮点数运算
				if(num1.indexOf(".")==-1 && num2.indexOf(".")==-1)
				{
					Integer value = Integer.parseInt(num1) * Integer.parseInt(num2);
					result.push( value.toString() ) ;
				}
				else
				{
					Double value = Double.parseDouble(num1) * Double.parseDouble(num2);
					result.push( value.toString() ) ;
				}
			}
            else if (elements[i].equals("/")) {
            	
				num2 = result.pop();
				num1 = result.pop();
				
				
				
				if(Double.parseDouble(num2) == 0)
				{
					calResult = "除数不能为0";
					return null;
				}
				else
				{
					Double value = Double.parseDouble(num1) / Double.parseDouble(num2);
					result.push( value.toString() ) ;
				}		
				
            }
            else      //数值
            {
            	result.push(elements[i]);
            }
		}
		
		return result.pop();
	}
	
	//得到的数据是“±、√、%、1/x”  对当前数据有影响的符号
	private void _selfOP(String value)
	{
		//如果editNum为空，则作用与editNumCache
		if(editNum.isEmpty())
		{
			//改变正负号
			if(value.equals("±"))
			{
				if(editNumCache.startsWith("-"))  
				{
					editNumCache = editNumCache.substring(1);
				}
				else
					editNumCache = "-" + editNumCache;
			}
			else if(value.equals("√"))
			{
				Double data = Math.sqrt(Double.parseDouble(editNumCache));
				editNumCache = data.toString();
			}
			else if (value.equals("%")) 
			{
				Double data = Double.parseDouble(editNumCache) * 0.01;
				editNumCache = data.toString();
			}
			else if (value.equals("1/x")) 
			{
				Double data = 1 / Double.parseDouble(editNumCache) ;
				editNumCache = data.toString();
			}
		}
		else      //输入框有数据
		{
			//改变正负号
			if(value.equals("±"))
			{
				if(editNum.startsWith("-"))  
				{
					editNum = editNum.substring(1);
				}
				else
					editNum = "-" + editNum;
			}
			else if(value.equals("√"))
			{
				Double data = Math.sqrt(Double.parseDouble(editNum));
				editNum = data.toString();
			}
			else if (value.equals("%")) 
			{
				Double data = Double.parseDouble(editNum) * 0.01;
				editNum = data.toString();
			}
			else if (value.equals("1/x")) 
			{
				Double data = 1 / Double.parseDouble(editNum) ;
				editNum = data.toString();
			}
		}
	}
	
	//表达式计算部分
	
}
