package home.ingvar.passbook.console.commands;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

import java.util.Map;

public class ItemAdd extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get(Parameter.ITEM_DAO);
		Item item = new Item();
		item.setOwner((User) params.get(Parameter.USER));
		item.setService((String) params.get(Parameter.SERVICE));
		item.setUsername((String) params.get(Parameter.USERNAME));
		item.setPassword((String) params.get(Parameter.PASSWORD));
		item.setComment((String) params.get(Parameter.COMMENT));
		try {
			itemDAO.add(item);
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
		return new Parameter[] {Parameter.PASSWORD, Parameter.COMMAND};
	}
	
	@Override
	public String toString() {
		return "Add new item";
	}
	
}
