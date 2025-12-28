import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class JsonStorage {
    private static Path filePath(String filename) {
        return Paths.get(System.getProperty("user.dir")).resolve(filename).toAbsolutePath();
    }

    public static String read(String filename) throws IOException {
        Path path = filePath(filename);
        if (!Files.exists(path)) return "";
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static void write(String filename, String content) throws IOException {
        Path path = filePath(filename);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static Path getPath(String filename) {
        return filePath(filename);
    }
}