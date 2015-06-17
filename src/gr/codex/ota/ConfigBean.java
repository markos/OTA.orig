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

/* $Id: ConfigBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.ota;

import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.ota.helpers.ActionsCategory;
import gr.codex.ota.helpers.EUFPSSection;
import gr.codex.ota.helpers.EUFPSSubsection;
import gr.codex.ota.helpers.EUFramework;
import gr.codex.ota.helpers.EUMeasure;
import gr.codex.ota.helpers.EUPriorityAxis;
import gr.codex.ota.helpers.EUProgramme;
import gr.codex.ota.helpers.EUSubmeasure;
import gr.codex.ota.helpers.InvoiceType;
import gr.codex.ota.helpers.SubProjectType;
import gr.codex.ota.helpers.TDE_Deiktes;
import gr.codex.ota.helpers.TDE_Funders;
import gr.codex.ota.helpers.TDE_Vendors;
import gr.codex.ota.helpers.TDY_Evolution;
import gr.codex.ota.helpers.TDY_Financial;
import gr.codex.ota.helpers.TDY_Progress;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author markos
 *
 */
public class ConfigBean {
	private static final Integer configId = -1;
	
	Context ctx;
	KatartisisBeanLocal kbean;
	private List<TDE_Funders> defaultFundersList;
	private List<TDE_Vendors> defaultVendorsList;
	private List<TDE_Deiktes> defaultDeiktesList;
	private List<TDY_Evolution> defaultEvolutionList;
	private List<TDY_Progress> defaultProgressList;
	private List<TDY_Financial> defaultExpensesList;
	private List<InvoiceType> defaultInvoiceTypesList;
	private List<SubProjectType> defaultSubProjectTypesList;
	private List<ActionsCategory> defaultActionsCategoriesList;
	private List<EUFramework> defaultEUFrameworksList;
	private List<EUProgramme> defaultEUProgrammesList;
	private List<EUPriorityAxis> defaultEUPriorityAxesList;
	private List<EUMeasure> defaultEUMeasuresList;
	private List<EUSubmeasure> defaultEUSubMeasuresList;
	private List<EUFPSSection> defaultEUSectionsList;
	private List<EUFPSSubsection> defaultEUSubSectionsList;
	private List testSubs;
	
	// HtmlDataTables
	private HtmlDataTable tmp_dataTable;
	private HtmlDataTable tmp_dataTable2;
	private HtmlDataTable tmp_dataTable3;
	private HtmlDataTable tmp_dataTable4;
	private HtmlDataTable tmp_dataTable5;
	private HtmlDataTable tmp_dataTable6;
	private HtmlDataTable tmp_dataTable7;
	private HtmlDataTable tmp_dataTable8;
	private HtmlDataTable progressDataTable;
	private HtmlDataTable deiktesDataTable;
	private HtmlSelectManyListbox tmp_selectManyListBox;
	private ArrayList<SelectItem> defaultEUFrameworksSelectItemsList;
	private ArrayList<SelectItem> defaultEUProgrammesSelectItemsList;
	private ArrayList<SelectItem> defaultEUPriorityAxesSelectItemsList;
	private ArrayList<SelectItem> defaultSubProjectTypesSelectItemsList;
	private ArrayList<SelectItem> defaultActionsCategoriesSelectItemsList;
	
