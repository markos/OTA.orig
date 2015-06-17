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

/* $Id: ReportBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.reports;

import gr.codex.katartisis.CycleType;
import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.katartisis.ParticipantType;
import gr.codex.katartisis.entitybeans.AttendanceList;
import gr.codex.katartisis.entitybeans.Cycle;
import gr.codex.katartisis.entitybeans.KatartisisEvent;
import gr.codex.katartisis.entitybeans.Participant;
import gr.codex.katartisis.entitybeans.forms.Form_DS_4;
import gr.codex.katartisis.entitybeans.forms.Form_DS_6;
import gr.codex.katartisis.entitybeans.forms.Form_DS_7;
import gr.codex.katartisis.entitybeans.forms.Form_HO_4;
import gr.codex.katartisis.entitybeans.forms.Form_SEM_4;
import gr.codex.ota.OTABean;
import gr.codex.ota.entitybeans.PhysicalObject;
import gr.codex.ota.entitybeans.Section_L;
import gr.codex.ota.entitybeans.Section_M;
import gr.codex.ota.helpers.NOM_Contractor;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.Contractor;
import gr.codex.usermgmt.entitybeans.Municipality;
import gr.codex.usermgmt.entitybeans.Prefecture;
import gr.codex.usermgmt.entitybeans.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReportBean {
	// private vars

	private List<PhysicalObject> physical_objects = new ArrayList<PhysicalObject>();
	private List<SelectItem> contractorSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> regionSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> prefectureSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> municipalitySelectItems = new ArrayList<SelectItem>();

	private ParticipantType needParticipantType;
	private Integer contractor_id = -1;
	private Integer region_id = -1;
	private String prefecture_id = "-1";
	private String municipality_id = "-1";
	
	private int events_total;
	private int events_total_actual;
	private int electives_total;
	private int participants_total;
	private double total_percentage;
	private int mayors_total;
	private int councilmembers_total;
	private int speakers_total;
	
	private List<StatsEvent> statsList = new ArrayList<StatsEvent>();
	private List<RegionKey> regionKeysList = new ArrayList<RegionKey>();
	private Map<RegionKey, RegionStats> regionStatsMap = new HashMap<RegionKey, RegionStats>();
	private HashMap<Integer, Integer> subProjectGivenIds = new HashMap<Integer, Integer>();
	
	Context ctx;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;
	OTABean otaBean;
	
	private String sectionClass;
	private int localcouncilmembers_total;
	private int men_total;
	private int women_total;
	private int men_speakers_total;
	private int women_speakers_total;
	
	List<Participant> participants = new ArrayList<Participant>();
	private HtmlDataTable participantDataTable;
	
	public ReportBean() {
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
		
		populateRegionSelectItems(true);
	}
	
	public void clearAllDataTables() {
		this.participantDataTable = new HtmlDataTable();
	}
	
	public void changeRegion(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			populatePrefectureSelectItems();
		}	
	}
	
	public String loadReportPhysical() {
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		this.physical_objects = (List<PhysicalObject>) kbean.getRecordsByQuery("num=" + this.sectionClass, PhysicalObject.class );
			
		this.subProjectGivenIds.clear();
		for (PhysicalObject po: this.physical_objects) {
			if (po.getSubprojectId() != null) {
				Section_L l = (Section_L) kbean.getRecordById(po.getSubprojectId(), Section_L.class);
				if (l != null && l.getId() != null)
					this.subProjectGivenIds.put(l.getId(), l.getGiven_id());
			}
		}

		return "loaded_reportPhysical";
	}
	
	private void populateContractorSelectItems( List<Section_L> subs ) {
		this.contractorSelectItems.clear();
		for( Section_L sub : subs ) {
			System.out.println("subproject: " + sub.getGiven_id() + ", " + sub.getTitle());
			if (sub.isKatartisis() == true) {
				System.out.println("subproject is katartisis project");
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					System.out.println("subproject has a legal binding attached");
					for (NOM_Contractor nc: sec_m.getContractors()) {
						System.out.println("Contractors:");
						Contractor c = (Contractor) kbean.getRecordById(nc.getContractor().getId(), Contractor.class );
						if (c != null) {
							System.out.println("contractor id: " + nc.getContractor().getId() +", " + nc.getContractor().getName() + ", for " + nc.getAmount());
							SelectItem s = new SelectItem( c.getId(), c.getName() );
							this.contractorSelectItems.add(s);
						}
					}
				}
			}
		}
	}
	
	public void katartisisContractors_changeContractor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisContractors();
		}	
	}
	
	public String loadReportKatartisisContractors() {
		
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		repopulateKatartisisContractors();
		
		return "loaded_katartisisContractors";
	}

	public void repopulateKatartisisContractors() {
		this.electives_total = 0;
		this.participants_total = 0;
		
		populateContractorSelectItems(otaBean.getSubProjects());
		this.statsList.clear();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					for (NOM_Contractor nc: sec_m.getContractors()) {
						if (this.contractor_id.equals(nc.getContractor().getId()) ||
								(this.contractor_id.equals(-1) )) {
							for (Cycle cycle: sub.getKatartisis_Cycles()) {
								if (cycle.getType().equals(CycleType.CYCLE_IMERIDA) == true) {
									for (KatartisisEvent event : cycle.getEvents()) {
										StatsEvent cstat = new StatsEvent();
										// we have to do this, because for some reason it doesn't load the map objects otherwise
										event = (KatartisisEvent) kbean.getRecordById(event.getId(), KatartisisEvent.class);
										cstat.setContractor(nc.getContractor());
										cstat.setEvent(event);
										
										int estimate = event.getEstimate();
										int actual = event.getElectivesActual();
										this.electives_total += estimate;
										this.participants_total += actual;
										statsList.add(cstat);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void katartisisPhysical_changeContractor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisPhysical();
		}	
	}
	
	public String loadReportKatartisisPhysical() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		repopulateKatartisisPhysical();
		
		return "loaded_katartisisPhysical";
	}
	
	public void repopulateKatartisisPhysical() {
		populateContractorSelectItems(otaBean.getSubProjects());
		this.regionStatsMap.clear();
		this.regionKeysList.clear();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					for (NOM_Contractor nc : sec_m.getContractors()) {
						if (this.contractor_id.equals(nc.getContractor().getId())
								|| (this.contractor_id.equals(-1))) {
							for (Cycle cycle : sub.getKatartisis_Cycles()) {
								if (cycle.getType().equals(
										CycleType.CYCLE_IMERIDA) == true) {
									for (KatartisisEvent event : cycle.getEvents()) {
										RegionKey rkey = new RegionKey(nc.getContractor().getName());
										rkey.setRegion(event.getMunicipality().getRegion().getName());
										rkey.setPrefecture(event.getMunicipality().getPrefecture().getName());

										System.out.println("got here, #1");
										RegionStats rstats;
										if (this.regionStatsMap.containsKey(rkey) == true) {
											System.out.println("already there, adding");
											rstats = regionStatsMap.get(rkey);
											rstats.incrEvents_total();
											if (event.isCompleted() == true)
												rstats.incrEvents_total_actual();
											rstats.addToElectives_total(event.getEstimate());
											rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
											rstats.addToMayors_total(event.getMayors_total());
											rstats.addToParticipants_total(event.getParticipants_total_size());
											rstats.addToSpeakers_total(event.getParticipants_speakers_size());
										} else {
											System.out.println("not there, new key");
											rstats = new RegionStats();
											rstats.setEvents_total(1);
											if (event.isCompleted() == true)
												rstats.setEvents_total_actual(1);
											rstats.addToElectives_total(event.getEstimate());
											rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
											rstats.addToMayors_total(event.getMayors_total());
											rstats.addToParticipants_total(event.getParticipants_total_size());
											rstats.addToSpeakers_total(event.getParticipants_speakers_size());
											regionStatsMap.put(rkey, rstats);
											regionKeysList.add(rkey);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void imerides_changeRegion(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisRegion();
		}	
	}
	
	public String loadReportKatartisisRegion() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		this.region_id = -1;
		
		repopulateKatartisisRegion();
		
		return "loaded_katartisisRegion";
	}
	
	public void repopulateKatartisisRegion() {
		Region region = null;
		String contractor = "";
		events_total = 0;
		electives_total = 0;
		participants_total = 0;;
		mayors_total = 0;
		councilmembers_total = 0;
		speakers_total = 0;
		total_percentage = 0.0;
		
		populateRegionSelectItems(true);
		this.regionStatsMap.clear();
		this.regionKeysList.clear();

		// now get the subprojects that are of type Katartisis - IF CONTRACTOR HAS BEEN GIVEN THEN ADD THAT
		if( this.region_id != null && !this.region_id.equals(-1) ) {
			region = (Region) kbean.getRecordById(this.region_id, Region.class);
		}
		if (region == null)
			region = new Region();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					if (sec_m.getContractors().size() > 0) {
						contractor = sec_m.getContractors().get(0).getContractor().getName();
					}
				}
				for (Cycle cycle: sub.getKatartisis_Cycles()) {
					if (cycle.getType().equals(CycleType.CYCLE_IMERIDA) == true) {
						for (KatartisisEvent event : cycle.getEvents()) {
							if (this.region_id.equals(-1) || 
									(region.getId() != null && event.getMunicipality().getRegion().getId().compareTo(region.getId()) == 0)) {
								System.out.println("event.region= " + event.getMunicipality().getRegion().getId() + ", region = " + region.getId());
								System.out.println("Here here! event.place: " + event.getPlace() + ", date: "+ event.getDate());
								RegionKey rkey = new RegionKey(contractor);
								rkey.setRegion(event.getMunicipality().getRegion().getName());
								rkey.setPrefecture(event.getMunicipality().getPrefecture().getName());

								System.out.println("got here, #1");
								RegionStats rstats;
								if (regionStatsMap.containsKey(rkey) == true) {
									System.out.println("already there, adding");
									rstats = regionStatsMap.get(rkey);
									rstats.incrEvents_total();
									rstats.incrEvents_total_actual();
									rstats.addToElectives_total(event.getEstimate());
									rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
									rstats.addToMayors_total(event.getMayors_total());
									rstats.addToParticipants_total(event.getParticipants_total_size());
									rstats.addToSpeakers_total(event.getParticipants_speakers_size());
								} else {
									System.out.println("not there, new key");
									rstats = new RegionStats();
									rstats.setEvents_total(1);
									if (event.isCompleted() == true)
										rstats.setEvents_total_actual(1);
									rstats.addToElectives_total(event.getEstimate());
									rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
									rstats.addToMayors_total(event.getMayors_total());
									rstats.addToParticipants_total(event.getParticipants_total_size());
									rstats.addToSpeakers_total(event.getParticipants_speakers_size());
									regionStatsMap.put(rkey, rstats);
									regionKeysList.add(rkey);
								}
								events_total++;
								electives_total += event.getEstimate();
								participants_total += event.getParticipants_total_size();
								total_percentage = (double)(participants_total)/(double)(electives_total);
								mayors_total += event.getMayors_total();
								councilmembers_total += event.getCouncilmembers_total();
								speakers_total += event.getParticipants_speakers_size();
							}
						}
					}
				}
			}
		}
	}

	public void imerides_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisPrefecture();
		}	
	}
	
	public String loadReportKatartisisPrefecture() {
		
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		populateRegionSelectItems(false);
		repopulateKatartisisPrefecture();

		return "loaded_katartisisPrefecture";
	}

	public void repopulateKatartisisPrefecture() {
		
		electives_total = 0;
		participants_total = 0;
		mayors_total = 0;
		councilmembers_total = 0;
		total_percentage = 0.0;
		
		populatePrefectureSelectItems();
		Prefecture prefecture = ubean.getPrefectureById(this.prefecture_id);
		if (prefecture == null)
			prefecture = new Prefecture();
		this.statsList.clear();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				for (Cycle cycle: sub.getKatartisis_Cycles()) {
					if (cycle.getType().equals(CycleType.CYCLE_IMERIDA) == true) {
						Contractor contractor = (Contractor) kbean.getRecordById(sub.getContractorId(), Contractor.class);
						if (contractor == null)
							contractor = new Contractor();

						for (KatartisisEvent event : cycle.getEvents()) {
							System.out.println("event.prefecture = " + event.getMunicipality().getPrefecture().getId() + ", prefecture = " + prefecture.getId());
							if (event.getMunicipality().getPrefecture().equals(prefecture) == true) {
								System.out.println("Here here! event.place: " + event.getPlace() + ", date: "+ event.getDate());
								StatsEvent cstat = new StatsEvent();

								cstat.setContractor(contractor);
								cstat.setEvent(event);
								int estimate = event.getEstimate();
								int actual = event.getElectivesActual();
								this.electives_total += estimate;
								this.participants_total += actual;
								statsList.add(cstat);
							}
						}
					}
				}
			}
		}
		if (electives_total > 0)
			total_percentage = (double)(participants_total)/(double)(electives_total);
		else
			total_percentage = 0.0;
	}

	public String loadReportKatartisisTotalImerides() {
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		events_total = 0;
		electives_total = 0;
		participants_total = 0;;
		mayors_total = 0;
		councilmembers_total = 0;
		speakers_total = 0;
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				for (Cycle cycle: sub.getKatartisis_Cycles()) {
					if (cycle.getType() == CycleType.CYCLE_IMERIDA) {						
						for (KatartisisEvent event : cycle.getEvents()) {							
							events_total++;
							electives_total += event.getEstimate();
							participants_total += event.getParticipants_total_size();
							mayors_total += event.getMayors_total();
							councilmembers_total += event.getCouncilmembers_total();
							speakers_total += event.getParticipants_speakers_size();
						}
					}
				}
			}
		}
		total_percentage = (double)(participants_total)/(double)(electives_total);
			
		return "loaded_katartisisTotalImerides";
	}

	public String loadReportKatartisisDSContractors() {
		
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		populateContractorSelectItems(otaBean.getSubProjects());
		repopulateKatartisisDSContractors();
		
		return "loaded_katartisisDSContractors";
	}

	public void repopulateKatartisisDSContractors() {
		electives_total = 0;
		participants_total = 0;;
		this.statsList.clear();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					for (NOM_Contractor nc: sec_m.getContractors()) {
						if (this.contractor_id.equals(nc.getContractor().getId()) ||
								(this.contractor_id.equals(-1) )) {
							for (Cycle cycle: sub.getKatartisis_Cycles()) {
								if (cycle.getType().equals(CycleType.CYCLE_DS) == true) {
									for (KatartisisEvent event : cycle.getEvents()) {
										StatsEvent cstat = new StatsEvent();
										cstat.setContractor(nc.getContractor());
										cstat.setEvent(event);
										
										int estimate = event.getEstimate();
										int actual = event.getElectivesActual();
										this.electives_total += estimate;
										this.participants_total += actual;
										statsList.add(cstat);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void katartisisDSPhysical_changeContractor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisDSPhysical();
		}	
	}
	
	public String loadReportKatartisisDSPhysical() {
		
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		repopulateKatartisisDSPhysical();
		populateContractorSelectItems(otaBean.getSubProjects());
		
		return "loaded_katartisisDSPhysical";
	}

	public void repopulateKatartisisDSPhysical() {
		events_total = 0;
		events_total_actual = 0;
		electives_total = 0;
		participants_total = 0;;
		total_percentage = 0.0;
		
		populateRegionSelectItems(true);
		this.regionStatsMap.clear();
		this.regionKeysList.clear();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					for (NOM_Contractor nc: sec_m.getContractors()) {
						if (this.contractor_id.equals(nc.getContractor().getId()) ||
								(this.contractor_id.equals(-1) )) {
							for (Cycle cycle: sub.getKatartisis_Cycles()) {
								if (cycle.getType().equals(CycleType.CYCLE_DS) == true) {
									for (KatartisisEvent event : cycle.getEvents()) {
										System.out.println("Here here! event.place: " + event.getPlace() + ", date: "+ event.getDate());
										RegionKey rkey = new RegionKey(nc.getContractor().getName());
										rkey.setRegion(event.getMunicipality().getRegion().getName());
										rkey.setPrefecture(event.getMunicipality().getPrefecture().getName());

										System.out.println("got here, #1");
										RegionStats rstats;
										if (regionStatsMap.containsKey(rkey) == true) {
											System.out.println("already there, adding");
											rstats = regionStatsMap.get(rkey);
											rstats.incrEvents_total();
											if (event.isCompleted() == true)
												rstats.incrEvents_total_actual();
											rstats.addToElectives_total(event.getEstimate());
											rstats.addToParticipants_total(event.getElectivesActual());
										} else {
											System.out.println("not there, new key");
											rstats = new RegionStats();
											rstats.setEvents_total(1);
											if (event.isCompleted() == true)
												rstats.setEvents_total_actual(1);
											rstats.addToElectives_total(event.getEstimate());
											rstats.addToParticipants_total(event.getElectivesActual());
											regionStatsMap.put(rkey, rstats);
											regionKeysList.add(rkey);
										}
										events_total++;
										if (event.isCompleted() == true)
											events_total_actual++;
										electives_total += event.getEstimate();
										participants_total += event.getElectivesActual();
										total_percentage = (double)(participants_total/electives_total);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public String loadReportKatartisisDSPrefecture() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		this.region_id = -1;
		
		populateRegionSelectItems(true);
		repopulateKatartisisDSPrefecture();
		
		return "loaded_katartisisDSPrefecture";
	}
	
	public void ds_changeRegion(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			populatePrefectureSelectItems();
			this.prefecture_id = "-1";
			this.municipality_id = "-1";
			repopulateKatartisisDSPrefecture();
		}	
	}
	
	public void ds_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			populateMunicipalitySelectItems();
			this.municipality_id = "-1";
			repopulateKatartisisDSPrefecture();
		}	
	}
	
	//public void ds_changeMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
	/*	PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateKatartisisDSPrefecture();
		}	
	}*/
	
	public void repopulateKatartisisDSPrefecture() {
		Region region = null;
		Prefecture prefecture = null;
		Municipality municipality = null;
		RegionKey rkey = new RegionKey();
		String contractor = "";
		events_total = 0;
		electives_total = 0;
		participants_total = 0;;
		mayors_total = 0;
		councilmembers_total = 0;
		speakers_total = 0;
		total_percentage = 0.0;
		
		this.regionStatsMap.clear();
		this.regionKeysList.clear();

		// now get the subprojects that are of type Katartisis - IF CONTRACTOR HAS BEEN GIVEN THEN ADD THAT
		if( this.region_id != null && !this.region_id.equals(-1) ) {
			region = ubean.getRegionById(this.region_id);
		}
		if (region == null)
			region = new Region();
		
		if( this.prefecture_id != null && !this.prefecture_id.equals("-1") ) {
			prefecture = ubean.getPrefectureById(this.prefecture_id);
		}
		if (prefecture == null)
			prefecture = new Prefecture();
		
		if( this.municipality_id != null && !this.municipality_id.equals("-1") ) {
			municipality = ubean.getMunicipalityById(this.municipality_id);
		}
		if (municipality == null)
			municipality = new Municipality();
		
		for (Section_L sub : otaBean.getSubProjects()) {
			if (sub.isKatartisis() == true) {
				Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
				if (sec_m != null) {
					if (sec_m.getContractors().size() > 0) {
						contractor = sec_m.getContractors().get(0).getContractor().getName();
					}
				}
				for (Cycle cycle: sub.getKatartisis_Cycles()) {
					if (cycle.getType().equals(CycleType.CYCLE_DS) == true) {
						for (KatartisisEvent event : cycle.getEvents()) {
							rkey = new RegionKey(contractor);
							if (this.region_id.equals(-1) || 
									(region.getId() != null && event.getMunicipality().getRegion().getId().equals(region.getId()) == true)) {
								rkey.setRegion(event.getMunicipality().getRegion().getName());
							}
							if (this.prefecture_id.equals("-1") || 
									(prefecture.getId() != null && event.getMunicipality().getPrefecture().getId().equals(prefecture.getId()) == true)) {
								rkey.setPrefecture(event.getMunicipality().getPrefecture().getName());
							}
							if (this.municipality_id.equals("-1") || 
								(municipality.getId() != null && event.getMunicipality().getId().equals(municipality.getId()) == true)) {
								rkey.setMunicipality(event.getMunicipality().getName());
							}

							System.out.println("got here, #1");
							RegionStats rstats;
							if (regionStatsMap.containsKey(rkey) == true) {
								System.out.println("already there, adding");
								rstats = regionStatsMap.get(rkey);
								rstats.incrEvents_total();
								if (event.isCompleted() == true)
									rstats.incrEvents_total_actual();
								rstats.addToElectives_total(event.getEstimate());
								rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
								rstats.addToMayors_total(event.getMayors_total());
								rstats.addToParticipants_total(event.getParticipants_total_size());
								rstats.addToSpeakers_total(event.getParticipants_speakers_size());
							} else {
								System.out.println("not there, new key");
								rstats = new RegionStats();
								rstats.setEvents_total(1);
								if (event.isCompleted() == true)
									rstats.setEvents_total_actual(1);
								rstats.addToElectives_total(event.getEstimate());
								rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
								rstats.addToMayors_total(event.getMayors_total());
								rstats.addToParticipants_total(event.getParticipants_total_size());
								rstats.addToSpeakers_total(event.getParticipants_speakers_size());
								regionStatsMap.put(rkey, rstats);
								regionKeysList.add(rkey);
							}
							events_total++;
							electives_total += event.getEstimate();
							participants_total += event.getParticipants_total_size();
							mayors_total += event.getMayors_total();
							councilmembers_total += event.getCouncilmembers_total();
							speakers_total += event.getParticipants_speakers_size();
						}
					}
				}
				total_percentage = (double)(participants_total/electives_total);
			}
		}
	}
	
	public String loadReportKatartisisDSTotal() {
		otaBean.goToTDE();
		otaBean.goToSectionB();
		
		events_total = 0;
		electives_total = 0;
		participants_total = 0;;
		mayors_total = 0;
		councilmembers_total = 0;
		localcouncilmembers_total = 0;
		men_total = 0;
		women_total = 0;
		speakers_total = 0;
		men_speakers_total = 0;
		women_speakers_total = 0;
		
		if (otaBean.getSubProjects() != null) {
			for (Section_L sub : otaBean.getSubProjects()) {
				if (sub.isKatartisis() == true) {
					for (Cycle cycle: sub.getKatartisis_Cycles()) {
						if (cycle.getType() == CycleType.CYCLE_IMERIDA) {						
							for (KatartisisEvent event : cycle.getEvents()) {							
								events_total++;
								electives_total += event.getEstimate();
								participants_total += event.getParticipants_total_size();
								total_percentage = (double)participants_total/electives_total;
								mayors_total += event.getMayors_total();
								councilmembers_total += event.getCouncilmembers_total();
								localcouncilmembers_total += event.getLocalCouncilmembers_total();
								speakers_total += event.getParticipants_speakers_size();
								men_total += event.getMen_total();
								women_total = participants_total - men_total;
								men_speakers_total = event.getParticipants_speakers_size();
								women_speakers_total = speakers_total - men_speakers_total;
							}
						}
					}
				}
			}
		}
		return "loaded_katartisisDSTotal";
	}

	private void populateRegionSelectItems(boolean includeAll) {
		List<Region> regList = (List<Region>) ubean.getAllRegions();
		this.regionSelectItems.clear();
			
		for( Region reg : regList ) {
			if (includeAll == false && reg.getId().equals(-1) == true)
				continue;
			SelectItem s = new SelectItem( reg.getId(), reg.getName() );
			this.regionSelectItems.add(s);
		}
	}
	
	private void populatePrefectureSelectItems() {

		Region r = ubean.getRegionById(this.region_id);
		List<Prefecture> prefList = r.getPrefectures();
		this.prefectureSelectItems.clear();

		for( Prefecture pref : prefList ) { 
			SelectItem s = new SelectItem( pref.getId(), pref.getName() );
			this.prefectureSelectItems.add(s);
		}
	}
	
	private void populateMunicipalitySelectItems() {
		Prefecture p = ubean.getPrefectureById(this.prefecture_id);
		List<Municipality> munList = p.getMunicipalities();
		this.municipalitySelectItems.clear();

		for( Municipality mun : munList ) { 
			SelectItem s = new SelectItem( mun.getId(), mun.getName() );
			this.municipalitySelectItems.add(s);
		}
	}

	public String loadReportKatartisisSemRegion() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		this.region_id = -1;
		
		repopulateKatartisisSemRegion();
		
		return "loaded_katartisisDSRegion";
	}
	
	public void repopulateKatartisisSemRegion() {
		Region region = null;
		String contractor = "";
		events_total = 0;
		electives_total = 0;
		participants_total = 0;;
		mayors_total = 0;
		councilmembers_total = 0;
		speakers_total = 0;
		total_percentage = 0.0;
		
		populateRegionSelectItems(true);
		this.regionStatsMap.clear();
		this.regionKeysList.clear();

		// now get the subprojects that are of type Katartisis - IF CONTRACTOR HAS BEEN GIVEN THEN ADD THAT
		if( this.region_id != null && !this.region_id.equals(-1) ) {
			region = (Region) kbean.getRecordById(this.region_id, Region.class);
		}
		if (region == null)
			region = new Region();
		
		if (otaBean.getSubProjects() != null) {
			for (Section_L sub : otaBean.getSubProjects()) {
				if (sub.isKatartisis() == true) {
					Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(sub);
					if (sec_m != null) {
						if (sec_m.getContractors().size() > 0) {
							contractor = sec_m.getContractors().get(0).getContractor().getName();
						}
					}
					for (Cycle cycle: sub.getKatartisis_Cycles()) {
						if (cycle.getType().equals(CycleType.CYCLE_SEMINAR) == true) {
							for (KatartisisEvent event : cycle.getEvents()) {
								if (this.region_id.equals(-1) || 
										(region.getId() != null && event.getMunicipality().getRegion().getId().compareTo(region.getId()) == 0)) {
									System.out.println("event.region= " + event.getMunicipality().getRegion().getId() + ", region = " + region.getId());
									System.out.println("Here here! event.place: " + event.getPlace() + ", date: "+ event.getDate());
									RegionKey rkey = new RegionKey(contractor);
									rkey.setRegion(event.getMunicipality().getRegion().getName());
									rkey.setPrefecture(event.getMunicipality().getPrefecture().getName());

									System.out.println("got here, #1");
									RegionStats rstats;
									if (regionStatsMap.containsKey(rkey) == true) {
										System.out.println("already there, adding");
										rstats = regionStatsMap.get(rkey);
										rstats.incrEvents_total();
										rstats.incrEvents_total_actual();
										rstats.addToElectives_total(event.getEstimate());
										rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
										rstats.addToMayors_total(event.getMayors_total());
										rstats.addToParticipants_total(event.getParticipants_total_size());
										rstats.addToSpeakers_total(event.getParticipants_speakers_size());
									} else {
										System.out.println("not there, new key");
										rstats = new RegionStats();
										rstats.setEvents_total(1);
										rstats.setEvents_total_actual(1);
										rstats.addToElectives_total(event.getEstimate());
										rstats.addToCouncilmembers_total(event.getCouncilmembers_total());
										rstats.addToMayors_total(event.getMayors_total());
										rstats.addToParticipants_total(event.getParticipants_total_size());
										rstats.addToSpeakers_total(event.getParticipants_speakers_size());
										regionStatsMap.put(rkey, rstats);
										regionKeysList.add(rkey);
									}
									events_total++;
									electives_total += event.getEstimate();
									participants_total += event.getParticipants_total_size();
									total_percentage = (double)(participants_total)/(double)(electives_total);
									mayors_total += event.getMayors_total();
									councilmembers_total += event.getCouncilmembers_total();
									speakers_total += event.getParticipants_speakers_size();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public String loadReportElectives() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		clearAllDataTables();
		
		this.region_id = -1;
		this.needParticipantType = ParticipantType.ELECTIVE;
		
		repopulateParticipantsArea();
		
		return "loaded_katartisisElectives";
	}
	
	public String loadReportSpeakers() {

		otaBean.goToTDE();
		otaBean.goToSectionB();
		clearAllDataTables();
		
		this.region_id = -1;
		this.needParticipantType = ParticipantType.KEY_SPEAKER;
		
		repopulateParticipantsArea();
		
		return "loaded_katartisisSpeakers";
	}
	
	public void participants_changeRegion(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			populatePrefectureSelectItems();
			this.prefecture_id = "-1";
			this.municipality_id = "-1";
			repopulateParticipantsArea();
		}	
	}
	
	public void participants_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			populateMunicipalitySelectItems();
			this.municipality_id = "-1";
			repopulateParticipantsArea();
		}	
	}
	
	public void participants_changeMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			repopulateParticipantsArea();
		}	
	}
	
	public void repopulateParticipantsArea() {
		Region region = null;
		Prefecture prefecture = null;
		Municipality municipality = null;
		
		participants.clear();
		populateRegionSelectItems(true);

		// now get the subprojects that are of type Katartisis - IF CONTRACTOR HAS BEEN GIVEN THEN ADD THAT
		if( this.region_id != null && !this.region_id.equals(-1) ) {
			region = ubean.getRegionById(this.region_id);
		}
		if (region == null)
			region = new Region();
		
		if( this.prefecture_id != null && !this.prefecture_id.equals("-1") ) {
			prefecture = ubean.getPrefectureById(this.prefecture_id);
		}
		if (prefecture == null)
			prefecture = new Prefecture();
		
		if( this.municipality_id != null && !this.municipality_id.equals("-1") ) {
			municipality = ubean.getMunicipalityById(this.municipality_id);
		}
		if (municipality == null)
			municipality = new Municipality();
		
		if (otaBean.getSubProjects() != null) {
			for (Section_L sub : otaBean.getSubProjects()) {
				if (sub.isKatartisis() == true) {
					for (Cycle cycle: sub.getKatartisis_Cycles()) {
						for (KatartisisEvent event : cycle.getEvents()) {
							if (this.region_id.equals(-1) || 
									(region.getId() != null && event.getMunicipality().getRegion().getId().equals(region.getId()) == true)) {
								if (this.prefecture_id.equals("-1") || 
										(prefecture.getId() != null && event.getMunicipality().getPrefecture().getId().equals(prefecture.getId()) == true)) {
									if (this.municipality_id.equals("-1") || 
											(municipality.getId() != null && event.getMunicipality().getId().equals(municipality.getId()) == true)) {
										if (cycle.getType().equals(CycleType.CYCLE_IMERIDA) == true) {
											Form_HO_4 form_ho_4 = (Form_HO_4) event.getForms().get(Form_HO_4.class.getSimpleName());
											if (this.needParticipantType == ParticipantType.ELECTIVE) {
												participants.addAll(form_ho_4.getElectives());
											} else if (this.needParticipantType == ParticipantType.SPEAKER || this.needParticipantType == ParticipantType.KEY_SPEAKER) {
												participants.addAll(form_ho_4.getSpeakers());
											}
										}
										
										if (cycle.getType().equals(CycleType.CYCLE_DS) == true) {
											if (this.needParticipantType == ParticipantType.ELECTIVE) {
												Form_DS_6 form_ds_6 = (Form_DS_6) event.getForms().get(Form_DS_6.class.getSimpleName());
												participants.addAll(form_ds_6.getElectives());
											} else if (this.needParticipantType == ParticipantType.SPEAKER || this.needParticipantType == ParticipantType.KEY_SPEAKER) {
												Form_DS_7 form_ds_7 = (Form_DS_7) event.getForms().get(Form_DS_7.class.getSimpleName());
												participants.addAll(form_ds_7.getSpeakers());
											}
										}
										
										if (cycle.getType().equals(CycleType.CYCLE_SEMINAR) == true) {
											Form_SEM_4 form_sem_4 = (Form_SEM_4) event.getForms().get(Form_SEM_4.class.getSimpleName());
											if (this.needParticipantType == ParticipantType.ELECTIVE) {
												for (AttendanceList a: form_sem_4.getParticipants()) {
													participants.addAll(a.getParticipants());
												}
											} else if (this.needParticipantType == ParticipantType.SPEAKER || this.needParticipantType == ParticipantType.KEY_SPEAKER) {
												for (AttendanceList a: form_sem_4.getSpeakers()) {
													participants.addAll(a.getParticipants());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// Sort the collection
		Collections.sort(participants);
	}
	
	/**
	 * @return the contractorSelectItems
	 */
	public List<SelectItem> getContractorSelectItems() {
		return contractorSelectItems;
	}

	/**
	 * @return the contractor_id
	 */
	public Integer getContractor_id() {
		return contractor_id;
	}

	/**
	 * @param contractor_id the contractor_id to set
	 */
	public void setContractor_id(Integer contractor_id) {
		this.contractor_id = contractor_id;
	}

	/**
	 * @return the regionSelectItems
	 */
	public List<SelectItem> getRegionSelectItems() {
		return regionSelectItems;
	}

	/**
	 * @param regionSelectItems the regionSelectItems to set
	 */
	public void setRegionSelectItems(List<SelectItem> regionSelectItems) {
		this.regionSelectItems = regionSelectItems;
	}

	/**
	 * @return the prefectureSelectItems
	 */
	public List<SelectItem> getPrefectureSelectItems() {
		return prefectureSelectItems;
	}

	/**
	 * @param prefectureSelectItems the prefectureSelectItems to set
	 */
	public void setPrefectureSelectItems(List<SelectItem> prefectureSelectItems) {
		this.prefectureSelectItems = prefectureSelectItems;
	}

	/**
	 * @return the region_id
	 */
	public Integer getRegion_id() {
		return region_id;
	}

	/**
	 * @param region_id the region_id to set
	 */
	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	/**
	 * @return the prefecture_id
	 */
	public String getPrefecture_id() {
		return prefecture_id;
	}

	/**
	 * @param prefecture_id the prefecture_id to set
	 */
	public void setPrefecture_id(String prefecture_id) {
		this.prefecture_id = prefecture_id;
	}

	/**
	 * @return the municipality_id
	 */
	public String getMunicipality_id() {
		return municipality_id;
	}

	/**
	 * @param municipality_id the municipality_id to set
	 */
	public void setMunicipality_id(String municipality_id) {
		this.municipality_id = municipality_id;
	}

	/**
	 * @return the statsList
	 */
	public List<StatsEvent> getStatsList() {
		return statsList;
	}

	/**
	 * @param statsList the statsList to set
	 */
	public void setStatsList(List<StatsEvent> statsList) {
		this.statsList = statsList;
	}

	/**
	 * @return the physical_objects
	 */
	public List<PhysicalObject> getPhysical_objects() {
		return physical_objects;
	}

	/**
	 * @return the regionKeysList
	 */
	public List<RegionKey> getRegionKeysList() {
		return regionKeysList;
	}

	/**
	 * @return the regionStatsMap
	 */
	public Map<RegionKey, RegionStats> getRegionStatsMap() {
		return regionStatsMap;
	}

	/**
	 * @return the subProjectGivenIds
	 */
	public HashMap<Integer, Integer> getSubProjectGivenIds() {
		return subProjectGivenIds;
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
		return total_percentage;
	}

	/**
	 * @param total_percentage the total_percentage to set
	 */
	public void setTotal_percentage(double total_percentage) {
		this.total_percentage = total_percentage;
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
	 * @return the localcouncilmembers_total
	 */
	public int getLocalcouncilmembers_total() {
		return localcouncilmembers_total;
	}

	/**
	 * @param localcouncilmembers_total the localcouncilmembers_total to set
	 */
	public void setLocalcouncilmembers_total(int localcouncilmembers_total) {
		this.localcouncilmembers_total = localcouncilmembers_total;
	}

	/**
	 * @return the men_total
	 */
	public int getMen_total() {
		return men_total;
	}

	/**
	 * @param men_total the men_total to set
	 */
	public void setMen_total(int men_total) {
		this.men_total = men_total;
	}

	/**
	 * @return the women_total
	 */
	public int getWomen_total() {
		return women_total;
	}

	/**
	 * @param women_total the women_total to set
	 */
	public void setWomen_total(int women_total) {
		this.women_total = women_total;
	}

	/**
	 * @return the men_speakers_total
	 */
	public int getMen_speakers_total() {
		return men_speakers_total;
	}

	/**
	 * @param men_speakers_total the men_speakers_total to set
	 */
	public void setMen_speakers_total(int men_speakers_total) {
		this.men_speakers_total = men_speakers_total;
	}

	/**
	 * @return the women_speakers_total
	 */
	public int getWomen_speakers_total() {
		return women_speakers_total;
	}

	/**
	 * @param women_speakers_total the women_speakers_total to set
	 */
	public void setWomen_speakers_total(int women_speakers_total) {
		this.women_speakers_total = women_speakers_total;
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
	 * @return the municipalitySelectItems
	 */
	public List<SelectItem> getMunicipalitySelectItems() {
		return municipalitySelectItems;
	}

	/**
	 * @param municipalitySelectItems the municipalitySelectItems to set
	 */
	public void setMunicipalitySelectItems(List<SelectItem> municipalitySelectItems) {
		this.municipalitySelectItems = municipalitySelectItems;
	}

	/**
	 * @return the uniqueParticipants
	 */
	

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
	 * @return the participants
	 */
	public List<Participant> getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
}
