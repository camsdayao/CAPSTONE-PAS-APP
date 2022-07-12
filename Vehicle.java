import java.util.Calendar;
import java.util.Scanner;

/**
 * 
 */

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 *
 */
public class Vehicle extends PolicyHolder{

	PASAppHelperClass helper = new PASAppHelperClass();		//object help has been created 				
	public static double priceRating;
	public static double yearRating;
	
	public String make() {									//prompt user for the make of the vehicle
		System.out.print("Please enter make/brand of the car: ");
		String make = scan.nextLine();
		return make;
	}
	
	public String model() {									//prompt user for the model of the vehicle
		System.out.print("Please enter model of car: ");
		String model = scan.nextLine();
		return model;
	}
	
	public double year() {										//prompt user for the year of the vehicle
		String yearString = null;
		boolean repeatInput = true;							// initial value for validation of 
		while(repeatInput) {								//while the condition is true, the loop continues
			System.out.print("Please enter year of the car [YYYY]: ");
			yearString = scan.nextLine();
			if(helper.isNumberValid(yearString, 1900, 9999)){	//setted value for the valid years that will be accepted as year of the car
				
				repeatInput = false;						//if the input value is valid the boolean will return false		
			}
			else {
				repeatInput =true;							//if the input value is invalid the boolean will return true	
			}
		}
		yearRating= Double.parseDouble(yearString);			
		 return yearRating;
	}
		
	public String type() {									//prompt user for the type of the vehicle
		System.out.print("Please enter car type [4-door sedan, 2-door sports car, SUV, or truck]: ");
		String type = scan.next() + scan.nextLine();
		return type;
	}
	public String fuelType() {								//prompt user for the Fuel type of the vehicle
		System.out.print("Please enter fuel type: ");
		String fuelType = scan.next();
		return fuelType;
	}
	public double price() {									//prompt user for the price of the vehicle
		
		String priceString = null;
		boolean repeatInput = true;		// initial
		while(repeatInput) {
			System.out.print("Please enter price of the car: ");
			priceString = scan.nextLine();
			if(helper.isNumberValid(priceString, 0, 999999999)){
				repeatInput = false;
			}
			else {
				repeatInput =true;
			}
		}
		 priceRating = Double.parseDouble(priceString);
		 return priceRating;
	}	

	public String color() {									//prompt user for the color of the vehicle
		System.out.print("Please enter car color: ");
		String color = scan.next() + scan.nextLine();
		return color;
	}
	public double premiumCharge() {							// this method will call the rating engine class to compute the total premium charge of the vehicles
		RatingEngine ratingEngine = new RatingEngine();
		double premiumRatingVehicle = ratingEngine.premiumChargeofVehicle();
		return premiumRatingVehicle;
	}
}
