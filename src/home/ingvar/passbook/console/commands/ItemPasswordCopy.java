package home.ingvar.passbook.console.commands;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Map;

public class ItemPasswordCopy extends UserCommand {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get("itemDAO");
		User owner = (User) params.get("user");
		
		String service  = (String) params.get("service");
		String username = (String) params.get("username");
		try {
			Item item = itemDAO.get(owner, service, username);
			StringSelection string = new StringSelection(item.getPassword());
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(string, null);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected String[] requiredParams() {
		return new String[] {"service", "username"};
	}
	
	@Override
	public String toString() {
		return "Copy password from item to clipboard";
	}

}
