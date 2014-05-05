//This is the driver class for the item expiration simulation
//Author: Edward Salcido
//Project: Item Expire
//Date: 2-2-2011
//
   import java.util.*;
   import java.io.*;
   import java.sql.*;

    public class ItemExpiration{
   
       public static void main(String [] args)throws IOException{
      

         try{
         
         //----------------------------------------------------------------------------------------INSTANCE VARIABLES
         //creation of the bufferedreader for user input
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in) );
         //String to hold user input
            String input0, response;
         //object item which will be created during user input
         //creation of the two tables.Good items which will hold all items that have not perished
         //ExpiredItems which will hold all items that have perished
            Table goodItems    = new Table();
            Table expiredItems = new Table();
				Table items        = new Table();
				
         //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-
      System.out.println("Retrieving Data from the Item database");   
		
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String url = "jdbc:odbc:ItemExpirationDB.mdb";
            String user = "anonymous";
            String password = "guest";
         
             Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement();
		
   							
				
			//Getting the DATA and placing it into the Table goodItems 
			//getting SQL string to use for execute method
			inputFromDB(goodItems,stmt,"GoodItems");
			inputFromDB(expiredItems,stmt,"ExpiredItems");
			
			
			for(int i=0;i<goodItems.getSize();i++){
				String s = getLocation(goodItems.getItem(i),stmt);
				
				goodItems.getItem(i).setLocation(s);
				
			}
			
			
			//getting the locations for the items
			//
			for(int j=0;j<expiredItems.getSize();j++){
				String s =getLocation(expiredItems.getItem(j),stmt);
			
			expiredItems.getItem(j).setLocation(s);
			}
//=======================================================

			if(con.isClosed() )
				System.out.println("connection to the database failed.");  
			else 
         System.out.println("Data retreived from the Item database");   
			
			
			//------------------------------------------------------------------------------END OF RETREIVING DATA
		//=======================================================
		//Begin the do-while command line loop.
		do{	
         
            
            
               boolean resume=false, resume2=false;
               //Print out the initial options available tot he user.
               System.out.println("enter option: 'a'(add <UPC> <expiration date>),  'd'(delete <UPC>), 'v'(view), addLocation, 'cd'(checkDates), 'q' for (quit)");
               //read in input from user
               input0 = in.readLine();
            
            //==============================================================================Add Case
               if(input0.equalsIgnoreCase("a") ){
                  System.out.println("add function.  press q to quit");
                  do{
                  
                     resume = addItem(goodItems, in, stmt,"Item");
                  	
                  }while(resume );
               resume2=true;
               
               }
  
  				//-----------------------------------------------------------------------------Add location case
					if(input0.equalsIgnoreCase("addLocation")){
						System.out.println("addLocation function. press q to quit");
						do{
							resume = addLocation(goodItems, in, stmt);
							
						
						}while(resume);
						
					}//end if
  
               
               //------------------------------------------------------------------------Delete Case
               else if (input0.equalsIgnoreCase("d")){
						boolean val=true;
						System.out.println("delete function");
						System.out.println("delete all? y or n.");
         			input0 = in.readLine();         
						if(input0.equalsIgnoreCase("n") ){
                  	 
							do{
								val = deleteItem(goodItems, in,stmt);
							}while(val);
                  	
						}
						else if(input0.equalsIgnoreCase("y") ){
							System.out.println("good or expired?");
							input0 = in.readLine();
							if(input0.equalsIgnoreCase("good") ){
								goodItems.deleteAll();
								
								System.out.println("deleted all from goodItems table\n");
							}
							if(input0.equalsIgnoreCase("expired"))
								expiredItems.deleteAll();
								stmt.execute("delete * from ExpiredItems");
								System.out.println("deleted all from expiredItems table\n");
						
						}
						
               }
               
					
					
               
               //------------------------------------------------------------------------View Case
               else if(input0.equalsIgnoreCase("v")){
                  try{
                     System.out.println("good or expired");
                     input0 = in.readLine();
                  
                     if(input0.equalsIgnoreCase("good") ){
							
							
								if(!goodItems.isEmpty() ){
									
																		
                        	printTable(goodItems);
									System.out.println("view function");
								}
								else
									System.out.println("The good Items table is empty.\n");
							
							}
                     else if(input0.equalsIgnoreCase("expired")){	
                     
                        if(!expiredItems.isEmpty() ){
                           printTable(expiredItems);
                           System.out.println("view function");
                        }
                        else
                           System.out.println("Expired table is empty\n");
                     }
                  }
                      catch(Exception e){
                        System.out.println("Sorry, did not understand");
                     }
               }
               
               
               //----------------------------------------------------------------------CheckDates Case
               else if(input0.equalsIgnoreCase("cd") ){
                  System.out.println("Checked Dates\n");
              
						
							checkDates(goodItems, expiredItems,stmt);	
										
               }//end checkdates case
               
               
               
               //--------------------------------------------------------------------------------------Quit Case
               else if(input0.equalsIgnoreCase("q")){
                  System.out.println("Thank you for using my utility.\nThis program was written by Edward Salcido");
                  break;
               }
               
               //-------------------------------------------------------------------------------------all other cases
               else{
						if( resume2==false)
							System.out.println("Sorry, did not understand. ");
       				else     
						;
            
            	}
					
					
  			}  while(! input0.equalsIgnoreCase("yes") );           
         
			//--------------------------------------------------------------Disconnection from database
			stmt.close();
         con.close();
			//---------------------------------------------------------------------------------------||
			
         }
             catch (SQLException ex) {
               System.out.println ("SQLException:");
               while (ex != null) {
                  System.out.println ("SQLState: " + ex.getSQLState());
                  System.out.println ("Message:  " + ex.getMessage());
                  System.out.println ("Vendor:   " + ex.getErrorCode());
                  ex = ex.getNextException();
                  System.out.println (" ");
               }
            }
             catch (java.lang.Exception ex) {
               System.out.println("Exception: " + ex);
               ex.printStackTrace ();
            }
      
		
	
     
      
      }//end main
   
   
   //((((((((((((((((((((((((((((((((((((((())))))))))))))))))))))))))))))))))))))()))))))))))))))))))))((((((((((((((((((((((((((((((((())))))))))))))))))))))))
   //METHODS
	
   //------------------------------------------------------------------------------------Add Item to Table
	//this adds an item one by one
       public static boolean addItem(Table t, BufferedReader in, Statement stmt,String s){
      
         boolean value=false;
         String input1, input2;
         Item item;
         java.util.Date date;
      
      //Getting the user input for adding items onto a table
         try{
            System.out.println("Please enter item upc");
         
            input1 = in.readLine();
            if(input1.equalsIgnoreCase("q")){
               throw new Exception();
            }
         
            System.out.println("Please enter expiration date in the form:mm/dd/yyyy");
         	input2 = in.readLine();
         
            if(input2.equalsIgnoreCase("q") ){
               throw new Exception();
            
            }
         
         //creating instances of the object item
            item = new Item(input1, input2);
            value = true;
            t.addItem(item);
				//add to database
				String add = insertItemDB(item);
				
				stmt.execute(add);
				
				//************************
				if(!s.equalsIgnoreCase("true") ){
				//Ask user if a location is needed 
					System.out.println("do you want to add a location?(y/n)");
					input2 = in.readLine();
					if(input2.equalsIgnoreCase("y") ){
				
						addLocation(t,in,stmt);
						System.out.println("added Location of the Item \n");
					}
				
				}//end if
								
				
            System.out.println("added Item onto the good Items list\n");
         
         }
             catch(Exception e){
               System.out.println("back to main menu\n");
               value=false;
            }//end try-catch block
      
         return value;
      
      }//end addItem method
   
	
	//========================================================
	//Print Table to screen---------------------------------------------------------------------------------Print Table to screen
       public static void printTable(Table t){
		 	
         t.print();
			
         System.out.println("\n "+t.getSize() );
      }
   //============================================================
	
	//Delete from Table--------------------------------------------------------------------------------Delete from Table
       public static boolean deleteItem(Table t,BufferedReader in,Statement stmt){
      
         String input1;
         try{
				System.out.println("Delete Item.");
            System.out.println("Please enter item upc");
            input1 = in.readLine();
         	if(input1.equalsIgnoreCase("q")){
					throw new Exception();
				}
    
				t.deleteItem(input1 );
            String delete = deleteItemDB(input1);
				stmt.execute(delete);
				
            System.out.println("deleted Item from the list\n");
         	return true;
         }
             catch(Exception e){
               System.out.println("Back to main menu\n ");
					return false;
            }//end try-catch block
      }//end delete method



