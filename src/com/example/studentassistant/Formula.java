package com.example.studentassistant;

import java.util.Vector;


public class Formula
{
	private String str; //�������� ������
	
	private Vector<Double> constants; //��������� ��������
	private Vector<Lexeme> operands; //��������� ���������
	private Vector<Lexeme> operations; //��������� ��������
	
	private Vector<Formula> next; //��������� ������
	
	public boolean integralSucceeded = false;
	
	public Formula()
	{
		
	}
	
	public Formula(String s)
	{
		str = "";
		constants = new Vector<Double>(); //��������� ��������
		operands = new Vector<Lexeme>(); //��������� ���������
		operations = new Vector<Lexeme>(); //��������� ��������
		
		//�������� ��������
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i) != ' ') //������ � ������ �������� ��������
				str += s.charAt(i);
		}
		
		str = str.toLowerCase();
						
	}
	
	public boolean parse()
	{
		int index = 0; //������ �������
		int brackets = 0; //���������� ������
		
		String buf = ""; //������������� ����������: ������ � ����������
		
		boolean parseConstant = false; //���� ������� ���������
		boolean pointParsed = false; //����� ��� �����������
		int prevLexem = -1; //���������� �������: -1 - �� ����; 0 - ���������; 1 - �����������; 2 - ��������; 3 - ������; 4 - exp(); 5 - sqrt(); 6 - sin(); 7 - cos(); 8 - tg(); 9 - ctg(); 10 - asin(); 11 - acos(); 12 - atg(); 13 - actg(); 14 - lg(); 15 - ln(); 16 - abs();
		int idConstant = 0; //id ���������
		int idBracket = 0;
		
		//������� ������
		for(int i = 0; i < str.length(); i++)
		{			
			if(str.charAt(i) == '(') //������ � ������ �������� ������������� �������
				brackets++;
			else if(str.charAt(i) == ')') //������ � ������ �������� ������������� �������
				brackets--;
			
			if(brackets < 0) //�������� ������������� ������ ����� �������������
				break;
		}
		
		System.out.println("String is \"" + str + "\"");
		
		//�������� ���������� �������������/������������� ������
		if(brackets != 0)
			return false;
		
		while(true)
		{
			if(index == str.length())
			{	
				if(prevLexem == -1 || prevLexem == 2)
					return false;
				
				break;
			}
			
			if((str.charAt(index) >= '0' && str.charAt(index) <= '9') || str.charAt(index) == '.')
			{
				if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
					operations.add(new Lexeme(2, 2));
				
				while(true)
				{
					if(index == str.length())
					{
						if(parseConstant)
						{
							if(buf.length() == 1 && buf.charAt(0) == '.')
								return false;
							
							constants.add(Double.valueOf(buf));
							
							operands.add(new Lexeme(0, idConstant));
							idConstant++;
							
							parseConstant = false;
							pointParsed = false;
							buf = "";
							prevLexem = 0;
						}
						
						break;
					}
					
					if((str.charAt(index) >= '0' && str.charAt(index) <= '9') || str.charAt(index) == '.')
					{
						if(str.charAt(index) == '.' && pointParsed)
							return false;
						else if(str.charAt(index) == '.')
							pointParsed = true;
						
						buf += str.charAt(index);
						parseConstant = true;
						index++;
					}
					else if(parseConstant)
					{
						if(buf.length() == 1 && buf.charAt(0) == '.')
							return false;
						
						constants.add(Double.valueOf(buf));
						
						operands.add(new Lexeme(0, idConstant));
						idConstant++;
						
						parseConstant = false;
						pointParsed = false;
						buf = "";
						prevLexem = 0;
						
						break;
					}
				}
				
				continue;
			}
			else if(str.charAt(index) == 'x')
			{
				if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
					operations.add(new Lexeme(2, 2));
				
				operands.add(new Lexeme(1, 0));
				index++;
				prevLexem = 1;
				
				continue;
			}
			else if(str.charAt(index) == '+')
			{
				if(prevLexem == -1 || prevLexem == 2)
					return false;
				
				operations.add(new Lexeme(2, 0));				
				index++;
				prevLexem = 2;
				
				continue;
			}
			else if(str.charAt(index) == '-')
			{
				if(prevLexem == -1 || prevLexem == 2)
				{
					boolean signParsed = false; //���� ��� ��������
					
					while(true)
					{
						if(index == str.length())
						{
							if(parseConstant)
							{
								if(buf.length() == 1 && buf.charAt(0) == '.' || buf.length() == 2 && buf.charAt(0) == '-' && buf.charAt(1) == '.' || buf.length() == 1 && buf.charAt(0) == '-')
									return false;
								
								if(buf.length() == 1 && buf.charAt(0) == '-')
									buf = "-1";
								
								constants.add(Double.valueOf(buf));
								
								operands.add(new Lexeme(0, idConstant));
								idConstant++;
								
								parseConstant = false;
								pointParsed = false;
								buf = "";
								prevLexem = 0;
							}
							
							break;
						}
						
						if((str.charAt(index) >= '0' && str.charAt(index) <= '9') || str.charAt(index) == '.' || str.charAt(index) == '-' && !signParsed)
						{
							if(str.charAt(index) == '.' && pointParsed)
								return false;							
							else if(str.charAt(index) == '.')
								pointParsed = true;
													
							if(str.charAt(index) == '-')
								signParsed = true;
							
							buf += str.charAt(index);
							parseConstant = true;
							index++;
						}
						else if(parseConstant)
						{
							if(buf.length() == 1 && buf.charAt(0) == '.' || buf.length() == 2 && buf.charAt(0) == '-' && buf.charAt(1) == '.')
								return false;
							
							if(buf.length() == 1 && buf.charAt(0) == '-')
								buf = "-1";
							
							constants.add(Double.valueOf(buf));
							
							operands.add(new Lexeme(0, idConstant));
							idConstant++;
							
							parseConstant = false;
							pointParsed = false;
							buf = "";
							prevLexem = 0;
							
							break;
						}
					}
				}
				else if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
				{				
					operations.add(new Lexeme(2, 1));				
					index++;
					prevLexem = 2;
				}
				else
					return false;
				
				continue;
			}
			else if(str.charAt(index) == '*')
			{
				if(prevLexem == -1 || prevLexem == 2)
					return false;
				
				operations.add(new Lexeme(2, 2));				
				index++;
				prevLexem = 2;
				
				continue;
			}
			else if(str.charAt(index) == '/')
			{
				if(prevLexem == -1 || prevLexem == 2)
					return false;
				
				operations.add(new Lexeme(2, 3));				
				index++;
				prevLexem = 2;
				
				continue;
			}
			else if(str.charAt(index) == '^')
			{
				if(prevLexem == -1 || prevLexem == 2)
					return false;
				
				operations.add(new Lexeme(2, 4));				
				index++;
				prevLexem = 2;
				
				continue;
			}
			else if(str.charAt(index) == '(')
			{
				if(next == null)
					next = new Vector<Formula>();
				
				if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
					operations.add(new Lexeme(2, 2));
				
				int beginIndex = index; //��������� ������ ���������
				brackets = 1;
				while(brackets != 0)
				{
					index++;
					
					if(str.charAt(index) == '(')
						brackets++;
					else if(str.charAt(index) == ')')
						brackets--;
				}
				
				index++;
				
				buf = str.substring(beginIndex + 1, index - 1);
				System.out.println("------------bracket (" + buf + ")------------");
				next.add(new Formula(buf));
				if(!next.get(idBracket).parse())
					return false;
				
				System.out.println("------------end (" + buf + ")----------------");
				operands.add(new Lexeme(3, idBracket));
				idBracket++;
				prevLexem = 3;
				
				buf = "";
			}
			else if(str.charAt(index) == 'p')
			{
				index++;
				if(index == str.length())
					return false;
				else if(str.charAt(index) == 'i')
				{
					if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
						operations.add(new Lexeme(2, 2));
					
					constants.add(Double.valueOf(Math.PI));
					
					operands.add(new Lexeme(0, idConstant));
					idConstant++;
					
					prevLexem = 0;
					
					index++;
				}
				else
					return false;
			}
			else if(str.charAt(index) == 'e')
			{
				index++;
				if(index == str.length())
				{
					if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
						operations.add(new Lexeme(2, 2));
					
					constants.add(Double.valueOf(Math.E));
					
					operands.add(new Lexeme(0, idConstant));
					idConstant++;
					
					prevLexem = 0;
				}				
				else if(str.charAt(index) == 'x')
				{
					index++;
					
					if(index == str.length() || str.charAt(index) != 'p')
					{
						if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
							operations.add(new Lexeme(2, 2));
						
						operations.add(new Lexeme(2, 2));
						constants.add(Double.valueOf(Math.E));
						operands.add(new Lexeme(0, idConstant));						
						operands.add(new Lexeme(1, 0));

						prevLexem = 1;
						
						continue;
					}
					else if(str.charAt(index) == 'p')
					{
						index++;
						
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(4, idBracket));
							idBracket++;							
							buf = "";
							prevLexem = 4;
						}
						else
							return false;
												
						continue;							
					}
				}
				else
				{
					if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
						operations.add(new Lexeme(2, 2));
					
					constants.add(Double.valueOf(Math.E));
					
					operands.add(new Lexeme(0, idConstant));
					idConstant++;
					
					prevLexem = 0;
				}
			}
			else if(str.charAt(index) == 's')
			{
				index++;				
				if(index == str.length())
					return false;
				
				if(str.charAt(index) == 'q')
				{
					index++;					
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'r')
					{
						index++;					
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == 't')
						{
							index++;
							if(index == str.length())
								return false;
							
							if(str.charAt(index) == '(')
							{
								if(next == null)
									next = new Vector<Formula>();
								
								if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
									operations.add(new Lexeme(2, 2));
								
								int beginIndex = index; //��������� ������ ���������
								brackets = 1;
								while(brackets != 0)
								{
									index++;
									
									if(str.charAt(index) == '(')
										brackets++;
									else if(str.charAt(index) == ')')
										brackets--;
								}
								
								index++;
								
								buf = str.substring(beginIndex + 1, index - 1);
								System.out.println("------------bracket (" + buf + ")------------");
								next.add(new Formula(buf));
								if(!next.get(idBracket).parse())
									return false;
								
								System.out.println("------------end (" + buf + ")----------------");
								operands.add(new Lexeme(5, idBracket));
								idBracket++;				
								buf = "";
								prevLexem = 5;
							}
							else
								return false;
						}
						else
							return false;
					}
					else
						return false;
					
				}
				else if(str.charAt(index) == 'i')
				{
					index++;					
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'n')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(6, idBracket));
							idBracket++;
							buf = "";
							prevLexem = 6;
						}
						else
							return false;
					}
					else
						return false;
				}
				else
					return false;
				
				continue;
			}
			else if(str.charAt(index) == 'c')
			{
				index++;
				if(index == str.length())
					return false;
				
				if(str.charAt(index) == 'o')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 's')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(7, idBracket));
							idBracket++;
							buf = "";
							prevLexem = 7;
						}
						else
							return false;						
					}
					else
						return false;
				}
				else if(str.charAt(index) == 't')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'g')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(9, idBracket));
							idBracket++;
							buf = "";
							prevLexem = 9;
						}
						else
							return false;						
					}
					else
						return false;
				}
				else
					return false;
				
				continue;
			}
			else if(str.charAt(index) == 't')
			{
				index++;
				if(index == str.length())
					return false;
				
				if(str.charAt(index) == 'g')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == '(')
					{
						if(next == null)
							next = new Vector<Formula>();
						
						if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
							operations.add(new Lexeme(2, 2));
						
						int beginIndex = index; //��������� ������ ���������
						brackets = 1;
						while(brackets != 0)
						{
							index++;
							
							if(str.charAt(index) == '(')
								brackets++;
							else if(str.charAt(index) == ')')
								brackets--;
						}
						
						index++;
						
						buf = str.substring(beginIndex + 1, index - 1);
						System.out.println("------------bracket (" + buf + ")------------");
						next.add(new Formula(buf));
						if(!next.get(idBracket).parse())
							return false;
						
						System.out.println("------------end (" + buf + ")----------------");
						operands.add(new Lexeme(8, idBracket));
						idBracket++;
						buf = "";
						prevLexem = 8;
					}
					else
						return false;						
				}
				else
					return false;
				
				continue;
			}
			else if(str.charAt(index) == 'a')
			{
				index++;
				if(index == str.length())
					return false;
				
				if(str.charAt(index) == 's')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'i')
					{
						index++;					
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == 'n')
						{
							index++;
							if(index == str.length())
								return false;
							
							if(str.charAt(index) == '(')
							{
								if(next == null)
									next = new Vector<Formula>();
								
								if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
									operations.add(new Lexeme(2, 2));
								
								int beginIndex = index; //��������� ������ ���������
								brackets = 1;
								while(brackets != 0)
								{
									index++;
									
									if(str.charAt(index) == '(')
										brackets++;
									else if(str.charAt(index) == ')')
										brackets--;
								}
								
								index++;
								
								buf = str.substring(beginIndex + 1, index - 1);
								System.out.println("------------bracket (" + buf + ")------------");
								next.add(new Formula(buf));
								if(!next.get(idBracket).parse())
									return false;
								
								System.out.println("------------end (" + buf + ")----------------");
								operands.add(new Lexeme(10, idBracket));
								idBracket++;
								buf = "";
								prevLexem = 10;
							}
							else
								return false;
						}
						else
							return false;
					}
					else
						return false;
				}
				else if(str.charAt(index) == 'c')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'o')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == 's')
						{
							index++;
							if(index == str.length())
								return false;
							
							if(str.charAt(index) == '(')
							{
								if(next == null)
									next = new Vector<Formula>();
								
								if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
									operations.add(new Lexeme(2, 2));
								
								int beginIndex = index; //��������� ������ ���������
								brackets = 1;
								while(brackets != 0)
								{
									index++;
									
									if(str.charAt(index) == '(')
										brackets++;
									else if(str.charAt(index) == ')')
										brackets--;
								}
								
								index++;
								
								buf = str.substring(beginIndex + 1, index - 1);
								System.out.println("------------bracket (" + buf + ")------------");
								next.add(new Formula(buf));
								if(!next.get(idBracket).parse())
									return false;
								
								System.out.println("------------end (" + buf + ")----------------");
								operands.add(new Lexeme(11, idBracket));
								idBracket++;
								buf = "";
								prevLexem = 11;
							}
							else
								return false;						
						}
						else
							return false;
					}
					else if(str.charAt(index) == 't')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == 'g')
						{
							index++;
							if(index == str.length())
								return false;
							
							if(str.charAt(index) == '(')
							{
								if(next == null)
									next = new Vector<Formula>();
								
								if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
									operations.add(new Lexeme(2, 2));
								
								int beginIndex = index; //��������� ������ ���������
								brackets = 1;
								while(brackets != 0)
								{
									index++;
									
									if(str.charAt(index) == '(')
										brackets++;
									else if(str.charAt(index) == ')')
										brackets--;
								}
								
								index++;
								
								buf = str.substring(beginIndex + 1, index - 1);
								System.out.println("------------bracket (" + buf + ")------------");
								next.add(new Formula(buf));
								if(!next.get(idBracket).parse())
									return false;
								
								System.out.println("------------end (" + buf + ")----------------");
								operands.add(new Lexeme(13, idBracket));
								idBracket++;
								buf = "";
								prevLexem = 13;
							}
							else
								return false;						
						}
						else
							return false;
					}
					else
						return false;
					
				}
				else if(str.charAt(index) == 't')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 'g')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(12, idBracket));
							idBracket++;
							buf = "";
							prevLexem = 12;
						}
						else
							return false;						
					}
					else
						return false;		
				}
				else if(str.charAt(index) == 'b')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == 's')
					{
						index++;
						if(index == str.length())
							return false;
						
						if(str.charAt(index) == '(')
						{
							if(next == null)
								next = new Vector<Formula>();
							
							if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
								operations.add(new Lexeme(2, 2));
							
							int beginIndex = index; //��������� ������ ���������
							brackets = 1;
							while(brackets != 0)
							{
								index++;
								
								if(str.charAt(index) == '(')
									brackets++;
								else if(str.charAt(index) == ')')
									brackets--;
							}
							
							index++;
							
							buf = str.substring(beginIndex + 1, index - 1);
							System.out.println("------------bracket (" + buf + ")------------");
							next.add(new Formula(buf));
							if(!next.get(idBracket).parse())
								return false;
							
							System.out.println("------------end (" + buf + ")----------------");
							operands.add(new Lexeme(16, idBracket));
							idBracket++;
							buf = "";
							prevLexem = 16;
						}
						else
							return false;						
					}
					else
						return false;		
				}
				else
					return false;				
					
				continue;
			}
			else if(str.charAt(index) == 'l')
			{
				index++;
				if(index == str.length())
					return false;
				
				if(str.charAt(index) == 'g')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == '(')
					{
						if(next == null)
							next = new Vector<Formula>();
						
						if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
							operations.add(new Lexeme(2, 2));
						
						int beginIndex = index; //��������� ������ ���������
						brackets = 1;
						while(brackets != 0)
						{
							index++;
							
							if(str.charAt(index) == '(')
								brackets++;
							else if(str.charAt(index) == ')')
								brackets--;
						}
						
						index++;
						
						buf = str.substring(beginIndex + 1, index - 1);
						System.out.println("------------bracket (" + buf + ")------------");
						next.add(new Formula(buf));
						if(!next.get(idBracket).parse())
							return false;
						
						System.out.println("------------end (" + buf + ")----------------");
						operands.add(new Lexeme(14, idBracket));
						idBracket++;
						buf = "";
						prevLexem = 14;
					}
					else
						return false;						
				}
				else if(str.charAt(index) == 'n')
				{
					index++;
					if(index == str.length())
						return false;
					
					if(str.charAt(index) == '(')
					{
						if(next == null)
							next = new Vector<Formula>();
						
						if(prevLexem == 0 || prevLexem == 1 || (prevLexem >= 3 && prevLexem <= 16))
							operations.add(new Lexeme(2, 2));
						
						int beginIndex = index; //��������� ������ ���������
						brackets = 1;
						while(brackets != 0)
						{
							index++;
							
							if(str.charAt(index) == '(')
								brackets++;
							else if(str.charAt(index) == ')')
								brackets--;
						}
						
						index++;
						
						buf = str.substring(beginIndex + 1, index - 1);
						System.out.println("------------bracket (" + buf + ")------------");
						next.add(new Formula(buf));
						if(!next.get(idBracket).parse())
							return false;
						
						System.out.println("------------end (" + buf + ")----------------");
						operands.add(new Lexeme(15, idBracket));
						idBracket++;
						buf = "";
						prevLexem = 15;
					}
					else
						return false;						
				}
				else
					return false;
				
				continue;
			}
			else
				return false;
			
			//index++;
		}
		
		System.out.print("Constants: ");
		for(int i = 0; i < constants.size(); i++)
			System.out.print(constants.get(i) + " ");
		System.out.println();
		
		System.out.print("Operands: ");
		for(int i = 0; i < operands.size(); i++)
			System.out.print("(" + operands.get(i).getLexemeClass() + "; " + operands.get(i).getLexemeValue() + ") ");
		System.out.println();
		
		System.out.print("Operations: ");
		for(int i = 0; i < operations.size(); i++)
			System.out.print("(" + operations.get(i).getLexemeClass() + "; " + operations.get(i).getLexemeValue() + ") ");
		System.out.println();
		
		return true;
	}
	
	//��������� �������� �������
	public double calculate(double x)
	{
		Vector<Double> opValues = new Vector<Double>(); //��������� �������� ���������
		//opValues.setSize(operands.size()); //��������� ������� ���������� �������� ���������
		
		//��������� ��������� �������� ���������
		for(int i = 0; i < operands.size(); i++)
		{
			//������������� ��������
			if(operands.get(i).getLexemeClass() == 0)
			{//���������
				opValues.add(new Double(constants.get(operands.get(i).getLexemeValue())));
			}
			else if(operands.get(i).getLexemeClass() == 1)
			{//�����������
				opValues.add(new Double(x));
			}
			else if(operands.get(i).getLexemeClass() == 3)
			{//������
				opValues.add(new Double(next.get(operands.get(i).getLexemeValue()).calculate(x)));
			}
			else if(operands.get(i).getLexemeClass() >= 4 && operands.get(i).getLexemeClass() <= 16)
			{//�������
				double tmp = next.get(operands.get(i).getLexemeValue()).calculate(x);
				
				//����� �������
				switch(operands.get(i).getLexemeClass())
				{
				case 4:
					tmp = Math.exp(tmp);
					break;
				case 5:
					tmp = Math.sqrt(tmp);
					break;
				case 6:
					tmp = Math.sin(tmp);
					break;
				case 7:
					tmp = Math.cos(tmp);
					break;
				case 8:
					tmp = Math.tan(tmp);
					break;
				case 9:
					tmp = 1/Math.tan(tmp);
					break;
				case 10:
					tmp = Math.asin(tmp);
					break;
				case 11:
					tmp = Math.acos(tmp);
					break;
				case 12:
					tmp = Math.atan(tmp);
					break;
				case 13:
					tmp = Math.PI/2 - Math.atan(tmp);
					break;
				case 14:
					tmp = Math.log10(tmp);
					break;
				case 15:
					tmp = Math.log(tmp);
					break;
				case 16:
					tmp = Math.abs(tmp);
					break;
				}
				
				opValues.add(new Double(tmp)); //����������
			}
		}
		
		Vector<Lexeme> tmpOperations = new Vector<Lexeme>(operations); //��������� ��������
		
		//���������� �������� ���������� � �������
		for(int i = 0; i < tmpOperations.size(); i++)
		{
			if(tmpOperations.get(i).getLexemeValue() == 4)
			{
				//����������� ��������
				double tmp = Math.pow(opValues.get(i), opValues.get(i + 1));
				
				//������� ��� �������� �� ���������� � ��������� �����
				opValues.remove(i);
				opValues.remove(i);
				opValues.add(i, new Double(tmp));
				
				//������� �������� �� ���������� ���������
				tmpOperations.remove(i);
				
				i--;
			}
		}
		
		//���������� �������� ��������� � �������
		for(int i = 0; i < tmpOperations.size(); i++)
		{
			if(tmpOperations.get(i).getLexemeValue() == 2 || tmpOperations.get(i).getLexemeValue() == 3)
			{
				double tmp = 0;
				
				//����������� ��������
				if(tmpOperations.get(i).getLexemeValue() == 2)
					tmp = opValues.get(i)*opValues.get(i + 1);
				else if(tmpOperations.get(i).getLexemeValue() == 3)
					tmp = opValues.get(i)/opValues.get(i + 1);
				
				//������� ��� �������� �� ���������� � ��������� �����
				opValues.remove(i);
				opValues.remove(i);
				opValues.add(i, new Double(tmp));
				
				//������� �������� �� ���������� ���������
				tmpOperations.remove(i);
				
				i--;
			}
		}
		//���������� �������� �������� � ���������
		for(int i = 0; i < tmpOperations.size(); i++)
		{
			double tmp = 0;
			
			//����������� ��������
			if(tmpOperations.get(i).getLexemeValue() == 0)
				tmp = opValues.get(i) + opValues.get(i + 1);
			else if(tmpOperations.get(i).getLexemeValue() == 1)
				tmp = opValues.get(i) - opValues.get(i + 1);
			
			//������� ��� �������� �� ���������� � ��������� �����
			opValues.remove(i);
			opValues.remove(i);
			opValues.add(i, new Double(tmp));
			
			//������� �������� �� ���������� ���������
			tmpOperations.remove(i);
			
			i--;
		}
		
		return opValues.get(0);
	}
	
	//���������� ���������
	public double integral(double a, double b, int n, double e)
	{
		double result; //��������� ���������� ���������

		n /= 2; //��������� �������� n ��� �����

		do
		{//������� ��������
			if(n > 10000000)
			{
				integralSucceeded = false;
				return 0;
			}
			
			n *= 2;
			result = simpson(a, b, n);

		}
		while(Math.abs((simpson(a, b, 2*n) - result))/1.5 > e); //���� ������ ���������� ������ �������� ������

		if(Double.isInfinite(result) || Double.isNaN(result))
		{
			integralSucceeded = false;
		}
		else
		{
			integralSucceeded = true;
		}
			
		return result;
	}
	
	//������ �������� ��������� ������� ��������
	private double simpson(double a, double b, int n)
	{
		double x; //��������������� �����
		int i; //���������� �����
		double result; //��������� ����������
		double h; //���

		h = (b - a)/(double)n; //������ ����
		
		x = a + h; //��������� �������� x
		result = 0; //��������� �������� ����������
		
		//������ �������� ��������� �� ������� ��������
		for(i = 0; i < n - 1; i = i + 2)
		{
			result += 4*this.calculate(x);
			x += 2*h;
		}

		x = a + 2*h; //��������� �������� x

		for(i = 1; i < n - 2; i = i + 2)
		{
			result += 2*this.calculate(x);
			x += 2*h;
		}

		result += this.calculate(a) + this.calculate(b);
		result *= (h/3);

		return result; //���������� ��������� ����������
	}
	
	/*public static boolean isDigit(char c)
	{
		return (c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6')
				|| (c == '7') || (c == '8') || (c == '9');
	}	*/
}
