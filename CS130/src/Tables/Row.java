package Tables;

import java.util.ArrayList;

public class Row {
	ArrayList<Column> column = new ArrayList<Column>();
	
	public void addColumn(Column c)
	{
		column.add(c);
	}
	
	public void printRow()
	{
		for(int i = 0; i < column.size(); i++)
		{
			column.get(i).printColumn();
			if(i + 1 < column.size())
			{
				System.out.print(", ");
			}
		}
	}
}
