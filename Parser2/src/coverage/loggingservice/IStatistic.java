package coverage.loggingservice;

import java.util.List;

public interface IStatistic {

	public int getTotalNumber();

	public int getTestedNumber();

	public List<IStatistic> getSubStatistics();

	public void addSubStatistik(IStatistic statistic);

	public void setTestedNumber(int testedNumber);

	public void setTotalNumber(int totalNumber);

}
