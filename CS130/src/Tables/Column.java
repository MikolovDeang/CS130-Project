package Tables;

import java.util.ArrayList;

public class Column {
	ArrayList<Token> tokens = new ArrayList<Token>();

	public boolean simplify()
	{
		if(tokens.get(0).getId().equals("LBRACKET") && tokens.get(tokens.size() - 1).getId().equals("RBRACKET"))
		{
			tokens.remove(tokens.size() - 1);
			tokens.remove(0);
			return true;
		}
		return false;
	}
	
	public void evaluate()
	{
		if(tokens.get(0).getId().equals("EQUALS"))
		{
			double n = 0;
			ArrayList<Token> temp = new ArrayList<Token>();
			for(int i = 1; i < tokens.size(); i++)
			{
				String id = tokens.get(i).getId(), lexeme = tokens.get(i).getLexeme();
				temp.add(new Token(id,lexeme));
			}
			tokens.remove(0);
			n = math(temp);
			tokens.clear();
			tokens.add(new Token("NUMBER",Double.toString(n)));
		}
	}
	
	public double math(ArrayList<Token> t)
	{
		boolean LPARENFound = false;
		int s = 0;
		loop:
		while(s < t.size())
		{
			if(t.get(s).getId().equals("LPAREN"))
			{
				LPARENFound = true;
				break loop;
			}
			s++;
		}
		if(LPARENFound)
		{
			int leftCounter = 1, rightCounter = 0, i = s + 1;
			loop:
			while(i < t.size())
			{
				if(t.get(i).getId().equals("LPAREN"))
				{
					leftCounter++;
				}
				else if(t.get(i).getId().equals("RPAREN"))
				{
					rightCounter++;
					if(rightCounter == leftCounter)
					{
						break loop;
					}
				}
				i++;
			}
			ArrayList<Token> temp = new ArrayList<Token>();
			for(int h = s + 1; h < i; h++)
			{
				temp.add(t.get(h));
			}
			ArrayList<Token> rest = new ArrayList<Token>();
			rest.add(new Token("NUMBER",Double.toString(math(temp))));
			for(int h = i + 1; h < t.size(); h++)
			{
				rest.add(t.get(h));
			}
			return math(rest);
		}
		else
		{
			for(int i = 0; i < t.size(); i++)
			{
				if(t.get(i).getId().equals("EXP"))
				{
					double n = Math.pow(Double.parseDouble(t.get(i-1).getLexeme()),Double.parseDouble(t.get(i+1).getLexeme()));
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
			}
			for(int i = 0; i < t.size(); i++)
			{
				if(t.get(i).getId().equals("MULT"))
				{
					double n = Double.parseDouble(t.get(i-1).getLexeme()) * Double.parseDouble(t.get(i+1).getLexeme());
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
				else if(t.get(i).getId().equals("DIVIDE"))
				{
					double n = Double.parseDouble(t.get(i-1).getLexeme()) / Double.parseDouble(t.get(i+1).getLexeme());
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
				else if(t.get(i).getId().equals("MODULO"))
				{
					double n = Double.parseDouble(t.get(i-1).getLexeme()) % Double.parseDouble(t.get(i+1).getLexeme());
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
			}
			for(int i = 0; i < t.size(); i++)
			{
				if(t.get(i).getId().equals("PLUS"))
				{
					double n = Double.parseDouble(t.get(i-1).getLexeme()) + Double.parseDouble(t.get(i+1).getLexeme());
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
				else if(t.get(i).getId().equals("MINUS"))
				{
					double n = Double.parseDouble(t.get(i-1).getLexeme()) - Double.parseDouble(t.get(i+1).getLexeme());
					Token temp = new Token("NUMBER",Double.toString(n));
					t.remove(i-1); t.remove(i-1); t.remove(i-1);
					t.add(i-1, temp);
				}
			}
			return Double.parseDouble(t.get(0).getLexeme());
		}
	}
	
	public void addToken(Token t)
	{
		tokens.add(t);
	}
	
	public void printColumn()
	{
		if (!simplify())
		{
			evaluate();
		}
		for(int i = 0; i < tokens.size(); i++)
		{
			System.out.print(tokens.get(i).getLexeme());
			if(i + 1 < tokens.size())
			{
				System.out.print(" ");
			}
		}
	}
}
