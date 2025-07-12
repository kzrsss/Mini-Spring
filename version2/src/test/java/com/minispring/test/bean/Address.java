package com.minispring.test.bean;

/**
 * 地址类
 * 用于测试 XML 配置文件解析功能
 */
public class Address {
    
    private String city;
    private String district;
    private String street;
    
    public Address() {
    }
    
    public Address(String city, String district, String street) {
        this.city = city;
        this.district = district;
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
} 