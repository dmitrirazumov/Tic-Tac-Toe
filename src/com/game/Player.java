package com.game;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;


class Player {

	private String name;
	private Image sign;
	private final Set<Cell> cellsAcquired; //ячейки, на которые кликнули. они больше не свободны

	Player(String name, Image sign) {

		super();
		this.name = name;
		this.sign = sign;
		this.cellsAcquired = new HashSet<>();
	}

	Image getSign() {

		return sign;
	}

	String getName() {

		return name;
	}

	void acquire(Cell cell) {

		cell.checkBy(this);
		cellsAcquired.add(cell);
	}

	void clearCell() {

		cellsAcquired.clear();
	}

	boolean possess(Cell cell) {

		return cellsAcquired.contains(cell);
	}
}
