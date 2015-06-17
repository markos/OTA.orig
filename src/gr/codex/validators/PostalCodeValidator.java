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

/* $Id: PostalCodeValidator.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author markos
 *
 */
public class PostalCodeValidator implements Validator {

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) 
    throws ValidatorException {
		String strValue = (String)value;

		strValue = strValue.replace(" ", "");
		
		try {
			Integer n = Integer.valueOf(strValue);
			if (n < 10000 || n > 99999) {
				throw new ValidatorException(new FacesMessage(component.getId() + ": Πρέπει να εισάγετε ένα σωστό Ταχυδρομικό Κωδικό!"));
			}
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(component.getId() + ": Πρέπει να εισάγετε ένα σωστό Ταχυδρομικό Κωδικό!"));
		}
	}
}
