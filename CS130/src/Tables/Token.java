package Tables;
public class Token {
    String id, lexeme;

    public Token(String id, String lexeme) {
        this.id = id;
        this.lexeme = lexeme.trim();
    }

    public String getId() {
        return id;
    }

    public String getLexeme() {
        return lexeme;
    }
    
    public String getIDLexeme()
    {
    	String whitespace = "";
    	for(int i = 0; i < 12 - id.length(); i++)
    	{
    		whitespace += " ";
    	}
    	return id + whitespace + lexeme;
    }
}