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

/* $Id: OTABean.java 1553 2011-12-18 10:13:33Z markos $ */

package gr.codex.ota;

import gr.codex.katartisis.CycleType;
import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.katartisis.SubmissionStatus;
import gr.codex.katartisis.entitybeans.Cycle;
import gr.codex.ota.connectors.Section_I__TDE_Employees;
import gr.codex.ota.connectors.Section_K__TDE_Employees;
import gr.codex.ota.entitybeans.InvoiceExpense;
import gr.codex.ota.entitybeans.InvoicePayment;
import gr.codex.ota.entitybeans.Invoice_Contractor;
import gr.codex.ota.entitybeans.Invoice_Vendor;
import gr.codex.ota.entitybeans.OTA_Projects;
import gr.codex.ota.entitybeans.PhysicalObject;
import gr.codex.ota.entitybeans.Section_A;
import gr.codex.ota.entitybeans.Section_B;
import gr.codex.ota.entitybeans.Section_C;
import gr.codex.ota.entitybeans.Section_D;
import gr.codex.ota.entitybeans.Section_E;
import gr.codex.ota.entitybeans.Section_H;
import gr.codex.ota.entitybeans.Section_I;
import gr.codex.ota.entitybeans.Section_K;
import gr.codex.ota.entitybeans.Section_L;
import gr.codex.ota.entitybeans.Section_M;
import gr.codex.ota.entitybeans.Section_N;
import gr.codex.ota.entitybeans.Section_O;
import gr.codex.ota.entitybeans.Section_P;
import gr.codex.ota.entitybeans.Section_ST;
import gr.codex.ota.entitybeans.Section_Th;
import gr.codex.ota.entitybeans.Section_Z;
import gr.codex.ota.helpers.ActionsCategory;
import gr.codex.ota.helpers.DeiktesType;
import gr.codex.ota.helpers.DocumentEntry;
import gr.codex.ota.helpers.EUFPSSection;
import gr.codex.ota.helpers.EUFPSSubsection;
import gr.codex.ota.helpers.EUFramework;
import gr.codex.ota.helpers.EUMeasure;
import gr.codex.ota.helpers.EUPriorityAxis;
import gr.codex.ota.helpers.EUProgramme;
import gr.codex.ota.helpers.EUSubmeasure;
import gr.codex.ota.helpers.InvoiceType;
import gr.codex.ota.helpers.M3_TDE_Deiktes;
import gr.codex.ota.helpers.M3_TDE_Places;
import gr.codex.ota.helpers.M3_TDE_Problems;
import gr.codex.ota.helpers.M3_TDE_Semesters;
import gr.codex.ota.helpers.M3_TDY_Certification;
import gr.codex.ota.helpers.M3_TDY_Deiktes;
import gr.codex.ota.helpers.M3_TDY_Evolution;
import gr.codex.ota.helpers.M3_TDY_Places;
import gr.codex.ota.helpers.M3_TDY_Progress;
import gr.codex.ota.helpers.NOM_Contractor;
import gr.codex.ota.helpers.NOM_SubProjects;
import gr.codex.ota.helpers.SectionD_Deiktis;
import gr.codex.ota.helpers.SubProjectType;
import gr.codex.ota.helpers.TDE_Analysis;
import gr.codex.ota.helpers.TDE_Deiktes;
import gr.codex.ota.helpers.TDE_Employees;
import gr.codex.ota.helpers.TDE_Financial;
import gr.codex.ota.helpers.TDE_FinancialQuarters;
import gr.codex.ota.helpers.TDE_FundDistribution;
import gr.codex.ota.helpers.TDE_Funders;
import gr.codex.ota.helpers.TDE_Katartisis;
import gr.codex.ota.helpers.TDE_Permissions;
import gr.codex.ota.helpers.TDE_ProjectPlaces;
import gr.codex.ota.helpers.TDE_Tasks;
import gr.codex.ota.helpers.TDE_VendorTasks;
import gr.codex.ota.helpers.TDE_Vendors;
import gr.codex.ota.helpers.TDY_Deiktes;
import gr.codex.ota.helpers.TDY_Evolution;
import gr.codex.ota.helpers.TDY_Financial;
import gr.codex.ota.helpers.TDY_Places;
import gr.codex.ota.helpers.TDY_Progress;
import gr.codex.ota.helpers.TDY_ProgressActivities;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.Contractor;
import gr.codex.usermgmt.entitybeans.Municipality;
import gr.codex.usermgmt.entitybeans.Prefecture;
import gr.codex.usermgmt.entitybeans.Region;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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

public class OTABean {
	
	// dummy project id = -1, used in configBean.
	private static final Integer configId = -1;
	
	private String errorMsg;
	private String label;
	private String form_id;
	private Long sectionClass;
	private String sectionName;
	private String sectionId;
	private Integer revision;
	private Integer tmp_id;
	
	private List<OTA_Projects> projects = new ArrayList<OTA_Projects>();
	private List<Section_A> section_A_Projects = new ArrayList<Section_A>();
	private List<Contractor> contractors = new ArrayList<Contractor>();
	private List<PhysicalObject> physicalObjects = new ArrayList<PhysicalObject>();
	private List<Section_L> subProjects = new ArrayList<Section_L>();
	private List<Section_M> nomList = new ArrayList<Section_M>();
	private List<TDE_ProjectPlaces> projectPlaces = new ArrayList<TDE_ProjectPlaces>();
	private List<TDE_Vendors> vendors = new ArrayList<TDE_Vendors>();
	private List<SectionD_Deiktis> deiktes = new ArrayList<SectionD_Deiktis>();
	private List<SectionD_Deiktis> deiktes_ekrois = new ArrayList<SectionD_Deiktis>();
	private List<SectionD_Deiktis> deiktes_apotelesmatos = new ArrayList<SectionD_Deiktis>();
	private List<SectionD_Deiktis> deiktes_epiptoseon = new ArrayList<SectionD_Deiktis>();
	private List<TDE_Permissions> permissions = new ArrayList<TDE_Permissions>();
	private List<TDE_Analysis> analysis = new ArrayList<TDE_Analysis>();
	private List<TDE_Katartisis> katartisis = new ArrayList<TDE_Katartisis>();
	private List<TDE_Tasks> tasks = new ArrayList<TDE_Tasks>();
	private List<TDE_VendorTasks> vendorTasks = new ArrayList<TDE_VendorTasks>();
	private List<TDE_Employees> section_I_employees = new ArrayList<TDE_Employees>();
	private List<TDE_Employees> section_K_employees = new ArrayList<TDE_Employees>();
	private List<TDY_Progress> tdy_progress = new ArrayList<TDY_Progress>();
	private List<TDY_Places> tdy_places = new ArrayList<TDY_Places>();
	private List<TDY_Deiktes> tdy_deiktes = new ArrayList<TDY_Deiktes>();
	private List<TDY_ProgressActivities> tdy_progressActivities = new ArrayList<TDY_ProgressActivities>();
	private List<TDY_Financial> tdy_financial = new ArrayList<TDY_Financial>();
	private List<TDY_Evolution> tdy_evolution = new ArrayList<TDY_Evolution>();
	private List<TDE_Financial> tde_financial = new ArrayList<TDE_Financial>();
	private List<TDY_Financial> subprj_tdy_financial = new ArrayList<TDY_Financial>();
	private List<Invoice_Contractor> invoices_contractor = new ArrayList<Invoice_Contractor>();
	private List<Invoice_Vendor> invoices_vendor = new ArrayList<Invoice_Vendor>();
	private Map<Section_L, List<TDE_Financial>> all_tde_financials = new HashMap<Section_L, List<TDE_Financial>>();
	private Map<Section_L, List<TDY_Progress>> all_tdy_progress = new HashMap<Section_L, List<TDY_Progress>>();
	private Map<Section_L, List<TDY_Places>> all_tdy_places = new HashMap<Section_L, List<TDY_Places>>();
	private Map<Section_L, List<TDY_Deiktes>> all_tdy_deiktes = new HashMap<Section_L, List<TDY_Deiktes>>();
	private Map<Section_L, List<TDY_ProgressActivities>> all_tdy_progressActivities = new HashMap<Section_L, List<TDY_ProgressActivities>>();
	private Map<Section_L, List<TDY_Financial>> all_tdy_financial = new HashMap<Section_L, List<TDY_Financial>>();
	private Map<Section_L, List<TDY_Evolution>> all_tdy_evolution = new HashMap<Section_L, List<TDY_Evolution>>();
	private Map<Section_L, Contractor> all_contractors = new HashMap<Section_L, Contractor>();
	private List<TDE_Funders> tde_funders = new ArrayList<TDE_Funders>();
	private List<TDE_FundDistribution> tde_fundDistribution = new ArrayList<TDE_FundDistribution>();
	private List<NOM_Contractor> nom_contractors = new ArrayList<NOM_Contractor>();
	private List<NOM_SubProjects> nom_subProjects = new ArrayList<NOM_SubProjects>();
	private List<Section_N> monthly_reports = new ArrayList<Section_N>();
	private List<Section_P> sub3monthly_reports = new ArrayList<Section_P>();
	private List<M3_TDY_Deiktes> m3_tdy_deiktes = new ArrayList<M3_TDY_Deiktes>();
	private List<M3_TDY_Evolution> m3_tdy_evolution = new ArrayList<M3_TDY_Evolution>();
	private List<M3_TDY_Places> m3_tdy_places = new ArrayList<M3_TDY_Places>();
	private List<M3_TDY_Progress> m3_tdy_progress = new ArrayList<M3_TDY_Progress>();
	private List<M3_TDY_Certification> m3_tdy_certifications = new ArrayList<M3_TDY_Certification>();
	private List<Section_O> prj3monthly_reports = new ArrayList<Section_O>();
	private List<M3_TDE_Places> m3_tde_places = new ArrayList<M3_TDE_Places>();
	private List<M3_TDE_Deiktes> m3_tde_deiktes = new ArrayList<M3_TDE_Deiktes>();
	private List<M3_TDE_Problems> m3_tde_problems = new ArrayList<M3_TDE_Problems>();
	private List<M3_TDE_Semesters> m3_tde_semesters = new ArrayList<M3_TDE_Semesters>();
	private List<Integer> years = new ArrayList<Integer>();
	private List<Integer> quarters = new ArrayList<Integer>();
	private List<YearQuarters> quartersList = new ArrayList<YearQuarters>();
	
	// select item Lists
	private List<SelectItem> defaultVendorsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> supervisingVendorsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultDeiktesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultProgressSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEvolutionSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultInvoiceTypesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultFundersSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUFrameworksSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUProgrammesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUAxonsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUMeasuresSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUSubMeasuresSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUSectionsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultEUSubSectionsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> regionsSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> proposalVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> proposalVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> implementationVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> implementationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> operationVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> operationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> contractorSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> subProjectSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> expensesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> defaultSubProjectTypesSelectItemsList = new ArrayList<SelectItem>();
	private HashMap<NOM_SubProjects, Boolean> selectedNomSubProjects = new HashMap<NOM_SubProjects, Boolean>();
	private HashMap<Integer, BigDecimal> yearBudgetMatrix = new HashMap<Integer, BigDecimal>();
	
	private List<Section_A> revisions = new ArrayList<Section_A>();
	
	private Contractor contractor;
	private Integer contractorId;
	private InvoiceType invoiceType;
	private Invoice_Contractor invoice_contractor;
	private Invoice_Vendor invoice_vendor;
	private TDE_Vendors vendor;
	private BigDecimal sectionZ_categoriesBudget; 
	
	private Section_N tmp_monthly;
	private Section_P tmp_sub3monthly;
	private Section_O tmp_prj3monthly;
	private M3_TDE_Problems tmp_m3_tde_problems;
	private M3_TDE_Semesters tmp_m3_tde_semester;
	private int section_I_employees_currQty;
	private int section_I_employees_estQty;
	private int section_K_employees_currQty;
	private int section_K_employees_estQty;
	private BigDecimal section_o_total_budget;
	private HtmlDataTable projectdataTable;
	private HtmlDataTable revisionsdataTable;
	private HtmlDataTable subProjectdataTable;
	private HtmlDataTable nomikesDesmefseisDataTable;
	private HtmlDataTable invoiceContractorListDataTable;
	private HtmlDataTable invoiceVendorListDataTable;
	private HtmlDataTable reportsListDataTable;
	private HtmlDataTable tmp_dataTable;
	private HtmlDataTable tmp_dataTable2;
	private HtmlDataTable tmp_dataTable3;
	private HtmlDataTable tmp_dataTable4;
	private HtmlDataTable tmp_dataTable5;
	private HtmlDataTable tmp_dataTable6;
	private HtmlDataTable tmp_dataTable7;
	private HtmlDataTable tmp_dataTable8;
	private HtmlDataTable tmp_dataTable9;
	private HtmlDataTable tmp_dataTable10;
	private HtmlDataTable tmp_dataTable11;
	private HtmlDataTable tmp_dataTable12;
	private HtmlDataTable tmp_dataTable13;
	private HtmlDataTable tmp_dataTable14;
	private HtmlDataTable tmp_dataTable15;
	private HtmlDataTable tmp_dataTable16;
	private String tmp_municipality;
	private String reportOutputHTML;
	private String reportE1HTML;
	private String reportE3HTML;
	
	// OTA objects
	private EUFramework framework;
	private EUProgramme programme;
	private EUPriorityAxis axis;
	private EUMeasure measure;
	private EUSubmeasure submeasure;
	private EUFPSSection section;
	private EUFPSSubsection subsection;
	private ActionsCategory projectaction;
	private Section_A sec_a;
	private Section_B sec_b;
	private Section_C sec_c;
	private Section_D sec_d;
	private Section_E sec_e;
	private Section_H sec_h;
	private Section_I sec_i;
	private Section_K sec_k;
	private Section_L sec_l;
	private Section_M sec_m;
	private Section_N sec_n;
	private Section_O sec_o;
	private Section_P sec_p;
	private Section_ST sec_st;
	private Section_Th sec_th;
	private Section_Z sec_z;

	private HashMap<Class<?>, Boolean> saveable = new HashMap<Class<?>, Boolean>();
	private boolean submittable;
	private boolean checkProcessed;
	private Date date_approval;
	
	Context ctx;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;

	private NumberFormat currencyFormatter;
	
	/**
	 * Class constructor, performs initial loading.
	 */
	public OTABean() {
		// start remote connection to EJB3 backend.
		try {
			ctx = new InitialContext();
		
			// initialize remote interface
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
			getProjectList();
		} catch(NamingException e) {
			e.printStackTrace();
		}
		this.errorMsg = "";
		this.date_approval = new Date();
		
		this.saveable.put(Section_A.class, false);
		this.saveable.put(Section_B.class, false);
		this.saveable.put(Section_C.class, false);
		this.saveable.put(Section_D.class, false);
		this.saveable.put(Section_E.class, false);
		this.saveable.put(Section_H.class, false);
		this.saveable.put(Section_Th.class, false);
		this.saveable.put(Section_I.class, false);
		this.saveable.put(Section_K.class, false);
		this.saveable.put(Section_L.class, false);
		this.saveable.put(Section_ST.class, false);
		this.saveable.put(Section_Z.class, false);
		
		this.quarters = new ArrayList<Integer>();
		for (int i=1; i<= 4; i++)
			this.quarters.add(i);
		
		this.currencyFormatter = NumberFormat.getNumberInstance();
		currencyFormatter.setGroupingUsed(true);
		currencyFormatter.setMaximumFractionDigits(2);
		currencyFormatter.setMinimumFractionDigits(2);
	}
	
	public void clearProjectDataTables() {
		this.projectdataTable = new HtmlDataTable();
		this.revisionsdataTable = new HtmlDataTable();
	}
	
	public void clearAllDataTables() {
		this.subProjectdataTable = new HtmlDataTable();
		this.nomikesDesmefseisDataTable = new HtmlDataTable();
		this.reportsListDataTable = new HtmlDataTable();
		this.tmp_dataTable = new HtmlDataTable();
		this.tmp_dataTable2 = new HtmlDataTable();
		this.tmp_dataTable3 = new HtmlDataTable();
		this.tmp_dataTable4 = new HtmlDataTable();
		this.tmp_dataTable5 = new HtmlDataTable();
		this.tmp_dataTable6 = new HtmlDataTable();
		this.tmp_dataTable7 = new HtmlDataTable();
		this.tmp_dataTable8 = new HtmlDataTable();
		this.tmp_dataTable9 = new HtmlDataTable();
		this.tmp_dataTable10 = new HtmlDataTable();
		this.tmp_dataTable11 = new HtmlDataTable();
		this.tmp_dataTable12 = new HtmlDataTable();
		this.tmp_dataTable13 = new HtmlDataTable();
		this.tmp_dataTable14 = new HtmlDataTable();
		this.tmp_dataTable15 = new HtmlDataTable();
		this.tmp_dataTable16 = new HtmlDataTable();
		this.invoiceContractorListDataTable = new HtmlDataTable();
		this.invoiceVendorListDataTable = new HtmlDataTable();
	}
	
	public void regenerateRegionsSelectItems() {

		// get list of all regions as a selectItem
		this.regionsSelectItems = new ArrayList<SelectItem>();

		List<Region> regions = ubean.getAllRegions();
		for( Iterator iter = regions.iterator(); iter.hasNext(); ) {
			Region r = (Region) iter.next();
			SelectItem item = new SelectItem( r.getId(), r.getName() );
			this.regionsSelectItems.add(item);
		}
	}
	
	/**
	 * Creates objects and data that are needed in the Index page of OTA application.
	 * in this method, which is the first to be called, we load the projects for the index page.
	 * 
	 * @return String 'success'
	 */
	public String getProjectList() {

		// get Projects LIST
		this.projects = (List<OTA_Projects>) kbean.getRecordsByQuery("sectionName='Section_A'", OTA_Projects.class);

		// init List for INDEX page
		this.section_A_Projects = new ArrayList();

		// I must construct a HashMap with the project Data
		for (Iterator iter = this.projects.iterator(); iter.hasNext();) {
			OTA_Projects o = (OTA_Projects) iter.next();

			Section_A a = (Section_A) kbean.getRecordsByQuery("id=" + o.getSectionId(), Section_A.class).get(0);

			// once we get the object we get the class then we load it with
			// latest revision
			Section_A a_ = (Section_A) kbean.getLatestRevisionObject(Section_A.class, a.getNum());

			// add to indexProjects LIST
			this.section_A_Projects.add(a_);

			clearAllDataTables();
		}

		this.checkProcessed = false;
		
		return "success";
	}
	
	/* Physical Object methods */
	public String goToPhysicalObjectSubProjectList() {
		goToTDE();
		goToSectionB();
					
		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "physicalObjectSubProjectList";
	}
	
	/**
	 * Loads the Physical Objects as a LIst from the database, to display in appropriate Page
	 * 
	 * @return String 'physical_object_loaded'
	 */
	public void savePhysicalObjects() {
		for( Iterator iter=this.physicalObjects.iterator(); iter.hasNext(); ) {
			PhysicalObject pobj = (PhysicalObject) iter.next();
			
			if ( pobj.getDate_programmedStart() != null && pobj.getDate_programmedEnd() != null && 
					pobj.getDate_programmedStart().after( pobj.getDate_programmedEnd()) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προσθήκη Φυσικού Αντικειμένου: Η προγραμματισμένη ημ/νια έναρξης δεν μπορεί να είναι " +
						"μεγαλύτερη από την προγραμματισμένη ημ/νια λήξης.") );
				return;
			}
			
