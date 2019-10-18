package berthold.scrooge;

/*
 * ChallengeData.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/2/19 6:38 PM
 */

import android.text.Spannable;

public class ChallengeData {
    private int key1;
    private String dateStarted;
    private String dateEnded;
    private String challengeName;
    private float challengeGoal;
    private float endingBalance;
    private boolean isActive;

    public ChallengeData(int key1, String dateStarted, String dateEnded, String challengeName, float challengeGoal, float endingBalance, boolean isActive) {
        this.key1 = key1;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
        this.challengeName = challengeName;
        this.challengeGoal = challengeGoal;
        this.endingBalance = endingBalance;
        this.isActive = isActive;
    }

    public int getKey1() {
        return key1;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public float getChallengeGoal() {
        return challengeGoal;
    }

    public String getDateEnded() {
        return dateEnded;
    }

    public float getEndingBalance() {
        return endingBalance;
    }

    public boolean isActive() {
        return isActive;
    }
}
