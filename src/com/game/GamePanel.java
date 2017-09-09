package com.game;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


class GamePanel extends JPanel {

	private final Cell[][] boardCells;
	private final List<Cell> cells;

	GamePanel(int size, List<ActionListener> clickListeners) {

		boardCells = new Cell[size][size];
		cells = new ArrayList<>(size * size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				boardCells[i][j] = new Cell(i, j, clickListeners);
				cells.add(boardCells[i][j]);
				add(boardCells[i][j]);
			}
		}
	}

	Cell getCellAt(int x, int y) {

		return boardCells[x][y];
	}

	void reset(List<ActionListener> clickListeners) { //сброс ячеек

		for (Cell c : cells) {
			c.reset(clickListeners);
		}
	}

	boolean boardIsFull() {

		for (Cell cell : cells) {
			if (cell.isFree()) {
				return false;
			}
		}
		return true;
	}

	List<Cell> getCells() {

		return cells;
	}
}
