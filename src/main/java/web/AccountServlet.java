package web;

import exceptions.MoneyNotEnough;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AccountService;
import service.Impl.AccountServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = "/transfer")
public class AccountServlet extends HttpServlet {

	private AccountService accountService = new AccountServiceImpl();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
			IOException {
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		double money = Double.parseDouble(request.getParameter("money"));

		try {
			accountService.transfer(from, to, money);
			response.sendRedirect("success.html");
		} catch (MoneyNotEnough e) {
			response.sendRedirect("error1.html");
		} catch (Exception e) {
			response.sendRedirect("error2.html");
		}
	}
}
