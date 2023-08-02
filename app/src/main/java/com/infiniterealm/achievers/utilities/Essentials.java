package com.infiniterealm.achievers.utilities;

public class Essentials {
    public static String getStandard(String ID) {
        String std = ID.substring(0,1);
        String standard;
        switch (std) {
            case "J":
                 standard = "Jr. KG";
                break;
            case "S":
                standard = "Sr. KG";
                break;
            case "1":
                standard = "1st Standard";
                break;
            case "2":
                standard = "2nd Standard";
                break;
            case "3":
                standard = "3rd Standard";
                break;
            case "4":
                standard = "4th Standard";
                break;
            case "5":
                standard = "5th Standard";
                break;
            case "6":
                standard = "6th Standard";
                break;
            case "7":
                standard = "7th Standard";
                break;
            case "8":
                standard = "8th Standard";
                break;
            case "9":
                standard = "9th Standard";
                break;
            case "X":
                standard = "10th Standard";
                break;
            default:
                standard = "Pre-School";
                break;
        }
        return standard;
    }
}
