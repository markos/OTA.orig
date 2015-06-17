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

/* $Id: VATValidator.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author markos
 *
 */
public class VATValidator implements Validator {
	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	/* Taken from http://www.theregister.co.uk/2006/03/24/java_regex_tutorial/
	 * If a VAT number starts with a country code we will  remove it
	 */
	
    private static final String COUNTRY_CODE_EXP = "\\w{2}";  
    
    /** 1 block of 9 digits */
    private static final String GERMANY = "\\d{9}";
    private static final String GREECE = "[0-9]{9}";
        
    private static final Pattern countryCodePattern = Pattern.compile(COUNTRY_CODE_EXP);

    private String stripCountryCodes(String vatNumber) {
        String result = vatNumber;
        Matcher matcher = countryCodePattern.matcher(vatNumber);
        if (matcher.find()) {
            result = result.substring(2);
        }
        return result;
    }

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) 
    	throws ValidatorException { 
		String vatNumber = value.toString();
		//vatNumber = stripCountryCodes(vatNumber);
		Pattern pattern = Pattern.compile(GREECE);
		Matcher matcher = pattern.matcher(vatNumber);
		if (matcher.matches() == false) {
			throw new ValidatorException(new FacesMessage(component.getId() + ": Το ΑΦΜ " + vatNumber + " δεν έχει τη σωστή μορφή!"));
		}
	}
}
