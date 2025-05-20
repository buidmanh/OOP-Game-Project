import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake extends GameObject {
    private static final int SEGMENT_SIZE = 20;
    private static final int INITIAL_LENGTH = 3;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private List<Point> segments;
    private Direction direction;
    private boolean growing;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Snake(int x, int y) {
        super(x, y, SEGMENT_SIZE, SEGMENT_SIZE, Color.GREEN);
        segments = new ArrayList<>();
        direction = Direction.RIGHT;
        growing = false;
        
        // Initialize snake body
        for (int i = 0; i < INITIAL_LENGTH; i++) {
            segments.add(new Point(x - (i * SEGMENT_SIZE), y));
        }
    }

    @Override
    public void update() {
        // Move the snake
        Point head = segments.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y -= SEGMENT_SIZE;
                break;
            case DOWN:
                newHead.y += SEGMENT_SIZE;
                break;
            case LEFT:
                newHead.x -= SEGMENT_SIZE;
                break;
            case RIGHT:
                newHead.x += SEGMENT_SIZE;
                break;
        }

        // Wrap around screen
        if (newHead.x < 0) {
            newHead.x = SCREEN_WIDTH - SEGMENT_SIZE;
        } else if (newHead.x >= SCREEN_WIDTH) {
            newHead.x = 0;
        }
        if (newHead.y < 0) {
            newHead.y = SCREEN_HEIGHT - SEGMENT_SIZE;
        } else if (newHead.y >= SCREEN_HEIGHT) {
            newHead.y = 0;
        }

        // Add new head
        segments.add(0, newHead);
        
        // Remove tail if not growing
        if (!growing) {
            segments.remove(segments.size() - 1);
        }
        growing = false;
    }

    @Override
    public void render(Graphics g) {
        // Draw each segment
        for (int i = 0; i < segments.size(); i++) {
            Point segment = segments.get(i);
            // Make head slightly darker
            if (i == 0) {
                g.setColor(new Color(0, 200, 0));
            } else {
                g.setColor(color);
            }
            g.fillRect(segment.x, segment.y, width, height);
        }
    }

    public void setDirection(Direction newDirection) {
        // Prevent 180-degree turns
        if ((direction == Direction.UP && newDirection == Direction.DOWN) ||
            (direction == Direction.DOWN && newDirection == Direction.UP) ||
            (direction == Direction.LEFT && newDirection == Direction.RIGHT) ||
            (direction == Direction.RIGHT && newDirection == Direction.LEFT)) {
            return;
        }
        direction = newDirection;
    }

    public void grow() {
        growing = true;
    }

    public Point getHead() {
        return segments.get(0);
    }

    public boolean checkCollision() {
        Point head = getHead();
        
        // Only check self collision (skip the head)
        for (int i = 1; i < segments.size(); i++) {
            if (head.equals(segments.get(i))) {
                return true;
            }
        }
        
        return false;
    }

    public boolean checkFoodCollision(Point food) {
        return getHead().equals(food);
    }

    public List<Point> getSegments() {
        return new ArrayList<>(segments); // Return a copy to prevent external modification
    }
} 