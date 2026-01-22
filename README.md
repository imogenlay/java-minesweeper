## Project Summary: Console Minesweeper (Java)

This project recreates a simplified version of the classic Minesweeper game as a Java console application. The game
generates a configurable square grid containing randomly placed mines.

Players interact with the game by entering coordinate-based commands to reveal tiles on the grid. After every command,
the current state of the grid is rendered to the console. Each revealed tile displays a number from 0 to 8, indicating how
many mines are adjacent to that location. If a mine is revealed, the game
ends. The game continues until either a mine is triggered or all non-mine tiles have been successfully revealed, in which case
the player wins. When an empty tile (with zero adjacent mines) is revealed, the game automatically reveals all
surrounding tiles and recursively cascades through connected empty areas, matching standard Minesweeper behavior.
