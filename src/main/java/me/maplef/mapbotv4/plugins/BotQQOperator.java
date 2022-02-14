package me.maplef.mapbotv4.plugins;

import me.maplef.mapbotv4.Main;
import me.maplef.mapbotv4.MapbotPlugin;
import me.maplef.mapbotv4.exceptions.InvalidSyntaxException;
import me.maplef.mapbotv4.listeners.GroupListeners;
import me.maplef.mapbotv4.utils.BotOperator;
import net.mamoe.mirai.message.data.MessageChain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class BotQQOperator implements MapbotPlugin {
    static final FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    public static final Long botAcc = config.getLong("bot-account");
    public static final Long opGroup = config.getLong("op-group");
    private static final String botPassword = config.getString("bot-password");

    public static void login(){
        new BukkitRunnable(){
            @Override
            public void run() {
                getServer().getLogger().info("Mapbot正在登陆，请耐心等待...");
                BotOperator.login(botAcc, botPassword);
                BotOperator.getBot().getEventChannel().registerListenerHost(new GroupListeners());
                BotOperator.sendGroupMessage(opGroup, "Mapbot ON");
                getServer().getLogger().info("Mapbot登陆成功");
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public static void logout(){
        try {
            BotOperator.sendGroupMessage(opGroup, "Mapbot OFF");
            BotOperator.close();
        } catch (Exception ignored) {}
    }

    @Override
    public MessageChain onEnable(Long groupID, Long senderID, String[] args) throws Exception {
        if(args.length == 0)
            throw new InvalidSyntaxException();

        if(args[0].equals("login")) login();
        else if(args[0].equals("logout")) logout();
        else throw new IllegalArgumentException("未知的参数: " + args[0]);

        return null;
    }

    @Override
    public Map<String, Object> register() throws NoSuchMethodException {
        Map<String, Object> info = new HashMap<>();
        Map<String, Method> commands = new HashMap<>();
        Map<String, String> usages = new HashMap<>();

        commands.put("bot", BotQQOperator.class.getMethod("onEnable", Long.class, Long.class, String[].class));

        usages.put("bot", "#bot - 操作bot账号");

        info.put("name", "BotQQOperator");
        info.put("commands", commands);
        info.put("usages", usages);
        info.put("author", "Maplef");
        info.put("description", "操作bot账号");
        info.put("version", "1.0");

        return info;
    }
}