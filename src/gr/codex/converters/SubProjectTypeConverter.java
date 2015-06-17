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

/* $Id: SubProjectTypeConverter.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.converters;

import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.ota.helpers.SubProjectType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author markos
 *
 */
public class SubProjectTypeConverter implements Converter {
	private static Context ctx;
	private static KatartisisBeanLocal kbean;
	
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String arg) {
		try {
			ctx = new InitialContext();
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			
			return (SubProjectType) kbean.getRecordById(Integer.parseInt(arg), SubProjectType.class);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object ob) {

		return String.valueOf( ((SubProjectType) ob).getId() );
	}

}
