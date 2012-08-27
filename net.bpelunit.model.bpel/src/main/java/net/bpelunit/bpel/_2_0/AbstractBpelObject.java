package net.bpelunit.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TDocumentation;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TExtensibleElements;

import net.bpelunit.bpel.IActivity;
import net.bpelunit.bpel.IBpelObject;

abstract class AbstractBpelObject implements IBpelObject {

	protected List<Documentation> documentations = new ArrayList<Documentation>();
	private TExtensibleElements nativeElement;

	public AbstractBpelObject(TExtensibleElements t) {
		if (t == null) {
			throw new NullPointerException("Wrapped activity must not be null!");
		}

		this.nativeElement = t;

		for (TDocumentation d : nativeElement.getDocumentation()) {
			documentations.add(new Documentation(d));
		}
	}

	@Override
	public BpelFactory getFactory() {
		return BpelFactory.getInstance();
	}

	void checkForCorrectModel(IBpelObject o) {
		if (!(o instanceof AbstractBpelObject)) {
			throw new IllegalArgumentException(o.getClass()
					+ " is not supported by the BPEL 2.0 facade.");
		}
	}

	AbstractActivity<?> checkForCorrectModel(IActivity a) {
		try {
			return (AbstractActivity<?>) a;
		} catch (Exception e) {
			throw new IllegalArgumentException(a.getClass()
					+ " is not supported by the BPEL 2.0 facade.");
		}
	}

	@Override
	public List<Documentation> getDocumentation() {
		return Collections.unmodifiableList(documentations);
	}

	@Override
	public Documentation addDocumentation() {
		TDocumentation bpelDoc = new TDocumentation();

		Documentation bpelDocumentation = getFactory().createDocumentation(
				bpelDoc);

		nativeElement.getDocumentation().add(bpelDoc);
		this.documentations.add(bpelDocumentation);

		return bpelDocumentation;
	}
	
	@Override
	public String getXPathInDocument() {
		return null;
	}
}
