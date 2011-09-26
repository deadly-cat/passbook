package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;


public class Login extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		UserDAO userDAO = (UserDAO) params.get("userDAO");
		User user = (User) params.get("user");
		
		String username = (String) params.get("username");
		String password = (String) params.get("password");
		try {
			User login = userDAO.get(username, password);
			user.fill(login);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected String[] requiredParams() {
		return new String[] {"username", "password"};
	}
	
	@Override
	public String toString() {
		return "Log in as user";
	}
	
}
