package readInesData;

public class DataVariables<T> {
	
	private T variable;
	private String name;
	
	public DataVariables() {
		name = "";
	}
	
	public T getVariable() {
		return variable;
	}

	public void setVariable(T variable) {
		this.variable = variable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
