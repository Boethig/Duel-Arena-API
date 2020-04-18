package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import scripts.dax_api.walker.utils.AccurateMouse;

public abstract class DuelProgression {

    private final int master;

    DuelProgression(int master) {
        this.master = master;
    }

    public int getMaster() {
        return master;
    }

    public abstract boolean accept();

    public abstract boolean decline();

    public abstract boolean hasPlayerAccepted();

    public abstract boolean hasOpponentAccepted();

    public boolean isOpen() {
        return Interfaces.isInterfaceSubstantiated(master);
    }

    public boolean close(RSInterface close) {
        if (close != null) {
            return AccurateMouse.click(close) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return !isOpen();
                }, General.random(3000, 5000));
        }
        return false;
    }
}
