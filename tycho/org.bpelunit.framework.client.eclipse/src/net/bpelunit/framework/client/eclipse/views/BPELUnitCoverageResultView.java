package net.bpelunit.framework.client.eclipse.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import net.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import net.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * @author Alex Salnikow
 * 
 */
public class BPELUnitCoverageResultView extends ViewPart implements Observer {

	private CoverageModel model;

	private List fileList;

	private boolean initialized = false;

	private Table tableTestCases;

	private Table table;

	private Text text;

	private Composite fParent;

	@Override
	public void createPartControl(Composite parent) {
		fParent = parent;
		GridData gData;
		
		parent.setLayout(new GridLayout(3, false));

		Label fInfoLabel = new Label(parent, SWT.LEFT);
		fInfoLabel.setText("BPELUnit Test Coverage");
		gData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gData.horizontalSpan = 3;
		fInfoLabel.setLayoutData(gData);
		
		this.text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
		gData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gData.horizontalSpan = 3;
		this.text.setLayoutData(gData);

		Label labelFilesInArchive = new Label(parent, SWT.CENTER);
		labelFilesInArchive.setText("BPELFiles in archive");
		gData = new GridData();
		gData.verticalAlignment = GridData.END;
		labelFilesInArchive.setLayoutData(gData);

		Label labelTestCoverage = new Label(parent, SWT.CENTER);
		labelTestCoverage.setText("Test coverage");
		gData = new GridData();
		gData.verticalAlignment = GridData.END;
		labelTestCoverage.setLayoutData(gData);

		Label labelTestCases = new Label(parent, SWT.CENTER);
		labelTestCases.setText("Test cases");
		gData = new GridData();
		gData.verticalAlignment = GridData.END;
		labelTestCases.setLayoutData(gData);

		fileList = new List(parent, SWT.V_SCROLL | SWT.MULTI
				| SWT.H_SCROLL | SWT.BORDER);
		gData = new GridData(GridData.FILL_BOTH);
		gData.grabExcessVerticalSpace = true;
		gData.grabExcessHorizontalSpace = true;
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
		
		initializeTable(parent);
		gData = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gData);

		tableTestCases = new Table(parent, SWT.BORDER
				| SWT.VIRTUAL | SWT.CHECK);
		tableTestCases.setLayout(new GridLayout(1, false));
		tableTestCases.setBackground(parent.getBackground());
		gData = new GridData(GridData.FILL_BOTH);
		tableTestCases.setLayoutData(gData);

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

		initializeElements();
		model = new CoverageModel();
		model.addObserver(this);
	}

	private Table initializeTable(Composite parent) {
		table = new Table(parent, SWT.BORDER | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn col1 = new TableColumn(table, 0);
		col1.setText("Metric");
		col1.setWidth(180);
		col1.setMoveable(false);
		TableColumn col2 = new TableColumn(table, SWT.CENTER);
		col2.setText("total");
		col2.setWidth(100);
		col2.setMoveable(false);
		TableColumn col3 = new TableColumn(table, SWT.CENTER);
		col3.setText("tested");
		col3.setWidth(100);
		col3.setMoveable(false);
		TableColumn col4 = new TableColumn(table, 0);
		col4.setText("           %");
		col4.setWidth(100);
		col4.setMoveable(false);

		return this.table;
	}

	private void initializeElements() {
		text.setText("Info: ");
		fileList.removeAll();
		tableTestCases.removeAll();
		table.removeAll();
	}

	@Override
	public void setFocus() {
		// nothing to be done
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
				TableItem item = new TableItem(tableTestCases, SWT.NULL);
				item.setText(iter.next());
			}
			String infos = model.getInfos();
			if (infos != null && !infos.equals("")) {
				text.append(infos);
			} else {
				text.append("Test coverage is successful measured.");
			}
			initialized = true;
			fileList.selectAll();
			model.selectedFiles = fileList.getSelection();
			for (int i = 0; i < tableTestCases.getItemCount(); i++) {
				TableItem item = tableTestCases.getItem(i);
				item.setChecked(true);
				model.checkedTestCases.add(item.getText());
			}
		}
		// text.pack(true);

		table.removeAll();
		java.util.List<String[]> list = model.getCurrentData();
		for (Iterator<String[]> iter = list.iterator(); iter.hasNext();) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(iter.next());
		}
		fParent.pack();
	}

	public void setData(java.util.List<String> testCases,
			java.util.List<IFileStatistic> statistics, String string) {
		initialized = false;
		model.setData(testCases, statistics, string);

	}

	public void initialize() {
		model.initialize();
		initializeElements();
	}

	class CoverageModel extends Observable {

		private java.util.List<String> testCases;

		private String[] selectedFiles;

		private Set<String> checkedTestCases;

		private String info = null;

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
				java.util.List<IFileStatistic> statistics, String string) {
			init();
			this.info = string;
			if (statistics != null) {
				this.testCases = testCases;

				for (Iterator<IFileStatistic> iter = statistics.iterator(); iter
						.hasNext();) {
					IFileStatistic fileStatistic = iter.next();
					fileStatistics.put(fileStatistic.getBPELFilename(),
							fileStatistic);
				}
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
				relativ = Float
						.toString((tested * 1000 / total) / (float) 10.0)
						+ "%";
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

		public String getInfos() {
			return info;
		}

		public void initialize() {
			testCases = new ArrayList<String>();
			fileStatistics = new Hashtable<String, IFileStatistic>();
			checkedTestCases = new HashSet<String>();
			selectedFiles = new String[] {};
		}
	}

}
