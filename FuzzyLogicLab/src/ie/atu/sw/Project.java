package ie.atu.sw;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Project {
	private static final String FCL_FILE = "./res/project";
	private FIS fis;
	private FunctionBlock fb;
	
	public Project() throws Exception{
		fis = FIS.load(FCL_FILE, true);
		fb = fis.getFunctionBlock("Project");
		
		JFuzzyChart.get().chart(fb);
	}
	
	public double getRisk(int funding, int staff) {
		fb.setVariable("funding", Double.valueOf(funding));
		fb.setVariable("staff", Double.valueOf(staff));
		fis.evaluate();
		
		Variable risk = fb.getVariable("risk");
		
		JFuzzyChart.get().chart(risk,true);
		
		JFuzzyChart.get().chart(risk, risk.getDefuzzifier(), true);
		return risk.defuzzify();
	}
	
	public static void main(String[] args) throws Exception{
		double risk = new Project().getRisk(5, 5);
		System.out.println(risk);
	}
}
