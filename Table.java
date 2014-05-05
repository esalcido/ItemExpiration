   import java.util.*;

    public class Table{
   
      ArrayList<Item> table ;
   
       public Table(){
         table = new ArrayList<Item>();
      
      }
   
       public Item getItem(int i){
         return (table.get(i));
      }
       public void addItem( Item i){
         table.add(i);
      
      }
   
	
		public boolean contains(Item i){
		
			boolean result = 	table.contains(i);
			
			return result;
		
		}
	
       public void deleteItem(String j){
      
         for(int i=0;i<table.size();i++){
            if( (table.get(i).getUpc()).equalsIgnoreCase( j ) )
               table.remove(i);
         }//end for
      }
   
       public int getSize(){
         return table.size();
      }
   
       public void print(){
         for(int i=0;i<table.size();i++)
            System.out.println(table.get(i).toString() );
      }
   
	
	
	
	//======================================================================================================
   //this method will check the table "this" against the table t and compare
   //each item with today's date. if they are equal, it will be added to table t.
	/*
       public boolean checkDates( Table t){
         Date today = new Date();
      
         for(int i=0; i<this.getSize();i++){
         
      		if(! table.contains(t ) ){      
   				      
			
			
               if( this.getItem(i).getDate().getMonth() <=today.getMonth() && this.getItem(i).getDate().getYear()==today.getYear()  ){
						
						t.addItem(this.getItem(i) );
               
               }//end if
				}//end if
            
         }//end for
      
         return true;
      }//end checkDates
*/   
   
       public boolean isEmpty(){
         return (table.isEmpty() );
      }
   
		public boolean deleteAll(){
			table.clear();
			return true;
		}
   
	
					
   //MAIN+_+_+_+_+_+_+_+__+_+_+_
       public static void main(String [] args){
      
               
         
        Table t = new Table();
		  Table t2 = new Table();
      
		System.out.println(t.contains(t2.getItem(1)) );
      
      }//end main
   
   }//end class