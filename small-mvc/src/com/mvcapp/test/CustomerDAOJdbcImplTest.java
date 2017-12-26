package com.mvcapp.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mvcapp.dao.CustomerDAO;
import com.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.mvcapp.domain.Customer;

class CustomerDAOJdbcImplTest {

	private CustomerDAO customerDAO=new CustomerDAOJdbcImpl();
	
	@Test
	void testGetAll() {
		
	}

	@Test
	void testSave() {
		fail("Not yet implemented");
	}

	@Test
	void testGetInteger() {
		Customer customer=customerDAO.get(1);
		System.out.println(customer);
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCountWithName() {
		fail("Not yet implemented");
	}

}
