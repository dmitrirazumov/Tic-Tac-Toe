package com.game;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;


class Player {

	private String name;
	private Image image;
	private final Set<Cell> cellsAcquired;

	Player(String name, Image image) {

		super();
		this.name = name;
		this.image = image;
		this.cellsAcquired = new HashSet<>();
	}

	Image getImage() {

		return image;
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
