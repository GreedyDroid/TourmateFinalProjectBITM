package com.example.sayed.tourmate.events;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nurud on 10/18/2017.
 */

@SuppressWarnings("serial")
public class UserEvent implements Serializable{
    private String eventKey;
    private String location;
    private String budget;
    private String startDate;
    private String returnDate;
    private ArrayList<SpentMoneyFor> allExpenses;

    public UserEvent(String eventKey, String location, String budget, String startDate, String returnDate, ArrayList<SpentMoneyFor> allExpenses) {
        this.eventKey = eventKey;
        this.location = location;
        this.budget = budget;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.allExpenses = allExpenses;
    }

    public UserEvent() {
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public ArrayList<SpentMoneyFor> getAllExpenses() {
        return allExpenses;
    }

    public void setAllExpenses(ArrayList<SpentMoneyFor> allExpenses) {
        this.allExpenses = allExpenses;
    }
}
