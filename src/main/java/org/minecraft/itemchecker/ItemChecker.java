package org.minecraft.itemchecker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.itemchecker.baseclasses.YamlData;

public final class ItemChecker extends JavaPlugin implements Listener {

    public ItemCheckManager icm = new ItemCheckManager();
    @Override
    public void onEnable() {
        // Plugin startup logic
        //getCommand("itemchecker").setExecutor(new CommandClass());
        icm.LoadFile();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent e){
        ItemStack stack = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if(stack != null) {
            Player player = e.getPlayer();
            icm.CheckItem(player, stack, e.getNewSlot());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("itemchecker")){
            Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage("§8--------------------§f[§6ItemChecker§f]§8--------------------");
                p.sendMessage("-§bプラグイン説明");
                p.sendMessage("&6このプラグインは条件に一致したアイテムを自動的に削除し、コマンドを実行するプラグインです");
                p.sendMessage("-§bYamlファイルの形式");
                p.sendMessage("[定義名]§7:");
                p.sendMessage("  CheckNBT§7: §atest");
                p.sendMessage("  └§6#NBTに含まれている文字列");
                p.sendMessage("  ContainedText§7: §atestitem");
                p.sendMessage("  └§6#アイテム名に含まれている文字列");
                p.sendMessage("  ConsoleCommand§7: §a/kill %player%");
                p.sendMessage("  └§6#上記を両方満たした場合に実行されるコマンド(%player%=所持者)");
                p.sendMessage("-§b使用可能コマンド");
                p.sendMessage(" §a/ic reload");
                p.sendMessage(" └§6Yamlファイルのリロード");
                p.sendMessage(" §a/ic show");
                p.sendMessage(" └§6読み込まれているリストの表示");
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    icm.LoadFile();
                    p.sendMessage("[§6ItemChecker§f]§e リロード完了");
                    p.sendMessage("[§6ItemChecker§f]§b " + icm.list.size() + "§e個読み込まれました");
                } else if(args[0].equalsIgnoreCase("show")) {
                    p.sendMessage("[§6ItemChecker§f]§b " + icm.list.size() + "§e個読み込まれています");
                    String nameList = new String();
                    for(YamlData data: icm.list){
                        nameList += data.name;
                        nameList += ",";
                    }
                    p.sendMessage("§b" + nameList);
                }
                else{
                    p.sendMessage("[§6ItemChecker§f]§c 不明なコマンド");
                }
            }
            return true;
        }
        return false;
    }
}
