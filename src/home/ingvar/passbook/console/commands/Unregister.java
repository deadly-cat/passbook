package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

public class Unregister extends UserCommand {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		UserDAO userDAO = (UserDAO) params.get("userDAO");
		User user = (User) params.get("user");
		String password = (String) params.get("password");
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
	protected String[] requiredParams() {
		return new String[] {"password"};
	}
	
	@Override
	public String toString() {
		return "Delete registered user";
	}

}
