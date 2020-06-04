import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BankAccount implements Serializable{

	private String accountNumber;
	private double balance;
	private double withdrawalFee;
	private double annualInterestRate;
	private ArrayList<Transaction> transaction = new ArrayList<Transaction>();

	public BankAccount(String accountNumber){
		
		this.accountNumber = accountNumber;
		balance = 0;
		withdrawalFee = 0;
		annualInterestRate = 0;
		
	}
	public BankAccount(String accountNumber, double balance){
			
		this.accountNumber = accountNumber;
		this.balance = balance;
		withdrawalFee = 0;
		annualInterestRate = 0;
		
	}
	public BankAccount(String accountNumber, double balance, double withdrawalFee, double annualInterestRate){
		
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.withdrawalFee = withdrawalFee;
		this.annualInterestRate = annualInterestRate;
		
	}
	public String getAccountNumber(){
		
		return accountNumber;
		
	}
	public double getBalance(){
		
		return balance;
		
	}
	public double getAnnualInterestRate(){
		
		return annualInterestRate;
		
	}
	public double getWithdrawalFee(){
	
	return withdrawalFee;
	
	}
	public void setWithdrawalFee(double withdrawalFee){
		
		this.withdrawalFee = withdrawalFee;
	}
	public void setAnnualInterestRate(double annualInterestRate){
		
		this.annualInterestRate = annualInterestRate;
	}
	public void deposit(double amount){

		this.deposit(LocalDateTime.now(), amount, null);
		
	}
	public void withdraw(double amount){
		
		this.withdraw(LocalDateTime.now(), amount, null);
		
	}
	public void deposit(LocalDateTime transactionTime, double amount, String description){

		this.balance = this.balance + amount;
		insertTransaction(transactionTime, amount, description);
		
	}
	public void deposit(double amount, String description){

		this.deposit(LocalDateTime.now(), amount, description);
		
	}
	public void withdraw(LocalDateTime transactionTime, double amount, String description){
		
		this.balance = this.balance - amount - this.withdrawalFee;
		insertTransaction(transactionTime, amount, description);
		
	}
	public void withdraw(double amount, String description){
		
		this.withdraw(LocalDateTime.now(), amount, description);
		
	}
	public ArrayList<Transaction> getTransactions(LocalDateTime startTime, LocalDateTime endTime){

		int indexStartTime = 0;
		int indexEndTime = 0;
		
		for(int i = 0; i < transaction.size() - 1; i++){
			if(transaction.get(i).getTransactionTime().equals(startTime)){
				indexStartTime = i;
			}else if (startTime == null){
				indexStartTime = 0;
			}if (transaction.get(i).getTransactionTime().equals(endTime)){
				indexEndTime = i + 1;
			}else if (endTime == null){
				indexEndTime = transaction.size();
				}
			}
		if (transaction.size() == 0){
			return null;
		}else{
			return new ArrayList<Transaction>(transaction.subList(indexStartTime, indexEndTime));
			}
	}
	private void insertTransaction(LocalDateTime transactionTime, double amount, String description){
		
		int i = 0;
		if (transaction.size() > 0){
			for (i = 0; i < transaction.size(); i++){
				if (transaction.get(i).getTransactionTime().isAfter(transactionTime)){
					this.transaction.add(i, new Transaction(transactionTime, amount, description));
					break;
				}
			}
			if (i == transaction.size()){
				this.transaction.add(new Transaction(transactionTime, amount, description));
			}
		}else{
			transaction.add(new Transaction(transactionTime, amount, description));
		}
		
	}
	
	
	public boolean isOverDrawn(){
		
		if(this.balance < 0){
			return true;
			
		}else{
			return false;
		}
	}
	public String toString(){
		
		if (this.isOverDrawn() == true){
			return String.format("BankAccount %s: ($%.2f)", accountNumber, -balance);
		}
		else{
			return String.format("BankAccount %s: $%.2f", accountNumber, balance);
		}
	}
}
