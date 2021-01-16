package honeb1.regionaltownypermission;

import com.palmergames.bukkit.towny.TownyAPI;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RegionalTownyPermission extends JavaPlugin {
    LuckPerms luckPermsApi;
    TownyAPI townyApi;
    PlayerPosChecker posChecker;
    List<PermissionNode> townNodes = new ArrayList<>();
    List<PermissionNode> nationNodes = new ArrayList<>();
    RTPConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("regionalperm").setExecutor(new RTPCommands(this));
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();
        }
        townyApi = TownyAPI.getInstance();

        posChecker = new PlayerPosChecker(this);
        posChecker.run();

        config = new RTPConfiguration(this);
        config.loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
