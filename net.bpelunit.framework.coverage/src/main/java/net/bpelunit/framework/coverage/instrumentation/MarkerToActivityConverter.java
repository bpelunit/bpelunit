package net.bpelunit.framework.coverage.instrumentation;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.services.marker.Mark;

public class MarkerToActivityConverter {

	private static final String INSTRUMENTATION_SCOPE_NAME = "INSTRUMENTATION_";

	public void convertMarkersToActivities(IProcess p) {
		addWsdlToProcess();
		addPartnerlinkToProcess();

	}

	private void addPartnerlinkToProcess() {
		// TODO Auto-generated method stub
		
	}

	private void addWsdlToProcess() {
		// TODO Auto-generated method stub

	}

	IScope createScopeForMarkers(IActivity a) {
		Mark e = createRequestForMarkers(a);

		IBpelFactory factory = a.getFactory();

		IScope scope = factory.createScope();
		scope.setName(INSTRUMENTATION_SCOPE_NAME + a.getName());

		ISequence sequence = factory.createSequence();
		scope.setMainActivity(sequence);
		IVariable v = scope.addVariable();
		v.setName("markRequest");
		v.setMessageType(CoverageConstants.MARKER_SERVICE_MARK_REQUEST_MESSAGE_TYPE);

		IAssign assign = factory.createAssign();
		assign.setName("Prepare_Mark_Request");
		ICopy copy = assign.addCopy();
		copy.getFrom().setLiteral(e);
		copy.getTo().setVariable(v);
		copy.getTo().setPart(CoverageConstants.MARKER_SERVICE_MARK_REQUEST_PART);
		copy.getTo().setExpression("/");
		sequence.addActivity(assign);

		IInvoke invoke = factory.createInvoke();
		invoke.setName("Invoke_Marker_Service");
		invoke.setInputVariable(v);
		invoke.setOperation(CoverageConstants.MARKER_SERVICE_MARK_OPERATION);
		invoke.setPartnerLink(CoverageConstants.MARKER_SERVICE_PARTNERLINK);
		sequence.addActivity(invoke);

		return scope;
	}

	Mark createRequestForMarkers(IActivity a) {
		Mark m = new Mark();

		if (a.getDocumentation().size() > 0) {
			List<Object> documentationElements = new ArrayList<Object>(a.getDocumentation().get(0)
					.getDocumentationElements());

			for (Object o : a.getDocumentation().get(0)
					.getDocumentationElements()) {
				if (o instanceof Marker) {
					m.getMarker().add(((Marker) o).getName());

					documentationElements.remove(o);
				}
			}

			a.getDocumentation().get(0)
					.setDocumentationElements(documentationElements);
		}
		return m;
	}
}
