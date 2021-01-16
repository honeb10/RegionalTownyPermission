package honeb1.regionaltownypermission;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RTPConfiguration {
    RegionalTownyPermission plugin;

    RTPConfiguration(RegionalTownyPermission plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration configuration = plugin.getConfig();
        for(String perm : configuration.getStringList("TownPermissions")){
            PermissionNode node
                    = PermissionNode.builder(perm).permission(perm)
                        .value(true)
                        .expiry(Duration.ofHours(1)).build();
            plugin.townNodes.add(node);
        }
        for(String perm : configuration.getStringList("NationPermissions")){
            PermissionNode node
                    = PermissionNode.builder(perm).permission(perm)
                    .value(true)
                    .expiry(Duration.ofHours(1)).build();
            plugin.nationNodes.add(node);
        }
    }
    public void reloadConfig(){
        plugin.townNodes.clear();
        plugin.nationNodes.clear();
        loadConfig();
    }
}
