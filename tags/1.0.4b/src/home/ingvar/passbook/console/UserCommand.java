package home.ingvar.passbook.console;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

import java.util.Map;


public abstract class UserCommand extends Command {
	
	@Override
	protected void validate(Map<Parameter, Object> params) throws CommandException {
		super.validate(params);
		UserDAO userDAO = (UserDAO) params.get(Parameter.USER_DAO);
		User user = (User) params.get(Parameter.USER);
		try {
			userDAO.get(user.getUsername(), user.getPassword());
		} catch(ResultException e) {
			throw new CommandException("Permission denied");
		}
	}
	
}
