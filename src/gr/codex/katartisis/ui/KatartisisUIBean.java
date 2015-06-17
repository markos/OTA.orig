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

/* $Id: KatartisisUIBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.katartisis.ui;

import gr.codex.katartisis.CycleType;
import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.katartisis.PhaseType;
import gr.codex.katartisis.SubmissionStatus;
import gr.codex.katartisis.entitybeans.Cycle;
import gr.codex.katartisis.entitybeans.KatartisisEvent;
import gr.codex.katartisis.entitybeans.forms.KatartisisForm;
import gr.codex.ota.OTABean;
import gr.codex.ota.entitybeans.Section_L;
import gr.codex.ota.entitybeans.Section_M;
import gr.codex.ota.helpers.NOM_Contractor;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.Contractor;
import gr.codex.usermgmt.entitybeans.Municipality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * KatartisisUIBean is the main managed bean that controls subprojects of type Katartisis, it is responsible for
 * creating new Imerides, Dimotika Symboulia, Seminaria objects, create forms, and controls navigation throughout Katartisis subproejcts.
 * 
 * @author Theodore Karkoulis <tkarkoulis@theodorekarkoulis.com>
 *
 */

public class KatartisisUIBean {

	private Section_L sec_l;
	private Cycle cycle;
	private KatartisisEvent event;
	private KatartisisForm form;
	
	private HtmlDataTable cycles_dataTable;
	private HtmlDataTable events_dataTable;
	private HtmlDataTable forms_dataTable;
	private String addEventButtonMsg;
	private String eventTypeString;
	private String contractorName;
	
	Municipality def_mun;
	private Contractor contractor;
	
	private static final List<PhaseType> allPhases = 
		new ArrayList<PhaseType>() {{ 
			add(PhaseType.PREPARATION);
			add(PhaseType.IMPLEMENTATION);
			add(PhaseType.REVIEW);
		}};
	private static final TreeMap<String, String> producedFormsNames = 
		new TreeMap<String, String>() {{  
				put("A1", "Α1");
				put("A2", "Α2");
				put("A3", "Α3");
				put("A4", "Α4");
				put("A5", "Α5");
				put("A6", "Α6");
				put("A7", "Α7");
				put("B1", "Β1");
				put("B2", "Β2");
				put("B3", "Β3");
				put("B4", "Β4");
				put("B5", "Β5");
				put("B6", "Β6");
				put("B7", "Β7");
				put("C1", "Γ1");
				put("C2", "Γ2");
				put("C3", "Γ3");
				put("C4", "Γ4");
				put("C5", "Γ5");
				put("C6", "Ελεγχος Εκτέλεσης");
		}};
	private Map<String, Boolean> producedFormsShowMap;
	
	Context ctx;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;
	OTABean otaBean;
	
