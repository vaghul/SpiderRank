package locus.task.rank;

import java.util.Scanner;

public class Spidermain {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
	Scanner input =new Scanner(System.in);
	System.out.println("Enter the Wiki URL");
	String url=input.next();
	SpiderHelper helpobj=new SpiderHelper();
		try {
				if(helpobj.isValidUrl(url))
				{
					String title = url.substring(url.lastIndexOf("/")+1);
					helpobj.gettitles(title);
					//helpobj.printtitles();  // To print the titles of links
					System.out.println("Checking Popularity For The WebPage ... Please Wait ");
					helpobj.getpopularity();
					System.out.println("\n\n\nThe Popular Links Are");
					helpobj.printpoprank();
					
				}
				else
						System.out.println("Not A Valid Link");
				input.close();
			} 
			catch (Exception e) 
			{
					// TODO Auto-generated catch block
					System.out.println("Error" +e);
			}
	
	} // end of Main

}
