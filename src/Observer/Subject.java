package Observer;
import Observer.Observer;

public interface Subject {
    public void registerObserver(Observer o);//注册观察者
    public void removeObserver(Observer o);//删除观察者
    public void notifyObservers(String value); //当主题状态改变时，这个方法会被调用，以通知所有的观察者
}