	/**
	 * Class Constructor, initializes some default values to null, "", or creates some default new objects. 
	 */
	public KatartisisUIBean() {
		FacesContext fc = FacesContext.getCurrentInstance();
		otaBean = (OTABean) fc.getApplication().createValueBinding("#{otaBean}").getValue(fc);
		
		// start remote connection to EJB3 backend.
		try {
			ctx = new InitialContext();
		
			// initialize remote interface
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
		this.producedFormsShowMap = new HashMap<String, Boolean>();
		
		// Create a municipality object as the default (value is A101 = Athens)
		this.def_mun = ubean.getMunicipalityById("Α101");
	}

	public void clearAllDataTables() {
		this.cycles_dataTable = new HtmlDataTable();
		this.events_dataTable = new HtmlDataTable();
		this.forms_dataTable  = new HtmlDataTable();
	}
	
	/**
	 * This method loads the Cycles for the selected subproject, and populates a list of the cycles for the selected subproject.
	 * Its return value will then be used by JSF Faces to determine the navigation action.
	 * 
	 * @return String 'success' or 'failure'
	 */
	public String loadCycles() {
		this.sec_l = (Section_L) otaBean.getSubProjectdataTable().getRowData();

		otaBean.clearAllDataTables();
		clearAllDataTables();
		
		for (Cycle c: this.sec_l.getKatartisis_Cycles()) {
			System.out.println("sec_l.id : " + this.sec_l.getId());
			System.out.println("Cycle c: " + c.getId() +", name: " + c.getName() + ", type: " + c.getType() + ", subproject: " +c.getSubProject().getId());
		}

		Section_M m = kbean.getLastNomikiDesmefsiBySubProject(this.sec_l);
		if (m != null) {
			if (m.isContractorsUnion() == false) {
				List<NOM_Contractor> contractors= kbean.getContractorsBySubProject(this.sec_l);
				for (NOM_Contractor c: contractors) {
					System.out.println("subproject: " + this.sec_l.getId() + ", contractor: " + c.getContractor().getName());
				}
				if (contractors.size() > 0) {
					this.contractor = contractors.get(0).getContractor();
					this.contractorName = this.contractor.getFullName();
				}
			} else {
				this.contractorName = m.getUnionTitle();
			}
		}

		return "cycles";
	}
	
	public String loadEvents() {
		Cycle oldcycle = this.cycle;
		try {
			this.cycle = (Cycle) this.cycles_dataTable.getRowData();
		} catch (Exception e) {
			this.cycle = oldcycle;
		}

		clearAllDataTables();
				
		// Go to the respective Imerides/DS/Seminaria page
		if (this.cycle.getType() == CycleType.CYCLE_IMERIDA) {
			this.addEventButtonMsg = "Προσθήκη Ημερίδας";
			this.eventTypeString = "Ημερίδες";
		} else if (this.cycle.getType() == CycleType.CYCLE_DS) {
			this.addEventButtonMsg = "Προσθήκη Δημοτικού Συμβουλίου";
			this.eventTypeString = "Δημοτικά Συμβούλια";
		} else if (this.cycle.getType() == CycleType.CYCLE_SEMINAR) {
			this.addEventButtonMsg = "Προσθήκη Σεμιναρίου";
			this.eventTypeString = "Σεμινάρια";
		}
		
		reloadCycle();
		
		return "events";
	}
	
	/**
	 * Here's where we will create a new Event.
	 * 
	 * @return String 'event-created'
	 */
	public String addEvent() {

		KatartisisEvent event = new KatartisisEvent(this.cycle.getType());
		if (this.cycle.getEvents() != null)
			event.setIndex(this.cycle.getEvents().size() +1);
		else
			event.setIndex(1);
		event.setMunicipality(def_mun);
		event.setParentCycle(this.cycle);
		kbean.createRecord(event);
		
		reloadCycle();
		
		clearAllDataTables();
		
		return "event_created";			
	}
	
	/**
	 * Here's where we will create a new Event.
	 * 
	 * @return String 'event-created'
	 */
	public String deleteEvent() {

		try {
			KatartisisEvent event = (KatartisisEvent) this.events_dataTable.getRowData();
			this.cycle.getEvents().remove(event);
			this.cycle = (Cycle) kbean.updateRecord(this.cycle);
			kbean.deleteRecord(KatartisisEvent.class, event.getId());
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Shouldn't be here!");
		}
		
		clearAllDataTables();
		
		reloadCycle();
		
		return "event_deleted";			
	}
	
	/**
	 * Loads the Forms for the current Event
	 * 
	 * @return String 'success' or 'failure'
	 */
	public String loadForms() {
		KatartisisEvent oldevent = this.event;
		try {
			this.event = (KatartisisEvent) this.events_dataTable.getRowData();
		} catch (Exception e) {
			if (oldevent != null) 
				this.event = oldevent;
		}
			
		clearAllDataTables();

		reloadEvent();
		
		// populate a table which shows, depending on the rest of the forms status, the Produced Forms.
		populateProducedFormsTable();

		return "events_forms";
	}
	
	public void reloadCycle() {
		this.cycle = (Cycle) kbean.getRecordById(this.cycle.getId(), Cycle.class);
	}
	
	public void reloadEvent() {
		this.event = (KatartisisEvent) kbean.getRecordById(this.event.getId(), KatartisisEvent.class);
	}
	
	public void updateFormsStatus() {
		// check PREPARATION forms first, preset 
		boolean result = true;
		for (KatartisisForm form: this.event.getFormsList()) {
			if (form.getPhase() == PhaseType.PREPARATION && form.getStatus() != SubmissionStatus.APPROVED) {
				result = false;
			}
		}
		if (result == true) {
			for (KatartisisForm form: this.event.getFormsList()) {
				if (form.getPhase() == PhaseType.IMPLEMENTATION && form.getStatus() == SubmissionStatus.STALLED) {
					form.setStatus(SubmissionStatus.INCOMPLETE);
					kbean.updateRecord(form);
				}
			}
		}
		// Do sth similar for IMPLEMENTATION PHASE, but we don't have to have approval
		result = true;
		for (KatartisisForm form: this.event.getFormsList()) {
			if (form.getPhase() == PhaseType.IMPLEMENTATION && 
					(form.getStatus() != SubmissionStatus.SUBMITTED && form.getStatus() != SubmissionStatus.APPROVED)) {
				result = false;
			}
		}
		if (result == true) {
			for (KatartisisForm form: this.event.getFormsList()) {
				if (form.getPhase() == PhaseType.REVIEW && form.getStatus() == SubmissionStatus.STALLED) {
					form.setStatus(SubmissionStatus.INCOMPLETE);
					kbean.updateRecord(form);
				}
			}
		}
	}
	
	/**
	 * Retrieves all the Forms for the current Phase, which are then evaluated. Depending on the Status of those Forms,
	 * various PRODUCED FORMS will be unlocked, and SHOWN, otherwise they will remain HIDDEN. This method will construct
	 * a JSF PanelGrid object will be BINDED to the front-end XHTML.
	 */
	public void populateProducedFormsTable() {
		for (String key: producedFormsNames.keySet()) {
			producedFormsShowMap.put(key, true);
		}
		if (this.cycle.getType() == CycleType.CYCLE_DS) {
			producedFormsShowMap.put("A4", false);
			producedFormsShowMap.put("A5", false);
			producedFormsShowMap.put("A6", false);
			producedFormsShowMap.put("A7", false);
			producedFormsShowMap.put("B6", false);
			producedFormsShowMap.put("B7", false);
			producedFormsShowMap.put("C3", false);
			producedFormsShowMap.put("C4", false);
			producedFormsShowMap.put("C5", false);
			producedFormsShowMap.put("C6", false);
		}
		if (this.cycle.getType() == CycleType.CYCLE_SEMINAR) {
			producedFormsShowMap.put("B4", false);
			producedFormsShowMap.put("B5", false);
			producedFormsShowMap.put("B6", false);
			producedFormsShowMap.put("B7", false);
			producedFormsShowMap.put("C3", false);
			producedFormsShowMap.put("C4", false);
			producedFormsShowMap.put("C5", false);
			producedFormsShowMap.put("C6", false);
		}
		
		// evaluate cycleType to know which Forms we will be evaluating next.
		for (KatartisisForm form : this.event.getFormsList()) {
			for (String key: form.getProducedForms().keySet()) {
				boolean current = producedFormsShowMap.get(key);
				producedFormsShowMap.put(key, current & form.getProducedForms().get(key));
			}
		}
	}

	// GETTERS - SETTERS
	/**
	 * @return the sec_l
	 */
	public Section_L getSec_l() {
		return sec_l;
	}

	/**
	 * @param sec_l the sec_l to set
	 */
	public void setSec_l(Section_L sec_l) {
		this.sec_l = sec_l;
	}
	
	/**
	 * @return the contractor
	 */
	public Contractor getContractor() {
		return contractor;
	}

	/**
	 * @param contractor the contractor to set
	 */
	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	/**
	 * @return the cycle
	 */
	public Cycle getCycle() {
		return cycle;
	}

	/**
	 * @param cycle the cycle to set
	 */
	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return the form
	 */
	public KatartisisForm getForm() {
		return form;
	}

	/**
	 * @return the def_mun
	 */
	public Municipality getDef_mun() {
		return def_mun;
	}

	/**
	 * @return the producedFormsNames
	 */
	public TreeMap<String, String> getProducedFormsNames() {
		return producedFormsNames;
	}

	/**
	 * @return the addEventButtonMsg
	 */
	public String getAddEventButtonMsg() {
		return addEventButtonMsg;
	}

	/**
	 * @param addEventButtonMsg the addEventButtonMsg to set
	 */
	public void setAddEventButtonMsg(String addEventButtonMsg) {
		this.addEventButtonMsg = addEventButtonMsg;
	}

	/**
	 * @return the event
	 */
	public KatartisisEvent getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(KatartisisEvent event) {
		this.event = event;
	}

	/**
	 * @return the cycles_dataTable
	 */
	public HtmlDataTable getCycles_dataTable() {
		return cycles_dataTable;
	}

	/**
	 * @param cycles_dataTable the cycles_dataTable to set
	 */
	public void setCycles_dataTable(HtmlDataTable cycles_dataTable) {
		this.cycles_dataTable = cycles_dataTable;
	}

	/**
	 * @return the events_dataTable
	 */
	public HtmlDataTable getEvents_dataTable() {
		return events_dataTable;
	}

	/**
	 * @param events_dataTable the events_dataTable to set
	 */
	public void setEvents_dataTable(HtmlDataTable events_dataTable) {
		this.events_dataTable = events_dataTable;
	}

	/**
	 * @return the forms_dataTable
	 */
	public HtmlDataTable getForms_dataTable() {
		return forms_dataTable;
	}

	/**
	 * @param forms_dataTable the forms_dataTable to set
	 */
	public void setForms_dataTable(HtmlDataTable forms_dataTable) {
		this.forms_dataTable = forms_dataTable;
	}

	/**
	 * @return the eventTypeString
	 */
	public String getEventTypeString() {
		return eventTypeString;
	}

	/**
	 * @param eventTypeString the eventTypeString to set
	 */
	public void setEventTypeString(String eventTypeString) {
		this.eventTypeString = eventTypeString;
	}

	/**
	 * @return the allPhases
	 */
	public List<PhaseType> getAllPhases() {
		return allPhases;
	}

	/**
	 * @return the producedFormsShowMap
	 */
	public Map<String, Boolean> getProducedFormsShowMap() {
		return producedFormsShowMap;
	}
	
	public List<String> getProducedFormsNamesList() {
		List<String> names = new ArrayList<String>();
		for (Map.Entry<String,String> e: this.producedFormsNames.entrySet())
			names.add(e.getKey());
		return names;
	}

	/**
	 * @return the contractorName
	 */
	public String getContractorName() {
		return contractorName;
	}

	/**
	 * @param contractorName the contractorName to set
	 */
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
}