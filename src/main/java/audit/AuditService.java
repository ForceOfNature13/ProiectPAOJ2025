package audit;

import observer.EventBus;
import observer.Observer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class AuditService implements Observer {

    private static final Path FILE = Paths.get("audit.csv");
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static AuditService instance;
    public static AuditService getInstance() {
        if (instance == null) instance = new AuditService();
        return instance;
    }

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    private AuditService() {
        Thread writerThread = new Thread(this::writerLoop, "audit-writer");
        writerThread.setDaemon(true);
        writerThread.start();
    }

    @Override
    public void update(String actionName) {
        String line = "%s,%s%n".formatted(actionName,
                LocalDateTime.now().format(FMT));
        queue.offer(line);
    }

    private void writerLoop() {
        try {
            if (Files.notExists(FILE))
                Files.writeString(FILE, "nume_actiune,timestamp%n");

            try (BufferedWriter bw = Files.newBufferedWriter(
                    FILE, StandardOpenOption.APPEND)) {

                while (!Thread.currentThread().isInterrupted()) {
                    bw.write(queue.take());
                    bw.flush();
                }
            }
        } catch (InterruptedException ignored) { }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void initObserver() {
        EventBus.getInstance().registerObserver(getInstance());
    }
}
