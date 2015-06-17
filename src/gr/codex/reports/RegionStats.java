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

/* $Id: RegionStats.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.reports;


public class RegionStats {
	private int events_total;
	private int events_total_actual;
	private int electives_total;
	private int participants_total;
	private int mayors_total;
	private int councilmembers_total;
	private int speakers_total;
	
	/**
	 * 
	 */
	public RegionStats() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the electives_total
	 */
	public int getElectives_total() {
		return electives_total;
	}

	/**
	 * @param electives_total the electives_total to set
	 */
	public void setElectives_total(int electives_total) {
		this.electives_total = electives_total;
	}

	/**
	 * @return the participants_total
	 */
	public int getParticipants_total() {
		return participants_total;
	}

	/**
	 * @param participants_total the participants_total to set
	 */
	public void setParticipants_total(int participants_total) {
		this.participants_total = participants_total;
	}

	/**
	 * @return the total_percentage
	 */
	public double getTotal_percentage() {
		if (electives_total > 0)
			return (double)(participants_total)/(double)(electives_total);
		else
			return 0.0;
	}

	/**
	 * @return the mayors_total
	 */
	public int getMayors_total() {
		return mayors_total;
	}

	/**
	 * @param mayors_total the mayors_total to set
	 */
	public void setMayors_total(int mayors_total) {
		this.mayors_total = mayors_total;
	}

	/**
	 * @return the councilmembers_total
	 */
	public int getCouncilmembers_total() {
		return councilmembers_total;
	}

	/**
	 * @param councilmembers_total the councilmembers_total to set
	 */
	public void setCouncilmembers_total(int councilmembers_total) {
		this.councilmembers_total = councilmembers_total;
	}

	/**
	 * @return the speakers_total
	 */
	public int getSpeakers_total() {
		return speakers_total;
	}

	/**
	 * @param speakers_total the speakers_total to set
	 */
	public void setSpeakers_total(int speakers_total) {
		this.speakers_total = speakers_total;
	}

	/**
	 * @return the events_total
	 */
	public int getEvents_total() {
		return events_total;
	}

	/**
	 * @param events_total the events_total to set
	 */
	public void setEvents_total(int events_total) {
		this.events_total = events_total;
	}
	
	public void incrEvents_total() {
		this.events_total++;
	}
	
	public void incrEvents_total_actual() {
		this.events_total_actual++;
	}
	
	public void addToElectives_total(int electives_total) {
		this.electives_total += electives_total;
	}
	
	public void addToParticipants_total(int participants_total) {
		this.participants_total += participants_total;
	}
	
	public void addToMayors_total(int mayors_total) {
		this.mayors_total += mayors_total;
	}
	
	public void addToCouncilmembers_total(int councilmembers_total) {
		this.councilmembers_total += councilmembers_total;
	}
	
	public void addToSpeakers_total(int speakers_total) {
		this.speakers_total += speakers_total;
	}

	/**
	 * @return the events_total_actual
	 */
	public int getEvents_total_actual() {
		return events_total_actual;
	}

	/**
	 * @param events_total_actual the events_total_actual to set
	 */
	public void setEvents_total_actual(int events_total_actual) {
		this.events_total_actual = events_total_actual;
	}

	/**
	 * @return the events_total_diff
	 */
	public int getEvents_total_diff() {
		return Math.abs(this.events_total - this.events_total_actual);
	}

	/**
	 * @return the participants_total_diff
	 */
	public int getParticipants_total_diff() {
		return Math.abs(this.electives_total - this.participants_total);
	}
}
