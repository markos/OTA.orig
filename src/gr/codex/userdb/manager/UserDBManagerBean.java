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

/* $Id: UserDBManagerBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.userdb.manager;

import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.usermgmt.MailManager;
import gr.codex.usermgmt.UserManagementBeanLocal;
import gr.codex.usermgmt.entitybeans.Certification;
import gr.codex.usermgmt.entitybeans.Contractor;
import gr.codex.usermgmt.entitybeans.ContractorUser;
import gr.codex.usermgmt.entitybeans.Degree;
import gr.codex.usermgmt.entitybeans.ElectiveType;
import gr.codex.usermgmt.entitybeans.Municipality;
import gr.codex.usermgmt.entitybeans.Newsletter;
import gr.codex.usermgmt.entitybeans.Prefecture;
import gr.codex.usermgmt.entitybeans.ProfExperience;
import gr.codex.usermgmt.entitybeans.Region;
import gr.codex.usermgmt.entitybeans.SentStatus;
import gr.codex.usermgmt.entitybeans.Trainer;
import gr.codex.usermgmt.entitybeans.User;
import gr.codex.usermgmt.entitybeans.UserGroup;
import gr.codex.usermgmt.entitybeans.UserRole;
import gr.codex.usermgmt.entitybeans.UserRoleRequest;
import gr.codex.usermgmt.helpers.DegreeType;
import gr.codex.usermgmt.helpers.EducationField;
import gr.codex.usermgmt.helpers.Email;
import gr.codex.usermgmt.helpers.NoValidEmailException;
import gr.codex.usermgmt.helpers.ProfessionField;
import gr.codex.usermgmt.helpers.RequestType;
import gr.codex.usermgmt.helpers.RidiculousPasswordException;
import gr.codex.usermgmt.helpers.SexType;
import gr.codex.usermgmt.helpers.TalksExperience;
import gr.codex.usermgmt.helpers.TeachingExperience;
import gr.codex.usermgmt.helpers.UserPrivilegeType;
import gr.codex.usermgmt.helpers.UserStatus;
import gr.codex.usermgmt.helpers.UserType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class UserDBManagerBean {
	
	// variables
	private String username;
	private String password;
	private String email;
	
	private String tmpPassword;
	private String tmpPassword2;
	
	private Integer selectRegion_id;
	private String selectPrefecture_id;
	private String selectMunicipality_id;
	private String selectUserType;
	
	private String filterUserName;
	private String filterFirstName;
	private String filterLastName;
	
	private HtmlDataTable users_dataTable;
	private HtmlDataTable requests_dataTable;
	private HtmlDataTable groups_dataTable;
	private HtmlDataTable roles_dataTable;
	private HtmlDataTable privs_dataTable;
	private HtmlDataTable newsletter_dataTable;
	private HtmlDataTable degrees_dataTable;
	private HtmlDataTable profxp_dataTable;
	
	private UserGroup currentGroup;
	private UserRole currentRole;
	private User currentUser;
	private Trainer currentTrainer;
	private ContractorUser currentContractor;
	private Integer contractor_id;
	private Newsletter newsletter;
	private boolean form_is_user_edit; // true for edit, false for add (new user)
	
	private UserGroup defaultGroup_Elective;
	private UserGroup defaultGroup_Trainer;
	private UserGroup defaultGroup_ContractorUser;
	private UserGroup defaultGroup_AdministratorForum;
	private UserGroup defaultGroup_AdministratorKatartisis;
	private UserGroup defaultGroup_AdministratorTDE;
	private UserGroup defaultGroup_Administrator;
	private UserRole defaultRole_ContractorUser;
	private UserRole defaultRole_AdministratorForum;
	private UserRole defaultRole_AdministratorKatartisis;
	private UserRole defaultRole_AdministratorTDE;
	private UserRole defaultRole_Administrator;
	
	private int currentGroup_users_size;
	private int newUserApplications_size;
	private int newRoleRequests_size;
	private int allTotalUsers_size;
	private int allPlainUsers_size;
	private int allElectives_size;
	private int allTrainers_size;
	private int allContractors_size;
	private int allOTAOperators_size;
	private int allKatartisisOperators_size;
	private int allAdmins_size;
	
	private boolean allNewUsersCheckBox;
	private boolean allNewUserRoleRequestsCheckBox;
	private boolean allUsersInGroupCheckBox;
	private boolean allPrivsInRoleCheckBox;
	
	private Map<User, Boolean> newUserMultiSelect = new HashMap<User, Boolean>();
	private Map<UserRoleRequest, Boolean> newUserRoleRequestsMultiSelect = new HashMap<UserRoleRequest, Boolean>();
	private Map<User, Boolean> usersInGroupMultiSelect = new HashMap<User, Boolean>();
	private EnumMap<UserPrivilegeType, Boolean> privsInRoleMultiSelect = new EnumMap<UserPrivilegeType, Boolean>(UserPrivilegeType.class);
	
	private List<User> newUserApplications = new ArrayList<User>();
	private List<UserRoleRequest> newRoleRequests = new ArrayList<UserRoleRequest>();
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	private List<UserRole> userRoles = new ArrayList<UserRole>();
	private List<User> userList = new ArrayList<User>();
	private List<Newsletter> newslettersList = new ArrayList<Newsletter>();
	
	private List<UserPrivilegeType> defaultPrivilegeKeys = new ArrayList<UserPrivilegeType>();
	private List<UserGroup> selectedUserGroups = new ArrayList<UserGroup>();
	private List<Integer> selectedUserGroupIds = new ArrayList<Integer>();
	private List<UserRole> selectedUserRoles = new ArrayList<UserRole>();
	private List<Integer> selectedUserRoleIds = new ArrayList<Integer>();
	private List<Integer> requestedUserRoleIds = new ArrayList<Integer>();
	private List<Integer> regionAvailabilityIds = new ArrayList<Integer>();
	private List<String> certificationIds = new ArrayList<String>();
	
	private List<SelectItem> regionsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> prefectureSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> municipalitySelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> userGroupsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> userRolesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> sexSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> electiveTypesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> degreeTypesSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> degreeFieldsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> profxpFieldsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> talksExpListSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> teachingExpListSelectItems = new ArrayList<SelectItem>();
	private List<SelectItem> certificationsSelectItemsList = new ArrayList<SelectItem>();
	private List<SelectItem> contractorSelectItemsList = new ArrayList<SelectItem>();
	
	private MailManager mailmgr = new MailManager();
	Context ctx;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;
	UserLoginBean userLoginBean;

	public UserDBManagerBean() {
		try {
			ctx = new InitialContext();
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
		FacesContext fc = FacesContext.getCurrentInstance();
		userLoginBean = (UserLoginBean) fc.getApplication().createValueBinding("#{userLoginBean}").getValue(fc);
		
		clearAllDataTables();
		
		for (UserPrivilegeType t: UserPrivilegeType.values()) {
			this.defaultPrivilegeKeys.add(t);
			this.privsInRoleMultiSelect.put(t, false);
		}

		List<Region> regions = ubean.getAllRegions();
		for (Region r: regions) {
			this.regionsSelectItemsList.add(new SelectItem(r.getId(), r.getName()));
		}
		
		this.prefectureSelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
		this.municipalitySelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
		
		for (SexType s: SexType.values()) {
			this.sexSelectItemsList.add(new SelectItem(s.name(), s.toString()));
		}
		
		for (ElectiveType e: ElectiveType.values()) {
			this.electiveTypesSelectItemsList.add(new SelectItem(e.name(), e.toString()));
		}
		
		for (DegreeType d: DegreeType.values()) {
			this.degreeTypesSelectItemsList.add(new SelectItem(d.name(), d.toString()));
		}
		
		for (EducationField e: EducationField.values()) {
			this.degreeFieldsSelectItemsList.add(new SelectItem(e.name(), e.toString()));
		}
		
		for (ProfessionField p: ProfessionField.values()) {
			this.profxpFieldsSelectItemsList.add(new SelectItem(p.name(), p.toString()));
		}
		
		for (TalksExperience t: TalksExperience.values()) {
			this.talksExpListSelectItems.add(new SelectItem(t.name(), t.toString()));
		}
		
		for (TeachingExperience t: TeachingExperience.values()) {
			this.teachingExpListSelectItems.add(new SelectItem(t.name(), t.toString()));
		}
		
		List<Certification> certs = (List<Certification>) kbean.getAllRecordsOfType(Certification.class);
		for (Certification c: certs) {
			this.certificationsSelectItemsList.add(new SelectItem(c.getCertCode(), c.getCertCode() + ": " + c.getCertTitle()));
		}
		
		this.currentUser = new User();
	}

	public void clearAllDataTables() {
		this.users_dataTable = new HtmlDataTable();
		this.requests_dataTable = new HtmlDataTable();
		this.groups_dataTable = new HtmlDataTable();
		this.roles_dataTable = new HtmlDataTable();
		this.privs_dataTable = new HtmlDataTable();
		this.newsletter_dataTable = new HtmlDataTable();
		this.degrees_dataTable = new HtmlDataTable();
		this.profxp_dataTable = new HtmlDataTable();
	}
		
	public String gotoGroups() {
		clearAllDataTables();
		
		this.userGroups = ubean.getAllGroups();
		
		this.usersInGroupMultiSelect.clear();
		
		return "user-groups";
	}
	
	public String addUserGroup() {
		this.currentGroup = new UserGroup();
		this.currentGroup.setGroupname("νέα ομάδα");
		this.currentGroup.setType(UserType.User);
		this.userGroups.add(this.currentGroup);
		
		return "group-added";
	}
	
	public String saveUserGroups() {
		for (UserGroup g: this.userGroups) {
			if (g.getType() == UserType.User)
				kbean.updateRecord(g);
		}
		
		this.userGroups = ubean.getAllGroups();
		
		return "groups-saved";
	}
	
	public String deleteUserGroup() {
		UserGroup g = (UserGroup) this.groups_dataTable.getRowData();
		this.userGroups.remove(g);
		
		this.currentGroup = null;
		
		kbean.deleteRecord(UserGroup.class, g.getId());
		
		return "group-deleted";
	}
	
	public String changeCurrentGroup() {
		this.currentGroup = (UserGroup) this.groups_dataTable.getRowData();
		if (this.currentGroup.getUsers() != null) {
			for (User u: this.currentGroup.getUsers()) {
				this.usersInGroupMultiSelect.put(u, false);
			}
			this.currentGroup_users_size = this.currentGroup.getUsers().size();
		} else {
			this.currentGroup_users_size = 0;
		}
		
		return "group-changed";
	}
	
	public String gotoNewsletter() {
		clearAllDataTables();
		
		this.newsletter = new Newsletter();
		this.userGroups = ubean.getAllGroups();
		
		this.selectedUserGroups.clear();
		this.userGroupsSelectItemsList.clear();
		for (UserGroup g: this.userGroups) {
			this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname()));
		}
		
		this.newslettersList = ubean.getAllNewsletters();
		
		return "newsletters";
	}
	
	public boolean addNewsletter() {
		boolean willadd = true;
		this.selectedUserGroups.clear();
		for (Integer id: this.selectedUserGroupIds) {
			UserGroup g = (UserGroup) kbean.getRecordById(id, UserGroup.class);
			this.selectedUserGroups.add(g);
		}
		if (this.selectedUserGroups.size() == 0) {
			willadd = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε τουλάχιστον μία ομάδα!") );
		}
		if (this.newsletter.getSubject().length() == 0) {
			willadd = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε Θέμα!") );
		}
		if (this.newsletter.getSender().length() == 0) {
			willadd = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε Αποστολέα!") );
		}
		if (this.newsletter.getBody().length() == 0) {
			willadd = false;
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε Κείμενο!") );
		}
		if (willadd == false) {
			return false;
		}
		this.newsletter.setRecipientGroups(this.selectedUserGroups);
		this.newsletter.setDate_sent(new Date());
		this.newsletter.setStatus(SentStatus.STATUS_SAVED);
		this.newsletter = (Newsletter) kbean.createRecord(this.newsletter);
		return true;
	}
	
	public String deleteNewsletter() {
		Newsletter n = (Newsletter) this.newsletter_dataTable.getRowData();
		this.newslettersList.remove(n);
				
		kbean.deleteRecord(Newsletter.class, n.getId());
		
		return "role-deleted";
	}
	
	public String viewNewsletter() {
		this.newsletter = (Newsletter) this.newsletter_dataTable.getRowData();
		
		return "edit_newsletter";
	}
	
	public String sendNewsletter() {
		if (addNewsletter() == true) {
			try {
				this.newsletter.send(mailmgr);
				this.newsletter.setStatus(SentStatus.STATUS_SENT);
				this.newsletter = (Newsletter) kbean.updateRecord(this.newsletter);
			} catch (Exception e) {
				this.newsletter.setStatus(SentStatus.STATUS_FAILED);
				this.newsletter = (Newsletter) kbean.updateRecord(this.newsletter);
			}

			// refresh list and object
			this.newslettersList = ubean.getAllNewsletters();
			this.newsletter = new Newsletter();
		}
		return "newsletters";
	}
	
	public String saveDraftNewsletter() {
		if (addNewsletter() == true) {
			// refresh list and object
			this.newslettersList = ubean.getAllNewsletters();
			this.newsletter = new Newsletter();
		}
		return "newsletters";
	}
	
	public String gotoRoles() {
		clearAllDataTables();
		
		this.userRoles = ubean.getAllRoles();
		
		return "user-roles";
	}
	
	public String addUserRole() {
		this.currentRole = new UserRole();
		this.currentRole.setName("νέος ρόλος");
		this.userRoles.add(this.currentRole);
		
		return "role-added";
	}
	
	public String saveUserRoles() {
		for (UserRole r: this.userRoles) {
			kbean.updateRecord(r);
		}
		
		this.userRoles = ubean.getAllRoles();
		
		return "roles-saved";
	}
	
	public String deleteUserRole() {
		UserRole r = (UserRole) this.roles_dataTable.getRowData();
		this.userRoles.remove(r);
		
		kbean.deleteRecord(UserRole.class, r.getId());
		
		return "role-deleted";
	}
	
	public String changeCurrentRole() {
		this.currentRole = (UserRole) this.roles_dataTable.getRowData();
		this.privsInRoleMultiSelect = this.currentRole.getPrivileges();
		
		return "role-changed";
	}
	
	public String saveRolePrivileges() {
		this.currentRole.setPrivileges(this.privsInRoleMultiSelect);
		this.currentRole = (UserRole) kbean.updateRecord(this.currentRole);
		
		return "roleprives-saved";
	}
	
	public void reloadUserGroupSelectItems() {
		this.userGroupsSelectItemsList.clear();
		for (UserGroup g: this.userGroups) {
			this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname()));
		}
	}
	
	public void reloadUserRoleSelectItems() {
		this.userRolesSelectItemsList.clear();
		for (UserRole r: this.userRoles) {
			this.userRolesSelectItemsList.add(new SelectItem(r.getId(), r.getName()));
		}
	}
	
	public void gotoUsers_real() {
		clearAllDataTables();
		
		this.userGroups = ubean.getAllGroups();
		this.userRoles = ubean.getAllRoles();
		this.userList = new ArrayList<User>();
		
		reloadUserGroupSelectItems();
		reloadUserRoleSelectItems();
		
		for (UserGroup g: this.userGroups) {
			if (g.getType() == UserType.Elective) {
				this.defaultGroup_Elective = g; 
			} else if (g.getType() == UserType.Trainer) {
				this.defaultGroup_Trainer = g; 
			} else if (g.getType() == UserType.ContractorUser) {
				this.defaultGroup_ContractorUser = g; 
			} else if (g.getType() == UserType.AdministratorForum) {
				this.defaultGroup_AdministratorForum = g; 
			} else if (g.getType() == UserType.AdministratorKatartisis) {
				this.defaultGroup_AdministratorKatartisis = g; 
			} else if (g.getType() == UserType.AdministratorTDE) {
				this.defaultGroup_AdministratorTDE = g; 
			} else if (g.getType() == UserType.Administrator) {
				this.defaultGroup_Administrator = g; 
			}
		}
		
		for (UserRole g: this.userRoles) {
			if (g.getPrivilege(UserPrivilegeType.PRIV_ACCESS_KATARTISIS) == true) {
				this.defaultRole_ContractorUser = g;
			} else if (g.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_FORUM) == true) {
				this.defaultRole_AdministratorForum = g; 
			} else if (g.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_KATARTISIS) == true) {
				this.defaultRole_AdministratorKatartisis = g; 
			} else if (g.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_OTA_PROJECTS) == true) {
				this.defaultRole_AdministratorTDE = g; 
			} else if (g.getPrivilege(UserPrivilegeType.PRIV_ACCESS_MANAGE_USERS) == true) {
				this.defaultRole_Administrator = g; 
			}
		}
		
		this.selectedUserGroupIds.clear();
		this.selectedUserRoleIds.clear();
		this.selectedUserGroups.clear();
		this.selectedUserRoles.clear();
		this.regionAvailabilityIds.clear();
		this.contractorSelectItemsList.clear();
		
		List<Contractor> contractors = (List<Contractor>) kbean.getAllRecordsOfType(Contractor.class);
		for (Contractor c: contractors) {
			this.contractorSelectItemsList.add(new SelectItem(c.getId(), c.getName()));
		}
	}
	
	public String gotoUsers() {
		gotoUsers_real();
		
		return "users";
	}
	
	public void users_changedRegion(ValueChangeEvent event) throws AbortProcessingException {
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if (this.selectRegion_id.equals(-1) == false) {
				Region r = ubean.getRegionById(this.selectRegion_id);
				List<Prefecture> prefectures = r.getPrefectures();
				
				this.prefectureSelectItemsList.clear();
				this.prefectureSelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
				for( Prefecture p : prefectures) {
					this.prefectureSelectItemsList.add(new SelectItem(p.getId(), p.getName()));
				}
			} else {
				this.prefectureSelectItemsList.clear();
				this.prefectureSelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΝΟΜΟΙ"));
				this.municipalitySelectItemsList.clear();
				this.municipalitySelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
			}
		}
	}
	
	public void users_changedPrefecture(ValueChangeEvent event) throws AbortProcessingException {
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			List<Municipality> municipalities;
			if (this.selectPrefecture_id.equals("-1") == true) {
				municipalities = ubean.getAllMunicipalitiesByRegionId(this.selectRegion_id);
			} else {
				Prefecture p = ubean.getPrefectureById(this.selectPrefecture_id);
				municipalities = p.getMunicipalities();
			}
			this.municipalitySelectItemsList.clear();
			this.municipalitySelectItemsList.add(new SelectItem("-1", "ΟΛΟΙ ΟΙ ΟΤΑ"));
			for( Municipality m : municipalities) {
				this.municipalitySelectItemsList.add(new SelectItem(m.getId(), m.getName()));
			}
		}
	}
	
	public String searchUsers() {
		this.userList.clear();
		this.selectedUserGroups.clear();
		this.selectedUserRoles.clear();
		
		for (Integer id: this.selectedUserGroupIds) {
			UserGroup g = (UserGroup) kbean.getRecordById(id, UserGroup.class);
			this.selectedUserGroups.add(g);
		}
		for (Integer id: this.selectedUserRoleIds) {
			UserRole g = (UserRole) kbean.getRecordById(id, UserRole.class);
			this.selectedUserRoles.add(g);
		}
												
		if (this.selectRegion_id.equals(-1) == true &&
				this.selectedUserGroupIds.size() == 0 &&
				this.selectedUserRoleIds.size() == 0 &&
				this.filterUserName.length() == 0 &&
				this.filterFirstName.length() == 0 &&
				this.filterLastName.length() == 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε τουλάχιστον ένα φίλτρο όταν η χωροθέτηση αφορά όλη τη χώρα!") );
			return "users";
		}
		
		this.userList = ubean.getUsersByCriteria(this.filterUserName, this.filterFirstName, this.filterLastName,
				this.selectRegion_id, this.selectPrefecture_id, this.selectMunicipality_id,
				this.selectedUserGroups, this.selectedUserRoles);
		
		Collections.sort(this.userList);

		return "users-search";
	}
	
	public String getCleanUserCredentials() {
		this.username = "";
		this.email = "";
		this.tmpPassword = "";
		this.tmpPassword2 = "";
		this.form_is_user_edit = false;
		return "";
	}
	
	public String users_addUser() {
		getCleanUserCredentials();

		this.selectedUserGroups.clear();
		this.selectedUserRoles.clear();
		this.selectedUserGroupIds.clear();
		this.selectedUserRoleIds.clear();
		this.userGroupsSelectItemsList.clear();
		
		for (UserGroup g: this.userGroups) {
			if (g.getType() == UserType.User) {
				this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname()));
			} else {
				this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname(),g.getGroupname(), true, true));
			}
		}
		
		if (this.selectUserType.equals("add_plain_user")) {
			this.currentUser = new User();
			this.currentUser.setType(UserType.User);
			this.currentUser.setElectiveType(ElectiveType.NO_ELECTIVE);
			this.currentUser.setStatus(UserStatus.USER_APPROVED);

			return "edit_user";
		}
		if (this.selectUserType.equals("add_elective")) {
			this.currentUser = new User();
			this.currentUser.setType(UserType.Elective);
			this.currentUser.setElectiveType(ElectiveType.ELECTIVE_MUNICIPALITY_COUNCIL);
			this.currentUser.setStatus(UserStatus.USER_APPROVED);
			
			return "edit_user";
		}
		if (this.selectUserType.equals("add_trainer")) {
			this.currentTrainer = new Trainer();
			this.currentTrainer.setType(UserType.Trainer);
			this.currentTrainer.setStatus(UserStatus.USER_APPROVED);
			this.currentTrainer.setElectiveType(ElectiveType.NO_ELECTIVE);
			
			return "edit_trainer";
		}
		if (this.selectUserType.equals("add_contractor")) {
			this.currentContractor = new ContractorUser();
			this.currentContractor.setType(UserType.ContractorUser);
			this.currentContractor.setElectiveType(ElectiveType.NO_ELECTIVE);
			this.currentContractor.setStatus(UserStatus.USER_APPROVED);
			this.selectedUserRoleIds.add(this.defaultRole_ContractorUser.getId());

			return "edit_contractor";
		}
		if (this.selectUserType.equals("add_admin")) {
			this.currentUser = new User();
			this.currentUser.setType(UserType.Administrator);
			this.currentUser.setType(UserType.User);
			this.currentUser.setElectiveType(ElectiveType.NO_ELECTIVE);
			this.currentUser.setStatus(UserStatus.USER_APPROVED);

			return "edit_user";
		}
		
		// if we get here it's definitely because the user type was not selected
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να επιλέξετε τον τύπο του χρήστη!") );
		
		return "users";
	}
	
	public String deleteUser() {
		this.currentUser = (User) users_dataTable.getRowData();
		
		this.userList.remove(this.currentUser);
		ubean.deleteUser(this.currentUser.getUsername());
		
		return "users";
	}
	
	public String editAccount() {
		this.currentUser = ubean.findUserByUsername(userLoginBean.getUsername());
		
		gotoUsers_real();
		
		return editUser_real();
	}
	
	public String editUser() {
		this.currentUser = (User) users_dataTable.getRowData();
		
		return editUser_real();
	}
	
	public String editUser_real() {
		this.username = this.currentUser.getUsername();
		this.email = this.currentUser.getEmail();
		this.tmpPassword = "";
		this.tmpPassword2 = "";
		this.form_is_user_edit = true;
		
		this.userGroupsSelectItemsList.clear();
		for (UserGroup g: this.userGroups) {
			if (g.getType() == UserType.User) {
				this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname()));
			} else {
				this.userGroupsSelectItemsList.add(new SelectItem(g.getId(), g.getGroupname(),g.getGroupname(), true, true));
			}
		}
		
		for (UserGroup g: this.currentUser.getGroups()) {
			this.selectedUserGroupIds.add(g.getId());
		}
		
		for (UserRole g: this.currentUser.getRoles()) {
			this.selectedUserRoleIds.add(g.getId());
		}
		
		if (this.currentUser.getType() == UserType.User || this.currentUser.getType() == UserType.Elective 
				|| this.currentUser.getType() == UserType.Administrator) {
			return "edit_user";
		}
		if (this.currentUser.getType() == UserType.Trainer) {
			this.currentTrainer = (Trainer) this.currentUser;
			this.regionAvailabilityIds.clear();
			for (Region r: this.currentTrainer.getRegionAvailability()) {
				this.regionAvailabilityIds.add(r.getId());
			}
			
			this.certificationIds.clear();
			for (Certification c: this.currentTrainer.getCertifications()) {
				this.certificationIds.add(c.getCertCode());
			}
			
			return "edit_trainer";
		}
		if (this.currentUser.getType() == UserType.ContractorUser ) {
			this.currentContractor = (ContractorUser) this.currentUser;
			this.contractor_id = this.currentContractor.getCompany().getId();
			
			return "edit_contractor";
		}
		
		return "users";
	}
	
	public boolean validateUser(User u) {
		if (this.username.equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε όνομα χρήστη!") );
			return false;
		}
		if (u.getFirstName().equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε όνομα!") );
			return false;
		}
		if (u.getLastName().equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε επώνυμο!") );
			return false;
		}
		if (u.getBirthDate() != null && u.getBirthDate().after(new Date()) == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Δεν είναι δεκτή μελλοντική ημερομηνία γέννησης!") );
			return false;
		}
		if (this.email.equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε email!") );
			return false;
		} else {
			try {
				Email mail = new Email(this.email);
				u.setEmail(mail.getValue());
			} catch (NoValidEmailException e) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(e.getMessage()));
				return false;
			}
		}
		if (this.tmpPassword.equals("") == false || this.tmpPassword2.equals("") == false) {
			if (this.tmpPassword.equals(this.tmpPassword2) == false) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Οι κωδικοί δεν είναι ίδιοι!") );
				return false;
			}
			try {
				u.setPassword(this.tmpPassword);
			} catch (RidiculousPasswordException e) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(e.getMessage()));
				return false;
			}
		} else if (this.form_is_user_edit == false) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε κωδικό πρόσβασης!") );
			return false;
		}
		
		// Only if we are adding new users
		if (this.form_is_user_edit == false && ubean.findUserByUsername(this.username) != null) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το όνομα χρήστη " + this.username + " υπάρχει ήδη!") );
			return false;
		}
		
		return true;
	}
	
	public String saveUser() {
		
		// Validation checks
		if (validateUser(this.currentUser) == false) {
			return "save_user_error";
		}
		
		// username changed
		System.out.println("this.username =  " + this.username + ", currentUser.username = " + this.currentUser.getUsername());
		if (form_is_user_edit == true && this.currentUser.getUsername() != null && 
				this.username.equals(this.currentUser.getUsername()) == false) {
			String oldusername = this.currentUser.getUsername();

			this.currentUser.setUsername(this.username);
			System.out.println("Deleting user " + oldusername);
			ubean.deleteUser(oldusername);
			this.currentUser = (User) ubean.updateRecord(this.currentUser);
		} else {
			this.currentUser.setUsername(this.username);
		}
		
		for (Integer id: this.selectedUserGroupIds) {
			UserGroup g = (UserGroup) kbean.getRecordById(id, UserGroup.class);
			this.currentUser.addToGroup(g);
		}
		if (this.currentUser.getType() == UserType.Elective) {
			this.currentUser.addToGroup(this.defaultGroup_Elective);
		}
		for (Integer id: this.selectedUserRoleIds) {
			UserRole g = (UserRole) kbean.getRecordById(id, UserRole.class);
			this.currentUser.addToRole(g);
		}
		
		this.currentUser = (User) ubean.updateRecord(this.currentUser);
		
		reloadUserGroupSelectItems();
		
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Επιτυχής αποθήκευση") );
		return "users";
	}
	
	public String saveTrainer() {
		// Validation checks
		if (validateUser(this.currentTrainer) == false) {
			return "save_trainer_error";
		}
		
		// username changed
		System.out.println("this.username =  " + this.username + ", currentUser.username = " + this.currentTrainer.getUsername());
		if (form_is_user_edit == true && this.currentTrainer.getUsername() != null && 
				this.username.equals(this.currentTrainer.getUsername()) == false) {
			String oldusername = this.currentTrainer.getUsername();

			this.currentTrainer.setUsername(this.username);
			System.out.println("Deleting user " + oldusername);
			ubean.deleteUser(oldusername);
			this.currentTrainer = (Trainer) ubean.updateRecord(this.currentTrainer);
		} else {
			this.currentTrainer.setUsername(this.username);
		}
		
		for (Integer id: this.selectedUserGroupIds) {
			UserGroup g = (UserGroup) kbean.getRecordById(id, UserGroup.class);
			this.currentTrainer.addToGroup(g);
		}
		this.currentTrainer.addToGroup(this.defaultGroup_Trainer);
		
		for (Integer id: this.selectedUserRoleIds) {
			UserRole g = (UserRole) kbean.getRecordById(id, UserRole.class);
			this.currentTrainer.addToRole(g);
		}
		
		for (Integer rid: this.regionAvailabilityIds) {
			Region r = ubean.getRegionById(rid);
			this.currentTrainer.addToRegion(r);
		}
		
		for (String cid: this.certificationIds) {
			Certification c = ubean.getCertificationById(cid);
			this.currentTrainer.getCertifications().add(c);
		}
		
		this.currentTrainer = (Trainer) ubean.updateRecord(this.currentTrainer);
		
		reloadUserGroupSelectItems();
		
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Επιτυχής αποθήκευση") );
		return "users";
	}
	
	public String saveContractor() {
		// Validation checks
		if (validateUser(this.currentContractor) == false) {
			return "save_contractor_error";
		}
		
		// username changed
		System.out.println("this.username =  " + this.username + ", currentUser.username = " + this.currentContractor.getUsername());
		if (form_is_user_edit == true && this.currentContractor.getUsername() != null && 
				this.username.equals(this.currentContractor.getUsername()) == false) {
			String oldusername = this.currentContractor.getUsername();

			this.currentContractor.setUsername(this.username);
			System.out.println("Deleting user " + oldusername);
			ubean.deleteUser(oldusername);
			this.currentContractor = (ContractorUser) ubean.updateRecord(this.currentContractor);
		} else {
			this.currentContractor.setUsername(this.username);
		}
		
		for (Integer id: this.selectedUserGroupIds) {
			UserGroup g = (UserGroup) kbean.getRecordById(id, UserGroup.class);
			this.currentContractor.addToGroup(g);
		}
		this.currentContractor.addToGroup(this.defaultGroup_ContractorUser);
		
		for (Integer id: this.selectedUserRoleIds) {
			UserRole g = (UserRole) kbean.getRecordById(id, UserRole.class);
			this.currentContractor.addToRole(g);
		}		
		this.currentContractor.addToRole(this.defaultRole_ContractorUser);
		
		Contractor contractor = (Contractor) kbean.getRecordById(this.contractor_id, Contractor.class);
		this.currentContractor.setCompany(contractor);
		
		this.currentContractor = (ContractorUser) ubean.updateRecord(this.currentContractor);
		
		reloadUserGroupSelectItems();
		
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Επιτυχής αποθήκευση") );
		return "users";
	}
	
	public void trainer_AddDegree() {
		Degree d = new Degree();
		d.setTitle("εισάγετε τίτλο");
		d.setParentTrainer(this.currentTrainer);
		this.currentTrainer.addDegree(d);
		
	}
	
	public void trainer_DeleteDegree() {
		Degree d = (Degree) degrees_dataTable.getRowData();
		this.currentTrainer.removeDegree(d);
		
		kbean.deleteRecord(Degree.class, d.getId());
	}
	
	public void trainer_AddProfExperience() {
		ProfExperience xp = new ProfExperience();
		xp.setTitle("εισάγετε τίτλο");
		xp.setParentTrainer(this.currentTrainer);
		this.currentTrainer.addProfXP(xp);
	}
	
	public void trainer_DeleteProfExperience() {
		ProfExperience xp = (ProfExperience) this.profxp_dataTable.getRowData();
		this.currentTrainer.removeProfXP(xp);
		
		kbean.deleteRecord(ProfExperience.class, xp.getId());
	}
	
	public String gotoUsersMain() {
		clearAllDataTables();
		
		this.newUserApplications = ubean.getAllUsersByStatus(UserStatus.USER_PENDING_APPROVAL);
		this.newRoleRequests = (List<UserRoleRequest>) kbean.getAllRecordsOfType(UserRoleRequest.class);
		this.newRoleRequests_size = this.newRoleRequests.size();
		this.newUserApplications_size = this.newUserApplications.size();
		
		this.allAdmins_size = ubean.countUsersOfType(UserType.Administrator);
		this.allContractors_size = ubean.countUsersOfType(UserType.ContractorUser);
		this.allElectives_size = ubean.countUsersOfType(UserType.Elective);
		this.allTrainers_size = ubean.countUsersOfType(UserType.Trainer);
		this.allPlainUsers_size = ubean.countUsersOfType(UserType.User);
		this.allTotalUsers_size = ubean.countAllUsers();
		
		this.newUserMultiSelect.clear();
		for (User u: this.newUserApplications) {
			this.newUserMultiSelect.put(u, false);
		}
		
		for (UserRoleRequest u: this.newRoleRequests) {
			this.newUserRoleRequestsMultiSelect.put(u, false);
		}
		
		return "users-main";
	}
	
	public void selectAllNewUsers(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			for (User u: this.newUserApplications) {
				this.newUserMultiSelect.put(u, this.allNewUsersCheckBox);
			}
		}
		
		return;
	}
	
	public String approveUsers() {
		List<User> doneUsers = new ArrayList<User>();
		for (Map.Entry<User, Boolean> e :this.newUserMultiSelect.entrySet()) {
			if (e.getValue() == true) {
				User u = e.getKey();
				u.setStatus(UserStatus.USER_APPROVED);
				doneUsers.add(u);
				this.newUserApplications.remove(u);
			}
			this.newUserApplications_size = this.newUserApplications.size();
		}
		
		ubean.updateUserList(doneUsers);
		
		for (User u: doneUsers)
			this.newUserMultiSelect.remove(u);
		
		this.allNewUsersCheckBox = false;
		
		return "users-approved";
	}
	
	public String rejectUsers() {
		List<User> doneUsers = new ArrayList<User>();
		for (Map.Entry<User, Boolean> e :this.newUserMultiSelect.entrySet()) {
			if (e.getValue() == true) {
				User u = e.getKey();
				u.setStatus(UserStatus.USER_DELETED);
				doneUsers.add(u);
				this.newUserApplications.remove(u);
			}
			this.newUserApplications_size = this.newUserApplications.size();
		}
		
		ubean.updateUserList(doneUsers);
		
		for (User u: doneUsers)
			this.newUserMultiSelect.remove(u);
		
		this.allNewUsersCheckBox = false;
		
		return "users-rejected";
	}
	
	public void selectAllNewUserRoleRequests(ValueChangeEvent event) throws AbortProcessingException{
		/* These need to get in here, according to
		 * http://java-server-faces.blogspot.com/2006/04/valuechangelisteners-what-you-need-to.html
		 * due to design limitations in JSF ValueChangeEvent
		 */
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			for (UserRoleRequest u: this.newRoleRequests) {
				this.newUserRoleRequestsMultiSelect.put(u, this.allNewUserRoleRequestsCheckBox);
			}
		}
		
		return;
	}
	
	public String approveRoleRequests() {
		for (UserRoleRequest roleRequest :this.newRoleRequests) {
			if (this.newUserRoleRequestsMultiSelect.get(roleRequest) == true) {
				roleRequest.getUser().addToRole(roleRequest.getRole());
				kbean.updateRecord(roleRequest.getUser());
				roleRequest.setType(RequestType.APPROVED);
				kbean.updateRecord(roleRequest);
			}
		}
		return "roles-approved";
	}
	
	public String rejectRoleRequests() {
		for (UserRoleRequest roleRequest :this.newRoleRequests) {
			if (this.newUserRoleRequestsMultiSelect.get(roleRequest) == true) {
				roleRequest.setType(RequestType.REJECTED);
				kbean.updateRecord(roleRequest);
			}
		}
		return "roles-rejected";
	}
	
	public void requestRoles() {
		for (Integer rid: this.requestedUserRoleIds) {
			UserRole r = (UserRole) kbean.getRecordById(rid, UserRole.class);
			UserRoleRequest req = new UserRoleRequest();
			req.setUser(this.currentUser);
			req.setType(RequestType.PENDING);
			req.setRole(r);
			req = (UserRoleRequest) kbean.createRecord(req);
		}
	}
	
	public String recoverPassword() {
	
		try {
			User p = ubean.findUserByUsername(username);
			
			if (p == null) {
				return "error-recover";
			}
			
			String newpasswd = p.generateRandomPassword();
			p.setPassword(newpasswd);
			ubean.updateRecord(p);
			// Send an email with the new Password
			mailmgr.sendNewPassword(p.getEmail(), newpasswd);
			return "pwdrecovery_ok";
		} catch (RidiculousPasswordException e) {
			// We should not get here at all!
			e.printStackTrace();
			return "pwdrecovery_failure";
		}
	}
	
	public String registerNewUser() {
		try {
			if (this.username.equals("") == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε όνομα χρήστη!") );
				return "register_failure";
			}
			if (this.currentUser.getFirstName().equals("") == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε όνομα!") );
				return "register_failure";
			}
			if (this.currentUser.getLastName().equals("") == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε επώνυμο!") );
				return "register_failure";
			}
			
			// Validate the username, check if it already exists in the DB.
			User u = ubean.findUserByUsername(this.currentUser.getUsername());

			if ( u != null && u.getUsername().equals( this.currentUser.getUsername()) == true ) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Το όνομα χρήστη " + this.currentUser.getUsername() + " χρησιμοποιείται ήδη, παρακαλούμε επιλέξτε άλλο όνομα χρήστη.") );
				return "register_failure";
			}
			// Validate the password
			if (tmpPassword2.equals(tmpPassword))
				this.currentUser.setPassword(tmpPassword2);
			else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage( RidiculousPasswordException.PASSWORDS_DO_NOT_MATCH ) );
				return "register_failure";
			}
				
			// Validate the email
			this.currentUser.setEmail(email);
			
			this.currentUser.setType(UserType.User);
			this.currentUser.setElectiveType(ElectiveType.NO_ELECTIVE);
			this.currentUser.setStatus(UserStatus.USER_PENDING_APPROVAL);
			
			ubean.createRecord( this.currentUser );			
			
			return "register_ok";
		} catch (RidiculousPasswordException e) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage( e.getMessage() ) );
			return "register_failure";
		} catch (NoValidEmailException e) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage( e.getMessage() ) );
			return "register_failure";
		}	
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTmpPassword() {
		return tmpPassword;
	}

	public void setTmpPassword(String tmpPassword) {
		this.tmpPassword = tmpPassword;
	}

	public String getTmpPassword2() {
		return tmpPassword2;
	}

	public void setTmpPassword2(String tmpPassword2) {
		this.tmpPassword2 = tmpPassword2;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the users_dataTable
	 */
	public HtmlDataTable getUsers_dataTable() {
		return users_dataTable;
	}

	/**
	 * @param users_dataTable the users_dataTable to set
	 */
	public void setUsers_dataTable(HtmlDataTable users_dataTable) {
		this.users_dataTable = users_dataTable;
	}

	/**
	 * @return the requests_dataTable
	 */
	public HtmlDataTable getRequests_dataTable() {
		return requests_dataTable;
	}

	/**
	 * @param requests_dataTable the requests_dataTable to set
	 */
	public void setRequests_dataTable(HtmlDataTable requests_dataTable) {
		this.requests_dataTable = requests_dataTable;
	}

	/**
	 * @return the newUserApplications
	 */
	public List<User> getNewUserApplications() {
		return newUserApplications;
	}

	/**
	 * @param newUserApplications the newUserApplications to set
	 */
	public void setNewUserApplications(List<User> newUserApplications) {
		this.newUserApplications = newUserApplications;
	}

	/**
	 * @return the newRoleRequests
	 */
	public List<UserRoleRequest> getNewRoleRequests() {
		return newRoleRequests;
	}

	/**
	 * @param newRoleRequests the newRoleRequests to set
	 */
	public void setNewRoleRequests(List<UserRoleRequest> newRoleRequests) {
		this.newRoleRequests = newRoleRequests;
	}

	/**
	 * @return the allNewUsersCheckBox
	 */
	public boolean isAllNewUsersCheckBox() {
		return allNewUsersCheckBox;
	}

	/**
	 * @param allNewUsersCheckBox the allNewUsersCheckBox to set
	 */
	public void setAllNewUsersCheckBox(boolean allNewUsersCheckBox) {
		this.allNewUsersCheckBox = allNewUsersCheckBox;
	}

	/**
	 * @return the newUserMultiSelect
	 */
	public Map<User, Boolean> getNewUserMultiSelect() {
		return newUserMultiSelect;
	}

	/**
	 * @param newUserMultiSelect the newUserMultiSelect to set
	 */
	public void setNewUserMultiSelect(Map<User, Boolean> newUserMultiSelect) {
		this.newUserMultiSelect = newUserMultiSelect;
	}

	/**
	 * @return the allNewUserRoleRequestsCheckBox
	 */
	public boolean isAllNewUserRoleRequestsCheckBox() {
		return allNewUserRoleRequestsCheckBox;
	}

	/**
	 * @param allNewUserRoleRequestsCheckBox the allNewUserRoleRequestsCheckBox to set
	 */
	public void setAllNewUserRoleRequestsCheckBox(
			boolean allNewUserRoleRequestsCheckBox) {
		this.allNewUserRoleRequestsCheckBox = allNewUserRoleRequestsCheckBox;
	}

	/**
	 * @return the newUserRoleRequestsMultiSelect
	 */
	public Map<UserRoleRequest, Boolean> getNewUserRoleRequestsMultiSelect() {
		return newUserRoleRequestsMultiSelect;
	}

	/**
	 * @param newUserRoleRequestsMultiSelect the newUserRoleRequestsMultiSelect to set
	 */
	public void setNewUserRoleRequestsMultiSelect(
			Map<UserRoleRequest, Boolean> newUserRoleRequestsMultiSelect) {
		this.newUserRoleRequestsMultiSelect = newUserRoleRequestsMultiSelect;
	}

	/**
	 * @return the newUserApplications_size
	 */
	public int getNewUserApplications_size() {
		return newUserApplications_size;
	}

	/**
	 * @param newUserApplications_size the newUserApplications_size to set
	 */
	public void setNewUserApplications_size(int newUserApplications_size) {
		this.newUserApplications_size = newUserApplications_size;
	}

	/**
	 * @return the newRoleRequests_size
	 */
	public int getNewRoleRequests_size() {
		return newRoleRequests_size;
	}

	/**
	 * @param newRoleRequests_size the newRoleRequests_size to set
	 */
	public void setNewRoleRequests_size(int newRoleRequests_size) {
		this.newRoleRequests_size = newRoleRequests_size;
	}

	/**
	 * @return the allTotalUsers_size
	 */
	public int getAllTotalUsers_size() {
		return allTotalUsers_size;
	}

	/**
	 * @param allTotalUsers_size the allTotalUsers_size to set
	 */
	public void setAllTotalUsers_size(int allTotalUsers_size) {
		this.allTotalUsers_size = allTotalUsers_size;
	}

	/**
	 * @return the allPlainUsers_size
	 */
	public int getAllPlainUsers_size() {
		return allPlainUsers_size;
	}

	/**
	 * @param allPlainUsers_size the allPlainUsers_size to set
	 */
	public void setAllPlainUsers_size(int allPlainUsers_size) {
		this.allPlainUsers_size = allPlainUsers_size;
	}

	/**
	 * @return the allElectives_size
	 */
	public int getAllElectives_size() {
		return allElectives_size;
	}

	/**
	 * @param allElectives_size the allElectives_size to set
	 */
	public void setAllElectives_size(int allElectives_size) {
		this.allElectives_size = allElectives_size;
	}

	/**
	 * @return the allTrainers_size
	 */
	public int getAllTrainers_size() {
		return allTrainers_size;
	}

	/**
	 * @param allTrainers_size the allTrainers_size to set
	 */
	public void setAllTrainers_size(int allTrainers_size) {
		this.allTrainers_size = allTrainers_size;
	}

	/**
	 * @return the allContractors_size
	 */
	public int getAllContractors_size() {
		return allContractors_size;
	}

	/**
	 * @param allContractors_size the allContractors_size to set
	 */
	public void setAllContractors_size(int allContractors_size) {
		this.allContractors_size = allContractors_size;
	}

	/**
	 * @return the allOTAOperators_size
	 */
	public int getAllOTAOperators_size() {
		return allOTAOperators_size;
	}

	/**
	 * @param allOTAOperators_size the allOTAOperators_size to set
	 */
	public void setAllOTAOperators_size(int allOTAOperators_size) {
		this.allOTAOperators_size = allOTAOperators_size;
	}

	/**
	 * @return the allKatartisisOperators_size
	 */
	public int getAllKatartisisOperators_size() {
		return allKatartisisOperators_size;
	}

	/**
	 * @param allKatartisisOperators_size the allKatartisisOperators_size to set
	 */
	public void setAllKatartisisOperators_size(int allKatartisisOperators_size) {
		this.allKatartisisOperators_size = allKatartisisOperators_size;
	}

	/**
	 * @return the allAdmins_size
	 */
	public int getAllAdmins_size() {
		return allAdmins_size;
	}

	/**
	 * @param allAdmins_size the allAdmins_size to set
	 */
	public void setAllAdmins_size(int allAdmins_size) {
		this.allAdmins_size = allAdmins_size;
	}

	/**
	 * @return the groups_dataTable
	 */
	public HtmlDataTable getGroups_dataTable() {
		return groups_dataTable;
	}

	/**
	 * @param groups_dataTable the groups_dataTable to set
	 */
	public void setGroups_dataTable(HtmlDataTable groups_dataTable) {
		this.groups_dataTable = groups_dataTable;
	}

	/**
	 * @return the userGroups
	 */
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	/**
	 * @param userGroups the userGroups to set
	 */
	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	/**
	 * @return the currentGroup
	 */
	public UserGroup getCurrentGroup() {
		return currentGroup;
	}

	/**
	 * @param currentGroup the currentGroup to set
	 */
	public void setCurrentGroup(UserGroup currentGroup) {
		this.currentGroup = currentGroup;
	}

	/**
	 * @return the allUsersInGroupCheckBox
	 */
	public boolean isAllUsersInGroupCheckBox() {
		return allUsersInGroupCheckBox;
	}

	/**
	 * @param allUsersInGroupCheckBox the allUsersInGroupCheckBox to set
	 */
	public void setAllUsersInGroupCheckBox(boolean allUsersInGroupCheckBox) {
		this.allUsersInGroupCheckBox = allUsersInGroupCheckBox;
	}

	/**
	 * @return the usersInGroupMultiSelect
	 */
	public Map<User, Boolean> getUsersInGroupMultiSelect() {
		return usersInGroupMultiSelect;
	}

	/**
	 * @param usersInGroupMultiSelect the usersInGroupMultiSelect to set
	 */
	public void setUsersInGroupMultiSelect(
			Map<User, Boolean> usersInGroupMultiSelect) {
		this.usersInGroupMultiSelect = usersInGroupMultiSelect;
	}

	/**
	 * @return the currentGroup_users_size
	 */
	public int getCurrentGroup_users_size() {
		return currentGroup_users_size;
	}

	/**
	 * @param currentGroup_users_size the currentGroup_users_size to set
	 */
	public void setCurrentGroup_users_size(int currentGroup_users_size) {
		this.currentGroup_users_size = currentGroup_users_size;
	}

	/**
	 * @return the roles_dataTable
	 */
	public HtmlDataTable getRoles_dataTable() {
		return roles_dataTable;
	}

	/**
	 * @param roles_dataTable the roles_dataTable to set
	 */
	public void setRoles_dataTable(HtmlDataTable roles_dataTable) {
		this.roles_dataTable = roles_dataTable;
	}

	/**
	 * @return the privs_dataTable
	 */
	public HtmlDataTable getPrivs_dataTable() {
		return privs_dataTable;
	}

	/**
	 * @param privs_dataTable the privs_dataTable to set
	 */
	public void setPrivs_dataTable(HtmlDataTable privs_dataTable) {
		this.privs_dataTable = privs_dataTable;
	}

	/**
	 * @return the userRoles
	 */
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return the currentRole
	 */
	public UserRole getCurrentRole() {
		return currentRole;
	}

	/**
	 * @param currentRole the currentRole to set
	 */
	public void setCurrentRole(UserRole currentRole) {
		this.currentRole = currentRole;
	}

	/**
	 * @return the allPrivsInRoleCheckBox
	 */
	public boolean isAllPrivsInRoleCheckBox() {
		return allPrivsInRoleCheckBox;
	}

	/**
	 * @param allPrivsInRoleCheckBox the allPrivsInRoleCheckBox to set
	 */
	public void setAllPrivsInRoleCheckBox(boolean allPrivsInRoleCheckBox) {
		this.allPrivsInRoleCheckBox = allPrivsInRoleCheckBox;
	}

	/**
	 * @return the privsInRoleMultiSelect
	 */
	public Map<UserPrivilegeType, Boolean> getPrivsInRoleMultiSelect() {
		return privsInRoleMultiSelect;
	}

	/**
	 * @param privsInRoleMultiSelect the privsInRoleMultiSelect to set
	 */
	public void setPrivsInRoleMultiSelect(
			EnumMap<UserPrivilegeType, Boolean> privsInRoleMultiSelect) {
		this.privsInRoleMultiSelect = privsInRoleMultiSelect;
	}

	/**
	 * @return the defaultPrivilegeKkeys
	 */
	public List<UserPrivilegeType> getDefaultPrivilegeKeys() {
		return defaultPrivilegeKeys;
	}

	/**
	 * @param defaultPrivilegeKkeys the defaultPrivilegeKkeys to set
	 */
	public void setDefaultPrivilegeKkeys(
			List<UserPrivilegeType> defaultPrivilegeKeys) {
		this.defaultPrivilegeKeys = defaultPrivilegeKeys;
	}

	/**
	 * @return the userList
	 */
	public List<User> getUserList() {
		return userList;
	}

	/**
	 * @param userList the userList to set
	 */
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the newslettersList
	 */
	public List<Newsletter> getNewslettersList() {
		return newslettersList;
	}

	/**
	 * @param newslettersList the newslettersList to set
	 */
	public void setNewslettersList(List<Newsletter> newslettersList) {
		this.newslettersList = newslettersList;
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
	 * @return the regionsSelectItemsList
	 */
	public List<SelectItem> getRegionsSelectItemsList() {
		return regionsSelectItemsList;
	}

	/**
	 * @param regionsSelectItemsList the regionsSelectItemsList to set
	 */
	public void setRegionsSelectItemsList(List<SelectItem> regionsSelectItemsList) {
		this.regionsSelectItemsList = regionsSelectItemsList;
	}

	/**
	 * @return the prefectureSelectItemsList
	 */
	public List<SelectItem> getPrefectureSelectItemsList() {
		return prefectureSelectItemsList;
	}

	/**
	 * @param prefectureSelectItemsList the prefectureSelectItemsList to set
	 */
	public void setPrefectureSelectItemsList(
			List<SelectItem> prefectureSelectItemsList) {
		this.prefectureSelectItemsList = prefectureSelectItemsList;
	}

	/**
	 * @return the municipalitySelectItemsList
	 */
	public List<SelectItem> getMunicipalitySelectItemsList() {
		return municipalitySelectItemsList;
	}

	/**
	 * @param municipalitySelectItemsList the municipalitySelectItemsList to set
	 */
	public void setMunicipalitySelectItemsList(
			List<SelectItem> municipalitySelectItemsList) {
		this.municipalitySelectItemsList = municipalitySelectItemsList;
	}

	/**
	 * @return the selectedUserGroups
	 */
	public List<UserGroup> getSelectedUserGroups() {
		return selectedUserGroups;
	}

	/**
	 * @param selectedUserGroups the selectedUserGroups to set
	 */
	public void setSelectedUserGroups(List<UserGroup> selectedUserGroups) {
		this.selectedUserGroups = selectedUserGroups;
	}

	/**
	 * @return the selectedUserRoles
	 */
	public List<UserRole> getSelectedUserRoles() {
		return selectedUserRoles;
	}

	/**
	 * @param selectedUserRoles the selectedUserRoles to set
	 */
	public void setSelectedUserRoles(List<UserRole> selectedUserRoles) {
		this.selectedUserRoles = selectedUserRoles;
	}

	/**
	 * @return the selectedUserGroupIds
	 */
	public List<Integer> getSelectedUserGroupIds() {
		return selectedUserGroupIds;
	}

	/**
	 * @param selectedUserGroupIds the selectedUserGroupIds to set
	 */
	public void setSelectedUserGroupIds(List<Integer> selectedUserGroupIds) {
		this.selectedUserGroupIds = selectedUserGroupIds;
	}

	/**
	 * @return the selectedUserRoleIds
	 */
	public List<Integer> getSelectedUserRoleIds() {
		return selectedUserRoleIds;
	}

	/**
	 * @param selectedUserRoleIds the selectedUserRoleIds to set
	 */
	public void setSelectedUserRoleIds(List<Integer> selectedUserRoleIds) {
		this.selectedUserRoleIds = selectedUserRoleIds;
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
	 * @return the newsletter_dataTable
	 */
	public HtmlDataTable getNewsletter_dataTable() {
		return newsletter_dataTable;
	}

	/**
	 * @param newsletter_dataTable the newsletter_dataTable to set
	 */
	public void setNewsletter_dataTable(HtmlDataTable newsletter_dataTable) {
		this.newsletter_dataTable = newsletter_dataTable;
	}

	/**
	 * @return the userGroupsSelectItemsList
	 */
	public List<SelectItem> getUserGroupsSelectItemsList() {
		return userGroupsSelectItemsList;
	}

	/**
	 * @param userGroupsSelectItemsList the userGroupsSelectItemsList to set
	 */
	public void setUserGroupsSelectItemsList(
			List<SelectItem> userGroupsSelectItemsList) {
		this.userGroupsSelectItemsList = userGroupsSelectItemsList;
	}

	/**
	 * @return the userRolesSelectItemsList
	 */
	public List<SelectItem> getUserRolesSelectItemsList() {
		return userRolesSelectItemsList;
	}

	/**
	 * @param userRolesSelectItemsList the userRolesSelectItemsList to set
	 */
	public void setUserRolesSelectItemsList(
			List<SelectItem> userRolesSelectItemsList) {
		this.userRolesSelectItemsList = userRolesSelectItemsList;
	}

	/**
	 * @return the newsletter
	 */
	public Newsletter getNewsletter() {
		return newsletter;
	}

	/**
	 * @param newsletter the newsletter to set
	 */
	public void setNewsletter(Newsletter newsletter) {
		this.newsletter = newsletter;
	}

	/**
	 * @return the currentTrainer
	 */
	public Trainer getCurrentTrainer() {
		return currentTrainer;
	}

	/**
	 * @param currentTrainer the currentTrainer to set
	 */
	public void setCurrentTrainer(Trainer currentTrainer) {
		this.currentTrainer = currentTrainer;
	}

	/**
	 * @return the currentContractor
	 */
	public ContractorUser getCurrentContractor() {
		return currentContractor;
	}

	/**
	 * @param currentContractor the currentContractor to set
	 */
	public void setCurrentContractor(ContractorUser currentContractor) {
		this.currentContractor = currentContractor;
	}

	/**
	 * @return the sexSelectItemsList
	 */
	public List<SelectItem> getSexSelectItemsList() {
		return sexSelectItemsList;
	}

	/**
	 * @param sexSelectItemsList the sexSelectItemsList to set
	 */
	public void setSexSelectItemsList(List<SelectItem> sexSelectItemsList) {
		this.sexSelectItemsList = sexSelectItemsList;
	}

	/**
	 * @return the electiveTypesSelectItemsList
	 */
	public List<SelectItem> getElectiveTypesSelectItemsList() {
		return electiveTypesSelectItemsList;
	}

	/**
	 * @param electiveTypesSelectItemsList the electiveTypesSelectItemsList to set
	 */
	public void setElectiveTypesSelectItemsList(
			List<SelectItem> electiveTypesSelectItemsList) {
		this.electiveTypesSelectItemsList = electiveTypesSelectItemsList;
	}

	/**
	 * @return the degrees_dataTable
	 */
	public HtmlDataTable getDegrees_dataTable() {
		return degrees_dataTable;
	}

	/**
	 * @param degrees_dataTable the degrees_dataTable to set
	 */
	public void setDegrees_dataTable(HtmlDataTable degrees_dataTable) {
		this.degrees_dataTable = degrees_dataTable;
	}

	/**
	 * @return the profxp_dataTable
	 */
	public HtmlDataTable getProfxp_dataTable() {
		return profxp_dataTable;
	}

	/**
	 * @param profxp_dataTable the profxp_dataTable to set
	 */
	public void setProfxp_dataTable(HtmlDataTable profxp_dataTable) {
		this.profxp_dataTable = profxp_dataTable;
	}

	/**
	 * @return the degreeTypesSelectItemsList
	 */
	public List<SelectItem> getDegreeTypesSelectItemsList() {
		return degreeTypesSelectItemsList;
	}

	/**
	 * @param degreeTypesSelectItemsList the degreeTypesSelectItemsList to set
	 */
	public void setDegreeTypesSelectItemsList(
			List<SelectItem> degreeTypesSelectItemsList) {
		this.degreeTypesSelectItemsList = degreeTypesSelectItemsList;
	}

	/**
	 * @return the degreeFieldsSelectItemsList
	 */
	public List<SelectItem> getDegreeFieldsSelectItemsList() {
		return degreeFieldsSelectItemsList;
	}

	/**
	 * @param degreeFieldsSelectItemsList the degreeFieldsSelectItemsList to set
	 */
	public void setDegreeFieldsSelectItemsList(
			List<SelectItem> degreeFieldsSelectItemsList) {
		this.degreeFieldsSelectItemsList = degreeFieldsSelectItemsList;
	}

	/**
	 * @return the profxpFieldsSelectItemsList
	 */
	public List<SelectItem> getProfxpFieldsSelectItemsList() {
		return profxpFieldsSelectItemsList;
	}

	/**
	 * @param profxpFieldsSelectItemsList the profxpFieldsSelectItemsList to set
	 */
	public void setProfxpFieldsSelectItemsList(
			List<SelectItem> profxpFieldsSelectItemsList) {
		this.profxpFieldsSelectItemsList = profxpFieldsSelectItemsList;
	}

	/**
	 * @return the talksExpListSelectItems
	 */
	public List<SelectItem> getTalksExpListSelectItems() {
		return talksExpListSelectItems;
	}

	/**
	 * @param talksExpListSelectItems the talksExpListSelectItems to set
	 */
	public void setTalksExpListSelectItems(List<SelectItem> talksExpListSelectItems) {
		this.talksExpListSelectItems = talksExpListSelectItems;
	}

	/**
	 * @return the teachingExpListSelectItems
	 */
	public List<SelectItem> getTeachingExpListSelectItems() {
		return teachingExpListSelectItems;
	}

	/**
	 * @param teachingExpListSelectItems the teachingExpListSelectItems to set
	 */
	public void setTeachingExpListSelectItems(
			List<SelectItem> teachingExpListSelectItems) {
		this.teachingExpListSelectItems = teachingExpListSelectItems;
	}

	/**
	 * @return the certificationsSelectItemsList
	 */
	public List<SelectItem> getCertificationsSelectItemsList() {
		return certificationsSelectItemsList;
	}

	/**
	 * @param certificationsSelectItemsList the certificationsSelectItemsList to set
	 */
	public void setCertificationsSelectItemsList(
			List<SelectItem> certificationsSelectItemsList) {
		this.certificationsSelectItemsList = certificationsSelectItemsList;
	}

	/**
	 * @return the selectUserType
	 */
	public String getSelectUserType() {
		return selectUserType;
	}

	/**
	 * @param selectUserType the selectUserType to set
	 */
	public void setSelectUserType(String selectUserType) {
		this.selectUserType = selectUserType;
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
	 * @return the contractorSelectItemsList
	 */
	public List<SelectItem> getContractorSelectItemsList() {
		return contractorSelectItemsList;
	}

	/**
	 * @param contractorSelectItemsList the contractorSelectItemsList to set
	 */
	public void setContractorSelectItemsList(
			List<SelectItem> contractorSelectItemsList) {
		this.contractorSelectItemsList = contractorSelectItemsList;
	}

	/**
	 * @return the form_is_user_edit
	 */
	public boolean isForm_is_user_edit() {
		return form_is_user_edit;
	}

	/**
	 * @param form_is_user_edit the form_is_user_edit to set
	 */
	public void setForm_is_user_edit(boolean form_is_user_edit) {
		this.form_is_user_edit = form_is_user_edit;
	}

	/**
	 * @return the regionAvailabilityIds
	 */
	public List<Integer> getRegionAvailabilityIds() {
		return regionAvailabilityIds;
	}

	/**
	 * @param regionAvailabilityIds the regionAvailabilityIds to set
	 */
	public void setRegionAvailabilityIds(List<Integer> regionAvailabilityIds) {
		this.regionAvailabilityIds = regionAvailabilityIds;
	}

	/**
	 * @return the certificationIds
	 */
	public List<String> getCertificationIds() {
		return certificationIds;
	}

	/**
	 * @param certificationIds the certificationIds to set
	 */
	public void setCertificationIds(List<String> certificationIds) {
		this.certificationIds = certificationIds;
	}

	/**
	 * @return the requestedUserRoleIds
	 */
	public List<Integer> getRequestedUserRoleIds() {
		return requestedUserRoleIds;
	}

	/**
	 * @param requestedUserRoleIds the requestedUserRoleIds to set
	 */
	public void setRequestedUserRoleIds(List<Integer> requestedUserRoleIds) {
		this.requestedUserRoleIds = requestedUserRoleIds;
	}
}