	private String label;
	
	
	public ConfigBean() {
		try {
			ctx = new InitialContext();
			
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			
			this.defaultFundersList = kbean.getFundersBySectionZId(this.configId);
			this.defaultVendorsList = kbean.getVendorsBySectionAId(this.configId);
					
			this.defaultDeiktesList        = (List<TDE_Deiktes>) kbean.getAllRecordsOfType(TDE_Deiktes.class);

			this.defaultEvolutionList = kbean.getEvolutionBySectionLId(this.configId);
			this.defaultProgressList = kbean.getProgressBySectionLId(this.configId);
			this.defaultExpensesList = kbean.getTDYFinancialBySectionLId(this.configId);
			this.defaultInvoiceTypesList = (List<InvoiceType>) kbean.getAllRecordsOfType(InvoiceType.class);
			this.defaultActionsCategoriesList = (List<ActionsCategory>) kbean.getAllRecordsOfType(ActionsCategory.class);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		label = "";
	}

	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	public void clearAllDataTables() {
		this.tmp_dataTable = new HtmlDataTable();
		this.tmp_dataTable2 = new HtmlDataTable();
		this.tmp_dataTable3 = new HtmlDataTable();
		this.tmp_dataTable4 = new HtmlDataTable();
		this.tmp_dataTable5 = new HtmlDataTable();
		this.tmp_dataTable6 = new HtmlDataTable();
		this.tmp_dataTable7 = new HtmlDataTable();
		this.tmp_dataTable8 = new HtmlDataTable();
		this.progressDataTable = new HtmlDataTable();
		this.deiktesDataTable = new HtmlDataTable();
	}
	
	/*
	public <T> void saveEntities(List<T> oblist) {
		for( Iterator iter= oblist.iterator(); iter.hasNext(); ) {
			 T ob = (T) iter.next();
			
			if (ob.getId() == null)
				ob = (T) kbean.createRecord( ob );
			else
				kbean.updateRecord(ob);
		}
	}*/
	
	/* goTo Methods */
	public String goToFunders() {	
		this.defaultFundersList = kbean.getFundersBySectionZId(this.configId);
			
		clearAllDataTables();
			
		return "funders";
	}
	
	public String goToVendors() {	
		this.defaultVendorsList = kbean.getVendorsBySectionAId(this.configId);
			
		clearAllDataTables();
			
		return "vendors";
	}
	
	public String goToDeiktes() {
		this.defaultDeiktesList        = (List<TDE_Deiktes>) kbean.getAllRecordsOfType(TDE_Deiktes.class);
		this.defaultActionsCategoriesList = (List<ActionsCategory>) kbean.getAllRecordsOfType(ActionsCategory.class);
		
		regenerateDeiktesSelectLists();
		
		clearAllDataTables();
		
		return "deiktes";
	}
	
	public void regenerateDeiktesSelectLists() {
		this.defaultActionsCategoriesSelectItemsList = new ArrayList<SelectItem>();
		for (ActionsCategory action: this.defaultActionsCategoriesList) {
			SelectItem s = new SelectItem(action, action.getCategory());
			this.defaultActionsCategoriesSelectItemsList.add(s);
		}
	}
	
	public String goToEvolution() {	
		this.defaultEvolutionList = kbean.getEvolutionBySectionLId(this.configId);
		this.defaultSubProjectTypesList = (List<SubProjectType>) kbean.getAllRecordsOfType(SubProjectType.class);
				
		regenerateSubProjectTypesSelectLists();
		
		clearAllDataTables();
			
		return "evolution";
	}
	
	public String goToProgress() {
		this.defaultProgressList = kbean.getProgressBySectionLId(this.configId);
		this.defaultActionsCategoriesList = (List<ActionsCategory>) kbean.getAllRecordsOfType(ActionsCategory.class);
	
		regenerateProgressSelectLists();
			
		clearAllDataTables();
			
		return "progress";
	}
	
	public void regenerateProgressSelectLists() {
		this.defaultActionsCategoriesSelectItemsList = new ArrayList<SelectItem>();
		for (ActionsCategory action: this.defaultActionsCategoriesList) {
			SelectItem s = new SelectItem(action.getId(), action.getCategory());
			this.defaultActionsCategoriesSelectItemsList.add(s);
		}
	}
	
	public String goToExpenses() {
		this.defaultExpensesList = kbean.getTDYFinancialBySectionLId(this.configId);
		this.defaultEUFrameworksList = (List<EUFramework>) kbean.getAllRecordsOfType(EUFramework.class);
		
		regeneratePlaisioSelectLists();
		
		clearAllDataTables();
		
		return "expenses";
	}
	
	public String goToInvoiceTypes() {
		this.defaultInvoiceTypesList = (List<InvoiceType>) kbean.getAllRecordsOfType(InvoiceType.class);
		
		clearAllDataTables();
		
		return "invoicetypes";
	}
	
	public String goToSubProjectTypes() {
		this.defaultSubProjectTypesList = (List<SubProjectType>) kbean.getAllRecordsOfType(SubProjectType.class);
		
		regenerateSubProjectTypesSelectLists();
		
		clearAllDataTables();
		
		return "subprojecttypes";
	}
	
	public void regenerateSubProjectTypesSelectLists() {
		this.defaultSubProjectTypesSelectItemsList = new ArrayList<SelectItem>();
		for (SubProjectType sub: this.defaultSubProjectTypesList) {
			SelectItem s = new SelectItem(sub, sub.getType() );
			this.defaultSubProjectTypesSelectItemsList.add(s);
		}
	}
	
	public String goToPlaisio() {
		this.defaultEUFrameworksList = (List<EUFramework>) kbean.getAllRecordsOfType(EUFramework.class);
		
		this.defaultEUProgrammesList = (List<EUProgramme>) kbean.getAllRecordsOfType(EUProgramme.class);
		
		this.defaultEUPriorityAxesList = (List<EUPriorityAxis>) kbean.getAllRecordsOfType(EUPriorityAxis.class);
		
		this.defaultEUMeasuresList = (List<EUMeasure>) kbean.getAllRecordsOfType(EUMeasure.class);
		
		this.defaultEUSubMeasuresList = (List<EUSubmeasure>) kbean.getAllRecordsOfType(EUSubmeasure.class);
		
		this.defaultEUSectionsList = (List<EUFPSSection>) kbean.getAllRecordsOfType(EUFPSSection.class);
		
		this.defaultEUSubSectionsList = (List<EUFPSSubsection>) kbean.getAllRecordsOfType(EUFPSSubsection.class);
		
		regeneratePlaisioSelectLists();
		
		clearAllDataTables();
		
		return "plaisio";
	}
	
	public void regeneratePlaisioSelectLists() {
		this.defaultEUFrameworksSelectItemsList = new ArrayList<SelectItem>();
		this.defaultEUFrameworksSelectItemsList.add(new SelectItem(new Integer(-1), "Παρακαλώ επιλέξτε"));
		for (EUFramework frm: this.defaultEUFrameworksList) {
			SelectItem s = new SelectItem(frm.getId(), frm.getName());
			this.defaultEUFrameworksSelectItemsList.add(s);
		}
	}
	
	/* Funder methods */	
	public void saveDefaultFundersList() {	
		for( Iterator iter=this.defaultFundersList.iterator(); iter.hasNext(); ) {
			TDE_Funders funder = (TDE_Funders) iter.next();
			String code = funder.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			if (funder.getId() == null)
				funder = (TDE_Funders) kbean.createRecord( funder );
			else
				funder = (TDE_Funders) kbean.updateRecord(funder);
			
			if (funder == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Πηγή Χρηματοδότησης με Κωδικό " + code) );
			}
		}	
	}
	
	public String addDefaultFunder() {
		TDE_Funders f = new TDE_Funders();
		f.setDescription("νέος χρηματοδότης");
		f.setSec_z_id(this.configId);
		this.defaultFundersList.add(f);
		
		return "funders";
	}

	public String deleteDefaultFunder() {
		TDE_Funders tmp_pobj = (TDE_Funders) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Funders object: " +tmp_pobj.getDescription() + ", " + tmp_pobj.getId() );
		this.defaultFundersList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {   		
   			// now remove the object
       		kbean.deleteRecord( TDE_Funders.class, tmp_pobj.getId() );
		}
		
		return "funders";
	}
	
	/* Vendors methods */
	public void saveDefaultVendorsList() {
		for( Iterator iter=this.defaultVendorsList.iterator(); iter.hasNext(); ) {
			TDE_Vendors vendor = (TDE_Vendors) iter.next();
			String code = vendor.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			if (vendor.getId() == null)
				vendor = (TDE_Vendors) kbean.createRecord( vendor );
			else
				vendor = (TDE_Vendors) kbean.updateRecord(vendor);
						
			if (vendor == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Φορέας με Κωδικό " + code) );
			}
		}
	}
	
	public String addDefaultVendor() {
		
		TDE_Vendors tmp_vendor = new TDE_Vendors();
		tmp_vendor.setSec_a_id(this.configId);
		tmp_vendor.setName("νέος φορέας");
		this.defaultVendorsList.add( tmp_vendor);
		
		return "vendors";		
	}
		
	public String deleteDefaultVendor() {
		TDE_Vendors tmp_vendor = (TDE_Vendors) this.tmp_dataTable.getRowData(); 
        
        System.out.println( "removing Vendor object: " +tmp_vendor.getName() + ", " + tmp_vendor.getId() );
        this.defaultVendorsList.remove(tmp_vendor);
        if (tmp_vendor.getId() != null) {
        	kbean.deleteRecord(TDE_Vendors.class, tmp_vendor.getId() );
			
        }
        
		return "vendors";
	}
	
	/* Deiktes methods */
	public void saveDefaultDeiktesList() {
		// Do Deiktes Ekrois
		for( Iterator iter=this.defaultDeiktesList.iterator(); iter.hasNext(); ) {
			TDE_Deiktes deiktis = (TDE_Deiktes) iter.next();
			String code = deiktis.getCode();
			System.out.println("deiktes.code: " +  deiktis.getCode());
			System.out.println("actions : " + deiktis.getActions() );
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			// Create the list of Actions
			List<ActionsCategory> actionList = new ArrayList<ActionsCategory>();
			
			for( Object actionId : deiktis.getActions() ) {
				Integer action_id = Integer.parseInt( actionId.toString() );
				ActionsCategory acat = (ActionsCategory) kbean.getRecordById( action_id, ActionsCategory.class );
				actionList.add(acat);
			}
			
			deiktis.setActions( actionList );
			
			if (deiktis.getId() == null) {
				deiktis = (TDE_Deiktes) kbean.createRecord( deiktis );
			} else
				deiktis = (TDE_Deiktes) kbean.updateRecord(deiktis);
			
			if (deiktis == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Δείκτης με Κωδικό " + code) );
			}
		}
	}
	
	public String addDefaultDeiktes() {
		TDE_Deiktes tmp_deiktis = new TDE_Deiktes();
		tmp_deiktis.setName("νέος δείκτης");
		this.defaultDeiktesList.add(tmp_deiktis);
			
		return "deiktes";
	}
		
	public String deleteDefaultDeiktes() {
		TDE_Deiktes tmp_deiktis = (TDE_Deiktes) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDE_Deiktis object: " +tmp_deiktis.getName() + ", " + tmp_deiktis.getId() );
		this.defaultDeiktesList.remove(tmp_deiktis);
		
		if (tmp_deiktis.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord(TDE_Deiktes.class, tmp_deiktis.getId() );
        }
		        
        return "deiktes";
	}
	
	public void deiktesPageFirst() {
		this.deiktesDataTable.setFirst(0);
    }

    public void deiktesPagePrevious() {
    	this.deiktesDataTable.setFirst(this.deiktesDataTable.getFirst() - this.deiktesDataTable.getRows());
    }

    public void deiktesPageNext() {
    	this.deiktesDataTable.setFirst(this.deiktesDataTable.getFirst() + this.deiktesDataTable.getRows());
    }

    public void deiktesPageLast() {
        int count = this.deiktesDataTable.getRowCount();
        int rows = this.deiktesDataTable.getRows();
        this.deiktesDataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
    }
	
	/* Evolution methods */
	public void saveDefaultEvolutionList() {
		// saveEntities(this.defaultEvolutionList);
		
		System.out.println( "TEST SUBS - " + this.testSubs );
		
		for( Iterator iter=this.defaultEvolutionList.iterator(); iter.hasNext(); ) {
			TDY_Evolution evolution = (TDY_Evolution) iter.next();
			String code = evolution.getCode();
			System.out.println( evolution.getSubprojectTypes() );
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			// Create the list of Actions
			List<SubProjectType> subList = new ArrayList<SubProjectType>();
			
			for( Object subId : evolution.getSubprojectTypes() ) {
				Integer sub_id = Integer.parseInt( subId.toString() );
				SubProjectType asub = (SubProjectType) kbean.getRecordById( sub_id, SubProjectType.class );
				subList.add(asub);
			}
			
			evolution.setSubprojectTypes( subList );
			
			if (evolution.getId() == null)
				evolution = (TDY_Evolution) kbean.createRecord( evolution );
			else
				evolution = (TDY_Evolution) kbean.updateRecord(evolution);
			
			if (evolution == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Διοικητική Κατάσταση Εξέλιξης Υποέργου με Κωδικό " + code) );
			}
		}
	}
	
	public String addDefaultEvolution() {
		TDY_Evolution evolution = new TDY_Evolution();
		evolution.setDescription("νέα κατάσταση");
		evolution.setSec_l_id(this.configId);
		this.defaultEvolutionList.add( evolution );
		
		return "evolution";
	}
	
	public String deleteDefaultEvolution() {
		TDY_Evolution tmp_pobj = (TDY_Evolution) this.tmp_dataTable.getRowData();
        
		this.defaultEvolutionList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( TDY_Evolution.class, tmp_pobj.getId() );
		}

		return "evolution";	
	}

