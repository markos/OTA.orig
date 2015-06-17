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

/* $Id: PollBean.java 1514 2009-11-10 11:00:35Z markos $ */

package gr.codex.polls;

import gr.codex.katartisis.KatartisisBeanLocal;
import gr.codex.usermgmt.UserManagementBeanLocal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

public class PollBean {
	Context ctx;
	KatartisisBeanLocal kbean;
	UserManagementBeanLocal ubean;
	
	HtmlDataTable polls_dataTable = new HtmlDataTable();
	HtmlDataTable questions_dataTable = new HtmlDataTable();
	HtmlDataTable choices_dataTable = new HtmlDataTable();
	
	private List<SelectItem> questionOperatorsSelectItemsList = new ArrayList<SelectItem>();
	
	private Map<PollQuestion,Object> results = new HashMap<PollQuestion, Object>();
	
	private Map<PollQuestion, Map<PollOption, Integer>> stats = new HashMap<PollQuestion, Map<PollOption,Integer>>();
	
	private Map<PollQuestion, String> chart_URL = new HashMap<PollQuestion, String>();
	
	List<Poll> polls = new ArrayList<Poll>();
	private Poll currentPoll;
	private PollEntry pollentry;
	private int entries_size;
	private Integer pollid;
	
	final Color[] colors = {
			Color.FIREBRICK,
			Color.ROYALBLUE,
			Color.FORESTGREEN,
			Color.FUCHSIA,
			Color.WHEAT,
			Color.BISQUE,
			Color.GRAY,
			Color.MAGENTA
		};

	public PollBean() {
		try {
			ctx = new InitialContext();
			kbean = (KatartisisBeanLocal) ctx.lookup("KatartisisBean/local");
			ubean = (UserManagementBeanLocal) ctx.lookup("UserManagementBean/local");
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
		for (QuestionOperator t: QuestionOperator.values()) {
			this.questionOperatorsSelectItemsList.add(new SelectItem(t.name(), t.toString()));
		}
	}
	
	public void clearAllDataTables() {
		this.polls_dataTable = new HtmlDataTable();
		this.questions_dataTable = new HtmlDataTable();
		this.choices_dataTable = new HtmlDataTable();
	}
	
	public void reloadPolls() { 
		this.polls = (List<Poll>) kbean.getAllRecordsOfType(Poll.class);
	}
	
	public String gotoPolls() {
		reloadPolls();

		clearAllDataTables();
		
		return "polls";
	}
	
	public String addPoll() {
		this.currentPoll = new Poll();
		this.currentPoll.setName("Νέο ερωτηματολόγιο");
		this.currentPoll.setDate_start(new Date());
		this.currentPoll.setActive(true);
		this.currentPoll = (Poll) kbean.createRecord(this.currentPoll);
		this.polls.add(this.currentPoll);
		
		reloadPolls();
		
		return "polls";
	}
	
	public String editPoll() {
		this.currentPoll = (Poll) this.polls_dataTable.getRowData(); 
		clearAllDataTables();
		
		reloadPolls();
		
		return "edit_poll";
	}
	
	public String deletePoll() {
		this.currentPoll = (Poll) this.polls_dataTable.getRowData();
		this.polls.remove(this.currentPoll);
		kbean.deleteRecord(Poll.class, this.currentPoll.getId());
		
		reloadPolls();
		
		return "polls";
	}
	
	public String viewPoll() {
		this.currentPoll = (Poll) this.polls_dataTable.getRowData();
		this.pollentry = new PollEntry();
		this.results.clear();
		clearAllDataTables();
		
		reloadPolls();
		
		return "view_poll";
	}
	
	public void setPollfromURL(Integer pollid) {
		// dummy do nothing
	}
	
	public Integer getPollfromURL() {
		String pid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pollid");
		System.out.println("pollid = " + pid);
		if (pid != null && pid.compareTo("") != 0) {
			this.pollid = Integer.parseInt(pid);
			this.currentPoll = (Poll) kbean.getRecordById(this.pollid, Poll.class);
		}
		System.out.println("pollid = " + this.pollid);
		this.pollentry = new PollEntry();
		this.results.clear();
		clearAllDataTables();
		
		return this.pollid;
	}
	
	public String endPoll() {
		this.currentPoll = (Poll) this.polls_dataTable.getRowData();
		this.currentPoll.setActive(false);
		this.currentPoll = (Poll) kbean.updateRecord(this.currentPoll);
	
		reloadPolls();
		
		return "polls";
	}
	
	public String savePoll() {
		if (this.currentPoll.getName().equals("") == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε όνομα ερωτηματολογίου!") );
			return "edit_poll";
		}
		if (this.currentPoll.getDate_start() == null) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε ημερομηνία έναρξης! Αν δεν εισάγετε ημερομηνία λήξης το ερωτηματολόγιο θα είναι πάντοτε ανοικτό.") );
			return "edit_poll";
		}
		if (this.currentPoll.getDate_start() != null && this.currentPoll.getDate_end() != null &&
				this.currentPoll.getDate_start().after(this.currentPoll.getDate_end()) == true) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Η ημ/νία έναρξης πρέπει να είναι προγενέστερη της ημ/νίας λήξης!") );
			return "edit_poll";
		}
		if (this.currentPoll.getQuestions().size() == 0) {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Πρέπει να εισάγετε τουλάχιστον μια ερώτηση!") );
			return "edit_poll";
		}

