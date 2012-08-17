package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.transfer.User;


public class Logout extends Command {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		User user = (User) params.get(Parameter.USER);
		user.setId(0);
		user.setUsername("unreg");
		user.setPassword("");
		user.setFullname("unreg");
	}

	@Override
	protected Parameter[] requiredParams() {
		return null;
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Log out as user";
	}
	
}
