package wta.util.utils;

public enum RegType {
	REG(true),
	REREG(true),
	UNREG(false),
	REG_ALWAYS(true),
	UNREG_OR_NONE(false);

	private final boolean register;

	RegType(final boolean register){
		this.register=register;
	}

	public boolean isRegister() {
		return register;
	}
}
