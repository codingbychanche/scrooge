package berthold.scrooge;

/*
 * ExpensesData.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/8/19 7:47 PM
 */

public class ExpensesData {
    private int     key1,ke1OfAssociatedChallenge;
    private float   amountOfMoneySpend;
    private String  moneySpendFor,date;

    public ExpensesData(int key1,int key1OfAssociatedChallenge, float amountOfMoneySpend, String moneySpendFor, String date) {
        this.key1 = key1;
        this.ke1OfAssociatedChallenge=key1OfAssociatedChallenge;
        this.amountOfMoneySpend = amountOfMoneySpend;
        this.moneySpendFor = moneySpendFor;
        this.date = date;
    }

    public int getKey1() {
        return key1;
    }

    public int getKe1OfAssociatedChallenge() {
        return ke1OfAssociatedChallenge;
    }

    public float getAmountOfMoneySpend() {
        return amountOfMoneySpend;
    }

    public String getMoneySpendFor() {
        return moneySpendFor;
    }

    public String getDate() {
        return date;
    }
}

