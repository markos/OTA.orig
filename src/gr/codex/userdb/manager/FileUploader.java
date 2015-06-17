/* Copyright (C) 2008,2009 Central Union Of Municipalities & Communities Of Greece (http://www.kedke.gr)
   Copyright (C) 2008,2009 Profile S.A. (http://www.profile.gr)
   Copyright (C) 2008,2009 Codex O.E. (http://www.codex.gr)
   
   This file is part of the Integrated Management System for Public Projects (IMSPP).

   IMSPP is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   IMSPP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with IMSPP.  If not, see <http://www.gnu.org/licenses/>.
   
   Authors:
     Konstantinos Margaritis <markos@codex.gr>
     Theodore Karkoulis <t.karkoulis@gmail.com>
*/

/* $Id: FileUploader.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.userdb.manager;

import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.Trainer;
import gr.codex.usermgmt.entitybeans.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUploader extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FileUploader() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// base Path
		String basePath = "/tmp/OTA/";  // trailing slash is needed please

		final PrintWriter objOut = response.getWriter();
		
		objOut.print("<center><small>μεταφορά...");
		
		// create the progressslistener
        ProgressListener pl = new ProgressListener() {
            public void update(long pBytesRead, long pContentLength, int pItems) {
                    //objOut.print(".");
                    objOut.flush();
            }
        };
		
        try {
        	HttpServletMultipartRequest fileRequest = new HttpServletMultipartRequest(
        			request,
        			HttpServletMultipartRequest.MAX_CONTENT_LENGTH,
        			HttpServletMultipartRequest.MAX_CONTENT_MEMORY_THRESHOLD,
        			HttpServletMultipartRequest.IGNORE_ON_MAX_LENGTH,
        			"utf-8",
        			pl);
        	
        	
        	// ok then, the form is listened. lets do what we gotta do.
        
    		// Get username from request
    		
        	
        	fileRequest.setCharacterEncoding("UTF-8");
        	
    		String username = fileRequest.getParameter("trainerUsername");
    		
    		System.out.println(" I am user:" + username );
    		
    		// Check IF person exists in the database, continue
    		
    		
    		if( username.equals("") ) {
    			// return beacause we dont want to do anything!
    			objOut.println("<strong>Σφάλμα! Δεν μπορείτε να ανεβάσετε αρχεία για ένα χρήστη που δεν έχει αποθηκευτεί πρώτα!</strong>");
    			return;
    		} else {
    			// get the person by ID
    			try {
    				
    				Context context;
    				
    				context = new InitialContext();
    				
    				UserManagementBeanLocal ubean = (UserManagementBeanLocal) context.lookup("UserManagementBean/local");
    				
    				User p = ubean.findUserByUsername( username );
    				
    				if( p == null ) {
    					objOut.println("<strong>Σφάλμα! Το όνομα χρήστη δεν βρέθηκε στη βάση δεδομένων!</strong>");
    					return;					
    				}
    				
    			} catch( NamingException e ) {
    				e.printStackTrace();
    				return;
    			}
    		}

    		MultipartFile flong = fileRequest.getFileParameter("longCVFile");
    		MultipartFile fshort = fileRequest.getFileParameter("shortCVFile");

    		// is there a file uploaded though?
    		if( flong == null ) objOut.println("δεν επιλέξατε κάποιο αρχείο!");
    		else if( flong.getSize() > 2048000 ) {
    			objOut.println("Το αρχείο είναι μεγαλύτερο από 2MB!");
    			return;
    		} else {
    			System.out.println("I FOUND A LONG CV" + flong.getSize() );
    			
    			// create filename
    			String canonicalFileName = flong.getName();
    			String extension = (canonicalFileName.lastIndexOf(".")==-1)?"":canonicalFileName.substring(canonicalFileName.lastIndexOf(".")+1,canonicalFileName.length());
    			String filename = username + new Date().getTime() + "." + extension;
    			
    			InputStream fin = flong.getInputStream();
    			byte[] buf = new byte[fin.available()];
    			fin.read(buf);
    			File file = new File(basePath + filename );
    			FileOutputStream fout = new FileOutputStream(file);
    			fout.write(buf);
    			
    			// write to database
    			
    			try {
    				
    				Context context;
    				
    				context = new InitialContext();
    				
    				UserManagementBeanLocal ubean = (UserManagementBeanLocal) context.lookup("UserManagementBean/local");
    				
    				Trainer t = ubean.findTrainerByUsername(username);
    				
    				t.setLongCVPath( filename );
    				
    				ubean.updateRecord(t);
    	        	// done uploading
    	        	objOut.println(".επιτυχής!</strong><br/><br/><script language=\"javascript\">window.parent.removeUpload();</script></center>");
    	        	return;
    			} catch( NamingException e ) {
    				e.printStackTrace();
    				return;
    			}    			
    		}
    		
    		// is there a file uploaded though?
    		if( fshort == null ) objOut.println("δεν επιλέξατε κάποιο αρχείο!");
    		else if( fshort.getSize() > 2048000 ) {
    			objOut.println("Το αρχείο είναι μεγαλύτερο από 2MB!");
    			return;
    		} else {
    			System.out.println("I FOUND A SHORT CV" );
    			
    			// create filename
    			String canonicalFileName = fshort.getName();
    			String extension = (canonicalFileName.lastIndexOf(".")==-1)?"":canonicalFileName.substring(canonicalFileName.lastIndexOf(".")+1,canonicalFileName.length());
    			String filename = username + new Date().getTime() + "." + extension;
    			
    			InputStream fin = fshort.getInputStream();
    			byte[] buf = new byte[fin.available()];
    			fin.read(buf);
    			File file = new File(basePath + filename );
    			FileOutputStream fout = new FileOutputStream(file);
    			fout.write(buf);
    			
    			// write to database
    			
    			try {
    				
    				Context context;
    				
    				context = new InitialContext();
    				
    				UserManagementBeanLocal ubean = (UserManagementBeanLocal) context.lookup("UserManagementBean/local");
    				
    				Trainer t = ubean.findTrainerByUsername(username);
    				
    				t.setShortCVPath( filename );
    				
    				ubean.updateRecord(t);
    	        	// done uploading
    	        	objOut.println(".επιτυχής!</strong><br/><br/><script language=\"javascript\">window.parent.removeUpload();</script></center>");
    	        	return;
    			} catch( NamingException e ) {
    				e.printStackTrace();
    				return;
    			}
    		}        	
        	
        } catch (Exception e) {
        	try {
        		// we want to gobble up the input stream in this case.
        		InputStream in = request.getInputStream();
        		while ( in.read() !=-1);
        	} catch(Exception e2) { }

        	e.printStackTrace(objOut);
        }

	}
}
