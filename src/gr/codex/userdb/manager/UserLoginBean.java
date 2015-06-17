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

/* $Id: UserLoginBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.userdb.manager;

import gr.codex.userdb.ws.Authenticate;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.User;
import gr.codex.usermgmt.helpers.RidiculousPasswordException;
import gr.codex.usermgmt.helpers.UserPrivilegeType;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class UserLoginBean {
	private User loggedUser;
	private String username;
	private String password;
	
	private FacesContext facesContext;
	private static Context ctx;
	private static UserManagementBeanLocal ubean;
	
	public UserLoginBean() {
		try {
			
			ctx = new InitialContext();
			ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String authenticate() {
		try {
			facesContext = FacesContext.getCurrentInstance();
			System.out.println("checking user "+ username);
			
			// check if user is a trainer first
			loggedUser = ubean.findTrainerByUsername(username);
			
			if (loggedUser != null) {
				loggedUser.checkPassword(password);
				
				return "login-ok";
			}
			
			loggedUser = ubean.findUserByUsername(username);
			
			if (loggedUser != null) {
				loggedUser.checkPassword(password);
				System.out.println("found person "+ username);
				
				// the following cookie is set for use by JForum SSO mechanism.
				Cookie cookie = new Cookie("psifiakosdimos_forum_SSO", loggedUser.getUsername());
				cookie.setMaxAge(-1); // session cookie, or set to positive number.
				cookie.setPath("/");
		        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		        response.addCookie(cookie);
				
				return "login-ok";
			}
			
			facesContext.addMessage("", new FacesMessage(Authenticate.MSG_NOSUCHUSER) );
			return "error-login";
			
		} catch (RidiculousPasswordException e) {
			facesContext.addMessage("", new FacesMessage(Authenticate.MSG_WRONGPASSWORD) );
			return "error-login";
		}
	}

	public String disconnect() {
		facesContext = FacesContext.getCurrentInstance();
		
		// clear everything
		this.loggedUser = null;
		Cookie cookie = new Cookie("psifiakosdimos_forum_SSO", "dummy");
		cookie.setMaxAge(0); // session cookie, or set to positive number.
		cookie.setPath("/");
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
		
		return "disconnected";		
	}

	/**
	 * @return the loggedUser
	 */
	public User getLoggedUser() {
		return loggedUser;
	}

	/**
	 * @param loggedUser the loggedUser to set
	 */
	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		if (this.loggedUser != null)
			return true;
		else
			return false;
	}

	public boolean isAdminUser() {
		if (this.loggedUser != null && this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_USERS))
			return true;
		else
			return false;
	}
	
	public boolean isAdminCMS() {
		if (this.loggedUser != null && this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_CMS))
			return true;
		else
			return false;
	}
	
	public boolean isAdminConfig() {
		if (this.loggedUser != null && this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_CONFIG))
			return true;
		else
			return false;
	}
	
	public boolean isAdminPolls() {
		if (this.loggedUser != null && this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_POLLS))
			return true;
		else
			return false;
	}

	public boolean isAdminKatartisis() {
		if (this.loggedUser != null && (this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_KATARTISIS)))
			return true;
		else
			return false;
	}
	
	public boolean isAdminTDE() {
		if (this.loggedUser != null && (this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_OTA_PROJECTS)))
			return true;
		else
			return false;
	}
	
	public boolean isContractorUser() {
		if (this.loggedUser != null && this.loggedUser.getPrivilege(UserPrivilegeType.PRIV_ACCESS_KATARTISIS))
			return true;
		else
			return false;
	}
}
