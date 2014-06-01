package com.example.studentassistant;


public class Lexeme {

	private int opClass; //класс
	private int opValue; //значение
	
	public Lexeme()
	{
		opClass = 0;
		opValue = 0;
	}
	
	public Lexeme(int cl, int val)
	{
		opClass = cl;
		opValue = val;
	}
	
	public int getLexemeClass()
	{
		return opClass;
	}
	public int getLexemeValue()
	{
		return opValue;
	}
}
