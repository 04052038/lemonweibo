package com.star.yytv.common;

import java.io.PrintWriter;
import java.io.StringWriter;



public class StackTrace
{
	
    public static String getExTrace(Throwable e)
    {
    	String exDesc = "";
    	try
		{
    		StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
                
            exDesc = sw.toString();
            
            pw.close();
            sw.close();
		}
    	catch(Exception ex)
		{
    		exDesc = ex.getMessage();
		}
        
        
        return exDesc;
    }    

}
