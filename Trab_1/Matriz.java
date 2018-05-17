public class Matriz {

	private String direction; // left, up, diag
	private int value;

	Matriz(String d, int v) {
		direction = d;
		value = v;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String d) {
		direction = d;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int v) {
		value = v;
	}
}
