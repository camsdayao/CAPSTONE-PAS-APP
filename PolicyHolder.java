import java.time.LocalDate;
import java.util.Scanner;

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 *
 */
public class PolicyHolder{
	PASAppHelperClass helper = new 	PASAppHelperClass();
	private LocalDate bDate;
	private LocalDate dateLicense;
	private String holderFirstName;
	private String holderLastName;
	private String address;
	private String licenseNum;
	
	Scanner scan = new Scanner(System.in);
	public static String licenseDatetoCompute;
	
	
	public String holderFirstName() {									//prompt user for the policy holder first name 
		boolean repeatInput = true;										// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please enter First name of policy holder: ");
			String holderFirstName = scan.nextLine();
			this.holderFirstName = holderFirstName;
			if(helper.isInputNull(holderFirstName) == false){			//the method will validate if the input value is within the range
				repeatInput = false;									//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;										//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return holderFirstName;
	}
	
	public String holderLastName() {									//prompt user for the policy holder last name 
		boolean repeatInput = true;										// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please enter Last name of policy holder: ");
			String holderLastName = scan.nextLine();
			this.holderLastName = holderLastName;
			if(helper.isInputNull(holderLastName) == false){			//the method will validate if the input value is within the range
				repeatInput = false;									//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;										//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return holderLastName;
	}
		
	public LocalDate birthDate() {										//prompt user for the policy holder birthdate
		int repeatInp = 0;
		String birthDate;
		while(!(repeatInp ==2)) {
			System.out.print("Please input Birth Date [YYYY-MM-DD]: ");
			birthDate = scan.nextLine();
			if(helper.isDateValid(birthDate) == 2) {
				bDate = helper.getDatevalue();
				repeatInp =2;
			}
			else{				
				System.out.println("NOTICE: Invalid! Please try again. Please input a past date!\n");
			}
		}
		return bDate;
	}
	
	public String address() {											//prompt user for the policy holder address
		boolean repeatInput = true;										// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please enter Address: ");
			String address = scan.nextLine();
			this.address = address;
			if(helper.isInputNull(address) == false){					//the method will validate if the input value is within the range
				repeatInput = false;									//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;										//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return address;
	}
	
	public String licenseNum() {										//prompt user for the policy holder license number
		
		boolean repeatInput = true;										// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please enter License Number ");
			String licenseNum = scan.nextLine();
			this.licenseNum = licenseNum;
			if(helper.isInputNull(licenseNum) == false){				//the method will validate if the input value is within the range
				repeatInput = false;									//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;										//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return licenseNum;
	}
	
	public LocalDate licenseDate() {									//prompt user for the policy holder license date issued
		int repeatInp = 0;
		String licenseDate;
		while(!(repeatInp ==2)) {
			System.out.print("Please enter date driver's license first issued [YYYY-MM-DD]: ");
			licenseDate = scan.nextLine();
			if(helper.isDateValid(licenseDate) == 2 ||helper.isDateValid(licenseDate) == 1 ) {
				dateLicense= helper.getDatevalue();
				repeatInp =2;
			}
			else{				
				System.out.println("NOTICE: Invalid! Please try again. Please input present or past date! \n");
			}
		}
		licenseDatetoCompute = dateLicense.toString();
		return dateLicense;
	}
	public void closeScan() {
		scan.close();
	}
}
