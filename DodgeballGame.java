import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class DodgeballGame extends JPanel implements ActionListener {
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 150;
    private final int BALL_SIZE = 20;
    private final int BALL_SPEED = 10;

    private int playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
    private ArrayList<Point> balls = new ArrayList<>();
    private ArrayList<Integer> ballSpeedsX = new ArrayList<>();
    private Random random = new Random();
    private Timer timer;
    private boolean gameOver = false;
    private Image playerImage;
    private Image tennisBallImage;
    private int score = 0;

    public DodgeballGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        tennisBallImage = new ImageIcon("tennis_ball.jpg").getImage();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playerX -= 15;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playerX += 15;
                }
                playerX = Math.max(0, Math.min(WIDTH - PLAYER_WIDTH, playerX));
            }
        });
        timer = new Timer(30, this);
        timer.start();
        spawnBall();
        playerImage = new ImageIcon("player.jpg").getImage();
    }

    private void spawnBall() {
        int ballX = random.nextInt(WIDTH - BALL_SIZE);
        balls.add(new Point(ballX, 0));
        int ballSpeedX = random.nextInt(5) - 2; 
        ballSpeedsX.add(ballSpeedX);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(playerImage, playerX, HEIGHT - PLAYER_HEIGHT - 10, PLAYER_WIDTH, PLAYER_HEIGHT, null);
        for (Point ball : balls) {
            g.drawImage(tennisBallImage, ball.x, ball.y, BALL_SIZE, BALL_SIZE, null);
        }
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over!", WIDTH / 2 - 30, HEIGHT / 2);
            g.drawString("Score: " + score, WIDTH / 2 - 30, HEIGHT / 2 + 20);
        } else {
            g.drawString("Score: " + score, 10, 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        for (int i = balls.size() - 1; i >= 0; i--) {
            Point ball = balls.get(i);
            int ballSpeedX = ballSpeedsX.get(i);
            ball.x += ballSpeedX; 
            ball.y += BALL_SPEED;

            if (ball.y > HEIGHT) {
                balls.remove(i);
                ballSpeedsX.remove(i);
                spawnBall();
                score++;
            }

            if (ball.y + BALL_SIZE >= HEIGHT - PLAYER_HEIGHT - 10 && ball.x + BALL_SIZE >= playerX && ball.x <= playerX + PLAYER_WIDTH) {
                gameOver = true;
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dodgeball Game");
        DodgeballGame game = new DodgeballGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}