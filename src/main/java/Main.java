import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final int LENGTH_OF_LETTERS = 100_000;
    public static final int NUMBER_OF_TEXTS = 10_000;
    public static final int QUEUE_SIZE = 100;
    public static final String LETTERS = "abc";
    public static BlockingQueue<String> analysisA = new ArrayBlockingQueue<>(QUEUE_SIZE);
    public static BlockingQueue<String> analysisB = new ArrayBlockingQueue<>(QUEUE_SIZE);
    public static BlockingQueue<String> analysisC = new ArrayBlockingQueue<>(QUEUE_SIZE);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_TEXTS; i++) {
                try {
                    String text = generateText(LETTERS, LENGTH_OF_LETTERS);
                    analysisA.put(text);
                    analysisB.put(text);
                    analysisC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
        getThreadForCount(analysisA, 'a').start();
        getThreadForCount(analysisB, 'b').start();
        getThreadForCount(analysisC, 'c').start();
    }

    public static Thread getThreadForCount(BlockingQueue<String> queue, char ch) {
        return new Thread(() -> {
            long numberOfABC;
            long max = 0;
            for (int i = 0; i < NUMBER_OF_TEXTS; i++) {
                try {
                    String line = queue.take();
                    numberOfABC = line.chars().filter(x -> x == ch)
                            .count();
                } catch (InterruptedException e) {
                    return;
                }
                if (numberOfABC > max) {
                    max = numberOfABC;
                }
            }
            System.out.println("Количество символов " + ch + ": " + max + " шт.");
        });
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
