package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;


public class ItemShow extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get(Parameter.ITEM_DAO);
		User owner = (User) params.get(Parameter.USER);
		
		String service  = (String) params.get(Parameter.SERVICE);
		String username = (String) params.get(Parameter.USERNAME);
		try {
			Item item = itemDAO.get(owner, service, username);
			StringBuilder out = new StringBuilder(item.toString());
			out.append(":").append(item.getPassword()).append(" (").append(item.getComment()).append(")");
			System.out.println(out);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected Parameter[] requiredParams() {
		return new Parameter[] {Parameter.SERVICE, Parameter.USERNAME};
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Show one item";
	}

}
