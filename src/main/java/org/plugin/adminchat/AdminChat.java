package org.plugin.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class AdminChat extends JavaPlugin {

    public final Logger logger = Logger.getLogger("minecraft");

    //define default values
    public static String ac = "§c§lADMINCHAT>> ";
    public static String acPrefix = "§8[§4ADMINCHAT§8] ";
    public static String nameColor = "§c"; //§ is requid
    public static String textColor = ":§e "; //§ is requid
    public static String invalidUsage = "§cPlease provide a message!";
    public static String usage = "§6Usage: §a/ac <message>"; //It is behind ac string
    public static String permission = "ac";
    public static String permissionMessage = "§cYou have no permission to use this command!";
    public static boolean nonStaffUsage = true;
    public static String separator = ": ";
    public static String reloadSuccessfully = "§5Reload successfully!";
    public static String reloadPermission = permission + "reload";
    public static String helpBorder = "§b+-------------------------+";
    public static String helpUsageCommand = "§b/ac usage §4» §aShow the usage."; //§4§l»
    public static String helpReloadCommand = "§b/ac reload §4» §aReload §6config.yml §avalues";
    public static String helpUsage = "§b/ac <message> §4» §aSend a message to the admin chat.";
    public static ArrayList<String> help = new ArrayList<>();

    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        this.logger.info("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " started!");

        getCommand("ac").setExecutor(this);

        saveDefaultConfig();
        reloadConfig();

        FileConfiguration config = getConfig();

        ac = config.getString("ac");
        acPrefix = config.getString("acPrefix");
        nameColor = config.getString("nameColor");
        textColor = config.getString("textColor");
        separator = config.getString("textSeparator");
        permission = config.getString("permission");
        nonStaffUsage = config.getBoolean("nonStaffUsage");
        permissionMessage = config.getString("permissionMessage");
        reloadSuccessfully = config.getString("reloadSuccessfully");
        reloadPermission = permission + "." + config.getString("reloadPermission");
        helpBorder = config.getString("help.border");
        helpUsageCommand = config.getString("help.usageCommand");
        helpUsage = config.getString("help.usage");

        help = new ArrayList<>();
        help.add(helpBorder);
        help.add(helpUsageCommand);
        help.add(helpReloadCommand);
        help.add(helpUsage);
        help.add(helpBorder);
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        this.logger.info("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " stopped!");

        saveDefaultConfig();
        reloadConfig();

        FileConfiguration config = getConfig();

        ac = config.getString("ac");
        acPrefix = config.getString("acPrefix");
        nameColor = config.getString("nameColor");
        textColor = config.getString("textColor");
        helpBorder = config.getString("help.border");
        helpUsageCommand = config.getString("help.usageCommand");
        helpUsage = config.getString("help.usage");

        help = new ArrayList<>();
        help.add(helpBorder);
        help.add(helpUsageCommand);
        help.add(helpReloadCommand);
        help.add(helpUsage);
        help.add(helpBorder);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ac")) {
            Player player;

            if (args.length == 0) {
                sender.sendMessage(ac + invalidUsage);
                return true;
            }

            if (args[0].equalsIgnoreCase("usage")) {
                sender.sendMessage(ac + usage);
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                help.forEach(s -> sender.sendMessage(s));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    if (player.hasPermission(reloadPermission) || player.hasPermission("ac.*")) {
                        reloadConfigValues(sender);
                        return true;
                    } else {
                        player.sendMessage(ac + "You have no permission to do this!");
                    }
                }
                reloadConfigValues(sender);
                return true;
            }

            if (sender instanceof Player) {
                player = (Player) sender;
                String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                if (nonStaffUsage) {
                    if (player.hasPermission(permission)) {
                        if (!player.hasPermission("ac")) {
                            player.sendMessage(acPrefix + nameColor + player.getName() + separator + textColor + message);
                        }
                        Send send = () -> {
                            for (Player recipient : getServer().getOnlinePlayers()) {
                                if (recipient.hasPermission(ac)) recipient.sendMessage(acPrefix + nameColor + player.getDisplayName() + separator + textColor + message);
                            }
                        };
                        send.send();
                    } else {
                        player.sendMessage(permissionMessage);
                    }
                } else {
                    if (!player.hasPermission("ac")) {
                        player.sendMessage(acPrefix + nameColor + player.getName() + separator + textColor + message);
                    }
                    Send send = () -> {
                        for (Player recipient : getServer().getOnlinePlayers()) {
                            if (recipient.hasPermission(ac)) recipient.sendMessage(acPrefix + nameColor + player.getDisplayName() + separator + textColor + message);
                        }
                    };
                    send.send();
                }
                return true;
            }

            if (!(sender instanceof Player)) {
                String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                ((Send) () -> {
                    for (Player recipient : getServer().getOnlinePlayers()) {
                        if (recipient.hasPermission(ac))
                            recipient.sendMessage(acPrefix + ChatColor.BLACK + "CONSOLE" + separator + textColor + message);
                    }
                }).send();
                sender.sendMessage(acPrefix + ChatColor.BLACK + "CONSOLE" + separator + textColor + message);
                return true;
            }
        }

        return true;
    }

    public void reloadConfigValues(CommandSender sender) {
        saveDefaultConfig();
        reloadConfig();

        FileConfiguration config = getConfig();

        ac = config.getString("ac");
        acPrefix = config.getString("acPrefix");
        nameColor = config.getString("nameColor");
        textColor = config.getString("textColor");
        separator = config.getString("textSeparator");
        permission = config.getString("permission");
        nonStaffUsage = config.getBoolean("nonStaffUsage");
        permissionMessage = config.getString("permissionMessage");
        helpBorder = config.getString("help.border");
        helpUsageCommand = config.getString("help.usageCommand");
        helpUsage = config.getString("help.usage");

        help = new ArrayList<>();
        help.add(helpBorder);
        help.add(helpUsageCommand);
        help.add(helpReloadCommand);
        help.add(helpUsage);
        help.add(helpBorder);

        sender.sendMessage( ac + reloadSuccessfully);
    }
}
