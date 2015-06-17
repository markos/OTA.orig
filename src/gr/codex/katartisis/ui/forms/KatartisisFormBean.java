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

/* $Id: KatartisisFormBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.katartisis.ui.forms;

import gr.codex.katartisis.CycleType;
import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.katartisis.ParticipantType;
import gr.codex.katartisis.SubmissionStatus;
import gr.codex.katartisis.entitybeans.AttendanceList;
import gr.codex.katartisis.entitybeans.Course;
import gr.codex.katartisis.entitybeans.KatartisisMaterialType;
import gr.codex.katartisis.entitybeans.Leaflet;
import gr.codex.katartisis.entitybeans.Participant;
import gr.codex.katartisis.entitybeans.forms.Form_DS_1;
import gr.codex.katartisis.entitybeans.forms.Form_DS_2;
import gr.codex.katartisis.entitybeans.forms.Form_DS_4;
import gr.codex.katartisis.entitybeans.forms.Form_DS_5;
import gr.codex.katartisis.entitybeans.forms.Form_DS_6;
import gr.codex.katartisis.entitybeans.forms.Form_DS_7;
import gr.codex.katartisis.entitybeans.forms.Form_HO_1;
import gr.codex.katartisis.entitybeans.forms.Form_HO_2;
import gr.codex.katartisis.entitybeans.forms.Form_HO_3;
import gr.codex.katartisis.entitybeans.forms.Form_HO_4;
import gr.codex.katartisis.entitybeans.forms.Form_HO_5;
import gr.codex.katartisis.entitybeans.forms.Form_HO_6;
import gr.codex.katartisis.entitybeans.forms.Form_HO_7;
import gr.codex.katartisis.entitybeans.forms.Form_HO_8;
import gr.codex.katartisis.entitybeans.forms.Form_HO_9;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_1;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_2;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_3;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_4;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_5;
import gr.codex.katartisis.entitybeans.forms.KatartisisForm;
import gr.codex.katartisis.ui.KatartisisUIBean;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.ElectiveType;
import gr.codex.usermgmt.entitybeans.Municipality;
import gr.codex.usermgmt.entitybeans.Prefecture;
import gr.codex.usermgmt.entitybeans.Region;
import gr.codex.usermgmt.entitybeans.Trainer;
import gr.codex.usermgmt.entitybeans.User;
import gr.codex.usermgmt.entitybeans.UserGroup;
import gr.codex.usermgmt.helpers.UserType;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author Konstantinos Margaritis <markos@codex.gr>
 * @author Theodore Karkoulis <tkarkoulis@theodorekarkoulis.com>
 *
 */
public class KatartisisFormBean {

	// Hmerides form objects
	Form_HO_1 form_ho_1;
	Form_HO_2 form_ho_2;
	Form_HO_3 form_ho_3;
	Form_HO_4 form_ho_4;
	Form_HO_5 form_ho_5;
	Form_HO_6 form_ho_6;
	Form_HO_7 form_ho_7;
	Form_HO_8 form_ho_8;
	Form_HO_9 form_ho_9;

	// Dimotika Sumboulia form objects
	Form_DS_1 form_ds_1;
	Form_DS_2 form_ds_2;
	Form_DS_4 form_ds_4;
	Form_DS_5 form_ds_5;
	Form_DS_6 form_ds_6;
	Form_DS_7 form_ds_7;
	
	// Hmerides form objects
	Form_SEM_1 form_sem_1;
	Form_SEM_2 form_sem_2;
	Form_SEM_3 form_sem_3;
	Form_SEM_4 form_sem_4;
	Form_SEM_5 form_sem_5;
	
	KatartisisForm currentForm;
	
	private Date invitationDate;
	private int participants_total;
	
	private List<Region> allRegions = new ArrayList<Region>();
	private List<Prefecture> allPrefectures = new ArrayList<Prefecture>();
	private List<Municipality> allMunicipalities = new ArrayList<Municipality>();
	
	private List<SelectItem> allRegionsSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> allPrefecturesSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> allMunicipalitiesSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> implPrefecturesSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> implMunicipalitiesSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> electiveTypeSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> participantTypeSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> speakerTypeSelectItems = new ArrayList<SelectItem>();
	
	private List<Participant> allElectives = new ArrayList<Participant>();
	private List<UserGroup> electiveGroups = new ArrayList<UserGroup>();
	private List<Participant> searchResultTrainers = new ArrayList<Participant>();
	private List<Participant> searchResultElectives = new ArrayList<Participant>();
	
	// Used in Form_SEM_4
	private String currentDate;
	private AttendanceList currentSpeakersList;
	private AttendanceList currentOfficialsList;
	private AttendanceList currentParticipantsList;
	
	private Integer selectRegion_id;
	private String selectPrefecture_id;
	private String selectMunicipality_id;
	private String selectMunicipality2_id;
	private String filterUserName;
	private String filterEKEPIS;
	private String filterFirstName;
	private String filterLastName;
	
	private HtmlDataTable speakerDataTable;
	private HtmlDataTable leafletDataTable;
	private HtmlDataTable electiveDataTable;
	private HtmlDataTable participantDataTable;
	private HtmlDataTable officialsDataTable;
	private HtmlDataTable coursesDataTable;
	private HtmlDataTable searchTrainersDataTable;
	private HtmlDataTable searchElectivesDataTable;
	private HtmlDataTable materialsDataTable;
	private HtmlDataTable reviewItemsDataTable;
	
	private HtmlDataTable printLeafletDataTable = new HtmlDataTable();
	private HtmlDataTable printMaterialDatatable = new HtmlDataTable();
	private HtmlDataTable printElectiveDatatable = new HtmlDataTable();
	private HtmlDataTable printSpeakerDatatable = new HtmlDataTable();
	
	private static DateFormat fmt = DateFormat.getDateInstance(DateFormat.SHORT);
	
	Context context;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;
	KatartisisUIBean mainBean;

