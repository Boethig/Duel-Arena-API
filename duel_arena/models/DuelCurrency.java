package scripts.boe_api.duel_arena.models;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.helpers.DuelHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public enum DuelCurrency {

    COINS(DuelInterfaces.DUEL_STAKE_COINS, DuelInterfaces.DUEL_STAKE_COINS_OFFERED),
    PLATINUM_TOKENS(DuelInterfaces.DUEL_STAKE_TOKENS, DuelInterfaces.DUEL_STAKE_TOKENS_OFFERED);

    private DuelInterfaces offerInterface;
    private DuelInterfaces container;

    DuelCurrency(DuelInterfaces container, DuelInterfaces offerInterface) {
        this.container = container;
        this.offerInterface = offerInterface;
    }

    public boolean offer(int amount) {
        if (DuelInterfaces.ENTER_QUANTITY.isSubstantiated()) return enterQuantity(amount);
        int offerAmountInterface = DuelHelper.getOfferInterface(amount);
        RSInterface offer = Interfaces.get(container.getMaster(), container.getChild(), offerAmountInterface);
        if (offer != null) {
            return AccurateMouse.click(offer) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return hasOffered(amount);
            }, General.random(2000, 4000));
        }
        return false;
    }

    private boolean enterQuantity(int amount) {
        Keyboard.typeSend(Integer.toString(amount));
        return Timing.waitCondition(() -> {
            General.random(100,300);
            return hasOffered(amount);
        }, General.random(4000,6000));
    }

    private boolean hasOffered(int coins) {
        return getCurrentOffer() == coins;
    }

    public int getCurrentOffer() {
        RSInterface offer = offerInterface.get();
        if (offer != null) {
            return DuelHelper.parseOffer(offer.getText());
        }
        return 0;
    }
}
