package me.ding.service;

import me.ding.exceptions.MoneyNotEnough;
import me.ding.exceptions.UnknownException;

public interface AccountService {
	void transfer(String from, String to, double money) throws MoneyNotEnough, UnknownException;
}
