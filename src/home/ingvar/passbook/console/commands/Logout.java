package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.transfer.User;


public class Logout extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		User user = (User) params.get("user");
		user.setId(0);
		user.setUsername("unreg");
		user.setPassword("");
		user.setFullname("unreg");
	}

	@Override
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Log out as user";
	}
	
}