		this.currentPoll = (Poll) kbean.updateRecord(this.currentPoll);
		
		reloadPolls();
		
		clearAllDataTables();
		
		return "polls";
	}
	
	public String submitPoll() {
		
		this.pollentry.setEntryDate(new Date());
		this.pollentry.setParentPoll(this.currentPoll);
		for (PollQuestion q : this.currentPoll.getQuestions()) {
			System.out.println("question: " + q.getId() + ", " + q.getQuestion());
			PollResult result = new PollResult();
			result.setQuestion(q);
			result.setParentEntry(this.pollentry);
			if (q.getOperator() == QuestionOperator.OPERATOR_XOR) {
				String opid = (String) this.results.get(q);
				System.out.println("single:");
				System.out.println("op: " + opid);
				PollOption op = (PollOption) kbean.getRecordById(Integer.valueOf(opid), PollOption.class);
				PollOption newop = new PollOption();
				PollOptionResult opresult = new PollOptionResult();
				opresult.setParentPollOption(op);
				opresult.setParentPollResult(result);
				result.addOption(opresult);
			}
			if (q.getOperator() == QuestionOperator.OPERATOR_OR) {
				System.out.println("multi:");
				String[] options = (String[]) this.results.get(q);
				for (String opid: options) {
					System.out.println("op: " + opid);
					PollOption op = (PollOption) kbean.getRecordById(Integer.valueOf(opid), PollOption.class);
					PollOptionResult opresult = new PollOptionResult();
					opresult.setParentPollOption(op);
					opresult.setParentPollResult(result);
					result.addOption(opresult);
				}
			}
			this.pollentry.addResult(result);
		}
		this.pollentry = (PollEntry) kbean.createRecord(this.pollentry);
		
		return "poll_submitted";
	}
	
	public String statisticsPoll() {
		this.currentPoll = (Poll) this.polls_dataTable.getRowData();
		
		this.stats.clear();
		for (PollQuestion q: this.currentPoll.getQuestions()) {
			this.stats.put(q, new HashMap<PollOption, Integer>());
			for (PollOption op: q.getChoices()) {
				this.stats.get(q).put(op, 0);
			}
		}
		for (PollEntry entry: this.currentPoll.getEntries()) {
			System.out.println("entry.date" + entry.getEntryDate());
			
			for (PollResult res: entry.getResults()) {
				System.out.println("result.question: " + res.getQuestion().getQuestion());
				for (PollOptionResult opres: res.getOptionResults()) {
					System.out.println("option selected: " + opres.getParentPollOption().getOption());
					PollOption op = opres.getParentPollOption();
					Integer counter = this.stats.get(res.getQuestion()).get(op);
					System.out.println("counter = " + counter);
					counter++;
					this.stats.get(res.getQuestion()).put(op, counter);
					System.out.println("new counter = " + counter);
				}
			}
			System.out.println();
		}
		
		createPercentages();
		createCharts();
		
		this.entries_size = this.currentPoll.getEntries().size();
		
		return "stats_poll";
	}
	
	public void createPercentages() {
		for (PollQuestion q: this.currentPoll.getQuestions()) {
			int total = 0;
			for (PollOption op: q.getChoices()) {
				total += this.stats.get(q).get(op);
			}
			for (PollOption op: q.getChoices()) {
				double perc_d = 100.0 * ((double)this.stats.get(q).get(op) / (double)total);
				int perc_i = (int)perc_d;
				this.stats.get(q).put(op, perc_i);
			}
		}
	}
	
	public void createCharts() {
		this.chart_URL.clear();
		List<Slice> slices = new ArrayList<Slice>();
		
		for (PollQuestion q: this.currentPoll.getQuestions()) {
			slices.clear();
			int c = 0;
			for (PollOption op: q.getChoices()) {
				String legend = op.getOption() + " (" + this.stats.get(q).get(op) + "%) ";
				Slice s = Slice.newSlice(this.stats.get(q).get(op), colors[c], legend);
				slices.add(s);
				c++;
			}

			PieChart chart = GCharts.newPieChart(slices);
			//chart.setTitle(q.getQuestion(), Color.BLACK, 12);
			chart.setSize(550, 200);
			chart.setThreeD(true);
			String url = chart.toURLString();
			this.chart_URL.put(q, url);
		}
	}
	
	public String addPollQuestion() {
		PollQuestion q = new PollQuestion();
		q.setQuestion("νέα ερώτηση");
		q.setOperator(QuestionOperator.OPERATOR_XOR);
		q.setParentPoll(this.currentPoll);
		this.currentPoll.addQuestion(q);
		
		return "edit_poll";
	}
	
	public String deletePollQuestion() {
		PollQuestion q = (PollQuestion) this.questions_dataTable.getRowData();
		this.currentPoll.removeQuestion(q);
		kbean.deleteRecord(PollOption.class, q.getId());
		
		return "edit_poll";
	}
	
	public String addPollOption() {
		PollQuestion q = (PollQuestion) this.questions_dataTable.getRowData();
		PollOption op = new PollOption();
		op.setOption("νέα επιλογή");
		op.setParentPollQuestion(q);
		q.addOption(op);
		
		return "edit_poll";
	}
	
	public String deletePollOption() {
		PollOption op = (PollOption) this.choices_dataTable.getRowData();
		PollQuestion q = op.getParentPollQuestion();
		q.removeOption(op);
		
		kbean.deleteRecord(PollOption.class, op.getId());
		
		return "edit_poll";
	}

	/**
	 * @return the polls
	 */
	public List<Poll> getPolls() {
		return polls;
	}

	/**
	 * @param polls the polls to set
	 */
	public void setPolls(List<Poll> polls) {
		this.polls = polls;
	}

	/**
	 * @return the polls_dataTable
	 */
	public HtmlDataTable getPolls_dataTable() {
		return polls_dataTable;
	}

	/**
	 * @param polls_dataTable the polls_dataTable to set
	 */
	public void setPolls_dataTable(HtmlDataTable polls_dataTable) {
		this.polls_dataTable = polls_dataTable;
	}

	/**
	 * @return the currentPoll
	 */
	public Poll getCurrentPoll() {
		return currentPoll;
	}

	/**
	 * @param currentPoll the currentPoll to set
	 */
	public void setCurrentPoll(Poll currentPoll) {
		this.currentPoll = currentPoll;
	}

	/**
	 * @return the questions_dataTable
	 */
	public HtmlDataTable getQuestions_dataTable() {
		return questions_dataTable;
	}

	/**
	 * @param questions_dataTable the questions_dataTable to set
	 */
	public void setQuestions_dataTable(HtmlDataTable questions_dataTable) {
		this.questions_dataTable = questions_dataTable;
	}

	/**
	 * @return the choices_dataTable
	 */
	public HtmlDataTable getChoices_dataTable() {
		return choices_dataTable;
	}

	/**
	 * @param choices_dataTable the choices_dataTable to set
	 */
	public void setChoices_dataTable(HtmlDataTable choices_dataTable) {
		this.choices_dataTable = choices_dataTable;
	}

	/**
	 * @return the questionOperatorsSelectItemsList
	 */
	public List<SelectItem> getQuestionOperatorsSelectItemsList() {
		return questionOperatorsSelectItemsList;
	}

	/**
	 * @param questionOperatorsSelectItemsList the questionOperatorsSelectItemsList to set
	 */
	public void setQuestionOperatorsSelectItemsList(
			List<SelectItem> questionOperatorsSelectItemsList) {
		this.questionOperatorsSelectItemsList = questionOperatorsSelectItemsList;
	}

	/**
	 * @return the results
	 */
	public Map<PollQuestion, Object> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(Map<PollQuestion, Object> results) {
		this.results = results;
	}

	/**
	 * @return the chartXOR_URL
	 */
	public Map<PollQuestion, String> getChart_URL() {
		return chart_URL;
	}

	/**
	 * @return the entries_size
	 */
	public int getEntries_size() {
		return entries_size;
	}

	/**
	 * @param entries_size the entries_size to set
	 */
	public void setEntries_size(int entries_size) {
		this.entries_size = entries_size;
	}

	/**
	 * @return the pollid
	 */
	public Integer getPollid() {
		return pollid;
	}

	/**
	 * @param pollid the pollid to set
	 */
	public void setPollid(Integer pollid) {
		this.pollid = pollid;
	}
}
