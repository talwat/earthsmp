package dev.talwat.earthsmp.config;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Handles the nations.yml config file.
 */
public class NationsConfig {
    private final YamlConfiguration yaml;
    private final File file;
    public final List<Map<String, Object>> parsed;

    public NationsConfig(List<Map<String, Object>> parsed, YamlConfiguration yaml, File file) {
        this.parsed = parsed;
        this.yaml = yaml;
        this.file = file;
    }

    public static UUID[] getUUIDs(List<String> args) {
        return args.stream().map(UUID::fromString).toArray(UUID[]::new);
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
    public int findNationByTag(String inputTag) {
        for (int i = 0; i < parsed.size(); i++) {
            String tag = (String) parsed.get(i).get("tag");

            if (Objects.equals(tag, inputTag)) {
                return i;
            }
        }

        return -1;
    }
}
