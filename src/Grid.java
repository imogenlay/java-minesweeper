import java.util.Random;

public class Grid
{
	private final int width;
	private final int height;
	private final byte[] mineGrid;
	private byte gameState = Const.PLAYING;

	public Grid(int _width, int _height, float minePercent)
	{
		width = _width;
		height = _height;
		mineGrid = new byte[width * height];
		restart(minePercent);
	}

	public byte getGameState() { return gameState; }

	public void restart(float minePercent)
	{
		minePercent = Math.clamp(minePercent, 0f, 0.95f);
		int bombCount = Math.max(2, (int) (mineGrid.length * minePercent));
		int[] bombIndex = Utils.getRandomIndexArray(mineGrid.length);

		for (int i = 0; i < bombCount && i < mineGrid.length; i++)
			mineGrid[bombIndex[i]] = Const.GRID_MINE;
	}

	public int getIndex(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width || y >= height)
			return -1;
		return y * width + x;
	}

	public void attemptRunCommand(String command)
	{
		int[] coord = Utils.parseCoordInput(command);
		int x = coord[0];
		int y = coord[1];

		if (x == -1 || y == -1)
			return;

		revealGrid(x, y, true);

		// Scan grid to see if we have a winner!
		boolean winner = true;
		for (byte b : mineGrid)
			if (b == Const.GRID_UNKNOWN)
			{
				winner = false;
				break;
			}

		if (winner)
			gameState = Const.WIN;
	}

	private void revealGrid(int x, int y, boolean force)
	{
		byte value = getValueAtCoordinate(x, y);

		if (value == Const.GRID_OUT_OF_BOUNDS)
			return;

		if (value == Const.GRID_MINE)
		{
			if (force)
			{
				// We only reveal the mine if it was 'forced'.
				// Forcing only occurs on the first click, all
				// recursive functions do not force.
				gameState = Const.LOSE;
			}
			return;
		}

		if (value >= Const.REVEALED_MIN && value < Const.REVEALED_MAX)
		{
			// Already revealed.
			return;
		}

		if (value == Const.GRID_UNKNOWN)
		{
			// Check neighbours for mines.
			byte neighbouringMines = Const.REVEALED_MIN;
			for (int neighbourX = x - 1; neighbourX <= x + 1; neighbourX++)
				for (int neighbourY = y - 1; neighbourY <= y + 1; neighbourY++)
				{
					if (neighbourX == x && neighbourY == y)
						continue;

					byte neighbouringCell = getValueAtCoordinate(neighbourX, neighbourY);
					if (neighbouringCell == Const.GRID_MINE)
						neighbouringMines++;
				}

			setValueAtCoordinate(x, y, neighbouringMines);

			if (neighbouringMines == Const.REVEALED_MIN)
			{
				// Try reveal neighbours if this spot is empty
				// and there are no nearby mines.
				// These will not force any reveal on mines.
				revealGrid(x - 1, y - 1, false);
				revealGrid(x - 1, y, false);
				revealGrid(x - 1, y + 1, false);

				revealGrid(x, y - 1, false);
				revealGrid(x, y + 1, false);

				revealGrid(x + 1, y - 1, false);
				revealGrid(x + 1, y, false);
				revealGrid(x + 1, y + 1, false);
			}
		}
	}

	private byte getValueAtCoordinate(int x, int y)
	{
		int index = getIndex(x, y);
		if (index == -1)
			// Out of bounds.
			return Const.GRID_OUT_OF_BOUNDS;

		return mineGrid[index];
	}

	private void setValueAtCoordinate(int x, int y, byte value)
	{
		int index = getIndex(x, y);
		if (index == -1)
			return;

		mineGrid[index] = value;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(" ");
		for (int x = 0; x < width; x++)
		{
			// Add the row of numbers across the top.
			// Check to see if the number will take up
			// one or two characters.
			if (x > 10)
				sb.append("  ").append(x); // Needs two spaces.
			else
				sb.append("   ").append(x); // Needs three spaces.
		}
		sb.append('\n');
		Utils.addHorizontalLineToStringBuilder(sb, width, true);

		for (int y = 0; y < height; y++)
		{
			// Add the number at the beginning of a row.
			sb.append(y);
			if (y < 10)
				sb.append(" ");
			sb.append("┃");

			for (int x = 0; x < width; x++)
			{
				// Add each grid item.
				byte value = mineGrid[getIndex(x, y)];

				if (value == Const.GRID_MINE)
				{
					// Change the mine visual if the game is over!
					if (gameState == Const.WIN || gameState == Const.LOSE)
						value = Const.GRID_REVEALED_MINE;
				}

				sb.append(" ");
				sb.append(Const.dataDefinitions[value]);
				sb.append(" ");
				if (x + 1 < width)
					sb.append("┼");
			}

			// End row.
			sb.append("┃\n");
		}

		// Add final row.
		Utils.addHorizontalLineToStringBuilder(sb, width, false);
		return sb.toString();
	}
}
