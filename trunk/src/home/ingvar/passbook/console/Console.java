package home.ingvar.passbook.console;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

import java.util.Map;
import java.util.Scanner;

public class Console {
	
	private final DaoFactory factory;
	private final UserDAO userDAO;
	private final ItemDAO itemDAO;
	private User user;
	
	public static void main(String[] args) throws Exception {
		new Console().program();
	}
	
	public Console() throws InstantiationException {
		factory = DaoFactory.newInstance(DaoFactory.H2);
		userDAO = factory.getUserDAO();
		itemDAO = factory.getItemDAO();
		user = new User("unreg", "", "unreg");
	}
	
	public void program() {
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to passbook!\n'help' - get command list\n'command help' - get help for command\n");
		while(true) {
			System.out.print(user + "> ");
			
			String input = in.nextLine();
			Map<Parameter, Object> params = Command.parseParams(input);
			params.put(Parameter.USER, user);
			params.put(Parameter.FACTORY, factory);
			params.put(Parameter.USER_DAO, userDAO);
			params.put(Parameter.ITEM_DAO, itemDAO);
			try {
				Command cmd = Command.newInstance((String) params.get("command"));
				if(params.containsKey(Parameter.HELP)) {
					cmd.help();
				} else {
					cmd.execute(params);
				}
				
			} catch(CommandException e) {
				System.out.println(e.getMessage());
			} catch(InstantiationException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
