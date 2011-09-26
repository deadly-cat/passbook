package home.ingvar.passbook.console;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

import java.util.Map;


public abstract class UserCommand extends Command {
	
	@Override
	protected void validate(Map<String, Object> params) throws CommandException {
		super.validate(params);
		UserDAO userDAO = (UserDAO) params.get("userDAO");
		User user = (User) params.get("user");
		try {
			userDAO.get(user.getUsername(), user.getPassword());
		} catch(ResultException e) {
			throw new CommandException("Permission denied");
		}
	}
	
}
