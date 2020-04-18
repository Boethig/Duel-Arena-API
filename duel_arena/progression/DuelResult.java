package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.models.DuelInterfaces;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelResult {

    public String getWinner() {
        RSInterface winner = DuelInterfaces.DUEL_RESULTS_WINNER.get();
        return winner != null ? winner.getText() : null;
    }

    public String getLoser() {
        RSInterface loser = DuelInterfaces.DUEL_RESULTS_LOSER.get();
        return loser != null ? loser.getText() : null;
    }

    public boolean hasPlayerWon() {
        String winner = getWinner();
        if (winner != null) {
            return Player.getRSPlayer().getName().equals(winner);
        }
        return false;
    }

    public boolean close() {
        RSInterface close = DuelInterfaces.DUEL_RESULTS_CLOSE.get();
        if (close != null) {
            return AccurateMouse.click(close, "Close") && Timing.waitCondition(() -> !DuelInterfaces.DUEL_RESULTS.isSubstantiated(), General.random(4000, 6000));
        }
        return false;
    }
}
