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
	private CustomerDAO customeDAO = new CustomerDAOJdbcImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String path = request.getServletPath();
		String pathMethod = path.substring(1, path.length() - 3); // 获取方法名
		try {
			Method method = getClass().getDeclaredMethod(pathMethod, HttpServletRequest.class,
					HttpServletResponse.class);
			method.invoke(this, request, response);

		} catch (Exception e) {

			response.sendRedirect("error.jsp");
		}

	}

	/**
	 * 添加数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");

		// 判断是否重名
		long count = customeDAO.getCountWithName(name);
		if (count > 0) {
			request.setAttribute("message", "用户名" + name + "已经被占用,请重新选择");
			request.getRequestDispatcher("newCustomer.jsp").forward(request, response);
		}

		Customer customer = new Customer(name, address, phone);
		customeDAO.save(customer);

		response.sendRedirect("success.jsp");

	}

	/**
	 * 模糊查询
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");

		CriteriaCustomer criteriaCustomer = new CriteriaCustomer(name, address, phone);

		List<Customer> customers = customeDAO.getCountWithCriteriaCustomer(criteriaCustomer);

		request.setAttribute("customers", customers);

		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String id = request.getParameter("id");

		try {
			int deleteId = Integer.parseInt(id);
			customeDAO.delete(deleteId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		response.sendRedirect("query.do");
	}

	/**
	 * 查询信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String forwardPath = "/error.jsp";

		// 1. 获取请求参数 id
		String idStr = request.getParameter("id");

		// 2. 调用 CustomerDAO 的 customerDAO.get(id) 获取和 id 对应的 Customer 对象 customer
		try {
			Customer customer = customeDAO.get(Integer.parseInt(idStr));
			if (customer != null) {
				forwardPath = "/updatecustomer.jsp";
				// 3. 将 customer 放入 request 中
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
			
			System.out.println(e.getMessage());
		}

		// 4. 响应 updatecustomer.jsp 页面: 转发.
		request.getRequestDispatcher(forwardPath).forward(request, response);

	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1. 获取表单参数: id, name, address, phone, oldName
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String oldName = request.getParameter("oldName");

		// 2. 检验 name 是否已经被占用:

		// 2.1 比较 name 和 oldName 是否相同, 若相同说明 name 可用.
		// 2.1 若不相同, 则调用 CustomerDAO 的 getCountWithName(String name) 获取 name 在数据库中是否存在
		if (!oldName.equalsIgnoreCase(name)) {
			long count = customeDAO.getCountWithName(name);
			// 2.2 若返回值大于 0, 则响应 updatecustomer.jsp 页面: 通过转发的方式来响应 newcustomer.jsp
			if (count > 0) {
				// 2.2.1 在 updatecustomer.jsp 页面显示一个错误消息: 用户名 name 已经被占用, 请重新选择!
				// 在 request 中放入一个属性 message: 用户名 name 已经被占用, 请重新选择!,
				// 在页面上通过 request.getAttribute("message") 的方式来显示
				request.setAttribute("message", "用户名" + name + "已经被占用, 请重新选择!");

				// 2.2.2 newcustomer.jsp 的表单值可以回显.
				// address, phone 显示提交表单的新的值, 而 name 显示 oldName, 而不是新提交的 name

				// 2.2.3 结束方法: return
				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				return;
			}
		}

		// 3. 若验证通过, 则把表单参数封装为一个 Customer 对象 customer
		Customer customer = new Customer(name, address, phone);
		customer.setId(Integer.parseInt(id));

		// 4. 调用 CustomerDAO 的 update(Customer customer) 执行更新操作
		customeDAO.update(customer);

		// 5. 重定向到 query.do
		response.sendRedirect("query.do");

	}
}
