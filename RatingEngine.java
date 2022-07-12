
import java.util.Calendar;
import java.util.Formatter;

/**
 * 
 */

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company.  
 *
 */
public class RatingEngine extends Vehicle{
	
	public double vpf;
	private int dlxNum;
	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	Formatter format = new Formatter();
	
	
	
	public double premiumChargeofVehicle() {						//this is the method to compute all the premium charges of the vehicle
		int index = licenseDatetoCompute.indexOf("-");
		String stringDLX = licenseDatetoCompute.substring(0, index);		//get the year of the license date using indexOf
		
		dlxNum = Integer.parseInt(stringDLX);
		
		int dlx = currentYear - dlxNum;
	
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		double vehicleAge = currentYear - yearRating;

		if(vehicleAge<=1) {												//get the vpf using vehicle age
			vpf = 0.01;
		}
		else if(vehicleAge<=3 && vehicleAge>1) {
			 vpf = 0.008;
		}
		else if(vehicleAge<=5 && vehicleAge>3) {
			 vpf = 0.007;
		}
		else if(vehicleAge<=10 && vehicleAge>5) {
			 vpf = 0.006;
		}
		else if(vehicleAge<=15 && vehicleAge>10) {
			 vpf = 0.004;
		}
		else if(vehicleAge<=20 && vehicleAge>15) {
			 vpf = 0.002;
		}
		else if(vehicleAge<=40 && vehicleAge>20) {
			 vpf = 0.001;
		}
		else if(vehicleAge>40) {
			vpf = 0.001;
		}
		if(dlx<1) {
			dlx=1;
		}
		double PtoRoundOff = (priceRating * vpf) + ((priceRating / 100)/dlx);				//computation to get the value of P
		String stringP = String.format("%.2f" , PtoRoundOff);								//convert the P into 2 decimal places
		double P = Double.parseDouble(stringP);				
		return P;
		
	}
	}

	
	