	/* Progress methods */
	public void saveDefaultProgressList() {	
		for( Iterator iter=this.defaultProgressList.iterator(); iter.hasNext(); ) {
			TDY_Progress progress = (TDY_Progress) iter.next();
			String code = progress.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			if (progress.getId2() != null) {
				ActionsCategory action = (ActionsCategory) kbean.getRecordById(progress.getId2(), ActionsCategory.class);
				progress.setAction(action);
			}
			
			if (progress.getId() == null)
				progress = (TDY_Progress) kbean.createRecord( progress );
			else
				progress = (TDY_Progress) kbean.updateRecord(progress);
			
			if (progress == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Διακριτό Τμήμα με Κωδικό " + code) );
			}
		}
		
		regenerateProgressSelectLists();
	}
	
	public String addDefaultProgress() {
		TDY_Progress progress = new TDY_Progress();
		progress.setDescription("νέο διακριτό τμήμα Φ.Α.");
		progress.setAction(new ActionsCategory());
		progress.setSec_l_id(this.configId);
		this.defaultProgressList.add( progress );
		
		regenerateProgressSelectLists();
		
		return "progress";
	}
	
	public String deleteDefaultProgress() {
		TDY_Progress progress = (TDY_Progress) this.progressDataTable.getRowData();
        
		System.out.println( "removing TDY_Progress object: " + progress.getId() );
		this.defaultProgressList.remove(progress);
        if (progress.getId() != null) {
        	// now remove the object
        	kbean.deleteRecord(TDY_Progress.class, progress.getId() );
        }
        
        regenerateProgressSelectLists();
        
        return "progress";	
	}
	
	public void progressPageFirst() {
		this.progressDataTable.setFirst(0);
    }

    public void progressPagePrevious() {
    	this.progressDataTable.setFirst(this.progressDataTable.getFirst() - this.progressDataTable.getRows());
    }

    public void progressPageNext() {
    	this.progressDataTable.setFirst(this.progressDataTable.getFirst() + this.progressDataTable.getRows());
    }

    public void progressPageLast() {
        int count = this.progressDataTable.getRowCount();
        int rows = this.progressDataTable.getRows();
        this.progressDataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
    }

