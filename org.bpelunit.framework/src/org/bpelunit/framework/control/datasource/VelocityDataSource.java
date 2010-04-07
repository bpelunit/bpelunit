package org.bpelunit.framework.control.datasource;

import java.io.InputStream;

import org.apache.velocity.context.Context;
import org.bpelunit.framework.control.ext.IDataSource;
import org.bpelunit.framework.exception.DataSourceException;

public class VelocityDataSource implements IDataSource {

	@Override
	public int getNumberOfRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initializeContext(Context ctx, int rowIndex)
			throws DataSourceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromStream(InputStream is) throws DataSourceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(String name, String value)
			throws DataSourceException {
		// TODO Auto-generated method stub

	}

}
