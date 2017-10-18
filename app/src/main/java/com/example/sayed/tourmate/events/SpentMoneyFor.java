package com.example.sayed.tourmate.events;

/**
 * Created by nurud on 10/18/2017.
 */

public class SpentMoneyFor {
    private String spentSector;
    private String spentMoney;

    public SpentMoneyFor(String spentSector, String spentMoney) {
        this.spentSector = spentSector;
        this.spentMoney = spentMoney;
    }

    public SpentMoneyFor() {
    }

    public String getSpentSector() {
        return spentSector;
    }

    public void setSpentSector(String spentSector) {
        this.spentSector = spentSector;
    }

    public String getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(String spentMoney) {
        this.spentMoney = spentMoney;
    }
}
