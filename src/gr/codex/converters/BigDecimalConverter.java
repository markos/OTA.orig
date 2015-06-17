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

/* $Id: BigDecimalConverter.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.NumberConverter;

/**
 * Converts a Double or Long value provided by standard jsf number
 * converter to a BigDecimal value
 *
 * To get a locale-sensitive converter, java.text.NumberFormat is used
 * (through javax.faces.convert.NumberConverter).
 * The parsing done by java.math.BigDecimal is not affected by locale.
 * See javax.faces.convert.BigDecimalConverter
 *
 * found on: http://www.techienuggets.com/Comments?tx=11556
 */

public class BigDecimalConverter extends NumberConverter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object ret = super.getAsObject(context, component, value);
		System.out.println(this.getLocale());
		if (ret != null) {
			return new BigDecimal(ret.toString());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.NumberConverter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		BigDecimal ob = (BigDecimal) arg2;
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
		double doubleval = ob.doubleValue(); 
		return formatter.format(doubleval);
	}
	
	
}
