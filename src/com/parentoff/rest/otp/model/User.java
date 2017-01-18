package com.parentoff.rest.otp.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ankit on 10/5/16.
 */
public class User {

    public static Logger LOGGER = LoggerFactory
            .getLogger(User.class);

    String username;
    String firstname;
    String lastname;
    String email;
    String address;
    String area;
    String city;
    String state;
    String country;


    private String newUserName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonIgnore
    public String getNewUserName() {
        return newUserName;
    }

    @JsonIgnore
    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public void updateUser(User otherUser) {

        if(null != otherUser.getUsername()){
            this.setNewUserName(otherUser.getUsername());
        }

        if(null != otherUser.getFirstname()){
            this.setFirstname(otherUser.getFirstname());
        }
        if(null != otherUser.getLastname()){
            this.setLastname(otherUser.getLastname());
        }
        if(null != otherUser.getEmail()){
            this.setEmail(otherUser.getEmail());
        }
        if(null != otherUser.getAddress()){
            this.setAddress(otherUser.getAddress());
        }
        if(null != otherUser.getArea()){
            this.setArea(otherUser.getArea());
        }
        if(null != otherUser.getCity()){
            this.setCity(otherUser.getCity());
        }
        if(null != otherUser.getState()){
            this.setState(otherUser.getState());
        }
        if(null != otherUser.getCountry()){
            this.setCountry(otherUser.getCountry());
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

