/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package net.bpelunit.framework.control.ext;

import net.bpelunit.framework.coverage.exceptions.ArchiveFileException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.EndPointException;
import net.bpelunit.framework.model.Partner;

/**
 * This interface implementations provide two services to the corresponding
 * IBPELDeployer implementation which are highly deployer specific.
 * 
 * 1. Adding the coverage measurement service to the process. This involves
 * modifying deployment archive artifacts such as BPEL file, descriptors etc.
 * Some of them are deployer specific.
 * 
 * 2. Changing the end point URL of a given partner service to the simulated URL
 * of the framework.
 * 
 * @author chamith
 * 
 */
public interface IDeployment {

	/**
	 * This method replace the existing end point URL of a partner to the
	 * simulated end point URL. The correct Partner object corresponding to the
	 * PartnerLink object should be provided. This mapping has to be known by
	 * caller of this method.
	 * 
	 * @param pl
	 *            PartnerLink object which contains the QName of service whose
	 *            end point will be changed.
	 * @param p
	 *            Partner object which contains necessary information such as
	 *            simulated URL of this partner.
	 * @throws EndPointException
	 */
	void replaceEndpoints(PartnerLink pl, Partner p)
			throws EndPointException;

	void addLoggingService(String wsdl) throws ArchiveFileException;

	/**
	 * This method returns a the details about the partnerlinks that process
	 * defines. PartnerLink object encapsulates the mapping between the
	 * partnerlink and the respective partner service. This mapping is specified
	 * using process engine specific methods and thus the implementation will be
	 * different for different deployers.
	 * 
	 * @return created PartnerLinks
	 * @throws DeploymentException 
	 */
	PartnerLink[] getPartnerLinks() throws DeploymentException;

	String getArchive();

	Partner[] getPartners();

}