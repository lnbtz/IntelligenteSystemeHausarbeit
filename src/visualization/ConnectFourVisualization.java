package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectFourVisualization extends Canvas implements Runnable {
    private boolean running = false;
    private final Keyboard keyboard;
    private final Mouse mouse;
    private Graphics graphics;

    private final int NUM_OF_ROWS, NUM_OF_COLUMNS;
    private final Color[] gridColumnColors;
    private int selectedColumn = 0;
    private Color nextColor = Color.BLACK;
    private final Color[][] pieceColors;
    private final int slotPixelSize = 100;
    private final int borderPixelSize = 10;
    private final int piecePixelSize = slotPixelSize - borderPixelSize;

    public ConnectFourVisualization(String title, int numOfRows, int numOfColumns, Color startingColor) {
        Dimension size = new Dimension(numOfColumns * slotPixelSize, numOfRows * slotPixelSize);
        setPreferredSize(size);

        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        keyboard = new Keyboard();
        addKeyListener(keyboard);
        mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        NUM_OF_ROWS = numOfRows;
        NUM_OF_COLUMNS = numOfColumns;
        gridColumnColors = new Color[numOfColumns];
        pieceColors = new Color[numOfRows][numOfColumns];

        this.nextColor = startingColor;

        for (int col = 0; col < numOfColumns; col++) {
            gridColumnColors[col] = Color.BLACK;
        }
    }

    public synchronized void start() {
        running = true;
        Thread thread = new Thread(this, "Display");
        thread.start();
    }

    @Override
    public void run() {

        // zum Timen der update()-Aufrufe
        long time = System.nanoTime();
        long boundary = 1000000000 / 60;

        // game.Game Loop:
        while (running) {

            // 60 mal pro Sekunde
            if (System.nanoTime() - time >= boundary) {
                time = System.nanoTime();
                render();
            }
        }
    }

    public void colorPiece(int row, int column, Color color, Color nextColor) {
        pieceColors[row][column] = color;
        this.nextColor = nextColor;
    }

    // highlight the winning pieces
    public void highlightPieces(int[] coordinates) {

    }

    private boolean reportNextClick = false;
    private final Semaphore sem1 = new Semaphore(0);
    private final Semaphore sem2 = new Semaphore(0);

    // wird vom main-Thread aufgerufen
    public int nextClickedColumn() {
        reportNextClick = true;
        // warten bis freigegeben
        sem1.acquireUninterruptibly();
        int toReturn = selectedColumn;
        sem2.release();
        return toReturn;
    }

    private void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }
        graphics = bufferStrategy.getDrawGraphics();

        gridColumnColors[selectedColumn] = Color.BLACK;
        selectedColumn = mouse.x / slotPixelSize;
        gridColumnColors[selectedColumn] = nextColor;

        graphics.setColor(Color.GRAY);
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            for (int col = 0; col < NUM_OF_COLUMNS; col++) {
                drawSlot(row, col, pieceColors[row][col]);
            }
        }

        if (mouse.clicked() && reportNextClick) {
            reportNextClick = false;
            sem1.release();
            sem2.acquireUninterruptibly();
        }

        graphics.dispose();
        bufferStrategy.show();
    }

    private void drawSlot(int row, int column, Color pieceColor) {
        int x = column * slotPixelSize;
        int y = row * slotPixelSize;
        graphics.setColor(gridColumnColors[column]);
        graphics.fillRect(x, y, slotPixelSize, slotPixelSize);
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x + borderPixelSize / 2, y + borderPixelSize / 2, piecePixelSize, piecePixelSize);
        if (pieceColor != null) {
            graphics.setColor(pieceColor);
            graphics.fillOval(x + borderPixelSize / 2, y + borderPixelSize / 2, piecePixelSize, piecePixelSize);
        }
    }
}

