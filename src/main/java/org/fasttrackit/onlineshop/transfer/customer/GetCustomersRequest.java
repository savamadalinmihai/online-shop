package org.fasttrackit.onlineshop.transfer.customer;

public class GetCustomersRequest {

    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "GetCustomersRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
