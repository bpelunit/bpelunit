/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import java.util.Collection;
import java.util.Map;

import net.bpelunit.framework.client.eclipse.ExtensionControl;
import net.bpelunit.framework.client.model.DataSourceExtension;
import net.bpelunit.framework.client.model.DeployerExtension;
import net.bpelunit.framework.client.model.HeaderProcessorExtension;
import net.bpelunit.framework.client.model.SOAPEncoderExtension;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.deploy.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.control.deploy.IBPELDeployer.IBPELDeployerOption;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.ConfigurationOption;
import net.bpelunit.framework.exception.SpecificationException;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 * 
 */
public class RegisteredExtensionsPreferencePage extends PreferencePage
		implements IWorkbenchPreferencePage {

	private TabFolder tabs;
	private Tree deployersTree;
	private Tree deployersOptionTree;
	private Collection<DeployerExtension> deployers;
	private Tree dataSourcesOptionTree;
	private Tree dataSourcesTree;

	public RegisteredExtensionsPreferencePage() {
		super();
	}

	@Override
	public void init(IWorkbench arg0) {
		deployers = ExtensionControl.getDeployers().values();
	}

	@Override
	protected Control createContents(Composite parent) {
		tabs = new TabFolder(parent, SWT.NONE);

		createDeployerTab();
		createHeaderProcessorTab();
		createSOAPEncodersTab();
		createDataSourcesTab();

		return tabs;
	}

	private void createDataSourcesTab() {
		TabItem tabDataSources = new TabItem(tabs, SWT.NULL);
		tabDataSources.setText("Data Sources");
		
		Composite dataSourceComposite = new Composite(tabs, SWT.NONE);
		dataSourceComposite.setLayout(new FillLayout(SWT.VERTICAL));
		tabDataSources.setControl(dataSourceComposite);
		
		createDataSourcesTree(dataSourceComposite);
		createDataSourcesOptionsTree(dataSourceComposite);
	}

	private void createDataSourcesOptionsTree(Composite dataSourceComposite) {
		dataSourcesOptionTree = createTree(dataSourceComposite);
		addTreeColumn(dataSourcesOptionTree, "Name", 100);
		addTreeColumn(dataSourcesOptionTree, "Default Value", 150);
		addTreeColumn(dataSourcesOptionTree, "Description", 300);
	}

	private void createDataSourcesTree(Composite dataSourceComposite) {
		dataSourcesTree = createTree(dataSourceComposite);
		addTreeColumn(dataSourcesTree, "ID", 100);
		addTreeColumn(dataSourcesTree, "Name", 200);
		
		Map<String, DataSourceExtension> dataSources = ExtensionControl.getDataSources();
		for (String name : dataSources.keySet()) {
			TreeItem ti = new TreeItem(dataSourcesTree, SWT.NONE);
			DataSourceExtension dataSource = dataSources.get(name);
			ti.setText(new String[] { dataSource.getId(),
					dataSource.getName() });
			ti.setData(dataSource);
		}
		
		dataSourcesTree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dataSourcesOptionTree.removeAll();
				if(dataSourcesTree.getSelectionCount() == 1) {
					DataSourceExtension ds = (DataSourceExtension)dataSourcesTree.getSelection()[0].getData();
					try {
						Class<? extends IDataSource> dsClass = ds.createNew().getClass();
						Map<String, ConfigurationOption> annotations = ExtensionRegistry.getConfigurationAnnotations(dsClass);
						
						for(String name : annotations.keySet()) {
							TreeItem ti = new TreeItem(dataSourcesOptionTree, SWT.NONE);
							ConfigurationOption annotation = annotations.get(name);
							ti.setText(new String[]{ name, annotation.defaultValue(), annotation.description() });
						}
						
					} catch (SpecificationException e1) {
						logException(e1);
					}
				}
			}
			

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void logException(SpecificationException e) {
		e.printStackTrace();
		// FIXME Log to Eclipse
	}
	
	private void createSOAPEncodersTab() {
		TabItem tabSOAPEncoders = new TabItem(tabs, SWT.NULL);
		tabSOAPEncoders.setText("SOAP Encoders");
		
		Tree soapEncodersTree = createTree(tabs);
		addTreeColumn(soapEncodersTree, "ID", 100);
		addTreeColumn(soapEncodersTree, "Name", 200);
		
		Map<String, SOAPEncoderExtension> soapEncoders = ExtensionControl.getSOAPEncoders();
		
		for (String name : soapEncoders.keySet()) {
			TreeItem ti = new TreeItem(soapEncodersTree, SWT.NONE);
			ti.setText(new String[] { soapEncoders.get(name).getId(),
					soapEncoders.get(name).getName() });
		}
		
		tabSOAPEncoders.setControl(soapEncodersTree);
	}

	private void createHeaderProcessorTab() {
		TabItem tabHeaderProcessors = new TabItem(tabs, SWT.NULL);
		tabHeaderProcessors.setText("Header Processors");

		Tree headerProcessorTree = createTree(tabs);
		addTreeColumn(headerProcessorTree, "ID", 100);
		addTreeColumn(headerProcessorTree, "Name", 200);
		
		Map<String, HeaderProcessorExtension> headerProcessors = ExtensionControl
				.getHeaderProcessors();

		for (String name : headerProcessors.keySet()) {
			TreeItem ti = new TreeItem(headerProcessorTree, SWT.NONE);
			ti.setText(new String[] { headerProcessors.get(name).getId(),
					headerProcessors.get(name).getName() });
		}
		
		tabHeaderProcessors.setControl(headerProcessorTree);
	}

	private Tree createTree(Composite parent) {
		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		return tree;
	}

	private TreeColumn addTreeColumn(Tree tree, String name, int width) {
		TreeColumn c = new TreeColumn(tree, SWT.LEFT);

		c.setText(name);
		c.setWidth(width);

		return c;
	}

	private void createDeployerTab() {
		TabItem tabDeployers = new TabItem(tabs, SWT.NULL);
		tabDeployers.setText("Deployers");

		Composite deployersComposite = new Composite(tabs, SWT.NONE);
		tabDeployers.setControl(deployersComposite);
		deployersComposite.setLayout(new FillLayout(SWT.VERTICAL));

		createDeployerTree(deployersComposite);
		createDeployerOptionTree(deployersComposite);
	}

	private void createDeployerTree(Composite deployersComposite) {
		deployersTree = createTree(deployersComposite);
		addTreeColumn(deployersTree, "ID", 100);
		addTreeColumn(deployersTree, "Deployer Name", 150);
		addTreeColumn(deployersTree, "Deploy", 150);
		addTreeColumn(deployersTree, "Measure Test Coverage", 150);
		addTreeColumn(deployersTree, "Endpoint Replacement", 150);

		for (DeployerExtension d : deployers) {
			try {
				IBPELDeployer deployer = d.createNew();
				IBPELDeployerCapabilities annotation = deployer.getClass()
						.getAnnotation(IBPELDeployerCapabilities.class);
				if (annotation != null) {
					TreeItem treeItem = new TreeItem(deployersTree, SWT.NONE);
					treeItem.setText(new String[] {
							d.getId(),
							d.getName(),
							new Boolean(annotation.canDeploy()).toString(),
							new Boolean(annotation.canMeasureTestCoverage())
									.toString(),
							new Boolean(annotation.canIntroduceMocks())
									.toString() });
					treeItem.setData(d);
				}
			} catch (SpecificationException e) {
				logException(e);
			}
		}

		deployersTree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deployersOptionTree.removeAll();

				if (deployersTree.getSelectionCount() == 1) {
					TreeItem selection = deployersTree.getSelection()[0];
					Class<? extends IBPELDeployer> deployerClass;
					try {
						deployerClass = ((DeployerExtension) (selection
								.getData())).createNew().getClass();
						Map<String, IBPELDeployerOption> options = ExtensionRegistry
								.getConfigurationAnnotations(deployerClass,
										true);

						for (String option : options.keySet()) {
							IBPELDeployerOption annotation = options
									.get(option);
							TreeItem item = new TreeItem(deployersOptionTree,
									SWT.NONE);
							item.setText(new String[] {
									option,
									annotation.defaultValue(),
									annotation.description(),
									new Boolean(annotation.testSuiteSpecific())
											.toString() });
						}

					} catch (SpecificationException e1) {
						logException(e1);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void createDeployerOptionTree(Composite deployersComposite) {
		deployersOptionTree = createTree(deployersComposite);
		addTreeColumn(deployersOptionTree, "Deployer Option", 200);
		addTreeColumn(deployersOptionTree, "Default Value", 200);
		addTreeColumn(deployersOptionTree, "Description", 200);
		addTreeColumn(deployersOptionTree, "Test Suite Only", 100);
	}

}
