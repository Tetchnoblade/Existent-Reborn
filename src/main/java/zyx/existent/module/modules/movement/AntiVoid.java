package zyx.existent.module.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventMove;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.timer.Timer;

public class AntiVoid extends Module {
    private Timer timer = new Timer();
    private boolean saveMe;
    private String VOID = "VOID";
    private String MODE = "MODE";
    private String DISTANCE = "DIST";

    public AntiVoid(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(VOID, new Setting<>(VOID, true, "VoidCheck."));
        settings.put(DISTANCE, new Setting<>(DISTANCE, 5, "The fall distance needed to catch.", 1, 1, 10));
        settings.put(MODE,new Setting<>(MODE,new Options("Mode", "Hypixel", new String[] { "Hypixel", "Motion"}),"AntiVoid method."));
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        if ((saveMe && timer.delay(150)) || mc.thePlayer.isCollidedVertically) {
            saveMe = false;
            timer.reset();
        }
        int dist = ((Number) settings.get(DISTANCE).getValue()).intValue();
        if (mc.thePlayer.fallDistance > dist && !(Existent.getModuleManager().isEnabled(Flight.class) && Existent.getModuleManager().isEnabled(Glide.class))) {
            if (!((Boolean) settings.get(VOID).getValue()) || !isBlockUnder()) {
                if (!saveMe) {
                    saveMe = true;
                    timer.reset();
                }
                mc.thePlayer.fallDistance = 0;
                switch (((Options) settings.get(MODE).getValue()).getSelected()) {
                    case "Hypixel":
                        mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY + 12, mc.thePlayer.posZ, false));
                        break;
                    case "Motion":
                        eventMove.setY(mc.thePlayer.motionY = 0);
                        break;
                }
            }
        }
    }

    private boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0, -off, 0);
            if (!mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
