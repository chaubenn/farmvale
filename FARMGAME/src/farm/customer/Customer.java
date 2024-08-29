package farm.customer;

import farm.sales.Cart;

/**
 * A customer who interacts with the farmer's business.
 * Keeps a record of the customer's information.
 */
public class Customer {

    /**
     * String name of Customer
     */
    private String name;

    /**
     * Integer phone number of Customer.
     */
    private int phoneNumber;

    /**
     * String address of Customer.
     */
    private String address;

    /**
     * Cart of customer.
     */
    private final Cart cart;

    /**
     * Create a new customer instance with their details.
     */
    public Customer(String name, int phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.cart = new Cart();
    }

    /**
     * Retrieve the name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the current name of the customer with a new one.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Retrieve the phone number of the customer.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the current phone number of the customer to be newPhone.
     */
    public void setPhoneNumber(int newPhone) {
        this.phoneNumber = newPhone;
    }

    /**
     * Retrieve the address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the current address of the customer to be newAddress.
     */
    public void setAddress(String newAddress) {
        this.address = newAddress;
    }

    /**
     * Retrieves the customers cart.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Returns a string representation of this customer class.
     */
    // Override default Object toString() method
    @Override
    public String toString() {
        return "Name: " + name + " | Phone Number: " + phoneNumber + " | Address: " + address;
    }

    /**
     * Determines whether the provided object is equal to this customer instance.
     */
    @Override
    // Override default Object equals() method
    public boolean equals(Object obj) {
        // If the object is not a Customer, return false. Also assign pattern variable.
        if (!(obj instanceof Customer customer2)) {
            return false;
        }
        // Customers are equal if phone numbers and names match.
        return phoneNumber == customer2.getPhoneNumber() && name.equals(customer2.getName());
    }

    /**
     * A hashcode method that respects the equals(Object) method.
     */
    // Override default Object hashCode() method
    @Override
    public int hashCode() {
        return name.hashCode() * Integer.hashCode(phoneNumber) * 17;
    }
}