//---------------------------------------------------------------------------------DATABASE METHODS:
	//INSERT ITEM DB METHOD-----------------------------------------------------------------------------------------------INSERT ITEM DB METHOD
	
		public static String insertItemDB(Item i){
		String upc = i.getUpc();
		java.util.Date d = i.getDate();
		
		
		String date = (d.getMonth()+1)  + "/" + (d.getDate()) +"/"+(d.getYear()+1900);
		
		
		
		String command = "insert into Item(itemUPC, expDate) values('"+ upc +"','" + date+ "')";
		
		return command;
		}
		
//******************************************************
//String for Delete From Database Method------------------------------------------------------------------------------------------------String for Delete From Database Method
	public static String deleteItemDB(String upc){
		
		
		String command = "delete from Item where itemUPC='"+upc+"'";
		
		return command;
	}//end method
//*******************************************************	
	
public static boolean deleteAll(Table t){

t.deleteAll();
return true;
}	
	
//String for Update data----------------------------------------------------------------------------------------------String for Update data
public static String updateItems(String upc, String date    ){
		
	
	String command = "update Item set expDate='"+date+"' where itemUpc = "+upc;
   
	return command;

}//end method



//**************************************************************	
	
	//INSERT ITEM DB METHOD-------------------------------------------------------------------------------------------------INSERT ITEM DB METHOD
		public static String insertExpiredItemDB(Item i){
		String upc = i.getUpc();
		java.util.Date d = i.getDate();
		
		String date = (d.getMonth()+1)  + "/" + (d.getDay()+1) +"/"+(d.getYear()+1900);
		String command = "insert into ExpiredItems(expUPC, expDate) values('" + upc +"','" + date+ "')";
		
		return command;
		}

