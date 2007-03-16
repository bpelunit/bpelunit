package coverage.loggingservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Statistic implements IStatistic {
	
	private int totalNumber;
	
	private int testedNumber;
	
	private String name;
	
	private List<IStatistic> subStatistics=new ArrayList<IStatistic>();

	
	public Statistic(String string){
		this.name=string;
	}
	
	public Statistic(int totalNumber,int testedNamber,String name){
		this.totalNumber=totalNumber;
		this.testedNumber=testedNamber;
		this.name=name;
	}
	
	public List<IStatistic> getSubStatistics() {
		// TODO Auto-generated method stub
		return subStatistics;
	}
	
	public void addSubStatistik(IStatistic statistic){
		subStatistics.add(statistic);
	}

	public int getTestedNumber() {
		return totalNumber;
	}

	public int getTotalNumber() {
		return testedNumber;
	}

	public void setTestedNumber(int testedNumber) {
		this.testedNumber = testedNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	@Override
	public String toString() {
		String result="Statistic: "+name+"\n";
		result=result+"    Gesamtzahl: "+totalNumber+"\n";
		result=result+"    Getestetzahl: "+testedNumber+"\n";
		Iterator<IStatistic> iter=subStatistics.iterator();
		while(iter.hasNext()){
			result=result+"\n"+iter.next().toString();
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
