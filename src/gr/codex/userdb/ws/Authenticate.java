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

/* $Id: Authenticate.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.userdb.ws;

import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.User;
import gr.codex.usermgmt.helpers.RidiculousPasswordException;
import gr.codex.usermgmt.helpers.UserStatus;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Authenticate {
	AuthenticateResult rs;
	public static final String ARG_ERRORCODE		= "errorcode";
	public static final String ARG_ERRORMESSAGE	= "errormessage";
	public static final String ARG_FIRSTNAME		= "firstname";
	public static final String ARG_LASTNAME		= "lastname";
	
	public static final String RC_SUCCESS			= "0";
	public static final String RC_NOSUCHUSER		= "1";
	public static final String RC_WRONGPASSWORD	= "2";
	public static final String RC_SERVERERROR		= "3";
	public static final String RC_INACTIVEUSER	= "4";
	public static final String MSG_SUCCESS		= "Authentication Succeeded";
	public static final String MSG_NOSUCHUSER		= "User Does not Exist";
	public static final String MSG_WRONGPASSWORD	= "Password Incorrect";
	public static final String MSG_SERVERERROR	= "Server Error!";
	public static final String MSG_INACTIVEUSER	= "User not active/activation pending!";
	
	
	public AuthenticateResult authenticate( String username, String password ) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		try {
			Context ctx = new InitialContext();
			
			UserManagementBeanLocal ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
			
			User u = ubean.findUserByUsername(username);
			
			if (u == null) {
				map.put(ARG_ERRORCODE, RC_NOSUCHUSER);
				map.put(ARG_ERRORMESSAGE, MSG_NOSUCHUSER);
				
				rs = new AuthenticateResult();
				rs.result = map;
				
				return rs;				
			}
			
			if (u.getStatus() != UserStatus.USER_APPROVED) {
				map.put(ARG_ERRORCODE, RC_INACTIVEUSER);
				map.put(ARG_ERRORMESSAGE, MSG_INACTIVEUSER);
				
				rs = new AuthenticateResult();
				rs.result = map;
				
				return rs;
			}
			
			try {
				u.checkPassword(password);
				map.put(ARG_ERRORCODE, RC_SUCCESS);
				map.put(ARG_ERRORMESSAGE, MSG_SUCCESS);
				map.put(ARG_FIRSTNAME, u.getFirstName());
				map.put(ARG_LASTNAME, u.getLastName());
				
				rs = new AuthenticateResult();
				rs.result = map;
					
				return rs;							
			} catch (RidiculousPasswordException e) {
				map.put(ARG_ERRORCODE, RC_WRONGPASSWORD);
				map.put(ARG_ERRORMESSAGE, MSG_WRONGPASSWORD);
			
				rs = new AuthenticateResult();
				rs.result = map;
			
				return rs;
			}
			
		} catch( NamingException e) {
			e.printStackTrace();
			
			map.put(ARG_ERRORCODE, RC_SERVERERROR);
			map.put(ARG_ERRORMESSAGE, MSG_SERVERERROR);
			
			rs = new AuthenticateResult();
			rs.result = map;
			
			return rs;
		}
	}
}
