package readInesData;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DataVariables<T> {
	
	private ArrayList<T> variables;
	private String name;
	private ArrayList<Pattern> regexp;
	
	public DataVariables() {
		name = "DataVariable";
		variables = new ArrayList<T>();
		regexp = new ArrayList<Pattern>();
	}
	
	public ArrayList<T> getVariables() {
		return variables;
	}
	
	public void addVariable(T variable, String regexp) {
		this.variables.add(variable);
		this.regexp.add(Pattern.compile(regexp));
	}
	
	public void removeVariable(T variable, String regexp) {
		this.variables.remove(variable);
		this.regexp.remove(Pattern.compile(regexp));
	}
	
	public void removeVariable(int index) {
		this.variables.remove(index);
		this.regexp.remove(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Pattern> getRegexp() {
		return regexp;
	}
}
