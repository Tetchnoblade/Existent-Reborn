package zyx.existent.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.*;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.ChatUtils;
import zyx.existent.utils.MathUtils;
import zyx.existent.utils.MoveUtils;
import zyx.existent.utils.PlayerUtils;
import zyx.existent.utils.misc.MiscUtils;
import zyx.existent.utils.pathfinding.CustomVec3d;
import zyx.existent.utils.timer.TickTimer;
import zyx.existent.utils.timer.Timer;

public class Flight extends Module {
    private final Timer timer = new Timer();
    private final Timer groundTimer = new Timer();
    private int counter, level, stage, ticks;
    private double moveSpeed, lastDist, y;

    private String MODE = "MODE";
    private String SHOTBOWMODE = "SHOTBOWMODE";
    private String SPEED = "SPEED";
    private String BOBBING = "BOBBING";
    private String MULTIPLIER = "MULTIPLIER";
    private String KICKBYPASS = "KICKBYPASS";
    private String ONGROUND = "ONGROUND";

    public Flight(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(MODE,new Setting<>(MODE,new Options("Mode", "Hypixel", new String[] {"Hypixel", "CubeCraft", "Shotbow", "Float", "Vanilla", "Smooth"}),"Flight method."));
        settings.put(SHOTBOWMODE,new Setting<>(SHOTBOWMODE, new Options("Shotbow Mode", "Normal", new String[] {"Normal", "OldFast"}),"SubMode method."));
        settings.put(SPEED, new Setting<>(SPEED, 1.0, "Speed.", 0.1, 0.1, 10.0));
        settings.put(BOBBING, new Setting<>(BOBBING, true, "ViewBobbing"));
        settings.put(MULTIPLIER, new Setting<>(MULTIPLIER, true, "Timer Boost"));
        settings.put(KICKBYPASS, new Setting<>(KICKBYPASS, true, "Vnilla KickBypass"));
        settings.put(ONGROUND, new Setting<>(ONGROUND, false, "OngroundSet"));
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null)
            return;

        this.y = 0.0D;
        this.lastDist = 0.0D;
        this.moveSpeed = 0.0D;
        this.counter = 0;
        this.stage = 0;
        this.ticks = 0;

