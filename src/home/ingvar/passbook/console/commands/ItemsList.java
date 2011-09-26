package home.ingvar.passbook.console.commands;

import java.util.List;
import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;


public class ItemsList extends UserCommand {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get("itemDAO");
		User owner = (User) params.get("user");
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
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Show list of items";
	}

}
