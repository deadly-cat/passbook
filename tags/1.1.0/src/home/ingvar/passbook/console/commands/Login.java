package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;


public class Login extends Command {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		UserDAO userDAO = (UserDAO) params.get(Parameter.USER_DAO);
		User user = (User) params.get(Parameter.USER);
		
		String username = (String) params.get(Parameter.USERNAME);
		String password = (String) params.get(Parameter.PASSWORD);
		try {
			User login = userDAO.get(username, password);
			user.fill(login);
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
		return null;
	}
	
	@Override
	public String toString() {
		return "Log in as user";
	}
	
}
