package com.appbank.bank.model;

public class SavingAccount extends Account{
 private double interestRate;

  public SavingAccount (){
  super();
 

 }

    public SavingAccount (String id, Customer owner, Money balance, double interestRate){
        Super(id,owner, balance);
        this.interestRate = interestRate;
    }
    private void Super(String id, Customer owner, Money balance) {
     
    }

    @Override
    public void applyInterest(){
         double newBalance = getBalance(). getAmount() + (getBalance().getAmount() * interestRate);
         getBalance().setAmount(newBalance);
    }
    public double getInterestRate(){
    return interestRate;
 }
    public void setInterestRate (double interestRate){this.interestRate = interestRate;}
}
