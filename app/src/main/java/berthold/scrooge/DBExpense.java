package berthold.scrooge;

/*
 * DBExpense.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/28/19 3:59 PM
 */

public class DBExpense {

    private String nameOfProduct;
    private float moneySpendOnProduct;

    public DBExpense(String nameOFProduct,float moneySpendOnProduct) {
        this.nameOfProduct=nameOFProduct;
        this.moneySpendOnProduct=moneySpendOnProduct;
    }

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public float getMoneySpendOnProduct() {
        return moneySpendOnProduct;
    }

    public void setNameOfProduct(String nameOfProduct) {
        this.nameOfProduct = nameOfProduct;
    }

    public void setMoneySpendOnProduct(float moneySpendOnProduct) {
        this.moneySpendOnProduct = moneySpendOnProduct;
    }
}
