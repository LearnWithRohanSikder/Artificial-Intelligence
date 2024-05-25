package ie.atu.sw;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Tipper {
	private static final String FCL_FILE = "./res/tipper";
	private FIS fis;
	private FunctionBlock fb;
	
	public Tipper() throws Exception{
		fis = FIS.load(FCL_FILE, true);
		fb = fis.getFunctionBlock("tipper");
		
		JFuzzyChart.get().chart(fb);
	}
	
	public double getTip(int service, int food) {
		fb.setVariable("service", Double.valueOf(service));
		fb.setVariable("food", Double.valueOf(food));
		fis.evaluate();
		
		Variable tip = fb.getVariable("tip");
		
		//JFuzzyChart.get().chart(tip,true);
		
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		return tip.defuzzify();
	}
	
	public static void main(String[] args) throws Exception{
		double tip = new Tipper().getTip(5, 5);
		System.out.println(tip);
	}
}
