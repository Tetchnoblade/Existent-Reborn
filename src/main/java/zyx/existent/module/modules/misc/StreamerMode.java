package zyx.existent.module.modules.misc;

import net.minecraft.network.play.server.SPacketChat;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventPacket;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.ChatUtils;

public class StreamerMode extends Module {
    private final String SPOOFSKINS = "SPOOFSKINS";
    private final String SCRAMBLE = "SCRAMBLENAMES";
    public static String YOURNAME = "YOURNAME";
    private String NAMEPROTECT = "NAMEPROTECT";
    public static boolean spoofskin;
    public static boolean scrambleNames;
    public static boolean nameprotect;
    public static String name;

    public StreamerMode(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(NAMEPROTECT, new Setting<>(NAMEPROTECT, false, "Protect your name."));
        settings.put(SPOOFSKINS, new Setting<>(SPOOFSKINS, false, "Spoofs player skins."));
        settings.put(SCRAMBLE, new Setting<>(SCRAMBLE, false, ""));
        settings.put(YOURNAME, new Setting<>(YOURNAME, "You", ""));
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        name = (String) settings.get(YOURNAME).getValue();
        spoofskin = (Boolean) settings.get(SPOOFSKINS).getValue();
        scrambleNames = (Boolean) settings.get(SCRAMBLE).getValue();
        nameprotect = (Boolean) settings.get(NAMEPROTECT).getValue();

        if (event.getPacket() != null && event.isIncoming() && event.getPacket() instanceof SPacketChat && (Boolean) settings.get(NAMEPROTECT).getValue()) {
            SPacketChat packet = (SPacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains(mc.thePlayer.getName())) {
                String temp = packet.getChatComponent().getFormattedText();
                ChatUtils.printChat(temp.replaceAll(mc.thePlayer.getName(), "\247d" + name + "\247r"));
                event.setCancelled(true);
            } else {
                String[] list = new String[]{"join", "left", "leave", "leaving", "lobby", "server", "fell", "died", "slain", "burn", "void", "disconnect", "kill", "by", "was", "quit", "blood", "game"};
                for (String str : list) {
                    if (packet.getChatComponent().getUnformattedText().toLowerCase().contains(str)) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
}
