package dev.talwat.earthsmp;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;


public class News {
    private final Earthsmp plugin;
    public LinkedHashMap<Date, String> articles;
    public Map.Entry<Date, String> current;

    public News(Earthsmp plugin) {
        this.plugin = plugin;
        Load();
    }

    public void Load() {
        File directory = new File(plugin.getDataFolder(), "news");

        File[] fileList = directory.listFiles();
        if (fileList == null) {
            return;
        }

        HashMap<Date, String> unsorted = new HashMap<>();
        for (File file : fileList) {
            int dotIdx = file.getName().lastIndexOf('.') + 1;

            String extension = file.getName().substring(dotIdx);
            String raw = file.getName().substring(0, dotIdx - 1);

            if (!extension.equals("txt")) {
                continue;
            }

            Date date;
            String content;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(raw);
                content = new String(new FileInputStream(file).readAllBytes());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error parsing or reading article!", e);

                return;
            }

            unsorted.put(date, content);
        }

        articles = new LinkedHashMap<>();

        unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> articles.put(entry.getKey(), entry.getValue().trim()));

        current = null;

        for (Map.Entry<Date, String> entry : articles.entrySet()) {
            current = entry;
        }
    }
}
