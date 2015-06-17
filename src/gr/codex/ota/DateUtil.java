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

/* $Id: DateUtil.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.ota;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DateUtil {
	public static List<Integer> getYearList(Date d1, Date d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		int y1 = c.get(Calendar.YEAR);
		c.setTime(d2);
		int y2 = c.get(Calendar.YEAR);
		
		List<Integer> yearList = new LinkedList<Integer>();
		if (y1 > y2) {
			int tmp = y2;
			y2 = y1;
			y1 = tmp;
		}
		
		while (y1 <= y2) {
			yearList.add(y1);
			y1++;
		}
		return yearList;
	}

	public static String cts(Calendar c) {
		return c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
	}
	
	public static String cts(Date c) {
		return c.getDate() + "/" + Integer.valueOf(c.getMonth()+1) + "/" + Integer.valueOf(c.getYear()+1900) ;
	}
	
	public static List<YearQuarters> getQuartersList(Date d1, Date d2) {
		List<Integer> yearList = getYearList(d1, d2);
		// swap d1, d2 if d1 > d2;
		if (d1.compareTo(d2) > 0) {
			Date tmp = d2;
			d2 = d1;
			d1 = tmp;
		}
		
		List<YearQuarters> quarterList = new ArrayList<YearQuarters>();
		Iterator iter;
		for (iter = yearList.iterator(); iter.hasNext();) {
			Integer y = (Integer) iter.next();
			List<Integer> yqlist = new LinkedList<Integer>();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);

			Calendar qs = Calendar.getInstance(); // quarter start
			qs.set(y, 1, 1); 
			Calendar qe = Calendar.getInstance(); // next quarter start
			qe.set(y, 4, 1);
			
			System.out.println("c1 = " +cts(c1) + ", c2 = " + cts(c2));
			
			for (int i=1; i <= 4; i++) {
				System.out.println("Comparing to...");
				System.out.println("qs = " + cts(qs) + ", qe = " + cts(qe));
				if (!(c1.compareTo(qe) >= 0) && !(c2.compareTo(qs) <= 0)) {
					yqlist.add(i);
					System.out.println( "adding quarter :" + i );
				}
				qs.add(Calendar.MONTH, 3);
				qe.add(Calendar.MONTH, 3);
			}
			
			YearQuarters qL = new YearQuarters();
			qL.setYear(y);
			qL.setQuarters(yqlist);
			
			quarterList.add(qL);
		}
		
		return quarterList;
	}
	
	// return year, month
	public static List<Date> getMonthsList(Date d1, Date d2) {
		// swap d1, d2 if d1 > d2;
		if (d1.compareTo(d2) > 0) {
			Date tmp = d2;
			d2 = d1;
			d1 = tmp;
		}

		List<Date> monthsList = new ArrayList<Date>();
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		// current month
		Calendar cm = Calendar.getInstance(); // start month
		cm.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), 1);
		
		do {
			monthsList.add(cm.getTime()); // add current month to the list
			cm.add(Calendar.MONTH, 1);    // add a month
		} while (cm.compareTo(c2) <= 0);
		
		return monthsList;
	}

	public static String time(Date d) {
		DateFormat fmt = DateFormat.getTimeInstance();
		
		return fmt.format(d);
	}


}
