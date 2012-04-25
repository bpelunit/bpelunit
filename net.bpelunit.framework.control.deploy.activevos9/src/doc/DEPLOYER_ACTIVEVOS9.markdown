ActiveVOS 9.x Deployer
======================

Active Endpoints, Inc. is the author of the ActiveVOS commercial product - 
see www.activevos.com <http://www.activevos.com> for details.

ActiveVOS is a trademark of Active Endpoints, Inc.

The BPELUnit deployer for ActiveVOS 9.x contains WSDL files for administrative
services that are part of ActiveVOS. With the courtesy of Active Endpoints, Inc. 
BPELUnit may use these to access the ActiveVOS engine.

Configuration
-------------

By using the ActiveVOS 9.x deployer, BPELUnit is able to deploy and handle 
BPR archives that run on ActiveVOS 9.x servers.

The ActiveVOS 9.x deployer is identified as follows:
ID:    activevos9
Class: net.bpelunit.framework.control.deploy.activevos9.ActiveVOS9Deployer
Name:  ActiveVOS 9.x

Currently, the ActiveVOS 9.x deployer has the following feature set:
- Deploy: yes
- Undeploy: yes (configurable)
- Test Coverage: no
- Endpoint replacement: no

Configuration
-------------

Choose the ActiveVOS 9.x deployer either by selecting "ActiveVOS 9.x" in the 
BPELUnit test suite editor or by specifying "activevos9" as the deployer id in the
bpts file directly.

Following configuration switches are provided by the deployer:
- DeploymentLocation (required): The path to the BPR file to be deployed
- DeploymentServiceEndpoint (required, default: http://localhost:8080/active-bpel/services/ActiveBpelDeployBPR): The service endpoint of the deployment service of a running ActiveVOS 9.x server
- DeployerUserName (optional, default: bpelunit): User name for deploying. This user must have administrative rights on the ActiveVOS server if security is configured. Embedded ActiveVOS servers are configured with no security by default, so any user name will suffice. However, the user name will appear in the logs and as the deploy user.
- DeployerPassword (optional): Password for the user specified in DeployerUserName 
- DoUndeploy (required, default: false): true/false for specifying if after a successfully completed test run the process shall be undeployed and all process instances shall be removed. If there are process instances left by the test that are not terminated, undeployment will fail and report an error.
- TerminatePendingProcessesBeforeTestSuiteIsRun (required, default: false): If true, terminate ALL running process instances on the server in order to have a clean environment. DO NOT USE ON PRODUCTION SYSTEMS!!! 

Due to the default values, the only property you really need to configure in the
best case is the DeploymentLocation.

Working around missing endpoint replacement
-------------------------------------------

The ActiveVOS 9.x deployer will not transparently change the service endpoints 
at the moment. In order to avoid packaging different BPRs for unit test and live 
deployment, you can use the URN endpoint mapping to specify logical URNs in the 
PDD and map them in the URN Mappings on the server to either the BPELUnit 
service mocks or to the life services.

To do so, make sure that in the PDD the invoke handler is set to WSA-Address
(and not WSDL port) and specify a URN like urn:domain:webservice:a as the 
endpoint. Afterwards, go to the URN Mapping section in the Administrative Console
and set the URN you specified before to the real URL.

Because you can configure different URN Mappings on different servers, you can use
the same BPR for deploying to different environments (developer, test, prod, ...)