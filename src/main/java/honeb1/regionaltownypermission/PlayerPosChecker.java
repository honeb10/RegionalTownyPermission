package honeb1.regionaltownypermission;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
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

        if(!plugin.townyApi.isWilderness(player.getLocation())) {//町中
            RegionalTownyPermission.TownyRelation relation;
            Town playerTown;
            try {
                playerTown = plugin.townyApi.getDataSource().getResident(player.getName()).getTown();
            }catch (NotRegisteredException e){
                playerTown = null;
            }
            Town locTown = plugin.townyApi.getTown(player.getLocation());


            if(playerTown == null)
                relation = RegionalTownyPermission.TownyRelation.OUTSIDER;
            else{
                if(locTown.equals(playerTown))
                    relation = RegionalTownyPermission.TownyRelation.RESIDENT;
                else{
                    try {
                        relation = playerTown.getNation().equals(locTown.getNation()) ?
                                RegionalTownyPermission.TownyRelation.ALLY :
                                RegionalTownyPermission.TownyRelation.OUTSIDER;
                    }catch (NotRegisteredException e){
                        relation = RegionalTownyPermission.TownyRelation.OUTSIDER;
                    }
                }
            }
            //街との関係の状態に応じて権限変更
            for(PermissionNode node : plugin.townNodes.keySet()){
                if(plugin.townNodes.get(node).weight <= relation.weight)
                    nodesToAdd.add(node);
                else
                    nodesToRemove.add(node);
            }


            if (locTown != null && locTown.hasNation()) {//国の中
                Nation playerNation;
                try {
                    playerNation = playerTown.getNation();
                }catch (Exception e){
                    playerNation = null;
                }
                try {
                    relation = locTown.getNation().equals(playerNation) ?
                            RegionalTownyPermission.TownyRelation.RESIDENT :
                            RegionalTownyPermission.TownyRelation.OUTSIDER; //国との関係
                }catch (NotRegisteredException e){//起こらない
                }

                //邦との関係の状態に応じて権限変更
                for(PermissionNode node : plugin.nationNodes.keySet()){
                    if(plugin.nationNodes.get(node).weight <= relation.weight)
                        nodesToAdd.add(node);
                    else
                        nodesToRemove.add(node);
                }
            }else{
                nodesToRemove.addAll(plugin.nationNodes.keySet());//国で付与される権限をはく奪
            }
        }else{//街、国の権限をはく奪
            nodesToRemove.addAll(plugin.nationNodes.keySet());
            nodesToRemove.addAll(plugin.townNodes.keySet());
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
