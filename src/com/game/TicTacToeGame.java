package com.game;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


class TicTacToeGame {

    private final JFrame frame;

    private final List<ActionListener> clickListeners;
    private final List<Player> humanPlayers;
    private final Player aiPlayer;
    private final List<Player> allPlayers;
    private int turn = 0;
    private final GamePanel gamePanel;
    private final int size;
    private Runnable currentMode;

    private void cellClicked(ActionEvent e) {

        final Object source = e.getSource();
        if (source instanceof Cell) {

            final Cell cell = (Cell) source;
            final Player currentPlayer = getCurrentPlayer();
            currentPlayer.acquire(cell);
        }
    }

    private Player getCurrentPlayer() {

        return allPlayers.get(turn);
    }

    private void showWonMessage(Player winningPlayer) {

        if (JOptionPane.showConfirmDialog(frame, winningPlayer.getName() + " win, do you want to restart game?", "Tic-tac-toe",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            restart();

        } else {

            System.exit(0);
        }
    }

    private void showDrawMessage() {

        if (JOptionPane.showConfirmDialog(null, "Draw, do you want to restart game?", "Tic-tac-toe",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            restart();

        } else {

            System.exit(0);
        }
    }

    private boolean isPlayerHaveWon(Player player, Cell currentCell) { //игрок, которого мы хотим проверить; в какой ячейке он играл

        int fullLineAcquired = 0;
        int fullColumnAcquired = 0;
        int fullLDiagonalAcquired = 0;
        int fullRDiagonalAcquired = 0;

        int i = 0;

        while (i < size) {

            if (player.possess(gamePanel.getCellAt(currentCell.getPositionX(), i))) { //проверяем колонну

                fullColumnAcquired++;
            }
            if (player.possess(gamePanel.getCellAt(i, currentCell.getPositionY()))) { //проверяем линию

                fullLineAcquired++;
            }
            if (player.possess(gamePanel.getCellAt(i, i))) { //побочная диагональ

                fullLDiagonalAcquired++;
            }
            if (player.possess(gamePanel.getCellAt(i, this.size - 1 - i))) { //главная диагональ

                fullRDiagonalAcquired++;
            }
            if (fullColumnAcquired > i ||
                    fullLineAcquired > i ||
                    fullLDiagonalAcquired > i ||
                    fullRDiagonalAcquired > i) {

                i++;

            } else break;
        }

        return i == size;
    }

    private void checkEndGameOrNext(ActionEvent e) { //статус игры; смена игрока

        final Object source = e.getSource();
        if (source instanceof Cell) {

            final Cell cell = (Cell) source;
            final Player currentPlayer = getCurrentPlayer();
            if (isPlayerHaveWon(currentPlayer, cell)) {

                showWonMessage(currentPlayer);
            } else if (gamePanel.boardIsFull()) {

                showDrawMessage();
            } else {

                turn = (turn + 1) % allPlayers.size();
            }
        }


    }

    private void oneByOneSetup() { //переключение на режим "One By One"

        clickListeners.clear();
        clickListeners.add(e -> checkEndGameOrNext(e));
        clickListeners.add(e -> cellClicked(e));

        allPlayers.clear();
        allPlayers.addAll(humanPlayers);
    }

    private void aiSetup() { //переключение на режим "With AI"

        clickListeners.clear();
        clickListeners.add(e -> aITurn(e));
        clickListeners.add(e -> checkEndGameOrNext(e));
        clickListeners.add(e -> cellClicked(e));

        allPlayers.clear();
        allPlayers.add(humanPlayers.get(0));
        allPlayers.add(aiPlayer);
    }

    TicTacToeGame(int size) { //конструктор

        this.size = size;

        humanPlayers = new ArrayList<>();
        aiPlayer = new Player("AI", new ImageIcon("zero.png").getImage());

        humanPlayers.add(new Player("Player 1", new ImageIcon("cross.png").getImage()));
        humanPlayers.add(new Player("Player 2", new ImageIcon("zero.png").getImage()));

        clickListeners = new ArrayList<>();

        allPlayers = new ArrayList<>();

        currentMode = () -> oneByOneSetup(); //запускается стандартным режим "One By One"
        currentMode.run();


        final JMenuBar menuBar = createMenu();

        gamePanel = createGamePanel(size, clickListeners);
        frame = createFrame(menuBar, gamePanel);

        frame.setVisible(true);
    }

    private JMenuBar createMenu() {

        final JMenuBar menuBar = new JMenuBar(); //создается меню
        final JMenu gameMenu = new JMenu("Game");
        final JMenuItem withHuman = new JMenuItem("One by one");
        withHuman.setSelected(true);
        withHuman.addActionListener(e -> {

            currentMode = () -> oneByOneSetup();
            restart();
        });

        final JMenuItem withAI = new JMenuItem("With AI");
        withAI.addActionListener(e -> {

            currentMode = () -> aiSetup();
            restart();
        });
        gameMenu.add(withHuman);
        gameMenu.add(withAI);
        gameMenu.add(new JSeparator());
        final JMenuItem exit = new JMenuItem("Quit");
        exit.addActionListener(e -> {

            if (JOptionPane.showConfirmDialog(exit, "You sure?", "Tic-tac-toe",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                System.exit(0);
            }
        });
        gameMenu.add(exit);

        final JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> restart());

        menuBar.add(gameMenu);
        menuBar.add(restart);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JLabel());
        menuBar.setSize(250, 150);
        return menuBar;
    }

    private GamePanel createGamePanel(int size, List<ActionListener> clickListeners) {

        final GamePanel gamePanel = new GamePanel(size, clickListeners);
        gamePanel.setLayout(new GridLayout(size, size));
        gamePanel.setBorder(new EtchedBorder(Color.BLUE, Color.BLUE));

        return gamePanel;
    }

    private JFrame createFrame(final JMenuBar menuBar, GamePanel gamePanel) {

        final JFrame frame = new JFrame();
        int WIDTH, HEIGHT;
        WIDTH = 800;
        HEIGHT = 800;
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setVisible(false);
        frame.pack();
        frame.setTitle("Tic-tac-toe game");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);

        return frame;
    }

    private void restart() { //перезапуск игры

        currentMode.run();
        reloadBoard();
        reloadPlayers();
    }

    private void reloadPlayers() {

        turn = 0;
        allPlayers.forEach(Player::clearCell);
    }

    private void reloadBoard() {

        gamePanel.reset(clickListeners);
    }

    private void aITurn(ActionEvent e) { //случайный AI

        if (getCurrentPlayer().equals(aiPlayer)) {

            final Random random = new Random();
            /*
            все ячейки закидываются в стрим -> отбираются толлько свободные -> представдение результата в виде коллекции
            */
            final List<Cell> freeCells = gamePanel.getCells().stream().filter(Cell::isFree).collect(Collectors.toList());
            final OptionalInt freeCellIndex = random.ints(1, 0, freeCells.size()).findAny();

            if (freeCellIndex.isPresent()) {

                final Cell cell = freeCells.get(freeCellIndex.getAsInt());
                final ActionEvent aiClick = new ActionEvent(cell, 0, null);
                cellClicked(aiClick);
                checkEndGameOrNext(aiClick);
            }
        }
    }
}
