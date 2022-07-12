import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 */

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 *
 */
public class Claim {
	
	Scanner scan = new Scanner(System.in);
	private String claimNumber;
	private LocalDate dateAccident;
	private String addressAccident;
	private String descriptionAccident;
	private String damageVehicle;
	private String claimNum;
	private final String hdg_fmt_claim= "%-11s %-11s %-17s %-22s %-30s %-26s %-18s\n";
	private String Data_fmt_claim = "%-11s %-11s %-17s %-22s %-30s %-26s %-18s\n";
	private String seperator = "----------------------------------------------------------------------"
		  		+ "------------------------------------------------------------------------------";
	PASAppHelperClass help = new PASAppHelperClass();
	private int policyNum;
	private String expiDate;
	private String effectDate;
	double roundCostRepair;
	
	
	public String getClaimNumber() {			//generate a claim number
		Random randomNum = new Random();
	    int number = randomNum.nextInt(99999);	// this will convert any number sequence into 5 character.		
	    String claimNum = String.format("%05d", number);
	    claimNumber = "C"+claimNum;
	    System.out.println("--------------------------------\n" + "Your Claim Number is: "+claimNumber + "\n--------------------------------\n");
		return claimNumber;
	}
	
	public boolean getNameandPolicyNum(){			// method to get the policy number of the user and use the policy number to retrieve the name of policy holder sa welcome display
		boolean result = help.getNameUsingPolicyNumber();
		boolean getNameandPolicyNumResult;
		if(result == true) {
			double doublePolicyNum = help.getPolicyNum();
			this.policyNum = (int)doublePolicyNum;			// the getPolicyNum method is returning  a double data type - convert the double to int for policy num
			getNameandPolicyNumResult = true;
	}
		else {
			getNameandPolicyNumResult = false;
		}
		
		return getNameandPolicyNumResult;
	}
	
	public LocalDate dateAccident() {				// method to prompt the user to input the date of accident
		
		int repeatInp = 0;
		String dateAcc;
		while(!(repeatInp ==2)) {
			System.out.print("Please enter the date of accident happened: [YYYY-MM-DD]: ");
			dateAcc = scan.nextLine();
			if(help.isDateValid(dateAcc) == 2 ||help.isDateValid(dateAcc) == 1 ) {
				dateAccident= help.getDatevalue();
				repeatInp =2;
			}
			else{				
				System.out.println("NOTICE: INVALID DATE! Date of accident cannot be future date. \n");
			}
		}
		return dateAccident;
	}
	
	public String addressAccident() {				// method to prompt the user to input the address of accident
		System.out.print("Please input the Address where Accident happened: ");
		addressAccident = scan.nextLine();
		return addressAccident;
	}
	
	public String descriptionAccident() {			// method to prompt the user to input the description of accident
		System.out.print("Please input the description of accident: ");
		descriptionAccident = scan.nextLine();
		return descriptionAccident;
	}
	
	public String damageVehicle() {					// method to prompt the user to input the damage of vehicle
		System.out.print("Please input the description of damage to Vehicle: ");
		damageVehicle = scan.nextLine();
		return damageVehicle;
	}
	public double costRepair() {					// method to prompt the user to input the cost of repair
		
		String stringCost = null;
		boolean repeatInput = true;		// initial
		while(repeatInput) {
			System.out.print("Please input the estimated cost of repair: ");
			stringCost = scan.nextLine();
			System.out.println("\n");
			if(help.isNumberValid(stringCost, 0, 99999999)){
			//	help.checkDecimal(option);
				repeatInput = false;
			}
		}
		double costRepair = Double.parseDouble(stringCost);
		double roundCostRepair = Math.round(costRepair*100.0)/100.0;
		this.roundCostRepair = roundCostRepair;
		return costRepair;
				
	}
	
	public void Summary() {						// method to print out the summary of the claim details
		
		System.out.printf(seperator + "\n"+hdg_fmt_claim,"Policy No.", "Claim No.", "Date of Accident", "Address of Accident", "Description of Accident", 
				"Damage to Vehicle", "Estimated Cost of Repair" + "\n" + seperator);
		System.out.printf(Data_fmt_claim, policyNum,claimNumber, dateAccident, addressAccident, descriptionAccident,damageVehicle, roundCostRepair);
		System.out.println("\n");
	}
	
