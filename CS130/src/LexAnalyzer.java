import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

import Tables.*;

public class LexAnalyzer {
	static ArrayList<Token> tokens = new ArrayList<Token>();
	static String input;
	public static void main(String[] args) {
	    try { 
	        String s = "";
	        @SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader(new File("Input.txt")));
	        while((s = in.readLine()) != null) {
	            input += s;
	        }
	        input = input.trim();
	        while(input.length() != 0)
	        {
	        	Token t = getNewToken();
	        	if(t != null && t.getLexeme() != null && t.getLexeme().trim().length() != 0)
	        		tokens.add(t);
	        }
        	try {System.setOut(new PrintStream(new File("Lex-output.txt")));} catch (Exception e) { e.printStackTrace();}
	        System.out.println("TOKEN       LEXEME");
	        System.out.println("------------------");
	        for(int i = 0; i < tokens.size(); i++) {
	            System.out.println(tokens.get(i).getIDLexeme());
	        }
	        
	        ArrayList<Table> tables = new ArrayList<Table>();
	        while(tokens.size() > 0)
	        {
	        	tables.add(tableComponent());
	        }
	        System.out.println("------------------");
	        try {System.setOut(new PrintStream(new File("Parser-output.csv")));} catch (Exception e) { e.printStackTrace();}
	        for(int i = 0; i < tables.size(); i++)
	        {
	        	tables.get(i).printTable();
	        }
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void remove(int i)
	{
		for(int h = 0; h < i; h++)
		{
			tokens.remove(0);
		}
	}
	public static Table tableComponent()
	{
		Table temp = new Table();
    	if(tokens.get(0).getLexeme().equals("<table") && tokens.get(1).getId().equals("GTHAN"))
    	{
    		remove(2);
    		while(!(tokens.get(0).getId().equals("ENDTAGHEAD") && tokens.get(1).getLexeme().equals("table") && tokens.get(2).getId().equals("GTHAN"))) 
    		{
    			temp.addRow(rowComponent());
    		}
    		remove(3);
    	}
    	return temp;
	}
	public static Row rowComponent()
	{
		Row temp = new Row();
		if(tokens.get(0).getLexeme().equals("<tr") && tokens.get(1).getId().equals("GTHAN"))
		{
			remove(2);
			while(!(tokens.get(0).getId().equals("ENDTAGHEAD") && tokens.get(1).getLexeme().equals("tr") && tokens.get(2).getId().equals("GTHAN"))) 
			{
				temp.addColumn(columnComponent());
			}
			remove(3);
		}
		return temp;
	}
	public static Column columnComponent()
	{
		Column temp = new Column();
		if(tokens.get(0).getLexeme().equals("<th") && tokens.get(1).getId().equals("GTHAN"))
    	{
    		remove(2);
    		while(!(tokens.get(0).getId().equals("ENDTAGHEAD") && tokens.get(1).getLexeme().equals("th") && tokens.get(2).getId().equals("GTHAN"))) 
    		{
    			temp.addToken(tokens.get(0));
    			remove(1);
    		}
    		remove(3);
    	}
    	else if(tokens.get(0).getLexeme().equals("<td") && tokens.get(1).getId().equals("GTHAN"))
    	{
    		remove(2);
    		while(!(tokens.get(0).getId().equals("ENDTAGHEAD") && tokens.get(1).getLexeme().equals("td") && tokens.get(2).getId().equals("GTHAN"))) 
    		{
    			temp.addToken(tokens.get(0));
    			remove(1);
    		}
    		remove(3);
    	}
    	return temp;
	}
	public static Token getNewToken()
	{
		if(input.charAt(0) == '<')
		{
			if(input.charAt(1) == '/')
			{
				input = input.substring(2, input.length());
				return new Token("ENDTAGHEAD","</");
			}
			else if(input.substring(0, 4).equals("<!--"))
			{
				int i = 4;
				while(!input.substring(i, i + 3).equals("-->"))
				{
					i++;
				}
				input = input.substring(i + 3, input.length());
				return null;
			}
			else if(isNumeric(input.substring(1,3).trim()) || isNumeric(input.substring(1,2).trim()))
			{
				input = input.substring(1, input.length());
				return new Token("LTHAN","<");
			}
			
			String lexeme = "";
			int i = 0;
			while(input.charAt(i) != '>')
			{
				lexeme += input.charAt(i++);
			}
			input = input.substring(i, input.length());
			return new Token("TAGIDENT",lexeme);
		}
		else if(input.charAt(0) == '>')
		{
			input = input.substring(1, input.length());
			return new Token("GTHAN",">");
		}
		else
		{
			Token temp = checkToken(0);
			if(temp != null)
			{
				input = input.substring(1, input.length());
				return temp;
			}
			else if(input.subSequence(0, 3).equals("**"))
			{
				input = input.substring(2, input.length());
				return new Token("EXP","**");
			}
			String lexeme = "";
			int i = 0;
			while(input.charAt(i) != '<' && input.charAt(i) != '>' && checkToken(i) == null)
			{
				lexeme += input.charAt(i++);
				if(i == input.length())
					break;
			}
			input = input.substring(i, input.length());
			try {
				Double.parseDouble(lexeme);
	            return new Token("NUMBER",lexeme);
	        } catch (NumberFormatException e) {
	        	if(lexeme.equals(null) || lexeme.equals("null"))
	        	{
	        		return null;
	        	}
	        	else
	        	{
	        		if(lexeme.equals("."))
	        		{
	        			return new Token("PERIOD",".");
	        		}
	        		else
	        		{
	        			return new Token("IDENT",lexeme);
	        		}
	        	}
	        }
		}
	}
	public static Token checkToken(int i)
	{
		char temp = input.charAt(i);
		Token t = null;
		switch(temp)
		{
			case '+':
				t = new Token("PLUS","+");
				break;
			case '-':
				t = new Token("MINUS","-");
				break;
			case '*':
				t = new Token("MULT","*");
				break;
			case '/':
				t = new Token("DIVIDE","/");
				break;
			case '%':
				t = new Token("MODULO","%");
				break;
			case '[':
				t = new Token("LBRACKET","[");
				break;
			case ']':
				t = new Token("RBRACKET","]");
				break;
			case '(':
				t = new Token("LPAREN","(");
				break;
			case ')':
				t = new Token("RPAREN",")");
				break;
			case ':':
				t = new Token("COLON",":");
				break;
			case ',':
				t = new Token("COMMA",",");
				break;
			case ';':
				t = new Token("SCOLON",";");
				break;
			case '=':
				t = new Token("EQUALS","=");
				break;
			case '"':
				t = new Token("DQUOTE","\"");
				break;
			case '\'':
				t = new Token("QUOTE","\'");
				break;
		}
		return t;
	}	
	public static boolean isNumeric(String in)
	{
		try {
			Double.parseDouble(in);
			return true;
        } catch (NumberFormatException e) {
        	return false;
        }
	}
}