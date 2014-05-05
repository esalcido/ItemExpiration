//This is the driver class for the item expiration simulation

   import java.util.*;
   import java.io.*;
   import java.sql.*;

    public class ItemExpiration2{
   
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
			ItemExpiration.inputFromDB(goodItems,stmt,"GoodItems");
			ItemExpiration.inputFromDB(expiredItems,stmt,"ExpiredItems");
			
			
			for(int i=0;i<goodItems.getSize();i++){
				String s = ItemExpiration.getLocation(goodItems.getItem(i),stmt);
				
				goodItems.getItem(i).setLocation(s);
				
			}
			
			
			//getting the locations for the items
			//
			for(int j=0;j<expiredItems.getSize();j++){
				String s = ItemExpiration.getLocation(expiredItems.getItem(j),stmt);
			
			expiredItems.getItem(j).setLocation(s);
			}
//=======================================================

			if(con.isClosed() )
				System.out.println("connection to the database failed.");  
			else 
         System.out.println("Data retreived from the Item database");   
			
			
			//------------------------------------------------------------------------------END OF RETREIVING DATA
//=======================================================
		do{	
         
            
            
               boolean resume=false, resume2=false;
               System.out.println("enter option: 'a'(add <UPC> <expiration date>),  d(delete <UPC>), v(view), addLocation, cd(checkDates), q for (quit)");
               input0 = in.readLine();
            
            //==============================================================================Add Case
               if(input0.equalsIgnoreCase("a") ){
                  System.out.println("add function.  press q to quit");
                  do{
                  
                     resume = ItemExpiration.addItem(goodItems, in, stmt,"Item");
                  	
                  }while(resume );
               resume2=true;
               
               }
  
  				//-----------------------------------------------------------------------------Add location case
					if(input0.equalsIgnoreCase("addLocation")){
						System.out.println("addLocation function. press q to quit");
						do{
						
							resume = ItemExpiration.addLocation(goodItems, in, stmt);
							
						
						}while(resume);
						
					}//end if
  
               
               //------------------------------------------------------------------------Delete Case
               else if (input0.equalsIgnoreCase("d")){
					
						System.out.println("delete function");
						System.out.println("delete all? y or n.");
         			input0 = in.readLine();         
						if(input0.equalsIgnoreCase("n") ){
                  	boolean val = ItemExpiration.deleteItem(goodItems, in,stmt);
							if(val==true)
                  	     ItemExpiration.deleteItem(expiredItems, in,stmt);
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
									
																		
                        	ItemExpiration.printTable(goodItems);
									System.out.println("view function");
								}
								else
									System.out.println("The good Items table is empty.\n");
							
							}
                     else if(input0.equalsIgnoreCase("expired")){	
                     
                        if(!expiredItems.isEmpty() ){
                           ItemExpiration.printTable(expiredItems);
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
                  
						
						
						
						//putting contents of goodItemsDB into ExpiredItemsDB
						
						try{
							ItemExpiration.checkDates(goodItems, expiredItems,stmt);	
							
							for(int i=0;i<expiredItems.getSize(); i++){	
								String insert = ItemExpiration.insertExpiredItemDB(expiredItems.getItem(i) );
								stmt.execute(insert);
								stmt.execute(ItemExpiration.deleteItemDB(expiredItems.getItem(i).getUpc() ) );
								
							} //end for
							
						}catch(Exception ex){
						System.out.println(ex+".    checkDates has already been run.");
						
						}
						
               }//end checkdates case
               
               
               
               //--------------------------------------------------------------------------------------Quit Case
               else if(input0.equalsIgnoreCase("q")){
                  System.out.println("Thank you for using my utility.\nThis program was written by Edward Salcido");
                  break;
               }
          /*    
				   else if(input0.equalsIgnoreCase("gi")){//-----------------------------------------------goodItemFill Case
                  
                  break;
               }
*/
				   
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
}//end class