package honeb1.regionaltownypermission;

import com.palmergames.bukkit.towny.TownyAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.graalvm.compiler.lir.LIRInstruction;
import org.w3c.dom.Node;

import java.security.Permission;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerPosChecker implements Runnable{
    RegionalTownyPermission plugin;
    PlayerPosChecker(RegionalTownyPermission plugin){
        this.plugin = plugin;
    }

    @Override
    public void run(){
        for(Player player : plugin.getServer().getOnlinePlayers()){
            processPlayerCheck(player);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);//次回予約
    }

    void processPlayerCheck(Player player){
        List<PermissionNode> nodesToAdd = new ArrayList<>();
        List<PermissionNode> nodesToRemove = new ArrayList<>();

        if(!plugin.townyApi.isWilderness(player.getLocation())) {//街なか
            nodesToAdd.addAll(plugin.townNodes);//街で付与される権限を付与
            if (plugin.townyApi.getTown(player.getLocation()).hasNation()) {//国
                nodesToAdd.addAll(plugin.nationNodes);//国で付与される権限を付与
            }else{
                nodesToRemove.addAll(plugin.nationNodes);//国で付与される権限をはく奪
            }
        }else{//街、国の権限をはく奪
            nodesToRemove.addAll(plugin.nationNodes);
            nodesToRemove.addAll(plugin.townNodes);
        }

        for (PermissionNode node : nodesToAdd) {
            //権限付与
            plugin.luckPermsApi.getPlayerAdapter(Player.class).getUser(player).data().add(node);
        }
        for (PermissionNode node : nodesToRemove) {
            //権限剥奪
            plugin.luckPermsApi.getPlayerAdapter(Player.class).getUser(player).data().remove(node);
        }
    }
}
