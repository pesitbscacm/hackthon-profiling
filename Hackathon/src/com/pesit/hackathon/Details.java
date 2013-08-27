package com.pesit.hackathon;

public class Details {
	int _id;
	String _variable;
	String _value;

	// Empty constructor
	public Details() {

	}

	// constructor
	public Details(int id, String variable, String _value) {
		this._id = id;
		this._variable = variable;
		this._value = _value;
	}

	// constructor
	public Details(String variable, String _value) {
		this._variable = variable;
		this._value = _value;
	}

	// getting ID
	public int getID() {
		return _id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting variable
	public String getVariable() {
		return _variable;
	}

	// setting variable
	public void setVariable(String variable) {
		this._variable = variable;
	}

	// getting value
	public String getValue() {
		return _value;
	}

	// setting value
	public void setValue(String value) {
		this._value = value;
	}
}
