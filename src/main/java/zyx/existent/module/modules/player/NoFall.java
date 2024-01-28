package zyx.existent.module.modules.player;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.BlockPos;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventPacket;
import zyx.existent.event.events.EventPacketSend;
import zyx.existent.event.events.EventUpdate;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.MoveUtils;

public class NoFall extends Module {
    private final String MODE = "MODE";
    boolean canmeme;

    public NoFall(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(MODE, new Setting(MODE, new Options("Mode", "Hypixel", new String[]{"Normal", "Hypixel", "Spoof", "Packet"}), "NoFall method"));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();

            switch (currentmode) {
                case "Normal":
                    if (mc.thePlayer.onGround) {
                        canmeme = false;
                        mc.thePlayer.fallDistance = 0;
                    }
                    if (mc.thePlayer.fallDistance >= 2.95) {
                        event.setOnGround(canmeme = true);
                    } else {
                        canmeme = false;
                    }
                    break;
                case "Hypixel":
                    if (mc.thePlayer.fallDistance > 2.5F && (mc.thePlayer.posY % 0.0625D != 0.0D || mc.thePlayer.posY % 0.015256D != 0.0D)) {
                        mc.getConnection().sendPacket(new CPacketPlayer(true));
                        mc.thePlayer.fallDistance = 0.0F;
                    }
                    break;
                case "Spoof":
                    if (mc.thePlayer.fallDistance > 2) {
                        event.setOnGround(true);
                        mc.thePlayer.fallDistance = 0;
                    }
                    break;
                case "Packet":
                    if (mc.thePlayer.fallDistance > 2) {
                        mc.getConnection().getNetworkManager().sendPacketNoEvent(new CPacketPlayer(mc.thePlayer.ticksExisted % 2 == 0));
                    }
                    break;
            }
    }
    @EventTarget
    public void onPacketSent(EventPacketSend event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (canmeme) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0D); i > 0; ) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
                i--;
                continue;
            }
            return true;
        }
        return false;
    }
}
