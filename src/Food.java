import java.awt.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Food extends GameObject {
    private static final int FOOD_SIZE = 20;
    private static final Random random = new Random();
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private Snake snake;

    public Food(Snake snake) {
        super(0, 0, FOOD_SIZE, FOOD_SIZE, Color.RED);
        this.snake = snake;
        respawn();
    }

    public void respawn() {
        // Create a list of all possible positions
        List<Point> possiblePositions = new ArrayList<>();
        
        // Generate all possible grid positions
        for (int x = 0; x < SCREEN_WIDTH; x += FOOD_SIZE) {
            for (int y = 0; y < SCREEN_HEIGHT; y += FOOD_SIZE) {
                possiblePositions.add(new Point(x, y));
            }
        }
        
        // Remove positions occupied by snake
        List<Point> snakeSegments = snake.getSegments();
        for (Point segment : snakeSegments) {
            possiblePositions.removeIf(pos -> pos.x == segment.x && pos.y == segment.y);
        }
        
        // If there are no valid positions (shouldn't happen in normal gameplay)
        if (possiblePositions.isEmpty()) {
            // Try to find any position that's not occupied by the snake's head
            Point head = snake.getHead();
            for (int x = 0; x < SCREEN_WIDTH; x += FOOD_SIZE) {
                for (int y = 0; y < SCREEN_HEIGHT; y += FOOD_SIZE) {
                    if (x != head.x || y != head.y) {
                        x = x;
                        y = y;
                        return;
                    }
                }
            }
            // If we still can't find a position, place it at (0,0)
            x = 0;
            y = 0;
            return;
        }
        
        // Pick a random position from remaining valid positions
        Point newPosition = possiblePositions.get(random.nextInt(possiblePositions.size()));
        x = newPosition.x;
        y = newPosition.y;
    }

    @Override
    public void update() {
        // Food doesn't need to update
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    // Helper method to check if a position is valid
    private boolean isValidPosition(int x, int y) {
        // Check if position is within bounds
        if (x < 0 || x >= SCREEN_WIDTH || y < 0 || y >= SCREEN_HEIGHT) {
            return false;
        }
        
        // Check if position is not occupied by snake
        for (Point segment : snake.getSegments()) {
            if (segment.x == x && segment.y == y) {
                return false;
            }
        }
        
        return true;
    }
} 