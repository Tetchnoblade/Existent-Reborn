package zyx.existent.utils;

import net.minecraft.block.Block;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class PlayerUtils implements MCUtil {

    public static void damage() {
        NetHandlerPlayClient netHandler = mc.getConnection();
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        for (int i = 0; i < getMaxFallDist() / 0.05510000046342611D + 1.0D; i++) {
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 0.060100000351667404D, z, false));
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 5.000000237487257E-4D, z, false));
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
        }
        netHandler.sendPacketSilent(new CPacketPlayer(true));
    }

    public static void damage2() {
        for (int i = 0; i < 48; i++) {
            mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625D, mc.thePlayer.posZ, false));
            mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            if (i % 3 == 0)
                mc.thePlayer.connection.sendPacket(new CPacketKeepAlive(System.currentTimeMillis()));
        }
        mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6D, mc.thePlayer.posZ, false));
        mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }
    
    public static void damage3() {
        for(int i = 0; (double)i < 29.2D ; ++i) {
			   mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY + 0.0525D , mc.thePlayer.posZ, false));
			   mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY - 0.0525D, mc.thePlayer.posZ, false));
	    }
		  mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY , mc.thePlayer.posZ, true));
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.getPotionById(8));
        int f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0;
        return (mc.thePlayer.getMaxFallHeight() + f);
    }
}
