package com.lafed.taf.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String email;
    private String password;
    private String title;
    private String birthDate;
    private String birthMonth;
    private String birthYear;
    private String firstName;
    private String lastName;
    private String company;
    private String address1;
    private String address2;
    private String country;
    private String zipcode;
    private String state;
    private String city;
    private String mobileNumber;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getBirthMonth() { return birthMonth; }
    public void setBirthMonth(String birthMonth) { this.birthMonth = birthMonth; }
    public String getBirthYear() { return birthYear; }
    public void setBirthYear(String birthYear) { this.birthYear = birthYear; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }
    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public Map<String, Object> toFormParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("title", title);
        params.put("birth_date", birthDate);
        params.put("birth_month", birthMonth);
        params.put("birth_year", birthYear);
        params.put("firstname", firstName);
        params.put("lastname", lastName);
        params.put("company", company);
        params.put("address1", address1);
        params.put("address2", address2);
        params.put("country", country);
        params.put("zipcode", zipcode);
        params.put("state", state);
        params.put("city", city);
        params.put("mobile_number", mobileNumber);
        return params;
    }
}
