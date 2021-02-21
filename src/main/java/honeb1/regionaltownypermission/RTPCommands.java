package honeb1.regionaltownypermission;

import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.Permission;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;

public class RTPCommands implements CommandExecutor {
    RegionalTownyPermission plugin;
    final String noPermMessage = "権限がありません。";

    RTPCommands(RegionalTownyPermission plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission("rtperm.reload")){
                    sender.sendMessage(noPermMessage);
                    return false;
                }
                plugin.config.reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "[RegionalTownyPermission]"
                                    +ChatColor.WHITE+"設定を再読み込みしました。");
                return true;
            }
        }
        /*/テスト用
        else{
            sender.sendMessage(args[0]+" : "+args[1]+"->"
                    +plugin.getServer().getPlayer(args[0]).hasPermission(args[1]));
        }
        //*/
        return false;
    }
}
