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
		positionX = x;
		positionY = y;
		free = true;
		this.clickListeners = new ArrayList<>(clickListeners);
		this.clickListeners.forEach(e -> addActionListener(e));
	}

	/*
	for(int i=0; i < this.clickListeners.size(); i++){
	ActionListener action = clickListeners.get(i);
	this.addActionLitesner(action);
	 }
	 */

	int getPositionX() {

		return positionX;
	}

	int getPositionY() {

		return positionY;
	}

	void reset(List<ActionListener> clickListeners) {

		if (free) {

			this.clickListeners.forEach(e -> removeActionListener(e));

		} else setEnabled(true);


		this.clickListeners = new ArrayList<>(clickListeners);
		this.clickListeners.forEach(e -> addActionListener(e));
		image = null;
		free = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image == null) return;

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	private void disableCell() {

		clickListeners.forEach(e -> removeActionListener(e));
		setEnabled(false);
	}

	void checkBy(Player currentPlayer) {

		disableCell();
		image = currentPlayer.getSign();
		free = false;
	}

	boolean isFree() {

		return free;
	}
}
