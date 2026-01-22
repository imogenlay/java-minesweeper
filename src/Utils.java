public class Utils
{
	public static boolean tryParseInt(String s, int[] out)
	{
		try
		{
			out[0] = Integer.parseInt(s);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	public static void addHorizontalLineToStringBuilder(StringBuilder sb, int width, boolean isTop)
	{
		sb.append(Const.HORIZONTAL_START);
		for (int i = 0; i < width; i++)
		{
			if (i == 0)
				sb.append(Const.HORIZONTAL_SEGMENT_FIRST);
			else if (isTop)
				sb.append(Const.HORIZONTAL_SEGMENT_TOP);
			else
				sb.append(Const.HORIZONTAL_SEGMENT_BOTTOM);
		}
		sb.append(Const.HORIZONTAL_END);
	}
}
