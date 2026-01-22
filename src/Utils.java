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

	public static int[] parseCoordInput(String s)
	{
		int[] coord = { -1, -1 };
		String[] commandParts = s.trim().split("\\s+");
		if (commandParts.length != 2)
		{
			System.out.println("Could not read input. Write only two numbers separated by a space.");
			return coord;
		}

		int x, y;
		int[] out = new int[1];
		if (Utils.tryParseInt(commandParts[0], out))
			x = out[0];
		else
		{
			System.out.println("Could not read x value. Write only two numbers separated by a space.");
			return coord;
		}

		if (Utils.tryParseInt(commandParts[1], out))
			y = out[0];
		else
		{
			System.out.println("Could not read y value. Write only two numbers separated by a space.");
			return coord;
		}

		coord[0] = x;
		coord[1] = y;
		return coord;
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
