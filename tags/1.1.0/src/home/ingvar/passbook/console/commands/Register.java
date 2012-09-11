package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;


public class Register extends Command {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		UserDAO userDAO = (UserDAO) params.get(Parameter.USER_DAO);
		User user = (User) params.get(Parameter.USER);
		
		String username = (String) params.get(Parameter.USERNAME);
		String password = (String) params.get(Parameter.PASSWORD);
		String fullname = (String) params.get(Parameter.FULLNAME);
		User reg = new User(username, password, fullname);
		try {
			userDAO.add(reg);
			user.fill(reg);
			System.out.println("Registration complete");
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected Parameter[] requiredParams() {
		return new Parameter[] {Parameter.USERNAME, Parameter.PASSWORD};
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return new Parameter[] {Parameter.FULLNAME};
	}
	
	@Override
	public String toString() {
		return "Add new user";
	}

}