			Section_L l = (Section_L) kbean.getRecordById(pobj.getSubprojectId(), Section_L.class);
			Section_M m = kbean.getLastNomikiDesmefsiBySubProject(l);
			if (m != null) {
				System.out.println("ND: "+ m.getId() + ", revision: " + m.getRevision() + ", title: "
						+ m.getTitle() + ", amount: " + m.getAmount());
				if ( pobj.getDate_programmedStart() != null && m.getDate_acceptance() != null && 
						pobj.getDate_programmedStart().before(m.getDate_acceptance()) ) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Φυσικό Αντικείμενο: " + pobj.getActivity() + ": Η προγραμματισμένη ημ/νια έναρξης δεν μπορεί να είναι " +
							"προγενέστερη από την ημ/νια ανάληψης της Νομικής Δέσμευσης του υποέργου '"+ l.getTitle()
							+ "', " + DateUtil.cts(m.getDate_acceptance())) );
					return;
				}
				if ( pobj.getDate_programmedEnd() != null && m.getDate_expiration() != null && 
						pobj.getDate_programmedEnd().after(m.getDate_expiration()) ) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Φυσικό Αντικείμενο: " + pobj.getActivity() + ": Η προγραμματισμένη ημ/νια λήξης δεν μπορεί να είναι " +
							"μεταγενέστερη από την ημ/νια λήξης της Νομικής Δέσμευσης του υποέργου '"+ l.getTitle()
							+ "', " + DateUtil.cts(m.getDate_expiration())) );
					return;
				}
				if ( pobj.getDate_actualStart() != null && m.getDate_acceptance() != null && 
						pobj.getDate_actualStart().before(m.getDate_acceptance()) ) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Φυσικό Αντικείμενο: " + pobj.getActivity() + ": Η πραγματική ημ/νια έναρξης δεν μπορεί να είναι " +
							"προγενέστερη από την ημ/νια ανάληψης της Νομικής Δέσμευσης του υποέργου '"+ l.getTitle()
							+ "', " + DateUtil.cts(m.getDate_acceptance())) );
					return;
				}
				if ( pobj.getDate_actualEnd() != null && m.getDate_expiration() != null && 
						pobj.getDate_actualEnd().after(m.getDate_expiration()) ) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Φυσικό Αντικείμενο: " + pobj.getActivity() + ": Η πραγματική ημ/νια λήξης δεν μπορεί να είναι " +
							"μεταγενέστερη από την ημ/νια λήξης της Νομικής Δέσμευσης του υποέργου '"+ l.getTitle()
							+ "', " + DateUtil.cts(m.getDate_expiration())) );
					return;
				}
			}
		}
		
		for( PhysicalObject pobj: this.physicalObjects) {
			if (pobj.getId() == null)
				pobj = (PhysicalObject) kbean.createRecord( pobj );
			else
				kbean.updateRecord(pobj);
		}
	}
	
	public String goToPhysicalObjectList() {
		/* It's possible that we get here from inside an invoice edit form
		   that way the datatable will be empty, so we do nothing, this.sec_l
		   will already be set to the right value.
		 */ 
		if (this.subProjectdataTable.getRowCount() > 0)
			this.sec_l = (Section_L) this.subProjectdataTable.getRowData();
		
		// load the physical obejcts list
		this.physicalObjects = (List<PhysicalObject>) kbean.getRecordsByQuery("num=" + this.sectionClass, PhysicalObject.class );

		clearAllDataTables();
			
		// call external methods to load data for TDE and Section_B
		goToTDE();
		goToSectionB();
		
		return "physical_object_loaded";
	}
	
	public String addNewPhysicalObject() {
		PhysicalObject pobj = new PhysicalObject();
		pobj.setActivity("νέα δραστηριότητα");
		pobj.setNum(this.sectionClass);
		pobj.setSubprojectId(this.sec_l.getId());
		
		this.physicalObjects.add(pobj);
		
		return "physical_object_loaded";
	}

	public String deletePhysicalObject() {
		PhysicalObject pobj = (PhysicalObject) this.tmp_dataTable.getRowData();
		
        System.out.println( "removing PhysicalObject object: " +pobj.getActivity() + ", " + pobj.getId() );
        
        this.physicalObjects.remove(pobj);
        
        if (pobj.getId() != null) {
        	kbean.deleteRecord(PhysicalObject.class, pobj.getId() );
        }
        
		return "deleted";
	}

	/* Invoices methods */
	/**
	 * Loads the Invoices List from the DAtabase
	 * 
	 * @return String 'invoiceList'
	 */
	public String goToInvoicesSubProjectList() {
		goToTDE();
		goToSectionB();
		goToSectionZ();
		
		List<InvoiceType> invoiceTypes = (List<InvoiceType>) kbean.getAllRecordsOfType(InvoiceType.class);
		this.defaultInvoiceTypesSelectItemsList = new ArrayList<SelectItem>();
		this.defaultInvoiceTypesSelectItemsList.add(new SelectItem(new Integer(-1), "Παρακαλώ επιλέξτε"));
		for (InvoiceType invoiceType : invoiceTypes) {
			SelectItem s = new SelectItem(invoiceType.getId(), invoiceType.getType());
			this.defaultInvoiceTypesSelectItemsList.add(s);
		}
		
		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "invoiceSubProjectList";
	}	
	
	public String goToInvoicesList() {
		/* It's possible that we get here from inside an invoice edit form
		   that way the datatable will be empty, so we do nothing, this.sec_l
		   will already be set to the right value.
		 */ 
		if (this.subProjectdataTable.getRowCount() > 0)
			this.sec_l = (Section_L) this.subProjectdataTable.getRowData();
		
		clearAllDataTables();
		
		this.invoices_contractor = kbean.getInvoiceContractorListBySubProjectId(this.sec_l.getId());
		this.invoices_vendor = kbean.getInvoiceVendorListBySubProjectId(this.sec_l.getId());
		this.invoiceContractorListDataTable = new HtmlDataTable();
		this.invoiceVendorListDataTable = new HtmlDataTable();
		
		// now please load the selectItems.
		this.expensesSelectItemsList = new ArrayList<SelectItem>();
		this.expensesSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for( TDY_Financial d : this.subprj_tdy_financial ) {
			SelectItem s = new SelectItem( d.getCategory() );
			this.expensesSelectItemsList.add(s);
		}
		
		return "invoiceList";
	}
	
	public String goToInvoiceContractor() {
		this.invoice_contractor = (Invoice_Contractor) this.invoiceContractorListDataTable.getRowData();
		this.contractors = new ArrayList<Contractor>();
		
		this.contractorId = this.invoice_contractor.getContractorId();
		if (this.contractorId != null) {
			this.contractor = (Contractor) kbean.getRecordById(this.contractorId, Contractor.class );
		} else {
			this.contractorId = new Integer(-1);
		}

		this.invoiceType = this.invoice_contractor.getInvoiceType();
		
		Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(this.sec_l); 
		if (sec_m != null) {
			for (NOM_Contractor nc: sec_m.getContractors()) {
				this.contractors.add(nc.getContractor());
			}
		}
		this.contractorSelectItems.clear();
		for (Contractor c: this.contractors) {
			contractorSelectItems.add(new SelectItem(c.getId(), c.getName()));
		}
		if (this.contractors.size() == 1) {
			this.contractor = this.contractors.get(0);
		}
		
		clearAllDataTables();
		
		return "add_invoiceContractor";
	}
	
	public String goToInvoiceVendor() {
		this.invoice_vendor = (Invoice_Vendor) this.invoiceVendorListDataTable.getRowData();
		
		clearAllDataTables();
		
		return "add_invoiceVendor";
	}
	
	public void invoiceContractor_updateContractor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			this.contractor = (Contractor) kbean.getRecordById(this.contractorId, Contractor.class);
		}
        
		return;
	}
	
	public String addInvoiceContractor() {
		this.invoice_contractor = new Invoice_Contractor();
		this.contractors = new ArrayList<Contractor>();
		this.contractorId = new Integer(-1);
		this.invoiceType = new InvoiceType();
		
		Section_M sec_m = kbean.getLastNomikiDesmefsiBySubProject(this.sec_l);
		if (sec_m != null) {
			for (NOM_Contractor nc: sec_m.getContractors()) {
				this.contractors.add(nc.getContractor());
			}
		}
		this.contractorSelectItems.clear();
		for (Contractor c: this.contractors) {
			contractorSelectItems.add(new SelectItem(c.getId(), c.getName()));
		}
		if (this.contractors.size() == 1) {
			this.contractor = this.contractors.get(0);
			this.contractorId = this.contractor.getId();
		}
		
		this.invoice_contractor.setSubProjectId(this.sec_l.getId());
		this.invoice_contractor.setDate_issue(new Date());
		
		clearAllDataTables();
	
		return "add_invoiceContractor";
	}
	
	public String addInvoiceVendor() {
		this.invoice_vendor = new Invoice_Vendor();
		this.invoice_vendor.setSubProjectId(this.sec_l.getId());
		this.invoice_vendor.setDate_payment(new Date());
		
		List<TDY_Financial> expensesList = kbean.getTDYFinancialBySectionLId(this.sec_l.getId());
		System.out.println("expensesList.size = " + expensesList.size());
		
		List<Invoice_Contractor> invoices = kbean.getInvoiceContractorListBySubProjectId(this.sec_l.getId());
		for (Invoice_Contractor i: invoices) {
			InvoicePayment p = new InvoicePayment();
			p.setInvoice(i);
			p.setVendorPayment(this.invoice_vendor);
			List<InvoiceExpense> paidExpenses = new ArrayList<InvoiceExpense>();
			for (TDY_Financial t: expensesList) {
				InvoiceExpense e = new InvoiceExpense();
				e.setExpense(t);
				e.setVendorPayment(p);
				paidExpenses.add(e);
			}
			System.out.println("paidExpenses.size = " + paidExpenses.size());
			p.setPaidExpenses(paidExpenses);
			
			this.invoice_vendor.addPaidInvoice(p);
		}
		
		clearAllDataTables();
		
		return "add_invoiceVendor";
	}
	
	public String saveInvoice_Contractor() {
		this.invoice_contractor.setTotal(this.invoice_contractor.getNet().add(this.invoice_contractor.getVat()));
		
		if (this.contractorId == null || 
				(this.contractorId != null && this.contractorId.equals(-1) == true)) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε ανάδοχο!"));
			return "failure";
		}
		this.invoice_contractor.setContractorId(this.contractorId);
		if (this.invoice_contractor.getInvoiceNumber().equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε αριθμό παραστατικού!"));
			return "failure";
		}
		/* Comment this as VAT % changes all the time 
		MathContext mc = new MathContext(2);
		BigDecimal vat = this.invoice_contractor.getNet().multiply(BigDecimal.valueOf(0.19), mc);
		if (this.invoice_contractor.getVat().compareTo(vat.) > 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Ο ΦΠΑ δε μπορεί να είναι μεγαλύτερος από το 19% του Ποσού, δηλ." + vat ));
			return "failure";
		}*/
		if (this.invoiceType.getId() == null || (this.invoiceType.getId() != null && this.invoiceType.getId().equals(-1))) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε τύπο Παραστατικού!" ));
			return "failure";
		} else {
			this.invoiceType = (InvoiceType) kbean.getRecordById(this.invoiceType.getId(), InvoiceType.class);
			this.invoice_contractor.setInvoiceType(this.invoiceType);
		}
		this.invoice_contractor.setNum( this.sectionClass );
		if (this.invoice_contractor.getId() == null)
			kbean.createRecord( this.invoice_contractor );
		else
			kbean.updateRecord(this.invoice_contractor);
		
		goToInvoicesList();
		
		return "saved_invoiceContractor";
	}
	
	public String saveInvoice_Vendor() {
		this.invoice_vendor.setNum( this.sectionClass );
		this.saveable.put(Invoice_Vendor.class, true);
		
		this.invoice_vendor.setAmount(this.invoice_vendor.getNet().add(this.invoice_vendor.getVat()));
		
		BigDecimal payments = BigDecimal.ZERO;
		for (InvoicePayment payment: this.invoice_vendor.getPaidInvoices()) {
			System.out.println("payment = " + payment.getPaidAmount() + ", rem = " + payment.getRemainingAmount());
			if (payment.getPaidAmount().compareTo(payment.getRemainingAmount()) > 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το ποσό πληρωμής για το τιμολόγιο "
						+ payment.getInvoice().getInvoiceNumber() + " υπερβαίνει το απομένον ποσό για την εξόφληση του τιμολογίου"));
				this.saveable.put(Invoice_Vendor.class, false);
			}
			payments = payments.add(payment.getPaidAmount());
		}
		
		for (InvoicePayment payment: this.invoice_vendor.getPaidInvoices()) {
			for (InvoiceExpense paidExpense: payment.getPaidExpenses()) {
				System.out.println("expense = " + paidExpense.getPaidAmount() + ", rem = " + paidExpense.getRemainingAmount());
				if (paidExpense.getPaidAmount().compareTo(paidExpense.getRemainingAmount()) > 0) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το ποσό πληρωμής για τη δαπάνη: "
							+ paidExpense.getExpense().getCategory() + ", υπερβαίνει το απομένον ποσό για την εξόφληση της δαπάνης"));
					this.saveable.put(Invoice_Vendor.class, false);
				}
			}
		}
		
		if (payments.compareTo(this.invoice_vendor.getAmount()) != 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("ΠΡΟΣΟΧΗ! Οι πληρωμές τιμολογίων πρέπει να ισούνται με το Συνολικό Ποσό της Πληρωμής"));
			this.saveable.put(Invoice_Vendor.class, false);
		}
		
		BigDecimal vat = this.invoice_vendor.getNet().multiply(BigDecimal.valueOf(19)).setScale(-2, BigDecimal.ROUND_HALF_UP);
		if (this.invoice_vendor.getVat().compareTo(vat) > 0 ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Ο ΦΠΑ δε μπορεί να είναι μεγαλύτερος από το 19% του Καθαρού Ποσού, δηλ." + vat ));
			this.saveable.put(Invoice_Vendor.class, false);
		}
		
		if (this.invoice_vendor.getInvoiceType() != null && this.invoice_vendor.getInvoiceType().getId() == -1) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε τύπο παραστατικού!"));
			this.saveable.put(Invoice_Vendor.class, false);
		}
		
		if (this.saveable.get(Invoice_Vendor.class) == true) {
			if (this.invoice_vendor.getId() == null)
				this.invoice_vendor = (Invoice_Vendor) kbean.createRecord( this.invoice_vendor );
			else
				this.invoice_vendor = (Invoice_Vendor) kbean.updateRecord(this.invoice_vendor);
			
/*			for (InvoicePayment payment: this.invoice_vendor.getPaidInvoices()) {
				if (payment.getId() == null)
					payment = (InvoicePayment) kbean.createRecord( payment );
				else
					payment = (InvoicePayment) kbean.updateRecord(payment);
			}
		
			for (InvoiceExpense paidExpense: this.invoice_vendor.getPaidExpenses()) {
				if (paidExpense.getId() == null)
					paidExpense = (InvoiceExpense) kbean.createRecord( paidExpense );
				else
					paidExpense = (InvoiceExpense) kbean.updateRecord(paidExpense);
			}*/
			
			goToInvoicesList();
			
			return "saved_invoiceVendor";
		}
				
		return "add_invoiceVendor";
	}
	
	public String deleteInvoiceContractor() {
		Invoice_Contractor inv = (Invoice_Contractor) this.invoiceContractorListDataTable.getRowData();
		this.invoices_contractor.remove(inv);
		
		kbean.deleteRecord( Invoice_Contractor.class, inv.getId() );
		
		return "invoiceList";
	}

	public String deleteInvoiceVendor() {
		Invoice_Vendor inv = (Invoice_Vendor) this.invoiceVendorListDataTable.getRowData();
		this.invoices_vendor.remove(inv);
		
		kbean.deleteRecord( Invoice_Vendor.class, inv.getId() );
		
		return "invoiceList";
	}
	
	/* Invoice Reports methods */
	public String goToReportInvoicesSubProjectList() {
		goToTDE();
		goToSectionB();
		goToSectionZ();
		
		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "invoiceReportsSubProjectList";
	}	
	
	public String goToInvoiceReportsList() {
		/* It's possible that we get here from inside an invoice edit form
		   that way the datatable will be empty, so we do nothing, this.sec_l
		   will already be set to the right value.
		 */ 
		if (this.subProjectdataTable.getRowCount() > 0)
			this.sec_l = (Section_L) this.subProjectdataTable.getRowData();
		
		clearAllDataTables();

		this.invoices_contractor = kbean.getInvoiceContractorListBySubProjectId(this.sec_l.getId());
		this.invoices_vendor = kbean.getInvoiceVendorListBySubProjectId(this.sec_l.getId());
		
		return "reportInvoicesList";
	}
	
	/* Section A methods */
	public void checkSection_A__Vendors() {
		for( Iterator iter=this.vendors.iterator(); iter.hasNext(); ) {
			TDE_Vendors vendor = (TDE_Vendors) iter.next();
		}
	}
	
	public void saveSection_A__Vendors() {
					
		for( Iterator iter=this.vendors.iterator(); iter.hasNext(); ) {
			TDE_Vendors vendor = (TDE_Vendors) iter.next();
			
			if (vendor.getId() == null)
				vendor = (TDE_Vendors) kbean.createRecord( vendor );
			else
				kbean.updateRecord(vendor);
		}
		
		this.label = "";
	}
	
	public String addSection_A__Vendor() {
		TDE_Vendors tmp_vendor = new TDE_Vendors();
		tmp_vendor.setSec_a_id(this.sec_a.getId());
		tmp_vendor.setName("νέος φορέας");
		this.vendors.add( tmp_vendor);
		this.label = "VendorsListLabel";
		
		return "section_a";		
	}
		
	public String deleteSection_A__TDE_Vendor() {
		TDE_Vendors tmp_vendor = (TDE_Vendors) this.tmp_dataTable.getRowData(); 
        
        System.out.println( "removing Vendor object: " +tmp_vendor.getName() + ", " + tmp_vendor.getId() );
        vendors.remove(tmp_vendor);
        if (tmp_vendor.getId() != null) {
        	kbean.deleteRecord(TDE_Vendors.class, tmp_vendor.getId() );
        }
        this.label = "";
        
		return "deleted";
	}
	
	public BigDecimal getSection_A__RemainingProjectBudget() {
		BigDecimal rem = this.sec_a.getAmount();
		
		for( Section_L sub :this.subProjects) {
			rem = rem.subtract(sub.getBudget());
		}
		
		return rem;
	}
	
	public void sectionA_changeVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDE_Vendors v1 = (TDE_Vendors) this.tmp_dataTable.getRowData();
			TDE_Vendors v2 = (TDE_Vendors) kbean.getRecordById(v1.getId2(), TDE_Vendors.class);
			v1.setCode(v2.getCode());
			v1.setName(v2.getName());
			v1.setSec_a_id(this.sec_a.getId());
		}
		
		this.label = "VendorsListLabel";
        
		return;
	}
	
	public void sectionA_changeProposalVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_a.getProposalVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_a.setProposalVendor_code(vendor.getCode());
			this.sec_a.setProposalVendor_name(vendor.getName());
		}
		
		this.label = "";
        
		return;
	}
	
	public void sectionA_changeImplementationVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_a.getImplementationVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_a.setImplementationVendor_code(vendor.getCode());
			this.sec_a.setImplementationVendor_name(vendor.getName());
		}
		
		this.label = "";
        
		return;
	}
	
	public void sectionA_changeFinancingVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_a.getFinancingVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_a.setFinancingVendor_code(vendor.getCode());
			this.sec_a.setFinancingVendor_name(vendor.getName());
		}
		
		this.label = "";
        
		return;
	}
	
	public void sectionA_changeOperationVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_a.getOperationVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_a.setOperationVendor_code(vendor.getCode());
			this.sec_a.setOperationVendor_name(vendor.getName());
		}
		
		return;
	}
	
	public void sectionA_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUFramework frm = (EUFramework) kbean.getRecordById(this.sec_a.getFramework(), EUFramework.class);
			System.out.println("prog.id = " + this.sec_a.getFramework() + ", frm = " + frm);
			if (frm != null) {
				this.framework = frm;
				this.sec_a.setKps(this.framework.getCode() + ": " + this.framework.getName());
				this.sec_a.setCategory_code("");
			}
		}
		
		return;
	}
	
	public void sectionA_changedProgramme(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUProgramme prog = (EUProgramme) kbean.getRecordById(this.sec_a.getProgramme(), EUProgramme.class);
			System.out.println("prog.id = " + this.sec_a.getProgramme() + ", prog = " + prog);
			if (prog != null) {
				this.programme = prog;
				this.sec_a.setEpix_program(this.programme.getName());
				this.sec_a.setEpix_code(this.programme.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedAxis(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUPriorityAxis axis = (EUPriorityAxis) kbean.getRecordById(this.sec_a.getAxis(), EUPriorityAxis.class);
			System.out.println("axis.id = " + this.sec_a.getAxis() + ", axis = " + axis);
			if (axis != null) {
				this.axis = axis;
				this.sec_a.setAxon(this.axis.getName());
				this.sec_a.setAxon_code(this.axis.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedMeasure(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUMeasure meas = (EUMeasure) kbean.getRecordById(this.sec_a.getMeasure(), EUMeasure.class);
			System.out.println("measure.id = " + this.sec_a.getMeasure() + ", measure = " + meas);
			if (meas != null) {
				this.measure = meas;
				this.sec_a.setMetro(this.measure.getName());
				this.sec_a.setMetro_code(this.measure.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedSubMeasure(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUSubmeasure submeas = (EUSubmeasure) kbean.getRecordById(this.sec_a.getSubmeasure(), EUSubmeasure.class);
			System.out.println("submeasure.id = " + this.sec_a.getSubmeasure() + ", submeasure = " + submeas);
			if (submeas != null) {
				this.submeasure = submeas;
				this.sec_a.setSubMetro(this.submeasure.getName());
				this.sec_a.setSubMetro_code(this.submeasure.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedSection(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUFPSSection fpssection = (EUFPSSection) kbean.getRecordById(this.sec_a.getSection(), EUFPSSection.class);
			System.out.println("section.id = " + this.sec_a.getSection() + ", section = " + fpssection);
			if (fpssection != null) {
				this.section = fpssection;
				this.sec_a.setKpsSection(this.section.getName());
				this.sec_a.setKpsSection_code(this.section.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedSubSection(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUFPSSubsection fpssection = (EUFPSSubsection) kbean.getRecordById(this.sec_a.getSubsection(), EUFPSSubsection.class);
			System.out.println("subsection.id = " + this.sec_a.getSubsection() + ", subsection = " + fpssection);
			if (fpssection != null) {
				this.subsection = fpssection;
				this.sec_a.setKpsSubSection(this.subsection.getName());
				this.sec_a.setKpsSubSection_code(this.subsection.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changedActionCategory(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			ActionsCategory action = (ActionsCategory) kbean.getRecordById(this.sec_a.getProjectAction(), ActionsCategory.class);
			System.out.println("action.id = " + this.sec_a.getProjectAction() + ", action = " + action);
			if (action != null) {
				this.projectaction = action;
				this.sec_a.setCategory(this.projectaction.getCategory());
				this.sec_a.setCategory_code(this.projectaction.getCode());
			}
		}
		
		return;
	}
	
	public void sectionA_changeProposalPrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateProposalPrefectureSelectItems();
			this.proposalVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
			this.proposalVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
		}
	}
	
	public void sectionA_changeProposalMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateProposalMunicipalitiesSelectItems();
		}
	}
	
	public void sectionA_changeImplementationPrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateImplementationPrefectureSelectItems();
			this.implementationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
			this.implementationVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
		}
	}
	
	public void sectionA_changeImplementationMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateImplementationMunicipalitiesSelectItems();
		}
	}
	
	public void sectionA_changeOperationPrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateOperationPrefectureSelectItems();
			this.operationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
			this.operationVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
		}
	}
	
	public void sectionA_changeOperationMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			regenerateOperationMunicipalitiesSelectItems();
		}
	}
	
	public void regenerateProposalPrefectureSelectItems() {
		this.proposalVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
		this.proposalVendor_rangePrefectureSelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
	
		Integer regionCode = this.sec_a.getProposalVendor_rangeRegionId();
		System.out.println("regionCode: " + regionCode);
	
		if (regionCode != null) {
			List<Prefecture> allPrefectures = new ArrayList<Prefecture>();
			if (regionCode.equals(-1)) {
				allPrefectures = ubean.getAllPrefectures();
			} else {
				Region r = ubean.getRegionById(regionCode);
				allPrefectures = r.getPrefectures();
			}
			System.out.println("found " + allPrefectures.size() + " prefectures");

			for( Iterator iter = allPrefectures.iterator(); iter.hasNext(); ) {
				Prefecture p = (Prefecture) iter.next();
				SelectItem item = new SelectItem(p.getId(), p.getName());
				this.proposalVendor_rangePrefectureSelectItems.add(item);
			}
		}
	}
	
	public void regenerateProposalMunicipalitiesSelectItems() {
		this.proposalVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
		this.proposalVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
	
		String prefectureCode = this.sec_a.getProposalVendor_rangePrefectureId();
	
		if (prefectureCode != null) {
			List<Municipality> allMunicipalities = new ArrayList<Municipality>();
			if (prefectureCode.equals("-1")) {
				allMunicipalities = ubean.getAllMunicipalitiesByRegionId(this.sec_a.getProposalVendor_rangeRegionId());
			} else {
				Prefecture p = ubean.getPrefectureById(prefectureCode);
				allMunicipalities = p.getMunicipalities();
			}
			System.out.println("found " + allMunicipalities.size() + " municipalities");

			for( Iterator iter = allMunicipalities.iterator(); iter.hasNext(); ) {
				Municipality m = (Municipality) iter.next();
				SelectItem item = new SelectItem(m.getId(), m.getName());
				this.proposalVendor_rangeMunicipalitySelectItems.add(item);
			}
		}
	}
	
	public void regenerateImplementationPrefectureSelectItems() {
		this.implementationVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
		this.implementationVendor_rangePrefectureSelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
	
		Integer regionCode = this.sec_a.getImplementationVendor_rangeRegionId();
		System.out.println("regionCode: " + regionCode);
	
		if (regionCode != null) {
			List<Prefecture> allPrefectures = new ArrayList<Prefecture>();
			if (regionCode.equals(-1)) {
				allPrefectures = ubean.getAllPrefectures();
			} else {
				Region r = ubean.getRegionById(regionCode);
				allPrefectures = r.getPrefectures();
			}
			System.out.println("found " + allPrefectures.size() + " prefectures");

			for( Iterator iter = allPrefectures.iterator(); iter.hasNext(); ) {
				Prefecture p = (Prefecture) iter.next();
				SelectItem item = new SelectItem(p.getId(), p.getName());
				this.implementationVendor_rangePrefectureSelectItems.add(item);
			}
		}
	}
	
	public void regenerateImplementationMunicipalitiesSelectItems() {
		this.implementationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
		this.implementationVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
	
		String prefectureCode = this.sec_a.getImplementationVendor_rangePrefectureId();
	
		if (prefectureCode != null) {
			List<Municipality> allMunicipalities = new ArrayList<Municipality>();
			if (prefectureCode.equals("-1")) {
				allMunicipalities = ubean.getAllMunicipalitiesByRegionId(this.sec_a.getProposalVendor_rangeRegionId());
			} else {
				Prefecture p = ubean.getPrefectureById(prefectureCode);
				allMunicipalities = p.getMunicipalities();
			}
			System.out.println("found " + allMunicipalities.size() + " municipalities");

			for( Iterator iter = allMunicipalities.iterator(); iter.hasNext(); ) {
				Municipality m = (Municipality) iter.next();
				SelectItem item = new SelectItem(m.getId(), m.getName());
				this.implementationVendor_rangeMunicipalitySelectItems.add(item);
			}
		}
	}
	
	public void regenerateOperationPrefectureSelectItems() {
		this.operationVendor_rangePrefectureSelectItems = new ArrayList<SelectItem>();
		this.operationVendor_rangePrefectureSelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
	
		Integer regionCode = this.sec_a.getOperationVendor_rangeRegionId();
		System.out.println("regionCode: " + regionCode);
	
		if (regionCode != null) {
			List<Prefecture> allPrefectures = new ArrayList<Prefecture>();
			if (regionCode.equals(-1)) {
				allPrefectures = ubean.getAllPrefectures();
			} else {
				Region r = ubean.getRegionById(regionCode);
				allPrefectures = r.getPrefectures();
			}
			System.out.println("found " + allPrefectures.size() + " prefectures");

			for( Iterator iter = allPrefectures.iterator(); iter.hasNext(); ) {
				Prefecture p = (Prefecture) iter.next();
				SelectItem item = new SelectItem(p.getId(), p.getName());
				this.operationVendor_rangePrefectureSelectItems.add(item);
			}
		}
	}
	
	public void regenerateOperationMunicipalitiesSelectItems() {
		this.operationVendor_rangeMunicipalitySelectItems = new ArrayList<SelectItem>();
		this.operationVendor_rangeMunicipalitySelectItems.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
	
		String prefectureCode = this.sec_a.getOperationVendor_rangePrefectureId();
		
		if (prefectureCode != null) {
			List<Municipality> allMunicipalities = new ArrayList<Municipality>();
			if (prefectureCode.equals("-1")) {
				allMunicipalities = ubean.getAllMunicipalitiesByRegionId(this.sec_a.getProposalVendor_rangeRegionId());
			} else {
				Prefecture p = ubean.getPrefectureById(prefectureCode);
				allMunicipalities = p.getMunicipalities();
			}
			System.out.println("found " + allMunicipalities.size() + " municipalities");

			for( Iterator iter = allMunicipalities.iterator(); iter.hasNext(); ) {
				Municipality m = (Municipality) iter.next();
				SelectItem item = new SelectItem(m.getId(), m.getName());
				this.operationVendor_rangeMunicipalitySelectItems.add(item);
			}
		}
	}
	
	/* Section B methods */
	public void saveSection_B__SubProjects() {
		for( Iterator iter=this.subProjects.iterator(); iter.hasNext(); ) {
			Section_L prj = (Section_L) iter.next();
			
			if (prj.getId() == null)
				prj = (Section_L) kbean.createRecord( prj );
			else
				kbean.updateRecord(prj);
		}
		
		this.label = "";
	}
	
	public void saveSection_B__ProjectPlaces() {
		for( Iterator iter=this.projectPlaces.iterator(); iter.hasNext(); ) {
			TDE_ProjectPlaces place = (TDE_ProjectPlaces) iter.next();
			place.setSec_b_id(this.sec_b.getId());
			
			if (place.getId() == null) {
				Municipality m = ubean.getMunicipalityById( place.getMunicipalityCode() );
				place.setMunicipality(m);
				place = (TDE_ProjectPlaces) kbean.createRecord( place );
			} else {
				kbean.updateRecord(place);
			}
		}
		
		// calculate the total budget and save the form (data safety requirement)
        updateSection_B_ProjectPlaceBudget();
        
        this.label = "";
	}
	
	public String addSection_B__TDE_SubProject() {
		Section_L tmp_subProject = new Section_L();
		tmp_subProject.setSec_b_id( this.sec_b.getId() );
		tmp_subProject.setGiven_id(Integer.valueOf(this.subProjects.size()+1));
		tmp_subProject.setTitle("νέο υποέργο");
		this.subProjects.add( tmp_subProject );
		
		this.sec_b.setSubProjectsTotal(this.subProjects.size());
		
		this.label = "SubProjectsListLabel";
		
		return "section_b";
	}
	
	
/*	public String addSection_B__TDE_SubProject_old() {
		try {
			Context ctx;
			
			ctx = new InitialContext();
			
			KatartisisBeanLocal kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			
			this.subProjects.add( this.tmp_subProject );
			this.sec_b.setSubProjectsTotal(this.subProjects.size());
			
			this.tmp_subProject.setSec_b_id( this.sec_b.getId() );
			
			this.tmp_subProject = (Section_L) kbean.createRecord( this.tmp_subProject );
			
			// now we need to create the katartisis stuff if they are ok
			if( this.tmp_subProject.isKatartisis() )
				createNewKatartisisSubproject( this.tmp_subProject );
				
			this.tmp_subProject = new Section_L();
			
			goToSectionB();
			
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
		return "section_b";
	}*/

	public String deleteSection_B__TDE_SubProject() {
		Section_L tmp_subProject = (Section_L) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing subProject object: " +tmp_subProject.getTitle() + ", " + tmp_subProject.getId() );
        subProjects.remove(tmp_subProject);
        
        deleteSectionL(tmp_subProject);
        
        this.label = "SubProjectsListLabel";
			        
		return "deleted";
	}
	
	public String addSection_B_TDE_ProjectPlace() {
		TDE_ProjectPlaces place = new TDE_ProjectPlaces();
		place.setRegionCode(-1);
		place.setPrefectureCode("-1");
		place.setMunicipalityCode("-1");
		place.setSec_b_id(this.sec_b.getId());
		this.projectPlaces.add( place );
		
		this.label = "ProjectPlacesListLabel";
		
		return "section_b";
	}
	
	public String deleteSection_B__TDE_ProjectPlace() {
		TDE_ProjectPlaces tmp_obj = (TDE_ProjectPlaces) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing TDE_ProjectPlaces object: " + tmp_obj.getId() );
        projectPlaces.remove(tmp_obj);
        if (tmp_obj.getId() != null) {
        	kbean.deleteRecord(TDE_ProjectPlaces.class, tmp_obj.getId() );
        }

        // calculate the total budget and save the form (data safety requirement)
        updateSection_B_ProjectPlaceBudget();
        
        this.label = "ProjectPlacesListLabel";
		
		return "deleted";
	}
	
	public void updateSection_B_ProjectPlaceBudget() {
		BigDecimal tmp_budget = BigDecimal.ZERO;

		try {
			TDE_ProjectPlaces tmp_obj = (TDE_ProjectPlaces) this.tmp_dataTable2.getRowData();
			System.out.println("found TDE_ProjectPlaces object" +  tmp_obj.getMunicipalityCode());

			tmp_budget = this.sec_b.getProjectsPlacesTotalBudget().subtract(this.projectPlaces.get(this.tmp_dataTable2.getRowIndex()).getBudget());

			if (tmp_budget.add(tmp_obj.getBudget()).compareTo(this.sec_a.getAmount()) > 0) {
				tmp_obj.setBudget(this.sec_a.getAmount().subtract(tmp_budget));
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπερβήκατε τον προυπολογισμό του έργου. Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
				this.saveable.put(Section_B.class, false);
			}
		} catch (java.lang.IllegalArgumentException e) {
		}

		tmp_budget = BigDecimal.ZERO;
		for( Iterator iter=this.projectPlaces.iterator(); iter.hasNext(); ) {
			TDE_ProjectPlaces tmp = (TDE_ProjectPlaces) iter.next();
			tmp_budget = tmp_budget.add(tmp.getBudget());
		}

		if (tmp_budget.compareTo(this.sec_a.getAmount()) > 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπερβήκατε τον προυπολογισμό του έργου. Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
			this.saveable.put(Section_B.class, false);
		} else {
			this.saveable.put(Section_B.class, true);
		}

		this.sec_b.setProjectsPlacesTotalBudget( tmp_budget );
	}
	
	public void sectionB_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		this.label = "ProjectPlacesListLabel";
		return;
	}
	
	public void sectionB_changeMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		TDE_ProjectPlaces placeItem = (TDE_ProjectPlaces) this.tmp_dataTable2.getRowData();
		

		if (!placeItem.getMunicipalityCode().equals("-1")) {
			Municipality m = ubean.getMunicipalityById(placeItem.getMunicipalityCode());
			placeItem.setMunicipality(m);
		} else {
			placeItem.setMunicipalityCode("-1");
		}
		
		this.label = "ProjectPlacesListLabel";
        
		return;
	}
	
	/* Section C methods */
	public void sectionC_changedProgramme(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUProgramme prog = (EUProgramme) kbean.getRecordById(this.sec_c.getProjectOther_programme_id(), EUProgramme.class);
			System.out.println("prog.id = " + this.sec_c.getProjectOther_programme_id() + ", prog = " + prog);
			if (prog != null) {
				this.programme = prog;
				this.sec_c.setProjectOther_programme(this.programme.getName());
				this.sec_c.setProjectOther_programme_id(this.programme.getId());
			}
		}
		
		return;
	}
	
	public void sectionC_changeVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDE_Vendors v = (TDE_Vendors) kbean.getRecordById(this.sec_c.getProjectOther_implementationVendor_id(), TDE_Vendors.class);
			this.sec_c.setProjectOther_implementationVendor(v.getName());
		}
        
		return;
	}
	
	/* Section D methods */
	public void saveSection_D__DeiktesEkrois() {
		// Do Deiktes Ekrois
		for( SectionD_Deiktis deiktis: this.deiktes_ekrois) {
			deiktis.setSec_d_id(this.sec_d.getId());

			if (deiktis.getId() == null) {
				deiktis = (SectionD_Deiktis) kbean.createRecord( deiktis );
			} else
				kbean.updateRecord(deiktis);
		}
		
		this.label = "";
	}
	
	public void saveSection_D__DeiktesApotelesmatos() {			
		// Do Deiktes Apotelesmatos
		for( SectionD_Deiktis deiktis: this.deiktes_apotelesmatos) {
			deiktis.setSec_d_id(this.sec_d.getId());

			if (deiktis.getId() == null) {
				deiktis = (SectionD_Deiktis) kbean.createRecord( deiktis );
			} else
				kbean.updateRecord(deiktis);
		}
		
		this.label = "";
	}
	
	public void saveSection_D__DeiktesEpiptoseon() {			
		// Do Deiktes Epiptoseon
		for( SectionD_Deiktis deiktis: this.deiktes_epiptoseon) {
			deiktis.setSec_d_id(this.sec_d.getId());

			if (deiktis.getId() == null) {
				deiktis = (SectionD_Deiktis) kbean.createRecord( deiktis );
			} else
				kbean.updateRecord(deiktis);
		}
		
		this.label = "";
	}
	
	public void sectionD_changedDeiktiEkrois(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
		}
		
		this.label = "";
        
		return;
	}
	
	public void sectionD_changedDeiktiApotelesmatos(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
		}
		
		this.label = "IndicesResultsListLabel";
        
		return;
	}
	
	public void sectionD_changedDeiktiEpiptoseon(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
		}
		
		this.label = "IndicesEffectsListLabel";
        
		return;
	}
	
	public String addSection_D_TDE_Deiktes_Ekrois() {
		SectionD_Deiktis tmp_deiktis = new SectionD_Deiktis();
		tmp_deiktis.setType( DeiktesType.EKROIS );
		deiktes_ekrois.add(tmp_deiktis);
		
		this.label = "";
			
		return "section_d";
	}
	
	public String addSection_D_TDE_Deiktes_Apotelesmatos() {
		SectionD_Deiktis tmp_deiktis = new SectionD_Deiktis();
		tmp_deiktis.setType( DeiktesType.APOTELESMATOS );
		deiktes_apotelesmatos.add(tmp_deiktis);
		
		this.label = "IndicesResultsListLabel";
		
		return "section_d";
	}
	
	public String addSection_D_TDE_Deiktes_Epiptoseon() {
		SectionD_Deiktis tmp_deiktis = new SectionD_Deiktis();	
		tmp_deiktis.setType( DeiktesType.EPIPTOSEON );
		deiktes_epiptoseon.add(tmp_deiktis);
		
		this.label = "IndicesEffectsListLabel";
				
		return "section_d";
	}
	
	public String deleteSection_D_TDE_Deiktes_Ekrois() {
		TDE_Deiktes tmp_deiktis = (TDE_Deiktes) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Deiktis object: " +tmp_deiktis.getName() + ", " + tmp_deiktis.getId() );
		this.deiktes_ekrois.remove(tmp_deiktis);
		
		if (tmp_deiktis.getId() != null) {
			// now remove the object
			kbean.deleteRecord(TDE_Deiktes.class, tmp_deiktis.getId() );
        }
		
		this.label = "";
                
        return "section_d";
	}
	
	public String deleteSection_D_TDE_Deiktes_Apotelesmatos() {
		TDE_Deiktes tmp_deiktis = (TDE_Deiktes) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing TDE_Deiktis object: " +tmp_deiktis.getName() + ", " + tmp_deiktis.getId() );
		this.deiktes_apotelesmatos.remove(tmp_deiktis);
		
		if (tmp_deiktis.getId() != null) {
			// now remove the object
			kbean.deleteRecord(TDE_Deiktes.class, tmp_deiktis.getId() );
		}
		
		this.label = "IndicesResultsListLabel";
        
        return "section_d";
	}
	
	public String deleteSection_D_TDE_Deiktes_Epiptoseon() {
		TDE_Deiktes tmp_deiktis = (TDE_Deiktes) this.tmp_dataTable3.getRowData();
        
		System.out.println( "removing TDE_Deiktis object: " +tmp_deiktis.getName() + ", " + tmp_deiktis.getId() );
		this.deiktes_epiptoseon.remove(tmp_deiktis);
		
		if (tmp_deiktis.getId() != null) {        		
			// now remove the object
			kbean.deleteRecord(TDE_Deiktes.class, tmp_deiktis.getId() );
        }
		
		this.label = "IndicesEffectsListLabel";
        
        return "section_d";
	}
	
	/* Section E methods */
	public void saveSection_E__Permissions() {
		this.saveable.put(Section_E.class, true);
		
		for( Iterator iter=this.permissions.iterator(); iter.hasNext(); ) {
			TDE_Permissions tmp_obj = (TDE_Permissions) iter.next();
			
			if ( (tmp_obj.getSubmitted() != null && tmp_obj.getAnticipatedAcceptance() != null 
				&& tmp_obj.getSubmitted().after(tmp_obj.getAnticipatedAcceptance()) )
			|| (tmp_obj.getSubmitted() != null && tmp_obj.getActualAcceptance() != null 
				&& tmp_obj.getSubmitted().after(tmp_obj.getActualAcceptance()) ) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προσθήκη Αδειας/Έγκρισης Έργου: Η ημ/νια υποβολής δεν μπορεί να είναι " +
					"μεταγενέστερη από την προβλεπόμενη/πραγματική ημ/νια έγκρισης.  Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
				this.saveable.put(Section_E.class, false);
				return;
			}
		}
		
		if (this.saveable.get(Section_E.class) == true) {
			for( Iterator iter=this.permissions.iterator(); iter.hasNext(); ) {
				TDE_Permissions tmp_obj = (TDE_Permissions) iter.next();
				tmp_obj.setSec_e_id(this.sec_e.getId());

				if (tmp_obj.getId() == null) {
					tmp_obj = (TDE_Permissions) kbean.createRecord( tmp_obj );
				} else
					kbean.updateRecord(tmp_obj);
			}
		}
	}
	
	public void saveSection_E__Tasks() {
		this.saveable.put(Section_E.class, true);
		
		for( Iterator iter=this.tasks.iterator(); iter.hasNext(); ) {
			TDE_Tasks tmp_obj = (TDE_Tasks) iter.next();
			
			if (tmp_obj.getVendor_id().compareTo(-1) == 0) {
				this.saveable.put(Section_E.class, false);
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προσθήκη Ενέργειας Τρίτου: Πρέπει να επιλέξετε Φορέα!") );
			}
			
			if ( tmp_obj.getStartDate() != null && tmp_obj.getEndDate() != null && 
					tmp_obj.getStartDate().after(tmp_obj.getEndDate()) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προσθήκη Ενέργειας Τρίτου: Η ημ/νια έναρξης δεν μπορεί να είναι μεταγενέστερη από την ημ/νια λήξης.") );
				this.saveable.put(Section_E.class, false);
			}
		}
		
		
		if (this.saveable.get(Section_E.class) == true) {
			for( Iterator iter=this.tasks.iterator(); iter.hasNext(); ) {
				TDE_Tasks tmp_obj = (TDE_Tasks) iter.next();
				tmp_obj.setSec_e_id(this.sec_e.getId());

				if (tmp_obj.getId() == null) {
					tmp_obj = (TDE_Tasks) kbean.createRecord( tmp_obj );
				} else
					kbean.updateRecord(tmp_obj);
			}
		}
	}
	
	public void saveSection_E__VendorTasks() {
		this.saveable.put(Section_E.class, true);
		
		for( Iterator iter=this.vendorTasks.iterator(); iter.hasNext(); ) {
			TDE_VendorTasks tmp_obj = (TDE_VendorTasks) iter.next();
			
			if ( tmp_obj.getStartDate() != null && tmp_obj.getEndDate() != null && 
					tmp_obj.getStartDate().after(tmp_obj.getEndDate()) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προσθήκη Ενέργειας Φορέα Υλοποίησης Έργου: Η ημ/νια έναρξης δεν μπορεί να είναι " +
					"μεταγενέστερη από την ημ/νια λήξης.  Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
				this.saveable.put(Section_E.class, false);
			}
		}
		
		if (this.saveable.get(Section_E.class) == true) {
			for( Iterator iter=this.vendorTasks.iterator(); iter.hasNext(); ) {
				TDE_VendorTasks tmp_obj = (TDE_VendorTasks) iter.next();
				tmp_obj.setSec_e_id(this.sec_e.getId());

				if (tmp_obj.getId() == null) {
					tmp_obj = (TDE_VendorTasks) kbean.createRecord( tmp_obj );
				} else
					kbean.updateRecord(tmp_obj);
			}
		}
		
		this.label = "";
	}
	
	public void saveSection_E__Analysis() {
		for( Iterator iter=this.analysis.iterator(); iter.hasNext(); ) {
			TDE_Analysis tmp_obj = (TDE_Analysis) iter.next();
			tmp_obj.setSec_e_id(this.sec_e.getId());

			if (tmp_obj.getId() == null) {
				tmp_obj = (TDE_Analysis) kbean.createRecord( tmp_obj );
			} else
				kbean.updateRecord(tmp_obj);
		}
		
		this.label = "";
		
	}
	
	public void saveSection_E__Katartisis() {
		for( Iterator iter=this.katartisis.iterator(); iter.hasNext(); ) {
			TDE_Katartisis tmp_obj = (TDE_Katartisis) iter.next();
			tmp_obj.setSec_e_id(this.sec_e.getId());

			if (tmp_obj.getId() == null) {
				tmp_obj = (TDE_Katartisis) kbean.createRecord( tmp_obj );
			} else
				kbean.updateRecord(tmp_obj);
		}
		
		this.label = "";
	}
	
	public void sectionE_changeVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_e.getCertificationVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_e.setCertificationVendorCode(vendor.getCode());
			this.sec_e.setCertificationVendor(vendor.getName());
		}
		
		this.label = "";
        
		return;
	}
	
	public void sectionE_changeTasksVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDE_Tasks taskItem = (TDE_Tasks) this.tmp_dataTable5.getRowData();
			TDE_Vendors v = (TDE_Vendors) kbean.getRecordById(taskItem.getVendor_id(), TDE_Vendors.class);
			taskItem.setVendor(v.getName());
			taskItem.setCode(v.getCode());
		}
		
		this.label = "VendorsListLabel";
        
		return;
	}
	
	public String addSection_E__TDE_Permissions() {
		TDE_Permissions tmp_permission = new TDE_Permissions();
		tmp_permission.setType("νέα άδεια");
		this.permissions.add(tmp_permission);
		
		this.label = "";
		
		return "section_e";
	}
	
	public String addSection_E__TDE_Analysis() {
		TDE_Analysis ta = new TDE_Analysis();
		ta.setType("νέα μελέτη");
		ta.setExpiration(this.sec_a.getDate_end());
		ta.setRequired("ΔΕΝ ΑΠΑΙΤΕΙΤΑΙ");
		ta.setStatus("ΔΕΝ ΥΠΑΡΧΕΙ");
		ta.setComments(new String(""));
		this.analysis.add(ta);
		
		System.out.println("TDE_Analysis: " + ta.getId() + ", status: " + ta.getStatus() + 
				", required: " + ta.getRequired() + ", expires: " + ta.getExpiration().toString() + ", comments: " + ta.getComments());
		
		this.label = "AnalysisListLabel";
					
		return "section_e";
	}
	
	public String addSection_E__TDE_Katartisis() {
		TDE_Katartisis tk = new TDE_Katartisis();
		tk.setType("νέα καταχώριση");
		tk.setRequired("ΔΕΝ ΑΠΑΙΤΕΙΤΑΙ");
		tk.setStatus("ΔΕΝ ΥΠΑΡΧΕΙ");
		
		this.katartisis.add(tk);
		
		this.label = "KatartisisListLabel";
		
		return "section_e";
	}
	
	public String addSection_E__TDE_Tasks() {
		TDE_Tasks tmp_task = new TDE_Tasks();
		tmp_task.setDescription("νέα ενέργεια");
		this.tasks.add(tmp_task);
		
		this.label = "TasksListLabel";
				
		return "section_e";
	}	
	
	public String addSection_E__TDE_VendorTasks() {
		TDE_VendorTasks tmp_vendorTask = new TDE_VendorTasks();
		tmp_vendorTask.setDescription("νέα ενέργεια");
		this.vendorTasks.add(tmp_vendorTask);
		
		this.label = "VendorTasksListLabel";
		
		return "section_e";
	}
	
	public String deleteSection_E__TDE_Permissions() {
		TDE_Permissions tmp_permissions = (TDE_Permissions) this.tmp_dataTable.getRowData();
	       
		System.out.println( "removing TDE_Permissions object: " +tmp_permissions.getType() + ", " + tmp_permissions.getId() );
		this.permissions.remove(tmp_permissions);
		
		if (tmp_permissions.getId() != null) {
			// now remove the object
			kbean.deleteRecord(TDE_Permissions.class, tmp_permissions.getId() );
		}
		
		this.label = "";
	        
		return "section_e";
	}
	
	public String deleteSection_E__TDE_Analysis() {
		TDE_Analysis tmp_analysis = (TDE_Analysis) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing TDE_Analysis object: " +tmp_analysis.getType() + ", " + tmp_analysis.getId() );
		this.analysis.remove(tmp_analysis);
		
		if (tmp_analysis.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDE_Analysis.class, tmp_analysis.getId() );
        }
		
		this.label = "AnalysisListLabel";
        
        return "section_e";
	}
	
	public String deleteSection_E__TDE_Katartisis() {
		TDE_Katartisis tmp_katartisis = (TDE_Katartisis) this.tmp_dataTable3.getRowData();
	       
		System.out.println( "removing TDE_Katartisis object: " +tmp_katartisis.getType() + ", " + tmp_katartisis.getId() );
		this.katartisis.remove(tmp_katartisis);
		
		if (tmp_katartisis.getId() != null) {
			// now remove the object
			kbean.deleteRecord(TDE_Katartisis.class, tmp_katartisis.getId() );
		}
		
		this.label = "KatartisisListLabel";
	        
		return "section_e";
	}
	
	public String deleteSection_E__TDE_Tasks() {
		TDE_Tasks tmp_task = (TDE_Tasks) this.tmp_dataTable5.getRowData();
	       
		System.out.println( "removing TDE_Tasks object: " +tmp_task.getDescription() + ", " + tmp_task.getId() );
		this.tasks.remove(tmp_task);
		
		if (tmp_task.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDE_Tasks.class, tmp_task.getId() );
		}
		
		this.label = "TasksListLabel";
	        
		return "section_e";
	}
	
	public String deleteSection_E__TDE_VendorTasks() {
		TDE_VendorTasks tmp_task = (TDE_VendorTasks) this.tmp_dataTable4.getRowData();
	       
		System.out.println( "removing TDE_VendorTasks object: " +tmp_task.getDescription() + ", " + tmp_task.getId() );
		this.tasks.remove(tmp_task);
		
		if (tmp_task.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDE_VendorTasks.class, tmp_task.getId() );
		}
		
		this.label = "VendorTasksListLabel";
	        
		return "section_e";
	}
	
	/* Section Th methods */
	public void saveSection_Th__Document() {
		for( Iterator iter=this.sec_th.getDocuments().iterator(); iter.hasNext(); ) {
			DocumentEntry tmp_obj = (DocumentEntry) iter.next();

			if (tmp_obj.getId() == null) {
				tmp_obj = (DocumentEntry) kbean.createRecord( tmp_obj );
			} else
				kbean.updateRecord(tmp_obj);
		}
		
		this.label = "";
	}
	
	public String addSection_Th__Document() {
		this.sec_th.add(new DocumentEntry("Νέο Έγγραφο", "Περιγραφή"));

		return "section_th";
	}	
	
	public String deleteSection_Th__Document() {
		DocumentEntry entry = (DocumentEntry) this.tmp_dataTable.getRowData();

		this.sec_th.remove(entry);
		        
		return "section_th";
	}
	
	/* Section I methods */
	public void sectionI_changeImplementationVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			Integer id = this.sec_i.getImplementationVendor_id();
			TDE_Vendors vendor = (TDE_Vendors) kbean.getRecordById(id, TDE_Vendors.class);
			this.sec_i.setImplementationVendor_code(vendor.getCode());
			this.sec_i.setImplementationVendor_name(vendor.getName());
		}
		
		this.label = "";
        
		return;
	}
	
	public void saveSection_I__Employees() {
		// Do Employees
		for( Iterator iter=this.section_I_employees.iterator(); iter.hasNext(); ) {
			TDE_Employees employees = (TDE_Employees) iter.next();

			if (employees.getId() == null) {
				employees = (TDE_Employees) kbean.createRecord( employees );
				// create the connector
				Section_I__TDE_Employees tmp_conn = new Section_I__TDE_Employees();
				tmp_conn.setSection_id(this.sec_b.getId());
				tmp_conn.setTde_id(employees.getId());
				tmp_conn = (Section_I__TDE_Employees) kbean.createRecord( tmp_conn );
			} else
				kbean.updateRecord(employees);
		}
		
		this.label = "";
		
		updateSection_I_Counters();
	}
	
	public String addSection_I__TDE_Employees() {
		TDE_Employees tmp_employee = new TDE_Employees();
		tmp_employee.setName("νέα ειδικότητα");
		tmp_employee.setCurrentQty(0);
		tmp_employee.setEstQty(0);
		section_I_employees.add(tmp_employee);
		updateSection_I_Counters();
		
		this.label = "VendorEmployeesIListLabel";
		
		return "section_i";		
	}
	
	public void updateSection_I_Counters() {
		this.section_I_employees_currQty = 0;
		this.section_I_employees_estQty = 0;
		
		for( Iterator iter = this.section_I_employees.iterator(); iter.hasNext(); ) {
			TDE_Employees tmp = (TDE_Employees) iter.next();
			
			this.section_I_employees_currQty += tmp.getCurrentQty();
			this.section_I_employees_estQty += tmp.getEstQty();
		}
	}
	
	public String deleteSection_I__TDE_Employees() {
		TDE_Employees tmp_employee = (TDE_Employees) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Employees object: " +tmp_employee.getName() + ", " + tmp_employee.getId() );
		this.section_I_employees.remove(tmp_employee);
		
		if (tmp_employee.getId() != null) {
			// also remove connectors, since we delete this employees, WHOEVER CONNECTOR REFERENCES IT, GETS DELETED
			kbean.deleteConnectorsByObjectId(Section_I__TDE_Employees.class, tmp_employee.getId());

			// now remove the object
			kbean.deleteRecord( TDE_Employees.class, tmp_employee.getId() );
        }
		
		this.label = "VendorEmployeesIListLabel";
        
        return "section_i";	
	}
	
	/* Section K methods */
	public void saveSection_K__Employees() {
		// Do Employees
		for( Iterator iter=this.section_K_employees.iterator(); iter.hasNext(); ) {
			TDE_Employees employees = (TDE_Employees) iter.next();

			if (employees.getId() == null) {
				employees = (TDE_Employees) kbean.createRecord( employees );
				// create the connector
				Section_K__TDE_Employees tmp_conn = new Section_K__TDE_Employees();
				tmp_conn.setSection_id(this.sec_b.getId());
				tmp_conn.setTde_id(employees.getId());
				tmp_conn = (Section_K__TDE_Employees) kbean.createRecord( tmp_conn );
			} else
				kbean.updateRecord(employees);
		}
		this.label = "";
		
		updateSection_K_Counters();
	}
	
	public String addSection_K__TDE_Employees() {
		TDE_Employees tmp_employee = new TDE_Employees();
		tmp_employee.setName("νέα ειδικότητα");
		tmp_employee.setCurrentQty(0);
		tmp_employee.setEstQty(0);
		section_K_employees.add(tmp_employee);
		updateSection_K_Counters();
		
		this.label = "VendorEmployeesKListLabel";
		
		return "section_k";		
	}
	
	public String deleteSection_K__TDE_Employees() {
		TDE_Employees tmp_employee = (TDE_Employees) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Employees object: " +tmp_employee.getName() + ", " + tmp_employee.getId() );
		this.section_K_employees.remove(tmp_employee);
		
		if (tmp_employee.getId() != null) {
			// also remove connectors, since we delete this employees, WHOEVER CONNECTOR REFERENCES IT, GETS DELETED
			kbean.deleteConnectorsByObjectId(Section_K__TDE_Employees.class, tmp_employee.getId());

			// now remove the object
			kbean.deleteRecord( TDE_Employees.class, tmp_employee.getId() );
        }
		
		this.label = "VendorEmployeesKListLabel";
        
        return "section_k";	
	}
	
	public void updateSection_K_Counters() {
		this.section_K_employees_currQty = 0;
		this.section_K_employees_estQty = 0;
		
		for( Iterator iter = this.section_K_employees.iterator(); iter.hasNext(); ) {
			TDE_Employees tmp = (TDE_Employees) iter.next();
			
			this.section_K_employees_currQty += tmp.getCurrentQty();
			this.section_K_employees_estQty += tmp.getEstQty();
		}
	}
	
	/* Section L methods */
	
	public void saveSection_L_TDY_ProjectPlaces() {
		// Tidy up budgets. If the tdyplace budget is greater than the 
		// remaining budget in the projectplace object, then the tdyplace budget
		// is adjusted accordingly.
		// TODO: we need to show the problematic place.
		this.saveable.put(Section_L.class, true);
		for( Iterator miter=this.tdy_places.iterator(); miter.hasNext(); ) {
			TDY_Places tdy_place = (TDY_Places) miter.next();

			
			for( Iterator iter=this.projectPlaces.iterator(); iter.hasNext(); ) {
				TDE_ProjectPlaces place = (TDE_ProjectPlaces) iter.next();

				if (tdy_place.getRegionCode().equals(place.getRegionCode()) ||
						place.getRegionCode().equals(-1)) {
					if (tdy_place.getPrefectureCode().equals(place.getPrefectureCode()) ||
							place.getPrefectureCode().equals("-1")) {
						if (tdy_place.getMunicipalityCode().equals(place.getMunicipalityCode()) ||
								place.getMunicipalityCode().equals("-1")) {
							
							System.out.println("saveSection_L_TDY_ProjectPlaces: TDE " + place.getRegionCode()
									+ ", " + place.getPrefectureCode() + ", " + place.getMunicipalityCode()
									+ ", budget = " + place.getBudget() + ", rem = " + place.getRemainingBudget(this.sec_l.getId())); 
							System.out.println("saveSection_L_TDY_ProjectPlaces: TDY " + tdy_place.getRegionCode()
									+ ", " + tdy_place.getPrefectureCode() + ", " + tdy_place.getMunicipalityCode()
									+ ", budget = " + tdy_place.getBudget() ); 
							if (tdy_place.getBudget().compareTo(place.getRemainingBudget(this.sec_l.getId())) > 0) {
								FacesContext.getCurrentInstance().addMessage("", new FacesMessage(tdy_place.getMunicipalityName() +": Η χωροθέτηση υπερέβη τον διαθέσιμο προυπολογισμό. Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
								this.saveable.put(Section_L.class, false);
							}
						}
					}
				}
			}

			if (this.saveable.get(Section_L.class) == true) {
				// Save/update tdyplace objects
				if (tdy_place.getId() == null) {
					Municipality m = ubean.getMunicipalityById( tdy_place.getMunicipalityCode() );
					tdy_place.setMunicipality(m);
					tdy_place = (TDY_Places) kbean.createRecord( tdy_place );
				} else {
					kbean.updateRecord(tdy_place);
				}
			}
		}
		
		this.label = "";
	}
	
	public void saveSection_L_TDY_Progress() {
		for( TDY_Progress progress: this.tdy_progress) {
			if (progress.getId() == null)
				progress = (TDY_Progress) kbean.createRecord( progress );
			else
				kbean.updateRecord(progress);
		}
		
		this.label = "";
	}
	
	public void saveSection_L_TDY_ProgressActivities() {
		// Check dates
		this.saveable.put(Section_L.class, true);
		for( TDY_ProgressActivities tmp_obj: this.tdy_progressActivities) {
			if( tmp_obj.getStartDate() != null && tmp_obj.getEndDate() != null && tmp_obj.getStartDate().after( tmp_obj.getEndDate() ) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Προγραμματισμός Προόδου Δραστηριοτήτων : "
						+ tmp_obj.getActivity() + " : H ημ/νια έναρξης δεν μπορεί να είναι μεταγενέστερη από την ημ/νια λήξης.") );
				this.saveable.put(Section_L.class, false);
			}
		}
		
		if (this.saveable.get(Section_L.class) == true) {
			for( Iterator iter=this.tdy_progressActivities.iterator(); iter.hasNext(); ) {
				TDY_ProgressActivities progressActivity = (TDY_ProgressActivities) iter.next();

				if (progressActivity.getId() == null)
					progressActivity = (TDY_ProgressActivities) kbean.createRecord( progressActivity );
				else
					kbean.updateRecord(progressActivity);
			}
		}
		
		this.label = "";
	}
	
	public void saveSection_L_TDY_Deiktes() {
		for( Iterator iter=this.tdy_deiktes.iterator(); iter.hasNext(); ) {
			TDY_Deiktes deiktis = (TDY_Deiktes) iter.next();

			if (deiktis.getId() == null)
				deiktis = (TDY_Deiktes) kbean.createRecord( deiktis );
			else
				kbean.updateRecord(deiktis);
		}
		
		this.label = "";
	}
	
	public void saveSection_L_TDY_Financial() {
		for( Iterator iter=this.tdy_financial.iterator(); iter.hasNext(); ) {
			TDY_Financial financial = (TDY_Financial) iter.next();
			
			System.out.println("financial.id: " +  financial.getId() );
			System.out.println("financial.code: " +  financial.getCode() );
			System.out.println("financial.framework: " +  financial.getFramework() );
			System.out.println("financial.framework.id: " +  financial.getFramework().getId() );
			
			if( financial.getFramework() == null || 
					(financial.getFramework() != null && financial.getFramework().getId() == null)) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Επιλέξιμη Δαπάνη: " 
					+ ": Πρέπει να επιλέξετε μια δαπάνη πριν αποθηκεύσετε το ΤΔΥ. Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
				this.saveable.put(Section_L.class, false);
				return;
			}
			
			if (financial.getId() == null)
				financial = (TDY_Financial) kbean.createRecord( financial );
			else
				kbean.updateRecord(financial);
		}
		
		updateSectionL_budget();
		
		this.label = "";
	}
	
	public void saveSection_L_TDY_Evolution() {
		/* TODO: markos: Commented out till we figure out if we include this validation check or not
		for( TDY_Evolution tmp_obj : this.tdy_evolution) {
			if (this.sec_a.getDate_start() != null) {
				if( tmp_obj.getDate_initial() != null && tmp_obj.getDate_initial().before( this.sec_a.getDate_start())) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Διοικητική Κατάσταση Εξέλιξης Υποέργου: " 
						+ tmp_obj.getDescription() + ": H ημ/νια αρχικού προγραμματισμού δεν μπορεί να είναι προγενέστερη από την ημ/νία έναρξης του έργου: "
						+ DateUtil.cts(this.sec_a.getDate_start())) );
					this.saveable.put(Section_L.class, false);
					return;
				}
				if( tmp_obj.getDate_real() != null && tmp_obj.getDate_real().before( this.sec_a.getDate_start())) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Διοικητική Κατάσταση Εξέλιξης Υποέργου: " 
						+ tmp_obj.getDescription() + ": H πραγματική ημ/νια δεν μπορεί να είναι προγενέστερη από την ημ/νία έναρξης του έργου: "
						+ DateUtil.cts(this.sec_a.getDate_start())) );
					this.saveable.put(Section_L.class, false);
					return;
				}
			}
			if (this.sec_a.getDate_end() != null) {
				if( tmp_obj.getDate_initial() != null && tmp_obj.getDate_initial().after( this.sec_a.getDate_end())) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Διοικητική Κατάσταση Εξέλιξης Υποέργου: " 
						+ tmp_obj.getDescription() + ": H ημ/νια αρχικού προγραμματισμού δεν μπορεί να είναι μεταγενέστερη από την ημ/νία λήξης του έργου: "
						+ DateUtil.cts(this.sec_a.getDate_end())) );
					this.saveable.put(Section_L.class, false);
					return;
				}
				if( tmp_obj.getDate_real() != null && tmp_obj.getDate_real().after( this.sec_a.getDate_end())) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Διοικητική Κατάσταση Εξέλιξης Υποέργου: " 
						+ tmp_obj.getDescription() + ": H πραγματική ημ/νια δεν μπορεί να είναι προγενέστερη από την ημ/νία λήξης του έργου: "
						+ DateUtil.cts(this.sec_a.getDate_end())) );
					this.saveable.put(Section_L.class, false);
					return;
				}
			}
		}*/
		
		for( Iterator iter=this.tdy_evolution.iterator(); iter.hasNext(); ) {
			TDY_Evolution evolution = (TDY_Evolution) iter.next();

			if (evolution.getId() == null)
				evolution = (TDY_Evolution) kbean.createRecord( evolution );
			else
				kbean.updateRecord(evolution);
		}
		
		this.label = "";
	}
	
	public String addSection_L_TDY_ProjectPlaces() {
		TDY_Places place = new TDY_Places();
		if (this.projectPlaces.size() > 0) {
			place.setRegionCode(this.projectPlaces.get(0).getRegionCode());
			place.setPrefectureCode(this.projectPlaces.get(0).getPrefectureCode());
			place.setMunicipalityCode(this.projectPlaces.get(0).getMunicipalityCode());
			place.setSec_b_id(this.sec_b.getId());
			place.setSec_l_id(this.sec_l.getId());
			this.tdy_places.add( place );
			
			this.label = "PlacesListLabel";
		} else {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να ορίσετε χωροθέτηση για το έργο πρώτα!"));
			this.label = "";
		}
		
		return "section_l";
	}
	
	public void updateContractorSectionL(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if ( this.sec_l.getContractorId().intValue() > 0 )
				this.contractor = (Contractor) kbean.getRecordById(this.sec_l.getContractorId(), Contractor.class );
		}
	}
	
	public void sectionL_changedActionCategory(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			sectionL_regenerateTDYProgressSelectItems();
		}
		
		return;
	}
	
	public void sectionL_changeSupervisingVendor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDE_Vendors v = (TDE_Vendors) kbean.getRecordById(this.vendor.getId(), TDE_Vendors.class);
			this.sec_l.setSupervisingVendor(v.getName());
			this.sec_l.setSupervisingVendor_code(v.getCode());
		}
        
		return;
	}
	
	public void sectionL_changePrefecture(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		}
		
		this.label = "PlacesListLabel";
		
		return;
	}
	
	public void sectionL_changeMunicipality(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDY_Places placeItem = (TDY_Places) this.tmp_dataTable.getRowData();
		
			System.out.println("sectionL_changeMunicipality: prefecture:" + placeItem.getPrefectureCode());
		
			if (!placeItem.getMunicipalityCode().equals("-1")) {
				Municipality m = ubean.getMunicipalityById(placeItem.getMunicipalityCode());
				placeItem.setMunicipality(m);
			}
		}
        
		this.label = "PlacesListLabel";
		
		return;
	}
	
	public void sectionL_changedProgress(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDY_Progress v1 = (TDY_Progress) this.tmp_dataTable2.getRowData();
			TDY_Progress v2 = (TDY_Progress) kbean.getRecordById(v1.getId2(), TDY_Progress.class);
			v1.setCode(v2.getCode());
			v1.setDescription(v2.getDescription());
			v1.setMeasuringUnit(v2.getMeasuringUnit());
		}
		
		this.label = "ProgressListLabel";
        
		return;
	}
	
	public void sectionL_changedExpense(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDY_Financial v1 = (TDY_Financial) this.tmp_dataTable5.getRowData();
			TDY_Financial v2 = (TDY_Financial) kbean.getRecordById(v1.getId2(), TDY_Financial.class);
			v1.setCode(v2.getCode());
			v1.setCategory(v2.getCategory());
			v1.setFramework(v2.getFramework());
			updateSectionL_budget();
		}
		
		this.label = "FinancialListLabel";
        
		return;
	}
	
	public void sectionL_changedEvolution(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDY_Evolution v1 = (TDY_Evolution) this.tmp_dataTable6.getRowData();
			TDY_Evolution v2 = (TDY_Evolution) kbean.getRecordById(v1.getId2(), TDY_Evolution.class);
			v1.setCode(v2.getCode());
			v1.setDescription(v2.getDescription());
		}
		
		this.label = "EvolutionListLabel";
        
		return;
	}
		
	public void sectionL_changedSubprojectType(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			this.sec_l.setSubproject_type_code(this.sec_l.getSubproject_type().getCode());
			this.sec_l.setSubproject_type_description(this.sec_l.getSubproject_type().getType());
			regenerateEvolutionSelectItems();
		}
		
		this.label = "EvolutionListLabel";
	}
	
	public void regenerateEvolutionSelectItems() {
		this.defaultEvolutionSelectItemsList = new ArrayList<SelectItem>();
		this.defaultEvolutionSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		
		if (this.sec_l.getSubproject_type() != null) {
			List<TDY_Evolution> defaultEvolutionList = kbean.getEvolutionBySectionLIdSubProjectType(this.configId, this.sec_l.getSubproject_type());
			for (TDY_Evolution t : defaultEvolutionList) {
				SelectItem s = new SelectItem(t.getId(), t.getCode()+": "+t.getDescription());
				this.defaultEvolutionSelectItemsList.add(s);
			}
		}
	}
	
	public String addSection_L_TDY_Progress() {
		TDY_Progress progress = new TDY_Progress();
		progress.setDescription("νέο διακριτό τμήμα Φ.Α.");
		progress.setSec_l_id(this.sec_l.getId());
		this.tdy_progress.add( progress );
		
		this.label = "ProgressListLabel";
		
		return "section_l";
	}
	
	public String addSection_L__TDY_ProgressActivities() {		
		TDY_ProgressActivities progressActivity = new TDY_ProgressActivities();
		progressActivity.setActivity("νέο δραστηριότητα");
		progressActivity.setSec_l_id(this.sec_l.getId());
		this.tdy_progressActivities.add( progressActivity );
		
		this.label = "ProgressActivitiesListLabel";
		
		return "section_l";
	}
	
	public void copySection_L_TDY_Deiktes() {
		// Copy Deiktes ekrois to the TDY
		this.deiktes_ekrois = kbean.getDeiktesBySectionDId(this.sec_d.getId(), DeiktesType.EKROIS);

		for( SectionD_Deiktis deiktis: this.deiktes_ekrois) {
			TDY_Deiktes tdy_deiktis = new TDY_Deiktes();
			tdy_deiktis.setId(null);
			tdy_deiktis.setName(deiktis.getDeiktis().getName());
			tdy_deiktis.setCode(deiktis.getDeiktis().getCode());
			tdy_deiktis.setMeasuringSystem(deiktis.getDeiktis().getMeasuringSystem());
			tdy_deiktis.setTarget(deiktis.getTarget());
			tdy_deiktis.setType(deiktis.getType());
			tdy_deiktis.setSec_l_id(this.sec_l.getId());
			this.tdy_deiktes.add(tdy_deiktis);
		}
		
		this.label = "DeiktesListLabel";
	}
	
	public String addSection_L__TDY_Financial() {
		TDY_Financial financial = new TDY_Financial();
		financial.setCategory("νέα κατηγορία");
		financial.setSec_l_id(this.sec_l.getId());
		this.tdy_financial.add( financial );
		
		updateSectionL_budget();
		
		this.label = "FinancialListLabel";
		
		return "section_l";
	}

	public void updateSectionL_budget() {
		BigDecimal remBudget = this.getSection_A__RemainingProjectBudget();
		System.out.println("remBudget = " + remBudget);
		
		BigDecimal budget = BigDecimal.ZERO;
		for( TDY_Financial f : this.tdy_financial ) {
			budget = budget.add(f.getBudget());
			System.out.println("tdy_financial = " +  f.getCategory() + "(" + f.getBudget() + ") -> budget = " + budget);
		}
		if (budget.subtract(this.sec_l.getBudget()).compareTo(remBudget) > 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπερβήκατε τον προυπολογισμό του έργου.  Δεν είναι δυνατή η αποθήκευση μέχρι την επίλυση του προβλήματος") );
			this.saveable.put(Section_L.class, false);
			return;
		}
		this.saveable.put(Section_L.class, true);
		this.sec_l.setBudget(budget);
	}

	public String addSection_L__TDY_Evolution() {
		TDY_Evolution evolution = new TDY_Evolution();
		evolution.setDescription("νέα κατάσταση");
		evolution.setSec_l_id(this.sec_l.getId());
		this.tdy_evolution.add( evolution );
		
		this.label = "EvolutionListLabel";
		
		return "section_l";
	}

	public String deleteSection_L__TDY_ProjectPlace() {
		TDY_Places placeItem = (TDY_Places) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDY_Places object: " + placeItem.getId() );
        tdy_places.remove(placeItem);
        if (placeItem.getId() != null) {
       		kbean.deleteRecord(TDY_Places.class, placeItem.getId() );
        }
        
        this.label = "PlacesListLabel";
        
		return "deleted";
	}

	public String deleteSection_L__TDY_Progress() {
		TDY_Progress progress = (TDY_Progress) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing TDY_Progress object: " + progress.getId() );
        tdy_progress.remove(progress);
        if (progress.getId() != null) {
       		kbean.deleteRecord(TDY_Progress.class, progress.getId() );
        }
        
        this.label = "ProgressListLabel";
        
        return "section_l";	
	}
	
	public String deleteSection_L__TDY_Deiktes() {
		TDY_Deiktes tmp_pobj = (TDY_Deiktes) this.tmp_dataTable3.getRowData();
        
		System.out.println( "removing TDY_Deiktes object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.tdy_deiktes.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDY_Deiktes.class, tmp_pobj.getId() );
        }
        
		this.label = "DeiktesListLabel";
		
        return "section_l";	
	}	
	
	public String deleteSection_L__TDY_ProgressActivities() {
		TDY_ProgressActivities tmp_pobj = (TDY_ProgressActivities) this.tmp_dataTable4.getRowData();
        
		System.out.println( "removing TDY_ProgressActivities object: " +tmp_pobj.getActivity() + ", " + tmp_pobj.getId() );
		this.tdy_progressActivities.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDY_ProgressActivities.class, tmp_pobj.getId() );
        }

		this.label = "ProgressActivitiesListLabel";
		
        return "section_l";	
	}
	
	public String deleteSection_L__TDY_Financial() {
		TDY_Financial tmp_pobj = (TDY_Financial) this.tmp_dataTable5.getRowData();
        
		System.out.println( "removing TDY_Financial object: " +tmp_pobj.getCategory() + ", " + tmp_pobj.getId() );
		this.tdy_financial.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDY_Financial.class, tmp_pobj.getId() );
        }
		
		updateSectionL_budget();
        
		this.label = "FinancialListLabel";
		
        return "section_l";	
	}
	
	public String deleteSection_L__TDY_Evolution() {
		TDY_Evolution tmp_pobj = (TDY_Evolution) this.tmp_dataTable6.getRowData();
        
		System.out.println( "removing TDY_Evolution object: " +tmp_pobj.getDescription() + ", " + tmp_pobj.getId() );
		this.tdy_evolution.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDY_Evolution.class, tmp_pobj.getId() );
		}

		this.label = "EvolutionListLabel";
		
        return "section_l";	
	}	
	
	/* Section ST methods */
	public void saveSection_ST_TDE_Financial() {
		this.saveable.put(Section_ST.class, true);
		
		for (Section_L prj: this.subProjects) {
			BigDecimal budget = BigDecimal.ZERO;
			System.out.println("Section_L: " + prj.getGiven_id() + ": " +  prj.getTitle() + ", budget = " + prj.getBudget());
			
			for (Integer y: prj.getYearBudgetMatrix().keySet()) {
				if (prj.getYearMatrix().get(y) == true) {
					System.out.println("prj: " + prj.getGiven_id() + ", year: " + y + " -> " + prj.getYearBudgetMatrix().get(y));
					TDE_Financial f = kbean.getTDEFinancialBySectionLIdYear(prj.getId(), y);
					if (f == null) {
						f = new TDE_Financial();
						f.setSec_l_id(prj.getId());
						f.setYear(y);
					}
					f.setBudget(prj.getYearBudgetMatrix().get(y));
					budget = budget.add(f.getBudget());
					this.all_tde_financials.get(prj).add(f);
				}
			}
			
			System.out.println("Project budget = " +  prj.getBudget() + ", sum: " + budget);
			if (budget.compareTo(prj.getBudget()) != 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το άθροισμα των ετήσιων προϋπολογισμών δεν ισούται με τον προϋπολογισμό του υποέργου "
						+ prj.getGiven_id() + ".") );
				this.saveable.put(Section_ST.class, false);
				break;
			}
		}

		if (this.saveable.get(Section_ST.class) == false)
			return;

		for (Section_L prj: this.subProjects) {
			System.out.println("Section_L: " + prj.getGiven_id() + ": " +  prj.getTitle());

			for (TDE_Financial f: this.all_tde_financials.get(prj)) {
				System.out.println("TDE_Financial:  " +  f.getYear() + ": " + f.getBudget());
				kbean.updateRecord(f);
			}
		}

		this.saveable.put(Section_ST.class, true);
	}
	
	public BigDecimal getSectionST_totalBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for( Integer y: this.yearBudgetMatrix.keySet()) {
			budget = budget.add(this.yearBudgetMatrix.get(y));
		}
		return budget;
	}
	
	/* Section Z methods */	
	public void saveSection_Z_TDE_Funders() {	
		for( Iterator iter=this.tde_funders.iterator(); iter.hasNext(); ) {
			TDE_Funders funder = (TDE_Funders) iter.next();

			if (funder.getId() == null)
				funder = (TDE_Funders) kbean.createRecord( funder );
			else
				kbean.updateRecord(funder);
		}
		this.saveable.put(Section_Z.class, true);
	}
	
	public void saveSection_Z_TDE_FundDistribution() {	
		for( Iterator iter=this.tde_fundDistribution.iterator(); iter.hasNext(); ) {
			TDE_FundDistribution fd = (TDE_FundDistribution) iter.next();

			if (fd.getId() == null)
				fd = (TDE_FundDistribution) kbean.createRecord( fd );
			else
				kbean.updateRecord(fd);
		}
		this.saveable.put(Section_Z.class, true);
	}
	
	public void sectionZ_changedFunder(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			TDE_Funders v1 = (TDE_Funders) this.tmp_dataTable.getRowData();
			TDE_Funders v2 = (TDE_Funders) kbean.getRecordById(v1.getId2(), TDE_Funders.class);
			v1.setCode(v2.getCode());
			v1.setDescription(v2.getDescription());
		}
		
		return;
	}
	
	public BigDecimal getSectionZ_totalPublicBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for( Iterator iter=this.tde_fundDistribution.iterator(); iter.hasNext(); ) {
			TDE_FundDistribution fd = (TDE_FundDistribution) iter.next();

			budget = budget.add(fd.getAmount_publicBudget());
		}
		return budget;
	}
	
	public BigDecimal getSectionZ_totalPrivateBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for( Iterator iter=this.tde_fundDistribution.iterator(); iter.hasNext(); ) {
			TDE_FundDistribution fd = (TDE_FundDistribution) iter.next();

			budget = budget.add(fd.getAmount_PrivateParticipation());
		}
		return budget;
	}
	
	public BigDecimal getSectionZ_totalBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for( Iterator iter=this.tde_fundDistribution.iterator(); iter.hasNext(); ) {
			TDE_FundDistribution fd = (TDE_FundDistribution) iter.next();

			budget = budget.add(fd.getBudget());
		}
		return budget;
	}
	
	public void sectionZ_updateFundDistribution() {
		for( Iterator iter=this.tde_fundDistribution.iterator(); iter.hasNext(); ) {
			TDE_FundDistribution fd = (TDE_FundDistribution) iter.next();
			double pd = fd.getPercentage_DistributionOffice();
			double pn = fd.getPercentage_NationalParticipation();
			double po = fd.getPercentage_Other();
			double pp = fd.getPercentage_PrivateParticipation();
			if (pd < 0.0 || pd > 100.0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(fd.getYear() + ":153: Πρέπει να εισάγετε ένα ποσοστό (0-100%)!" ));
				this.saveable.put(Section_Z.class, false);
				return;
			}
			if (pn < 0.0 || pn > 100.0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(fd.getYear() + ":154: Πρέπει να εισάγετε ένα ποσοστό (0-100%)!" ));
				this.saveable.put(Section_Z.class, false);
				return;
			}
			if (po < 0.0 || po > 100.0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(fd.getYear() + ":155: Πρέπει να εισάγετε ένα ποσοστό (0-100%)!" ));
				this.saveable.put(Section_Z.class, false);
				return;
			}
			if (pp < 0.0 || pp > 100.0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(fd.getYear() + ":156B: Πρέπει να εισάγετε ένα ποσοστό (0-100%)!" ));
				this.saveable.put(Section_Z.class, false);
				return;
			}
			if (pd + pn + po + pp > 100.0 ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(fd.getYear() + ": Τα ποσοστά 153, 154, 155, 156Β πρέπει να έχουν άθροισμα 100%!" ));
				this.saveable.put(Section_Z.class, false);
				return;
			}
			
			fd.setPercentage_publicBudget(pd + pn + po);
		}
	}
	
	public String addSection_Z__TDE_Funder() {
		TDE_Funders f = new TDE_Funders();
		f.setDescription("νέος χρηματοδότης");
		f.setSec_z_id(this.sec_z.getId());
		this.tde_funders.add(f);
		
		return "section_z";
	}

	public String deleteSection_Z__TDE_Funder() {
		TDE_Funders tmp_pobj = (TDE_Funders) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Funders object: " +tmp_pobj.getDescription() + ", " + tmp_pobj.getId() );
		this.tde_funders.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( TDE_Funders.class, tmp_pobj.getId() );
		}
		
		return "section_z";
	}
	
	/* Nomikes Desmefseis methods */

	public String addNOM() {
		Section_M nom = new Section_M();
		List<Section_M> nomlistrev0 = kbean.getNomikesDesmefseisBySectionBIdRevision(this.sec_b.getId(), Integer.valueOf(0));
		nom.setIndex(new Integer(nomlistrev0.size()+1));
		nom.setTitle( "Τίτλος Νομικής Δέσμευσης" );
		nom.setSubmissionStatus( SubmissionStatus.INCOMPLETE );
		nom.setSec_b_id( this.sec_b.getId() );
		nom.setDate_edit(new Date());
		nom.setEditable(true);
		nom.setContractorsUnion(false);
		kbean.createRecord( nom );

		this.nomList.add(nom);
		this.nomList = kbean.getNomikesDesmefseisBySectionBId(this.sec_b.getId());
		
		return "addNom";
	}
	
	public String addNOM_revision() {
		Section_M nom = new Section_M(this.sec_m);
		nom.setSubmissionStatus( SubmissionStatus.INCOMPLETE );
		nom.setSec_b_id( this.sec_b.getId() );
		nom.setDate_edit(new Date());
		nom.increaseRevision();
		nom.setEditable(true);
		kbean.createRecord( nom );
		
		for (NOM_Contractor nomc :this.nom_contractors) {
			NOM_Contractor newnomc = new NOM_Contractor(nomc);
			newnomc.setParentSectionM(nom);
			kbean.createRecord(newnomc);
		}
		
		for (NOM_SubProjects noms :this.sec_m.getSubprojects()) {
			NOM_SubProjects newnoms = new NOM_SubProjects(noms);
			newnoms.setParentSectionM(nom);
			kbean.createRecord(newnoms);
		}

		this.sec_m.setEditable(false);
		kbean.updateRecord(this.sec_m);
		
		this.sec_m = nom;
		
		this.nomList.add(nom);
		this.nomList = kbean.getNomikesDesmefseisBySectionBId(this.sec_b.getId());
		
		return "addNom";
	}
	
	public String finalizeNOM() {
		BigDecimal totalBudget = BigDecimal.ZERO;
		
		for (NOM_SubProjects noms :this.sec_m.getSubprojects()) {
			totalBudget = totalBudget.add(noms.getAmount());
		}
		
		if (totalBudget.compareTo(this.sec_m.getAmount()) < 0 ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το άθροισμα των συνολικών ποσών των Υποέργων είναι μικρότερο του ποσού της Νομικής Δέσμευσης! Αδύνατη η οριστικοποίηση!") );
			return "not-finalized";
		}
		
		this.sec_m.setSubmissionStatus( SubmissionStatus.FINAL );
		this.sec_m.setEditable(false);
		kbean.updateRecord(this.sec_m);
		
		return "finalizeNom";
	}
	
	/* Section M methods */
	public void saveSection_M__NOM_Contractors() {
		for( Iterator iter=this.nom_contractors.iterator(); iter.hasNext(); ) {
			NOM_Contractor nomc = (NOM_Contractor) iter.next();

			if (nomc.getId() == null)
				nomc = (NOM_Contractor) kbean.createRecord( nomc );
			else
				kbean.updateRecord(nomc);
		}
		this.saveable.put(Section_M.class, true);
	}
	
	public void saveSection_M__NOM_SubProjects() {
		this.saveable.put(Section_M.class, true);
		this.nom_subProjects.clear();
		System.out.println("selectedNomSubProjects.size = " + this.selectedNomSubProjects.size());
		
		for (Iterator iter = this.selectedNomSubProjects.entrySet().iterator(); iter.hasNext();) {
			Entry<NOM_SubProjects, Boolean> entry = (Entry<NOM_SubProjects, Boolean>) iter.next();
			NOM_SubProjects noms = entry.getKey();
			System.out.println("Nom_Subproject = " + noms.getId() + ", " + noms.getSubProject().getGiven_id() + ", " + entry.getValue());

			if (entry.getValue() == false) {
				this.nom_subProjects.add(noms);
				this.sec_m.removeSubProject(noms);
			} else {
				if (noms.getSelectableAmount().compareTo(noms.getSubProject().getBudget()) > 0 ) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υποέργο " + noms.getSubProject().getGiven_id() + ": Το Επιλέξιμο ποσό Υποέργου στη Νομική Δέσμευση πρέπει να είναι μικρότερο από το σύνολο των επιλέξιμων δαπανών του συγκεκριμένου υποέργου: " + currencyFormatter.format(noms.getSubProject().getBudget()) ));
					this.saveable.put(Section_M.class, false);
				}
				if (noms.getBudgetPrivate().add(noms.getBudgetPublic()).compareTo(noms.getAmount()) != 0) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το άθροισμα της Δημόσιας Δαπάνης και της Ιδιωτικής Συμμετοχής του Υποέργου " + noms.getSubProject().getGiven_id() + " είναι πρέπει να ισούται με το Συνολικό Ποσό του Υποέργου: " + currencyFormatter.format(noms.getAmount())) );
					this.saveable.put(Section_M.class, false);
				}
			}
		}
	}
	
	public void sectionM_changedContractor(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			NOM_Contractor n1 = (NOM_Contractor) this.tmp_dataTable.getRowData();
			Contractor c1 = (Contractor) kbean.getRecordById(n1.getContractor().getId(), Contractor.class);
			n1.setContractor(c1);
		}
		
		this.label = "ContractorListLabel";
		
		return;
	}
	
	public String addSection_M__NOM_Contractor() {
		NOM_Contractor nomc = new NOM_Contractor();
		nomc.setParentSectionM(this.sec_m);
		this.nom_contractors.add(nomc);
		
		this.label = "ContractorListLabel";
		
		return "nom_contractor";
	}
	
	public String deleteSection_M__NOM_Contractor() {
		NOM_Contractor tmp_pobj = (NOM_Contractor) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing NOM_Contractor object: " +tmp_pobj.getContractor().getName() + ", " + tmp_pobj.getId() );
		this.nom_contractors.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( NOM_Contractor.class, tmp_pobj.getId() );
		}
		
		this.label = "ContractorListLabel";
		
		return "deleted";
	}

	/* Section O methods */
	public String addSection_O_Problem() {
		this.tmp_m3_tde_problems.setSec_o_id( this.sec_o.getId() );

		kbean.createRecord( this.tmp_m3_tde_problems );

		this.m3_tde_problems = (List<M3_TDE_Problems>) kbean.getRecordsByQuery("sec_o_id=" + this.sec_o.getId(), M3_TDE_Problems.class );

		this.tmp_m3_tde_problems = new M3_TDE_Problems();
			
		return "saved";
	}
	
	/* Contractors methods */
	public String addContractor() {
		Contractor c = new Contractor();
		c.setFullName("Νέος Ανάδοχος");
		c.setName("Νέος Ανάδοχος");
		this.contractors.add(c);
		
		clearAllDataTables();
			
		return "added";
	}

	public String editContractor() {
		this.contractor = (Contractor) this.tmp_dataTable.getRowData();
		
		clearAllDataTables();
		
		return "editContractor";
	}
	
	public void saveContractor() {
		if (this.contractor.getId() == null)
			this.contractor = (Contractor) kbean.createRecord( this.contractor );
		else
			this.contractor = (Contractor) kbean.updateRecord(this.contractor);
		
		if (this.contractor == null) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Σφάλμα κατά την αποθήκευση!") );
		}
	}
	
	public String deleteContractor() {
		Contractor tmp_pobj = (Contractor) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing Contractor object: " + ", " + tmp_pobj.getId() );
		this.contractors.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( Contractor.class, tmp_pobj.getId() );
		}
		
		return "deleted";
	}

	public String completeSectionN() {
		Section_N n = (Section_N) this.tmp_dataTable.getRowData();
		this.saveable.put(Section_N.class, true);
		
		if (n.getAuthor() == null || n.getAuthor().equals("")) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να συμπληρώσετε τον Συντάκτη!"));
			this.saveable.put(Section_N.class, false);
		}
		if (n.getPublicationDate() == null) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να συμπληρώσετε Ημ/νία Σύνταξης!"));
			this.saveable.put(Section_N.class, false);
		}
		if (this.saveable.get(Section_N.class) == false) 
			return "validation-error";
		
		n.setStatus(SubmissionStatus.APPROVED);
		kbean.updateRecord(n);
		
		return "activated";
	}

	/* Section P methods */
	public String addSection_P__Certification() {
		M3_TDY_Certification c = new M3_TDY_Certification();
		c.setSec_p_id(this.sec_p.getId());
		this.m3_tdy_certifications.add(c);
		
		return "saved";
	}
	
	public String deleteSection_P__Certification() {
		M3_TDY_Certification tmp_pobj = (M3_TDY_Certification) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing M3_TDY_Certification object: " + ", " + tmp_pobj.getId() );
		this.m3_tdy_certifications.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( M3_TDY_Certification.class, tmp_pobj.getId() );
		}
		
		return "deleted";
	}
	
	public String completeSectionP() {
		Section_P p = (Section_P) this.reportsListDataTable.getRowData();
		
		//if (this.saveable.get(Section_P.class) == false) 
		//	return "validation-error";
		
		p.setStatus(SubmissionStatus.APPROVED);
		kbean.updateRecord(p);
		
		clearAllDataTables();
		
		return "activated";
	}
	
	public BigDecimal getSectionP_PlacesBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for (M3_TDY_Places p: this.m3_tdy_places) {
			budget = budget.add(p.getBudget());
		}
		return budget;
	}
	
	public BigDecimal getSectionP_PlacesRealBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for (M3_TDY_Places p: this.m3_tdy_places) {
			budget = budget.add(p.getRealBudget());
		}
		return budget;
	}
	
	public void prepareSectionP(Section_P sp) {
		// We're going to repopulate the section P M3 Lists. Not the best, but until we figure out
		// how to do proper object management, it will have to do
		kbean.deleteM3ListsBySectionPId(M3_TDY_Deiktes.class, sp.getId());
		kbean.deleteM3ListsBySectionPId(M3_TDY_Evolution.class, sp.getId());
		kbean.deleteM3ListsBySectionPId(M3_TDY_Places.class, sp.getId());
		kbean.deleteM3ListsBySectionPId(M3_TDY_Progress.class, sp.getId());
		kbean.deleteM3ListsBySectionPId(M3_TDY_Deiktes.class, sp.getId());
		
		// first, get all the TDY_Deiktes and copy them
		
		List<TDY_Deiktes> d = (List<TDY_Deiktes>) kbean.getDeiktesBySectionLId( this.sec_l.getId() );
		for( TDY_Deiktes dItem : d ) {
			M3_TDY_Deiktes tmp = new M3_TDY_Deiktes();
			tmp.setCode( dItem.getCode() );
			tmp.setMeasuringSystem( dItem.getMeasuringSystem() );
			tmp.setName( dItem.getName() );
			tmp.setTarget( dItem.getTarget() );
			tmp.setType( dItem.getType() );
			tmp.setSec_p_id( sp.getId() );

			kbean.createRecord( tmp );
		}

		// next is the TDY_Evolution
		List<TDY_Evolution> e = (List<TDY_Evolution>) kbean.getEvolutionBySectionLId( this.sec_l.getId() );
		for( TDY_Evolution eItem : e ) {
			M3_TDY_Evolution tmp = new M3_TDY_Evolution();
			tmp.setCode( eItem.getCode() );
			tmp.setDescription( eItem.getDescription() );
			tmp.setDate_initial( eItem.getDate_initial() );
			tmp.setSec_p_id( sp.getId() );

			kbean.createRecord( tmp );
		}

		// next is the TDY_Places
		System.out.println("prepareSectionP: "+  this.sec_l.getId());
		List<TDY_Places> p = (List<TDY_Places>) kbean.getPlacesBySectionLId( this.sec_l.getId() );
		for( TDY_Places pItem : p ) {
			System.out.println("prepareSectionP: "+  pItem.getMunicipality().getFullString());
			M3_TDY_Places tmp = new M3_TDY_Places();
			tmp.setMunicipality( pItem.getMunicipality() );
			tmp.setMunicipalityCode( pItem.getMunicipalityCode() );
			tmp.setPrefectureCode( pItem.getPrefectureCode() );
			tmp.setRegionCode( pItem.getRegionCode() );
			tmp.setBudget( pItem.getBudget() );
			tmp.setSec_p_id( sp.getId() );

			kbean.createRecord( tmp );
		}

		// next is the TDY_Progress
		List<TDY_Progress> pr = (List<TDY_Progress>) kbean.getProgressBySectionLId( this.sec_l.getId() );
		for( TDY_Progress pItem : pr ) {
			M3_TDY_Progress tmp = new M3_TDY_Progress();
			tmp.setCode( pItem.getCode() );
			tmp.setDescription( pItem.getDescription() );
			tmp.setMeasuringUnit( pItem.getMeasuringUnit() );
			tmp.setQuantity( pItem.getQuantity() );
			tmp.setBudget( pItem.getBudget() );
			tmp.setSec_p_id( sp.getId() );

			kbean.createRecord( tmp );
		}
	}

	/* Section O methods */
	public void prepareSectionO(Section_O o) {
		
		kbean.deleteM3ListsBySectionOId(M3_TDE_Deiktes.class, o.getId());
		kbean.deleteM3ListsBySectionOId(M3_TDE_Places.class, o.getId());
		
		// first, get all the TDY_Deiktes and copy them
		List<SectionD_Deiktis> d = (List<SectionD_Deiktis>) kbean.getAllDeiktesBySectionDId( this.sec_d.getId() );
		for( SectionD_Deiktis dItem : d ) {
			M3_TDE_Deiktes tmp = new M3_TDE_Deiktes();
			tmp.setCode( dItem.getDeiktis().getCode() );
			tmp.setMeasuringSystem( dItem.getDeiktis().getMeasuringSystem() );
			tmp.setName( dItem.getDeiktis().getName() );
			tmp.setTarget( dItem.getTarget() );
			tmp.setType( dItem.getType() );
			tmp.setSec_o_id( o.getId() );

			kbean.createRecord( tmp );
		}

		// next is the TDY_Places
		List<TDE_ProjectPlaces> p = (List<TDE_ProjectPlaces>) kbean.getProjectPlacesBySectionBId( this.sec_b.getId() );
		for( TDE_ProjectPlaces pItem : p ) {
			M3_TDE_Places tmp = new M3_TDE_Places();
			tmp.setMunicipality( pItem.getMunicipality() );
			tmp.setMunicipalityName( pItem.getMunicipalityName() );
			tmp.setMunicipalityCode( pItem.getMunicipalityCode() );
			tmp.setPrefectureCode( pItem.getPrefectureCode() );
			tmp.setPrefectureName( pItem.getPrefectureName() );
			tmp.setRegionCode( pItem.getRegionCode() );
			tmp.setRegionName( pItem.getRegionName() );
			System.out.println("RegionName: " + tmp.getRegionName());
			tmp.setBudget( pItem.getBudget() );
			tmp.setSec_o_id( o.getId() );

			kbean.createRecord( tmp );
		}
		
		// load subProjects
		goToSectionB();

		// get the quarter lists and shit like that to create objects for the triminiaio project report, used on the last form E3
		List<YearQuarters> qt = DateUtil.getQuartersList( this.sec_a.getDate_start(), this.sec_a.getDate_end() );

		for( YearQuarters qtItem : qt ) {
			for( Section_L sub : this.subProjects ) {

				// now add it for each quarter
				for( Integer quarter : qtItem.getQuarters() ) {
					// create the object
					M3_TDE_Semesters m = new M3_TDE_Semesters();
					m.setSec_o_id( o.getId() );
					m.setYear( qtItem.getYear() );
					m.setSubProject( sub );

					// we need to create some calendars
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.set( Calendar.YEAR, m.getYear() );
					c1.set( Calendar.DAY_OF_MONTH, 1 );
					c1.set( Calendar.HOUR_OF_DAY, 1 );
					c1.set( Calendar.MINUTE, 1 );

					c2.set( Calendar.YEAR, m.getYear() );
					c2.set( Calendar.HOUR_OF_DAY, 1 );
					c2.set( Calendar.MINUTE, 1 );
					c2.set( Calendar.DAY_OF_MONTH, 31 );

					if( quarter > 2 ) { 
						m.setSemester(2);
						c1.set( Calendar.MONTH, 6 );
						c2.set( Calendar.MONTH, 11 );
					} else {
						m.setSemester(1);
						c1.set( Calendar.MONTH, 0 );
						c2.set( Calendar.MONTH, 5 );
					}

					// set the amount
					m.setAmount( calculateInvoiceSum( sub, c1.getTime(), c2.getTime() ) );

					if( !kbean.exists_M3TDESemester(m) ) kbean.createRecord(m);
				}
			}
		}
	}
	
	public String completeSectionO() {
		Section_O o = (Section_O) this.reportsListDataTable.getRowData();
		
		//if (this.saveable.get(Section_P.class) == false) 
		//	return "validation-error";
		
		o.setStatus(SubmissionStatus.APPROVED);
		kbean.updateRecord(o);
		
		clearAllDataTables();
		
		return "activated";
	}
	
	public String addSection_O__Problem() {
		
		M3_TDE_Problems p = new M3_TDE_Problems();
		this.m3_tde_problems.add(p);
		
		//clearAllDataTables();
		
		return "saved";
	}
	
	public String deleteSection_O__Problem() {
		M3_TDE_Problems tmp_pobj = (M3_TDE_Problems) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing M3_TDE_Problems object: " + ", " + tmp_pobj.getId() );
		this.m3_tde_problems.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
			// now remove the object
			kbean.deleteRecord( M3_TDE_Problems.class, tmp_pobj.getId() );
		}
		
		return "deleted";
	}
	
	public BigDecimal getSectionO_PlacesBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for (M3_TDE_Places p: this.m3_tde_places) {			
			budget = budget.add(p.getBudget());
		}
		return budget;
	}
	
	public BigDecimal getSectionO_PlacesRealBudget() {
		BigDecimal budget = BigDecimal.ZERO;
		for (M3_TDE_Places p: this.m3_tde_places) {
			budget = budget.add(p.getRealBudget());
		}
		return budget;
	}
	
	/* Project methods */
	public String addNewProject() {
		
		Date currentDate = new Date();
			
		// add the Forms first
		Section_A a = new Section_A();
		a.setKps("Κ.Π.Σ. Έργου");
		a.setCode_ergorama("Κωδικός");
		a.setEpix_program("επιχ.πρόγραμμα");
		a.setDate_start( currentDate );
		a.setTitle_el("νέο έργο");
		a.setNum(currentDate.getTime());
		a.setRevision(1);
		a.setFramework(null);
		a.setProgramme(null);
		a.setAxis(null);
		a.setMeasure(null);
		a.setSubmeasure(null);
		a.setSection(null);
		a.setSubsection(null);
		a.setProjectAction(null);
		a = (Section_A) kbean.createRecord(a);
		
		Section_B b = new Section_B();
		b.setNum(currentDate.getTime());
		b.setRevision(1);
		b = (Section_B) kbean.createRecord(b);
		
		Section_C c = new Section_C();
		c.setNum(currentDate.getTime());
		c.setRevision(1);
		c = (Section_C) kbean.createRecord(c);
		
		Section_D d = new Section_D();
		d.setNum(currentDate.getTime());
		d.setRevision(1);
		d = (Section_D) kbean.createRecord(d);
		
		Section_E e = new Section_E();
		e.setNum(currentDate.getTime());
		e.setRevision(1);
		e = (Section_E) kbean.createRecord(e);
		
		Section_ST st = new Section_ST();
		st.setNum(currentDate.getTime());
		st.setRevision(1);
		st = (Section_ST) kbean.createRecord(st);
		
		Section_Z z = new Section_Z();
		z.setNum(currentDate.getTime());
		z.setRevision(1);
		z = (Section_Z) kbean.createRecord(z);
		
		Section_H h = new Section_H();
		h.setNum(currentDate.getTime());
		h.setRevision(1);
		h = (Section_H) kbean.createRecord(h);
		
		Section_Th th = new Section_Th();
		th.setNum(currentDate.getTime());
		th.setRevision(1);
		th = (Section_Th) kbean.createRecord(th);
		
		Section_I i = new Section_I();
		i.setNum(currentDate.getTime());
		i.setRevision(1);
		i = (Section_I) kbean.createRecord(i);
		
		Section_K k = new Section_K();
		k.setNum(currentDate.getTime());
		k.setRevision(1);
		k = (Section_K) kbean.createRecord(k);
		
		Section_L l = new Section_L();
		l = (Section_L) kbean.createRecord(l);
		
		Section_M m = new Section_M();
		m = (Section_M) kbean.createRecord(m);
		
		Section_N n = new Section_N();
		n = (Section_N) kbean.createRecord(n);
		
		Section_O o = new Section_O();
		o = (Section_O) kbean.createRecord(o);
		
		// then create links for the control table
		OTA_Projects op = new OTA_Projects(a.getId(), "Section_A", "ΟΝΤΟΤΗΤΑ Α - ΤΜΗΜΑ Α (ΤΑΥΤΟΤΗΤΑ ΕΡΓΟΥ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		// then create links for the control table
		op = new OTA_Projects(b.getId(), "Section_B", "ΟΝΤΟΤΗΤΑ B - ΤΜΗΜΑ B (ΦΥΣΙΚΟ ΑΝΤΙΚΕΙΜΕΝΟ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(c.getId(), "Section_C", "ΟΝΤΟΤΗΤΑ Γ - ΤΜΗΜΑ Γ (ΣΤΟΙΧΕΙΑ ΣΚΟΠΙΜΟΤΗΤΑΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(d.getId(), "Section_D", "ΟΝΤΟΤΗΤΑ Δ - ΤΜΗΜΑ Δ (ΔΕΙΚΤΕΣ ΠΑΡΑΚΟΛΟΥΘΗΣΗΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(e.getId(), "Section_E", "ΟΝΤΟΤΗΤΑ E - ΤΜΗΜΑ E (ΩΡΙΜΑΝΣΗ ΕΡΓΟΥ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(st.getId(), "Section_ST", "ΟΝΤΟΤΗΤΑ ΣΤ - ΤΜΗΜΑ ΣΤ (ΧΡΟΝΙΚΟΣ ΚΑΙ ΟΙΚΟΝΟΜΙΚΟΣ ΠΡΟΓΡΑΜΜΑΤΙΣΜΟΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(z.getId(), "Section_Z", "ΟΝΤΟΤΗΤΑ Z - ΤΜΗΜΑ Z (ΧΡΗΜΑΤΟΔΟΤΙΚΟ ΣΧΕΔΙΟ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(h.getId(), "Section_H", "ΟΝΤΟΤΗΤΑ H - ΤΜΗΜΑ H (ΕΙΔΙΚΕΣ ΠΛΗΡΟΦΟΡΙΕΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(th.getId(), "Section_Th", "ΟΝΤΟΤΗΤΑ Θ - ΤΜΗΜΑ Θ (ΚΑΤΑΛΟΓΟΣ ΣΥΝΗΜΜΕΝΩΝ ΕΓΓΡΑΦΩΝ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(i.getId(), "Section_I", "ΟΝΤΟΤΗΤΑ I - ΤΜΗΜΑ I (ΑΠΟΓΡΑΦΙΚΟ ΔΕΛΤΙΟ ΦΟΡΕΑ ΥΛΟΠΟΙΗΣΗΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(k.getId(), "Section_K", "ΟΝΤΟΤΗΤΑ K - ΤΜΗΜΑ K (ΑΠΟΓΡΑΦΙΚΟ ΔΕΛΤΙΟ ΦΟΡΕΑ ΕΠΙΒΛΕΨΗΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(l.getId(), "Section_L", "ΟΝΤΟΤΗΤΑ Λ - ΤΜΗΜΑ Λ (ΤΕΧΝΙΚΟ ΔΕΛΤΙΟ ΥΠΟΕΡΓΟΥ - ΕΚΤ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(m.getId(), "Section_M", "ΟΝΤΟΤΗΤΑ M - ΤΜΗΜΑ M (ΝΟΜΙΚΕΣ ΔΕΣΜΕΥΣΕΙΣ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(n.getId(), "Section_N", "ΟΝΤΟΤΗΤΑ N - ΤΜΗΜΑ N (ΜΗΝΑΙΑ ΔΕΛΤΙΑ ΠΑΡΑΚΟΛΟΥΘΗΣΗΣ ΥΠΟΕΡΓΟΥ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		op = new OTA_Projects(o.getId(), "Section_O", "ΟΝΤΟΤΗΤΑ O - ΤΜΗΜΑ O (ΤΡΙΜΗΝΙΑΙΟ ΔΕΛΤΙΟ ΠΑΡΑΚΟΛΟΥΘΗΣΗΣ ΥΠΟΕΡΓΟΥ)", "", currentDate.getTime(), SubmissionStatus.INCOMPLETE );
		op = (OTA_Projects) kbean.createRecord(op);
		System.out.println("added Form " + op.getSectionName() + " for project with Class = " + op.getSectionClass() );
		
		getProjectList();
		
		return "success";
	}
	
	public String deleteProject() {
		this.projects = (List<OTA_Projects>) kbean.getRecordsByQuery("sectionClass=" + this.sectionClass, OTA_Projects.class );

		for( OTA_Projects projectItem : this.projects ) {
			kbean.deleteRecord( OTA_Projects.class, projectItem.getId() );
		}

		getProjectList();
			
		return "project-deleted";
	}
	
	
	public String goToProject() {
		Section_A a = (Section_A) this.projectdataTable.getRowData();
		System.out.println("Section_A.id: " + a.getId() + ", revision: " + a.getRevision() + ", title : " +a.getTitle_el());
		this.sectionClass = a.getNum();
		this.revision = a.getRevision();

		this.projects = (List<OTA_Projects>) kbean.getRecordsByQuery("sectionClass=" + this.sectionClass, OTA_Projects.class );

		refreshRevisions();
		
		System.out.println( "current revision : " + this.revision );

		clearProjectDataTables();
		
		return "goToProject";
	}
	
	public void refreshRevisions() {
		// we need to get the revision list now.
		this.revisions = (List<Section_A>) kbean.getRecordsByQuery("num=" + this.sectionClass + " order by revision asc", Section_A.class );
	}
	
	public String createNewRevision() {
		// and here, now, we need to create 10000000000 objects and lists to copy stuff around
		int oldrev, newrev;

		goToTDE();
		goToSectionA();
		goToSectionB();
		goToSectionC();
		goToSectionD();
		goToSectionE();
		goToSectionH();
		goToSectionI();
		goToSectionK();
		goToSectionST();
		goToSectionZ();
		
		// FIRST IS THE SECTIONS FOR TDE (TDY WILL FOLLOW AS LIST ITEMS BOUND TO THE TDE)
		Section_A a = (Section_A) kbean.getLatestRevisionObject( Section_A.class, this.sectionClass );
		a.setId(null);
		
		oldrev = a.getRevision();
		newrev = oldrev +1;
		
		a.setRevision( newrev );
		a.setSubmissionStatus(SubmissionStatus.INCOMPLETE);
		a.setDate_approval(null);
		a.setDate_filing(null);
		a.setDate_submission(null);
		kbean.createRecord(a);

		Section_B b = (Section_B) kbean.getLatestRevisionObject( Section_B.class, this.sectionClass );
		b.setId(null); b.setRevision( newrev );
		b = (Section_B) kbean.createRecord(b);

		Section_C c = (Section_C) kbean.getLatestRevisionObject( Section_C.class, this.sectionClass );
		c.setId(null); c.setRevision( newrev );
		kbean.createRecord(c);

		Section_D d = (Section_D) kbean.getLatestRevisionObject( Section_D.class, this.sectionClass );
		d.setId(null); d.setRevision( newrev );
		kbean.createRecord(d);

		Section_E e = (Section_E) kbean.getLatestRevisionObject( Section_E.class, this.sectionClass );
		e.setId(null); e.setRevision( newrev );
		kbean.createRecord(e);

		Section_ST st = (Section_ST) kbean.getLatestRevisionObject( Section_ST.class, this.sectionClass );
		st.setId(null); st.setRevision( newrev );
		kbean.createRecord(st);

		Section_Z z = (Section_Z) kbean.getLatestRevisionObject( Section_Z.class, this.sectionClass );
		z.setId(null); z.setRevision( newrev );
		kbean.createRecord(z);

		Section_H h = (Section_H) kbean.getLatestRevisionObject( Section_H.class, this.sectionClass );
		h.setId(null); h.setRevision( newrev );
		kbean.createRecord(h);

		Section_Th th = (Section_Th) kbean.getLatestRevisionObject( Section_Th.class, this.sectionClass );
		th.setId(null); th.setDocuments(null); th.setRevision( newrev );
		kbean.createRecord(th);

		Section_I i = (Section_I) kbean.getLatestRevisionObject( Section_I.class, this.sectionClass );
		i.setId(null); i.setRevision( newrev );
		kbean.createRecord(i);

		Section_K k = (Section_K) kbean.getLatestRevisionObject( Section_K.class, this.sectionClass );
		k.setId(null); k.setRevision( newrev );
		kbean.createRecord(k);

		// NEXT IS THE LISTS

		// Section A
		this.sec_a  = (Section_A) kbean.getRecordByClassAndRevision(Section_A.class, this.sectionClass, oldrev );
		this.sec_b  = (Section_B) kbean.getRecordByClassAndRevision(Section_B.class, this.sectionClass, oldrev );
		this.sec_c  = (Section_C) kbean.getRecordByClassAndRevision(Section_C.class, this.sectionClass, oldrev );
		this.sec_d  = (Section_D) kbean.getRecordByClassAndRevision(Section_D.class, this.sectionClass, oldrev );
		this.sec_e  = (Section_E) kbean.getRecordByClassAndRevision(Section_E.class, this.sectionClass, oldrev );
		this.sec_h  = (Section_H) kbean.getRecordByClassAndRevision(Section_H.class, this.sectionClass, oldrev );
		this.sec_i  = (Section_I) kbean.getRecordByClassAndRevision(Section_I.class, this.sectionClass, oldrev );
		this.sec_k  = (Section_K) kbean.getRecordByClassAndRevision(Section_K.class, this.sectionClass, oldrev );
		this.sec_th = (Section_Th) kbean.getRecordByClassAndRevision(Section_Th.class, this.sectionClass, oldrev );
		this.sec_st = (Section_ST) kbean.getRecordByClassAndRevision(Section_ST.class, this.sectionClass, oldrev );
		this.sec_z  = (Section_Z) kbean.getRecordByClassAndRevision(Section_Z.class, this.sectionClass, oldrev );
		
		// Section A: vendors
		for( TDE_Vendors vendor : this.vendors ) {
			vendor.setSec_a_id(a.getId());
			vendor.setId(null);
			//kbean.createRecord( vendor );
		}
		kbean.updateListRecord(this.vendors);
		
		// Section B: projectplaces
		for( TDE_ProjectPlaces pp2 : this.projectPlaces ) {
			// copy the record
			pp2.setId(null);
			pp2.setSec_b_id(b.getId());
			//pp2 = (TDE_ProjectPlaces) kbean.createRecord(pp2);				
		}
		kbean.updateListRecord(this.projectPlaces);
				
		// Subprojects
		for( Section_L sl2 : this.subProjects ) {
			Integer sl_prev_id = sl2.getId();
			System.out.println("sl_prev: id " +  sl_prev_id);
			List<TDY_Deiktes> tdy_d = kbean.getDeiktesBySectionLId( sl2.getId() );
			List<TDY_Evolution> tdy_e = kbean.getEvolutionBySectionLId( sl2.getId() );
			List<TDY_Financial> tdy_f = kbean.getTDYFinancialBySectionLId( sl2.getId() );
			List<TDY_Places> tdy_p = kbean.getPlacesBySectionLId( sl2.getId() );
			List<TDY_Progress> tdy_pr = kbean.getProgressBySectionLId( sl2.getId() );
			List<TDY_ProgressActivities> tdy_pa = kbean.getProgressActivitiesBySectionLId( sl2.getId() );
			List<Cycle> tdy_katartisis = sl2.getKatartisis_Cycles();
			List<TDE_Financial> tdy_ff = kbean.getTDEFinancialBySectionLId(sl2.getId());

			sl2.setId(null);
			sl2.setRevision(newrev);
			sl2.setKatartisis_Cycles(null);
			sl2.setSec_b_id( b.getId() );
			
			for (Cycle cyc: tdy_katartisis) {
				cyc.setId(null);
				cyc.setSubProject(sl2);
			}

			sl2 = (Section_L) kbean.createRecord(sl2);

			for( TDY_Deiktes tdy_d2 : tdy_d ) {
				tdy_d2.setId(null);
				tdy_d2.setSec_l_id(sl2.getId());
				//tdy_d2 = (TDY_Deiktes) kbean.createRecord(tdy_d2);
			}
			kbean.updateListRecord(tdy_d);

			for( TDY_Evolution tdy_e2 : tdy_e ) {
				tdy_e2.setId(null);
				tdy_e2.setSec_l_id(sl2.getId());
				//tdy_e2 = (TDY_Evolution) kbean.createRecord(tdy_e2);
			}
			kbean.updateListRecord(tdy_e);

			for( TDY_Financial tdy_f2 : tdy_f ) {
				tdy_f2.setId(null);
				tdy_f2.setSec_l_id(sl2.getId());
				//tdy_f2 = (TDY_Financial) kbean.createRecord(tdy_f2);
			}
			kbean.updateListRecord(tdy_f);

			for( TDY_Places tdy_p2 : tdy_p ) {
				tdy_p2.setId(null);
				tdy_p2.setSec_l_id(sl2.getId());
				//tdy_p2 = (TDY_Places) kbean.createRecord(tdy_p2);
			}
			kbean.updateListRecord(tdy_p);

			for( TDY_Progress tdy_p2 : tdy_pr ) {
				tdy_p2.setId(null);
				tdy_p2.setSec_l_id(sl2.getId());
				//tdy_p2 = (TDY_Progress) kbean.createRecord(tdy_p2);
			}
			kbean.updateListRecord(tdy_pr);

			for( TDY_ProgressActivities tdy_p2 : tdy_pa ) {
				tdy_p2.setId(null);
				tdy_p2.setSec_l_id(sl2.getId());
				//tdy_p2 = (TDY_ProgressActivities) kbean.createRecord(tdy_p2);
			}
			kbean.updateListRecord(tdy_pa);

			// This is for Section Z
			System.out.println("sl_prev: id " + sl_prev_id);
			List<TDE_Financial> ff2 = kbean.getTDEFinancialBySectionLId(sl_prev_id);
			for( TDE_Financial f: ff2 ) {
				System.out.println("TDE_Financial: id " +  f.getId() + ", L: " + sl_prev_id);
				// copy the record
				f.setId(null);
				f.setSec_l_id(sl2.getId());
				//f = (TDE_Financial) kbean.createRecord(f);
			}
			kbean.updateListRecord(ff2);

			// This is for Section ST
			System.out.println("sl_prev: id " +  sl_prev_id);
			List<TDE_FinancialQuarters> fq2 = kbean.getTDEFinancialQuartersBySectionLId(sl_prev_id);
			for( TDE_FinancialQuarters fq: fq2 ) {
				System.out.println("TDE_FinancialQuarters: id " +  fq.getId() + ", L: " + sl_prev_id);
				// copy the record
				fq.setId(null);
				fq.setSec_l_id(sl2.getId());
				//f = (TDE_Financial) kbean.createRecord(f);
			}
			kbean.updateListRecord(fq2);
		}

		// Section_D: deiktes
		List<SectionD_Deiktis> dd = kbean.getAllDeiktesBySectionDId( this.sec_d.getId());
		for( SectionD_Deiktis dd2 : dd ) {
			dd2.setId(null);
			dd2.setSec_d_id(d.getId());
			//dd2 = (SectionD_Deiktis) kbean.createRecord(dd2);
		}
		kbean.updateListRecord(dd);

		// Section_E: analysis
		for( TDE_Analysis aa2 : this.analysis ) {
			aa2.setId(null);
			aa2.setSec_e_id(e.getId());
			//aa2 = (TDE_Analysis) kbean.createRecord(aa2);
		}
		kbean.updateListRecord(this.analysis);

		// Section_E: TDE_Katartisis
		for( TDE_Katartisis kk2 : this.katartisis ) {
			kk2.setId(null);
			kk2.setSec_e_id(e.getId());
			//kk2 = (TDE_Katartisis) kbean.createRecord(kk2);
		}
		kbean.updateListRecord(this.katartisis);

		// Section_E: TDE_Permissions
		for( TDE_Permissions per2 : this.permissions ) {
			per2.setId(null);
			per2.setSec_e_id(e.getId());
			//per2 = (TDE_Permissions) kbean.createRecord(per2);
		}
		kbean.updateListRecord(this.permissions);

		// Section_E: TDE_Tasks
		for( TDE_Tasks tt2 : this.tasks ) {
			tt2.setId(null);
			tt2.setSec_e_id(e.getId());
			//tt2 = (TDE_Tasks) kbean.createRecord(tt2);
		}
		kbean.updateListRecord(this.tasks);

		// Section_E: TDE_VendorTasks
		for( TDE_VendorTasks vt2 : this.vendorTasks ) {
			vt2.setId(null);
			vt2.setSec_e_id(e.getId());
			//vt2 = (TDE_VendorTasks) kbean.createRecord(vt2);
		}
		kbean.updateListRecord(this.vendorTasks);
		
		// Section_Th: DocumentEntry
		List<DocumentEntry> docs = new ArrayList<DocumentEntry>();
		for( DocumentEntry doc : this.sec_th.getDocuments() ) {
			doc.setId(null);
			docs.add(doc);
		}
		th.setDocuments(docs);
		kbean.updateRecord(th);

		// Section I: employees
		for( TDE_Employees ee2 : this.section_I_employees ) {
			ee2.setId(null);
			//ee2 = (TDE_Employees) kbean.createRecord( ee2 );
		}
		kbean.updateListRecord(this.section_I_employees);
		
		for( TDE_Employees ee2 : this.section_I_employees ) {
			// create the connector
			Section_I__TDE_Employees tmp_conn = new Section_I__TDE_Employees();
			tmp_conn.setSection_id(b.getId());
			tmp_conn.setTde_id(ee2.getId());
			tmp_conn = (Section_I__TDE_Employees) kbean.createRecord( tmp_conn );
		}
		
		// Section K: employees
		for( TDE_Employees ee2 : this.section_K_employees ) {
			ee2.setId(null);
		}
		kbean.updateListRecord(this.section_K_employees);
		
		for( TDE_Employees ee2 : this.section_K_employees ) {
			ee2 = (TDE_Employees) kbean.createRecord( ee2 );
			// create the connector
			Section_K__TDE_Employees tmp_conn = new Section_K__TDE_Employees();
			tmp_conn.setSection_id(b.getId());
			tmp_conn.setTde_id(ee2.getId());
			tmp_conn = (Section_K__TDE_Employees) kbean.createRecord( tmp_conn );
		}
		
		// Section Z
		for( TDE_Funders ff3 : this.tde_funders ) {
			// copy the record
			ff3.setId(null);
			ff3.setSec_z_id(z.getId());
			//ff3 = (TDE_Funders) kbean.createRecord(ff3);				
		}
		kbean.updateListRecord(this.tde_funders);
		
		// Section ST: Fund Distribution
		List<TDE_FundDistribution> tde_fd = kbean.getTDEFundDistributionBySectionZId(z.getId());
		for (TDE_FundDistribution fd: tde_fd) {
			// copy the record
			fd.setId(null);
			fd.setSec_z_id(z.getId());
		}
		kbean.updateListRecord(tde_fd);

		// now we need to set the global revision number to our revision number
		this.revision = a.getRevision();
		refreshRevisions();
		
		this.checkProcessed = false;
		
		return "added_revision";
	}
	
	public String deleteRevision() {

		Section_A prj = (Section_A) this.revisionsdataTable.getRowData();
		int revision = prj.getRevision();
		
		deleteRevision_real(revision);
		
		return "deleted_revision";
	}
		
	public void deleteRevision_real(int revision) {
		
		this.sec_a  = (Section_A) kbean.getRecordByClassAndRevision(Section_A.class, this.sectionClass, revision );
		this.sec_b  = (Section_B) kbean.getRecordByClassAndRevision(Section_B.class, this.sectionClass, revision );
		this.sec_c  = (Section_C) kbean.getRecordByClassAndRevision(Section_C.class, this.sectionClass, revision );
		this.sec_d  = (Section_D) kbean.getRecordByClassAndRevision(Section_D.class, this.sectionClass, revision );
		this.sec_e  = (Section_E) kbean.getRecordByClassAndRevision(Section_E.class, this.sectionClass, revision );
		this.sec_h  = (Section_H) kbean.getRecordByClassAndRevision(Section_H.class, this.sectionClass, revision );
		this.sec_i  = (Section_I) kbean.getRecordByClassAndRevision(Section_I.class, this.sectionClass, revision );
		this.sec_k  = (Section_K) kbean.getRecordByClassAndRevision(Section_K.class, this.sectionClass, revision );
		this.sec_th = (Section_Th) kbean.getRecordByClassAndRevision(Section_Th.class, this.sectionClass, revision );
		this.sec_st = (Section_ST) kbean.getRecordByClassAndRevision(Section_ST.class, this.sectionClass, revision );
		this.sec_z  = (Section_Z) kbean.getRecordByClassAndRevision(Section_Z.class, this.sectionClass, revision );
		
		goToSectionA();
		goToSectionB();
		goToSectionC();
		goToSectionD();
		goToSectionE();
		goToSectionH();
		goToSectionI();
		goToSectionK();
		goToSectionST();
		goToSectionZ();
		
		// Section A: vendors
		for (TDE_Vendors ob: this.vendors)
			kbean.deleteRecord(TDE_Vendors.class, ob.getId());
		
		// Section B: projectplaces
		for (TDE_ProjectPlaces ob: this.projectPlaces)
			kbean.deleteRecord(TDE_ProjectPlaces.class, ob.getId());
		
		// Subprojects
		for( Section_L sl2 : this.subProjects ) {
			// Section ST
			if (this.all_tde_financials.get(sl2) != null) {
				for (TDE_Financial ob: this.all_tde_financials.get(sl2))
					kbean.deleteRecord(TDE_Financial.class, ob.getId());
			}
			// delete sec_l project itself
			deleteSectionL(sl2);
		}

		// Section_D: deiktes
		List<SectionD_Deiktis> alldeiktes = kbean.getAllDeiktesBySectionDId( this.sec_d.getId());
		for (SectionD_Deiktis ob: alldeiktes)
			kbean.deleteRecord(SectionD_Deiktis.class, ob.getId());

		// Section_E: analysis
		for (TDE_Analysis ob: this.analysis)
			kbean.deleteRecord(TDE_Analysis.class, ob.getId());

		// Section_E: TDE_Katartisis
		for (TDE_Katartisis ob: this.katartisis)
			kbean.deleteRecord(TDE_Katartisis.class, ob.getId());

		// Section_E: TDE_Permissions
		for (TDE_Permissions ob: this.permissions)
			kbean.deleteRecord(TDE_Permissions.class, ob.getId());

		// Section_E: TDE_Tasks
		for (TDE_Tasks ob: this.tasks)
			kbean.deleteRecord(TDE_Tasks.class, ob.getId());

		// Section_E: TDE_VendorTasks
		for (TDE_VendorTasks ob: this.vendorTasks)
			kbean.deleteRecord(TDE_VendorTasks.class, ob.getId());

		// Section I: employees
		for (TDE_Employees ob: this.section_I_employees)
			kbean.deleteRecord(TDE_Employees.class, ob.getId());
		
		// Section K: employees
		for (TDE_Employees ob: this.section_K_employees)
			kbean.deleteRecord(TDE_Employees.class, ob.getId());
		
		// Section Z
		for (TDE_Funders ob: this.tde_funders)
			kbean.deleteRecord(TDE_Funders.class, ob.getId());

		if (this.sec_a != null) kbean.deleteRecord(Section_A.class, this.sec_a.getId());
		if (this.sec_b != null) kbean.deleteRecord(Section_B.class, this.sec_b.getId());
		if (this.sec_c != null) kbean.deleteRecord(Section_C.class, this.sec_c.getId());
		if (this.sec_d != null) kbean.deleteRecord(Section_D.class, this.sec_d.getId());
		if (this.sec_e != null) kbean.deleteRecord(Section_E.class, this.sec_e.getId());
		if (this.sec_th != null) kbean.deleteRecord(Section_Th.class, this.sec_th.getId());
		if (this.sec_h != null) kbean.deleteRecord(Section_H.class, this.sec_h.getId());
		if (this.sec_i != null) kbean.deleteRecord(Section_I.class, this.sec_i.getId());
		if (this.sec_k != null) kbean.deleteRecord(Section_K.class, this.sec_k.getId());
		if (this.sec_st != null) kbean.deleteRecord(Section_ST.class, this.sec_st.getId());
		if (this.sec_z != null) kbean.deleteRecord(Section_Z.class, this.sec_z.getId());
		
		this.checkProcessed = false;
				
		// FIRST IS THE SECTIONS FOR TDE (TDY WILL FOLLOW AS LIST ITEMS BOUND TO THE TDE)
		Section_A a = (Section_A) kbean.getLatestRevisionObject( Section_A.class, this.sectionClass );
		a.setSubmissionStatus(SubmissionStatus.INCOMPLETE);
		kbean.updateRecord(a);
		
		refreshRevisions();
	}
	
	public void deleteSectionL(Section_L sub) {
		if (sub.getId() != null) {
			
			List<TDY_Deiktes> tdy_d = kbean.getDeiktesBySectionLId( sub.getId() );
			List<TDY_Evolution> tdy_e = kbean.getEvolutionBySectionLId( sub.getId() );
			List<TDY_Financial> tdy_f = kbean.getTDYFinancialBySectionLId( sub.getId() );
			List<TDY_Places> tdy_p = kbean.getPlacesBySectionLId( sub.getId() );
			List<TDY_Progress> tdy_pr = kbean.getProgressBySectionLId( sub.getId() );
			List<TDY_ProgressActivities> tdy_pa = kbean.getProgressActivitiesBySectionLId( sub.getId() );

			for (TDY_Deiktes ob: tdy_d)
				kbean.deleteRecord(TDY_Deiktes.class, ob.getId());
			for (TDY_Evolution ob: tdy_e)
				kbean.deleteRecord(TDY_Evolution.class, ob.getId());
			for (TDY_Financial ob: tdy_f)
				kbean.deleteRecord(TDY_Financial.class, ob.getId());
			for (TDY_Places ob: tdy_p)
				kbean.deleteRecord(TDY_Places.class, ob.getId());
			for (TDY_Progress ob: tdy_pr)
				kbean.deleteRecord(TDY_Progress.class, ob.getId());
			for (TDY_ProgressActivities ob: tdy_pa)
				kbean.deleteRecord(TDY_ProgressActivities.class, ob.getId());
			
			// remove references first, before removing the object.
			kbean.deleteNOMSubProject(sub);
			
        	kbean.deleteMonthlyReportsBySubProject(sub);

        	kbean.deleteSub3MonthlyReportsBySubProject(sub);

        	kbean.deleteFinancialBySubProject(sub.getId());

        	kbean.deleteSemestersBySubProject(sub);

        	// now remove the object
        	kbean.deleteRecord(Section_L.class, sub.getId() );
        }
	}
		
	public String createNewKatartisisSubproject() {
		this.sec_l = (Section_L) this.subProjectdataTable.getRowData();
		
		List<Cycle> cycles = new ArrayList<Cycle>();
		Cycle c1= new Cycle("1ος", "ΗΜΕΡΙΔΕΣ", this.sec_l, CycleType.CYCLE_IMERIDA );
		Cycle c2 = new Cycle("2ος", "ΔΗΜΟΤΙΚΑ ΣΥΜΒΟΥΛΙΑ", this.sec_l, CycleType.CYCLE_DS );
		Cycle c3 = new Cycle("3ος", "ΣΕΜΙΝΑΡΙΑ", this.sec_l, CycleType.CYCLE_SEMINAR );
		cycles.add(c1);
		cycles.add(c2);
		cycles.add(c3);
		for (Cycle c: cycles)
			c = (Cycle) kbean.createRecord(c);

		this.sec_l.setKatartisis_Cycles(cycles);
		this.sec_l.setKatartisis(true);
		kbean.updateRecord(this.sec_l);
		
		return "saved_katartisis";
	}
	
	public String goToTDE() {
		try {
			Section_A prj = (Section_A) this.revisionsdataTable.getRowData();
			this.revision = prj.getRevision();
			System.out.println("Approval Date: " + this.date_approval);
		} catch (Exception e) {
			this.revision = kbean.getLatestRevisionNumber(Section_A.class, this.sectionClass);
		}

		this.sec_a  = (Section_A) kbean.getRecordByClassAndRevision(Section_A.class, this.sectionClass, this.revision );
		this.sec_b  = (Section_B) kbean.getRecordByClassAndRevision(Section_B.class, this.sectionClass, this.revision );
		this.sec_c  = (Section_C) kbean.getRecordByClassAndRevision(Section_C.class, this.sectionClass, this.revision );
		this.sec_d  = (Section_D) kbean.getRecordByClassAndRevision(Section_D.class, this.sectionClass, this.revision );
		this.sec_e  = (Section_E) kbean.getRecordByClassAndRevision(Section_E.class, this.sectionClass, this.revision );
		this.sec_h  = (Section_H) kbean.getRecordByClassAndRevision(Section_H.class, this.sectionClass, this.revision );
		this.sec_i  = (Section_I) kbean.getRecordByClassAndRevision(Section_I.class, this.sectionClass, this.revision );
		this.sec_k  = (Section_K) kbean.getRecordByClassAndRevision(Section_K.class, this.sectionClass, this.revision );
		this.sec_th = (Section_Th) kbean.getRecordByClassAndRevision(Section_Th.class, this.sectionClass, this.revision );
		this.sec_st = (Section_ST) kbean.getRecordByClassAndRevision(Section_ST.class, this.sectionClass, this.revision );
		this.sec_z  = (Section_Z) kbean.getRecordByClassAndRevision(Section_Z.class, this.sectionClass, this.revision );

		goToSectionA();
		
		this.checkProcessed = false;

		return "tde";
	}
	
	public void checkTDE() {
		// load TDE/TDY first
		goToTDE();

		this.submittable = true;
		this.checkProcessed = true;

		// Checks for Section A
		if (this.sec_a.getFramework() == null || 
				(this.sec_a.getFramework() != null && this.sec_a.getFramework().equals(Integer.valueOf(-1)))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Κοινοτικό Πλαίσιο στο Τμήμα Α.") );
		}
			
		
		if (this.sec_a.getProgramme() == null ||
				(this.sec_a.getProgramme() != null && this.sec_a.getProgramme().equals(Integer.valueOf(-1)))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Επιχειρησιακό Πρόγραμμα στο Τμήμα Α.") );
		}
			
		
		if (this.sec_a.getAxis() == null ||
				(this.sec_a.getAxis() != null && this.sec_a.getAxis().equals(Integer.valueOf(-1)))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Άξονας Προτεραιότητας στο Τμήμα Α.") );
		}
			
		if (this.sec_a.getMeasure() == null ||
				(this.sec_a.getMeasure() != null && this.sec_a.getMeasure().equals(Integer.valueOf(-1)))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Μέτρο στο Τμήμα Α.") );
		}
			
		/* TODO: revisit this check later
		if (this.sec_a.getSubmeasure() == null ||
				(this.sec_a.getSubmeasure() != null && this.sec_a.getSubmeasure().equals(Integer.valueOf(-1)))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Υπομέτρο στο Τμήμα Α.") );
		} */
		
		if (this.sec_a.getTitle_el() == null ||
				(this.sec_a.getTitle_el() != null && this.sec_a.getTitle_el().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τίτλος/Ενέργεια στο Τμήμα Α.") );
		}
				
		if (this.sec_a.getDate_start() == null) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ημερομηνία Έναρξης Έργου στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getDate_end() == null) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ημερομηνία Λήξης Έργου στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Προϋπολογισμός Έργου στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_id() == null ||
				(this.sec_a.getProposalVendor_id() != null && this.sec_a.getProposalVendor_id().equals(-1))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Φορέας Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_firstName() == null ||
				(this.sec_a.getProposalVendor_representative_firstName() != null && this.sec_a.getProposalVendor_representative_firstName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Όνομα Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_lastName() == null ||
				(this.sec_a.getProposalVendor_representative_lastName() != null && this.sec_a.getProposalVendor_representative_lastName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Επώνυμο Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_position() == null ||
				(this.sec_a.getProposalVendor_representative_position() != null && this.sec_a.getProposalVendor_representative_position().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Θέση Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_address() == null ||
				(this.sec_a.getProposalVendor_representative_address() != null && this.sec_a.getProposalVendor_representative_address().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Διεύθυνση Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_city() == null ||
				(this.sec_a.getProposalVendor_representative_city() != null && this.sec_a.getProposalVendor_representative_city().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Πόλη Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_postalCode() == null ||
				(this.sec_a.getProposalVendor_representative_postalCode() != null && this.sec_a.getProposalVendor_representative_postalCode().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τ.Κ. Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_telephone() == null ||
				(this.sec_a.getProposalVendor_representative_telephone() != null && this.sec_a.getProposalVendor_representative_telephone().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τηλέφωνο Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_email() == null ||
				(this.sec_a.getProposalVendor_representative_email() != null && this.sec_a.getProposalVendor_representative_email().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Email Νομίμου Εκπροσώπου Φορέα Πρότασης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_id() == null ||
				(this.sec_a.getImplementationVendor_id() != null && this.sec_a.getImplementationVendor_id().equals(-1))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Φορέας Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_firstName() == null ||
				(this.sec_a.getProposalVendor_representative_firstName() != null && this.sec_a.getProposalVendor_representative_firstName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Όνομα Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_lastName() == null ||
				(this.sec_a.getProposalVendor_representative_lastName() != null && this.sec_a.getProposalVendor_representative_lastName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Επώνυμο Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_position() == null ||
				(this.sec_a.getProposalVendor_representative_position() != null && this.sec_a.getProposalVendor_representative_position().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Θέση Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_address() == null ||
				(this.sec_a.getProposalVendor_representative_address() != null && this.sec_a.getProposalVendor_representative_address().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Διεύθυνση Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_city() == null ||
				(this.sec_a.getProposalVendor_representative_city() != null && this.sec_a.getProposalVendor_representative_city().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Πόλη Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_postalCode() == null ||
				(this.sec_a.getProposalVendor_representative_postalCode() != null && this.sec_a.getProposalVendor_representative_postalCode().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τ.Κ. Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_telephone() == null ||
				(this.sec_a.getProposalVendor_representative_telephone() != null && this.sec_a.getProposalVendor_representative_telephone().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τηλέφωνο Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getProposalVendor_representative_email() == null ||
				(this.sec_a.getProposalVendor_representative_email() != null && this.sec_a.getProposalVendor_representative_email().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Email Νομίμου Εκπροσώπου Φορέα Υλοποίησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getFinancingVendor_id() == null ||
				(this.sec_a.getFinancingVendor_id() != null && this.sec_a.getFinancingVendor_id().equals(-1))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Φορέας Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_firstName() == null ||
				(this.sec_a.getImplementationVendor_representative_firstName() != null && this.sec_a.getImplementationVendor_representative_firstName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Όνομα Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_lastName() == null ||
				(this.sec_a.getImplementationVendor_representative_lastName() != null && this.sec_a.getImplementationVendor_representative_lastName().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Επώνυμο Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_position() == null ||
				(this.sec_a.getImplementationVendor_representative_position() != null && this.sec_a.getImplementationVendor_representative_position().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Θέση Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_address() == null ||
				(this.sec_a.getImplementationVendor_representative_address() != null && this.sec_a.getImplementationVendor_representative_address().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Διεύθυνση Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_city() == null ||
				(this.sec_a.getImplementationVendor_representative_city() != null && this.sec_a.getImplementationVendor_representative_city().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Πόλη Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_postalCode() == null ||
				(this.sec_a.getImplementationVendor_representative_postalCode() != null && this.sec_a.getImplementationVendor_representative_postalCode().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τ.Κ. Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_telephone() == null ||
				(this.sec_a.getImplementationVendor_representative_telephone() != null && this.sec_a.getImplementationVendor_representative_telephone().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τηλέφωνο Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		if (this.sec_a.getImplementationVendor_representative_email() == null ||
				(this.sec_a.getImplementationVendor_representative_email() != null && this.sec_a.getImplementationVendor_representative_email().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Email Νομίμου Εκπροσώπου Φορέα Χρηματοδότησης στο Τμήμα Α.") );
		}
		
		goToSectionB();
		// Checks for Section B
		if (this.sec_b.getPhysicalObjectShortDescription() == null ||
				(this.sec_b.getPhysicalObjectShortDescription() != null && this.sec_b.getPhysicalObjectShortDescription().equals(""))) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Σύντομη Περιγραφή Φυσικού Αντικειμένου στο Τμήμα Β.") );
		}
		
		if (this.subProjects.size() == 0) {
			this.submittable = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχουν οριστεί Υποέργα στο Τμήμα Β.") );
		} else {
			for (Section_L l: this.subProjects) {
				if (l.getDate_start() == null) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ημερομηνία Έναρξης για το Υποέργο " + l.getGiven_id()) );
				}
				
				if (l.getDate_end() == null) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ημερομηνία Λήξης για το Υποέργο " + l.getGiven_id()) );
				}
				
				if (l.getTitle() == null ||
						(l.getTitle() == null && l.getTitle().equals(""))) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Τίτλος για το Υποέργο " + l.getGiven_id()) );
				}
				
				if (l.getSubProjectShortDescription() == null ||
						(l.getSubProjectShortDescription() != null && l.getSubProjectShortDescription().equals(""))) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Σύντομη Περιγραφή Φυσικού Αντικειμένου για το Υποέργο " + l.getGiven_id()) );
				}
				
				/* TODO: revisit this check later
				if (l.getContractorId() == null ||
						(l.getContractorId() != null && l.getContractorId().equals(-1))) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ανάδοχος για το Υποέργο " + l.getGiven_id()) );
				}*/
				
				List<TDY_Financial> tdy_financial = kbean.getTDYFinancialBySectionLId(l.getId());
				if (tdy_financial.size() == 0) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχουν οριστεί Επιλέξιμες Δαπάνες για το Υποέργο " + l.getGiven_id()) );
				}
				
				List<TDY_Evolution> tdy_evolution = kbean.getEvolutionBySectionLId(l.getId());
				if (tdy_evolution.size() == 0) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχουν οριστεί Διοικητικές Καταστάσεις Εξέλιξης για το Υποέργο " + l.getGiven_id()) );
				}
				
				List<TDY_ProgressActivities> tdy_progressActivities = kbean.getProgressActivitiesBySectionLId(l.getId());
				if (tdy_progressActivities.size() == 0) {
					this.submittable = false;
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχουν οριστεί Δραστηριότητες Προόδου για το Υποέργο " + l.getGiven_id()) );
				}
			}
		}
		
		if (this.submittable == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το ΤΔΕ πέρασε τους ελέγχους επιτυχώς!"));
		}
	}
	
	public String submitTDE() {
		checkTDE();
		
		
		if (this.sec_a.isEditable() == true) {
			this.sec_a.setSubmissionStatus(SubmissionStatus.SUBMITTED);
			this.sec_a = (Section_A) kbean.updateRecord(this.sec_a);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το ΤΔΕ υποβλήθηκε επιτυχώς"));
			refreshRevisions();
			return "submitted";
		}
		return "not_submitted";
	}
	
	public String approveTDE() {
		checkTDE();
		
		if (this.sec_a.isSubmitted() == true) {
			if (this.date_approval == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν έχει οριστεί Ημερομηνία Έγκρισης Φάσης ΤΔΕ στο Τμήμα Α.") );
				return "not_approved";
			}
			if( this.sec_a.getDate_submission() != null && this.date_approval != null && this.sec_a.getDate_submission().after( this.date_approval ) ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια έγκρισης δεν μπορεί να είναι προγενέστερη από την ημ/νια υποβολής.") );
				return "not_approved";
			}
			this.sec_a.setDate_approval(date_approval);
			this.sec_a.setSubmissionStatus(SubmissionStatus.APPROVED);
			this.sec_a = (Section_A) kbean.updateRecord(this.sec_a);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το ΤΔΕ εγκρίθηκε"));
			refreshRevisions();
			return "approved";
		}
		return "not_approved";
	}
	
	public String cancelApprovalTDE() {
		checkTDE();

		this.sec_a.setDate_approval(null);
		this.sec_a.setSubmissionStatus(SubmissionStatus.INCOMPLETE);
		this.sec_a = (Section_A) kbean.updateRecord(this.sec_a);

		FacesContext.getCurrentInstance().addMessage("", new FacesMessage("H έγκριση απορρίφθηκε"));
		refreshRevisions();
		return "not_approved";
	}
	
	public String goToTDY() {
		
		// start the objects of TDE to get data
		goToTDE();
		
		// start form Section_B to get SubProject Lists
		goToSectionB();
		
		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "tdy";
	}
	
	public String printTDE() {
		try {
			Section_A prj = (Section_A) this.revisionsdataTable.getRowData();
			this.revision = prj.getRevision();
		} catch (Exception e) {
			this.revision = kbean.getLatestRevisionNumber(Section_A.class, this.sectionClass);
		}

		this.sec_a  = (Section_A) kbean.getRecordByClassAndRevision(Section_A.class, this.sectionClass, this.revision );
		this.sec_b  = (Section_B) kbean.getRecordByClassAndRevision(Section_B.class, this.sectionClass, this.revision );
		this.sec_c  = (Section_C) kbean.getRecordByClassAndRevision(Section_C.class, this.sectionClass, this.revision );
		this.sec_d  = (Section_D) kbean.getRecordByClassAndRevision(Section_D.class, this.sectionClass, this.revision );
		this.sec_e  = (Section_E) kbean.getRecordByClassAndRevision(Section_E.class, this.sectionClass, this.revision );
		this.sec_h  = (Section_H) kbean.getRecordByClassAndRevision(Section_H.class, this.sectionClass, this.revision );
		this.sec_i  = (Section_I) kbean.getRecordByClassAndRevision(Section_I.class, this.sectionClass, this.revision );
		this.sec_k  = (Section_K) kbean.getRecordByClassAndRevision(Section_K.class, this.sectionClass, this.revision );
		this.sec_th = (Section_Th) kbean.getRecordByClassAndRevision(Section_Th.class, this.sectionClass, this.revision );
		this.sec_st = (Section_ST) kbean.getRecordByClassAndRevision(Section_ST.class, this.sectionClass, this.revision );
		this.sec_z  = (Section_Z) kbean.getRecordByClassAndRevision(Section_Z.class, this.sectionClass, this.revision );

		goToSectionA();
		goToSectionB();
		goToSectionC();
		goToSectionD();
		goToSectionE();
		goToSectionH();
		goToSectionI();
		goToSectionK();
		goToSectionST_print();
		goToSectionZ();
		goToSectionL_print();
		
		this.checkProcessed = false;

		return "printTDE";
	}

	public String goToMonthlyList() {
		this.sec_l = (Section_L) this.subProjectdataTable.getRowData();

		this.monthly_reports = kbean.getMonthlyReportsBySubProject(this.sec_l);
		
		clearAllDataTables();
		
		return "monthlyList";
	}
	
	public String goToSub3MonthlyList() {
		this.sec_l = (Section_L) this.subProjectdataTable.getRowData();

		this.sub3monthly_reports = kbean.getSub3MonthlyReportsBySubProject( this.sec_l);
		
		clearAllDataTables();
		
		return "monthly3List";
	}

	public String goToSectionA() {		
		clearAllDataTables();

		this.vendors = kbean.getVendorsBySectionAId(this.sec_a.getId());
		
		List<TDE_Vendors> defaultVendorsList = kbean.getVendorsBySectionAId(this.configId);
		this.defaultVendorsSelectItemsList = new ArrayList<SelectItem>();
		this.defaultVendorsSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (TDE_Vendors vendor: defaultVendorsList) {
			SelectItem s = new SelectItem(vendor.getId(), vendor.getName());
			this.defaultVendorsSelectItemsList.add(s);
		}
		
		regenerateRegionsSelectItems();
		regenerateProposalPrefectureSelectItems();
		regenerateProposalMunicipalitiesSelectItems();
		regenerateImplementationPrefectureSelectItems();
		regenerateImplementationMunicipalitiesSelectItems();
		regenerateOperationPrefectureSelectItems();
		regenerateOperationMunicipalitiesSelectItems();
		
		List<EUFramework> defaultEUFrameworksList = (List<EUFramework>) kbean.getAllRecordsOfType(EUFramework.class);
		this.defaultEUFrameworksSelectItemsList = new ArrayList<SelectItem>();
		this.defaultEUFrameworksSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (EUFramework frm: defaultEUFrameworksList) {
			SelectItem s = new SelectItem(frm.getId(), frm.getName());
			this.defaultEUFrameworksSelectItemsList.add(s);
		}
		
		if (this.sec_a.getFramework() != null && !this.sec_a.getFramework().equals(Integer.valueOf(-1)))
			this.framework  = (EUFramework) kbean.getRecordById(this.sec_a.getFramework(), EUFramework.class);
		else
			this.framework  = new EUFramework();
		System.out.println("framework: " + this.framework);
		
		if (this.sec_a.getProgramme() != null && !this.sec_a.getProgramme().equals(Integer.valueOf(-1)))
			this.programme  = (EUProgramme) kbean.getRecordById(this.sec_a.getProgramme(), EUProgramme.class);
		else
			this.programme  = new EUProgramme();
		System.out.println("programme: " + this.programme);
		
		if (this.sec_a.getAxis() != null && !this.sec_a.getAxis().equals(Integer.valueOf(-1)))
			this.axis  = (EUPriorityAxis) kbean.getRecordById(this.sec_a.getAxis(), EUPriorityAxis.class);
		else
			this.axis       = new EUPriorityAxis();
		System.out.println("axis: " + this.axis);
		
		if (this.sec_a.getMeasure() != null && !this.sec_a.getMeasure().equals(Integer.valueOf(-1)))
			this.measure  = (EUMeasure) kbean.getRecordById(this.sec_a.getMeasure(), EUMeasure.class);
		else
			this.measure    = new EUMeasure();
		System.out.println("measure: " + this.measure);
		
		if (this.sec_a.getSubmeasure() != null && !this.sec_a.getSubmeasure().equals(Integer.valueOf(-1)))
			this.submeasure  = (EUSubmeasure) kbean.getRecordById(this.sec_a.getSubmeasure(), EUSubmeasure.class);
		else
			this.submeasure = new EUSubmeasure();
		System.out.println("submeasure: " + this.submeasure);
		
		if (this.sec_a.getSection() != null && !this.sec_a.getSection().equals(Integer.valueOf(-1)))
			this.section  = (EUFPSSection) kbean.getRecordById(this.sec_a.getSection(), EUFPSSection.class);
		else
			this.section    = new EUFPSSection();
		System.out.println("section: " + this.section);
		
		if (this.sec_a.getSubsection() != null && !this.sec_a.getSubsection().equals(Integer.valueOf(-1)))
			this.subsection  = (EUFPSSubsection) kbean.getRecordById(this.sec_a.getSubsection(), EUFPSSubsection.class);
		else
			this.subsection    = new EUFPSSubsection();
		System.out.println("subsection: " + this.subsection);
		
		if (this.sec_a.getProjectAction() != null && !this.sec_a.getProjectAction().equals(Integer.valueOf(-1)))
			this.projectaction  = (ActionsCategory) kbean.getRecordById(this.sec_a.getProjectAction(), ActionsCategory.class);
		else
			this.projectaction  = new ActionsCategory();
		System.out.println("projectaction: " + this.projectaction);
		
		return "section_a";
	}
	
	public String goToSectionB() {
		clearAllDataTables();

		regenerateRegionsSelectItems();

		// get the subproject list from the connector class
		this.subProjects = kbean.getSubProjectsBySectionBId(this.sec_b.getId() );
		this.projectPlaces = kbean.getProjectPlacesBySectionBId( sec_b.getId() );
		
		return "section_b";
	}
	
	public String goToSectionC() {
		clearAllDataTables();
		
		List<EUFramework> defaultEUFrameworksList = (List<EUFramework>) kbean.getAllRecordsOfType(EUFramework.class);
		this.defaultEUFrameworksSelectItemsList = new ArrayList<SelectItem>();
		this.defaultEUFrameworksSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (EUFramework frm: defaultEUFrameworksList) {
			SelectItem s = new SelectItem(frm.getId(), frm.getName());
			this.defaultEUFrameworksSelectItemsList.add(s);
		}
		
		if (this.sec_a.getFramework() != null && !this.sec_a.getFramework().equals(Integer.valueOf(-1)))
			this.framework  = (EUFramework) kbean.getRecordById(this.sec_a.getFramework(), EUFramework.class);
		else
			this.framework  = new EUFramework();
		System.out.println("framework: " + this.framework);
		
		this.saveable.put(Section_C.class, true);
		return "section_c";
	}
	
	public String goToSectionD() {
		clearAllDataTables();

		// SECTION D
		this.deiktes_ekrois        = kbean.getDeiktesBySectionDId( this.sec_d.getId(), DeiktesType.EKROIS );
		this.deiktes_apotelesmatos = kbean.getDeiktesBySectionDId( this.sec_d.getId(), DeiktesType.APOTELESMATOS );
		this.deiktes_epiptoseon    = kbean.getDeiktesBySectionDId( this.sec_d.getId(), DeiktesType.EPIPTOSEON );
		
		List<TDE_Deiktes> defaultDeiktesList;
		if (this.sec_a.getProjectAction() != null && this.sec_a.getProjectAction().compareTo(-1) != 0) {
			ActionsCategory action = (ActionsCategory) kbean.getRecordById(this.sec_a.getProjectAction(), ActionsCategory.class);
			defaultDeiktesList = kbean.getAllDeiktesByActionCategory(action);
		} else {
			defaultDeiktesList = new ArrayList<TDE_Deiktes>();
		}
		
		this.defaultDeiktesSelectItemsList = new ArrayList<SelectItem>();
		this.defaultDeiktesSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (TDE_Deiktes t : defaultDeiktesList) {
			SelectItem s = new SelectItem(t.getId(), t.getCode()+ ": "+ t.getName());
			this.defaultDeiktesSelectItemsList.add(s);
		}
		
		return "section_d";
	}
	
	public String goToSectionE() {
		clearAllDataTables();

		// SECTION E
		this.permissions = kbean.getPermissionsBySectionEId( sec_e.getId() );
		this.analysis = kbean.getAnalysisBySectionEId( this.sec_e.getId() );
		this.katartisis = kbean.getKatartisisBySectionEId( this.sec_e.getId() );
		this.tasks = kbean.getTasksBySectionEId( this.sec_e.getId() );
		this.vendorTasks = kbean.getVendorTasksBySectionEId( this.sec_e.getId() );
		
/*		for (TDE_Analysis ta: this.analysis) {
			System.out.println("TDE_Analysis: " + ta.getId() + ", status: " + ta.getStatus() + 
					", required: " + ta.getRequired() + ", expires: " + ta.getExpiration().toString() + ", comments: " + ta.getComments());
		}
		for (TDE_Katartisis tk: this.katartisis) {
			System.out.println("TDE_Katartisis: " + tk.getId() + ", status: " + tk.getStatus() + 
					", required: " + tk.getRequired() + ", type: " + tk.getType());
		}*/
		return "section_e";
	}
	
	public String goToSectionH() {
		this.saveable.put(Section_H.class, true);
		return "section_h";
	}
	
	public String goToSectionTh() {
		this.saveable.put(Section_Th.class, true);
		
		clearAllDataTables();
		
		return "section_th";
	}
	
	public String goToSectionI() {			
		this.section_I_employees_currQty = 0;
		this.section_I_employees_estQty = 0;

		clearAllDataTables();

		// SECTION I
		if (this.sec_i != null) { 
			this.section_I_employees = kbean.getEmployeesBySectionIId( sec_i.getId() );

			// analyze lists to get total number of employees

			for( Iterator iter = this.section_I_employees.iterator(); iter.hasNext(); ) {
				TDE_Employees tmp = (TDE_Employees) iter.next();

				this.section_I_employees_currQty += tmp.getCurrentQty();
				this.section_I_employees_estQty += tmp.getEstQty();
			}
		}
		
		this.saveable.put(Section_I.class, true);
		
		return "section_i";
	}
	
	public String goToSectionK() {
		this.section_K_employees_currQty = 0;
		this.section_K_employees_estQty = 0;

		clearAllDataTables();

		// SECTION K
		if (this.sec_k != null) {
			this.section_K_employees = kbean.getEmployeesBySectionKId( sec_k.getId() );

			// analyze lists to get total number of employees

			for( Iterator iter = this.section_K_employees.iterator(); iter.hasNext(); ) {
				TDE_Employees tmp = (TDE_Employees) iter.next();

				this.section_K_employees_currQty += tmp.getCurrentQty();
				this.section_K_employees_estQty += tmp.getEstQty();
			}
		}

		this.saveable.put(Section_K.class, true);
		
		return "section_k";
	}
	
	public void sectionL_regenerateTDYProgressSelectItems() {
		ActionsCategory action = (ActionsCategory) kbean.getRecordById(this.sec_l.getCategory_id(), ActionsCategory.class);
		if (action != null) {
			this.sec_l.setProjectAction(action);
			this.sec_l.setCategory(action.getCategory());
			this.sec_l.setCategory_code(action.getCode());
		}
		
		List<TDY_Progress> defaultProgressList;
		if (this.sec_l.getProjectAction() != null ) {
			defaultProgressList = kbean.getProgressByActionCategory(this.sec_l.getProjectAction());
			System.out.println("Section L category: " + this.sec_l.getProjectAction().getId() + ": progress items: " + defaultProgressList.size());
		} else {
			defaultProgressList = new ArrayList<TDY_Progress>();
		}
		this.defaultProgressSelectItemsList.clear();
		this.defaultProgressSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (TDY_Progress t : defaultProgressList) {
			SelectItem s = new SelectItem(t.getId(), t.getCode()+": "+t.getDescription());
			this.defaultProgressSelectItemsList.add(s);
		}
	}
	
	public String goToSectionL() {
		
		this.sec_l = (Section_L) this.subProjectdataTable.getRowData();
		
		clearAllDataTables();
		sectionL_regenerateTDYProgressSelectItems();
		
		this.vendor = new TDE_Vendors();
		this.supervisingVendorsSelectItemsList = new ArrayList<SelectItem>();
		this.supervisingVendorsSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (TDE_Vendors vendor: this.vendors) {
			SelectItem s = new SelectItem(vendor.getId(), vendor.getName());
			this.supervisingVendorsSelectItemsList.add(s);
		}
		
		this.expensesSelectItemsList = new ArrayList<SelectItem>();
		this.expensesSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		if (this.sec_a.getFramework() != null) {
			EUFramework frm = (EUFramework) kbean.getRecordById(this.sec_a.getFramework(), EUFramework.class);
			List<TDY_Financial> expensesList = kbean.getTDYFinancialByKPS_SectionLId(frm, this.configId);
			for (TDY_Financial t : expensesList) {
				SelectItem s = new SelectItem(t.getId(), t.getCode()+": "+t.getCategory());
				this.expensesSelectItemsList.add(s);
			}
		}
		
		regenerateEvolutionSelectItems();

		this.tdy_progress = kbean.getProgressBySectionLId( this.sec_l.getId() );
		this.tdy_places = kbean.getPlacesBySectionLId( this.sec_l.getId() );
		this.tdy_deiktes = kbean.getDeiktesBySectionLId( this.sec_l.getId() );
		this.tdy_progressActivities = kbean.getProgressActivitiesBySectionLId( this.sec_l.getId() );
		this.tdy_financial = kbean.getTDYFinancialBySectionLId( this.sec_l.getId() );
		this.tdy_evolution = kbean.getEvolutionBySectionLId( this.sec_l.getId() );

		BigDecimal v = BigDecimal.ZERO;
		for( TDY_Financial f : this.tdy_financial ) {
			v = v.add(f.getBudget());
		}
		this.sec_l.setBudget(v);

		this.contractors = (List<Contractor>) ubean.getAllRecordsOfType(Contractor.class);
		this.contractorSelectItems.clear();
		for (Iterator i = contractors.iterator(); i.hasNext();) {
			Contractor c = (Contractor) i.next();
			contractorSelectItems.add(new SelectItem(c.getId(), c.getName()));
		}

		if( this.sec_l.getContractorId() != null && this.sec_l.getContractorId().intValue() > 0 )
			this.contractor = (Contractor) kbean.getRecordById(this.sec_l.getContractorId(), Contractor.class );
		else
			this.contractor = new Contractor();
		
		List<SubProjectType> defaultSubProjectTypesList = (List<SubProjectType>) kbean.getAllRecordsOfType(SubProjectType.class);
		this.defaultSubProjectTypesSelectItemsList = new ArrayList<SelectItem>();
		for (SubProjectType sub: defaultSubProjectTypesList) {
			SelectItem s = new SelectItem(sub, sub.getType());
			this.defaultSubProjectTypesSelectItemsList.add(s);
		}
		
		return "section_l";
	}
	
	public String goToSectionL_print() {
		this.all_tdy_progress			= new HashMap<Section_L, List<TDY_Progress>>();
		this.all_tdy_places				= new HashMap<Section_L, List<TDY_Places>>();
		this.all_tdy_deiktes			= new HashMap<Section_L, List<TDY_Deiktes>>();
		this.all_tdy_progressActivities = new HashMap<Section_L, List<TDY_ProgressActivities>>();
		this.all_tdy_financial			= new HashMap<Section_L, List<TDY_Financial>>();
		this.all_tdy_evolution			= new HashMap<Section_L, List<TDY_Evolution>>();
		this.all_contractors 			= new HashMap<Section_L, Contractor>();

		for (Iterator iter = this.subProjects.iterator(); iter.hasNext();) {
			Section_L prj = (Section_L) iter.next();

			this.all_tdy_progress.put(prj, kbean.getProgressBySectionLId(prj.getId()));
			this.all_tdy_places.put(prj, kbean.getPlacesBySectionLId(prj.getId()));
			this.all_tdy_deiktes.put(prj, kbean.getDeiktesBySectionLId(prj.getId()));
			this.all_tdy_progressActivities.put(prj, kbean.getProgressActivitiesBySectionLId(prj.getId()));
			this.all_tdy_financial.put(prj, kbean.getTDYFinancialBySectionLId(prj.getId()));
			this.all_tdy_evolution.put(prj, kbean.getEvolutionBySectionLId(prj.getId()));

			BigDecimal v = BigDecimal.ZERO;
			for( TDY_Financial f : this.all_tdy_financial.get(prj) ) {
				v = v.add(f.getBudget());
			}
			prj.setBudget(v);

			if( prj.getContractorId() != null && prj.getContractorId().intValue() > 0 ) {
				Contractor con = (Contractor) kbean.getRecordById(prj.getContractorId(), Contractor.class );
				if (con != null) this.all_contractors.put(prj, con);
			}
		}
		
		return "section_l";
	}

	public String gotoNomikesDesmefseis() {
		clearAllDataTables();
		
		goToTDE();
		goToSectionB();

		this.nomList = kbean.getNomikesDesmefseisBySectionBId(this.sec_b.getId());
		
		this.checkProcessed = false;
		
		return "nomList";
	}
	
	public String goToSectionM() {
		this.sec_m = (Section_M) this.nomikesDesmefseisDataTable.getRowData();
		
		clearAllDataTables();
		
		// load section B
		goToTDE();
		goToSectionB();

		this.nom_contractors = this.sec_m.getContractors();
		this.subProjects = kbean.getSubProjectsBySectionBId(this.sec_b.getId());
		List<Section_L> free_subProjects = kbean.getSubProjectsBySectionBNotInSectionM(this.sec_b.getId());
		
		this.selectedNomSubProjects = new HashMap<NOM_SubProjects, Boolean>();
		for(NOM_SubProjects noms: this.sec_m.getSubprojects()) {
			this.selectedNomSubProjects.put(noms, true);
			System.out.println("Nom = " + noms.getId() + ", " + noms.getSubProject().getGiven_id() + ", true");
		}

		for (Section_L subprj: free_subProjects) {
			NOM_SubProjects noms = new NOM_SubProjects();
			noms.setSubProject(subprj);
			noms.setAmount(subprj.getBudget());
			noms.setSelectableAmount(subprj.getBudget());
			noms.setParentSectionM(this.sec_m);
			this.sec_m.addSubProject(noms);
			this.selectedNomSubProjects.put(noms, false);
			System.out.println("Nom = " + noms.getId() + ", " + noms.getSubProject().getGiven_id() + ", free");
		}
		
		System.out.println("selectedNomSubProjects.size = " + this.selectedNomSubProjects.size());
		for (Iterator iter = this.selectedNomSubProjects.entrySet().iterator(); iter.hasNext();) {
			Entry<NOM_SubProjects, Boolean> entry = (Entry<NOM_SubProjects, Boolean>) iter.next();
			NOM_SubProjects noms = entry.getKey();
			System.out.println("Nom = " + noms.getId() + ", " + noms.getSubProject().getGiven_id() + ", " + entry.getValue());
		}

		this.contractors = (List<Contractor>) ubean.getAllRecordsOfType(Contractor.class);
		this.contractorSelectItems.clear();
		this.contractorSelectItems.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (Iterator i = contractors.iterator(); i.hasNext();) {
			Contractor c = (Contractor) i.next();
			String name = (c.getName().length() > 50) ? c.getName().substring(0, 50)+"..." : c.getName();
			SelectItem s = new SelectItem( c.getId(), name);
			contractorSelectItems.add(s);
		}
		
		this.label = "";
		
		return "section_m";
	}

	public String goToSectionN() {
		this.sec_n = (Section_N) this.tmp_dataTable.getRowData();

		// -	before that, we need to load up our vars we need Invoices for Contractor and Vendor.
		// create the dates that will be used for this
		Calendar c = Calendar.getInstance();
		c.setTime( this.sec_n.getDate_n() );
		c.add(Calendar.MONTH, 1);

		System.out.println( "DEBUG: start date is : " + this.sec_n.getDate_n() + " and end date is: " + c.getTime() );

		String contractorName = "";
		String vat = "", doy = "";
		Section_M m = kbean.getLastNomikiDesmefsiBySubProject(this.sec_l);
		if (m != null) {
			if (m.isContractorsUnion() == false) {
				List<NOM_Contractor> contractors= kbean.getContractorsBySubProject(this.sec_l);
				for (NOM_Contractor con: contractors) {
					System.out.println("subproject: " + this.sec_l.getId() + ", contractor: " + con.getContractor().getName());
				}
				if (contractors.size() > 0) {
					this.contractor = contractors.get(0).getContractor();
					contractorName = this.contractor.getFullName();
					vat = this.contractor.getVAT();
					doy = this.contractor.getDOY();
				}
			} else {
				contractorName = m.getUnionTitle();
			}
		}		
		
		List<Invoice_Vendor> inv_ven = kbean.getInvoiceVendorListBySubProjectDates( this.sec_l, this.sec_n.getDate_n(), c.getTime() );
		
		DateFormat dd  = new SimpleDateFormat("dd/MM/yyyy");
		
		String outputHTML = "";			
		outputHTML += "<table align=\"center\" class=\"data-table\" cellspacing=\"0\" cellpadding=\"5\">";
		outputHTML += "<tr>";
		outputHTML += "<td></td>";
		outputHTML += "<td colspan=\"4\" align=\"center\"><b>ΠΑΡΑΣΤΑΤΙΚΑ ΑΝΑΔΟΧΟΥ</b></td>";
		outputHTML += "<td colspan=\"8\" align=\"center\"><b>ΠΑΡΑΣΤΑΤΙΚΑ ΦΟΡΕΑ ΥΛΟΠΟΙΗΣΗΣ (Φ.Υ.)</b></td>";
		outputHTML += "<td colspan=\"3\" align=\"center\"><b>ΣΥΣΧΕΤΙΣΜΟΣ</b></td>";
		outputHTML += "</tr>";

		// add the cells for all this info
		outputHTML += "<tr>";
		outputHTML += "<td align=\"center\">Α/Α</td>";
		outputHTML += "<td align=\"center\">ΚΩΔ. ΕΙΔΟΥΣ ΠΑΡΑΣΤ.</td>";
		outputHTML += "<td align=\"center\">ΑΡΙΘ. ΠΑΡΑΣΤ.</td>";
		outputHTML += "<td align=\"center\">ΗΜ/ΝΙΑ ΕΚΔΟΣΗΣ</td>";
		outputHTML += "<td align=\"center\">ΠΟΣΟ(ΜΕ ΦΠΑ)</td>";

		outputHTML += "<td align=\"center\">ΗΜ/ΝΙΑ ΠΛΗΡΩΜΗΣ</td>";
		outputHTML += "<td align=\"center\">ΚΩΔ. ΕΙΔΟΥΣ ΠΑΡΑΣΤ.</td>";
		outputHTML += "<td align=\"center\">ΑΡΙΘ. ΠΑΡΑΣΤ.</td>";
		outputHTML += "<td align=\"center\">ΣΥΝΟΛΙΚΟ ΠΟΣΟ ΠΛΗΡΩΜΗΣ</td>";
		outputHTML += "<td align=\"center\">ΚΑΘΑΡΟ ΠΟΣΟ ΠΛΗΡΩΜΗΣ</td>";
		outputHTML += "<td align=\"center\">ΦΠΑ</td>";
		outputHTML += "<td align=\"center\">ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ ΠΛΗΡΩΜΗΣ</td>";
		outputHTML += "<td align=\"center\">ΑΙΤΙΟΛΟΓΙΑ ΠΛΗΡΩΜΗΣ</td>";

		outputHTML += "<td align=\"center\">ΚΑΤΗΓ.ΕΠΙΛΕΞ. ΔΑΠΑΝΗΣ</td>";
		outputHTML += "<td align=\"center\">ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ ΑΝΑ ΚΑΤΗΓΟΡΙΑ ΕΠΙΛ. ΔΑΠΑΝΗΣ</td>";
		outputHTML += "<td align=\"center\">ΕΙΔΟΣ ΔΑΠΑΝΗΣ</td>";
		outputHTML += "</tr>";
		outputHTML += "<tr>";
		outputHTML += "<td align=\"center\">(1)</td>";
		outputHTML += "<td align=\"center\">(2)</td>";
		outputHTML += "<td align=\"center\">(3)</td>";
		outputHTML += "<td align=\"center\">(4)</td>";
		outputHTML += "<td align=\"center\">(5)</td>";

		outputHTML += "<td align=\"center\">(6)</td>";
		outputHTML += "<td align=\"center\">(7)</td>";
		outputHTML += "<td align=\"center\">(8)</td>";
		outputHTML += "<td align=\"center\">(9)</td>";
		outputHTML += "<td align=\"center\">(10)</td>";
		outputHTML += "<td align=\"center\">(11)</td>";
		outputHTML += "<td align=\"center\">(12)</td>";
		outputHTML += "<td align=\"center\">(13)</td>";

		outputHTML += "<td align=\"center\">(14)</td>";
		outputHTML += "<td align=\"center\">(15)</td>";
		outputHTML += "<td align=\"center\">(16)</td>";
		outputHTML += "</tr>";

		// add the contractor line aight?
		outputHTML += "<tr><td colspan=\"16\" align=\"center\">";
		outputHTML += "<b>ΑΝΑΔΟΧΟΣ : </b>" + contractorName + "&nbsp;&nbsp;<b>ΑΦΜ : </b>" + vat + "&nbsp;&nbsp;&nbsp;<b>ΔΟΥ : </b>" + doy;
		outputHTML += "</td></tr>";

		if( inv_ven.size() <= 0 ) {
			outputHTML += "<tr><td colspan=\"16\" align=\"center\">";
			outputHTML += "<b>ΔΕΝ ΥΠΑΡΧΟΥΝ ΔΑΠΑΝΕΣ ΣΕ ΑΥΤΟΝ ΤΟΝ ΜΗΝΑ</b>";
			outputHTML += "</td></tr>";
			
			outputHTML += "</table>";

			this.reportOutputHTML = outputHTML;
			return "section_n";
		}
		
		int counter = 1;

		String invoiceHTML = "", blankInvoiceHTML = "";
		String paymentHTML = "", blankPaymentHTML = "";
		String expenseHTML = "", blankExpenseHTML = "";
		
		blankInvoiceHTML += "<td align=\"center\"></td>";
		blankInvoiceHTML += "<td align=\"center\"></td>";
		blankInvoiceHTML += "<td align=\"center\"></td>";
		blankInvoiceHTML += "<td align=\"center\"></td>";
		
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		blankPaymentHTML += "<td align=\"center\"></td>";
		
		blankExpenseHTML += "<td align=\"center\"></td>";
		blankExpenseHTML += "<td align=\"center\"></td>";
		blankExpenseHTML += "<td align=\"center\"></td>";
		
		for( Invoice_Vendor iv: inv_ven) {
			List<InvoicePayment> payments = iv.getPaidInvoices();
			
			paymentHTML = "<td align=\"center\">" + dd.format( iv.getDate_payment() ) + "</td>";
			paymentHTML += "<td align=\"center\">" + iv.getInvoiceType().getType() + "</td>";
			paymentHTML += "<td align=\"center\">" + iv.getInvoiceNumber() + "</td>";
			paymentHTML += "<td align=\"center\">" + currencyFormatter.format(iv.getAmount()) + "</td>";
			paymentHTML += "<td align=\"center\">" + currencyFormatter.format(iv.getNet()) + "</td>";
			paymentHTML += "<td align=\"center\">" + currencyFormatter.format(iv.getVat()) + "</td>";
			paymentHTML += "<td align=\"center\">" + currencyFormatter.format(iv.getAmount_selectable()) + "</td>";
			paymentHTML += "<td align=\"center\">" + iv.getComments() + "</td>";
			
			if (payments.size() > 0) {
				for (InvoicePayment payment: payments) {
					invoiceHTML  = "<td align=\"center\">" + payment.getInvoice().getInvoiceType().getType() + "</td>";
					invoiceHTML += "<td align=\"center\">" + payment.getInvoice().getInvoiceNumber() + "</td>";
					invoiceHTML += "<td align=\"center\">" + dd.format( payment.getInvoice().getDate_issue() ) + "</td>";
					invoiceHTML += "<td align=\"center\">" + currencyFormatter.format(payment.getInvoice().getTotal()) + "</td>";
					
					if (payment.getPaidExpenses().size() > 0) {
						for (InvoiceExpense expense: payment.getPaidExpenses()) {
							// don't print expenses with zero payments.
							if (expense.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) continue;
							
							expenseHTML = "<td align=\"center\">" + expense.getExpense().getCode() + "</td>";
							expenseHTML += "<td align=\"center\">" + currencyFormatter.format(expense.getPaidAmount()) + "</td>";
							expenseHTML += "<td align=\"center\">" + expense.getExpense().getCategory() + "</td>";
							
							outputHTML += "<tr>";
							outputHTML += "<td align=\"center\">" + counter + "</td>";
							outputHTML += invoiceHTML + paymentHTML + expenseHTML;
							outputHTML += "</tr>";
							counter++;
						}
					} else {
						outputHTML += "<tr>";
						outputHTML += "<td align=\"center\">" + counter + "</td>";
						outputHTML += invoiceHTML + paymentHTML + blankExpenseHTML;
						outputHTML += "</tr>";
						counter++;
					}
				}
			}
		}

		outputHTML += "</table>";

		this.reportOutputHTML = outputHTML;

		return "section_n";
	}

	public String goToSectionNList() {
		// we also need the TDE to get info so get them as well
		goToTDE();
		goToTDY();
		
		this.checkProcessed = false;
		
		return "sectionNList";
	}

	public String goToSectionO() {
		this.sec_o = (Section_O) this.reportsListDataTable.getRowData();

		clearAllDataTables();
		
		this.m3_tde_deiktes = (List<M3_TDE_Deiktes>) kbean.getRecordsByQuery("sec_o_id=" + this.sec_o.getId(), M3_TDE_Deiktes.class );
		this.m3_tde_places = (List<M3_TDE_Places>) kbean.getRecordsByQuery("sec_o_id=" + this.sec_o.getId(), M3_TDE_Places.class );
		this.m3_tde_problems = (List<M3_TDE_Problems>) kbean.getRecordsByQuery("sec_o_id=" + this.sec_o.getId(), M3_TDE_Problems.class );
		this.m3_tde_semesters = new ArrayList<M3_TDE_Semesters>();

		for (M3_TDE_Places mp: this.m3_tde_places) {
			System.out.println("Id: " + mp.getId() + ", region: " + mp.getRegionName());
		}
		
		this.tmp_m3_tde_problems = new M3_TDE_Problems();

		// in here we must construct some custom html tables im afraid.
		// first one is table E1 dapanes tou ergou

		// we need to goToSectionST first - that will give us the subprojects list for this project
		goToSectionST();
		
		// now, before we start creating html we need to load the objects that will be needed.
		// first init the dates we will be searching for
		Calendar c_start = Calendar.getInstance();
		Calendar c_end = Calendar.getInstance();
		// init total doubles
		BigDecimal total_net = BigDecimal.ZERO;
		BigDecimal total_vat = BigDecimal.ZERO;
		BigDecimal total_total_net = BigDecimal.ZERO;
		BigDecimal total_subproject_bugdet = BigDecimal.ZERO;
		// init output String
		String output = "";
		// get total budget
		this.section_o_total_budget = BigDecimal.ZERO;
		for( Section_L secl : this.subProjects ) {
			this.section_o_total_budget = this.section_o_total_budget.add(secl.getBudget());
		}

		output += "<table style=\"text-align:center;\" class=\"data-table\">";
		output += "<tr>";
		output += "<td rowspan=\"2\"><b>ΚΩΔ.</b></td>";
		output += "<td rowspan=\"2\"><b>ΤΙΤΛΟΣ ΥΠΟΕΡΓΟΥ</b></td>";
		output += "<td rowspan=\"2\"><b>EΠΙΛΕΞΙΜΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ</b></td>";
		output += "<td colspan=\"3\"><b>1ος ΜΗΝΑΣ ΤΡΙΜΗΝΟΥ</b></td>";
		output += "<td colspan=\"3\"><b>2ος ΜΗΝΑΣ ΤΡΙΜΗΝΟΥ</b></td>";
		output += "<td colspan=\"3\"><b>3ος ΜΗΝΑΣ ΤΡΙΜΗΝΟΥ</b></td>";
		output += "<td colspan=\"3\"><b>ΣΥΝΟΛΟ ΤΡΙΜΗΝΟΥ</b></td>";
		output += "</tr>";
		output += "<tr>";
		output += "<td>ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ</td>";
		output += "<td>ΦΠΑ</td>";
		output += "<td>ΣΥΝΟΛΟ</td>";
		output += "<td>ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ</td>";
		output += "<td>ΦΠΑ</td>";
		output += "<td>ΣΥΝΟΛΟ</td>";
		output += "<td>ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ</td>";
		output += "<td>ΦΠΑ</td>";
		output += "<td>ΣΥΝΟΛΟ</td>";
		output += "<td>ΕΠΙΛΕΞΙΜΟ ΠΟΣΟ</td>";
		output += "<td>ΦΠΑ</td>";
		output += "<td>ΣΥΝΟΛΟ</td>";
		output += "</tr>";

		// we need: Invoice_Contractor for every month, for every subproject.
		for( Section_L sub : this.subProjects ) {
			c_start.set( Calendar.YEAR, this.sec_o.getYear()  );
			c_start.set( Calendar.DAY_OF_MONTH, 1  );
			c_start.set( Calendar.HOUR_OF_DAY, 1 );
			c_start.set( Calendar.MINUTE, 1 );
			c_end.set( Calendar.YEAR, this.sec_o.getYear()  );
			c_end.set( Calendar.DAY_OF_MONTH, 1  );
			c_end.set( Calendar.HOUR_OF_DAY, 1 );
			c_end.set( Calendar.MINUTE, 1 );

			// init amount numbers
			BigDecimal net = BigDecimal.ZERO;
			BigDecimal vat = BigDecimal.ZERO;

			Integer[] months = new Integer[3];

			if( this.sec_o.getQuarter().equals(1) ) {
				months[0] = 1; months[1] = 2; months[2] = 3;
			}
			if ( this.sec_o.getQuarter().equals(2) ) {
				months[0] = 4; months[1] = 5; months[2] = 6;
			}
			if ( this.sec_o.getQuarter().equals(3) ) {
				months[0] = 7; months[1] = 8; months[2] = 9;
			}
			if ( this.sec_o.getQuarter().equals(4) ) {
				months[0] = 10; months[1] = 11; months[2] = 12;
			}

			// FIRST 3 columns
			output += "<tr>";
			output += "<td>" + sub.getGiven_id() + "</td>";
			output += "<td>" + sub.getTitle() + "</td>";
			output += "<td>" + currencyFormatter.format(sub.getBudget()) + "</td>";

			total_subproject_bugdet = total_subproject_bugdet.add(sub.getBudget());

			for( Integer m : months ) {
				c_start.set( Calendar.MONTH, m );
				c_end.set( Calendar.MONTH, m );
				c_end.add( Calendar.MONTH, 1 );

				List<Invoice_Contractor> ic = (List<Invoice_Contractor>) kbean.getInvoiceContractorListByDates( c_start.getTime(), c_end.getTime() );

				System.out.println( ":RESULTS:: " + ic.size() );

				for( Invoice_Contractor icItem : ic ) {
					if( icItem.getSubProjectId().equals( sub.getId() ) ) {
						System.out.println("im in here and my sybprohect name is: " + sub.getTitle() );

						net = net.add(icItem.getNet());
						vat = vat.add(icItem.getVat());
						// fix totals
						total_net = total_net.add(net);
						total_vat = total_vat.add(vat);
					}
				}

				// now create the Output
				output += "<td>" + currencyFormatter.format(net) + "</td>";
				output += "<td>" + currencyFormatter.format(vat) + "</td>";
				output += "<td>" + currencyFormatter.format(net.add(vat)) + "</td>";

				total_total_net = total_total_net.add(net);
				total_total_net = total_total_net.add(vat);
				net = BigDecimal.ZERO;
				vat = BigDecimal.ZERO;
			}

			output += "<td>"+ currencyFormatter.format(total_net) + "</td>";
			output += "<td>" + currencyFormatter.format(total_vat) + "</td>";
			output += "<td>" + currencyFormatter.format(total_net.add(total_vat)) + "</td>";
			output += "</tr>";				

			total_net = BigDecimal.ZERO;
			total_vat = BigDecimal.ZERO;

			// now work on List for table E3

			List<M3_TDE_Semesters> ls = kbean.getSemestersBySectionOSubproject( this.sec_o.getId(), sub );
			this.m3_tde_semesters.addAll(ls);
		}			

		output += "<tr>";
		output += "<td colspan=\"2\" style=\"text-align:center;\"><b>ΣΥΝΟΛΑ</b></td>";
		output += "<td style=\"text-align:center;\">" + currencyFormatter.format(total_subproject_bugdet) +"</td>";
		output += "</tr>";
		output += "<tr>";
		output += "<td colspan=\"2\" style=\"text-align:center;\"><b>ΣΥΝΟΛΙΚΕΣ EΠΙΛΕΞΙΜΕΣ ΔΑΠΑΝΕΣ ΜΕΧΡΙ ΚΑΙ ΤΟ ΤΡΕΧΟΝ ΤΡΙΜΗΝΟ</b></td>";
		output += "<td style=\"text-align:center;\">" + currencyFormatter.format( total_total_net ) + "</td>";
		output += "</tr>";
		output += "<tr>";
		output += "<td colspan=\"2\" style=\"text-align:center;\"><b>ΠΟΣΟΣΤΟ ΟΙΚΟΝΟΜΙΚΗΣ ΠΡΟΟΔΟΥ</b></td>";
		if( total_subproject_bugdet.compareTo(BigDecimal.ZERO) > 0 ) {
			double perc = (total_total_net.doubleValue() / total_subproject_bugdet.doubleValue()) * 100;
			output += "<td style=\"text-align:center;\">" + currencyFormatter.format(perc) + "%</td>";
		}
		else output += "<td style=\"text-align:center;\">" + 0 + "%</td>";
		output += "</tr>";

		output += "</table>";

		this.reportE1HTML = output;

		BigDecimal total = BigDecimal.ZERO;
		for( Section_L sub : this.subProjects ) {
			if (sub.getDate_start() != null && sub.getDate_end() != null) {
				Map<String, Boolean> semesterMatrix = new HashMap<String, Boolean>();
				Map<String, BigDecimal> semesterBudgetMatrix = new HashMap<String, BigDecimal>();
				
				for (Integer y: this.framework.getYears()) {
					semesterMatrix.put(y.toString() + 'A', false);
					semesterMatrix.put(y.toString() + 'B', false);
				}
				
				List<YearQuarters> yq = DateUtil.getQuartersList( sub.getDate_start(), sub.getDate_end() );

				for( YearQuarters s : yq ) {
					if( s.getQuarters().contains(1) | s.getQuarters().contains(2) )
						semesterMatrix.put(s.getYear().toString() + 'A', true);
					if( s.getQuarters().contains(3) | s.getQuarters().contains(4) )
						semesterMatrix.put(s.getYear().toString() + 'B', true);
				}
				sub.setSemesterMatrix(semesterMatrix);

				String key = "";
				for( M3_TDE_Semesters ss : this.m3_tde_semesters ) {
					if( ss.getSubProject().getId().equals( sub.getId() ) ) {
						total = total.add(ss.getAmount());
						if (ss.getSemester().compareTo(1) == 0) {
							key = ss.getYear().toString() + 'A';
						}
						if (ss.getSemester().compareTo(2) == 0) {
							key = ss.getYear().toString() + 'B';
						}
						semesterBudgetMatrix.put(key, ss.getAmount());
					}
				}
				sub.setSemesterBudgetMatrix(semesterBudgetMatrix);
			}
		}
		
		this.sec_o.setTotalBudget(total);
		
		if ( this.sec_o.getStatus() == SubmissionStatus.APPROVED )
			return "section_o_view";
		else
			return "section_o";
	}
	
	public String goToSectionOList() {
		goToTDE();		
					
		this.prj3monthly_reports = kbean.get3MonthlyReportsByNum( this.sectionClass );

		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "sectionOList";
	}
	
	public String goToSectionP() {
		this.sec_p = (Section_P) this.reportsListDataTable.getRowData();

		// we also need the TDE to get info so get them as well
		goToTDE();

		this.m3_tdy_certifications = (List<M3_TDY_Certification>) kbean.getRecordsByQuery("sec_p_id=" + this.sec_p.getId(), M3_TDY_Certification.class );
		this.m3_tdy_deiktes = (List<M3_TDY_Deiktes>) kbean.getRecordsByQuery("sec_p_id=" + this.sec_p.getId(), M3_TDY_Deiktes.class );
		this.m3_tdy_evolution = (List<M3_TDY_Evolution>) kbean.getRecordsByQuery("sec_p_id=" + this.sec_p.getId(), M3_TDY_Evolution.class );
		this.m3_tdy_places = (List<M3_TDY_Places>) kbean.getRecordsByQuery("sec_p_id=" + this.sec_p.getId(), M3_TDY_Places.class );
		this.m3_tdy_progress = (List<M3_TDY_Progress>) kbean.getRecordsByQuery("sec_p_id=" + this.sec_p.getId(), M3_TDY_Progress.class );

		this.physicalObjects = (List<PhysicalObject>) kbean.getRecordsByQuery("subprojectId=" + this.sec_p.getSubProject().getId(), PhysicalObject.class );

		clearAllDataTables();
		
		if ( this.sec_p.getStatus() == SubmissionStatus.APPROVED )
			return "section_p_view";
		else
			return "section_p";
	}
	
	public String goToSectionPList() {
		goToTDY();
		
		this.sub3monthly_reports = kbean.getSub3MonthlyReportsBySubProject( this.sec_l );
		
		clearAllDataTables();
		
		this.checkProcessed = false;
		
		return "sectionPList";		
	}
	
	public String goToSectionST() {
		
		goToTDE();
		
		// load sections A&B
		goToSectionA();
		goToSectionB();
		
		this.yearBudgetMatrix = this.getYearFunding();
		
		this.all_tde_financials = new HashMap<Section_L, List<TDE_Financial>>();
		if (this.subProjects != null) {
			for (Section_L prj: this.subProjects) {
				Map<Integer, Boolean> yearMatrix = new HashMap<Integer, Boolean>();
				Map<Integer, Map<Integer, Boolean>> yearQuarterMatrix = new HashMap<Integer, Map<Integer,Boolean>>();
				Map<Integer, BigDecimal> yearBudgetMatrix = new HashMap<Integer, BigDecimal>();
				Map<Integer, Boolean> quarterMatrix;
				
				System.out.println("subproject Dates: " + prj.getDate_start() + " - " + prj.getDate_end());
				for (Integer y: this.framework.getYears()) {
					// Year matrix
					yearMatrix.put(y, false);
					// year budget matrix
					yearBudgetMatrix.put(y, BigDecimal.ZERO);
					quarterMatrix = new HashMap<Integer, Boolean>();
					// year/quarter matrix
					for (int i=1; i <= 4; i++)
						quarterMatrix.put(i, false);
					yearQuarterMatrix.put(y, quarterMatrix);
				}
				
				if (prj.getDate_start() != null && prj.getDate_end() != null) {
					System.out.println("Before: ");
					for (Integer y: this.framework.getYears()) {
						System.out.println("Year: " + y);
						for (Entry<Integer, Boolean> q: yearQuarterMatrix.get(y).entrySet()) {
							System.out.println("Q" + q.getKey() + ": " + q.getValue() + ",  ");
						}
					}
					
					for (Integer y: yearMatrix.keySet()) {
						System.out.println("Year: " + y + " -> " + yearMatrix.get(y));
					}
					
					for (TDE_Financial f: prj.getFinancials()) {
						yearMatrix.put(f.getYear(), true);
						yearBudgetMatrix.put(f.getYear(), f.getBudget());
						System.out.println("TDE_Financial: Year: " + f.getYear() + " -> true");
					}
					
					for (Integer y: yearMatrix.keySet()) {
						System.out.println("YearMatrix: " + y + " -> " + yearMatrix.get(y));
					}
					
					List<YearQuarters> quartersList = DateUtil.getQuartersList( prj.getDate_start(), prj.getDate_end() );
					for (YearQuarters yq: quartersList) {
						System.out.println("Year: " + yq.getYear());
						for (Integer q: yq.getQuarters()) {
							System.out.println("quarter: " + q);
							if (yearQuarterMatrix.get(yq.getYear()) != null) {
								yearQuarterMatrix.get(yq.getYear()).put(q, true);
								System.out.println("Q" + q + ": true,  ");
							}
						}
					}
					System.out.println("After: ");
					for (Integer y: this.framework.getYears()) {
						System.out.println("Year: " + y);
						for (Entry<Integer, Boolean> q: yearQuarterMatrix.get(y).entrySet()) {
							System.out.println("Q" + q.getKey() + ": " + q.getValue() + ",  ");
						}
					}
				}
				
				prj.setYearMatrix(yearMatrix);
				prj.setYearQuarterMatrix(yearQuarterMatrix);
				prj.setYearBudgetMatrix(yearBudgetMatrix);
				this.all_tde_financials.put(prj, new ArrayList<TDE_Financial>());
			}
		}
		
		clearAllDataTables();

		return "section_st";
	}
	
	public String goToSectionST_print() {
		
		// we need this check to make sure we don't reload TDE when coming from Section Z			
		this.all_tde_financials = new HashMap<Section_L, List<TDE_Financial>>();
		for (Iterator iter = this.subProjects.iterator(); iter.hasNext();) {
			Section_L prj = (Section_L) iter.next();
			this.all_tde_financials.put(prj, prj.getFinancials());
		}

		return "section_st";
	}
	
	public HashMap<Integer, BigDecimal> getYearFunding() {
		HashMap<Integer, BigDecimal> year_funding = new HashMap<Integer, BigDecimal>();
		
		for (Section_L prj: this.subProjects) {
			for (TDE_Financial f: prj.getFinancials()) {
				if (year_funding.containsKey(f.getYear())) {
					BigDecimal budget = year_funding.get(f.getYear());
					year_funding.put(f.getYear(), budget.add(f.getBudget()));						
				} else
					year_funding.put(f.getYear(), f.getBudget());
			}
		}
		
		return year_funding;
	}
	
	public String goToSectionZ() {
		this.tde_funders = kbean.getFundersBySectionZId(this.sec_z.getId());
		this.tde_fundDistribution = new ArrayList<TDE_FundDistribution>();
		this.subprj_tdy_financial = new ArrayList<TDY_Financial>();

		List<TDE_Funders> defaultFundersList = kbean.getFundersBySectionZId(this.configId);
		this.defaultFundersSelectItemsList = new ArrayList<SelectItem>();
		this.defaultFundersSelectItemsList.add(new SelectItem(-1, "Παρακαλώ επιλέξτε"));
		for (TDE_Funders t0 : defaultFundersList) {
			SelectItem s = new SelectItem(t0.getId(), t0.getDescription());
			this.defaultFundersSelectItemsList.add(s);
		}
		
		clearAllDataTables();

		HashMap<String, TDY_Financial> tde_budget_hashmap = new HashMap<String, TDY_Financial>();
		HashMap<String, String> tde_categories_hashmap = new HashMap<String, String>();
		HashMap<Integer, BigDecimal> year_funding = getYearFunding();

		for (Iterator i = year_funding.keySet().iterator(); i.hasNext(); ) {
			Integer year = (Integer) i.next();
			TDE_FundDistribution tfd = kbean.getFundDistributionByYearSectionZId(year, this.sec_z.getId());  
			if (tfd == null) {
				tfd = new TDE_FundDistribution();
				tfd.setSec_z_id(this.sec_z.getId());
				tfd.setYear(year);
			}
			tfd.setBudget(year_funding.get(year));
			tde_fundDistribution.add(tfd);
		}

		for (Iterator iter = this.subProjects.iterator(); iter.hasNext();) {
			Section_L prj = (Section_L) iter.next();

			System.out.println("subproject id = " +  prj.getId());
			List<TDY_Financial> l = kbean.getTDYFinancialBySectionLId(prj.getId());
			for (Iterator j = l.iterator(); j.hasNext(); ) {
				TDY_Financial tf = (TDY_Financial) j.next();

				String hashkey = tf.getCode() + "_" + tf.getCategory();
				if (tde_budget_hashmap.containsKey(hashkey)) {
					TDY_Financial t = tde_budget_hashmap.get(hashkey);
					BigDecimal budget = t.getBudget();
					t.setBudget(budget.add(tf.getBudget()));						
				} else {
					TDY_Financial t = new TDY_Financial();
					t.setCode(tf.getCode());
					t.setCategory(tf.getCategory());
					t.setBudget(tf.getBudget());
					tde_budget_hashmap.put(hashkey, t);
				}
				System.out.println("code: " + hashkey + " ->  budget: " + tde_budget_hashmap.get(tf.getCode()));
				tde_categories_hashmap.put(hashkey, tf.getCategory());
			}
		}

		this.sectionZ_categoriesBudget = BigDecimal.ZERO;
		for (Iterator i = tde_budget_hashmap.keySet().iterator(); i.hasNext(); ) {
			String key = (String) i.next();
			TDY_Financial t = tde_budget_hashmap.get(key);
			this.sectionZ_categoriesBudget = this.sectionZ_categoriesBudget.add(t.getBudget());
			subprj_tdy_financial.add(t);
		}
		
		sectionZ_updateFundDistribution();

		return "section_z";
	}

	public String goToReportKatartisis() {
		
		return "loaded_katartisisReport";
	}
	
	public String goToContractors() {
		this.contractors = (List<Contractor>) ubean.getAllRecordsOfType(Contractor.class);
		
		clearAllDataTables();
		
		this.checkProcessed = false;

		return "load-contractors";
	}
	
	public void checkSectionA() {
		
		// Have to set the start hours to 00:00 and the end hours to 23:59, otherwise checks might fail. 
		if( this.sec_a.getDate_start() != null) {
			this.sec_a.getDate_start().setHours(0);
			this.sec_a.getDate_start().setMinutes(0);
		}
		if (this.sec_a.getDate_end() != null) {
			this.sec_a.getDate_end().setHours(23);
			this.sec_a.getDate_end().setMinutes(59);
		}
		
		if( this.sec_a.getDate_filing() != null && this.sec_a.getDate_submission() != null && this.sec_a.getDate_filing().after( this.sec_a.getDate_submission() ) ) {
			FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date1", new FacesMessage("Η ημ/νια συμπλήρωσης δεν μπορεί να είναι μεγαλύτερη από την ημ/νια υποβολής.") );
			System.out.println("Η ημ/νια συμπλήρωσης δεν μπορεί να είναι μεγαλύτερη από την ημ/νια υποβολής.");
			this.saveable.put(Section_A.class, false);
			return;
		}
		
		if( this.sec_a.getDate_start() != null && this.sec_a.getDate_end() != null && this.sec_a.getDate_start().after( this.sec_a.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης.") );
			System.out.println("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης.");
			this.saveable.put(Section_A.class, false);
			return;
		}
		
		// Commenting this out, actual TDE is like that.
		/*if( this.sec_a.getDate_start() != null && this.sec_a.getDate_submission() != null && this.sec_a.getDate_start().before( this.sec_a.getDate_submission() ) ) {
			FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι προγενέστερη από την ημ/νια υποβολής.") );
			System.out.println("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι προγενέστερη από την ημ/νια υποβολής.");
			this.saveable.put(Section_A.class, false);
			return;
		}*/
		
		if (this.framework != null) {
			if( this.sec_a.getDate_start() != null && this.framework.getDate_start() != null && this.sec_a.getDate_start().before( this.framework.getDate_start() ) ) {
				FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι προγενέστερη από το έτος έναρξης του ΚΠΣ: " + this.framework.getYear_start()) );
				this.saveable.put(Section_A.class, false);
				return;
			}

			if( this.sec_a.getDate_start() != null && this.framework.getDate_end() != null && this.sec_a.getDate_start().after( this.framework.getDate_end() ) ) {
				FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια έναρξης του έργου δεν μπορεί να είναι μεταγενέστερη από το έτος λήξης του ΚΠΣ: " + this.framework.getYear_end()) );
				this.saveable.put(Section_A.class, false);
				return;
			}
			
			if( this.sec_a.getDate_end() != null && this.framework.getDate_start() != null && this.sec_a.getDate_end().before( this.framework.getDate_start() ) ) {
				FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια λήξης του έργου δεν μπορεί να είναι προγενέστερη από το έτος έναρξης του ΚΠΣ: " + this.framework.getYear_start()) );
				this.saveable.put(Section_A.class, false);
				return;
			}

			if( this.sec_a.getDate_end() != null && this.framework.getDate_end() != null && this.sec_a.getDate_end().after( this.framework.getDate_end() ) ) {
				FacesContext.getCurrentInstance().addMessage("Τμημα_Α:date4", new FacesMessage("Η ημ/νια λήξης του έργου δεν μπορεί να είναι μεταγενέστερη από το έτος λήξης του ΚΠΣ: " + this.framework.getYear_end()) );
				this.saveable.put(Section_A.class, false);
				return;
			}
		}

		this.saveable.put(Section_A.class, true);
		
		if( this.vendors != null )
			checkSection_A__Vendors();
	}
	
	public String saveSectionA() {
		
		checkSectionA();

		if( this.vendors != null )
			saveSection_A__Vendors();
		
		if (this.saveable.get(Section_A.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_a ); 
		
		int sec_o_counter = 1;
		// create the quarter reports for the project;
		if( this.sec_a.getDate_start() != null && this.sec_a.getDate_end() != null) {
			List<YearQuarters> qt = DateUtil.getQuartersList( this.sec_a.getDate_start(), this.sec_a.getDate_end() );
			for( YearQuarters qItem : qt ) {

				// now create the quarters
				for( Iterator iter2 = qItem.getQuarters().iterator(); iter2.hasNext(); ) {
					Integer i = (Integer) iter2.next();

					System.out.println( "Adding QUARTER -------- : " + i );

					// now create the 3monthly reports
					Section_O o = kbean.findSectionOByYearQuarterNum( qItem.getYear(), i, this.sectionClass );
					if (o == null) {
						o = new Section_O();
						o.setNum( this.sectionClass );
						o.setQuarter( i );
						o.setYear( qItem.getYear() );
						o.setStatus( SubmissionStatus.INCOMPLETE );

						if( o.getQuarter() > 2 ) 
							o.setSemester(2);
						else 
							o.setSemester(1);

						Calendar c = Calendar.getInstance();
						c.set(qItem.getYear(), 1 +(i-1)*3, 1);
						o.setDate_n(c.getTime());
						o.setIndex(sec_o_counter);
						o = (Section_O) kbean.createRecord(o);
						sec_o_counter++;
					}

					prepareSectionO(o);
				}
			}
		}
		
		return "success";
	}
	
	public String saveSectionB() {
		if ( this.subProjects != null ) {
			saveSection_B__SubProjects();
			this.sec_b.setSubProjectsTotal(this.subProjects.size());
		}
		
		if (this.projectPlaces != null) {
			saveSection_B__ProjectPlaces();
		}

		if (this.saveable.get(Section_B.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_b );			
		
		return "success";
	}
	
	public String saveSectionC() {

		if (this.saveable.get(Section_C.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_c ); 
		
		return "success";
	}
	
	public String saveSectionD() {
		// Save deiktes for D
		if (this.deiktes_ekrois != null)
			saveSection_D__DeiktesEkrois();
		if (this.deiktes_apotelesmatos != null)
			saveSection_D__DeiktesApotelesmatos();
		if (this.deiktes_epiptoseon != null)
			saveSection_D__DeiktesEpiptoseon();

		if (this.saveable.get(Section_D.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_d ); 
		
		return "success";
	}
	
	public String saveSectionE() {
		// Save if validations pass
		if (this.permissions != null)
			saveSection_E__Permissions();
		if (this.tasks != null)
			saveSection_E__Tasks();
		if (this.vendorTasks != null)
			saveSection_E__VendorTasks();

		if (this.saveable.get(Section_E.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		// These have no validations checks
		if (this.analysis != null)
			saveSection_E__Analysis();
		if (this.katartisis != null)
			saveSection_E__Katartisis();

		kbean.updateRecord( this.sec_e ); 
		
		return "success";
	}
	
	public String saveSectionH() {
		if (this.saveable.get(Section_H.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_h ); 
		
		return "success";
	}
	
	public String saveSectionTh() {
		if (this.saveable.get(Section_Th.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}
		
		saveSection_Th__Document();
		
		kbean.updateRecord( this.sec_th ); 
		
		return "success";
	}
	
	public String saveSectionI() {
		// Save I employees
		if (this.section_I_employees != null)
			saveSection_I__Employees();

		if (this.saveable.get(Section_I.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_i ); 
		
		return "success";
	}
	
	public String saveSectionK() {
		// Save K employees
		if (this.section_K_employees != null)
			saveSection_K__Employees();

		if (this.saveable.get(Section_K.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_k ); 
		
		return "success";
	}
	
	public void checkSectionL() {
		// So we always are before the project's start and end dates
		if( this.sec_l.getDate_start() != null) {
			this.sec_l.getDate_start().setHours(1);
			this.sec_l.getDate_start().setMinutes(0);
		}
		if (this.sec_l.getDate_end() != null) {
			this.sec_l.getDate_end().setHours(23);
			this.sec_l.getDate_end().setMinutes(0);
		}
		if( this.sec_l.getDate_start() != null && this.sec_a.getDate_start() != null &&
				this.sec_l.getDate_start().before(this.sec_a.getDate_start() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια έναρξης του υποέργου δεν μπορεί να είναι μικρότερη από την ημ/νια έναρξης του έργου.") );
			this.saveable.put(Section_L.class, false);
		}
		if( this.sec_l.getDate_start() != null && this.sec_a.getDate_end() != null &&
				this.sec_l.getDate_start().after(this.sec_a.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια έναρξης του υποέργου δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης του έργου.") );
			this.saveable.put(Section_L.class, false);
		}
		if( this.sec_l.getDate_end() != null && this.sec_a.getDate_start() != null &&
				this.sec_l.getDate_end().before(this.sec_a.getDate_start() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια λήξης του υποέργου δεν μπορεί να είναι μικρότερη από την ημ/νια έναρξης του έργου.") );
			this.saveable.put(Section_L.class, false);
		}
		if( this.sec_l.getDate_end() != null && this.sec_a.getDate_end() != null &&
				!this.sec_l.getDate_end().before(this.sec_a.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια λήξης του υποέργου δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης του έργου.") );
			this.saveable.put(Section_L.class, false);
		}
		if( this.sec_l.getDate_start() != null && this.sec_l.getDate_end() != null &&
				this.sec_l.getDate_start().after( this.sec_l.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια έναρξης του υποέργου δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης.") );
			this.saveable.put(Section_L.class, false);
		}
	}
	
	public String saveSectionL() {
		checkSectionL();
		
		saveSection_L_TDY_ProjectPlaces();
		saveSection_L_TDY_Progress();
		saveSection_L_TDY_Deiktes();
		saveSection_L_TDY_Financial();
		saveSection_L_TDY_ProgressActivities();
		saveSection_L_TDY_Evolution();

		if (this.saveable.get(Section_L.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_l );
		
		updateYearQuartersDistributions(this.sec_l);
		
		update3MonthlyReports(this.sec_l);
		
		updateMonthlyReports(this.sec_l);
		
		return "success";
	}
	
	public void updateYearQuartersDistributions(Section_L l) {
		if (l.getDate_start() != null && l.getDate_end() != null) {
			// now that we have the dates, delete all the TDE_Financial that used to exist for this ID, and create the new ones.
			this.quartersList = DateUtil.getQuartersList( l.getDate_start(), l.getDate_end() );

			for( Iterator iter=this.quartersList.iterator(); iter.hasNext(); ) {

				YearQuarters y = (YearQuarters) iter.next();

		
				TDE_Financial t = kbean.findTDE_FinancialByYearSubproject( y.getYear(), l.getId() );
				if (t == null) {
					t = new TDE_Financial();
					t.setSec_l_id(l.getId() );
					t.setYear( y.getYear() );
					t.setBudget(l.getBudget().divide(BigDecimal.valueOf(this.quartersList.size()), 2));
					kbean.createRecord(t);
				}
				
				// now create the quarters
				for( Iterator iter2 = y.getQuarters().iterator(); iter2.hasNext(); ) {
					Integer i = (Integer) iter2.next();

					System.out.println( "Adding QUARTER -------- : " + i );

					TDE_FinancialQuarters tq = kbean.findQuarterByYearQuarterSubproject( y.getYear(), i, l.getId() );
					if (tq == null) {
						tq = new TDE_FinancialQuarters();
						tq.setQuarter( Integer.valueOf(i) );
						tq.setYear( y.getYear() );
						tq.setStatus( true );
						tq.setSec_l_id( l.getId() );
						kbean.createRecord(tq);
					}
				}
			}
		}
	}
	
	public void update3MonthlyReports(Section_L l) {
		int sec_p_counter = 1;
		if (l.getDate_start() != null && l.getDate_end() != null) {
			// now that we have the dates, delete all the TDE_Financial that used to exist for this ID, and create the new ones.
			this.quartersList = DateUtil.getQuartersList( l.getDate_start(), l.getDate_end() );

			for( Iterator iter=this.quartersList.iterator(); iter.hasNext(); ) {

				YearQuarters y = (YearQuarters) iter.next();
				
				// now create the quarters
				for( Iterator iter2 = y.getQuarters().iterator(); iter2.hasNext(); ) {
					Integer i = (Integer) iter2.next();

					System.out.println( "Adding QUARTER -------- : " + i );
					// now create the 3monthly reports
					Section_P p = kbean.findSectionPByYearQuarterSubproject( y.getYear(), i, l );
					if (p == null) {
						p = new Section_P();
						p.setQuarter(i);
						p.setYear( y.getYear() );
						p.setStatus( SubmissionStatus.INCOMPLETE );
						p.setSubProject( l );

						Calendar c = Calendar.getInstance();
						c.set(y.getYear(), 1 +(i-1)*3, 1);
						p.setDate_n(c.getTime());
						p.setIndex(sec_p_counter);
						p = (Section_P) kbean.createRecord(p);
						sec_p_counter++;
					}
					prepareSectionP(p);
				}
			}
		}
	}
	
	public void updateMonthlyReports(Section_L l) {
		if (l.getDate_start() != null && l.getDate_end() != null) {
			List<Date> months = DateUtil.getMonthsList( l.getDate_start(), l.getDate_end() );

			for( Iterator iter=months.iterator(); iter.hasNext(); ) {
				Date d = (Date) iter.next();
				Calendar c = Calendar.getInstance(new Locale("el") );
				c.setTime(d);

				// now create the 3monthly reports
				Section_N n = kbean.findSectionNByYearMonthSubproject( c.get(Calendar.YEAR), c.get(Calendar.MONTH), l );
				if (n == null) {
					n = new Section_N();
					n.setDate_n( d );
					n.setYear( c.get(Calendar.YEAR) );
					n.setMonth( c.get(Calendar.MONTH) );
					n.setStatus( SubmissionStatus.INCOMPLETE );
					n.setSubProject( l );

					n = (Section_N) kbean.createRecord(n);
				}
			}
		}
	}
	
	public String saveSectionM() {
		
		if( this.sec_m.getDate_acceptance() != null && this.sec_a.getDate_start() != null &&
				this.sec_m.getDate_acceptance().before(this.sec_a.getDate_start() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια ανάληψης της Ν.Δ. δεν μπορεί να είναι προγενέστερη από την ημ/νια έναρξης του έργου " + DateUtil.cts(this.sec_a.getDate_start())));
			this.saveable.put(Section_M.class, false);
			return "validation_error";
		}
		if( this.sec_m.getDate_acceptance() != null && this.sec_a.getDate_end() != null &&
				this.sec_m.getDate_acceptance().after(this.sec_a.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια ανάληψης της Ν.Δ. δεν μπορεί να είναι μεταγενέστερη από την ημ/νια λήξης του έργου " + DateUtil.cts(this.sec_a.getDate_end())));
			this.saveable.put(Section_M.class, false);
			return "validation_error";
		}
		if( this.sec_m.getDate_expiration() != null && this.sec_a.getDate_start() != null &&
				this.sec_m.getDate_expiration().before(this.sec_a.getDate_start() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια λήξης της Ν.Δ. δεν μπορεί να είναι προγενέστερη από την ημ/νια έναρξης του έργου " + DateUtil.cts(this.sec_a.getDate_start())));
			this.saveable.put(Section_M.class, false);
			return "validation_error";
		}
		if( this.sec_m.getDate_expiration() != null && this.sec_a.getDate_end() != null &&
				this.sec_m.getDate_expiration().after(this.sec_a.getDate_end() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια λήξης της Ν.Δ. δεν μπορεί να είναι μεταγενέστερη από την ημ/νια λήξης του έργου " + DateUtil.cts(this.sec_a.getDate_end())));
			this.saveable.put(Section_M.class, false);
			return "validation_error";
		}
		if( this.sec_m.getDate_acceptance() != null && this.sec_m.getDate_expiration() != null &&
				this.sec_m.getDate_acceptance().after(this.sec_m.getDate_expiration() ) ) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νια ανάληψης δεν μπορεί να είναι μεγαλύτερη από την ημ/νια λήξης"));
			this.saveable.put(Section_M.class, false);
			return "validation_error";
		}
		
		saveSection_M__NOM_Contractors();
		saveSection_M__NOM_SubProjects();
		
		if (this.saveable.get(Section_M.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}
		
		// Set Section M's edit date to now.
		this.sec_m.setDate_edit(new Date());
		kbean.updateRecord( this.sec_m );
		
		for (NOM_SubProjects noms: this.nom_subProjects) {
			this.sec_m.addSubProject(noms);
		}
		this.nom_subProjects.clear();
		
		return "success";
	}
	
	public String saveSectionNList() {
		for (Section_N n : this.monthly_reports) {
			if (n.getAccessible() == true && n.getCompleted() == false)
				kbean.updateRecord(n);
		}

		return "success";
	}
	
	public String saveSectionO() {
		this.sec_o.setPublicationDate(new Date());
		this.sec_o.setStatus(SubmissionStatus.SUBMITTED);
		kbean.updateRecord( this.sec_o ); 

		kbean.updateListRecord( this.m3_tde_semesters );
		kbean.updateListRecord( this.m3_tde_deiktes );
		kbean.updateListRecord( this.m3_tde_places );
		kbean.updateListRecord( this.m3_tde_problems );
		
		return "success";
	}

	public String saveSectionP() {
		this.sec_p.setStatus(SubmissionStatus.SUBMITTED);
		kbean.updateRecord( this.sec_p );
		
		kbean.updateListRecord( this.m3_tdy_deiktes );
		kbean.updateListRecord( this.m3_tdy_evolution );
		kbean.updateListRecord( this.m3_tdy_places );
		kbean.updateListRecord( this.m3_tdy_progress );
		kbean.updateListRecord( this.m3_tdy_certifications );
		
		clearAllDataTables();
		
		return "saved_p";
	}
	
	public String saveSectionST() {
		saveSection_ST_TDE_Financial();

		if (this.saveable.get(Section_ST.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "section_st";
		}

		kbean.updateRecord( this.sec_st ); 
		
		goToSectionST();
		
		return "section_st";
	}
	
	public String updateSectionST() {
		
		for (Section_L l: this.subProjects) {
			kbean.deleteFinancialBySubProject(l.getId());
			updateYearQuartersDistributions(l);
		}
		
		goToSectionST();
		
		return "section_st";
	}
	
	public String saveSectionZ() {
		saveSection_Z_TDE_Funders();
		saveSection_Z_TDE_FundDistribution();

		if (this.sec_z.getLoanVendor_id() != null && this.sec_z.getLoanVendor_id().equals(-1) != false) {
			TDE_Vendors v = (TDE_Vendors) kbean.getRecordById(this.sec_z.getLoanVendor_id(), TDE_Vendors.class);
			if (v != null)
				this.sec_z.setLoanVendor(v.getName());
		}
		if (this.saveable.get(Section_Z.class) == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Αδύνατη η αποθήκευση, παρακαλώ επιληφθείτε των εκκρεμοτήτων."));
			return "validation_error";
		}

		kbean.updateRecord( this.sec_z ); 
		
		return "success";
	}
	
	public String deleteSectionM() {
		Section_M m = (Section_M) this.nomikesDesmefseisDataTable.getRowData();

		kbean.deleteRecord( Section_M.class, m.getId() );

		gotoNomikesDesmefseis();
		
		return "deleted";
	}
	
	public BigDecimal calculateInvoiceSum( Section_L subProject, Date start, Date end ) {
		System.out.println( ": DATES : " + start + " -- " + end );

		List<Invoice_Contractor> ic = (List<Invoice_Contractor>) kbean.getInvoiceContractorListBySubProjectDates( subProject, start, end );

		System.out.println( ": INVOICES RETURNED: " + ic.size() );

		BigDecimal sum = BigDecimal.ZERO;

		for( Invoice_Contractor icItem : ic ) {
			System.out.println( ": invoice says value is : " + icItem.getNet() + " - " +  icItem.getVat() );

			sum = sum.add(icItem.getNet());
			sum = sum.add(icItem.getVat());
		}

		return sum;
	}
	
	// getters - setters
	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public Section_A getSec_a() {
		return sec_a;
	}


	public void setSec_a(Section_A sec_a) {
		this.sec_a = sec_a;
	}


	public Section_B getSec_b() {
		return sec_b;
	}


	public void setSec_b(Section_B sec_b) {
		this.sec_b = sec_b;
	}


	public Section_C getSec_c() {
		return sec_c;
	}


	public void setSec_c(Section_C sec_c) {
		this.sec_c = sec_c;
	}


	public Section_D getSec_d() {
		return sec_d;
	}


	public void setSec_d(Section_D sec_d) {
		this.sec_d = sec_d;
	}


	public Section_E getSec_e() {
		return sec_e;
	}


	public void setSec_e(Section_E sec_e) {
		this.sec_e = sec_e;
	}


	public Section_H getSec_h() {
		return sec_h;
	}


	public void setSec_h(Section_H sec_h) {
		this.sec_h = sec_h;
	}


	public Section_I getSec_i() {
		return sec_i;
	}


	public void setSec_i(Section_I sec_i) {
		this.sec_i = sec_i;
	}


	public Section_K getSec_k() {
		return sec_k;
	}


	public void setSec_k(Section_K sec_k) {
		this.sec_k = sec_k;
	}


	public Section_L getSec_l() {
		return sec_l;
	}


	public void setSec_l(Section_L sec_l) {
		this.sec_l = sec_l;
	}


	public Section_M getSec_m() {
		return sec_m;
	}


	public void setSec_m(Section_M sec_m) {
		this.sec_m = sec_m;
	}


	public Section_N getSec_n() {
		return sec_n;
	}


	public void setSec_n(Section_N sec_n) {
		this.sec_n = sec_n;
	}


	public Section_O getSec_o() {
		return sec_o;
	}


	public void setSec_o(Section_O sec_o) {
		this.sec_o = sec_o;
	}


	public Section_ST getSec_st() {
		return sec_st;
	}


	public void setSec_st(Section_ST sec_st) {
		this.sec_st = sec_st;
	}


	public Section_Th getSec_th() {
		return sec_th;
	}


	public void setSec_th(Section_Th sec_th) {
		this.sec_th = sec_th;
	}

	public Section_Z getSec_z() {
		return sec_z;
	}


	public void setSec_z(Section_Z sec_z) {
		this.sec_z = sec_z;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public List<OTA_Projects> getProjects() {
		return projects;
	}

	public void setProjects(List<OTA_Projects> projects) {
		this.projects = projects;
	}

	/**
	 * @return the revisionsdataTable
	 */
	public HtmlDataTable getRevisionsdataTable() {
		return revisionsdataTable;
	}

	/**
	 * @param revisionsdataTable the revisionsdataTable to set
	 */
	public void setRevisionsdataTable(HtmlDataTable revisionsdataTable) {
		this.revisionsdataTable = revisionsdataTable;
	}

	/**
	 * @return the section_A_Projects
	 */
	public List<Section_A> getSection_A_Projects() {
		return section_A_Projects;
	}

	/**
	 * @param section_A_Projects the section_A_Projects to set
	 */
	public void setSection_A_Projects(List<Section_A> section_A_Projects) {
		this.section_A_Projects = section_A_Projects;
	}

	public Long getSectionClass() {
		return sectionClass;
	}

	public void setSectionClass(Long sectionClass) {
		this.sectionClass = sectionClass;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * @return the invoices_contractor
	 */
	public List<Invoice_Contractor> getInvoices_contractor() {
		return invoices_contractor;
	}

	/**
	 * @param invoices_contractor the invoices_contractor to set
	 */
	public void setInvoices_contractor(List<Invoice_Contractor> invoices_contractor) {
		this.invoices_contractor = invoices_contractor;
	}

	/**
	 * @return the invoices_vendor
	 */
	public List<Invoice_Vendor> getInvoices_vendor() {
		return invoices_vendor;
	}

	/**
	 * @param invoices_vendor the invoices_vendor to set
	 */
	public void setInvoices_vendor(List<Invoice_Vendor> invoices_vendor) {
		this.invoices_vendor = invoices_vendor;
	}
	
	/**
	 * @return the invoiceContractorListDataTable
	 */
	public HtmlDataTable getInvoiceContractorListDataTable() {
		return invoiceContractorListDataTable;
	}

	/**
	 * @return the invoiceVendorListDataTable
	 */
	public HtmlDataTable getInvoiceVendorListDataTable() {
		return invoiceVendorListDataTable;
	}

	/**
	 * @param invoiceContractorListDataTable the invoiceContractorListDataTable to set
	 */
	public void setInvoiceContractorListDataTable(
			HtmlDataTable invoiceContractorListDataTable) {
		this.invoiceContractorListDataTable = invoiceContractorListDataTable;
	}

	/**
	 * @param invoiceVendorListDataTable the invoiceVendorListDataTable to set
	 */
	public void setInvoiceVendorListDataTable(
			HtmlDataTable invoiceVendorListDataTable) {
		this.invoiceVendorListDataTable = invoiceVendorListDataTable;
	}

	public Invoice_Vendor getTmp_invoice_vendor() {
		return invoice_vendor;
	}

	public void setTmp_invoice_vendor(Invoice_Vendor invoice_vendor) {
		this.invoice_vendor = invoice_vendor;
	}

	/**
	 * @return the invoice_vendor
	 */
	public Invoice_Vendor getInvoice_vendor() {
		return invoice_vendor;
	}

	/**
	 * @param invoice_vendor the invoice_vendor to set
	 */
	public void setInvoice_vendor(Invoice_Vendor invoice_vendor) {
		this.invoice_vendor = invoice_vendor;
	}

	/**
	 * @return the vendor
	 */
	public TDE_Vendors getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(TDE_Vendors vendor) {
		this.vendor = vendor;
	}

	public List<PhysicalObject> getPhysicalObjects() {
		return physicalObjects;
	}

	public void setPhysicalObjects(List<PhysicalObject> physicalObjects) {
		this.physicalObjects = physicalObjects;
	}

	public HtmlDataTable getTmp_dataTable() {
		return tmp_dataTable;
	}

	public void setTmp_dataTable(HtmlDataTable tmp_dataTable) {
		this.tmp_dataTable = tmp_dataTable;
	}

	public List<TDE_ProjectPlaces> getProjectPlaces() {
		return projectPlaces;
	}

	public void setProjectPlaces(List<TDE_ProjectPlaces> projectPlaces) {
		this.projectPlaces = projectPlaces;
	}

	public String getTmp_municipality() {
		return tmp_municipality;
	}

	public void setTmp_municipality(String tmp_municipality) {
		this.tmp_municipality = tmp_municipality;
	}

	public List<SectionD_Deiktis> getDeiktes() {
		return deiktes;
	}

	public void setDeiktes(List<SectionD_Deiktis> deiktes) {
		this.deiktes = deiktes;
	}


	public List<SectionD_Deiktis> getDeiktes_ekrois() {
		return deiktes_ekrois;
	}

	public void setDeiktes_ekrois(List<SectionD_Deiktis> deiktes_ekrois) {
		this.deiktes_ekrois = deiktes_ekrois;
	}

	public List<SectionD_Deiktis> getDeiktes_apotelesmatos() {
		return deiktes_apotelesmatos;
	}

	public void setDeiktes_apotelesmatos(List<SectionD_Deiktis> deiktes_apotelesmatos) {
		this.deiktes_apotelesmatos = deiktes_apotelesmatos;
	}

	public List<SectionD_Deiktis> getDeiktes_epiptoseon() {
		return deiktes_epiptoseon;
	}

	public void setDeiktes_epiptoseon(List<SectionD_Deiktis> deiktes_epiptoseon) {
		this.deiktes_epiptoseon = deiktes_epiptoseon;
	}
	public List<TDE_Permissions> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<TDE_Permissions> permissions) {
		this.permissions = permissions;
	}

	public List<TDE_Analysis> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(List<TDE_Analysis> analysis) {
		this.analysis = analysis;
	}

	public List<TDE_Katartisis> getKatartisis() {
		return katartisis;
	}

	public void setKatartisis(List<TDE_Katartisis> katartisis) {
		this.katartisis = katartisis;
	}

	public List<TDE_Tasks> getTasks() {
		return tasks;
	}

	public void setTasks(List<TDE_Tasks> tasks) {
		this.tasks = tasks;
	}

	public List<TDE_VendorTasks> getVendorTasks() {
		return vendorTasks;
	}

	public void setVendorTasks(List<TDE_VendorTasks> vendorTasks) {
		this.vendorTasks = vendorTasks;
	}

	

	/**
	 * @return the section_I_employees
	 */
	public List<TDE_Employees> getSection_I_employees() {
		return section_I_employees;
	}

	/**
	 * @param section_I_employees the section_I_employees to set
	 */
	public void setSection_I_employees(List<TDE_Employees> section_I_employees) {
		this.section_I_employees = section_I_employees;
	}

	/**
	 * @return the section_K_employees
	 */
	public List<TDE_Employees> getSection_K_employees() {
		return section_K_employees;
	}

	/**
	 * @param section_K_employees the section_K_employees to set
	 */
	public void setSection_K_employees(List<TDE_Employees> section_K_employees) {
		this.section_K_employees = section_K_employees;
	}


	/**
	 * @return the section_I_employees_currQty
	 */
	public int getSection_I_employees_currQty() {
		return section_I_employees_currQty;
	}

	/**
	 * @param section_I_employees_currQty the section_I_employees_currQty to set
	 */
	public void setSection_I_employees_currQty(int section_I_employees_currQty) {
		this.section_I_employees_currQty = section_I_employees_currQty;
	}

	/**
	 * @return the section_I_employees_estQty
	 */
	public int getSection_I_employees_estQty() {
		return section_I_employees_estQty;
	}

	/**
	 * @param section_I_employees_estQty the section_I_employees_estQty to set
	 */
	public void setSection_I_employees_estQty(int section_I_employees_estQty) {
		this.section_I_employees_estQty = section_I_employees_estQty;
	}

	/**
	 * @return the section_K_employees_currQty
	 */
	public int getSection_K_employees_currQty() {
		return section_K_employees_currQty;
	}

	/**
	 * @param section_K_employees_currQty the section_K_employees_currQty to set
	 */
	public void setSection_K_employees_currQty(int section_K_employees_currQty) {
		this.section_K_employees_currQty = section_K_employees_currQty;
	}

	/**
	 * @return the section_K_employees_estQty
	 */
	public int getSection_K_employees_estQty() {
		return section_K_employees_estQty;
	}

	/**
	 * @param section_K_employees_estQty the section_K_employees_estQty to set
	 */
	public void setSection_K_employees_estQty(int section_K_employees_estQty) {
		this.section_K_employees_estQty = section_K_employees_estQty;
	}

	public List<Section_L> getSubProjects() {
		return subProjects;
	}

	public void setSubProjects(List<Section_L> subProjects) {
		this.subProjects = subProjects;
	}

	public List<TDY_Progress> getTdy_progress() {
		return tdy_progress;
	}

	public void setTdy_progress(List<TDY_Progress> tdy_progress) {
		this.tdy_progress = tdy_progress;
	}

	public List<TDY_Places> getTdy_places() {
		return tdy_places;
	}

	public void setTdy_places(List<TDY_Places> tdy_places) {
		this.tdy_places = tdy_places;
	}

	public List<TDY_Deiktes> getTdy_deiktes() {
		return tdy_deiktes;
	}

	public void setTdy_deiktes(List<TDY_Deiktes> tdy_deiktes) {
		this.tdy_deiktes = tdy_deiktes;
	}

	public List<TDY_ProgressActivities> getTdy_progressActivities() {
		return tdy_progressActivities;
	}

	public void setTdy_progressActivities(
			List<TDY_ProgressActivities> tdy_progressActivities) {
		this.tdy_progressActivities = tdy_progressActivities;
	}

	public List<TDY_Financial> getTdy_financial() {
		return tdy_financial;
	}

	public void setTdy_financial(List<TDY_Financial> tdy_financial) {
		this.tdy_financial = tdy_financial;
	}

	public List<TDY_Evolution> getTdy_evolution() {
		return tdy_evolution;
	}

	public void setTdy_evolution(List<TDY_Evolution> tdy_evolution) {
		this.tdy_evolution = tdy_evolution;
	}

	/**
	 * @return the tde_financial
	 */
	public List<TDE_Financial> getTde_financial() {
		return tde_financial;
	}

	/**
	 * @param tde_financial the tde_financial to set
	 */
	public void setTde_financial(List<TDE_Financial> tde_financial) {
		this.tde_financial = tde_financial;
	}

	/**
	 * @return the tde_funders
	 */
	public List<TDE_Funders> getTde_funders() {
		return tde_funders;
	}

	/**
	 * @param tde_funders the tde_funders to set
	 */
	public void setTde_funders(List<TDE_Funders> tde_funders) {
		this.tde_funders = tde_funders;
	}

	/**
	 * @return the tde_fundDistribution
	 */
	public List<TDE_FundDistribution> getTde_fundDistribution() {
		return tde_fundDistribution;
	}

	/**
	 * @param tde_fundDistribution the tde_fundDistribution to set
	 */
	public void setTde_fundDistribution(
			List<TDE_FundDistribution> tde_fundDistribution) {
		this.tde_fundDistribution = tde_fundDistribution;
	}

	/**
	 * @return the years
	 */
	public List<Integer> getYears() {
		return years;
	}

	/**
	 * @param years the years to set
	 */
	public void setYears(List<Integer> years) {
		this.years = years;
	}


	/**
	 * @return the contractors
	 */
	public List<Contractor> getContractors() {
		return contractors;
	}

	/**
	 * @param contractors the contractors to set
	 */
	public void setContractors(List<Contractor> contractors) {
		this.contractors = contractors;
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
	 * @return the contractorSelectItems
	 */
	public List<SelectItem> getContractorSelectItems() {
		return contractorSelectItems;
	}

	/**
	 * @param contractorSelectItems the contractorSelectItems to set
	 */
	public void setContractorSelectItems(List<SelectItem> contractorSelectItems) {
		this.contractorSelectItems = contractorSelectItems;
	}

	public List<SelectItem> getSubProjectSelectItems() {
		return subProjectSelectItems;
	}

	public void setSubProjectSelectItems(List<SelectItem> subProjectSelectItems) {
		this.subProjectSelectItems = subProjectSelectItems;
	}

	public List<YearQuarters> getQuartersList() {
		return quartersList;
	}

	public void setQuartersList(List<YearQuarters> quartersList) {
		this.quartersList = quartersList;
	}

	public List<NOM_Contractor> getNom_contractors() {
		return nom_contractors;
	}

	public void setNom_contractors(List<NOM_Contractor> nom_contractors) {
		this.nom_contractors = nom_contractors;
	}

	public List<NOM_SubProjects> getNom_subProjects() {
		return nom_subProjects;
	}

	public void setNom_subProjects(List<NOM_SubProjects> nom_subProjects) {
		this.nom_subProjects = nom_subProjects;
	}

	public List<Section_M> getNomList() {
		return nomList;
	}

	public void setNomList(List<Section_M> nomList) {
		this.nomList = nomList;
	}

	public List<Section_N> getMonthly_reports() {
		return monthly_reports;
	}

	public void setMonthly_reports(List<Section_N> monthly_reports) {
		this.monthly_reports = monthly_reports;
	}

	public Section_N getTmp_monthly() {
		return tmp_monthly;
	}

	public void setTmp_monthly(Section_N tmp_monthly) {
		this.tmp_monthly = tmp_monthly;
	}

	public String getReportOutputHTML() {
		return reportOutputHTML;
	}

	public void setReportOutputHTML(String reportOutputHTML) {
		this.reportOutputHTML = reportOutputHTML;
	}

	public List<Section_P> getSub3monthly_reports() {
		return sub3monthly_reports;
	}

	public void setSub3monthly_reports(List<Section_P> sub3monthly_reports) {
		this.sub3monthly_reports = sub3monthly_reports;
	}

	public Section_P getTmp_sub3monthly() {
		return tmp_sub3monthly;
	}

	public void setTmp_sub3monthly(Section_P tmp_sub3monthly) {
		this.tmp_sub3monthly = tmp_sub3monthly;
	}

	public List<M3_TDY_Deiktes> getM3_tdy_deiktes() {
		return m3_tdy_deiktes;
	}

	public void setM3_tdy_deiktes(List<M3_TDY_Deiktes> m3_tdy_deiktes) {
		this.m3_tdy_deiktes = m3_tdy_deiktes;
	}

	public List<M3_TDY_Evolution> getM3_tdy_evolution() {
		return m3_tdy_evolution;
	}

	public void setM3_tdy_evolution(List<M3_TDY_Evolution> m3_tdy_evolution) {
		this.m3_tdy_evolution = m3_tdy_evolution;
	}

	public List<M3_TDY_Places> getM3_tdy_places() {
		return m3_tdy_places;
	}

	public void setM3_tdy_places(List<M3_TDY_Places> m3_tdy_places) {
		this.m3_tdy_places = m3_tdy_places;
	}

	public List<M3_TDY_Progress> getM3_tdy_progress() {
		return m3_tdy_progress;
	}

	public void setM3_tdy_progress(List<M3_TDY_Progress> m3_tdy_progress) {
		this.m3_tdy_progress = m3_tdy_progress;
	}

	public List<M3_TDY_Certification> getM3_tdy_certifications() {
		return m3_tdy_certifications;
	}

	public void setM3_tdy_certifications(
			List<M3_TDY_Certification> m3_tdy_certifications) {
		this.m3_tdy_certifications = m3_tdy_certifications;
	}

	public Section_P getSec_p() {
		return sec_p;
	}

	public void setSec_p(Section_P sec_p) {
		this.sec_p = sec_p;
	}


	public HtmlDataTable getTmp_dataTable2() {
		return tmp_dataTable2;
	}

	public void setTmp_dataTable2(HtmlDataTable tmp_dataTable2) {
		this.tmp_dataTable2 = tmp_dataTable2;
	}

	public HtmlDataTable getTmp_dataTable3() {
		return tmp_dataTable3;
	}

	public void setTmp_dataTable3(HtmlDataTable tmp_dataTable3) {
		this.tmp_dataTable3 = tmp_dataTable3;
	}

	public HtmlDataTable getTmp_dataTable4() {
		return tmp_dataTable4;
	}

	public void setTmp_dataTable4(HtmlDataTable tmp_dataTable4) {
		this.tmp_dataTable4 = tmp_dataTable4;
	}

	public HtmlDataTable getTmp_dataTable5() {
		return tmp_dataTable5;
	}
	public void setTmp_dataTable5(HtmlDataTable tmp_dataTable5) {
		this.tmp_dataTable5 = tmp_dataTable5;
	}
	public HtmlDataTable getTmp_dataTable6() {
		return tmp_dataTable6;
	}
	public void setTmp_dataTable6(HtmlDataTable tmp_dataTable6) {
		this.tmp_dataTable6 = tmp_dataTable6;
	}

	/**
	 * @return the tmp_dataTable7
	 */
	public HtmlDataTable getTmp_dataTable7() {
		return tmp_dataTable7;
	}

	/**
	 * @param tmp_dataTable7 the tmp_dataTable7 to set
	 */
	public void setTmp_dataTable7(HtmlDataTable tmp_dataTable7) {
		this.tmp_dataTable7 = tmp_dataTable7;
	}

	/**
	 * @return the tmp_dataTable8
	 */
	public HtmlDataTable getTmp_dataTable8() {
		return tmp_dataTable8;
	}

	/**
	 * @param tmp_dataTable8 the tmp_dataTable8 to set
	 */
	public void setTmp_dataTable8(HtmlDataTable tmp_dataTable8) {
		this.tmp_dataTable8 = tmp_dataTable8;
	}

	/**
	 * @return the tmp_dataTable9
	 */
	public HtmlDataTable getTmp_dataTable9() {
		return tmp_dataTable9;
	}

	/**
	 * @param tmp_dataTable9 the tmp_dataTable9 to set
	 */
	public void setTmp_dataTable9(HtmlDataTable tmp_dataTable9) {
		this.tmp_dataTable9 = tmp_dataTable9;
	}

	/**
	 * @return the tmp_dataTable10
	 */
	public HtmlDataTable getTmp_dataTable10() {
		return tmp_dataTable10;
	}

	/**
	 * @param tmp_dataTable10 the tmp_dataTable10 to set
	 */
	public void setTmp_dataTable10(HtmlDataTable tmp_dataTable10) {
		this.tmp_dataTable10 = tmp_dataTable10;
	}

	/**
	 * @return the tmp_dataTable11
	 */
	public HtmlDataTable getTmp_dataTable11() {
		return tmp_dataTable11;
	}

	/**
	 * @param tmp_dataTable11 the tmp_dataTable11 to set
	 */
	public void setTmp_dataTable11(HtmlDataTable tmp_dataTable11) {
		this.tmp_dataTable11 = tmp_dataTable11;
	}

	/**
	 * @return the tmp_dataTable12
	 */
	public HtmlDataTable getTmp_dataTable12() {
		return tmp_dataTable12;
	}

	/**
	 * @param tmp_dataTable12 the tmp_dataTable12 to set
	 */
	public void setTmp_dataTable12(HtmlDataTable tmp_dataTable12) {
		this.tmp_dataTable12 = tmp_dataTable12;
	}

	/**
	 * @return the tmp_dataTable13
	 */
	public HtmlDataTable getTmp_dataTable13() {
		return tmp_dataTable13;
	}

	/**
	 * @param tmp_dataTable13 the tmp_dataTable13 to set
	 */
	public void setTmp_dataTable13(HtmlDataTable tmp_dataTable13) {
		this.tmp_dataTable13 = tmp_dataTable13;
	}

	/**
	 * @return the tmp_dataTable14
	 */
	public HtmlDataTable getTmp_dataTable14() {
		return tmp_dataTable14;
	}

	/**
	 * @param tmp_dataTable14 the tmp_dataTable14 to set
	 */
	public void setTmp_dataTable14(HtmlDataTable tmp_dataTable14) {
		this.tmp_dataTable14 = tmp_dataTable14;
	}

	/**
	 * @return the tmp_dataTable15
	 */
	public HtmlDataTable getTmp_dataTable15() {
		return tmp_dataTable15;
	}

	/**
	 * @param tmp_dataTable15 the tmp_dataTable15 to set
	 */
	public void setTmp_dataTable15(HtmlDataTable tmp_dataTable15) {
		this.tmp_dataTable15 = tmp_dataTable15;
	}

	/**
	 * @return the tmp_dataTable16
	 */
	public HtmlDataTable getTmp_dataTable16() {
		return tmp_dataTable16;
	}

	/**
	 * @param tmp_dataTable16 the tmp_dataTable16 to set
	 */
	public void setTmp_dataTable16(HtmlDataTable tmp_dataTable16) {
		this.tmp_dataTable16 = tmp_dataTable16;
	}

	/**
	 * @return the projectdataTable
	 */
	public HtmlDataTable getProjectdataTable() {
		return projectdataTable;
	}

	/**
	 * @param projectdataTable the projectdataTable to set
	 */
	public void setSubProjectdataTable(HtmlDataTable subProjectdataTable) {
		this.subProjectdataTable = subProjectdataTable;
	}

	/**
	 * @return the projectdataTable
	 */
	public HtmlDataTable getSubProjectdataTable() {
		return subProjectdataTable;
	}

	/**
	 * @param projectdataTable the projectdataTable to set
	 */
	public void setProjectdataTable(HtmlDataTable projectdataTable) {
		this.projectdataTable = projectdataTable;
	}

	/**
	 * @return the nomikesDesmefseisDataTable
	 */
	public HtmlDataTable getNomikesDesmefseisDataTable() {
		return nomikesDesmefseisDataTable;
	}

	/**
	 * @param nomikesDesmefseisDataTable the nomikesDesmefseisDataTable to set
	 */
	public void setNomikesDesmefseisDataTable(
			HtmlDataTable nomikesDesmefseisDataTable) {
		this.nomikesDesmefseisDataTable = nomikesDesmefseisDataTable;
	}

	public List<Section_O> getPrj3monthly_reports() {
		return prj3monthly_reports;
	}

	public void setPrj3monthly_reports(List<Section_O> prj3monthly_reports) {
		this.prj3monthly_reports = prj3monthly_reports;
	}

	public List<M3_TDE_Places> getM3_tde_places() {
		return m3_tde_places;
	}

	public void setM3_tde_places(List<M3_TDE_Places> m3_tde_places) {
		this.m3_tde_places = m3_tde_places;
	}

	public List<M3_TDE_Deiktes> getM3_tde_deiktes() {
		return m3_tde_deiktes;
	}

	public void setM3_tde_deiktes(List<M3_TDE_Deiktes> m3_tde_deiktes) {
		this.m3_tde_deiktes = m3_tde_deiktes;
	}

	public List<M3_TDE_Problems> getM3_tde_problems() {
		return m3_tde_problems;
	}

	public void setM3_tde_problems(List<M3_TDE_Problems> m3_tde_problems) {
		this.m3_tde_problems = m3_tde_problems;
	}

	public Section_O getTmp_prj3monthly() {
		return tmp_prj3monthly;
	}

	public void setTmp_prj3monthly(Section_O tmp_prj3monthly) {
		this.tmp_prj3monthly = tmp_prj3monthly;
	}

	public M3_TDE_Problems getTmp_m3_tde_problems() {
		return tmp_m3_tde_problems;
	}

	public void setTmp_m3_tde_problems(M3_TDE_Problems tmp_m3_tde_problems) {
		this.tmp_m3_tde_problems = tmp_m3_tde_problems;
	}

	public String getReportE1HTML() {
		return reportE1HTML;
	}

	public void setReportE1HTML(String reportE1HTML) {
		this.reportE1HTML = reportE1HTML;
	}

	public List<M3_TDE_Semesters> getM3_tde_semesters() {
		return m3_tde_semesters;
	}

	public void setM3_tde_semesters(List<M3_TDE_Semesters> m3_tde_semesters) {
		this.m3_tde_semesters = m3_tde_semesters;
	}

	public M3_TDE_Semesters getTmp_m3_tde_semester() {
		return tmp_m3_tde_semester;
	}

	public void setTmp_m3_tde_semester(M3_TDE_Semesters tmp_m3_tde_semester) {
		this.tmp_m3_tde_semester = tmp_m3_tde_semester;
	}

	public List<SelectItem> getExpensesSelectItems() {
		return expensesSelectItemsList;
	}

	public void setExpensesSelectItems(List<SelectItem> expensesSelectItemsList) {
		this.expensesSelectItemsList = expensesSelectItemsList;
	}

	public String getReportE3HTML() {
		return reportE3HTML;
	}

	public void setReportE3HTML(String reportE3HTML) {
		this.reportE3HTML = reportE3HTML;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public List<Section_A> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<Section_A> revisions) {
		this.revisions = revisions;
	}

	/**
	 * @return the regionsSelectItems
	 */
	public List<SelectItem> getRegionsSelectItems() {
		return regionsSelectItems;
	}

	/**
	 * @param regionsSelectItems the regionsSelectItems to set
	 */
	public void setRegionsSelectItems(List<SelectItem> regionsSelectItems) {
		this.regionsSelectItems = regionsSelectItems;
	}

	/**
	 * @return the vendors
	 */
	public List<TDE_Vendors> getVendors() {
		return vendors;
	}

	/**
	 * @param vendors the vendors to set
	 */
	public void setVendors(List<TDE_Vendors> vendors) {
		this.vendors = vendors;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the all_tde_financials
	 */
	public Map<Section_L, List<TDE_Financial>> getAll_tde_financials() {
		return all_tde_financials;
	}

	/**
	 * @param all_tde_financials the all_tde_financials to set
	 */
	public void setAll_tde_financials(
			Map<Section_L, List<TDE_Financial>> all_tde_financials) {
		this.all_tde_financials = all_tde_financials;
	}

	/**
	 * @return the all_tdy_progress
	 */
	public Map<Section_L, List<TDY_Progress>> getAll_tdy_progress() {
		return all_tdy_progress;
	}

	/**
	 * @param all_tdy_progress the all_tdy_progress to set
	 */
	public void setAll_tdy_progress(
			Map<Section_L, List<TDY_Progress>> all_tdy_progress) {
		this.all_tdy_progress = all_tdy_progress;
	}

	/**
	 * @return the all_tdy_places
	 */
	public Map<Section_L, List<TDY_Places>> getAll_tdy_places() {
		return all_tdy_places;
	}

	/**
	 * @param all_tdy_places the all_tdy_places to set
	 */
	public void setAll_tdy_places(Map<Section_L, List<TDY_Places>> all_tdy_places) {
		this.all_tdy_places = all_tdy_places;
	}

	/**
	 * @return the all_tdy_deiktes
	 */
	public Map<Section_L, List<TDY_Deiktes>> getAll_tdy_deiktes() {
		return all_tdy_deiktes;
	}

	/**
	 * @param all_tdy_deiktes the all_tdy_deiktes to set
	 */
	public void setAll_tdy_deiktes(Map<Section_L, List<TDY_Deiktes>> all_tdy_deiktes) {
		this.all_tdy_deiktes = all_tdy_deiktes;
	}

	/**
	 * @return the all_tdy_progressActivities
	 */
	public Map<Section_L, List<TDY_ProgressActivities>> getAll_tdy_progressActivities() {
		return all_tdy_progressActivities;
	}

	/**
	 * @param all_tdy_progressActivities the all_tdy_progressActivities to set
	 */
	public void setAll_tdy_progressActivities(
			Map<Section_L, List<TDY_ProgressActivities>> all_tdy_progressActivities) {
		this.all_tdy_progressActivities = all_tdy_progressActivities;
	}

	/**
	 * @return the all_tdy_financial
	 */
	public Map<Section_L, List<TDY_Financial>> getAll_tdy_financial() {
		return all_tdy_financial;
	}

	/**
	 * @param all_tdy_financial the all_tdy_financial to set
	 */
	public void setAll_tdy_financial(
			Map<Section_L, List<TDY_Financial>> all_tdy_financial) {
		this.all_tdy_financial = all_tdy_financial;
	}

	/**
	 * @return the all_tdy_evolution
	 */
	public Map<Section_L, List<TDY_Evolution>> getAll_tdy_evolution() {
		return all_tdy_evolution;
	}

	/**
	 * @param all_tdy_evolution the all_tdy_evolution to set
	 */
	public void setAll_tdy_evolution(
			Map<Section_L, List<TDY_Evolution>> all_tdy_evolution) {
		this.all_tdy_evolution = all_tdy_evolution;
	}

	/**
	 * @return the subprj_tdy_financial
	 */
	public List<TDY_Financial> getSubprj_tdy_financial() {
		return subprj_tdy_financial;
	}

	/**
	 * @param subprj_tdy_financial the subprj_tdy_financial to set
	 */
	public void setSubprj_tdy_financial(List<TDY_Financial> subprj_tdy_financial) {
		this.subprj_tdy_financial = subprj_tdy_financial;
	}

	/**
	 * @return the all_contractors
	 */
	public Map<Section_L, Contractor> getAll_contractors() {
		return all_contractors;
	}

	/**
	 * @param all_contractors the all_contractors to set
	 */
	public void setAll_contractors(Map<Section_L, Contractor> all_contractors) {
		this.all_contractors = all_contractors;
	}

	/**
	 * @return the defaultActionsCategoriesSelectItemsList
	 */

	/**
	 * @return the defaultVendorsSelectItemsList
	 */
	public List<SelectItem> getDefaultVendorsSelectItemsList() {
		return defaultVendorsSelectItemsList;
	}

	/**
	 * @param defaultVendorsSelectItemsList the defaultVendorsSelectItemsList to set
	 */
	public void setDefaultVendorsSelectItemsList(
			List<SelectItem> defaultVendorsSelectItemsList) {
		this.defaultVendorsSelectItemsList = defaultVendorsSelectItemsList;
	}

	
	/**
	 * @return the supervisingVendorsSelectItemsList
	 */
	public List<SelectItem> getSupervisingVendorsSelectItemsList() {
		return supervisingVendorsSelectItemsList;
	}

	/**
	 * @param supervisingVendorsSelectItemsList the supervisingVendorsSelectItemsList to set
	 */
	public void setSupervisingVendorsSelectItemsList(
			List<SelectItem> supervisingVendorsSelectItemsList) {
		this.supervisingVendorsSelectItemsList = supervisingVendorsSelectItemsList;
	}

	/**
	 * @return the defaultDeiktesEkroisSelectItemsList
	 */
	public List<SelectItem> getDefaultDeiktesSelectItemsList() {
		return defaultDeiktesSelectItemsList;
	}

	/**
	 * @param defaultDeiktesEkroisSelectItemsList the defaultDeiktesEkroisSelectItemsList to set
	 */
	public void setDefaultDeiktesSelectItemsList(
			List<SelectItem> defaultDeiktesSelectItemsList) {
		this.defaultDeiktesSelectItemsList = defaultDeiktesSelectItemsList;
	}

	/**
	 * @return the defaultProgressSelectItemsList
	 */
	public List<SelectItem> getDefaultProgressSelectItemsList() {
		return defaultProgressSelectItemsList;
	}

	/**
	 * @param defaultProgressSelectItemsList the defaultProgressSelectItemsList to set
	 */
	public void setDefaultProgressSelectItemsList(
			List<SelectItem> defaultProgressSelectItemsList) {
		this.defaultProgressSelectItemsList = defaultProgressSelectItemsList;
	}

	/**
	 * @return the defaultEvolutionSelectItemsList
	 */
	public List<SelectItem> getDefaultEvolutionSelectItemsList() {
		return defaultEvolutionSelectItemsList;
	}

	/**
	 * @param defaultEvolutionSelectItemsList the defaultEvolutionSelectItemsList to set
	 */
	public void setDefaultEvolutionSelectItemsList(
			List<SelectItem> defaultEvolutionSelectItemsList) {
		this.defaultEvolutionSelectItemsList = defaultEvolutionSelectItemsList;
	}

	/**
	 * @return the defaultInvoiceTypesSelectItemsList
	 */
	public List<SelectItem> getDefaultInvoiceTypesSelectItemsList() {
		return defaultInvoiceTypesSelectItemsList;
	}

	/**
	 * @param defaultInvoiceTypesSelectItemsList the defaultInvoiceTypesSelectItemsList to set
	 */
	public void setDefaultInvoiceTypesSelectItemsList(
			List<SelectItem> defaultInvoiceTypesSelectItemsList) {
		this.defaultInvoiceTypesSelectItemsList = defaultInvoiceTypesSelectItemsList;
	}

	/**
	 * @return the defaultFundersSelectItemsList
	 */
	public List<SelectItem> getDefaultFundersSelectItemsList() {
		return defaultFundersSelectItemsList;
	}

	/**
	 * @param defaultFundersSelectItemsList the defaultFundersSelectItemsList to set
	 */
	public void setDefaultFundersSelectItemsList(
			List<SelectItem> defaultFundersSelectItemsList) {
		this.defaultFundersSelectItemsList = defaultFundersSelectItemsList;
	}

	/**
	 * @return the expensesSelectItemsList
	 */
	public List<SelectItem> getExpensesSelectItemsList() {
		return expensesSelectItemsList;
	}

	/**
	 * @param expensesSelectItemsList the expensesSelectItemsList to set
	 */
	public void setExpensesSelectItemsList(List<SelectItem> expensesSelectItemsList) {
		this.expensesSelectItemsList = expensesSelectItemsList;
	}
	
	public boolean getExpired() {
		if (this.kbean == null || this.ubean == null)
			return true;
		else
			return false;
	}

	/**
	 * @return the defaultEUFramworksSelectItemsList
	 */
	public List<SelectItem> getDefaultEUFrameworksSelectItemsList() {
		return defaultEUFrameworksSelectItemsList;
	}

	/**
	 * @param defaultEUFramworksSelectItemsList the defaultEUFramworksSelectItemsList to set
	 */
	public void setDefaultEUFrameworksSelectItemsList(
			List<SelectItem> defaultEUFrameworksSelectItemsList) {
		this.defaultEUFrameworksSelectItemsList = defaultEUFrameworksSelectItemsList;
	}

	/**
	 * @return the defaultEUProgrammesSelectItemsList
	 */
	public List<SelectItem> getDefaultEUProgrammesSelectItemsList() {
		return defaultEUProgrammesSelectItemsList;
	}

	/**
	 * @param defaultEUProgrammesSelectItemsList the defaultEUProgrammesSelectItemsList to set
	 */
	public void setDefaultEUProgrammesSelectItemsList(
			List<SelectItem> defaultEUProgrammesSelectItemsList) {
		this.defaultEUProgrammesSelectItemsList = defaultEUProgrammesSelectItemsList;
	}

	/**
	 * @return the defaultEUAxonsSelectItemsList
	 */
	public List<SelectItem> getDefaultEUAxonsSelectItemsList() {
		return defaultEUAxonsSelectItemsList;
	}

	/**
	 * @param defaultEUAxonsSelectItemsList the defaultEUAxonsSelectItemsList to set
	 */
	public void setDefaultEUAxonsSelectItemsList(
			List<SelectItem> defaultEUAxonsSelectItemsList) {
		this.defaultEUAxonsSelectItemsList = defaultEUAxonsSelectItemsList;
	}

	/**
	 * @return the defaultEUMeasuresSelectItemsList
	 */
	public List<SelectItem> getDefaultEUMeasuresSelectItemsList() {
		return defaultEUMeasuresSelectItemsList;
	}

	/**
	 * @param defaultEUMeasuresSelectItemsList the defaultEUMeasuresSelectItemsList to set
	 */
	public void setDefaultEUMeasuresSelectItemsList(
			List<SelectItem> defaultEUMeasuresSelectItemsList) {
		this.defaultEUMeasuresSelectItemsList = defaultEUMeasuresSelectItemsList;
	}

	/**
	 * @return the defaultEUSubMeasuresSelectItemsList
	 */
	public List<SelectItem> getDefaultEUSubMeasuresSelectItemsList() {
		return defaultEUSubMeasuresSelectItemsList;
	}

	/**
	 * @param defaultEUSubMeasuresSelectItemsList the defaultEUSubMeasuresSelectItemsList to set
	 */
	public void setDefaultEUSubMeasuresSelectItemsList(
			List<SelectItem> defaultEUSubMeasuresSelectItemsList) {
		this.defaultEUSubMeasuresSelectItemsList = defaultEUSubMeasuresSelectItemsList;
	}

	/**
	 * @return the defaultEUSectionsSelectItemsList
	 */
	public List<SelectItem> getDefaultEUSectionsSelectItemsList() {
		return defaultEUSectionsSelectItemsList;
	}

	/**
	 * @param defaultEUSectionsSelectItemsList the defaultEUSectionsSelectItemsList to set
	 */
	public void setDefaultEUSectionsSelectItemsList(
			List<SelectItem> defaultEUSectionsSelectItemsList) {
		this.defaultEUSectionsSelectItemsList = defaultEUSectionsSelectItemsList;
	}

	/**
	 * @return the defaultEUSubSectionsSelectItemsList
	 */
	public List<SelectItem> getDefaultEUSubSectionsSelectItemsList() {
		return defaultEUSubSectionsSelectItemsList;
	}

	/**
	 * @param defaultEUSubSectionsSelectItemsList the defaultEUSubSectionsSelectItemsList to set
	 */
	public void setDefaultEUSubSectionsSelectItemsList(
			List<SelectItem> defaultEUSubSectionsSelectItemsList) {
		this.defaultEUSubSectionsSelectItemsList = defaultEUSubSectionsSelectItemsList;
	}

	/**
	 * @return the reportsListDataTable
	 */
	public HtmlDataTable getReportsListDataTable() {
		return reportsListDataTable;
	}

	/**
	 * @param reportsListDataTable the reportsListDataTable to set
	 */
	public void setReportsListDataTable(HtmlDataTable reportsListDataTable) {
		this.reportsListDataTable = reportsListDataTable;
	}

	/**
	 * @return the sectionZ_categoriesBudget
	 */
	public BigDecimal getSectionZ_categoriesBudget() {
		return sectionZ_categoriesBudget;
	}

	/**
	 * @param sectionZ_categoriesBudget the sectionZ_categoriesBudget to set
	 */
	public void setSectionZ_categoriesBudget(BigDecimal sectionZ_categoriesBudget) {
		this.sectionZ_categoriesBudget = sectionZ_categoriesBudget;
	}

	/**
	 * @return the tmp_id
	 */
	public Integer getTmp_id() {
		return tmp_id;
	}

	/**
	 * @param tmp_id the tmp_id to set
	 */
	public void setTmp_id(Integer tmp_id) {
		this.tmp_id = tmp_id;
	}

	public BigDecimal getSection_o_total_budget() {
		return section_o_total_budget;
	}

	public void setSection_o_total_budget(BigDecimal section_o_total_budget) {
		this.section_o_total_budget = section_o_total_budget;
	}

	/**
	 * @return the defaultSubProjectTypesSelectItemsList
	 */
	public List<SelectItem> getDefaultSubProjectTypesSelectItemsList() {
		return defaultSubProjectTypesSelectItemsList;
	}

	/**
	 * @param defaultSubProjectTypesSelectItemsList the defaultSubProjectTypesSelectItemsList to set
	 */
	public void setDefaultSubProjectTypesSelectItemsList(
			List<SelectItem> defaultSubProjectTypesSelectItemsList) {
		this.defaultSubProjectTypesSelectItemsList = defaultSubProjectTypesSelectItemsList;
	}

	/**
	 * @return the framework
	 */
	public EUFramework getFramework() {
		return framework;
	}

	/**
	 * @param framework the framework to set
	 */
	public void setFramework(EUFramework framework) {
		this.framework = framework;
	}

	/**
	 * @return the programme
	 */
	public EUProgramme getProgramme() {
		return programme;
	}

	/**
	 * @param programme the programme to set
	 */
	public void setProgramme(EUProgramme programme) {
		this.programme = programme;
	}

	/**
	 * @return the axis
	 */
	public EUPriorityAxis getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(EUPriorityAxis axis) {
		this.axis = axis;
	}

	/**
	 * @return the measure
	 */
	public EUMeasure getMeasure() {
		return measure;
	}

	/**
	 * @param measure the measure to set
	 */
	public void setMeasure(EUMeasure measure) {
		this.measure = measure;
	}

	/**
	 * @return the submeasure
	 */
	public EUSubmeasure getSubmeasure() {
		return submeasure;
	}

	/**
	 * @param submeasure the submeasure to set
	 */
	public void setSubmeasure(EUSubmeasure submeasure) {
		this.submeasure = submeasure;
	}

	/**
	 * @return the section
	 */
	public EUFPSSection getSection() {
		return section;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(EUFPSSection section) {
		this.section = section;
	}

	/**
	 * @return the subsection
	 */
	public EUFPSSubsection getSubsection() {
		return subsection;
	}

	/**
	 * @param subsection the subsection to set
	 */
	public void setSubsection(EUFPSSubsection subsection) {
		this.subsection = subsection;
	}

	/**
	 * @return the projectaction
	 */
	public ActionsCategory getProjectaction() {
		return projectaction;
	}

	/**
	 * @param projectaction the projectaction to set
	 */
	public void setProjectaction(ActionsCategory projectaction) {
		this.projectaction = projectaction;
	}

	/**
	 * @return the selectedNomSubProjects
	 */
	public HashMap<NOM_SubProjects, Boolean> getSelectedNomSubProjects() {
		return selectedNomSubProjects;
	}

	/**
	 * @param selectedNomSubProjects the selectedNomSubProjects to set
	 */
	public void setSelectedNomSubProjects(
			HashMap<NOM_SubProjects, Boolean> selectedNomSubProjects) {
		this.selectedNomSubProjects = selectedNomSubProjects;
	}

	/**
	 * @return the proposalVendor_rangePrefectureSelectItems
	 */
	public List<SelectItem> getProposalVendor_rangePrefectureSelectItems() {
		return proposalVendor_rangePrefectureSelectItems;
	}

	/**
	 * @param proposalVendor_rangePrefectureSelectItems the proposalVendor_rangePrefectureSelectItems to set
	 */
	public void setProposalVendor_rangePrefectureSelectItems(
			List<SelectItem> proposalVendor_rangePrefectureSelectItems) {
		this.proposalVendor_rangePrefectureSelectItems = proposalVendor_rangePrefectureSelectItems;
	}

	/**
	 * @return the proposalVendor_rangeMunicipalitySelectItems
	 */
	public List<SelectItem> getProposalVendor_rangeMunicipalitySelectItems() {
		return proposalVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @param proposalVendor_rangeMunicipalitySelectItems the proposalVendor_rangeMunicipalitySelectItems to set
	 */
	public void setProposalVendor_rangeMunicipalitySelectItems(
			List<SelectItem> proposalVendor_rangeMunicipalitySelectItems) {
		this.proposalVendor_rangeMunicipalitySelectItems = proposalVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @return the implementationVendor_rangePrefectureSelectItems
	 */
	public List<SelectItem> getImplementationVendor_rangePrefectureSelectItems() {
		return implementationVendor_rangePrefectureSelectItems;
	}

	/**
	 * @param implementationVendor_rangePrefectureSelectItems the implementationVendor_rangePrefectureSelectItems to set
	 */
	public void setImplementationVendor_rangePrefectureSelectItems(
			List<SelectItem> implementationVendor_rangePrefectureSelectItems) {
		this.implementationVendor_rangePrefectureSelectItems = implementationVendor_rangePrefectureSelectItems;
	}

	/**
	 * @return the implementationVendor_rangeMunicipalitySelectItems
	 */
	public List<SelectItem> getImplementationVendor_rangeMunicipalitySelectItems() {
		return implementationVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @param implementationVendor_rangeMunicipalitySelectItems the implementationVendor_rangeMunicipalitySelectItems to set
	 */
	public void setImplementationVendor_rangeMunicipalitySelectItems(
			List<SelectItem> implementationVendor_rangeMunicipalitySelectItems) {
		this.implementationVendor_rangeMunicipalitySelectItems = implementationVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @return the operationVendor_rangePrefectureSelectItems
	 */
	public List<SelectItem> getOperationVendor_rangePrefectureSelectItems() {
		return operationVendor_rangePrefectureSelectItems;
	}

	/**
	 * @param operationVendor_rangePrefectureSelectItems the operationVendor_rangePrefectureSelectItems to set
	 */
	public void setOperationVendor_rangePrefectureSelectItems(
			List<SelectItem> operationVendor_rangePrefectureSelectItems) {
		this.operationVendor_rangePrefectureSelectItems = operationVendor_rangePrefectureSelectItems;
	}

	/**
	 * @return the operationVendor_rangeMunicipalitySelectItems
	 */
	public List<SelectItem> getOperationVendor_rangeMunicipalitySelectItems() {
		return operationVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @param operationVendor_rangeMunicipalitySelectItems the operationVendor_rangeMunicipalitySelectItems to set
	 */
	public void setOperationVendor_rangeMunicipalitySelectItems(
			List<SelectItem> operationVendor_rangeMunicipalitySelectItems) {
		this.operationVendor_rangeMunicipalitySelectItems = operationVendor_rangeMunicipalitySelectItems;
	}

	/**
	 * @return the checkProcessed
	 */
	public boolean isCheckProcessed() {
		return checkProcessed;
	}

	/**
	 * @param checkProcessed the checkProcessed to set
	 */
	public void setCheckProcessed(boolean checkProcessed) {
		this.checkProcessed = checkProcessed;
	}

	/**
	 * @return the quarters
	 */
	public List<Integer> getQuarters() {
		return quarters;
	}

	/**
	 * @param quarters the quarters to set
	 */
	public void setQuarters(List<Integer> quarters) {
		this.quarters = quarters;
	}

	/**
	 * @return the invoice_contractor
	 */
	public Invoice_Contractor getInvoice_contractor() {
		return invoice_contractor;
	}

	/**
	 * @param invoice_contractor the invoice_contractor to set
	 */
	public void setInvoice_contractor(Invoice_Contractor invoice_contractor) {
		this.invoice_contractor = invoice_contractor;
	}

	/**
	 * @return the yearBudgetMatrix
	 */
	public HashMap<Integer, BigDecimal> getYearBudgetMatrix() {
		return yearBudgetMatrix;
	}

	/**
	 * @param yearBudgetMatrix the yearBudgetMatrix to set
	 */
	public void setYearBudgetMatrix(HashMap<Integer, BigDecimal> yearBudgetMatrix) {
		this.yearBudgetMatrix = yearBudgetMatrix;
	}

	/**
	 * @return the date_approval
	 */
	public Date getDate_approval() {
		return date_approval;
	}

	/**
	 * @param date_approval the date_approval to set
	 */
	public void setDate_approval(Date date_approval) {
		this.date_approval = date_approval;
	}

	/**
	 * @return the contractorId
	 */
	public Integer getContractorId() {
		return contractorId;
	}

	/**
	 * @param contractorId the contractorId to set
	 */
	public void setContractorId(Integer contractorId) {
		this.contractorId = contractorId;
	}

	/**
	 * @return the invoiceType
	 */
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	/**
	 * @param invoiceType the invoiceType to set
	 */
	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	public boolean isLatestSubmitted() {
		try {
			Section_A a = (Section_A) kbean.getLatestRevisionObject( Section_A.class, this.sectionClass );
			if (a != null)
				return a.isSubmitted();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
