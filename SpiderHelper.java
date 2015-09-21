package locus.task.rank;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpiderHelper {

	private Set<String> listoflinks =new LinkedHashSet<String>();
	private Set<String> Popularlinks =new LinkedHashSet<String>();
	
		public boolean isValidUrl(String url) throws MalformedURLException, IOException
		{
			if(!(url.contains("en.wikipedia.org")))return false;
			System.out.println("Fetching for URL please wait...");
			    URL u = new URL(url); 
			    HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
			    huc.setRequestMethod("HEAD"); 
			    huc.connect(); 
			    return (huc.getResponseCode()==200)?true:false;
	
		}
	
			public void printtitles()
				{
					Iterator<String> iter = listoflinks.iterator();
					while (iter.hasNext()) {
						System.out.println(iter.next());
						}
					System.out.println("count of total titles"+listoflinks.size());
				}
	
		public void printpoprank()
			{
				Iterator<String> iter = Popularlinks.iterator();
				while (iter.hasNext()) {
				    System.out.println("https://en.wikipedia.org/wiki/"+iter.next());
				}
			}
		public String converttitle(String title) throws Exception
		{return java.net.URLEncoder.encode(title, "UTF-8").replace("+", "%20");}
		
	public void getpopularity()
	{
		int max=0;
		String maxtitle="";
		Iterator<String> iter = listoflinks.iterator();
		while (iter.hasNext()) {
			String Title=iter.next().toString();
			System.out.println("Checking popularity for "+Title );
			int popularrank=getreferencecount(Title);
				if(popularrank>max)
				{
					max=popularrank;
					maxtitle=Title;
				}
				
							}
		if(max!=0)
		Popularlinks.add(maxtitle+" with Popular rank "+max);
	}  //end of getpopularity function
	
	public void gettitles(String title)
	{
		HttpClient httpclient = new DefaultHttpClient();
		String continu="";
	    HttpPost httppost;
	    
		try {
			title=converttitle(title);
		 	
			while(true)
			{
			 	if(continu.equals(""))
			    	 httppost = new HttpPost("http://en.wikipedia.org/w/api.php?action=query&prop=links&format=json&plnamespace=0&pllimit=max&titles="+title+"");
			    	else
			    	 httppost = new HttpPost("http://en.wikipedia.org/w/api.php?action=query&prop=links&format=json&plnamespace=0&pllimit=max&plcontinue="+continu+"&titles="+title+"");
			  	httppost.getParams().setParameter(CookieSpecPNames.DATE_PATTERNS, Arrays.asList("EEE, d MMM yyyy HH:mm:ss z"));
		        HttpResponse response = httpclient.execute(httppost);
		        String resmessage=EntityUtils.toString(response.getEntity());
		        
		        JSONObject test = new JSONObject(resmessage);
		        
		        if(test.has("continue"))
		        {
		        continu=test.getJSONObject("continue").get("plcontinue").toString();
		        continu=converttitle(continu);
		       // System.out.println("this is contin"+continu);
		    	}
		        else
		        continu="";
		        
		        
		        
		        test=test.getJSONObject("query").getJSONObject("pages");
		        Iterator<String> keys = test.keys();
		        String key="";
		        
		        if( keys.hasNext() )
		            key = (String)keys.next(); // First key in  json object
		        JSONArray samp=test.getJSONObject(key).getJSONArray("links");
		        
		     	
		        for(int i=0;i<samp.length();i++)
		        {
		      	  JSONObject iterator =samp.getJSONObject(i);
		      	  listoflinks.add((String) iterator.get("title"));
		        }
		        if(continu.equals(""))
		        	break;
			}// end of while 
		    }  catch (Exception e) {
		        System.out.println("Error :"+e);
		       }
		   
	} //end of get titles function
	
	
	
	public int getreferencecount(String LinkTitle)
	{
		HttpClient httpclient = new DefaultHttpClient();
		String continu="";
	    HttpPost httppost;
	    int length=0;
		try {
			LinkTitle=converttitle(LinkTitle);
			
			while(true){
			if(continu.equals(""))
			    httppost = new HttpPost("http://en.wikipedia.org/w/api.php?action=query&prop=linkshere&format=json&lhprop=title&lhnamespace=0&lhlimit=max&titles="+LinkTitle+"");
			else
			    httppost = new HttpPost("http://en.wikipedia.org/w/api.php?action=query&prop=linkshere&format=json&lhprop=title&lhnamespace=0&lhlimit=max&lhcontinue="+continu+"&titles="+LinkTitle+"");
			httppost.getParams().setParameter(CookieSpecPNames.DATE_PATTERNS, Arrays.asList("EEE, d MMM yyyy HH:mm:ss z"));

			   HttpResponse response = httpclient.execute(httppost);
	        String resmessage=EntityUtils.toString(response.getEntity());
	        JSONObject test = new JSONObject(resmessage);
	        
	        if(test.has("continue"))
	        {
	        continu=test.getJSONObject("continue").get("lhcontinue").toString();
	        continu=converttitle(continu);
	       // System.out.println("this is contin "+continu);
	    	}
	        else
	        continu="";
	        
	        test=test.getJSONObject("query").getJSONObject("pages");
	        Iterator<String> keys = test.keys();
	        String key="";
	        if( keys.hasNext() )
	            key = (String)keys.next(); // First key in your json object
	        JSONArray samp=test.getJSONObject(key).getJSONArray("linkshere");
	        length =length+samp.length();
	        if(continu.equals(""))
	        	break;
			} //end of while
	    }  catch (Exception e) {
	        // TODO Auto-generated catch block
	        System.out.println("Error :"+e);
	       }
		return length;
	    
	}
	
}