	/**
	 * Class Constructor
	 */
	public KatartisisFormBean() {
		try {
			context = new InitialContext();
			kbean = (KatartisisBeanLocal) context.lookup("KatartisisBean/local");
			ubean = (UserManagementBeanLocal) context.lookup("UserManagementBean/local");
			
			FacesContext fc = FacesContext.getCurrentInstance();
			mainBean = (KatartisisUIBean) fc.getApplication().createValueBinding("#{katartisisUIBean}").getValue(fc);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		this.electiveTypeSelectItems.clear();
		for (ElectiveType type: ElectiveType.values()) {
			SelectItem s = new SelectItem(type.name(), type.toString());
			this.electiveTypeSelectItems.add(s);
		}
		
		this.speakerTypeSelectItems.clear();
		this.speakerTypeSelectItems.add( new SelectItem( ParticipantType.KEY_SPEAKER.name(), ParticipantType.KEY_SPEAKER.toString() ) );
		this.speakerTypeSelectItems.add( new SelectItem( ParticipantType.SPEAKER.name(), ParticipantType.SPEAKER.toString() ) );
		
		this.participantTypeSelectItems.clear();
		this.participantTypeSelectItems.add( new SelectItem( ParticipantType.PARTICIPANT.name(), ParticipantType.PARTICIPANT.toString() ) );
		this.participantTypeSelectItems.add( new SelectItem( ParticipantType.OFFICIAL.name(), ParticipantType.OFFICIAL.toString() ) );
		
		for (UserGroup g: ubean.getAllGroups()) {
			if (g.getType() == UserType.Elective) {
				this.electiveGroups.add(g);
			}
		}
	}
	
	public void clearAllDataTables() {
		this.speakerDataTable = new HtmlDataTable();
		this.leafletDataTable = new HtmlDataTable();
		this.electiveDataTable = new HtmlDataTable();
		this.officialsDataTable = new HtmlDataTable();
		this.participantDataTable = new HtmlDataTable();
		this.coursesDataTable = new HtmlDataTable();
		this.searchElectivesDataTable = new HtmlDataTable();
		this.searchTrainersDataTable = new HtmlDataTable();
		this.materialsDataTable = new HtmlDataTable();
		this.reviewItemsDataTable = new HtmlDataTable();
		
		this.searchResultElectives.clear();
		this.searchResultTrainers.clear();
	}
	
	public String getClearedDatatables() {
		this.printLeafletDataTable = new HtmlDataTable();
		this.printMaterialDatatable = new HtmlDataTable();
		this.printElectiveDatatable = new HtmlDataTable();
		this.printSpeakerDatatable = new HtmlDataTable();
		return "";
	}
	
	public String getClearAllDataTables() {
		clearAllDataTables();
		return "";
	}
	
	public String goToForm() {
		this.currentForm = (KatartisisForm) mainBean.getForms_dataTable().getRowData();
		
		return goToForm_actual();
	}
	
	public String goToForm_actual() {

		// create Municipality / Prefecture / Region
		Municipality mun = currentForm.getEvent().getMunicipality();
		this.selectRegion_id = mun.getRegion().getId();
		this.selectPrefecture_id = mun.getPrefecture().getId();
		this.selectMunicipality_id = mun.getId();
		
		// load HTML Lists for DropDown Menus
		regenerateRegionsSelectItems();
		regeneratePrefectureSelectItems();
		regenerateMunicipalitiesSelectItems();
		
		// Set up the actual form objects
		if (mainBean.getEvent().getParentCycle().getType() == CycleType.CYCLE_IMERIDA) {
			this.form_ho_1 = (Form_HO_1) mainBean.getEvent().getForms().get(Form_HO_1.class.getSimpleName());
			this.form_ho_2 = (Form_HO_2) mainBean.getEvent().getForms().get(Form_HO_2.class.getSimpleName());
			this.form_ho_3 = (Form_HO_3) mainBean.getEvent().getForms().get(Form_HO_3.class.getSimpleName());
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
			this.form_ho_5 = (Form_HO_5) mainBean.getEvent().getForms().get(Form_HO_5.class.getSimpleName());
			this.form_ho_6 = (Form_HO_6) mainBean.getEvent().getForms().get(Form_HO_6.class.getSimpleName());
			this.form_ho_7 = (Form_HO_7) mainBean.getEvent().getForms().get(Form_HO_7.class.getSimpleName());
			this.form_ho_8 = (Form_HO_8) mainBean.getEvent().getForms().get(Form_HO_8.class.getSimpleName());
			this.form_ho_9 = (Form_HO_9) mainBean.getEvent().getForms().get(Form_HO_9.class.getSimpleName());
		} else if (mainBean.getEvent().getParentCycle().getType() == CycleType.CYCLE_DS) {
			this.form_ds_1 = (Form_DS_1) mainBean.getEvent().getForms().get(Form_DS_1.class.getSimpleName());
			this.form_ds_2 = (Form_DS_2) mainBean.getEvent().getForms().get(Form_DS_2.class.getSimpleName());
			this.form_ds_4 = (Form_DS_4) mainBean.getEvent().getForms().get(Form_DS_4.class.getSimpleName());
			this.form_ds_5 = (Form_DS_5) mainBean.getEvent().getForms().get(Form_DS_5.class.getSimpleName());
			this.form_ds_6 = (Form_DS_6) mainBean.getEvent().getForms().get(Form_DS_6.class.getSimpleName());
			this.form_ds_7 = (Form_DS_7) mainBean.getEvent().getForms().get(Form_DS_7.class.getSimpleName());	
		} else if (mainBean.getEvent().getParentCycle().getType() == CycleType.CYCLE_SEMINAR) {
			this.form_sem_1 = (Form_SEM_1) mainBean.getEvent().getForms().get(Form_SEM_1.class.getSimpleName());
			this.form_sem_2 = (Form_SEM_2) mainBean.getEvent().getForms().get(Form_SEM_2.class.getSimpleName());
			this.form_sem_3 = (Form_SEM_3) mainBean.getEvent().getForms().get(Form_SEM_3.class.getSimpleName());
			this.form_sem_4 = (Form_SEM_4) mainBean.getEvent().getForms().get(Form_SEM_4.class.getSimpleName());
			this.form_sem_5 = (Form_SEM_5) mainBean.getEvent().getForms().get(Form_SEM_5.class.getSimpleName());
		}
		
		if (this.currentForm instanceof Form_SEM_4) {
			if (this.form_sem_1.getDates().size() > 0)
				this.currentDate = fmt.format(this.form_sem_1.getDates().get(0));
			
			if (this.form_sem_4.getOfficials().size() > 0)
				this.currentOfficialsList = this.form_sem_4.getOfficials().get(0);
			if (this.form_sem_4.getSpeakers().size() > 0)
				this.currentSpeakersList = this.form_sem_4.getSpeakers().get(0);
			if (this.form_sem_4.getParticipants().size() > 0)
				this.currentParticipantsList = this.form_sem_4.getParticipants().get(0);
			
			for (Participant p: this.currentOfficialsList.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				for (String d: p.getArrivalTimesMap().keySet())
					System.out.println("Date: " +  d + " -> " + p.getArrivalTimesMap().get(d));
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
			for (Participant p: this.currentSpeakersList.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				for (String d: p.getArrivalTimesMap().keySet())
					System.out.println("Date: " +  d + " -> " + p.getArrivalTimesMap().get(d));
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
			for (Participant p: this.currentParticipantsList.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				for (String d: p.getArrivalTimesMap().keySet())
					System.out.println("Date: " +  d + " -> " + p.getArrivalTimesMap().get(d));
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
		}
		
		mainBean.clearAllDataTables();
		clearAllDataTables();
				
		return this.currentForm.getClass().getSimpleName();
	}
	
	/**
	 * Save the Current Form Back in the Database!
	 * 
	 * @return String 'form-saved' or 'form-saveError'
	 */
	public String saveForm() {
		// check for Municipality
		if (this.currentForm instanceof Form_HO_1) {
			if( this.selectMunicipality_id != null && !this.selectMunicipality_id.equals("") ) {
				this.currentForm.getEvent().setMunicipality(ubean.getMunicipalityById(this.selectMunicipality_id));
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε ένα Δήμο!") );
				return "form_saveError";
			}
		}
		if( this.currentForm instanceof Form_DS_1) {
			if( this.selectMunicipality_id != null && !this.selectMunicipality_id.equals("") ) {
				this.currentForm.getEvent().setMunicipality(ubean.getMunicipalityById(this.selectMunicipality_id));
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε ένα Δήμο!") );
				return "form_saveError";
			}
		}
		if( this.currentForm instanceof Form_SEM_1) {
			if( this.selectMunicipality_id != null && !this.selectMunicipality_id.equals("") ) {
				this.currentForm.getEvent().setMunicipality(ubean.getMunicipalityById(this.selectMunicipality_id));
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε ένα Δήμο!") );
				return "form_saveError";
			}
			if (this.selectMunicipality2_id != null) {
				this.form_sem_1.setKEK_municipality(ubean.getMunicipalityById(this.selectMunicipality2_id));
			}
		}
		
		if (this.currentForm instanceof Form_SEM_3) {
			for (Course c: this.form_sem_3.getCourses()) {
				Participant p = (Participant) kbean.getRecordById(c.getKeyTrainer().getId(), Participant.class);
				c.setKeyTrainer(p);
			}
		}
		
		if (this.currentForm instanceof Form_SEM_4) {
			for (AttendanceList alist: this.form_sem_4.getSpeakers()) {
				for (Participant p: alist.getParticipants()) {
					for (String d: p.getArrivalTimesMap().keySet()) {
						System.out.println("date: " + d + " -> " + p.getArrivalTimesMap().get(d));
					}
					p.saveArrivalTimes(this.form_sem_1.getDates());
					p.saveDepartureTimes(this.form_sem_1.getDates());
					System.out.println("date string: " + p.getArrivalTimes());
				}
			}
			for (AttendanceList alist: this.form_sem_4.getOfficials()) {
				for (Participant p: alist.getParticipants()) {
					p.saveArrivalTimes(this.form_sem_1.getDates());
					p.saveDepartureTimes(this.form_sem_1.getDates());
				}
			}
			for (AttendanceList alist: this.form_sem_4.getParticipants()) {
				for (Participant p: alist.getParticipants()) {
					p.saveArrivalTimes(this.form_sem_1.getDates());
					p.saveDepartureTimes(this.form_sem_1.getDates());
				}
			}
		}
		
		System.out.println("Event date: " + this.currentForm.getEvent().getDate());
		// update DB record
		this.currentForm = (KatartisisForm) kbean.updateRecord( this.currentForm );
		mainBean.reloadEvent();
		mainBean.reloadCycle();
		
		return this.currentForm.getClass().getSimpleName();
	}
	
	public String submitForm() {
		this.saveForm();
		
		this.currentForm.setStatus(SubmissionStatus.SUBMITTED);
		this.currentForm = (KatartisisForm) kbean.updateRecord( this.currentForm );
		
		// Custom Handling of Imerides forms
		if (this.currentForm instanceof Form_HO_1) {
			reloadElectives(false);

			this.form_ho_1.setAllElectives_size(this.allElectives.size());
			
			this.form_ho_4.setDateActual(mainBean.getEvent().getDate());
			this.form_ho_4.setMunicipality(mainBean.getEvent().getMunicipality());
			for (Participant p: this.form_ho_1.getSpeakers()) {
				p.setArrivalTime(this.form_ho_1.getEvent().getTime_start());
				p.setDepartureTime(this.form_ho_1.getEvent().getTime_start());
			}
			this.form_ho_4.setSpeakers(this.form_ho_1.getSpeakers());
			this.form_ho_4.setElectives(this.allElectives);
			this.form_ho_4 = (Form_HO_4) kbean.updateRecord( this.form_ho_4 );
		}
		
		if (this.currentForm instanceof Form_HO_2) {
			for (KatartisisMaterialType material : this.form_ho_2.getMaterialsList()) {
				int materialqty = this.form_ho_2.getMaterialQty().get(material);
				if (materialqty > 0) {
					this.form_ho_8.getProducedMaterialsQty().put(material, materialqty);
					this.form_ho_8.getDistributedMaterialsQty().put(material, materialqty);
				} else {
					this.form_ho_8.getProducedMaterialsQty().remove(material);
					this.form_ho_8.getDistributedMaterialsQty().remove(material);
				}
			}
			this.form_ho_8 = (Form_HO_8) kbean.updateRecord( this.form_ho_8 );
		}
		
		if (this.currentForm instanceof Form_HO_2) {
			this.form_ho_6.setLeaflets(this.form_ho_2.getLeaflets());
			this.form_ho_6 = (Form_HO_6) kbean.updateRecord( this.form_ho_6 );
		}
		
		if (this.currentForm instanceof Form_HO_4) {
			for (Participant p: this.form_ho_4.getSpeakers()) {
				p.setArrivalTime(this.form_ho_1.getEvent().getTime_start());
				p.setDepartureTime(this.form_ho_1.getEvent().getTime_start());
			}
			this.form_ho_7.setSpeakers(this.form_ho_4.getSpeakers());
			this.form_ho_7 = (Form_HO_7) kbean.updateRecord( this.form_ho_7 );
		}

		// Custom handling of Municipal Councils forms
		if (this.currentForm instanceof Form_DS_1) {
			reloadElectives(true);

			this.form_ds_1.setAllElectives_size(this.allElectives.size());
			
			this.form_ds_4.setDateActual(mainBean.getEvent().getDate());
			this.form_ds_4.setPlaceActual(mainBean.getEvent().getPlace());
			this.form_ds_4.setMunicipality(mainBean.getEvent().getMunicipality());
			for (Participant p: this.form_ds_1.getSpeakers()) {
				p.setArrivalTime(this.form_ds_1.getEvent().getTime_start());
				p.setDepartureTime(this.form_ds_1.getEvent().getTime_start());
			}
			this.form_ds_4.setSpeakers(this.form_ds_1.getSpeakers());
			this.form_ds_4 = (Form_DS_4) kbean.updateRecord( this.form_ds_4 );
			
			this.form_ds_6.setElectives(this.allElectives);
			this.form_ds_6 = (Form_DS_6) kbean.updateRecord( this.form_ds_6 );
		}
		
		if (this.currentForm instanceof Form_DS_2) {
			for (Participant p: this.form_ds_2.getElectives()) {
				p.setElectiveInvitationDate(this.form_ds_2.getInvitationDate());
			}
			this.form_ds_2 = (Form_DS_2) kbean.updateRecord( this.form_ds_2 );
		}
		
		if (this.currentForm instanceof Form_DS_4) {
			for (Participant p: this.form_ds_4.getSpeakers()) {
				if (p.getArrivalTime() == null)
					p.setArrivalTime(this.form_ds_1.getEvent().getTime_start());
				if (p.getDepartureTime() == null)
					p.setDuration(6);
			}
			this.form_ds_7.setSpeakers(this.form_ds_4.getSpeakers());
			this.form_ds_7 = (Form_DS_7) kbean.updateRecord( this.form_ds_7 );
		}
		
		// Custom handling of Seminar forms
		if (this.currentForm instanceof Form_SEM_2) {
			for (Participant p: this.form_sem_2.getParticipants()) {
				Municipality m = ubean.getMunicipalityById(p.getMunicipality_id());
				p.setMunicipality(m);
			}

			this.form_sem_4.setSpeakers(new ArrayList<AttendanceList>());
			this.form_sem_4.setParticipants(new ArrayList<AttendanceList>());
			this.form_sem_4.setOfficials(new ArrayList<AttendanceList>());
			this.form_sem_4.setMunicipality(mainBean.getEvent().getMunicipality());
			
			for (Date date: this.form_sem_1.getDates()) {
				String d = fmt.format(date);
				for (Participant p: this.form_sem_2.getSpeakers()) {
					p.setArrivalTime(d, "09:30");
					p.setDepartureTime(d, "18:30");
				}
				for (Participant p: this.form_sem_2.getParticipants()) {
					p.setArrivalTime(d, "09:30");
					p.setDepartureTime(d, "18:30");
				}
				AttendanceList alist1 = new AttendanceList();
				alist1.setDate(d);
				alist1.setParticipants(this.form_sem_2.getSpeakers());
				this.form_sem_4.addSpeakers(alist1);
				AttendanceList alist2 = new AttendanceList();
				alist2.setDate(d);
				alist2.setParticipants(this.form_sem_2.getParticipants());
				this.form_sem_4.addParticipants(alist2);
				AttendanceList alist3 = new AttendanceList();
				alist3.setDate(d);
				this.form_sem_4.addOfficials(alist3);
			}
			this.form_sem_4 = (Form_SEM_4) kbean.updateRecord( this.form_sem_4 );
			
			this.form_sem_5.setSpeakers(this.form_sem_2.getSpeakers());
			this.form_sem_5 = (Form_SEM_5) kbean.updateRecord( this.form_sem_5 );
		}
		
		mainBean.loadForms();
		mainBean.updateFormsStatus();
		mainBean.loadForms();
		
		return "form_submitted";
	}
	
	public String approveForm() {
		this.saveForm();
		
		this.currentForm.setStatus(SubmissionStatus.APPROVED);
		this.currentForm = (KatartisisForm) kbean.updateRecord( this.currentForm );
		
		mainBean.loadForms();
		mainBean.updateFormsStatus();
		mainBean.loadForms();
		
		return "form_approved";
	}
	
	public String rejectForm() {
		this.currentForm.setStatus(SubmissionStatus.INCOMPLETE);
		this.currentForm = (KatartisisForm) kbean.updateRecord( this.currentForm );
		
		if (this.currentForm instanceof Form_HO_1 || this.currentForm instanceof Form_HO_3) {
			this.form_ho_4.getParticipants().clear();
			this.form_ho_4 = (Form_HO_4) kbean.updateRecord( this.form_ho_4 );
		}
		
		mainBean.loadForms();
		mainBean.updateFormsStatus();
		mainBean.loadForms();
		
		return "form_rejected";
	}
	
	public void form_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regeneratePrefectureSelectItems();
			this.allMunicipalitiesSelectItems = new ArrayList<SelectItem>();
		}
		
		return;
	}
	
	public void form_changeMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateMunicipalitiesSelectItems();
		}
		
		return;
	}
	
