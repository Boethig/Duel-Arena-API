package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.helpers.DuelHelper;
import scripts.boe_api.duel_arena.models.DuelCurrency;
import scripts.boe_api.duel_arena.models.DuelInterfaces;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelStake extends DuelProgression {

    public DuelStake(int master) {
        super(master);
    }

    @Override
    public boolean accept() {
        if (hasPlayerAccepted()) return false;
        RSInterface accept = DuelInterfaces.DUEL_STAKE_ACCEPT.get();
        if (accept != null) {
            if (InterfaceHelper.textEquals(accept,"Wait....")) return false;
            if (hasOpponentAccepted()) {
                return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return DuelInterfaces.DUEL_CONFIRMATION.isSubstantiated();
                }, General.random(5000, 7000));
            } else {
                return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return hasPlayerAccepted() || InterfaceHelper.textEquals(accept,"Wait....");
                }, General.random(3000, 5000));
            }
        }
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface decline = DuelInterfaces.DUEL_STAKE_DECLINE.get();
        return close(decline);
    }

    @Override
    public boolean hasPlayerAccepted() {
        RSInterface status = DuelInterfaces.DUEL_STAKE_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Waiting for other player...");
        }
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        RSInterface status = DuelInterfaces.DUEL_STAKE_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Other player has accepted.");
        }
        return false;
    }

    public boolean offer(DuelCurrency currency, int amount) {
        int gp = currency.equals(DuelCurrency.PLATINUM_TOKENS) ? (amount / 1000) : amount;
        return currency.offer(gp);
    }

    public boolean offer(DuelCurrency currency, String amount) {
        return offer(currency, DuelHelper.parseOffer(amount));
    }

    public int getTotalOffer() {
        return DuelCurrency.PLATINUM_TOKENS.getCurrentOffer() + DuelCurrency.COINS.getCurrentOffer();
    }

    public String getTotalOfferToString() {
        return getTotalOffer() + " gp";
    }
}
