package farm.customer;

import java.util.*;

import farm.core.*;

/**
 * A class where farmers store their customers' details.
 * Keeps track of all the customers that come and visit the Farm.
 */
public class AddressBook {

    /**
     * List of customers.
     **/
    private final List<Customer> customers;

    /**
     * Create a new AddressBook instance.
     **/
    public AddressBook() {
        this.customers = new ArrayList<>();
    }

    /**
     * Add a new customer to the address book.
     **/
    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        // If customer already exists, throw exception message of Customers representation.
        if (containsCustomer(customer)) {
            throw new DuplicateCustomerException("Duplicate customer: " + customer.toString());
        }
        customers.add(customer);
    }

    /**
     * Check to see if a customer is already in the address book.
     **/
    public boolean containsCustomer(Customer customer) {
        return customers.contains(customer);
    }

    /**
     * Retrieve all customer records stored in the address book.
     **/
    public List<Customer> getAllRecords() {
        return new ArrayList<>(customers);
    }

    /**
     * Lookup a customer in address book, if they exist using their details.
     **/
    public Customer getCustomer(String name, int phoneNumber) throws CustomerNotFoundException {
        // For each customer in (list) customers, return the customer with matching name & phone.
        for (Customer customer : customers) {
            if (customer.getName().equals(name) && customer.getPhoneNumber() == phoneNumber) {
                return customer;
            }
        }
        // Else return customer not found exception.
        throw new CustomerNotFoundException("Customer not found: " + name + ", " + phoneNumber);
    }
}