	public void storeDB() {						// method to store all the claim detail to database
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();

				
				String claimQuery = "INSERT INTO claims (policy_number,claim_number,date_accident,address_accident,description_accident,damage_vehicle,cost_repair) "
						+ "VALUES ('"+policyNum+"','"+claimNumber+"','"+dateAccident+"','"+addressAccident+"','"+descriptionAccident+"','"+damageVehicle+"','"+roundCostRepair+"')";
				stmt.executeUpdate(claimQuery);
						
		}catch(SQLException ex) {
		    ex.printStackTrace();

		}
	}
	
	public boolean searchClaimName() {				// method to search the name of the policy holder using claim number to serve as welcome display
		boolean searchnameResult = true;
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
		
		System.out.print("Please Enter your CLAIM NUMBER: ");
		String claimNum = scan.next();
		this.claimNum = claimNum;
		
		String getHolderName = "select * from claims join policy on claims.policy_number = policy.policy_number where claims.claim_number = '"+claimNum+"';";
		ResultSet rsetSearchName = stmt.executeQuery(getHolderName);
		if(rsetSearchName.next()) {
			String polName = rsetSearchName.getString("policy_holder");
		System.out.println("Welcome, "+ polName+ "\n");
		searchnameResult = true;
		}
		else {
			System.out.println("Notice: Claim Number doesn't exist! \n");
			searchnameResult = false;
		}
		}catch(SQLException ex) {
		    ex.printStackTrace();
		}
		return searchnameResult;
	}
		
	public void searchClaim() {						// method to search the claim number. This method is use in option #7 to search claim details
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
		String searchClaim = "select * from claims where claim_number='" +claimNum+"';";
		 ResultSet rsetSearchClaim = stmt.executeQuery(searchClaim);
		 if(rsetSearchClaim.next()) {
			 String polNum = rsetSearchClaim.getString("policy_number");
			 String claim = rsetSearchClaim.getString("claim_number");
			 String dateAccident = rsetSearchClaim.getString("date_accident");
			 String addressAcc = rsetSearchClaim.getString("address_accident");
			 String descriptionAcc = rsetSearchClaim.getString("description_accident");
			 String damageVehicle = rsetSearchClaim.getString("damage_vehicle");
			 String costRepair = rsetSearchClaim.getString("cost_repair");
			 
			 System.out.printf(seperator + "\n" +hdg_fmt_claim,"Policy No.", "Claim No.", "Date of Accident", "Address of Accident", "Description of Accident", 
						"Damage to Vehicle", "Estimated Cost of Rapair" + "\n"+ seperator);
			System.out.printf(Data_fmt_claim,polNum ,claim,dateAccident,addressAcc, descriptionAcc,damageVehicle,costRepair);
			System.out.println();
		 }
		 else {
			 System.out.println("Notice: Claim Number doesn't exist! \n");
		 }
	}catch(SQLException ex) {
	    ex.printStackTrace();
		}
	}
	
	//method to check if the policy is expired or not. If the policy is expired, user can no longer file a claim
	public boolean checkExpiDate() {						
															
		boolean result = true;
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
				
			String checkExpiDate = "select expiration_date,effective_date from policy where policy_number = '"+policyNum+"';";
			ResultSet rsetgetExpiDate = stmt.executeQuery(checkExpiDate);
			while(rsetgetExpiDate.next()){
				 expiDate = rsetgetExpiDate.getString("expiration_date");
				 effectDate = rsetgetExpiDate.getString("effective_date");
				}		
			 
				LocalDate effectiveDate = LocalDate.parse(effectDate);
				LocalDate expirationDate = LocalDate.parse(expiDate);

				if(LocalDate.now().isBefore(expirationDate) && LocalDate.now().isAfter(effectiveDate)) {
					result = true;
					
				}
				else if(LocalDate.now().isAfter(expirationDate)){		
					System.out.println("Policy Expiration date is: "+expiDate);
					System.out.println("NOTICE: Policy is already expired!\n");
					result = false;
					}
				else if(LocalDate.now().isBefore(effectiveDate)){
					System.out.println("Policy Effective Date is: "+effectiveDate);
					System.out.println("NOTICE: Policy is not yet effective! \n");
					result = false;
				}
			}
		catch(SQLException ex) {
		    ex.printStackTrace();

		}	
		return result;
	}
	
	public void closeScan() {
		scan.close();
	}
	
			
}
