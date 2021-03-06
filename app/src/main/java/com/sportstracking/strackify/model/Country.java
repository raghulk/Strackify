package com.sportstracking.strackify.model;

/**
 * strackify: model class for country
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */


public class Country {
    private String countryName;
    private String countryCode;
    private String countryUnicode;
    private String countryEmoji;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryUnicode() {
        return countryUnicode;
    }

    public void setCountryUnicode(String countryUnicode) {
        this.countryUnicode = countryUnicode;
    }

    public String getCountryEmoji() {
        return countryEmoji;
    }

    public void setCountryEmoji(String countryEmoji) {
        this.countryEmoji = countryEmoji;
    }
}
