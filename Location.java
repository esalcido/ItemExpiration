//This is the Location Class.  It will serve as a container for the locations of an Item.
//the upc of an Item will link with the upc of the location.

public class Location{

	String dept;
	String aisle;
	String side;
	String section;
	String shelf;
	String upc;
	
	public Location(){
		dept = " ";
		aisle = " ";
		side = " ";
		section = " ";
		shelf = " ";
		upc = " ";
	
	}//end constructor1

	public Location(String d, String a, String s, String sec, String sh,String u ){
		dept = d;
		aisle = a;
		side = s;
		section = sec;
		shelf = sh;
		upc = u;
	
	
	}//end constructor 2


	public String toString(){
		return dept+" "+aisle+"-"+side+"-"+section+"-"+shelf;
	
	
	}


	public static void main(String [] args){
		
		
		Location location = new Location("pantry","1", "b", "1", "2","12345");
		Location location2 = new Location("pantry","5", "b", "1", "2","12345");
		System.out.println(location.toString()+"\n"+location2.toString() );
	
	
	
	}


}//end class