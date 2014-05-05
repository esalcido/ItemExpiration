import java.sql.*;
import java.util.*;
import java.io.*;


class ItemExpireDatabaseTest {
   public static void main (String args[]) {
      try {
		
		//+_+_+_+_+_+_+_+_+_+_+_++_+_+_+_+_+_+_+_+_+_+_+_+_+_
		//Database connection PART
         // force loading of driver
  			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
         String url = "jdbc:odbc:Item";
         String user = "anonymous";
         String password = "guest";
			
		   Connection con = DriverManager.getConnection(url, user, password);
         Statement stmt = con.createStatement();
      //+_+_+_+_++_+_+_+_+_+__+_+_+_+_+_++_+_+_+_+_+_+_+_+_+_
		  
		  
		  

			
		 //The Arrays for the tables Item, ExpiredItems 
		 ArrayList<Location> list = new ArrayList<Location>();
		 ArrayList<Item> list2 = new ArrayList<Item>();
      			
			



        //=======================================================
 Item item1 = new Item("5678", "1/2/2007");
  Item item2 = new Item("1230", "1/2/2007");
   Item item3 = new Item("1230", "1/2/2007");
	 Item item4 = new Item("1234", "1/2/2007");
list2.add(item1);
list2.add(item2);
list2.add(item3);
list2.add(item4);

if( list2.contains(item2) )
System.out.println("it works");


//getLocation method
					String upc =item1.getUpc();
					String d=" ",a=" ",s=" ",sec=" ",sh=" ",u=" ";	
		stmt.execute("select * from Location where upc='"+upc+"'");
		ResultSet rs = stmt.getResultSet();

		while(rs.next() ){
		

			
         	d=rs.getString(1);
				a=rs.getString(2);
				s=rs.getString(3);
				sec=rs.getString(4);
				sh = rs.getString(5);
				u = rs.getString(6);

      }
		Location location = new Location(d,a,s,sec,sh,u);			  
		
		
		item2.setLocation(location.toString() );
		
		System.out.println( item1.toString() );
            
				
//!!!!!!!Note: Create the 	setLocation method			
				
				
				
				
										
			
//the closing of connections			
//+++++++++++++++++++++++++++++++++++++
         stmt.close();
         con.close();
			
			
			//	System.out.println(list+"\n");
				
			
      }
      catch (SQLException ex) {
         System.out.println ("SQLException:");
         while (ex != null) {
            System.out.println ("SQLState: " + ex.getSQLState());
            System.out.println ("Message:  " + ex.getMessage());
            System.out.println ("Vendor:   " + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
          }
      }
      catch (java.lang.Exception ex) {
         System.out.println("Exception: " + ex);
         ex.printStackTrace ();
      }
   }//end main
	
	//INSERT ITEM DB METHOD
		public static String insertItemDB(Item i){
		String upc = i.getUpc();
		java.util.Date d = i.getDate();
		
		
		String date = (d.getMonth()+1)  + "/" + (d.getDay()+1) +"/"+(d.getYear()+1900);
		System.out.println(date);
		
		
		
		String command = "insert into Item(itemUPC, expDate) values(" + upc +",'" + date+ "')";
		
		return command;
		}
//******************************************************
//----Delete From Database Method
	public static String deleteItemDB(int upc){
		
		
		String command = "delete from Item where itemUPC="+upc;
		
		return command;
	}//end method
//*******************************************************	
	
	//Retrieve data from Item Method
	public static String retreiveItemDB(String s){
	String data = s;
		String command = "select distinct itemUpc,expDate from "+data;
	
	return command;
	}//end method
	
//*****************************************************************

public static String updateItems(int upc, String date    ){
		
	
	String command = "update Item set expDate='"+date+"' where itemUpc = "+upc;
   
	return command;

}//end method

//**************************************************************
public static void inputFromDB(ArrayList t, Statement stmt, String s){
			String table=s;	     
			String retrieve = retreiveItemDB(table);
			try{
				stmt.execute(retrieve);
			
				ResultSet rs = stmt.getResultSet();
			
			while(rs.next() ){

				Item item;
		   	String upc=" ";
				String month=" ", day=" ", year=" ",data;
		   	upc =rs.getString(1) ;
				data = rs.getString(2);
				StringTokenizer token = new StringTokenizer(data, "-");
			
				while(token.hasMoreTokens() ){
			
					year = token.nextToken();
					month = token.nextToken();
					day   = token.nextToken();
				
					
					String date = day+"/" + month+"/"+ year;
				
					item = new Item(upc, date);
				
					t.add(item);

			
				}//end inner while
			
			}//end outer while
			
	}catch(SQLException e ){
	System.out.println(e+"error from inputfromDB method");
	}//end try-catch block
			
}//end input from DB method

	
	
	
	
	
}//end class