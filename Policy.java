import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 */
public class Policy{
	
	LocalDate effectiveDate,expiDate;  			//LocalDate variable
	public String stringPolicyNum;
												//vehicle string global variable
	private String make,model,fueltype,type,color;		
	private double vehicleNum,year,price,premiumCharge;					
												//policy holder variables
	private double getAccNum;
	String getAccName, holderName, holderCar;
	ResultSet rsetgetAccNum,rsetgetAccName;
												//for format
	 public final String Hdg_fmt = "%-17s%-13s%-13s%-13s%-13s%-13s%-17s%-13s%-15s";	
	 public final String Data_fmt	  = "%-17s%-13s%-13s%-13s%-13s%-13s%-17s%-13s%-15s";
	 public final String hdg_fmt_cancel= "%-13s%-13s%-23s%-17s%-20s%-17s%-17s";
	 public final String Data_fmt_cancel = "%-13s%-13s%-23s%-17s%-20s%-17s%-17s";
	 double summation;
	 String seperator = "-------------------------------------------------------"
		  		+ "---------------------------------------------------------------";
	 
	 int vehicleCounter =0;
	 double policyNum, storeSum;
	 
	 //cancel variables
	 String cExpiDate, cEffectDate;
	 
	PASAppHelperClass help = new PASAppHelperClass();
	Object [] storeArr = new Object [7];
	
	 Scanner scan = new Scanner(System.in);

