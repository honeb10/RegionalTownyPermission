package honeb1.regionaltownypermission;

import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
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
import java.util.Objects;

public class RTPConfiguration {
    RegionalTownyPermission plugin;

    RTPConfiguration(RegionalTownyPermission plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() throws Exception{
        plugin.reloadConfig();
        FileConfiguration configuration = plugin.getConfig();
        for(String perm : configuration.getStringList("TownPermissions")){
            String[] split = perm.split(",",2);
            PermissionNode node
                    = PermissionNode.builder(split[0]).permission(split[0])
                        .value(true)
                        .expiry(Duration.ofHours(1)).build();
            plugin.townNodes.put(node, RegionalTownyPermission.TownyRelation.valueOf(split[1].toUpperCase()));
        }
        for(String perm : configuration.getStringList("NationPermissions")){
            String[] split = perm.split(",",2);
            PermissionNode node
                    = PermissionNode.builder(split[0]).permission(split[0])
                    .value(true)
                    .expiry(Duration.ofHours(1)).build();
            plugin.nationNodes.put(node, RegionalTownyPermission.TownyRelation.valueOf(split[1].toUpperCase()));
        }
    }
    public void reloadConfig(){
        plugin.townNodes.clear();
        plugin.nationNodes.clear();
        try{
            loadConfig();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
