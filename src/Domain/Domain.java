package Domain;

import java.util.ArrayList;

import javax.swing.JPanel;

import Observer.Observer;
import Observer.Subject;

public abstract class Domain extends JPanel implements Subject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<Observer> observers;
	
	public Domain() {
		// TODO Auto-generated constructor stub
		observers = new ArrayList<Observer>();
	}
	public void registerObserver(Observer o){
		observers.add(o);
	}
	
	public void removeObserver(Observer o){
		observers.remove(o);
	}
}


