package org.bpelunit.framework.client.eclipse.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class BPELUnitCoverageResultView extends ViewPart implements Observer {

	private Label fInfoLabel;

	private CoverageModel model;

	private List fileList;

	private boolean initialized = false;

	private Table tableTestCases;

	private Table table;

	private Text text;

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(3, false));
		fInfoLabel = new Label(parent, SWT.LEFT);
		fInfoLabel.setText(" BPELUnit Testcoverage");
		GridData gData = new GridData();
		gData.horizontalSpan = 3;
		gData.grabExcessHorizontalSpace = true;
		fInfoLabel.setLayoutData(gData);

		text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
		gData = new GridData();
		gData.horizontalSpan = 3;
		gData.grabExcessHorizontalSpace = true;
		text.setLayoutData(gData);

		Group groupFilesStatistics = new Group(parent, SWT.V_SCROLL);
		gData = new GridData();
		gData.grabExcessHorizontalSpace = true;
		gData.horizontalAlignment = GridData.FILL;
		gData.verticalAlignment = GridData.FILL;
		groupFilesStatistics.setLayoutData(gData);
		groupFilesStatistics.setLayout(new GridLayout(3, false));
		Label label1=new Label(groupFilesStatistics,SWT.CENTER);
		label1.setText("BPELFiles in archive");
		label1=new Label(groupFilesStatistics,SWT.CENTER);
		label1.setText("Test coverage");
		label1=new Label(groupFilesStatistics,SWT.CENTER);
		label1.setText("Test cases");
		fileList = new List(groupFilesStatistics, SWT.V_SCROLL | SWT.MULTI
				| SWT.H_SCROLL|SWT.BORDER);
		gData = new GridData();
		gData.widthHint = 200;
		gData.heightHint = 150;
		fileList.setLayoutData(gData);
		fileList.setBackground(parent.getBackground());
		fileList.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent arg0) {
				if (arg0.getSource() instanceof List) {
					List list = (List) arg0.getSource();
					model.setSelectedFiles(list.getSelection());
				}
			}

		});
		table = new Table(groupFilesStatistics, SWT.BORDER | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		gData = new GridData();
//		gData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_FILL;
//		gData.grabExcessHorizontalSpace = true;
		 gData.widthHint = 450;
		gData.heightHint = 160;
		table.setLayoutData(gData);
		TableColumn col1 = new TableColumn(table, 0);
		col1.setText("Metric");
		col1.setWidth(180);
		col1.setMoveable(false);
		TableColumn col2 = new TableColumn(table, SWT.CENTER);
		col2.setText("total number");
		col2.setWidth(100);
		col2.setMoveable(false);
		TableColumn col3 = new TableColumn(table, SWT.CENTER);
		col3.setText("tested number");
		col3.setWidth(100);
		col3.setMoveable(false);
		TableColumn col4 = new TableColumn(table, 0);
		col4.setText("per cent");
		col4.setWidth(100);
		col4.setMoveable(false);
		tableTestCases = new Table(groupFilesStatistics, SWT.BORDER
				| SWT.VIRTUAL | SWT.CHECK);
		tableTestCases.setLayout(new GridLayout(1, false));
		tableTestCases.setBackground(parent.getBackground());
		tableTestCases.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.CHECK && e.item instanceof TableItem) {
					TableItem item = (TableItem) e.item;
					model.setTestcaseStatus(item.getText(), item.getChecked());
				}
			}
		});
		gData = new GridData();
		gData.widthHint = 200;
		gData.heightHint = 150;
//		gData.horizontalAlignment=SWT.LEFT;
		gData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_FILL;
		gData.grabExcessHorizontalSpace = true;
		tableTestCases.setLayoutData(gData);
		tableTestCases.setSize(180, 130);
		initializeElements();
		parent.pack();
		model = new CoverageModel();
		model.addObserver(this);
	}

	private void initializeElements() {

		text.setText("Info: ");
		fileList.removeAll();
		tableTestCases.removeAll();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void update(Observable arg0, Object arg1) {
		if (!initialized) {
			initializeElements();
			for (Iterator<String> iter = model.getFiles().iterator(); iter
					.hasNext();) {
				fileList.add(iter.next());
				fileList.update();
			}
			for (Iterator<String> iter = model.getAllTestCases().iterator(); iter
					.hasNext();) {
				// Button but = new Button(tableTestCases, SWT.CHECK);
				// but.setText(iter.next());
				// but.addSelectionListener(this);
				// // groupTestCases.update();
				// tableTestCases.pack(true);
				TableItem item = new TableItem(tableTestCases, SWT.NULL);
				item.setText(iter.next());
			}
			java.util.List<String> infos = model.getInfos();
			if (infos != null && infos.size() > 0) {
				for (Iterator<String> iter = infos.iterator(); iter.hasNext();) {
					text.append(iter.next());
				}
			} else {
				text.append("Test coverage is successful measured.");
			}
			initialized = true;
		}
		text.pack(true);

		table.removeAll();
		java.util.List<String[]> list = model.getCurrentData();
		for (Iterator<String[]> iter = list.iterator(); iter.hasNext();) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(iter.next());
		}
	}

