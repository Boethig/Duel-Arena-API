package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.models.DuelInterfaces;
import scripts.boe_api.duel_arena.models.DuelPreset;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelOptions extends DuelProgression {

    public DuelOptions(int master) {
        super(master);
    }

    public boolean isPresetLoaded(DuelPreset preset) {
        return preset.loaded();
    }

    public boolean loadPreset(DuelPreset preset) {
        return preset.load();
    }

    public boolean savePreset() {
        RSInterface save = DuelInterfaces.DUEL_OPTIONS_SAVE_PRESET.get();
        if (save != null) {
            return AccurateMouse.click(save) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return DuelPreset.SAVED.loaded();
                }, General.random(3000, 5000));
        }
        return false;
    }

    @Override
    public boolean accept() {
        if (hasPlayerAccepted()) return false;
        RSInterface accept = DuelInterfaces.DUEL_OPTIONS_ACCEPT.get();
        if (accept != null) {
            System.out.println(accept.getText());
            if (InterfaceHelper.textEquals(accept,"Wait....")) return false;
            return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return hasPlayerAccepted() || InterfaceHelper.textEquals(accept,"Wait....");
                }, General.random(3000, 5000));
        }
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface decline = DuelInterfaces.DUEL_OPTIONS_DECLINE.get();
        return close(decline);
    }

    @Override
    public boolean hasPlayerAccepted() {
        RSInterface status = DuelInterfaces.DUEL_OPTIONS_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Waiting for other player...");
        }
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        RSInterface status = DuelInterfaces.DUEL_OPTIONS_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Other player has accepted.");
        }
        return false;
    }
}