//String for Retrieve expired--------------------------------------------------------------------------------------------------String for Retrieve expired
public static String retrieveExpiredItemDB(){
	
		String command = "select distinct expUpc,expDate from ExpiredItems";
	
	return command;
	}//end method



//retrieveGoodItems---------------------------------------------------------------------------------retrieveGoodItems
public static String retrievGoodItems(){
	
		String command = "select distinct itemUPC from Item where perishable = true";
	
	return command;
	}//end method

	
//inputFromDB Method----------------------------------------------------------------------------------inputFromDB Method
public static void inputFromDB(Table t, Statement stmt, String s){

try{
			String table=s;
			String command = "select distinct * from "+ s;
			Item item;
			String upc2=" " ;
		   	String upc=" ";
				String month=" ", day=" ", year=" ",data;
				
			
				stmt.execute(command);
			
				ResultSet rs = stmt.getResultSet();
			
			while(rs.next() ){

				
		   	upc =rs.getString(1) ;
				
				data = rs.getString(2);
				
				StringTokenizer token = new StringTokenizer(data, "-");
			
				while(token.hasMoreTokens() ){
			
					year = token.nextToken();
					month = token.nextToken();
					day   = token.nextToken();
				
					
					String date = day+"/" + month+"/"+ year;
				
					item = new Item(upc, date);
				
				
				
					
					t.addItem(item);

				
			
				}//end inner while
			
			}//end outer while
				
		}catch(SQLException e ){
		System.out.println(e+"error from inputfromDB method");
		}//end try-catch block
			
}//end input from DB method


