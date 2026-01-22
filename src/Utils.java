public class Utils
{
	public static boolean tryParseInt(String s, int[] out) {
		try {
			out[0] = Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
