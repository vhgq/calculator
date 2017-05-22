package Observer;

public interface Observer {
	public void update(String value); //当主题状态发送改变时，主题会执行这个方法将状态值作为参数传递给观察者
}
