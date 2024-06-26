package dev.talwat.earthsmp.nations;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Handles the news.yml config file.
 */
public class NationsConfig {
    public final List<Map<String, Object>> parsed;
    private final YamlConfiguration yaml;
    private final File file;

    public NationsConfig(List<Map<String, Object>> parsed, YamlConfiguration yaml, File file) {
        this.parsed = parsed;
        this.yaml = yaml;
        this.file = file;
    }

    public static List<UUID> getUUIDs(List<String> args) {
        return args.stream().map(UUID::fromString).toList();
    }

    public static NationsConfig Load(Earthsmp plugin) {
        File file = new File(plugin.getDataFolder(), "nations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        return new NationsConfig((List<Map<String, Object>>) config.getList("nations"), config, file);
    }

    public void Save(Earthsmp plugin) {
        try {
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save the config file", e);
        }
    }

    /**
     * Returns an index for the nation corresponding to the input tag.
     * Returns -1 if not found.
     */
    public int get(String tag) {
        for (int i = 0; i < parsed.size(); i++) {
            String parsedTag = (String) parsed.get(i).get("tag");

            if (Objects.equals(tag, parsedTag)) {
                return i;
            }
        }

        return -1;
    }

    public int get(CommandSender ruler) {
        for (int i = 0; i < parsed.size(); i++) {
            Nation deserialized = Nation.deserialize(parsed.get(i));

            if (ruler instanceof Player && ((Player) ruler).getUniqueId().equals(deserialized.ruler())) {
                return i;
            }
        }

        return -1;
    }
}
