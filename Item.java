import java.util.*;


public class Item{

	private String itemUpc;
	private Date expDate = new Date();
	private String location;
	
	//constructor
	public Item(String i,String d){
		
		itemUpc = i;
		expDate = new Date(d);
		location="- - -";
	}
	
	public Item(String i,String d, String l){
		
		itemUpc = i;
		expDate = new Date(d);
		location=l;
	}
	
	
	public String getUpc(){
		return itemUpc;
	}
	
	public Date getDate(){
	
		return expDate;
	}
	
	public void setUpc(String u){
		itemUpc = u;
	}
	public void setexpDate(Date d){
		expDate=d;
	}
	
	public void setLocation (String s){
	
		location = s;
	
	}
	
	public String getLocation(){
		return location;
	}
	
	public String toString(){
		String s;
		int month = (expDate.getMonth() +1);
		int year  = expDate.getYear();
		
		if(month <10){
		s="("+itemUpc+")  (0"+month+"/"+ (year+1900)+ ") " +" Location: "+location;
		}
		else
		s="("+itemUpc+")  ("+month+"/"+ (year+1900)+ ") " +" Location: "+location;
		
		
		return s;
	}
	
	public static void main(String [] args){
	
	
	Item item = new Item("123456","2/2/2006");
	
	System.out.println(item.toString() );

	}//end main
	
}//end class