//GetLocation of an Item through DB--------------------------------------------------------------------------------------------GetLocation of an Item through DB
public static String getLocation(Item i,Statement s){
			 Statement stmt = s;
			String d=" ",a=" ",side=" ",sec=" ",sh=" ",u=" ",loc=" ";	
			try{
				stmt.execute("select * from Location where upc='"+ i.getUpc() +"'");
				ResultSet rs2 = stmt.getResultSet();

				while(rs2.next() ){
		
		
    	     d=rs2.getString(1);
				a=rs2.getString(2);
				side=rs2.getString(3);
				sec=rs2.getString(4);
				sh = rs2.getString(5);
				u = rs2.getString(6);
    		  }
		Location location = new Location(d,a,side,sec,sh,u);			  
		loc = location.toString();
		
		}catch(SQLException e ){
	System.out.println(e+"error from getLocation method");
	}//end try-catch block

		return loc;
		
		}//end method	
	
	
	
	
	//AddLocationDB-----------------------------------------------------------------------------------------------AddLocationDB
	//this adds items at once grouped by their locations.
	public static  boolean addLocation(Table t, BufferedReader in , Statement stmt){
	String d=" ",a=" ", sec=" ", side=" ",shelf=" ", u=" ", command;
	boolean value=true, inputResult=true;
	
		
	//Enter Items into Database
	try{
		System.out.println("We will begin with the location of the items you want to enter.");
		System.out.println("\nPlease enter department number: ");
		d = in.readLine();
		if(d.equalsIgnoreCase("q") ){
               throw new Exception();
            
      }

		
		System.out.println("Please enter Aisle number: ");
		a = in.readLine() ;
		if(a.equalsIgnoreCase("q") ){
               throw new Exception();
            
      }

		
				
		System.out.println("Please enter (aisle) Side number: ");
		side = in.readLine();
		
		if(side.equalsIgnoreCase("q") ){
               throw new Exception();
            
      }
		
		System.out.println("Please enter (side ) section number: ");
		sec = in.readLine() ;
		if(sec.equalsIgnoreCase("q") ){
               throw new Exception();
            
      }				
		
		System.out.println("Please enter (section) shelf number: ");
		shelf = in.readLine() ;
		if(shelf.equalsIgnoreCase("q") ){
               throw new Exception();
            
      }
		
		System.out.println("Now, enter Items.  Let me know when you are done by entering 'q'.");
		//AddItem method part
		do{

         String input1, input2;
         Item item;
         java.util.Date date;
      
      //Getting the user input for adding items onto a table
				
            System.out.println("Please enter item upc");
         
            input1 = in.readLine();
            if(input1.equalsIgnoreCase("q")){
               throw new Exception();
            }
         
            System.out.println("Please enter expiration date in the form:mm/dd/yyyy");
         	input2 = in.readLine();
         
            if(input2.equalsIgnoreCase("q") ){
               throw new Exception();
            
            }
         
         //creating instances of the object item
            item = new Item(input1, input2);
            
            t.addItem(item);
				//add to database
				String add = insertItemDB(item);
				
				stmt.execute(add);
			
		
		
			
		command = "insert into Location(dept,aisle,side,section,shelf,upc)values ('" + d + "','"+a+"','" + side + "','" + sec + "','" + shelf + "','" + item.getUpc() + "')"    ;
		stmt.execute(command);
		
		for(int i=0;i<t.getSize();i++){
				String s = getLocation(t.getItem(i),stmt);
				
				t.getItem(i).setLocation(s);
				
			}

		}while(inputResult);
		
		}catch(Exception e){
		value=false;
		System.out.println("Back to main menu\n");
		}
	return value;
	
	}//end addLocation method
	
	//DeleteAll from DB--------------------------------------------------------------------------------------------------------DeleteAll from DB
		public static String deleteAllFromExpiredDB(){
		
		
		String command = "delete * from ExpiredItems";
		
		return command;
	}//end method


	//checkDates method------------------------------------------------------------------------------------------------checkDates method
	public static boolean checkDates( Table t, Table t2,Statement stmt){
	
						//refreshTable(t);
			
         java.util.Date today = new java.util.Date();
			String insert=" ";
      try{
         for(int i=0; i<t.getSize();i++){
         
      		
               if( t.getItem(i).getDate().getMonth() <=today.getMonth() && t.getItem(i).getDate().getYear()==today.getYear()  ){
						
						//added here
							insert = insertExpiredItemDB(t.getItem(i) );
								stmt.execute(insert);
								stmt.execute(deleteItemDB(t.getItem(i).getUpc() ) );
						
						t2.addItem(t.getItem(i) );
						t.deleteItem(t.getItem(i).getUpc() );
               	
               }//end if
								
				
				
            
         }//end for
      
		
			}catch(Exception e){
			System.out.println(e+" error from checkdates method");
			}
         return true;
      }//end checkDates

	
	//REFRESH TABLE===========================================================================================================REFRESH TABLE	
	public static void refreshTable(Table t){
		t.deleteAll();
	}
	
	
	
}//end class