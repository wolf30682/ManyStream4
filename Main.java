package ManyStream4;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }).start();

        Thread a = new Thread(() -> {
            char letter = 'a';
            int maxA = maxCharCount(queue1, letter);
            System.out.println("Максимальное количество символов 'a': " + maxA);
        });
        a.start();

        Thread b = new Thread(() -> {
            char letter = 'b';
            int maxB = maxCharCount(queue2, letter);
            System.out.println("Максимальное количество символов 'b': " + maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            char letter = 'c';
            int maxC = maxCharCount(queue3, letter);
            System.out.println("Максимальное количество символов 'c': " + maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    private static int maxCharCount(ArrayBlockingQueue queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10_000; i++) {
                text = (String) queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return max;
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
