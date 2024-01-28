package zyx.existent.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import zyx.existent.Existent;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventMove;
import zyx.existent.event.events.EventPacket;
import zyx.existent.event.events.EventUpdate;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.ChatUtils;
import zyx.existent.utils.MoveUtils;
import zyx.existent.utils.timer.Timer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Speed extends Module {
    private final String MODE = "MODE";
    private double moveSpeed, lastDist;
    public int stage;
    public Timer timer = new Timer();
    public Timer lastCheck = new Timer();
    public Timer damageTimer = new Timer();
    private boolean slowDownHop;
    private double less;
    private double stair;
    private int state;
	private int state1;
	private int state2;
	private int state3;
	private int state4;
	private int state5;
	private int state6;
	private int state7;
	private int state8;
	private int state9;
	private int state10;
	private int state11;
	private int state12;
	private int state13;
	private int state14;
	String DamageBoost = "DamageBoost";
    public Speed(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);
        settings.put(DamageBoost, new Setting<>(DamageBoost, false, "DamageBoostSpeed"));
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Bhop", new String[]{"Bhop", "Hypixel", "OnGround", "DamageHop", "NigamiHop", "LegitHop"}), "Speed method."));
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            moveSpeed = MoveUtils.getBaseMoveSpeed();
            this.less = 0.0D;
            lastDist = 0.0;
            stage = 2;
            state3 = 0;
            state4=0;
            mc.timer.timerSpeed = 1;
            this.damageTimer.reset();
            if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("DamageHop")) {
                stage = 0;
                damage2();
            }
        }
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1;
        stage = 1;
        this.moveSpeed = MoveUtils.getBaseMoveSpeed();
        super.onDisable();
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        String currentMode = ((Options) settings.get(MODE).getValue()).getSelected();
        TargetStrafe targetStrafe = (TargetStrafe) Existent.getModuleManager().getClazz(TargetStrafe.class);

        switch (currentMode) {
            case "Bhop":
                if (MovementInput.moveForward == 0.0f && MovementInput.moveStrafe == 0.0f) {
                    this.moveSpeed = MoveUtils.getBaseMoveSpeed();
                }
                if (stage == 1 && mc.thePlayer.isCollidedVertically && (MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F)) {
                    this.moveSpeed = 1.35 + MoveUtils.getBaseMoveSpeed() - 0.01;
                }

                if (!mc.thePlayer.isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && (MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F)) {
                    double motY = 0.407D + MoveUtils.getJumpEffect() * 0.1D;
                    this.mc.thePlayer.motionY = motY;
                    eventMove.setY(motY);
                    mc.thePlayer.jump();
                    state7 = 0;
                    this.moveSpeed *= 1.533D;
                    if(state4 > 0) {
                    	this.moveSpeed *= 1.4D;
                    	state4--;
                    }
                } else if (stage == 3) {
                    final double difference = 0.66 * (lastDist - MoveUtils.getBaseMoveSpeed());
                    this.moveSpeed = lastDist - difference;
                } else {
                    final List collidingList = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
                    if ((collidingList.size() > 0 || MoveUtils.isOnGround(0.01)) && stage > 0) {
                        stage = ((MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F) ? 1 : 0);
                    }
                    this.moveSpeed = lastDist - lastDist / 159.0;
                }
                if(!mc.thePlayer.canRenderOnFire()) {
               //	ChatUtils.printChat("bruh");
                	 state3 = 0;
                }
                if(mc.thePlayer.hurtTime >= 9&&this.moveSpeed < 0.6&&(Boolean) settings.get(DamageBoost).getValue()&&state3==0) {
              	  this.moveSpeed *= 1.6D;
              	 state4 = 1;
                }

                this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed());

                if (stage > 0) {
                    if (mc.thePlayer.isInLiquid())
                        this.moveSpeed = 0.1;
                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(eventMove, this.moveSpeed);
                    } else {
                        MoveUtils.setMotion(eventMove, this.moveSpeed);
                    }
                }
                if (mc.thePlayer.isMoving()) {
                    ++stage;
                }
                break;
            case "Hypixel":
                boolean collided = this.mc.thePlayer.isCollidedHorizontally;
                if (collided)
                    this.stage = -1;
                if (this.stair > 0.0D)
                    this.stair -= 0.25D;
                this.less -= (this.less > 1.0D) ? 0.12D : 0.11D;
                if (this.less < 0.0D)
                    this.less = 0.0D;
                if (!this.mc.thePlayer.isInWater() && this.mc.thePlayer.onGround && this.mc.thePlayer.isMoving()) {
                    collided = this.mc.thePlayer.isCollidedHorizontally;
                    if (this.stage >= 0 || collided) {
                        this.stage = 0;
                        double motY = 0.407D + MoveUtils.getJumpEffect() * 0.1D;
                        if (this.stair == 0.0D) {
                            this.mc.thePlayer.motionY = motY;
                            eventMove.setY(this.mc.thePlayer.motionY = motY);
                        }
                        this.less++;
                        this.slowDownHop = this.less > 1.0D && !this.slowDownHop;
                        if (this.less > 1.12D)
                            this.less = 1.12D;
                    }
                }
                this.moveSpeed = getCurrentSpeed(this.stage) + 0.0312D;
                this.moveSpeed *= 0.8500000000000001D;
                if (this.stair > 0.0D)
                    this.moveSpeed *= 0.72D - MoveUtils.getSpeedEffect() * 0.206D;
                if (this.stage < 0)
                    this.moveSpeed = MoveUtils.getBaseMoveSpeed();
                if (this.slowDownHop)
                    this.moveSpeed *= 0.825D;
                if (this.mc.thePlayer.isInWater())
                    this.moveSpeed = 0.12D;
                if (this.mc.thePlayer.isMoving()) {
                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(eventMove, this.moveSpeed);
                    } else {
                        MoveUtils.setMotion(eventMove, this.moveSpeed);
                    }
                    this.stage++;
                }
                break;
            case "OnGround":
                if (mc.thePlayer.field_191988_bg == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
                    this.moveSpeed = MoveUtils.getBaseMoveSpeed();
                }
                if (this.stage == 0 && (mc.thePlayer.field_191988_bg != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                    this.stage = 1;
                    this.moveSpeed = MoveUtils.getBaseMoveSpeed() - 0.01;
                } else if (this.stage == 1 && (mc.thePlayer.field_191988_bg != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                    this.stage = 2;
                    this.moveSpeed = 1.63 * MoveUtils.getBaseMoveSpeed() - 0.01;
                } else if (mc.thePlayer.field_191988_bg != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                    this.stage = 1;
                    this.moveSpeed = 1.1 * MoveUtils.getBaseMoveSpeed() - 0.01;
                }
                if (this.stage > 0 && mc.thePlayer.onGround && MoveUtils.isOnGround(mc.thePlayer.motionX, mc.thePlayer.motionZ)) {
                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(eventMove, this.moveSpeed);
                    } else {
                        MoveUtils.setMotion(eventMove, this.moveSpeed);
                    }
                }
                break;
            case "DamageHop":
                if (MovementInput.moveForward == 0.0f && MovementInput.moveStrafe == 0.0f) {
                    this.moveSpeed = MoveUtils.getBaseMoveSpeed();
                }
                if (stage == 1 && mc.thePlayer.isCollidedVertically && (MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F)) {
                    this.moveSpeed = 1.2 * MoveUtils.getBaseMoveSpeed() - 0.01;
                }
                if (!mc.thePlayer.isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && (MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F)) {
                    if (this.damageTimer.delay(2500.0F)) {
                        this.damageTimer.reset();
                        damage2();
                    }

                    double motY = 0.407D + MoveUtils.getJumpEffect() * 0.1D;
                    this.mc.thePlayer.motionY = motY;
                    eventMove.setY(motY);
                    mc.thePlayer.jump();
                    this.moveSpeed *= 2.14D;
                } else if (stage == 3) {
                    final double difference = 0.66 * (lastDist - MoveUtils.getBaseMoveSpeed());
                    this.moveSpeed = lastDist - difference;
                } else {
                    final List collidingList = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
                    if ((collidingList.size() > 0 || MoveUtils.isOnGround(0.01)) && stage > 0) {
                        stage = ((MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F) ? 1 : 0);
                    }
                    this.moveSpeed = lastDist - lastDist / 159.0;
                }
                this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed());
                if (this.stage > 0) {
                    if (mc.thePlayer.isMoving()) {
                        if (mc.thePlayer.motionY > 0.0D) {
                            mc.thePlayer.motionY += 0.0012D;
                        } else if (mc.thePlayer.fallDistance < 1.5F) {
                            mc.thePlayer.motionY += 0.0012D;
                        }
                    }
                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(eventMove, this.moveSpeed);
                    } else {
                        MoveUtils.setMotion(eventMove, this.moveSpeed);
                    }
                }
                if (mc.thePlayer.field_191988_bg != 0.0F || mc.thePlayer.moveStrafing != 0.0F)
                    this.stage++;
                break;
            case "NigamiHop":
                if (mc.thePlayer.isMoving()) {
                	if(mc.gameSettings.keyBindJump.pressed) {

                		 eventMove.setY(this.mc.thePlayer.motionY = 0.22f);
                	}
                    if (mc.thePlayer.onGround) {
                        double motY = 0.900D + MoveUtils.getJumpEffect() * 0.1D;
                        this.mc.thePlayer.motionY = motY;
                        eventMove.setY(this.mc.thePlayer.motionY = motY);
                    }
                    if (targetStrafe.canStrafe()) {
                        targetStrafe.strafe(eventMove, 1.8);
                    } else {
                        MoveUtils.setMotion(eventMove, 2.8);
                    }
                }
                break;
            case "LegitHop":
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        if (mc.thePlayer.isPotionActive(Potion.getPotionById(8))) {
                            eventMove.setY(mc.thePlayer.motionY = 0.41999998688698 + (mc.thePlayer.getActivePotionEffect(Potion.getPotionById(8)).getAmplifier() + 1) * 0.1);
                        } else {
                            eventMove.setY(mc.thePlayer.motionY = 0.41999998688698);
                        }
                        mc.thePlayer.jump();
                    }
                }
        }
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();

        switch (currentmode) {
            case "OnGround":
                if (mc.thePlayer.isMoving() && mc.thePlayer.onGround && MoveUtils.isOnGround(mc.thePlayer.motionX, mc.thePlayer.motionZ)) {
                    if (mc.timer.timerSpeed >= 1.0f && this.stage == 2) {
                        mc.timer.timerSpeed = 1.2f;
                    } else if (mc.timer.timerSpeed > 1.0f) {
                        mc.timer.timerSpeed = 1.0f;
                    }
                    if (this.stage == 2) {
                        event.setOnGround(false);
                        if (mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, 0.42, 0.0)).isEmpty()) {
                            event.setY(event.getY() + 0.42);
                        } else {
                            event.setY(event.getY() + 0.2);
                        }
                    }
                } else {
                    if (MoveUtils.isOnGround(3.0) && mc.thePlayer.fallDistance < 0.1f) {
                        this.stage = 0;
                        mc.thePlayer.motionY -= 9.0;
                        return;
                    }
                    this.stage = -1;
                    if (mc.timer.timerSpeed > 1.0f) {
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
        }

        if (event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        Packet<?> p = eventPacket.getPacket();
        if (eventPacket.isIncoming()) {
            if (p instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook pac = (SPacketPlayerPosLook) eventPacket.getPacket();
               // ChatUtils.printChat("ajdibhuasihu");
                if(mc.thePlayer.canRenderOnFire()) {
                	state3 = 1;
                }
                if (lastCheck.delay(300)) {
                    pac.yaw = mc.thePlayer.rotationYaw;
                    pac.pitch = mc.thePlayer.rotationPitch;
                }
                stage = -4;
                lastCheck.reset();
            }
        }
    }

    public void damage2() {
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
    private double getCurrentSpeed(int stage) {
        double speed = MoveUtils.getBaseMoveSpeed() + 0.028D * MoveUtils.getSpeedEffect() + MoveUtils.getSpeedEffect() / 15.0D;
        double initSpeed = 0.4145D + MoveUtils.getSpeedEffect() / 12.5D;
        double decrease = stage / 500.0D * 1.87D;
        if (stage == 0) {
            speed = 0.64D + (MoveUtils.getSpeedEffect() + 0.028D * MoveUtils.getSpeedEffect()) * 0.134D;
        } else if (stage == 1) {
            speed = initSpeed;
        } else if (stage >= 2) {
            speed = initSpeed - decrease;
        }
        return Math.max(speed, this.slowDownHop ? speed : (MoveUtils.getBaseMoveSpeed() + 0.028D * MoveUtils.getSpeedEffect()));
    }
    public double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
