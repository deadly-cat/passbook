package home.ingvar.passbook.console;

public enum Parameter {
	
	COMMAND(null, "command"),
	HELP("h", "help"),
	USERNAME("u", "username"),
	PASSWORD("p", "password"),
	FULLNAME("f", "fullname"),
	SERVICE("s", "service"),
	COMMENT("c", "comment"),
	USER(null, "user"),
	FACTORY(null, "factory"),
	USER_DAO(null, "userDAO"),
	ITEM_DAO(null, "itemDAO");
	
	public final String shortName;
	public final String longName;
	
	public static Parameter getByName(String name) {
		for(Parameter p : Parameter.values()) {
			if(name.equals(p.shortName) || name.equals(p.longName)) {
				return p;
			}
		}
		return null;
	}
	
	Parameter(String shortName, String longName) {
		this.shortName = shortName;
		this.longName  = longName;
	}
	
}
