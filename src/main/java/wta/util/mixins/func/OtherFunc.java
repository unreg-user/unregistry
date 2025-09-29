package wta.util.mixins.func;

public class OtherFunc {
	public static <T> void requireNull(T obj) {
		if (obj != null) throw new NotNullPointerException();
	}
}
