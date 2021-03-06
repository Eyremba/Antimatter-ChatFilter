/*
AntiUrl which actually blocks it unlike half these shitty plugins
AntiIP which actually blocks it unlike half these shitty plugins
AntiSwear with a replacement config of my choice
ClearChat to do what it says on the tin
AntiBot to stop those pests
Antispam To stop people being able to type the same characters repeatedly in chat and say they said 'noooooooo' it would block that
AntiCaps as a percentage that I can reduce or extend for example if I had the config as 50% if they have more than 50% of their sentence in caps itll make it non-caps
 */
package com.djrapitops.antimatter;

import com.djrapitops.antimatter.filters.AntiReplacer;
import com.djrapitops.antimatter.listener.AntimatterChatListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

public class Antimatter extends JavaPlugin {

    private HashMap<UUID, String> lastMessages;
    private AntiReplacer replacer;
    private AntimatterChatListener listener;
    
    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        getConfig().options().copyDefaults(true);

        getConfig().options().header("Antimatter Filter - Config\n"
                + "debug - Errors are saved in Errors.txt when they occur\n"
                + "maxcapspercent - How much % of a message can be Caps before it is lowercased (default 40)\n"
                + "spamsimilaritypercent - How similar two messages must be to count as spam (default 90)\n"
                + "sendMsgSenderMsgIfBlocked - Notify the sender of the message if the message is blocked\n"
                + "enabled - Disable various parts and filters here\n"
                + "replacerules - wordtoreplace > replacewith\n"
                + ""
        );

        saveConfig();
        
        getCommand("amclear").setExecutor(new ClearChatCommand());
        replacer = new AntiReplacer();
        getCommand("amreload").setExecutor(new ReloadCommand(replacer));
        lastMessages = new HashMap<>();
        log("Registering Chat Listener");
        listener = new AntimatterChatListener(this);
        log("Antimatter Filter enabled.");
    }

    @Override
    public void onDisable() {
        log("Antimatter Filter disabled.");
    }

    public HashMap<UUID, String> getLastMessages() {
        return lastMessages;
    }

    public AntiReplacer getReplacer() {
        return replacer;
    }
    
    

    public void log(String message) {
        getLogger().info(message);
    }

    public void logError(String message) {
        getLogger().severe(message);
    }

    public void logToFile(String message) {
        if (getConfig().getBoolean("debug")) {
            File folder = getDataFolder();
            if (!folder.exists()) {
                folder.mkdir();
            }
            File log = new File(getDataFolder(), "Errors.txt");
            try {
                if (!log.exists()) {
                    log.createNewFile();
                }
                FileWriter fw = new FileWriter(log, true);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.println(message + "\n");
                    pw.flush();
                }
            } catch (IOException e) {
                logError("Failed to create log.txt file");
            }
        }
    }
}
