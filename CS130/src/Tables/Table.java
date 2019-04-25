package Tables;

import java.util.ArrayList;

import Tables.Row;

public class Table {
	ArrayList<Row> rows = new ArrayList<Row>();
	
	public void addRow(Row row)
	{
		rows.add(row);
	}
	
	public void printTable()
	{
		for(int i = 0; i < rows.size(); i++)
		{
			rows.get(i).printRow();
			System.out.println();
		}
	}
}
