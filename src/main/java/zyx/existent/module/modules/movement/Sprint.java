package zyx.existent.module.modules.movement;

import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventUpdate;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Setting;

public class Sprint extends Module {
    private String OMNIDIR = "OMNIDIR";

    public Sprint(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(OMNIDIR, new Setting<>(OMNIDIR, true, "VoidCheck."));
    }

    @EventTarget
    public void onUpdate(EventUpdate em) {
        this.setDisplayName("Sprint");
        if (em.isPre() && canSprint()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    private boolean canSprint() {
        if (!(Boolean) settings.get(OMNIDIR).getValue() && !mc.gameSettings.keyBindForward.isKeyDown())
            return false;
        return mc.thePlayer.isMoving() && mc.thePlayer.getFoodStats().getFoodLevel() > 6;
    }
}
