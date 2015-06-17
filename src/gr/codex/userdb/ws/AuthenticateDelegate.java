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

/* $Id: AuthenticateDelegate.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.userdb.ws;


@javax.jws.WebService(targetNamespace = "http://ws.userdb.codex.gr/", serviceName = "AuthenticateService", portName = "AuthenticatePort", wsdlLocation = "WEB-INF/wsdl/AuthenticateService.wsdl")
public class AuthenticateDelegate {

	gr.codex.userdb.ws.Authenticate authenticate = new gr.codex.userdb.ws.Authenticate();

	public AuthenticateResult authenticate(String username, String password) {
		return authenticate.authenticate(username, password);
	}

}