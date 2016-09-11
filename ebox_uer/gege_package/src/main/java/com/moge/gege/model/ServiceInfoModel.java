package com.moge.gege.model;

public class ServiceInfoModel
{
    private int province_id;
    private int city_id;
    private int district_id;
    private String street_id;
    private String community_id;
    private String community_name;

    /**
     * rent house
     */
    private int apartment_room;
    private int apartment_hall;
    private int apartment_washroom;

    /**
     * second hand
     */
    private int original_price;
    private int recency;
    private String classify;

    /**
     * pet
     */
    private int age;
    private int gender;
    private String breed;

    /**
     * marriage
     */
    // gender,age
    private String profession;


    /**
     *
     * together
     */

    private int indoor;

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public int getApartment_room() {
        return apartment_room;
    }

    public void setApartment_room(int apartment_room) {
        this.apartment_room = apartment_room;
    }

    public int getApartment_hall() {
        return apartment_hall;
    }

    public void setApartment_hall(int apartment_hall) {
        this.apartment_hall = apartment_hall;
    }

    public int getApartment_washroom() {
        return apartment_washroom;
    }

    public void setApartment_washroom(int apartment_washroom) {
        this.apartment_washroom = apartment_washroom;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public int getRecency() {
        return recency;
    }

    public void setRecency(int recency) {
        this.recency = recency;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }
}
