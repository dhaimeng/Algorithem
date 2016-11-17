package entity;


/*
 * Copyright (c) 2009-2012 The 99 Software Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.nnsoft.sameas4j.*;


/**
 *  
 */

public class ExtractSameAs {

	 public static void main(String args[]){
		 String URI="http://dbpedia.org/resource/New_York";
		 ArrayList entity=DataBaseSameAs(URI);
		 System.out.println(entity.size());
		 for(int i=0;i<entity.size();i++){
			 System.out.println(entity.get(i));
		 }
	 }
     public static  ArrayList  DataBaseSameAs( String Uri){

    	 ArrayList entity=new ArrayList();
    	 try{   		  
    		  SameAsService sameAsService = DefaultSameAsServiceFactory.createNew();
    		  EquivalenceList equivalences = null;
    		  Equivalence equivalence = null;
    		  String Urls = Uri; 
    		  equivalence = sameAsService.getDuplicates( new URI(Urls));
              for ( URI uri : equivalence )
    		  {
                	if(entity.indexOf(uri)==-1){
                		entity.add(uri);
                	}
                			
    		  }   		  
    	  }catch(Exception e){
    		  e.printStackTrace();
    	  }

    	  return entity;
	}
    public static void filterML(){
    	
    }
}