	public void changed_SEM_4_currentDate(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			System.out.println("Current Date:" + this.currentDate);
			for (AttendanceList alist: this.form_sem_4.getSpeakers()) {
				System.out.println("Attendance List Date:" + alist.getDate());
				if (alist.getDate().equals(this.currentDate) == true) {
					this.currentSpeakersList = alist;
					for (Participant p: this.currentSpeakersList.getParticipants()) {
						p.loadArrivalTimes(this.form_sem_1.getDates());
						p.loadDepartureTimes(this.form_sem_1.getDates());
					}
					break;
				}
			}
			for (AttendanceList alist: this.form_sem_4.getOfficials()) {
				System.out.println("Attendance List Date:" + alist.getDate());
				if (alist.getDate().equals(this.currentDate) == true) {
					this.currentOfficialsList = alist;
					for (Participant p: this.currentOfficialsList.getParticipants()) {
						p.loadArrivalTimes(this.form_sem_1.getDates());
						p.loadDepartureTimes(this.form_sem_1.getDates());
					}
					break;
				}
			}
			for (AttendanceList alist: this.form_sem_4.getParticipants()) {
				System.out.println("Attendance List Date:" + alist.getDate());
				if (alist.getDate().equals(this.currentDate) == true) {
					this.currentParticipantsList = alist;
					for (Participant p: this.currentParticipantsList.getParticipants()) {
						p.loadArrivalTimes(this.form_sem_1.getDates());
						p.loadDepartureTimes(this.form_sem_1.getDates());
					}
					break;
				}
			}
		}
		
