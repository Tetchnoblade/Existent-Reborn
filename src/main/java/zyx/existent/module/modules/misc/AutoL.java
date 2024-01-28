package zyx.existent.module.modules.misc;

import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.text.TextFormatting;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.*;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.module.modules.combat.KillAura;
import zyx.existent.utils.ChatUtils;
import zyx.existent.utils.timer.Timer;

import java.util.ArrayList;

public class AutoL extends Module {
    private final Timer LTimer = new Timer();
    public ArrayList<String> sendQueue = new ArrayList<>();
    public static final String MESSAGE = "MESSAGE";
    private final String MODE = "MODE";
    private final String DELAY = "DELAY";

    public AutoL(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Global", new String[]{"Global", "Direct", "Normal"}), "AutoL method"));
        settings.put(MESSAGE, new Setting<>(MESSAGE, "L", "AutoL Text"));
        settings.put(DELAY, new Setting<>(DELAY, 2, "LDelay", 1, 1, 5));
    }

    @EventTarget
    public void onTick(EventTick event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
        String message = (String) settings.get(MESSAGE).getValue();

        if (!this.sendQueue.isEmpty() && this.LTimer.delay(((Number) settings.get(DELAY).getValue()).floatValue() * 1000)) {
            String user = this.sendQueue.get(0);
            sendMessage(user, currentmode, message);
            this.sendQueue.remove(0);
            this.LTimer.reset();
        }
    }
    @EventTarget
    public void onPacket(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat) event.getPacket();
            String message = TextFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getUnformattedText());
            if (message != null && message.length() > 0) {
                String[] text = message.split(" ");
                if ((text[1].equalsIgnoreCase("killed") || text[1].equalsIgnoreCase("shot")) && text[0].startsWith(mc.thePlayer.getName() + "(")) {
                    String user = text[2].replaceAll("\\(.+?\\)", "");
                    this.sendQueue.add(user);
                }
            }
        }
    }

    private void sendMessage(String user, String mode, String message) {
        if (mode.equalsIgnoreCase("Global")) {
            mc.thePlayer.sendChatMessage("!" + user + " " + message);
        } else if (mode.equalsIgnoreCase("Direct")) {
            mc.thePlayer.sendChatMessage("@" + user + " " + message);
        } else if (mode.equalsIgnoreCase("Normal")) {
            mc.thePlayer.sendChatMessage(user + " " + message);
        }
    }
}
