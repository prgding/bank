package service;

import exceptions.MoneyNotEnough;
import exceptions.UnknownException;

public interface AccountService {
	void transfer(String from, String to, double money) throws MoneyNotEnough, UnknownException;
}
