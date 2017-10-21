package com.example.sayed.tourmate.events;

import java.io.Serializable;

/**
 * Created by nurud on 10/18/2017.
 */

public class SpentMoneyFor implements Serializable{
    private String spentSector;
    private String spentMoney;
    private String spentTime;

    public SpentMoneyFor(String spentSector, String spentMoney, String time) {
        this.spentSector = spentSector;
        this.spentMoney = spentMoney;
        this.spentTime = time;
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

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }
}
