package zyx.existent.module.modules.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventUpdate;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.utils.timer.Timer;

public class AntiImmobilizer extends Module {
    public AntiImmobilizer(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.isPotionActive(Potion.getPotionById(2)) && mc.thePlayer.onGround && mc.thePlayer.getActivePotionEffect(Potion.getPotionById(2)).getDuration() < 10000) {
            Potion.getPotionById(2).removeAttributesModifiersFromEntity(mc.thePlayer, mc.thePlayer.getAttributeMap(), 255);
            mc.thePlayer.setAIMoveSpeed(0.13000001F);

            for (int i = 0; i < mc.thePlayer.getActivePotionEffect(Potion.getPotionById(2)).getDuration() / 20; ++i) {
                mc.getConnection().sendPacket(new CPacketPlayer(mc.thePlayer.onGround));
            }
        }
    }
}
