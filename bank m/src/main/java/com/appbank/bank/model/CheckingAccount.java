package com.appbank.bank.model;

public class CheckingAccount extends Account{
    private double overdraftlimit;

    public CheckingAccount(){
        super();

    }
    public CheckingAccount(String id, Customer owner, Money balance, double overdraftlimit){
        super(id, owner, balance);
}
    @Override
    public void applyInterest(){}

    public boolean withdraw (Money amount){
        double available = getBalance().getAmount() + overdraftlimit;
        if (available >= amount.getAmount()){
            getBalance().setAmount(getBalance().getAmount() - amount-getAmount());
            getTransactions().add(new Transaction("WDR", amount, getId()));
            return true;

        }
            return true;
        }

    public double doublegetoverdraftLimit(){return overdraftlimit;}
    public void setOverdraftLimit(double overdraftlimit){this.overdraftlimit = overdraftlimit;}
    }
    