//	private void addStatistic(IStatistic statistic, Set<String> set,
//			boolean subStatistic) {
//		int total = 0;
//		int tested = 0;
//		String name;
//		name = statistic.getName();
//		if (subStatistic)
//			name = "     " + name;
//		total = statistic.getTotalNumber();
//		tested = statistic.getTestedNumber(set);
//		TableItem item = new TableItem(table, SWT.NULL);
//		String relativ;
//		if (total > 0)
//			relativ = Integer.toString((tested * 100 / total)) + "%";
//		else
//			relativ = "100%";
//		String[] data = new String[] { name, Integer.toString(total),
//				Integer.toString(tested), relativ };
//		item.setText(data);
//
//	}

	public void setData(java.util.List<String> testCases,
			java.util.List<IFileStatistic> statistics,
			java.util.List<String> infos) {
		initialized = false;
		model.setData(testCases, statistics, infos);

	}
	
	public void initialize() {
		model.initialize();
		initializeElements();
	}

	class CoverageModel extends Observable {

		private java.util.List<String> testCases;

		private String[] selectedFiles;

		private Set<String> checkedTestCases;

		private java.util.List<String> infos = null;

		private Hashtable<String, IFileStatistic> fileStatistics;

		CoverageModel() {
			init();
		}

		private void init() {
			testCases = new ArrayList<String>();
			checkedTestCases = new HashSet<String>();
			fileStatistics = new Hashtable<String, IFileStatistic>();
			selectedFiles = new String[] {};
		}

		public void setData(java.util.List<String> testCases,
				java.util.List<IFileStatistic> statistics,
				java.util.List<String> infos) {
			init();
			this.testCases = testCases;
			this.infos = infos;
			for (Iterator<IFileStatistic> iter = statistics.iterator(); iter
					.hasNext();) {
				IFileStatistic fileStatistic = iter.next();
				fileStatistics.put(fileStatistic.getBPELFilename(),
						fileStatistic);
			}
			setChanged();
			notifyObservers();
		}

		public void setSelectedFiles(String[] strings) {
			selectedFiles = strings;
			setChanged();
			notifyObservers();
		}

		public void setTestcaseStatus(String name, boolean status) {
			if (status)
				checkedTestCases.add(name);
			else
				checkedTestCases.remove(name);
			setChanged();
			notifyObservers();
		}

		public java.util.List<String[]> getCurrentData() {
			return mergeMetrics();
		}

		private java.util.List<String[]> mergeMetrics() {
			java.util.List<String[]> tableEntries = new ArrayList<String[]>();
			if (selectedFiles.length > 0) {
				java.util.List<IStatistic> statistics;
				IFileStatistic fileStatistic = fileStatistics
						.get(selectedFiles[0]);
				for (Iterator<IStatistic> iter = fileStatistic.getStatistics()
						.iterator(); iter.hasNext();) {
					statistics = new ArrayList<IStatistic>();
					IStatistic statistic1 = iter.next();
					statistics.add(statistic1);
					for (int i = 1; i < selectedFiles.length; i++) {
						IFileStatistic fileStatistic2 = fileStatistics
								.get(selectedFiles[i]);
						IStatistic statistic2 = fileStatistic2
								.getStatistic(statistic1.getName());
						statistics.add(statistic2);
					}
					tableEntries.add(mergeMetric(statistics, statistic1
							.getName()));
				}
			}
			return tableEntries;
		}

		private String[] mergeMetric(java.util.List<IStatistic> statistics,
				String statisticName) {
			int tested = 0;
			int total = 0;
			String relativ = "-";
			for (Iterator<IStatistic> iter = statistics.iterator(); iter
					.hasNext();) {
				IStatistic statistic = iter.next();
				total = total + statistic.getTotalNumber();
				tested = tested + statistic.getTestedNumber(checkedTestCases);
			}
			if (total > 0)
				relativ = Integer.toString((tested * 100 / total)) + "%";
			return new String[] { statisticName, Integer.toString(total),
					Integer.toString(tested), relativ };
		}

		public Set<String> getCheckedTestCases() {
			return checkedTestCases;
		}

		public java.util.List<String> getAllTestCases() {
			return testCases;
		}

		public Set<String> getFiles() {
			return fileStatistics.keySet();
		}

		public java.util.List<String> getInfos() {
			return infos;
		}

		public void initialize() {
			testCases = new ArrayList<String>();
			fileStatistics = new Hashtable<String, IFileStatistic>();
			checkedTestCases = new HashSet<String>();
			selectedFiles = new String[] {};
		}
	}



}
