package es.mqm.webapp.model;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User buyer;

    @OneToOne
    private Product product;

    // delivery data (names and surnames do not necessarily need to be the same as
    // the buyer's info)
    private String name;
    private String surnames;
    private String address;
    private String apartment;
    private String zipcode;
    private String city;
    private String province;
    private String country;
    private String phone;

    private String creditCardNumber;
    private String creditCardExpiryDate;
    private String creditCardCVV;

    private double totalPrice; // includes shipping price

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updatedAt;

    public Order() {
    }

    public Order(User buyer, Product product, String name, String surnames, String address, String apartment,
            String zipcode, String city, String province, String country, String phone, String creditCardNumber, String creditCardExpiryDate,
            String creditCardCVV, double totalPrice) {
        this.buyer = buyer;
        this.product = product;
        this.name = name;
        this.surnames = surnames;
        this.address = address;
        this.apartment = apartment;
        this.country = country;
        this.zipcode = zipcode;
        this.city = city;
        this.province = province;
        this.phone = phone;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiryDate = creditCardExpiryDate;
        this.creditCardCVV = creditCardCVV;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(String creditCardExpiryDate) {
        this.creditCardExpiryDate = creditCardExpiryDate;
    }

    public String getCreditCardCVV() {
        return creditCardCVV;
    }

    public void setCreditCardCVV(String creditCardCVV) {
        this.creditCardCVV = creditCardCVV;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
