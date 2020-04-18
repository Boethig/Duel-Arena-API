package scripts.boe_api.duel_arena;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import scripts.boe_api.duel_arena.helpers.DuelHelper;
import scripts.boe_api.duel_arena.models.DuelInterfaces;
import scripts.boe_api.duel_arena.progression.DuelConfirmation;
import scripts.boe_api.duel_arena.progression.DuelOptions;
import scripts.boe_api.duel_arena.progression.DuelProgression;
import scripts.boe_api.duel_arena.progression.DuelStake;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;

public class Duel {

    private static DuelOptions options;
    private static DuelStake stake;
    private static DuelConfirmation confirmation;

    public static boolean challenge(String username) {
        RSPlayer player = DuelHelper.getNearestPlayer(username);
        if (player != null) {
            if (InteractionHelper.click(player, "Challenge")) {
                return Timing.waitCondition(() -> { General.random(100, 300); return isDuelScreenOpen(); }, General.random(3000, 5000));
            }
        }
        return false;
    }

    public static boolean forfeit() {
        if (!isInDuel()) return false;
        RSObject[] trapdoor = Objects.findNearest(30, "Trapdoor");
        if (trapdoor.length > 0) {
            if (AccurateMouse.click(trapdoor[0], "Forfeit")) {
                RSInterface forfeit = DuelInterfaces.FORFEIT.get();
                if (forfeit != null) {
                    return AccurateMouse.click(forfeit, "Continue");
                }
            }
        }
        return false;
    }

    public static boolean isInDuel() {
        return DuelHelper.isInDuel();
    }

    public static DuelOptions getDuelOptions() {
        return options != null ? options : (options = new DuelOptions(DuelInterfaces.DUEL_OPTIONS.getMaster()));
    }

    public static DuelStake getDuelStake() {
        return stake != null ? stake : (stake = new DuelStake(DuelInterfaces.DUEL_STAKE.getMaster()));
    }

    public static DuelConfirmation getDuelConfirmation() {
        return confirmation != null ? confirmation : (confirmation = new DuelConfirmation(DuelInterfaces.DUEL_CONFIRMATION.getMaster()));
    }

    public static DuelProgression getCurrentStep() {
        if (DuelInterfaces.DUEL_CONFIRMATION.isSubstantiated()) {
            return confirmation != null ? confirmation : (confirmation = new DuelConfirmation(DuelInterfaces.DUEL_CONFIRMATION.getMaster()));
        } else if (DuelInterfaces.DUEL_STAKE.isSubstantiated()) {
            return stake != null ? stake : (stake = new DuelStake(DuelInterfaces.DUEL_STAKE.getMaster()));
        } else if (DuelInterfaces.DUEL_OPTIONS.isSubstantiated()) {
            return options != null ? options : (options = new DuelOptions(DuelInterfaces.DUEL_OPTIONS.getMaster()));
        }
        return null;
    }

    public static boolean isDuelScreenOpen() {
        return DuelInterfaces.DUEL_OPTIONS.isSubstantiated() || DuelInterfaces.DUEL_STAKE.isSubstantiated() || DuelInterfaces.DUEL_CONFIRMATION.isSubstantiated();
    }
}
