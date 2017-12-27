package com.mvcapp.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mvcapp.dao.CustomerDAO;
import com.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.mvcapp.domain.Customer;

class CustomerDAOJdbcImplTest {

	private CustomerDAO customerDAO=new CustomerDAOJdbcImpl();
	
	@Test
	void testGetAll() {
		List<Customer> customers=customerDAO.getAll();
		System.out.println(customers);
	}

	@Test
	void testSave() {
		Customer custmoer=new Customer();
		custmoer.setAddress("BeiJing");
		custmoer.setName("Mike");
		custmoer.setPhone("15165089756");
		customerDAO.save(custmoer);
		
	}

	@Test
	void testGetInteger() {
		Customer customer=customerDAO.get(1);
		System.out.println(customer);
	}

	@Test
	void testDelete() {
		customerDAO.delete(1);
		
	}

	@Test
	void testGetCountWithName() {
		long count=customerDAO.getCountWithName("ABC");
		System.out.println(count);
	}

}