        switch (((Options) settings.get(MODE).getValue()).getSelected()) {
            case "Hypixel":
                mc.thePlayer.motionX *= 0.0;
                mc.thePlayer.motionZ *= 0.0;
                break;
            case "Shotbow":
                switch (((Options) settings.get(SHOTBOWMODE).getValue()).getSelected()) {
                    case "Normal":
                        MoveUtils.setMotion(0.3 + MoveUtils.getSpeedEffect() * 0.05f);
                        break;
                    case "OldFast":
                        //PlayerUtils.damage2();
                        PlayerUtils.damage3();
                        this.mc.thePlayer.motionY = 0.41999998688698f;
                        level = 1;
                        timer.reset();
                }
                break;
            case "Float":
                MoveUtils.setMotion(0.3 + MoveUtils.getSpeedEffect() * 0.05f);
                mc.thePlayer.motionY = 0.41999998688698f + MoveUtils.getJumpEffect() * 0.1;
                mc.thePlayer.jumpMovementFactor = 0;
        }
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.jumpMovementFactor = 0;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.stepHeight = 0.625F;
        MoveUtils.setMotion(0.2F);
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel")) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.y, mc.thePlayer.posZ);
        } else if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Shotbow")) {
            if (((Options) settings.get(SHOTBOWMODE).getValue()).getSelected().equalsIgnoreCase("Zoom")) {
                mc.thePlayer.motionY = -0.003;
            }
        }

        this.y = 0.0D;
        this.lastDist = 0.0D;
        this.moveSpeed = 0.0D;
        this.counter = 0;
        this.stage = 0;
        this.ticks = 0;
        this.timer.reset();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
        String shotbowmode = ((Options) settings.get(SHOTBOWMODE).getValue()).getSelected();
        double speed = ((Number) settings.get(SPEED).getValue()).doubleValue();

        if ((Boolean) settings.get(BOBBING).getValue())
            mc.thePlayer.cameraYaw = 0.03F;
        if ((Boolean) settings.get(ONGROUND).getValue())
            mc.thePlayer.onGround = true;
        if ((Boolean) settings.get(MULTIPLIER).getValue()) {
            if (!timer.delay(1190)) {
                mc.timer.timerSpeed = 1.8F;
            } else {
                mc.timer.timerSpeed = 1.0F;
            }
        }

        switch (currentmode) {
            case "Hypixel":
                if (event.isPre()) {
                    if (this.stage > 2) {
                        mc.thePlayer.motionY = 0.0;
                    }
                    if (this.stage > 2) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.003, mc.thePlayer.posZ);
                        ++this.ticks;
                        switch (this.ticks) {
                            case 1:
                                this.y *= -0.949999988079071;
                                break;
                            case 2:
                            case 3:
                            case 4:
                                // offset
                                this.y += 3.25E-4;
                                break;
                            case 5:
                                this.y += 5.0E-4;
                                this.ticks = 0;
                                break;
                        }
                        event.setY(mc.thePlayer.posY + this.y);
                    }
                    break;
                } else if (this.stage > 2) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.003, mc.thePlayer.posZ);
                }
                break;
            case "CubeCraft":
                if (mc.thePlayer.onGround) {
                    this.moveSpeed = 1.4;
                    PlayerUtils.damage2();
                    mc.thePlayer.jump();
                } else {
                    ++this.stage;
                    double x2 = mc.thePlayer.posX;
                    double y2 = mc.thePlayer.posY;
                    double z2 = mc.thePlayer.posZ;
                    switch (this.stage) {
                        case 1: {
                            mc.thePlayer.setPosition(x2, y2 + 1.0E-12, z2);
                            break;
                        }
                        case 2: {
                            mc.thePlayer.setPosition(x2, y2 - 1.0E-12, z2);
                            break;
                        }
                        case 3: {
                            mc.thePlayer.setPosition(x2, y2 + 1.0E-12, z2);
                            this.stage = 0;
                            break;
                        }
                    }
                    mc.thePlayer.motionY = 0.0;
                    MoveUtils.setMotion(this.moveSpeed);
                    if (timer.delay(25L) && this.moveSpeed > 0.26) {
                        this.moveSpeed -= 0.035;
                        timer.reset();
                    }
                }
                break;
            case "Shotbow":
                if (shotbowmode.equalsIgnoreCase("OldFast")) {
                    ++this.counter;
                    if (!mc.thePlayer.onGround) {
                        switch (counter) {
                            case 1:
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
                                break;
                            case 2:
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10D, mc.thePlayer.posZ);
                                counter = 0;
                                break;
                        }
                        mc.thePlayer.motionY = 0.0D;
                    }
                } else if (shotbowmode.equalsIgnoreCase("Normal")) {
                    mc.thePlayer.motionY = -0.003D;
                }
                break;
            case "Float":
                ++counter;
                if (MovementInput.moveForward == 0.0F && MovementInput.moveStrafe == 0.0F) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + 1.0D, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ + 1.0D);
                    mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ);
                }
                mc.thePlayer.motionY = 0.0D;
                if (counter == 2 && !mc.thePlayer.onGround) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
                    counter = 0;
                }
                if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.mc.thePlayer.motionY = 0.5;
                } else if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    this.mc.thePlayer.motionY = -0.5;
                }
                break;
            case "Vanilla":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY += speed;
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY -= speed;
                handleVanillaKickBypass();
                break;
            case "Smooth":
                mc.thePlayer.capabilities.isFlying = true;
                handleVanillaKickBypass();
                break;
        }
        if (event.isPre()) {
            double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
        }
    }
    @EventTarget
    public void onMove(EventMove event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
        String shotbowmode = ((Options) settings.get(SHOTBOWMODE).getValue()).getSelected();
        TargetStrafe targetStrafe = (TargetStrafe) Existent.getModuleManager().getClazz(TargetStrafe.class);
        double speed = ((Number) settings.get(SPEED).getValue()).doubleValue();

        switch (currentmode) {
            case "Hypixel":
                if (mc.thePlayer.isMoving()) {
                    switch (this.stage) {
                        case 0: {
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                PlayerUtils.damage();
                                this.moveSpeed = 2.5 * speed;
                                break;
                            }
                            break;
                        }
                        case 1: {
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                final EntityPlayerSP entityPlayerSP = mc.thePlayer;
                                final double jumpBoostModifier = MoveUtils.getJumpBoostModifier(0.39999994);
                                entityPlayerSP.motionY = jumpBoostModifier;
                                event.setY(jumpBoostModifier);
                            }
                            this.moveSpeed *= 2.149;
                            break;
                        }
                        case 2: {
                            double boost = mc.thePlayer.isPotionActive(Potion.getPotionById(1)) ? 1.85D : 2.12;
                            this.moveSpeed = boost * MoveUtils.getBaseMoveSpeed();
                            break;
                        }
                        default: {
                            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                            break;
                        }
                    }
                }
                if (targetStrafe.canStrafe()) {
                    targetStrafe.strafe(event, Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed()));
                } else {
                    MoveUtils.setMotion(event, Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed()));
                }
                ++this.stage;
                break;
            case "Shotbow":
                if (shotbowmode.equalsIgnoreCase("OldFast")) {
                    if (level != 1) {
                        if (level == 2) {
                            level = 3;
                            moveSpeed *= 2.149D;
                            mc.timer.timerSpeed = 1.0f;
                        } else if (level == 3) {
                            level = 4;
                            double difference = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D) * (lastDist - MoveUtils.getBaseMoveSpeed());
                            moveSpeed = lastDist - difference;
                        } else {
                            moveSpeed = lastDist - lastDist / 159.0D;
                            if (moveSpeed <= 0.600 && moveSpeed >= 0.400) {
                                mc.timer.elapsedPartialTicks = 0.22F;
                            }
                        }
                    } else {
                        level = 2;
                        double boost = mc.thePlayer.isPotionActive(Potion.getPotionById(1)) ? 1.65D : 2.0;
                        this.moveSpeed = boost * MoveUtils.getBaseMoveSpeed();
                    }

                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(event, Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed()));
                    } else {
                        MoveUtils.setMotion(event, Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed()));
                    }
                } else if (shotbowmode.equalsIgnoreCase("Normal")) {
                    if (this.stage == 1 && (mc.thePlayer.field_191988_bg != 0.0F || mc.thePlayer.moveStrafing != 0.0F)) {
                        this.moveSpeed = 1.2D * MoveUtils.getBaseMoveSpeed() - 0.01D;
                    } else if (this.stage == 2) {
                        event.setY(0.425D);
                        mc.thePlayer.motionY = 0.425D;
                        this.moveSpeed *= 2.1D;
                    } else if (this.stage == 3) {
                        double difference = 0.66D * (this.lastDist - MoveUtils.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    } else {
                        if (mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically)
                            this.stage = 4;
                        this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                    }
                    this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed());
                    MoveUtils.setMotion(this.moveSpeed);
                    if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F)
                        this.stage++;
                }
                break;
            case "Vanilla":
            case "Float":
                if (targetStrafe.canStrafe()) {
                    targetStrafe.strafe(event, speed);
                } else {
                    MoveUtils.setMotion(event, speed);
                }
                break;
        }
    }
    @EventTarget
    public void onSendPacket(EventPacketSend event) {
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel") && this.stage == 0) {
            event.setCancelled(true);
        }
    }

    private void handleVanillaKickBypass() {
        if (!(Boolean) settings.get(KICKBYPASS).getValue() || !groundTimer.delay(1000))
            return;

        final double ground = calculateGround();

        for (double posY = mc.thePlayer.posY; posY > ground; posY -= 8D) {
            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if (posY - 8D < ground)
                break; // Prevent next step
        }

        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true));

        for (double posY = ground; posY < mc.thePlayer.posY; posY += 8D) {
            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if (posY + 8D > mc.thePlayer.posY)
                break; // Prevent next step
        }
        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        groundTimer.reset();
    }
    private double calculateGround() {
        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for (double ground = mc.thePlayer.posY; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }
}
