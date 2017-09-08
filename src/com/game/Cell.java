package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Cell extends JButton {

	private final int positionX;
	private final int positionY;
	private boolean free;
	private List<ActionListener> clickListeners;
	private Image image;

	Cell(int x, int y, List<ActionListener> clickListeners) {

		super();
		setBackground(Color.WHITE);
		this.positionX = x;
		this.positionY = y;
		this.free = true;
		this.clickListeners = new ArrayList<>(clickListeners);
		this.clickListeners.forEach(this::addActionListener);
	}

	int getPositionX() {

		return positionX;
	}

	int getPositionY() {

		return positionY;
	}

	void reset(List<ActionListener> clickListeners) {

		if (free) {

			this.clickListeners.forEach(this::removeActionListener);
		} else {

			this.setBorderPainted(true);
			this.setFocusPainted(true);
		}

		this.clickListeners = new ArrayList<>(clickListeners);
		this.clickListeners.forEach(this::addActionListener);
		this.image = null;
		this.free = true;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		if (image == null) {

			return;
		}

		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	private void disableCell() {

		this.clickListeners.forEach(this::removeActionListener);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
	}

	void checkBy(Player currentPlayer) {

		disableCell();
		this.image = currentPlayer.getImage();
		this.free = false;
	}

	boolean isFree() {

		return this.free;
	}
}