	/* Expenses methods */
	public void saveDefaultExpensesList() {
		for( Iterator iter=this.defaultExpensesList.iterator(); iter.hasNext(); ) {
			TDY_Financial financial = (TDY_Financial) iter.next();
			String code = financial.getCode();
			String category = financial.getCategory();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			Integer frmid = financial.getFramework().getId();
			
			if (financial.getId() == null)
				financial = (TDY_Financial) kbean.createRecord( financial );
			else
				financial = (TDY_Financial) kbean.updateRecord(financial);
			
			if (financial == null) {
				boolean unique = true;
				if (frmid == null || frmid < 0) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε ΚΠΣ!") );
				}
				
				if (code.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
					unique = false;
				}
				
				if (unique == true)
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Επιλέξιμη Δαπάνη με Κωδικό " + code) );
			}
		}
	}
	
	public String addDefaultExpense() {
		TDY_Financial financial = new TDY_Financial();
		financial.setCategory("νέα κατηγορία");
		financial.setSec_l_id(this.configId);
		this.defaultExpensesList.add( financial );
				
		return "expenses";
	}
	
	public String deleteDefaultExpense() {
		TDY_Financial tmp_pobj = (TDY_Financial) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing TDY_Financial object: " +tmp_pobj.getCategory() + ", " + tmp_pobj.getId() );
		this.defaultExpensesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( TDY_Financial.class, tmp_pobj.getId() );
        }

		return "expenses";	
	}
	
	/* InvoiceType methods */
	public void saveDefaultInvoiceTypesList() {
		for( Iterator iter=this.defaultInvoiceTypesList.iterator(); iter.hasNext(); ) {
			InvoiceType type = (InvoiceType) iter.next();
			String typename = type.getType();
			
			if (type.getId() == null)
				type = (InvoiceType) kbean.createRecord( type );
			else
				type = (InvoiceType) kbean.updateRecord(type);
			
			if (type == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Τύπος Παραστατικού " + typename) );
			}
		}
	}
	
	public String addDefaultInvoiceType() {
		InvoiceType type = new InvoiceType();
		type.setType("νέος τύπος");
		this.defaultInvoiceTypesList.add( type );
				
		return "invoicetypes";
	}
	
	public String deleteDefaultInvoiceType() {
		InvoiceType tmp_pobj = (InvoiceType) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing InvoiceType object: " +tmp_pobj.getType() + ", " + tmp_pobj.getId() );
		this.defaultInvoiceTypesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( InvoiceType.class, tmp_pobj.getId() );
        }

		return "invoicetypes";	
	}
	
	/* SubProjectType methods */
	public void saveDefaultSubprojectTypesList() {
		for( Iterator iter=this.defaultSubProjectTypesList.iterator(); iter.hasNext(); ) {
			SubProjectType type = (SubProjectType) iter.next();
			String code = type.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			if (type.getId() == null)
				type = (SubProjectType) kbean.createRecord( type );
			else
				type = (SubProjectType) kbean.updateRecord(type);
			
			if (type == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Τύπος Υποέργου με Κωδικό " + code) );
			}
		}
		
		regenerateSubProjectTypesSelectLists();
	}
	
	public String addDefaultSubprojectType() {
		SubProjectType type = new SubProjectType();
		type.setType("νέος τύπος");
		this.defaultSubProjectTypesList.add( type );
		
		regenerateSubProjectTypesSelectLists();
				
		return "subprojecttypes";
	}
	
	public String deleteDefaultSubprojectType() {
		SubProjectType tmp_pobj = (SubProjectType) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing SubProjectType object: " +tmp_pobj.getType() + ", " + tmp_pobj.getId() );
		this.defaultSubProjectTypesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( SubProjectType.class, tmp_pobj.getId() );
        }
		
		regenerateSubProjectTypesSelectLists();

		return "subprojecttypes";	
	}
	
	/* EUFrameworks methods */
	public void saveDefaultEUFrameworksList() {
		this.label = "EUFrameworksListLabel";
		
		for( Iterator iter=this.defaultEUFrameworksList.iterator(); iter.hasNext(); ) {
			EUFramework frm = (EUFramework) iter.next();
			String code = frm.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			if (frm.getYear_start().compareTo(frm.getYear_end()) > 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("ΚΠΣ : " + code + ": Το έτος έναρξης " + frm.getYear_start()
						+ " δε μπορεί να είναι μεταγενετερο του έτους λήξης " + frm.getYear_end() + "!") );
				this.label = "";
				return;
			}
			
			if (frm.getId() == null)
				frm = (EUFramework) kbean.createRecord( frm );
			else
				frm = (EUFramework) kbean.updateRecord(frm);
			
			if (frm == null) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Κοινοτικό Πλαίσιο Στήριξης με Κωδικό " + code) );
				this.label = "";
			}
		}
		
		regeneratePlaisioSelectLists();
	}
	
	public String addDefaultEUFramework() {
		EUFramework frm = new EUFramework();
		frm.setName("νέο Κοινοτικό Πλαίσιο Στήριξης");
		this.defaultEUFrameworksList.add( frm );
		
		regeneratePlaisioSelectLists();
		
		this.label = "EUFrameworksListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUFramework() {
		EUFramework tmp_pobj = (EUFramework) this.tmp_dataTable.getRowData();
        
		System.out.println( "removing EUFramework object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUFrameworksList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUFramework.class, tmp_pobj.getId() );
        }
		
		regeneratePlaisioSelectLists();

		this.label = "EUFrameworksListLabel";
		
		return "plaisio";	
	}
	
	public void saveDefaultEUProgrammesList() {
		this.label = "EUProgrammesListLabel";

		for( Iterator iter=this.defaultEUProgrammesList.iterator(); iter.hasNext(); ) {
			EUProgramme prog = (EUProgramme) iter.next();
			String code = prog.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUFramework frm = (EUFramework) kbean.getRecordById(prog.getParentFramework().getId(), EUFramework.class);
			if (frm != null) {
				prog.setParentFramework(frm);
			
				System.out.println("prog.framework.id = " + prog.getParentFramework().getId() + ", name = " + prog.getParentFramework().getName());
				if (prog.getId() == null)
					prog = (EUProgramme) kbean.createRecord( prog );
				else
					prog = (EUProgramme) kbean.updateRecord(prog);

				if (prog == null) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Επιχειρησιακό Πρόγραμμα με Κωδικό " + code) );
					this.label = "";
				}
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο!") );
				this.label = "";
			}
		}
	}
	
	public String addDefaultEUProgramme() {
		EUProgramme prog = new EUProgramme();
		prog.setName("νέο Επιχειρησιακό Πρόγραμμα");
		this.defaultEUProgrammesList.add( prog );
			
		this.label = "EUProgrammesListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUProgramme() {
		EUProgramme tmp_pobj = (EUProgramme) this.tmp_dataTable2.getRowData();
        
		System.out.println( "removing EUProgramme object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUProgrammesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUProgramme.class, tmp_pobj.getId() );
        }

		this.label = "EUProgrammesListLabel";
		
		return "plaisio";	
	}
	
	public void saveDefaultEUPriorityAxesList() {
		this.label = "EUPriorityAxesListLabel";
		
		for( Iterator iter=this.defaultEUPriorityAxesList.iterator(); iter.hasNext(); ) {
			EUPriorityAxis axis = (EUPriorityAxis) iter.next();
			String code = axis.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUProgramme prog = (EUProgramme) kbean.getRecordById(axis.getParentProgramme().getId(), EUProgramme.class);
			if (prog != null) {
				axis.setParentProgramme(prog);
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο και Πρόγραμμα!") );
				axis.setParentProgramme(new EUProgramme());
				this.label = "";
			}
			System.out.println("axis.programme.id = " + axis.getParentProgramme().getId() + ", name = " + axis.getParentProgramme().getName());
			if (axis.getParentProgramme().getId() != null && axis.getParentProgramme().getName() != null) {
				if (axis.getId() == null )
					axis = (EUPriorityAxis) kbean.createRecord( axis );
				else
					axis = (EUPriorityAxis) kbean.updateRecord(axis);

				if (axis == null) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Άξονας Προτεραιότητας με Κωδικό " + code) );
					this.label = "";
				}
			}			
		}
	}
	
	public String addDefaultEUPriorityAxis() {
		EUPriorityAxis axis = new EUPriorityAxis();
		axis.setName("νέος Άξονας");
		this.defaultEUPriorityAxesList.add( axis );
		
		this.label = "EUPriorityAxesListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUPriorityAxis() {
		EUPriorityAxis tmp_pobj = (EUPriorityAxis) this.tmp_dataTable3.getRowData();
        
		System.out.println( "removing EUPriorityAxon object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUPriorityAxesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUPriorityAxis.class, tmp_pobj.getId() );
        }

		this.label = "EUPriorityAxesListLabel";
		
		return "plaisio";	
	}
	
	public void axis_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUPriorityAxis axis = (EUPriorityAxis) this.tmp_dataTable3.getRowData();
			EUFramework frm = (EUFramework) kbean.getRecordById(axis.getParentProgramme().getParentFramework().getId(), EUFramework.class);
			axis.getParentProgramme().setParentFramework(frm);
		}
		
		this.label = "EUPriorityAxesListLabel";
		
		return;
	}
	
	public void saveDefaultEUMeasuresList() {
		this.label = "EUMeasuresListLabel";
		
		for( Iterator iter=this.defaultEUMeasuresList.iterator(); iter.hasNext(); ) {
			EUMeasure measure = (EUMeasure) iter.next();
			String code = measure.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			System.out.println("measure.axis.id = " + measure.getParentAxis().getId() + ", name = " + measure.getParentAxis().getName());
			EUPriorityAxis axis = (EUPriorityAxis) kbean.getRecordById(measure.getParentAxis().getId(), EUPriorityAxis.class);
			if (axis != null) {
				measure.setParentAxis(axis);
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο, Πρόγραμμα και Άξονα!") );
				measure.setParentAxis(new EUPriorityAxis());
				this.label = "";
			}
			System.out.println("measure.axis.id = " + measure.getParentAxis().getId() + ", name = " + measure.getParentAxis().getName());
			if (measure.getParentAxis().getId() != null && measure.getParentAxis().getName() != null) {
				if (measure.getId() == null )
					measure = (EUMeasure) kbean.createRecord( measure );
				else
					measure = (EUMeasure) kbean.updateRecord(measure);
			}			
		}
	}
	
	public String addDefaultEUMeasure() {
		EUMeasure measure = new EUMeasure();
		measure.setName("νέο Μέτρο");
		this.defaultEUMeasuresList.add( measure );
		
		this.label = "EUMeasuresListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUMeasure() {
		EUMeasure tmp_pobj = (EUMeasure) this.tmp_dataTable4.getRowData();
        
		System.out.println( "removing EUMeasure object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUMeasuresList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUMeasure.class, tmp_pobj.getId() );
        }

		this.label = "EUMeasuresListLabel";
		
		return "plaisio";	
	}
	
	public void measure_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUMeasure measure = (EUMeasure) this.tmp_dataTable4.getRowData();
			EUFramework frm = (EUFramework) kbean.getRecordById(measure.getParentAxis().getParentProgramme().getParentFramework().getId(), EUFramework.class);
			if (frm != null)
				measure.getParentAxis().getParentProgramme().setParentFramework(frm);
			System.out.println("measure: " + measure.getName());
			System.out.println("measure.axis.name: " + measure.getParentAxis().getName());
			System.out.println("measure.axis.measuresSelectItems: " + measure.getParentAxis().getMeasuresSelectItemsList());
			System.out.println("measure.axis.programme.name: " + measure.getParentAxis().getParentProgramme().getName());
			System.out.println("measure.axis.programme.axesSelectItems: " + measure.getParentAxis().getParentProgramme().getAxesSelectItemsList());
			System.out.println("measure.axis.programme.framework.name: " + measure.getParentAxis().getParentProgramme().getParentFramework().getName());
			System.out.println("measure.axis.programme.framework.programmesSelectItems: " + measure.getParentAxis().getParentProgramme().getParentFramework().getProgrammesSelectItemsList());
		}
		
		this.label = "EUMeasuresListLabel";
		
		return;
	}
	
	public void measure_changedProgramme(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUMeasure measure = (EUMeasure) this.tmp_dataTable4.getRowData();
			EUProgramme prog = (EUProgramme) kbean.getRecordById(measure.getParentAxis().getParentProgramme().getId(), EUProgramme.class);
			System.out.println("prog.id = " + measure.getParentAxis().getParentProgramme().getId() + ", prog = " + prog);
			if (prog != null)
				measure.getParentAxis().setParentProgramme(prog);
			System.out.println("measure: " + measure.getName());
			System.out.println("measure.axis.name: " + measure.getParentAxis().getName());
			System.out.println("measure.axis.measuresSelectItems: " + measure.getParentAxis().getMeasuresSelectItemsList());
			System.out.println("measure.axis.programme.name: " + measure.getParentAxis().getParentProgramme().getName());
			System.out.println("measure.axis.programme.axesSelectItems: " + measure.getParentAxis().getParentProgramme().getAxesSelectItemsList());
			System.out.println("measure.axis.programme.framework.name: " + measure.getParentAxis().getParentProgramme().getParentFramework().getName());
			System.out.println("measure.axis.programme.framework.programmesSelectItems: " + measure.getParentAxis().getParentProgramme().getParentFramework().getProgrammesSelectItemsList());
		}
		
		this.label = "EUMeasuresListLabel";
		
		return;
	}
	
	public void saveDefaultEUSubMeasuresList() {
		this.label = "EUSubMeasuresListLabel";
		
		for( Iterator iter=this.defaultEUSubMeasuresList.iterator(); iter.hasNext(); ) {
			EUSubmeasure submeasure = (EUSubmeasure) iter.next();
			String code = submeasure.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUMeasure measure = (EUMeasure) kbean.getRecordById(submeasure.getParentMeasure().getId(), EUMeasure.class);
			if (measure != null) {
				submeasure.setParentMeasure(measure);
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο, Πρόγραμμα, Άξονα και Μέτρο!") );
				submeasure.setParentMeasure(new EUMeasure());
				this.label = "";
			}
			
			System.out.println("measure.programme.id = " + submeasure.getParentMeasure().getId() + ", name = " + submeasure.getParentMeasure().getName());
			if (submeasure.getParentMeasure().getId() != null && submeasure.getParentMeasure().getName() != null) {
				if (submeasure.getId() == null )
					submeasure = (EUSubmeasure) kbean.createRecord( submeasure );
				else
					submeasure = (EUSubmeasure) kbean.updateRecord(submeasure);
			}			
		}
	}
	
	public String addDefaultEUSubMeasure() {
		EUSubmeasure submeasure = new EUSubmeasure();
		submeasure.setName("νέο Υπομέτρο");
		this.defaultEUSubMeasuresList.add( submeasure );
		
		this.label = "EUSubMeasuresListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUSubMeasure() {
		EUSubmeasure tmp_pobj = (EUSubmeasure) this.tmp_dataTable5.getRowData();
        
		System.out.println( "removing EUSubmeasure object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUSubMeasuresList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUSubmeasure.class, tmp_pobj.getId() );
        }

		this.label = "EUSubMeasuresListLabel";
		
		return "plaisio";	
	}
	
	public void subMeasure_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUSubmeasure submeasure = (EUSubmeasure) this.tmp_dataTable5.getRowData();
			EUFramework frm = (EUFramework) kbean.getRecordById(submeasure.getParentMeasure().getParentAxis().getParentProgramme().getParentFramework().getId(), EUFramework.class);
			if (frm != null)
				submeasure.getParentMeasure().getParentAxis().getParentProgramme().setParentFramework(frm);
		}
		
		this.label = "EUSubMeasuresListLabel";
		
		return;
	}
	
	public void subMeasure_changedProgramme(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUSubmeasure submeasure = (EUSubmeasure) this.tmp_dataTable5.getRowData();
			EUProgramme prog = (EUProgramme) kbean.getRecordById(submeasure.getParentMeasure().getParentAxis().getParentProgramme().getId(), EUProgramme.class);
			System.out.println("prog.id = " + submeasure.getParentMeasure().getParentAxis().getParentProgramme().getId() + ", prog = " + prog);
			if (prog != null)
				submeasure.getParentMeasure().getParentAxis().setParentProgramme(prog);
		}
		
		this.label = "EUSubMeasuresListLabel";
		
		return;
	}
	
	public void subMeasure_changedAxis(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUSubmeasure submeasure = (EUSubmeasure) this.tmp_dataTable5.getRowData();
			EUPriorityAxis axis = (EUPriorityAxis) kbean.getRecordById(submeasure.getParentMeasure().getParentAxis().getId(), EUPriorityAxis.class);
			System.out.println("axis.id = " + submeasure.getParentMeasure().getParentAxis().getId() + ", axis = " + axis);
			if (axis != null)
				submeasure.getParentMeasure().setParentAxis(axis);
		}
		
		this.label = "EUSubMeasuresListLabel";
		
		return;
	}
	
	public void saveDefaultEUFPSSectionsList() {
		this.label = "EUFPSSectionListLabel";
		
		for( Iterator iter=this.defaultEUSectionsList.iterator(); iter.hasNext(); ) {
			EUFPSSection section = (EUFPSSection) iter.next();
			String code = section.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUFramework framework = (EUFramework) kbean.getRecordById(section.getParentFramework().getId(), EUFramework.class);
			if (framework != null) {
				section.setParentFramework(framework);
				System.out.println("section.submeasure.id = " + section.getParentFramework().getId() + ", name = " + section.getParentFramework().getName());
				if (framework.getId() != null && framework.getName() != null) {
					if (section.getId() == null )
						section = (EUFPSSection) kbean.createRecord( section );
					else
						section = (EUFPSSection) kbean.updateRecord(section);

					if (section == null) {
						FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Τομέας ΚΠΣ με Κωδικό " + code) );
						this.label = "";
					}
				}
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο!") );
				section.setParentFramework(new EUFramework());
				this.label = "";
			}
		}
	}
	
	public String addDefaultEUFPSSection() {
		EUFPSSection section = new EUFPSSection();
		section.setName("νέος Τομέας");
		this.defaultEUSectionsList.add( section );
		
		this.label = "EUFPSSectionListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUFPSSection() {
		EUFPSSection tmp_pobj = (EUFPSSection) this.tmp_dataTable6.getRowData();
        
		System.out.println( "removing EUFPSSection object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUSectionsList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUFPSSection.class, tmp_pobj.getId() );
        }

		this.label = "EUFPSSectionListLabel";
		
		return "plaisio";	
	}
	
	public void section_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUFPSSection section = (EUFPSSection) this.tmp_dataTable6.getRowData();
			EUFramework frm = (EUFramework) kbean.getRecordById(section.getParentFramework().getId(), EUFramework.class);
			if (frm != null)
				section.setParentFramework(frm);
		}
		
		this.label = "EUFPSSectionListLabel";
		
		return;
	}
	
	public void saveDefaultEUFPSSubSectionsList() {
		this.label = "EUFPSSubSectionListLabel";
		
		for( Iterator iter=this.defaultEUSubSectionsList.iterator(); iter.hasNext(); ) {
			EUFPSSubsection subsection = (EUFPSSubsection) iter.next();
			String code = subsection.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUFPSSection section = (EUFPSSection) kbean.getRecordById(subsection.getParentSection().getId(), EUFPSSection.class);
			if (section != null) {
				subsection.setParentSection(section);
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο και Τομέα ΚΠΣ!") );
				subsection.setParentSection(new EUFPSSection());
				this.label = "";
			}
			
			System.out.println("subsection.section.id = " + subsection.getParentSection().getId() + ", name = " + subsection.getParentSection().getName());
			if (subsection.getParentSection().getId() != null && subsection.getParentSection().getName() != null) {
				if (subsection.getId() == null )
					subsection = (EUFPSSubsection) kbean.createRecord( subsection );
				else
					subsection = (EUFPSSubsection) kbean.updateRecord(subsection);

				if (subsection == null) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Υποτομέας ΚΠΣ με Κωδικό " + code) );
					this.label = "";
				}
			}
		}
	}
	
	public String addDefaultEUFPSSubSection() {
		EUFPSSubsection subsection = new EUFPSSubsection();
		subsection.setName("νέος Yπότομέας");
		this.defaultEUSubSectionsList.add( subsection );
		
		this.label = "EUFPSSubSectionListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultEUFPSSubSection() {
		EUFPSSubsection tmp_pobj = (EUFPSSubsection) this.tmp_dataTable7.getRowData();
        
		System.out.println( "removing EUFPSSubsection object: " +tmp_pobj.getName() + ", " + tmp_pobj.getId() );
		this.defaultEUSubSectionsList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( EUFPSSubsection.class, tmp_pobj.getId() );
        }

		this.label = "EUFPSSubSectionListLabel";
		
		return "plaisio";	
	}
	
	public void subsection_changedFramework(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			EUFPSSubsection subsection = (EUFPSSubsection) this.tmp_dataTable7.getRowData();
			EUFramework frm = (EUFramework) kbean.getRecordById(subsection.getParentSection().getParentFramework().getId(), EUFramework.class);
			if (frm != null)
				subsection.getParentSection().setParentFramework(frm);
		}
		
		this.label = "EUFPSSubSectionListLabel";
		
		return;
	}
	
	
	/* ActionsCategory methods */
	public void saveDefaultActionsCategoriesList() {
		this.label = "ActionCategoriesListLabel";
		
		for( Iterator iter=this.defaultActionsCategoriesList.iterator(); iter.hasNext(); ) {
			ActionsCategory category = (ActionsCategory) iter.next();
			String code = category.getCode();
			if (code.compareTo("") == 0) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό!") );
				continue;
			}
			
			EUFramework framework = (EUFramework) kbean.getRecordById(category.getParentFramework().getId(), EUFramework.class);
			if (framework != null) {
				category.setParentFramework(framework);
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε Κοινοτικό Πλαίσιο!") );
				category.setParentFramework(new EUFramework());
				this.label = "";
			}
			
			System.out.println("category.programme.id = " + category.getParentFramework().getId() + ", name = " + category.getParentFramework().getName());
			if (category.getParentFramework().getId() != null && category.getParentFramework().getName() != null) {
				if (category.getId() == null )
					category = (ActionsCategory) kbean.createRecord( category );
				else
					category = (ActionsCategory) kbean.updateRecord(category);

				if (category == null) {
					FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Υπάρχει ήδη Κατηγορίας Ενέργειας με Κωδικό " + code) );
					this.label = "";
				}
			}
		}
	}
	
	public String addDefaultActionsCategory() {
		ActionsCategory cat = new ActionsCategory();
		cat.setCategory("νέα κατηγορία");
		this.defaultActionsCategoriesList.add( cat );
		
		this.label = "ActionCategoriesListLabel";
		
		return "plaisio";
	}
	
	public String deleteDefaultActionsCategory() {
		ActionsCategory tmp_pobj = (ActionsCategory) this.tmp_dataTable8.getRowData();
        
		System.out.println( "removing ActionsCategory object: " +tmp_pobj.getCategory() + ", " + tmp_pobj.getId() );
		this.defaultActionsCategoriesList.remove(tmp_pobj);
		
		if (tmp_pobj.getId() != null) {
   			// now remove the object
       		kbean.deleteRecord( ActionsCategory.class, tmp_pobj.getId() );
        }

		return "plaisio";	
	}
	
	/**
	 * @return the defaultFundersList
	 */
	public List<TDE_Funders> getDefaultFundersList() {
		return defaultFundersList;
	}

	/**
	 * @param defaultFundersList the defaultFundersList to set
	 */
	public void setDefaultFundersList(List<TDE_Funders> defaultFundersList) {
		this.defaultFundersList = defaultFundersList;
	}

	/**
	 * @return the defaultVendorsList
	 */
	public List<TDE_Vendors> getDefaultVendorsList() {
		return defaultVendorsList;
	}

	/**
	 * @param defaultVendorsList the defaultVendorsList to set
	 */
	public void setDefaultVendorsList(List<TDE_Vendors> defaultVendorsList) {
		this.defaultVendorsList = defaultVendorsList;
	}
	
	/**
	 * @return the defaultDeiktesList
	 */
	public List<TDE_Deiktes> getDefaultDeiktesList() {
		return defaultDeiktesList;
	}


	/**
	 * @param defaultDeiktesList the defaultDeiktesList to set
	 */
	public void setDefaultDeiktesList(List<TDE_Deiktes> defaultDeiktesList) {
		this.defaultDeiktesList = defaultDeiktesList;
	}

	/**
	 * @return the defaultEvolutionList
	 */
	public List<TDY_Evolution> getDefaultEvolutionList() {
		return defaultEvolutionList;
	}

	/**
	 * @param defaultEvolutionList the defaultEvolutionList to set
	 */
	public void setDefaultEvolutionList(List<TDY_Evolution> defaultEvolutionList) {
		this.defaultEvolutionList = defaultEvolutionList;
	}

	
	/**
	 * @return the defaultProgressList
	 */
	public List<TDY_Progress> getDefaultProgressList() {
		return defaultProgressList;
	}

	/**
	 * @param defaultProgressList the defaultProgressList to set
	 */
	public void setDefaultProgressList(List<TDY_Progress> defaultProgressList) {
		this.defaultProgressList = defaultProgressList;
	}

	/**
	 * @return the defaultExpensesList
	 */
	public List<TDY_Financial> getDefaultExpensesList() {
		return defaultExpensesList;
	}

	/**
	 * @param defaultExpensesList the defaultExpensesList to set
	 */
	public void setDefaultExpensesList(List<TDY_Financial> defaultExpensesList) {
		this.defaultExpensesList = defaultExpensesList;
	}

	/**
	 * @return the defaultInvoiceTypesList
	 */
	public List<InvoiceType> getDefaultInvoiceTypesList() {
		return defaultInvoiceTypesList;
	}

	/**
	 * @param defaultInvoiceTypesList the defaultInvoiceTypesList to set
	 */
	public void setDefaultInvoiceTypesList(List<InvoiceType> defaultInvoiceTypesList) {
		this.defaultInvoiceTypesList = defaultInvoiceTypesList;
	}

	/**
	 * @return the defaultEUFrameworksList
	 */
	public List<EUFramework> getDefaultEUFrameworksList() {
		return defaultEUFrameworksList;
	}

	/**
	 * @param defaultEUFrameworksList the defaultEUFrameworksList to set
	 */
	public void setDefaultEUFrameworksList(List<EUFramework> defaultEUFrameworksList) {
		this.defaultEUFrameworksList = defaultEUFrameworksList;
	}

	
	/**
	 * @return the defaultEUProgrammesList
	 */
	public List<EUProgramme> getDefaultEUProgrammesList() {
		return defaultEUProgrammesList;
	}

	/**
	 * @param defaultEUProgrammesList the defaultEUProgrammesList to set
	 */
	public void setDefaultEUProgrammesList(List<EUProgramme> defaultEUProgrammesList) {
		this.defaultEUProgrammesList = defaultEUProgrammesList;
	}

	/**
	 * @return the defaultActionsCategoriesList
	 */
	public List<ActionsCategory> getDefaultActionsCategoriesList() {
		return defaultActionsCategoriesList;
	}

	/**
	 * @param defaultActionsCategoriesList the defaultActionsCategoriesList to set
	 */
	public void setDefaultActionsCategoriesList(
			List<ActionsCategory> defaultActionsCategoriesList) {
		this.defaultActionsCategoriesList = defaultActionsCategoriesList;
	}

	/**
	 * @return the defaultEUFrameworksSelectItemsList
	 */
	public ArrayList<SelectItem> getDefaultEUFrameworksSelectItemsList() {
		return defaultEUFrameworksSelectItemsList;
	}

	/**
	 * @param defaultEUFrameworksSelectItemsList the defaultEUFrameworksSelectItemsList to set
	 */
	public void setDefaultEUFrameworksSelectItemsList(
			ArrayList<SelectItem> defaultEUFrameworksSelectItemsList) {
		this.defaultEUFrameworksSelectItemsList = defaultEUFrameworksSelectItemsList;
	}

	/**
	 * @return the defaultEUPriorityAxesList
	 */
	public List<EUPriorityAxis> getDefaultEUPriorityAxesList() {
		return defaultEUPriorityAxesList;
	}

	/**
	 * @param defaultEUPriorityAxesList the defaultEUPriorityAxesList to set
	 */
	public void setDefaultEUPriorityAxesList(
			List<EUPriorityAxis> defaultEUPriorityAxesList) {
		this.defaultEUPriorityAxesList = defaultEUPriorityAxesList;
	}

	/**
	 * @return the defaultEUProgrammesSelectItemsList
	 */
	public ArrayList<SelectItem> getDefaultEUProgrammesSelectItemsList() {
		return defaultEUProgrammesSelectItemsList;
	}

	/**
	 * @param defaultEUProgrammesSelectItemsList the defaultEUProgrammesSelectItemsList to set
	 */
	public void setDefaultEUProgrammesSelectItemsList(
			ArrayList<SelectItem> defaultEUProgrammesSelectItemsList) {
		this.defaultEUProgrammesSelectItemsList = defaultEUProgrammesSelectItemsList;
	}

	/**
	 * @return the defaultEUPriorityAxesSelectItemsList
	 */
	public ArrayList<SelectItem> getDefaultEUPriorityAxesSelectItemsList() {
		return defaultEUPriorityAxesSelectItemsList;
	}

	/**
	 * @param defaultEUPriorityAxesSelectItemsList the defaultEUPriorityAxesSelectItemsList to set
	 */
	public void setDefaultEUPriorityAxesSelectItemsList(
			ArrayList<SelectItem> defaultEUPriorityAxesSelectItemsList) {
		this.defaultEUPriorityAxesSelectItemsList = defaultEUPriorityAxesSelectItemsList;
	}


	/**
	 * @return the defaultSubProjectTypesSelectItemsList
	 */
	public ArrayList<SelectItem> getDefaultSubProjectTypesSelectItemsList() {
		return defaultSubProjectTypesSelectItemsList;
	}


	/**
	 * @param defaultSubProjectTypesSelectItemsList the defaultSubProjectTypesSelectItemsList to set
	 */
	public void setDefaultSubProjectTypesSelectItemsList(
			ArrayList<SelectItem> defaultSubProjectTypesSelectItemsList) {
		this.defaultSubProjectTypesSelectItemsList = defaultSubProjectTypesSelectItemsList;
	}


	/**
	 * @return the defaultActionsCategoriesSelectItemsList
	 */
	public ArrayList<SelectItem> getDefaultActionsCategoriesSelectItemsList() {
		return defaultActionsCategoriesSelectItemsList;
	}


	/**
	 * @param defaultActionsCategoriesSelectItemsList the defaultActionsCategoriesSelectItemsList to set
	 */
	public void setDefaultActionsCategoriesSelectItemsList(
			ArrayList<SelectItem> defaultActionsCategoriesSelectItemsList) {
		this.defaultActionsCategoriesSelectItemsList = defaultActionsCategoriesSelectItemsList;
	}


	/**
	 * @return the defaultEUMeasuresList
	 */
	public List<EUMeasure> getDefaultEUMeasuresList() {
		return defaultEUMeasuresList;
	}


	/**
	 * @param defaultEUMeasuresList the defaultEUMeasuresList to set
	 */
	public void setDefaultEUMeasuresList(List<EUMeasure> defaultEUMeasuresList) {
		this.defaultEUMeasuresList = defaultEUMeasuresList;
	}


	/**
	 * @return the defaultEUSubMeasuresList
	 */
	public List<EUSubmeasure> getDefaultEUSubMeasuresList() {
		return defaultEUSubMeasuresList;
	}


	/**
	 * @param defaultEUSubMeasuresList the defaultEUSubMeasuresList to set
	 */
	public void setDefaultEUSubMeasuresList(
			List<EUSubmeasure> defaultEUSubMeasuresList) {
		this.defaultEUSubMeasuresList = defaultEUSubMeasuresList;
	}


	/**
	 * @return the defaultEUSectionsList
	 */
	public List<EUFPSSection> getDefaultEUSectionsList() {
		return defaultEUSectionsList;
	}


	/**
	 * @param defaultEUSectionsList the defaultEUSectionsList to set
	 */
	public void setDefaultEUSectionsList(List<EUFPSSection> defaultEUSectionsList) {
		this.defaultEUSectionsList = defaultEUSectionsList;
	}


	/**
	 * @return the defaultEUSubSectionsList
	 */
	public List<EUFPSSubsection> getDefaultEUSubSectionsList() {
		return defaultEUSubSectionsList;
	}


	/**
	 * @param defaultEUSubSectionsList the defaultEUSubSectionsList to set
	 */
	public void setDefaultEUSubSectionsList(
			List<EUFPSSubsection> defaultEUSubSectionsList) {
		this.defaultEUSubSectionsList = defaultEUSubSectionsList;
	}
	
	/**
	 * @return the defaultSubProjectTypesList
	 */
	public List<SubProjectType> getDefaultSubProjectTypesList() {
		return defaultSubProjectTypesList;
	}


	/**
	 * @param defaultSubProjectTypesList the defaultSubProjectTypesList to set
	 */
	public void setDefaultSubProjectTypesList(
			List<SubProjectType> defaultSubProjectTypesList) {
		this.defaultSubProjectTypesList = defaultSubProjectTypesList;
	}


	/**
	 * @return the tmp_dataTable
	 */
	public HtmlDataTable getTmp_dataTable() {
		return tmp_dataTable;
	}

	/**
	 * @param tmp_dataTable the tmp_dataTable to set
	 */
	public void setTmp_dataTable(HtmlDataTable tmp_dataTable) {
		this.tmp_dataTable = tmp_dataTable;
	}

	/**
	 * @return the tmp_dataTable2
	 */
	public HtmlDataTable getTmp_dataTable2() {
		return tmp_dataTable2;
	}

	/**
	 * @param tmp_dataTable2 the tmp_dataTable2 to set
	 */
	public void setTmp_dataTable2(HtmlDataTable tmp_dataTable2) {
		this.tmp_dataTable2 = tmp_dataTable2;
	}

	/**
	 * @return the tmp_dataTable3
	 */
	public HtmlDataTable getTmp_dataTable3() {
		return tmp_dataTable3;
	}

	/**
	 * @param tmp_dataTable3 the tmp_dataTable3 to set
	 */
	public void setTmp_dataTable3(HtmlDataTable tmp_dataTable3) {
		this.tmp_dataTable3 = tmp_dataTable3;
	}


	/**
	 * @return the tmp_dataTable4
	 */
	public HtmlDataTable getTmp_dataTable4() {
		return tmp_dataTable4;
	}


	/**
	 * @param tmp_dataTable4 the tmp_dataTable4 to set
	 */
	public void setTmp_dataTable4(HtmlDataTable tmp_dataTable4) {
		this.tmp_dataTable4 = tmp_dataTable4;
	}


	/**
	 * @return the tmp_dataTable5
	 */
	public HtmlDataTable getTmp_dataTable5() {
		return tmp_dataTable5;
	}


	/**
	 * @param tmp_dataTable5 the tmp_dataTable5 to set
	 */
	public void setTmp_dataTable5(HtmlDataTable tmp_dataTable5) {
		this.tmp_dataTable5 = tmp_dataTable5;
	}


	/**
	 * @return the tmp_dataTable6
	 */
	public HtmlDataTable getTmp_dataTable6() {
		return tmp_dataTable6;
	}


	/**
	 * @param tmp_dataTable6 the tmp_dataTable6 to set
	 */
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
	 * @return the progressDataTable
	 */
	public HtmlDataTable getProgressDataTable() {
		return progressDataTable;
	}


	/**
	 * @param progressDataTable the progressDataTable to set
	 */
	public void setProgressDataTable(HtmlDataTable progressDataTable) {
		this.progressDataTable = progressDataTable;
	}


	/**
	 * @return the deiktesDataTable
	 */
	public HtmlDataTable getDeiktesDataTable() {
		return deiktesDataTable;
	}


	/**
	 * @param deiktesDataTable the deiktesDataTable to set
	 */
	public void setDeiktesDataTable(HtmlDataTable deiktesDataTable) {
		this.deiktesDataTable = deiktesDataTable;
	}


	public List getTestSubs() {
		return testSubs;
	}


	public void setTestSubs(List testSubs) {
		this.testSubs = testSubs;
	}
}