		return;
	}
	
	public void regenerateRegionsSelectItems() {

		// get list of all regions as a selectItem
		this.allRegionsSelectItems.clear();

		List<Region> regions = ubean.getAllRegions();
		for( Region r : regions) {
			SelectItem item = new SelectItem( r.getId(), r.getName() );
			this.allRegionsSelectItems.add(item);
		}
	}
	
	public void regeneratePrefectureSelectItems() {
		this.allPrefecturesSelectItems.clear();
		
		if (this.selectRegion_id != null) {
			List<Prefecture> allPrefectures = new ArrayList<Prefecture>();
			if (this.selectRegion_id.equals(-1)) {
				allPrefectures = ubean.getAllPrefectures();
			} else {
				Region r = ubean.getRegionById(this.selectRegion_id);
				allPrefectures = r.getPrefectures();
			}
			System.out.println("found " + allPrefectures.size() + " prefectures");

			for( Prefecture p : allPrefectures) {
				SelectItem item = new SelectItem(p.getId(), p.getName());
				this.allPrefecturesSelectItems.add(item);
			}
		}
	}
	
	public void regenerateMunicipalitiesSelectItems() {
		this.allMunicipalitiesSelectItems.clear();
	
		if (this.selectPrefecture_id != null) {
			List<Municipality> allMunicipalities = new ArrayList<Municipality>();
			if (this.selectPrefecture_id.equals("-1")) {
				allMunicipalities = ubean.getAllMunicipalitiesByRegionId(this.selectRegion_id);
			} else {
				Prefecture p = ubean.getPrefectureById(this.selectPrefecture_id);
				allMunicipalities = p.getMunicipalities();
			}
			System.out.println("found " + allMunicipalities.size() + " municipalities");

			for( Municipality m : allMunicipalities) {
				SelectItem item = new SelectItem(m.getId(), m.getName());
				this.allMunicipalitiesSelectItems.add(item);
			}
		}
	}
	
	public void reloadElectives(boolean municipalityOnly) {
		this.allElectives.clear();
		if (municipalityOnly == true) {
			this.selectMunicipality_id = mainBean.getEvent().getMunicipality().getId();
			for (User u : ubean.getAllUsersByMunicipalityId(this.selectMunicipality_id)) {
				if (u.getType() == UserType.Elective) {
					Participant p = u.toParticipant();
					p.setType(ParticipantType.ELECTIVE);
					this.allElectives.add(p);
				}
			}
		} else {
			this.selectPrefecture_id = mainBean.getEvent().getMunicipality().getPrefecture().getId();
			for (User u : ubean.getAllUsersByPrefectureId(this.selectPrefecture_id)) {
				if (u.getType() == UserType.Elective) {
					Participant p = u.toParticipant();
					p.setType(ParticipantType.ELECTIVE);
					this.allElectives.add(p);
				}
			}
		}
	}
	
	// Search methods
	public String searchTrainers() {
		this.searchResultTrainers.clear();
		
		if (this.filterUserName.length() == 0 &&
				this.filterEKEPIS.length() == 0 &&
				this.filterFirstName.length() == 0 &&
				this.filterLastName.length() == 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε τουλάχιστον ένα φίλτρο όταν η χωροθέτηση αφορά όλη τη χώρα!") );
			return "users";
		}
		
		List<Trainer> userList = ubean.getTrainersByCriteria(this.filterUserName, this.filterFirstName, this.filterLastName,
				this.filterEKEPIS, this.selectRegion_id, null, null);
		
		for (Trainer u : userList)
			this.searchResultTrainers.add(u.toParticipant());
		
		Collections.sort(this.searchResultTrainers);

		return "users-search";
	}
	
	// Search methods
	public String searchElectives() {
		this.searchResultElectives.clear();
		
		if (this.filterUserName.length() == 0 &&
				this.filterFirstName.length() == 0 &&
				this.filterLastName.length() == 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε τουλάχιστον ένα φίλτρο όταν η χωροθέτηση αφορά όλη τη χώρα!") );
			return "users";
		}
		
		List<User> userList = ubean.getUsersByCriteria(this.filterUserName, this.filterFirstName, this.filterLastName,
				this.selectRegion_id, this.selectPrefecture_id, null, electiveGroups, null);
		
		for (User u : userList)
			this.searchResultElectives.add(u.toParticipant());
		
		Collections.sort(this.searchResultElectives);

		return "users-search";
	}
	
	// Form_HO_1 methods
	public String form_HO_1_addSpeaker() {
		Participant speaker = (Participant) this.searchTrainersDataTable.getRowData();
		
		// add the participant to the speakers List
		this.form_ho_1.addSpeaker(speaker);

		return "success";
	}
	
	public String form_HO_1_addNewSpeaker() {
		this.form_ho_1.addSpeaker(new Participant());

		return "success";
	} 
	
	public String form_HO_1_delSpeaker() {
		try {
			Participant speaker = (Participant) this.speakerDataTable.getRowData();
		
			this.form_ho_1.removeSpeaker(speaker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	// Form_HO_2 methods
	public String form_HO_2_addNewLeaflet() {
		// add the leaflet			
		this.form_ho_2.addLeaflet( new Leaflet() );
		
		return "success";			
	}
	
	public String form_HO_2_delLeaflet() {
		Leaflet lft = (Leaflet) this.leafletDataTable.getRowData();
		this.form_ho_2.removeLeaflet(lft);
		
		return "success";
	}
	
	// Form_HO_3 methods
	public String form_HO_3_addElective() {
		Participant elective = (Participant) this.searchElectivesDataTable.getRowData();
		
		elective.setType(ParticipantType.ELECTIVE);
		// add the participant to the speakers List
		this.form_ho_3.addElective(elective);

		return "success";
	}
	
	public String form_HO_3_addPrefectureElectives() {
		List<Participant> electives = new ArrayList<Participant>();
		List<User> userList = ubean.getAllUsersByPrefectureId( this.selectPrefecture_id);
		
		for (User u : userList) {
			Participant p = u.toParticipant();
			p.setType(ParticipantType.ELECTIVE);
			electives.add(p);
		}

		Collections.sort(this.searchResultElectives); 
		// add the participant to the speakers List
		this.form_ho_3.addElectives(electives);

		return "success";
	}
	
	public String form_HO_3_addNewElective() {
		Participant elective = new Participant();
		elective.setType(ParticipantType.ELECTIVE);
		
		this.form_ho_3.addElective(elective);

		return "success";
	}
	
	public String form_HO_3_delElective() {
		Participant elective = (Participant) this.electiveDataTable.getRowData();
		
		this.form_ho_3.removeElective(elective);
		
		return "success";
	}
	
	// Form_HO_4 methods
	public String form_HO_4_addSpeaker() {
		Participant speaker = (Participant) this.searchTrainersDataTable.getRowData();
		
		// add the participant to the speakers List
		this.form_ho_4.addSpeaker(speaker);

		return "success";
	}
	
	public String form_HO_4_addNewSpeaker() {
		Participant speaker = new Participant();
		
		// add the participant to the speakers List
		this.form_ho_4.addSpeaker(speaker);

		return "success";
	} 
	
	public String form_HO_4_addOfficial() {
		// add the participant to the electives List
		Participant official = new Participant();
		official.setType(ParticipantType.OFFICIAL);
		this.form_ho_4.addOfficial(official);
		
		return "success";
	}
	
	public String form_HO_4_addParticipant() {
		// add the participant to the electives List
		Participant official = new Participant();
		official.setType(ParticipantType.PARTICIPANT);
		this.form_ho_4.addParticipant(official);
		
		return "success";
	}
	
	public String form_HO_4_addElective() {
		// add the participant to the electives List
		Participant elective = new Participant();
		elective.setElectiveInvitationDate(this.invitationDate);
		this.form_ho_4.addParticipant(elective);

		return "success";
	}
	
	public String form_HO_4_delSpeaker() {
		Participant speaker = (Participant) this.speakerDataTable.getRowData();
		
		this.form_ho_4.removeSpeaker(speaker);
		
		return "success";
	}
	
	public String form_HO_4_delParticipant() {
		Participant participant = (Participant) this.participantDataTable.getRowData();
		
		this.form_ho_4.removeParticipant(participant);
		
		return "success";
	}
	
	public String form_HO_4_delOfficial() {
		Participant official = (Participant) this.officialsDataTable.getRowData();
		
		this.form_ho_4.removeOfficial(official);
		
		return "success";
	}
	
	public String form_HO_4_delElective() {
		Participant elective = (Participant) this.electiveDataTable.getRowData();
		
		this.form_ho_4.removeElective(elective);
		
		return "success";
	}
	
	// Form_HO_6 methods
	public String form_HO_6_addNewLeaflet() {
		// add the leaflet			
		this.form_ho_6.addLeaflet( new Leaflet() );
		
		return "success";			
	}
	
	public String form_HO_6_delLeaflet() {
		Leaflet lft = (Leaflet) this.leafletDataTable.getRowData();		
		this.form_ho_6.removeLeaflet(lft);
		
		return "success";
	}

	// Form_DS_1 methods
	public String form_DS_1_addSpeaker() {
		Participant speaker = (Participant) this.searchTrainersDataTable.getRowData();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		// add the participant to the speakers List
		this.form_ds_1.addSpeaker(speaker);

		return "success";
	}
	
	public String form_DS_1_addNewSpeaker() {
		Participant speaker = new Participant();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		this.form_ds_1.addSpeaker(speaker);

		return "success";
	} 
	
	public String form_DS_1_delSpeaker() {
		Participant speaker = (Participant) this.speakerDataTable.getRowData();
		
		this.form_ds_1.removeSpeaker(speaker);
		
		return "success";
	}
	
	// Form_DS_2 methods
	public String form_DS_2_addElective() {
		Participant elective = (Participant) this.searchElectivesDataTable.getRowData();
		elective.setType(ParticipantType.ELECTIVE);
		
		// add the participant to the speakers List
		this.form_ds_2.addElective(elective);

		return "success";
	}
	
	public String form_DS_2_addNewElective() {
		Participant elective = new Participant();
		elective.setType(ParticipantType.ELECTIVE);
		
		this.form_ds_2.addElective(elective);

		return "success";
	}
	
	public String form_DS_2_delElective() {
		Participant elective = (Participant) this.speakerDataTable.getRowData();
		
		this.form_ds_2.removeElective(elective);
		
		return "success";
	}
	
	// Form_DS_4 methods
	public String form_DS_4_addSpeaker() {
		Participant speaker = (Participant) this.searchTrainersDataTable.getRowData();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		// add the participant to the speakers List
		this.form_ds_4.addSpeaker(speaker);

		return "success";
	}
	
	public String form_DS_4_addNewSpeaker() {
		Participant speaker = new Participant();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		// add the participant to the speakers List
		this.form_ds_4.addSpeaker(speaker);

		return "success";
	} 
	public String form_DS_4_delSpeaker() {
		Participant speaker = (Participant) this.speakerDataTable.getRowData();
		
		this.form_ds_4.removeSpeaker(speaker);
		
		return "success";
	}	
	
	// Form_DS_6 methods
	public String form_DS_6_addOfficial() {
		Participant official = new Participant();
		
		this.form_ds_6.addOfficial(official);
		
		return "success";
	}
	
	public String form_DS_6_addParticipant() {
		Participant participant = new Participant();
		
		this.form_ds_6.addParticipant(participant);
		
		return "success";
	}
	
	public String form_DS_6_addElective() {

		return "success";
	}
	
	public String form_DS_6_delParticipant() {
		Participant participant = (Participant) this.participantDataTable.getRowData();
		
		this.form_ds_6.removeParticipant(participant);
		
		return "success";
	}
	
	public String form_DS_6_delOfficial() {
		Participant official = (Participant) this.officialsDataTable.getRowData();
		
		this.form_ds_6.removeOfficial(official);
		
		return "success";
	}
	
	public String form_DS_6_delElective() {
		Participant elective = (Participant) this.electiveDataTable.getRowData();
		
		this.form_ds_6.removeElective(elective);
		
		return "success";
	}
	
	// Form_SEM_2 methods
	public String form_SEM_2_addNewSpeaker() {
		Participant speaker = new Participant();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		// add the participant to the speakers List
		this.form_sem_2.addSpeaker(speaker);
		
		return "success";
	}
		
	public String form_SEM_2_addSpeaker() {
		Participant speaker = (Participant) this.searchTrainersDataTable.getRowData();
		speaker.setType(ParticipantType.KEY_SPEAKER);
		
		// add the participant to the speakers List
		this.form_sem_2.addSpeaker(speaker);
		
		return "success";
	}
	
	public String form_SEM_2_addParticipant() {
		Participant participant = new Participant();
		
		this.form_sem_2.addParticipant(participant);
		
		return "success";
	}
	
	public String form_SEM_2_delParticipant() {
		Participant participant = (Participant) this.participantDataTable.getRowData();
		
		this.form_sem_2.removeParticipant(participant);
		
		return "success";
	}
	
	public String form_SEM_2_delSpeaker() {
		Participant speaker = (Participant) this.speakerDataTable.getRowData();
		
		this.form_sem_2.removeSpeaker(speaker);
		
		return "success";
	}
	
	// Form_SEM_3 methods
	public String form_SEM_3_addCourse() {
		Course course = new Course(); 
		course.setDate(mainBean.getEvent().getDate());
		course.setLessonStart(mainBean.getEvent().getTime_start());
		course.setLessonEnd(mainBean.getEvent().getTime_end());
		
		this.form_sem_3.addCourse(course );
		
		return "success";			
	}
	
	public String form_SEM_3_delCourse() {
		Course course = (Course) this.coursesDataTable.getRowData();
		
		this.form_sem_3.removeCourse(course);
		
		return "success";
	}
	
	// Form_SEM_4 methods
	public String form_SEM_4_addParticipant() {
		Participant participant = new Participant();
		
		this.currentParticipantsList.addParticipant(participant);
		
		return "success";
	}
	
	public String form_SEM_4_addOfficial() {
		Participant official = new Participant();
		official.setType(ParticipantType.OFFICIAL);
		
		// add the participant to the speakers List
		this.currentParticipantsList.addParticipant(official);

		return "success";
	}
	
	public String form_SEM_4_delParticipant() {
		Participant participant = (Participant) this.participantDataTable.getRowData();
		
		this.currentOfficialsList.removeParticipant(participant);
		
		return "success";
	}
	
	public String form_SEM_4_delOfficial() {
		Participant official = (Participant) this.officialsDataTable.getRowData();
		
		this.currentOfficialsList.removeParticipant(official);
		
		return "success";
	}
	
	// Helper functions
	public boolean getReadonlyForm() {
		if (this.currentForm.getStatus() == SubmissionStatus.APPROVED ||
				this.currentForm.getStatus() == SubmissionStatus.SUBMITTED)
			return true; 
		else
			return false;
	}

	/**
	 * @return the currentForm
	 */
	public KatartisisForm getCurrentForm() {
		return currentForm;
	}

	/**
	 * @param currentForm the currentForm to set
	 */
	public void setCurrentForm(KatartisisForm currentForm) {
		this.currentForm = currentForm;
	}

	/**
	 * @return the allRegions
	 */
	public List<Region> getAllRegions() {
		return allRegions;
	}

	/**
	 * @param allRegions the allRegions to set
	 */
	public void setAllRegions(List<Region> allRegions) {
		this.allRegions = allRegions;
	}

	/**
	 * @return the allPrefectures
	 */
	public List<Prefecture> getAllPrefectures() {
		return allPrefectures;
	}

	/**
	 * @param allPrefectures the allPrefectures to set
	 */
	public void setAllPrefectures(List<Prefecture> allPrefectures) {
		this.allPrefectures = allPrefectures;
	}

	/**
	 * @return the allMunicipalities
	 */
	public List<Municipality> getAllMunicipalities() {
		return allMunicipalities;
	}

	/**
	 * @param allMunicipalities the allMunicipalities to set
	 */
	public void setAllMunicipalities(List<Municipality> allMunicipalities) {
		this.allMunicipalities = allMunicipalities;
	}

	/**
	 * @return the allRegionsSelectItems
	 */
	public List<SelectItem> getAllRegionsSelectItems() {
		return allRegionsSelectItems;
	}

	/**
	 * @param allRegionsSelectItems the allRegionsSelectItems to set
	 */
	public void setAllRegionsSelectItems(List<SelectItem> allRegionsSelectItems) {
		this.allRegionsSelectItems = allRegionsSelectItems;
	}

	/**
	 * @return the allPrefecturesSelectItems
	 */
	public List<SelectItem> getAllPrefecturesSelectItems() {
		return allPrefecturesSelectItems;
	}

	/**
	 * @param allPrefecturesSelectItems the allPrefecturesSelectItems to set
	 */
	public void setAllPrefecturesSelectItems(
			List<SelectItem> allPrefecturesSelectItems) {
		this.allPrefecturesSelectItems = allPrefecturesSelectItems;
	}

	/**
	 * @return the allMunicipalitiesSelectItems
	 */
	public List<SelectItem> getAllMunicipalitiesSelectItems() {
		return allMunicipalitiesSelectItems;
	}

	/**
	 * @param allMunicipalitiesSelectItems the allMunicipalitiesSelectItems to set
	 */
	public void setAllMunicipalitiesSelectItems(
			List<SelectItem> allMunicipalitiesSelectItems) {
		this.allMunicipalitiesSelectItems = allMunicipalitiesSelectItems;
	}

	/**
	 * @return the selectRegion_id
	 */
	public Integer getSelectRegion_id() {
		return selectRegion_id;
	}

	/**
	 * @param selectRegion_id the selectRegion_id to set
	 */
	public void setSelectRegion_id(Integer selectRegion_id) {
		this.selectRegion_id = selectRegion_id;
	}

	/**
	 * @return the selectPrefecture_id
	 */
	public String getSelectPrefecture_id() {
		return selectPrefecture_id;
	}

	/**
	 * @param selectPrefecture_id the selectPrefecture_id to set
	 */
	public void setSelectPrefecture_id(String selectPrefecture_id) {
		this.selectPrefecture_id = selectPrefecture_id;
	}

	/**
	 * @return the selectMunicipality_id
	 */
	public String getSelectMunicipality_id() {
		return selectMunicipality_id;
	}

	/**
	 * @param selectMunicipality_id the selectMunicipality_id to set
	 */
	public void setSelectMunicipality_id(String selectMunicipality_id) {
		this.selectMunicipality_id = selectMunicipality_id;
	}

	/**
	 * @return the form_ho_1
	 */
	public Form_HO_1 getForm_ho_1() {
		if (this.form_ho_1 == null)
			this.form_ho_1 = (Form_HO_1) mainBean.getEvent().getForms().get(Form_HO_1.class.getSimpleName());
		System.out.println("all electives: " + this.allElectives.size());
		if (this.allElectives.size() == 0 )
			reloadElectives(false);
		System.out.println("all electives: " + this.allElectives.size());
		return form_ho_1;
	}

	/**
	 * @return the form_ho_2
	 */
	public Form_HO_2 getForm_ho_2() {
		if (this.form_ho_2 == null)
			this.form_ho_2 = (Form_HO_2) mainBean.getEvent().getForms().get(Form_HO_2.class.getSimpleName());
		return form_ho_2;
	}

	/**
	 * @return the form_ho_3
	 */
	public Form_HO_3 getForm_ho_3() {
		if (this.form_ho_3 == null)
			this.form_ho_3 = (Form_HO_3) mainBean.getEvent().getForms().get(Form_HO_3.class.getSimpleName());
		return form_ho_3;
	}

	/**
	 * @return the form_ho_4
	 */
	public Form_HO_4 getForm_ho_4() {
		if (this.form_ho_4 == null) {
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
		}
		return form_ho_4;
	}

	/**
	 * @return the form_ho_5
	 */
	public Form_HO_5 getForm_ho_5() {
		if (this.form_ho_5 == null)
			this.form_ho_5 = (Form_HO_5) mainBean.getEvent().getForms().get(Form_HO_5.class.getSimpleName());
		return form_ho_5;
	}

	/**
	 * @return the form_ho_6
	 */
	public Form_HO_6 getForm_ho_6() {
		if (this.form_ho_6 == null)
			this.form_ho_6 = (Form_HO_6) mainBean.getEvent().getForms().get(Form_HO_6.class.getSimpleName());
		return form_ho_6;
	}

	/**
	 * @return the form_ho_7
	 */
	public Form_HO_7 getForm_ho_7() {
		if (this.form_ho_7 == null)
			this.form_ho_7 = (Form_HO_7) mainBean.getEvent().getForms().get(Form_HO_7.class.getSimpleName());
		return form_ho_7;
	}

	/**
	 * @return the form_ho_8
	 */
	public Form_HO_8 getForm_ho_8() {
		if (this.form_ho_8 == null)
			this.form_ho_8 = (Form_HO_8) mainBean.getEvent().getForms().get(Form_HO_8.class.getSimpleName());
		return form_ho_8;
	}

	/**
	 * @return the form_ho_9
	 */
	public Form_HO_9 getForm_ho_9() {
		if (this.form_ho_9 == null)
			this.form_ho_9 = (Form_HO_9) mainBean.getEvent().getForms().get(Form_HO_9.class.getSimpleName());
		return form_ho_9;
	}

	/**
	 * @return the form_ds_1
	 */
	public Form_DS_1 getForm_ds_1() {
		if (this.form_ds_1 == null)
			this.form_ds_1 = (Form_DS_1) mainBean.getEvent().getForms().get(Form_DS_1.class.getSimpleName());
		return form_ds_1;
	}

	/**
	 * @return the form_ds_2
	 */
	public Form_DS_2 getForm_ds_2() {
		if (this.form_ds_2 == null)
			this.form_ds_2 = (Form_DS_2) mainBean.getEvent().getForms().get(Form_DS_2.class.getSimpleName());
		return form_ds_2;
	}

	/**
	 * @return the form_ds_4
	 */
	public Form_DS_4 getForm_ds_4() {
		if (this.form_ds_4 == null)
			this.form_ds_4 = (Form_DS_4) mainBean.getEvent().getForms().get(Form_DS_4.class.getSimpleName());
		return form_ds_4;
	}

	/**
	 * @return the form_ds_5
	 */
	public Form_DS_5 getForm_ds_5() {
		if (this.form_ds_5 == null)
			this.form_ds_5 = (Form_DS_5) mainBean.getEvent().getForms().get(Form_DS_5.class.getSimpleName());
		return form_ds_5;
	}

	/**
	 * @return the form_ds_6
	 */
	public Form_DS_6 getForm_ds_6() {
		// we also need form_ds_4 and form_ds_7 here
		if (this.form_ds_4 == null)
			this.form_ds_4 = (Form_DS_4) mainBean.getEvent().getForms().get(Form_DS_4.class.getSimpleName());
		if (this.form_ds_7 == null)
			this.form_ds_7 = (Form_DS_7) mainBean.getEvent().getForms().get(Form_DS_7.class.getSimpleName());
		
		if (this.form_ds_6 == null)
			this.form_ds_6 = (Form_DS_6) mainBean.getEvent().getForms().get(Form_DS_6.class.getSimpleName());
		return form_ds_6;
	}

	/**
	 * @return the form_ds_7
	 */
	public Form_DS_7 getForm_ds_7() {
		if (this.form_ds_7 == null)
			this.form_ds_7 = (Form_DS_7) mainBean.getEvent().getForms().get(Form_DS_7.class.getSimpleName());
		return form_ds_7;
	}

	/**
	 * @return the form_sem_1
	 */
	public Form_SEM_1 getForm_sem_1() {
		if (this.form_sem_1 == null)
			this.form_sem_1 = (Form_SEM_1) mainBean.getEvent().getForms().get(Form_SEM_1.class.getSimpleName());
		return form_sem_1;
	}

	/**
	 * @return the form_sem_2
	 */
	public Form_SEM_2 getForm_sem_2() {
		if (this.form_sem_2 == null)
			this.form_sem_2 = (Form_SEM_2) mainBean.getEvent().getForms().get(Form_SEM_2.class.getSimpleName());
		return form_sem_2;
	}

	/**
	 * @return the form_sem_3
	 */
	public Form_SEM_3 getForm_sem_3() {
		if (this.form_sem_3 == null)
			this.form_sem_3 = (Form_SEM_3) mainBean.getEvent().getForms().get(Form_SEM_3.class.getSimpleName());
		return form_sem_3;
	}

	/**
	 * @return the form_sem_4
	 */
	public Form_SEM_4 getForm_sem_4() {
		if (this.form_sem_1 == null)
			this.form_sem_1 = (Form_SEM_1) mainBean.getEvent().getForms().get(Form_SEM_1.class.getSimpleName());
		
		if (this.form_sem_4 == null)
			this.form_sem_4 = (Form_SEM_4) mainBean.getEvent().getForms().get(Form_SEM_4.class.getSimpleName());
		
		for (AttendanceList alist: this.form_sem_4.getSpeakers()) {
			for (Participant p: alist.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
		}
		for (AttendanceList alist: this.form_sem_4.getOfficials()) {
			for (Participant p: alist.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
		}
		for (AttendanceList alist: this.form_sem_4.getParticipants()) {
			for (Participant p: alist.getParticipants()) {
				p.loadArrivalTimes(this.form_sem_1.getDates());
				p.loadDepartureTimes(this.form_sem_1.getDates());
			}
		}
		return form_sem_4;
	}

	/**
	 * @return the form_sem_5
	 */
	public Form_SEM_5 getForm_sem_5() {
		if (this.form_sem_5 == null)
			this.form_sem_5 = (Form_SEM_5) mainBean.getEvent().getForms().get(Form_SEM_5.class.getSimpleName());
		return form_sem_5;
	}

	/**
	 * @return the speakerDataTable
	 */
	public HtmlDataTable getSpeakerDataTable() {
		return speakerDataTable;
	}

	/**
	 * @param speakerDataTable the speakerDataTable to set
	 */
	public void setSpeakerDataTable(HtmlDataTable speakerDataTable) {
		this.speakerDataTable = speakerDataTable;
	}

	/**
	 * @return the leafletDataTable
	 */
	public HtmlDataTable getLeafletDataTable() {
		return leafletDataTable;
	}

	/**
	 * @param leafletDataTable the leafletDataTable to set
	 */
	public void setLeafletDataTable(HtmlDataTable leafletDataTable) {
		this.leafletDataTable = leafletDataTable;
	}

	/**
	 * @return the electiveDataTable
	 */
	public HtmlDataTable getElectiveDataTable() {
		return electiveDataTable;
	}

	/**
	 * @param electiveDataTable the electiveDataTable to set
	 */
	public void setElectiveDataTable(HtmlDataTable electiveDataTable) {
		this.electiveDataTable = electiveDataTable;
	}

	/**
	 * @return the electiveTypeSelectItems
	 */
	public List<SelectItem> getElectiveTypeSelectItems() {
		return electiveTypeSelectItems;
	}

	/**
	 * @param electiveTypeSelectItems the electiveTypeSelectItems to set
	 */
	public void setElectiveTypeSelectItems(List<SelectItem> electiveTypeSelectItems) {
		this.electiveTypeSelectItems = electiveTypeSelectItems;
	}

	/**
	 * @return the invitationDate
	 */
	public Date getInvitationDate() {
		return invitationDate;
	}

	/**
	 * @param invitationDate the invitationDate to set
	 */
	public void setInvitationDate(Date invitationDate) {
		this.invitationDate = invitationDate;
	}

	/**
	 * @return the participantTypeSelectItems
	 */
	public List<SelectItem> getParticipantTypeSelectItems() {
		return participantTypeSelectItems;
	}

	/**
	 * @param participantTypeSelectItems the participantTypeSelectItems to set
	 */
	public void setParticipantTypeSelectItems(
			List<SelectItem> participantTypeSelectItems) {
		this.participantTypeSelectItems = participantTypeSelectItems;
	}

	/**
	 * @return the allElectives
	 */
	public List<Participant> getAllElectives() {
		return allElectives;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return the kbean
	 */
	public KatartisisBeanLocal getKbean() {
		return kbean;
	}

	/**
	 * @return the ubean
	 */
	public UserManagementBeanLocal getUbean() {
		return ubean;
	}

	/**
	 * @return the mainBean
	 */
	public KatartisisUIBean getMainBean() {
		return mainBean;
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
	 * @return the participantDataTable
	 */
	public HtmlDataTable getParticipantDataTable() {
		return participantDataTable;
	}

	/**
	 * @param participantDataTable the participantDataTable to set
	 */
	public void setParticipantDataTable(HtmlDataTable participantDataTable) {
		this.participantDataTable = participantDataTable;
	}

	/**
	 * @return the officialDataTable
	 */
	public HtmlDataTable getOfficialsDataTable() {
		return officialsDataTable;
	}

	/**
	 * @param officialDataTable the officialDataTable to set
	 */
	public void setOfficialsDataTable(HtmlDataTable officialsDataTable) {
		this.officialsDataTable = officialsDataTable;
	}

	/**
	 * @return the coursesDataTable
	 */
	public HtmlDataTable getCoursesDataTable() {
		return coursesDataTable;
	}

	/**
	 * @param coursesDataTable the coursesDataTable to set
	 */
	public void setCoursesDataTable(HtmlDataTable coursesDataTable) {
		this.coursesDataTable = coursesDataTable;
	}

	/**
	 * @return the participants_elective_size
	 */
	public int getParticipants_elective_size() {
		if (this.form_ho_4 == null) {
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
		}
		return this.form_ho_4.getElectives().size();
	}

	/**
	 * @return the participants_participant_size
	 */
	public int getParticipants_participant_size() {
		if (this.form_ho_4 == null) {
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
		}
		return this.form_ho_4.getParticipants().size();
	}

	/**
	 * @return the participants_other_size
	 */
	public int getParticipants_other_size() {
		if (this.form_ho_4 == null) {
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
		}
		return this.form_ho_4.getSpeakers().size() + this.form_ho_4.getOfficials().size();
	}

	/**
	 * @return the participants_total_size
	 */
	public int getParticipants_total_size() {
		if (this.form_ho_4 == null) {
			this.form_ho_4 = (Form_HO_4) mainBean.getEvent().getForms().get(Form_HO_4.class.getSimpleName());
		}
		return	this.form_ho_4.getElectives().size()
				+ this.form_ho_4.getSpeakers().size()
				+ this.form_ho_4.getOfficials().size()
				+ this.form_ho_4.getParticipants().size();
	}

	/**
	 * @return the implPrefecturesSelectItems
	 */
	public List<SelectItem> getImplPrefecturesSelectItems() {
		return implPrefecturesSelectItems;
	}

	/**
	 * @param implPrefecturesSelectItems the implPrefecturesSelectItems to set
	 */
	public void setImplPrefecturesSelectItems(
			List<SelectItem> implPrefecturesSelectItems) {
		this.implPrefecturesSelectItems = implPrefecturesSelectItems;
	}

	/**
	 * @return the implMunicipalitiesSelectItems
	 */
	public List<SelectItem> getImplMunicipalitiesSelectItems() {
		return implMunicipalitiesSelectItems;
	}

	/**
	 * @param implMunicipalitiesSelectItems the implMunicipalitiesSelectItems to set
	 */
	public void setImplMunicipalitiesSelectItems(
			List<SelectItem> implMunicipalitiesSelectItems) {
		this.implMunicipalitiesSelectItems = implMunicipalitiesSelectItems;
	}

	/**
	 * @return the selectMunicipality2_id
	 */
	public String getSelectMunicipality2_id() {
		return selectMunicipality2_id;
	}

	/**
	 * @param selectMunicipality2_id the selectMunicipality2_id to set
	 */
	public void setSelectMunicipality2_id(String selectMunicipality2_id) {
		this.selectMunicipality2_id = selectMunicipality2_id;
	}

	/**
	 * @return the filterUserName
	 */
	public String getFilterUserName() {
		return filterUserName;
	}

	/**
	 * @param filterUserName the filterUserName to set
	 */
	public void setFilterUserName(String filterUserName) {
		this.filterUserName = filterUserName;
	}

	/**
	 * @return the filterEKEPIS
	 */
	public String getFilterEKEPIS() {
		return filterEKEPIS;
	}

	/**
	 * @param filterEKEPIS the filterEKEPIS to set
	 */
	public void setFilterEKEPIS(String filterEKEPIS) {
		this.filterEKEPIS = filterEKEPIS;
	}

	/**
	 * @return the filterFirstName
	 */
	public String getFilterFirstName() {
		return filterFirstName;
	}

	/**
	 * @param filterFirstName the filterFirstName to set
	 */
	public void setFilterFirstName(String filterFirstName) {
		this.filterFirstName = filterFirstName;
	}

	/**
	 * @return the filterLastName
	 */
	public String getFilterLastName() {
		return filterLastName;
	}

	/**
	 * @param filterLastName the filterLastName to set
	 */
	public void setFilterLastName(String filterLastName) {
		this.filterLastName = filterLastName;
	}

	/**
	 * @return the speakerTypeSelectItems
	 */
	public List<SelectItem> getSpeakerTypeSelectItems() {
		return speakerTypeSelectItems;
	}

	/**
	 * @param speakerTypeSelectItems the speakerTypeSelectItems to set
	 */
	public void setSpeakerTypeSelectItems(List<SelectItem> speakerTypeSelectItems) {
		this.speakerTypeSelectItems = speakerTypeSelectItems;
	}

	/**
	 * @return the searchResultTrainers
	 */
	public List<Participant> getSearchResultTrainers() {
		return searchResultTrainers;
	}

	/**
	 * @return the searchResultElectives
	 */
	public List<Participant> getSearchResultElectives() {
		return searchResultElectives;
	}

	/**
	 * @return the searchTrainersDataTable
	 */
	public HtmlDataTable getSearchTrainersDataTable() {
		return searchTrainersDataTable;
	}

	/**
	 * @return the searchElectivesDataTable
	 */
	public HtmlDataTable getSearchElectivesDataTable() {
		return searchElectivesDataTable;
	}

	/**
	 * @param searchResultTrainers the searchResultTrainers to set
	 */
	public void setSearchResultTrainers(List<Participant> searchResultTrainers) {
		this.searchResultTrainers = searchResultTrainers;
	}

	/**
	 * @param searchResultElectives the searchResultElectives to set
	 */
	public void setSearchResultElectives(List<Participant> searchResultElectives) {
		this.searchResultElectives = searchResultElectives;
	}

	/**
	 * @param searchTrainersDataTable the searchTrainersDataTable to set
	 */
	public void setSearchTrainersDataTable(HtmlDataTable searchTrainersDataTable) {
		this.searchTrainersDataTable = searchTrainersDataTable;
	}

	/**
	 * @param searchElectivesDataTable the searchElectivesDataTable to set
	 */
	public void setSearchElectivesDataTable(HtmlDataTable searchElectivesDataTable) {
		this.searchElectivesDataTable = searchElectivesDataTable;
	}

	/**
	 * @return the materialsDataTable
	 */
	public HtmlDataTable getMaterialsDataTable() {
		return materialsDataTable;
	}

	/**
	 * @param materialsDataTable the materialsDataTable to set
	 */
	public void setMaterialsDataTable(HtmlDataTable materialsDataTable) {
		this.materialsDataTable = materialsDataTable;
	}

	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * @return the currentSpeakersList
	 */
	public AttendanceList getCurrentSpeakersList() {
		return currentSpeakersList;
	}

	/**
	 * @param currentSpeakersList the currentSpeakersList to set
	 */
	public void setCurrentSpeakersList(AttendanceList currentSpeakersList) {
		this.currentSpeakersList = currentSpeakersList;
	}

	/**
	 * @return the currentOfficialsList
	 */
	public AttendanceList getCurrentOfficialsList() {
		return currentOfficialsList;
	}

	/**
	 * @param currentOfficialsList the currentOfficialsList to set
	 */
	public void setCurrentOfficialsList(AttendanceList currentOfficialsList) {
		this.currentOfficialsList = currentOfficialsList;
	}

	/**
	 * @return the currentParticipantsList
	 */
	public AttendanceList getCurrentParticipantsList() {
		return currentParticipantsList;
	}

	/**
	 * @param currentParticipantsList the currentParticipantsList to set
	 */
	public void setCurrentParticipantsList(AttendanceList currentParticipantsList) {
		this.currentParticipantsList = currentParticipantsList;
	}

	/**
	 * @return the reviewItemsDataTable
	 */
	public HtmlDataTable getReviewItemsDataTable() {
		return reviewItemsDataTable;
	}

	/**
	 * @param reviewItemsDataTable the reviewItemsDataTable to set
	 */
	public void setReviewItemsDataTable(HtmlDataTable reviewItemsDataTable) {
		this.reviewItemsDataTable = reviewItemsDataTable;
	}

	/**
	 * @return the printLeafletDataTable
	 */
	public HtmlDataTable getPrintLeafletDataTable() {
		return printLeafletDataTable;
	}

	/**
	 * @param printLeafletDataTable the printLeafletDataTable to set
	 */
	public void setPrintLeafletDataTable(HtmlDataTable printLeafletDataTable) {
		this.printLeafletDataTable = printLeafletDataTable;
	}

	/**
	 * @return the printMaterialDatatable
	 */
	public HtmlDataTable getPrintMaterialDatatable() {
		return printMaterialDatatable;
	}

	/**
	 * @param printMaterialDatatable the printMaterialDatatable to set
	 */
	public void setPrintMaterialDatatable(HtmlDataTable printMaterialDatatable) {
		this.printMaterialDatatable = printMaterialDatatable;
	}

	/**
	 * @return the printElectiveDatatable
	 */
	public HtmlDataTable getPrintElectiveDatatable() {
		return printElectiveDatatable;
	}

	/**
	 * @param printElectiveDatatable the printElectiveDatatable to set
	 */
	public void setPrintElectiveDatatable(HtmlDataTable printElectiveDatatable) {
		this.printElectiveDatatable = printElectiveDatatable;
	}

	/**
	 * @return the printSpeakerDatatable
	 */
	public HtmlDataTable getPrintSpeakerDatatable() {
		return printSpeakerDatatable;
	}

	/**
	 * @param printSpeakerDatatable the printSpeakerDatatable to set
	 */
	public void setPrintSpeakerDatatable(HtmlDataTable printSpeakerDatatable) {
		this.printSpeakerDatatable = printSpeakerDatatable;
	}
}