	// Prompt the user to input the existing account number as the system will not allow the user to buy or to quote a policy if the user doesnt have an account.
	public void getAccNum() {
		boolean repeat = true;					//initial value
		String AccNum = null;
		while(repeat) {
			System.out.print("Please input the 4 digit ACCOUNT NUMBER: ");
			AccNum = scan.nextLine();
			if(help.isNumberValid(AccNum, 0000, 9999)){
				repeat = false;
			}
			else {
				repeat =true;
			}
		}
		getAccNum = Double.parseDouble(AccNum);
		System.out.println();
		
		//SQL connection and query to check if the inputted account number is existing on the database
		try {				
			Connection conn = DriverManager.getConnection(			// Step 1: Construct a database 'Connection' object called 'conn'
					"jdbc:mysql://localhost:3306/sample_only","root", "root123");
			Statement stmt = conn.createStatement();
			String getAccNumquery = "select account_number from customer_account where account_number = '" +getAccNum+"'";
			 rsetgetAccNum = stmt.executeQuery(getAccNumquery);		
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	/*this method is used to check if the getAccNum method will get a result or not. This method will return a boolean value. If the database has a result,
	this method will return true, else false. */
	public boolean dataNull() {
	boolean result = true;
		try {
			if(!rsetgetAccNum.next()){		//empty
				result =  false;
			}
			else if(rsetgetAccNum.next()) {		
				result =true;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//retrieve first name from customer using account number to show the name of the account for welcome display 
	public void retrieveName() {
		try {
			Connection conn = DriverManager.getConnection(			// Step 1: Construct a database 'Connection' object called 'conn'
			"jdbc:mysql://localhost:3306/sample_only","root", "root123");
			Statement stmt = conn.createStatement();
				
			String getAccNameQuery = "select first_name from customer_account where account_number='" +getAccNum+"'";
			rsetgetAccName = stmt.executeQuery(getAccNameQuery);
			while(rsetgetAccName.next()) {
					 String AccName = rsetgetAccName.getString("first_name");
					 System.out.println("Welcome, " + AccName + "!");
				}
			}
			catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	
	//auto generate a 6 digit number that will serve as the policy number
	public String policyNum() {		
		Random randomNum = new Random();
	    int number = randomNum.nextInt(999999);	// this will convert any number sequence into 4 character.		
	    stringPolicyNum = String.format("%06d", number);
		System.out.println("\n---------------------------------------\n"+"Please note of POLICY NUMBER: "+ "#"+stringPolicyNum + "\n---------------------------------------\n");
		storeArr[0] =  stringPolicyNum;
		    return stringPolicyNum;
	}
	
	//prompt user to input an effective date
	public void effectiveDate() { 
		int repeatInp = 0;
		String effectDate;
		while(!(repeatInp ==1)) {
			System.out.print("Please input effective date [YYYY-MM-DD]: ");
			effectDate = scan.nextLine();
			if(help.isDateValid(effectDate) == 1 || help.isDateValid(effectDate) == 3) {
				effectiveDate = help.getDatevalue();
				repeatInp =1;
			}
			else if(help.isDateValid(effectDate) ==2){				
				System.out.println("NOTICE: Please Input Present Date or Future Date!\n");
			}
			else if((help.isDateValid(effectDate) == 0)){
				System.out.println("NOTICE: Invalid Date! Please try again! \n");
			}
		}
	}
	
	//auto generate an expiration date(6 months after effective date) base on the given effective date 
	public LocalDate expirationDate() {		
		System.out.print("Expiration Date is [YYYY-MM-DD]: ");
		expiDate = effectiveDate.plusMonths(6);
		System.out.println(expiDate);
		storeArr [2] = expiDate;
		return expiDate;
	}
	
	//get the policy holder details from PolicyHolder Class and store into local variable + to store on data base
	public boolean policyHolder() {
		boolean checkError;
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
				
		System.out.println("== POLICY HOLDER DETAILS: ==");
		PolicyHolder policyHolder = new PolicyHolder();
		
											//get the values of policy holder and store it into a variable
		String holderFirstname = policyHolder.holderFirstName();
		String holderLastname = policyHolder.holderLastName();
		holderName = holderFirstname + " " + holderLastname;
		LocalDate birthDate = policyHolder.birthDate();
		String address = policyHolder.address();
		String licenseNum= policyHolder.licenseNum();
		LocalDate licenseDate= policyHolder.licenseDate();
		System.out.println();
		String holderName = holderFirstname+" " + holderLastname;
		storeArr [3] =  holderName;
		
		//Store the policy holder details to database
		String policyHolderquery = "INSERT INTO policy_holder(holder_firstname,holder_lastname,birthdate,address,license_num,license_date,policy_number,account_number) "
				+ "VALUES ('"+holderFirstname+"','"+holderLastname+"','"+birthDate+"','"+address+"','"+licenseNum+"','"+licenseDate+"','"+stringPolicyNum+"','"+getAccNum+"');";
		stmt.executeUpdate(policyHolderquery);
		
		checkError =true;
		
		}
		catch(SQLException ex) {
			System.out.println("System cannot process the input! FAILED to save data. Please try again!\n");
			checkError = false;
		}
		return checkError;
	}
	
	//get the vehicle details from Vehicle Class and store into local variable + to store on data base
	public boolean Vehicles() {
		boolean checkResult = true;
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
				
		System.out.println("== CAR DETAILS: ==");
		
		String vehicleNumInput = null;
		boolean repeatInput = true;		// initial
		while(repeatInput) {
			System.out.print("How many cars you want to enroll? Please enter the number:  ");
			vehicleNumInput = scan.nextLine();
			System.out.println("");
			if(help.isNumberValid(vehicleNumInput, 1, 99)){
				repeatInput = false;
				double doubleVal = help.getNumValue();
				help.checkDecimal(doubleVal);
			}
			else {
				repeatInput =true;
			}
		}
		vehicleNum = Double.parseDouble(vehicleNumInput);
		
		
		for(int i = 1; i<=vehicleNum ; i++) {
		Vehicle vehicle = new Vehicle();
		
		//get the values of the vaehicle and store it into a variable
		System.out.println("\n== Please Enter information of Car #" + (i + " =="));
		make = vehicle.make();
		model = vehicle.model();
		holderCar = make + " " + model;				// for policy to store car name in DB
		year = vehicle.year();
		help.checkDecimal(year);
		type = vehicle.type();
		fueltype = vehicle.fuelType();
		color = vehicle.color();
		price = vehicle.price();
		premiumCharge = vehicle.premiumCharge();
		System.out.println();
		//Store the vehicle details to database
		String vehicleQuery = "INSERT INTO vehicle (make,model,car_year,car_type,fuel_type,price,color,premium,policy_number) "
				+ "VALUES ('"+make+"','"+model+"','"+year+"','"+type+"','"+fueltype+"','"+price+"','"+color+"','"+premiumCharge+"','"+stringPolicyNum+"')";
		stmt.executeUpdate(vehicleQuery);
		checkResult = true;
		
			}
		}catch(SQLException ex) {
			System.out.println("System cannot process the input! FAILED to save Vehicle and Policy Holder Data. Please try again!\n");
			checkResult = false;
			try {
				Connection conn = DriverManager.getConnection(			
					"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
					Statement stmt = conn.createStatement();

					String deletePolHolder = "DELETE FROM policy_holder where policy_number = '"+stringPolicyNum+"';";
					stmt.executeUpdate(deletePolHolder);
					
					String deleteVehicleQuery = "DELETE FROM vehicle where policy_number = '"+stringPolicyNum+"';";
					stmt.executeUpdate(deleteVehicleQuery);
	
					}catch(SQLException e) {
						ex.printStackTrace();
					}
			//ex.printStackTrace();
		}
		return checkResult;
	}

	//retrieve the data of vehicle stored in database and display all the details to show the the total premium of the vehicle(s) for the policy quotation
	public void retrieveVehicle() {			
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();

			String retrieveVehicleQuery = "select * from vehicle where policy_number = '"+stringPolicyNum+"'";
				
			ResultSet rset = stmt.executeQuery(retrieveVehicleQuery);
				
			System.out.printf("\n"+seperator+"\n"+"\t\t\t\t\t\t TOTAL PREMIUM COMPUTATION\n"+seperator + "\n"+Hdg_fmt,"Policy Number","Make","Model","Year","Type","Fuel type","Price","Color","Premium");
			System.out.println();
			NumberFormat number = NumberFormat.getIntegerInstance();
			double summation = 0;
			vehicleCounter=0;
			while(rset.next()){
				String policyNum = rset.getString("policy_number");  
				String make = rset.getString("make");
				String model = rset.getString("model");
				String year = rset.getString("car_year");
				String type = rset.getString("car_type");
				String fuel = rset.getString("fuel_type");
				String price = rset.getString("price");
				String color = rset.getString("color");
				String stringPremium = rset.getString("premium");
				double premium = Double.parseDouble(stringPremium);
				summation = summation + premium;
					 
				System.out.printf(Data_fmt, policyNum, make,model,year,type,fuel,price,color,stringPremium);
				System.out.println();
				vehicleCounter++;
				}
			System.out.println(seperator+"\n				   		   	  		  "
			+ "The total premium of "+vehicleCounter+ " Vehicle is: $" + number.format(summation)+ "\n");
			storeArr[4] = vehicleCounter;
			storeArr[5]= String.format("%.2f" , summation);;
			}
			catch(SQLException ex) {
			ex.printStackTrace();
				}
	}
	
	/*Method to ask whether to accept or to decline the offer. If user accept the quptation - the method will return '1': 
	If not, the method will return '2'*/
	public double PolicyNego() {
		
		String quoteNego = null;
		boolean repeatInput = true;			// initial
		while(repeatInput) {
		System.out.println(seperator+ "\n		Please press [1] to ACCEPT the quotation, Press [2] to decline\n" +seperator+ "\n");
		quoteNego = scan.nextLine();
		if(help.isNumberValid(quoteNego, 1, 2)){
			repeatInput = false;
		}
		else {
			repeatInput=true;
			}
		}
		double negoResult = Double.parseDouble(quoteNego);
		return negoResult;	
	}
	
	//Store policy data once the user accepts the policy
	public void storeDB() {			
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();

			String storePolicy = "INSERT INTO policy (policy_number,effective_date,expiration_date,policy_holder,vehicle,premium,account_number)"
					+ "VALUES ('"+stringPolicyNum+"','"+effectiveDate+"','"+expiDate+"','"+holderName+"','"+vehicleCounter+"','"+storeArr[5]+"','"+getAccNum+"')";
				stmt.execute(storePolicy);
		
		System.out.println("\nQUOTATION HAS BEEN ACCEPTED! \n Policy has been SAVED! Transaction SUCCESSFUL!\n");
		
	}catch(SQLException ex) {
		ex.printStackTrace();
		}

	}
	
	// Deletion of vehicle Data in DataBase if the user decline the quotation
	public void deleteVehicleDB() {						
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
				
				String deleteVehicleQuery = "DELETE FROM vehicle where policy_number = '"+stringPolicyNum+"';";
				stmt.executeUpdate(deleteVehicleQuery);
				
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
	}
	public void deletePolicyHolderDB() {				// Deletion of vehicle Data in DataBase if the user decline the quotation
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();

				String deletePolHolder = "DELETE FROM policy_holder where policy_number = '"+stringPolicyNum+"';";
				stmt.executeUpdate(deletePolHolder);
				
				System.out.println("\nQuotation has been declined! Vehicle(s) and Policy Holder Record has been removed from the system!\n");
				
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
	}
	
	/* To retrieve and display the current values of the policy
	the method will return a boolean value base on the result of the search. If the query didn't get any result the method will return false, else true.*/
		public boolean PolicyDetails() {
			boolean getName =help.getNameUsingPolicyNumber();
			boolean getNameResult;
			if(getName == true) {
			getNameResult = true;
			double policyNum = help.getPolicyNum();
			this.policyNum= policyNum;
			System.out.printf(seperator+"\n" + hdg_fmt_cancel, "Account No.", "Policy No.", "Policy Holder","Effective Date", "Expiration Date","No. of Vehicle","Total Premium\n" 
					+ seperator + "\n");
	
			try {
				Connection conn = DriverManager.getConnection(			
					"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
					Statement stmt = conn.createStatement();
		 String policyDetails = "select * from policy where policy_number = '"+policyNum+"';";
		 ResultSet rsetgetDetails = stmt.executeQuery(policyDetails); //query execution
		
		 while(rsetgetDetails.next()) {		//get the values from database
			 String polNum = rsetgetDetails.getString("policy_number");
			 String cEffectDate = rsetgetDetails.getString("effective_date");
			 String cExpiDate = rsetgetDetails.getString("expiration_date");
			 String polHolder = rsetgetDetails.getString("policy_holder");
			 String vehicle = rsetgetDetails.getString("vehicle");
			 String premium = rsetgetDetails.getString("premium");
			 String accountNum = rsetgetDetails.getString("account_number");
			 this.cEffectDate = cEffectDate;
			 this.cExpiDate = cExpiDate;
			 help.getExpiandEffectDate(cExpiDate, cEffectDate);
			System.out.printf(Data_fmt_cancel, accountNum, polNum, polHolder,cEffectDate,cExpiDate, vehicle, premium);
			System.out.println("\n");
		 	}
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		else {
			getNameResult= false;
		}
		return getNameResult;		// return value base on the search
	}
		
	//if the return value of the previous class is true, then this class will be excuted. 
	public boolean CancelPolicy() {
		boolean cancelResult = true;
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();
			
			boolean repeatInp = true;
			String stringNewExpiDate = null;
			while(repeatInp) {
				System.out.print("\nPlease enter new Expiration Date [YYYY-MM-DD ]:");
				stringNewExpiDate = scan.nextLine();
				if(help.validateNewExpiDate(stringNewExpiDate)) {
					repeatInp = false;
				}
				else{				
					repeatInp =true;
				}
			}
			
		  LocalDate cancellationDate = LocalDate.parse(stringNewExpiDate);
		  LocalDate cancelEffectDate = LocalDate.parse(cEffectDate);
		  LocalDate cancelExpiDate = LocalDate.parse(cExpiDate);
		  if(cancellationDate.compareTo(cancelExpiDate) < 0 && cancellationDate.compareTo(cancelEffectDate)>0) {
			  String updateEffectDate = "UPDATE policy SET expiration_date = '"+cancellationDate+"' "
			  		+ "WHERE policy_number = '"+help.getPolicyNum()+"';";
			  stmt.executeUpdate(updateEffectDate);
			  System.out.println("\nEffective Date has been updated Successfully!\n");
			  cancelResult = true;
		  }
		  else {
			  System.out.println("\nInvalid Input! Please Input Date before Expiration Date\n");
			  cancelResult = false;
		  }
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
			return cancelResult;
	}
	public void closeScan() {
		scan.close();
	}
}

	
		
	
	
	
