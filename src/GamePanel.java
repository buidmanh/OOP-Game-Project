import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener, Runnable {
    private static final int TARGET_FPS = 10; // Slower for snake game
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    
    private Snake snake;
    private Food food;
    private boolean running;
    private Thread gameThread;
    private int score;
    private boolean gameOver;
    private Font scoreFont;
    private Font gameOverFont;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        snake = new Snake(400, 300);
        food = new Food(snake);
        score = 0;
        gameOver = false;
        
        scoreFont = new Font("Arial", Font.BOLD, 20);
        gameOverFont = new Font("Arial", Font.BOLD, 40);
        
        startGame();
    }

    private void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw game objects
        snake.render(g);
        food.render(g);
        
        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString("Score: " + score, 10, 30);
        
        // Draw game over message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(gameOverFont);
            String gameOverText = "Game Over! Press SPACE to restart";
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
            int y = getHeight() / 2;
            g.drawString(gameOverText, x, y);
        }
    }

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateTime = now - lastUpdateTime;
            lastUpdateTime = now;

            if (!gameOver) {
                update();
            }

            repaint();

            long sleepTime = OPTIMAL_TIME - (System.nanoTime() - now);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void update() {
        snake.update();
        
        // Check for collisions
        if (snake.checkCollision()) {
            gameOver = true;
            return;
        }
        
        // Check for food collision
        Point snakeHead = snake.getHead();
        Point foodPosition = food.getPosition();
        if (snakeHead.x == foodPosition.x && snakeHead.y == foodPosition.y) {
            snake.grow();
            food.respawn();
            score += 10;
        }
    }

    private void resetGame() {
        snake = new Snake(400, 300);
        food = new Food(snake);
        score = 0;
        gameOver = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
} 