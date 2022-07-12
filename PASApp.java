import java.util.Scanner;

/**
 * @author CamilleDayao
 * *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 *
 */

public class PASApp{

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		final String resultHdn = "--------------------------------------------------------";
		double option = 0; 															//initial value of option;
		PASAppHelperClass helper = new PASAppHelperClass();								//new object helper
		do {																		//input validation
			String stringOption = null;		
			boolean repeatInput = true;												// initial value of repeat variable
			while(repeatInput) {											
				helper.Menu();														//main menu display method
				stringOption = scan.nextLine();
				System.out.println("\n");
				if(helper.isNumberValid(stringOption, 1, 8)){						//the method will validate if the input value is within the range
					helper.checkDecimal(option);									//validation if user input a decimal value
					repeatInput = false;											//if the statement satify all the requirement, then it will return false to stop the loop
				}
				else {
					repeatInput =true;												//if statement didn't meet the requirement, it will return true and continue with loop
				}
			}
			option = Double.parseDouble(stringOption);								//convertion of string to double
			
			if(option == 1) {										// option 1
				CustomerAccount create = new CustomerAccount();		//new object
				helper.opt1display();								//display the title of option1 
				create.firstName();									// method to prompt account first name
				create.lastName();									// method to prompt account last name
				create.Address();									// method to prompt account address
				create.AccountNumber();								// method to prompt account number
				create.Summary();									// to display the summary of account details
				create.storeDB();									// trigger method to store the account details in database
				helper.loader();									// loader method
			}
			else if(option == 2) {									// option 2
				Policy policy = new Policy();						//new object
				helper.opt2display();								//display the title of option2
				policy.getAccNum();									//method to prompt to get the account number
				if(policy.dataNull() == true) {						// method to check if the account number is existing or not.  
					policy.retrieveName();							//method to retrieve the name of the account as welcome greetings	
					policy.effectiveDate();							// method to prompt policy effective date
					policy.expirationDate();						// method to prompt policy expiration date
					policy.policyNum();								// method to generate 6 digit policy number
					if(policy.policyHolder() == true){				// method to prompt the all policy holder needed details
						if(policy.Vehicles() == true){				// method to prompt the all vahicle needed details
							policy.retrieveVehicle();				// method to retrieve all the saved vehicle data to use as display
							double result = policy.PolicyNego();	//method to ask the user if the will accept the computed quotation
							if(result ==1) {							
								policy.storeDB();					//if user accepted the quotation, all the details will be stored in database
					}
					else if(result ==2) {
						policy.deleteVehicleDB();					//if user decline the quotation, vehicle details will be deleted
						policy.deletePolicyHolderDB();				//if user decline the quotation, policy holder details will be deleted
							}
						}
					}
				}
				else {												//If the account number is not existing, the message will display the output
					System.out.println(resultHdn+"\nPlease create an ACCOUNT FIRST at OPTION #1. Thank you!\n"+resultHdn+"\n");
					}
				helper.loader();									// loader method
			}
			else if(option ==3) {									// option 3
				Policy cancel = new Policy();						//new object
				helper.opt3display();								//display the title of option3
				if(cancel.PolicyDetails() == true) {				//prompt user to input the existing policy number and check if the policy number exist
					if(cancel.CancelPolicy() ==true) {				//prompt user to enter new expiration date
						helper.viewUpdatedDate();					/*once the new expiration date has been saved, this method retrieve all the update details
																	from the database and print it out*/
					}
				}
				helper.loader();									// loader method
			}
			else if(option==4) {									//option 4
				Claim claim = new Claim();							//new object
				helper.opt4display();								//display the title of option4
				if(claim.getNameandPolicyNum() == true) {			//prompt user to input the existing policy number and check if the policy number exist
					if(claim.checkExpiDate() == true) {				//once the policy nunber has been checked, this method will check if the expiration date of the policy is still valid
				claim.dateAccident();								// once the expi date of policy is validated. This method will ask the user for date of accident
				claim.addressAccident();							//prompt the user for address of accident
				claim.descriptionAccident();						//prompt the user for description of accident
				claim.damageVehicle();								//prompt the user for the damage to vehicle
				claim.costRepair();									//prompt the user for the cost of repair
				claim.getClaimNumber();								//once the details has been gathered, this method will generate a claim number
				claim.Summary();									//to display all the details regarding claim
				claim.storeDB();									//to store all the details to database
				}
				}
				helper.loader();									// loader method
			}
			else if(option ==5) {									//option 4
				helper.opt5display();								//display the title of option5
				CustomerAccount search = new CustomerAccount();		//new object
				if(search.searchAccName() == true) {				//prompt the user for first name and last name of account and validate if the account is exising
					if(search.checkResult() ==false) {				// this method will check if the account has a policy or not
						search.searchAccNoPolicy();					// if not, this method will display the summary without policy
					}
					else {
						search.searchAcc();							//if yes, this methos will display the summary of the account details with policy
					}
				}
				helper.loader();									//loader method
			}
			else if(option ==6) {									// option 6
				helper.opt6display();								//display the title of option6
				Policy searchPolicy = new Policy();					//new object
				searchPolicy.PolicyDetails();						/*this method will prompt user to input policy number. The policy number will be validated if it is existing on DB or not
																	If the policy is existing in DB it will display all the policy details. */
				helper.loader();									//loader method		
			}
			else if(option ==7) {									//option 7
				Claim searchClaim = new Claim();					// new object
				helper.opt7display();								//display the title of option7
				if(searchClaim.searchClaimName() == true) {			//the method will prompt user to input claim number, if claim number is correct, it will return true
					searchClaim.searchClaim();						//once the claim num has been validated, this methos will search and display the details of the claim
				}
				helper.loader();									// loader method
			}
		}
		while(!(option == 8));										//option8
		if(option ==8) {									
			System.out.println("THANK YOU! BYE BYE!");							//print out message
			System.exit(0);
			scan.close();											//close scanner in this class
			helper.closeScan();										//this method has closes the scanner in all the clasess except main class
			}
		}
}
	