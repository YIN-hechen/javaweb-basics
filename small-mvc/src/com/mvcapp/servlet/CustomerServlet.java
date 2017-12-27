package com.mvcapp.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvcapp.dao.CustomerDAO;
import com.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.mvcapp.domain.CriteriaCustomer;
import com.mvcapp.domain.Customer;

public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CustomerDAO customeDAO=new  CustomerDAOJdbcImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletPath();
		String pathMethod = path.substring(1, path.length() - 3); // 获取方法名
		try {
			Method method = getClass().getDeclaredMethod(pathMethod, HttpServletRequest.class,
					HttpServletResponse.class);
			method.invoke(this, request, response);

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void add(HttpServletRequest request, HttpServletResponse response) {

		System.out.println("add");

	}

	public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String name=request.getParameter("name");
         String address=request.getParameter("address");
         String phone=request.getParameter("phone");
        
         CriteriaCustomer criteriaCustomer=new CriteriaCustomer(name,address,phone);
         
         List<Customer> customers=customeDAO.getCountWithCriteriaCustomer(criteriaCustomer);
		
         request.setAttribute("customers", customers);
         
         request.getRequestDispatcher("/index.jsp").forward(request, response);
		

	}

	public void delete(HttpServletRequest request, HttpServletResponse response) {

		System.out.println("delete");

	}

}
