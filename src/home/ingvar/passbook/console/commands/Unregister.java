package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

public class Unregister extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		UserDAO userDAO = (UserDAO) params.get(Parameter.USER_DAO);
		User user = (User) params.get(Parameter.USER);
		String password = (String) params.get(Parameter.PASSWORD);
		if(!user.getPassword().equals(password)) {
			throw new CommandException("Password incorrect!");
		}
		try {
			userDAO.delete(user);
			Command logout = newInstance("logout");
			logout.execute(params);
		} catch(ResultException e) {
			throw new CommandException(e);
		} catch(InstantiationException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected Parameter[] requiredParams() {
		return new Parameter[] {Parameter.PASSWORD};
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Delete registered user";
	}

}
