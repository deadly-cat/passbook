package home.ingvar.passbook.console.commands;

import java.util.List;
import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;


public class ItemsList extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get(Parameter.ITEM_DAO);
		User owner = (User) params.get(Parameter.USER);
		try {
			List<Item> items = itemDAO.list(owner);
			for(Item i : items) {
				System.out.println(i);
			}
		} catch(ResultException e) {
			throw new CommandException(e);
		}
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
		return "Show list of items";
	}

}
