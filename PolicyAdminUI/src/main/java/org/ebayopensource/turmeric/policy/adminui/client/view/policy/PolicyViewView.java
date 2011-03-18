/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view.policy;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.Display;
import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ExtraField;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Operation;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicySubjectAssignment;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Resource;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Subject;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.policy.PolicyViewPresenter.PolicyViewDisplay;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.policy.PolicyViewPresenter.ResourcesContentDisplay;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.policy.PolicyViewPresenter.SubjectContentDisplay;
import org.ebayopensource.turmeric.policy.adminui.client.util.PolicyExtraFieldsUtil;
import org.ebayopensource.turmeric.policy.adminui.client.view.ErrorDialog;
import org.ebayopensource.turmeric.policy.adminui.client.view.common.AbstractGenericView;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

public class PolicyViewView extends ResizeComposite implements
		PolicyViewDisplay {

	private final DockLayoutPanel mainPanel;
	private static final String TITLE_FORM = PolicyAdminUIUtil.policyAdminConstants
			.policyInformationView();
	protected static final UserAction SELECTED_ACTION = UserAction.POLICY_VIEW;

	private Display contentView;
	private ResourcesContentDisplay resourceContentView;
	private SubjectContentDisplay subjectContentView;

	protected Label policyName;
	protected Label policyDesc;
	protected Label policyType;
	protected Label policyStatus;

	protected boolean extraGridAvailable;

	private Button cancelButton;

	protected Grid extraFieldsGrid = new Grid(1, 1);
	protected List<ExtraField> extraFieldList;

	public PolicyViewView() {
		mainPanel = new DockLayoutPanel(Unit.EM);
		initWidget(mainPanel);
		mainPanel.setWidth("100%");
		initialize();

	}

	@Override
	public UserAction getSelectedAction() {
		return SELECTED_ACTION;
	}

	public void initialize() {

		policyName = new Label();
		policyDesc = new Label();
		policyType = new Label();
		policyStatus = new Label();
		extraFieldList = new ArrayList<ExtraField>();

		// CONTENT
		StackLayoutPanel policyContentPanel = new StackLayoutPanel(Unit.EM);
		policyContentPanel.add(initContentView(), TITLE_FORM, 2);
		policyContentPanel.add(initResourceContentView(),
				PolicyAdminUIUtil.policyAdminConstants.resources(), 2);
		policyContentPanel.add(initSubjectContentView(),
				PolicyAdminUIUtil.policyAdminConstants.subjectsAndSubjectGroups(), 2);
		policyContentPanel.setHeight("90%");

		cancelButton = new Button(PolicyAdminUIUtil.constants.cancel());
		final HorizontalPanel buttonsPannel = new HorizontalPanel();
		buttonsPannel.add(cancelButton);

		mainPanel.addSouth(buttonsPannel, 1);
		mainPanel.add(policyContentPanel);

	}

	protected Widget initContentView() {
		final ScrollPanel actionPanel = new ScrollPanel();
		contentView = new ContentView();
		actionPanel.add(contentView.asWidget());
		return actionPanel;
	}

	protected Widget initResourceContentView() {
		final ScrollPanel actionPanel = new ScrollPanel();
		resourceContentView = new ResourceContentView();
		actionPanel.add(resourceContentView.asWidget());

		return actionPanel;
	}

	protected Widget initSubjectContentView() {
		final ScrollPanel actionPanel = new ScrollPanel();
		subjectContentView = new SubjectContentView();
		actionPanel.add(subjectContentView.asWidget());

		return actionPanel;
	}

	private class ContentView extends AbstractGenericView implements Display {
		final private VerticalPanel mainPanel;
		private Grid policyInfoGrid;

		public ContentView() {
			mainPanel = new VerticalPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			mainPanel.clear();

			policyName.setWidth("300px");
			policyDesc.setWidth("250px");
			policyType.setWidth("300px");
			policyStatus.setWidth("300px");

			policyInfoGrid = new Grid(4, 2);

			policyInfoGrid.setWidget(0, 0, new Label(
					PolicyAdminUIUtil.policyAdminConstants.policyName() + ":"));
			policyInfoGrid.setWidget(0, 1, policyName);
			policyInfoGrid
					.setWidget(
							1,
							0,
							new Label(PolicyAdminUIUtil.policyAdminConstants
									.policyDescription() + ":"));
			policyInfoGrid.setWidget(1, 1, policyDesc);

			policyInfoGrid.setWidget(2, 0, new Label(
					PolicyAdminUIUtil.policyAdminConstants.policyType() + ":"));
			policyInfoGrid.setWidget(2, 1, policyType);

			policyInfoGrid.setWidget(3, 0, new Label(
					PolicyAdminUIUtil.policyAdminConstants.status() + ":"));
			policyInfoGrid.setWidget(3, 1, policyStatus);

			mainPanel.add(policyInfoGrid);
			setExtraFields();
			mainPanel.add(extraFieldsGrid);
			extraFieldsGrid.setVisible(extraGridAvailable);

		}

		protected void setExtraFields() {
			// TODO move this setting to a particular RL class
			extraFieldList = PolicyExtraFieldsUtil.getRLExtraFields();
			extraFieldsGrid = new Grid(extraFieldList.size() + 1, 3);
			for (ExtraField extraField : extraFieldList) {

				extraFieldsGrid.setWidget(extraField.getOrder() - 1, 0,
						new Label(extraField.getLabelName()));

				if (extraField.getFieldType() != null
						&& "CheckBox".equalsIgnoreCase(extraField
								.getFieldType())) {
					final CheckBox chBox = new CheckBox();
					// TODO set value
					chBox.setEnabled(false);
					extraField.setCheckBox(chBox);
					extraFieldsGrid.setWidget(extraField.getOrder() - 1, 1,
							extraField.getCheckBox());
				} else if (extraField.getFieldType() != null
						&& ("TextBox".equalsIgnoreCase(extraField
								.getFieldType())
								|| "TextArea".equalsIgnoreCase(extraField
										.getFieldType()) || "ListBox"
								.equalsIgnoreCase(extraField.getFieldType()))) {
					final Label label = new Label(" ");
					extraFieldsGrid.setWidget(extraField.getOrder() - 1, 1,
							label);
				}
			}

		}
	}

	private class ResourceContentView extends AbstractGenericView implements
			ResourcesContentDisplay {
		final private FlowPanel mainPanel;

		private CellTable<Resource> cellTable;
		private ProvidesKey<Resource> keyProvider;
		private ListDataProvider<Resource> dataProvider;

		public ResourceContentView() {
			mainPanel = new FlowPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			mainPanel.clear();

			// bottom part of panel is a table with search results
			final Grid summaryGrid = new Grid(2, 1);
			summaryGrid.setStyleName("sggrid");

			keyProvider = new ProvidesKey<Resource>() {
				public Object getKey(final Resource assignment) {
					return assignment == null ? null : assignment
							.getResourceType() + assignment.getResourceName();
				}
			};

			cellTable = new CellTable<Resource>(keyProvider);

			dataProvider = new ListDataProvider<Resource>();
			dataProvider.addDataDisplay(cellTable);
			final SimplePager pager = new SimplePager();
			pager.setDisplay(cellTable);
			// resource type
			final TextColumn<Resource> resourceTypeCol = new TextColumn<Resource>() {
				public String getValue(final Resource assignment) {
					if (assignment == null
							|| assignment.getResourceType() == null) {
						return null;
					}
					return assignment.getResourceType();
				}
			};
			cellTable.addColumn(resourceTypeCol,
					PolicyAdminUIUtil.policyAdminConstants.resourceType());

			// resource Name
			final TextColumn<Resource> resourceNameCol = new TextColumn<Resource>() {
				public String getValue(final Resource assignment) {
					if (assignment == null
							|| assignment.getResourceName() == null) {
						return null;
					}
					return assignment.getResourceName();
				}
			};
			cellTable.addColumn(resourceNameCol,
					PolicyAdminUIUtil.policyAdminConstants.resourceName());

			// operations
			// TODO add operations name into table
			Column<Resource, List<String>> resourceOpsCol = new Column<Resource, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(Resource resource) {

					if (resource == null || resource.getOpList() == null) {
						return null;
					}
					ArrayList<String> opsNamesList = new ArrayList<String>();
					for (Operation op : resource.getOpList()) {
						opsNamesList.add(op.getOperationName());
					}
					return opsNamesList;
				}
			};

			cellTable.addColumn(resourceOpsCol,
					PolicyAdminUIUtil.policyAdminConstants.operations());

			summaryGrid.setWidget(0, 0, cellTable);
			summaryGrid.setWidget(1, 0, pager);

			mainPanel.addStyleName("resource-summary");
			summaryGrid.addStyleName("resource-content");
			mainPanel.add(summaryGrid);
		}

		public void error(final String msg) {
			final ErrorDialog dialog = new ErrorDialog(true);
			dialog.setMessage(msg);
			dialog.getDialog().center();
			dialog.show();

		}

		public void setAssignments(final List<Resource> assignments) {
			final List<Resource> data = dataProvider.getList();
			data.clear();
			if (assignments != null) {
				data.addAll(assignments);
			}
			cellTable.redraw();
		}

	}

	private class SubjectContentView extends AbstractGenericView implements
			SubjectContentDisplay {
		private final SimplePanel mainPanel;
		private final Grid mainGrid;
		private CellTable<PolicySubjectAssignment> cellTable;
		private ProvidesKey<PolicySubjectAssignment> keyProvider;
		private ListDataProvider<PolicySubjectAssignment> dataProvider;
		private Grid subjectGrid;
		private SimplePager pager;

		public SubjectContentView() {
			mainPanel = new SimplePanel();
			mainGrid = new Grid(2, 1);
			mainPanel.add(mainGrid);
			initWidget(mainPanel);

			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			subjectGrid = new Grid(3, 1);
			createSubjectTableFields();
			subjectGrid.setWidget(0, 0, cellTable);
			subjectGrid.setWidget(1, 0, pager);
			mainGrid.setWidget(1, 0, subjectGrid);
			mainGrid.setWidth("60%");
		}

		protected void createSubjectTableFields() {

			keyProvider = new ProvidesKey<PolicySubjectAssignment>() {
				public Object getKey(PolicySubjectAssignment assignment) {
					return assignment == null ? null : assignment
							.getSubjectType();
				}
			};

			cellTable = new CellTable<PolicySubjectAssignment>(keyProvider);

			dataProvider = new ListDataProvider<PolicySubjectAssignment>();
			dataProvider.addDataDisplay(cellTable);

			pager = new SimplePager();
			pager.setDisplay(cellTable);
			// text column for type
			final TextColumn<PolicySubjectAssignment> typeCol = new TextColumn<PolicySubjectAssignment>() {
				public String getValue(final PolicySubjectAssignment assignment) {
					if (assignment == null ) {
						return null;
					}
					return assignment.getSubjectType();
				}
			};
			cellTable.addColumn(typeCol,
					PolicyAdminUIUtil.policyAdminConstants.subjectType());

			// text column for Subject names
			Column<PolicySubjectAssignment, List<String>> subjectNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getSubjects() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (Subject subject : assignment.getSubjects()) {
						namesList.add(subject.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(subjectNamesCol,
					PolicyAdminUIUtil.policyAdminConstants.subjects());

			// text column for Exclusion Subject names
			Column<PolicySubjectAssignment, List<String>> excluionSubjectNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getExclusionSubjects() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (Subject subject : assignment.getExclusionSubjects()) {
						namesList.add(subject.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(excluionSubjectNamesCol,
					PolicyAdminUIUtil.policyAdminConstants.exclusionSubjects());

			// text column for SubjectGroup names
			Column<PolicySubjectAssignment, List<String>> sgNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getSubjectGroups()  == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (SubjectGroup subjectGroup : assignment.getSubjectGroups()) {
						namesList.add(subjectGroup.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(sgNamesCol,
					PolicyAdminUIUtil.policyAdminConstants.subjectGroups());

			// text column for Exclusion Subject Group names
			Column<PolicySubjectAssignment, List<String>> exclusionSGNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getExclusionSubjectGroups() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (SubjectGroup subjectGroup : assignment.getExclusionSubjectGroups()) {
						namesList.add(subjectGroup.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(exclusionSGNamesCol,
					PolicyAdminUIUtil.policyAdminConstants.exclusionSubjectGroups());

		}

		public void setAssignments(
				final List<PolicySubjectAssignment> assignments) {
			final List<PolicySubjectAssignment> data = dataProvider.getList();
			data.clear();

			if (assignments != null) {
				data.addAll(assignments);
			}
			cellTable.redraw();
		}

	}

	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}

	public UserAction getActionSelected() {
		return UserAction.BL_POLICY_CREATE;
	}

	public Display getContentView() {
		return contentView;
	}

	public ResourcesContentDisplay getResourceContentView() {
		return resourceContentView;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	@Override
	public SubjectContentDisplay getSubjectContentView() {
		return subjectContentView;
	}

	public void setPolicyDesc(final String policyDesc) {
		this.policyDesc.setText(policyDesc);
	}

	public void setPolicyType(final String policyType) {
		this.policyType.setText(policyType);
	}

	public void setPolicyStatus(final boolean enabled) {
		if (enabled) {
			this.policyStatus
					.setText(PolicyAdminUIUtil.policyAdminConstants.enable());
		} else {
			this.policyStatus.setText(PolicyAdminUIUtil.policyAdminConstants
					.disable());
		}
	}

	public void setPolicyName(final String policyName) {
		this.policyName.setText(policyName);
	}

	public void setExtraFieldAvailable(final boolean available) {
		this.extraGridAvailable = available;
	}

	public void setExtraFieldList(List<ExtraField> extraFieldList) {

		for (ExtraField extraField : extraFieldList) {

			if (extraField.getFieldType() != null
					&& "CheckBox".equalsIgnoreCase(extraField.getFieldType())) {
				final CheckBox ch = (CheckBox) extraFieldsGrid.getWidget(
						extraField.getOrder() - 1, 1);
				ch.setValue(Boolean.parseBoolean(extraField.getValue()));

			} else if (extraField.getFieldType() != null
					&& ("Label".equalsIgnoreCase(extraField.getFieldType()))) {
				final Label lbl = (Label) extraFieldsGrid.getWidget(
						extraField.getOrder() - 1, 1);
				lbl.setText(extraField.getValue());

			}
		}
		extraFieldsGrid.setVisible(extraGridAvailable);
	}

	public void clear() {
		policyName.setText("");
		policyDesc.setText("");
		policyType.setText("");
		policyStatus.setText("");
		extraGridAvailable = false;

		if (this.extraFieldList != null && this.extraFieldList.size() > 0) {

			for (int i = 0; i < extraFieldList.size(); i++) {
				extraFieldList.get(i).setValue("");
			}

			setExtraFieldList(extraFieldList);
		}

	}

	public void error(final String msg) {
		final ErrorDialog dialog = new ErrorDialog(true);
		dialog.setMessage(msg);
		dialog.getDialog().center();
		dialog.show();
	}

	public void setAssociatedId(final String id) {
		// TODO Auto-generated method stub

	}

	public String getAssociatedId() {
		// TODO Auto-generated method stub
		return null;
	}

}
