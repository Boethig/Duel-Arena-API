package scripts.boe_api.duel_arena.helpers;

import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import scripts.boe_api.duel_arena.models.Arena;

public class DuelHelper {

    public static boolean isInDuel() {
        RSTile player = Player.getPosition();
        return Arena.MOVEMENT.contains(player) || Arena.NO_MOVEMENT.contains(player);
    }

    public static RSPlayer getNearestPlayer(String username) {
        RSPlayer[] players = Players.findNearest(username);
        return players.length > 0 ? players[0] : null;
    }


    public static int parseOffer(String offer) {
        offer = offer.replaceAll(",", "");
        System.out.println(offer);
        try {
            if (offer.endsWith("m")) {
                return 1000000 * Integer.parseInt(offer.substring(0, offer.length() - 1));
            } else if (offer.endsWith("k")) {
                System.out.println(offer.substring(0, offer.length() -1));
                return 1000 * Integer.parseInt(offer.substring(0, offer.length() - 1));
            } else if (offer.endsWith(" gp")) {
                return Integer.parseInt(offer.substring(0, offer.length() - 3));
            } else {
                return Integer.parseInt(offer);
            }
        }
        catch (NumberFormatException exception) {
            System.out.println(exception.toString());
        }
        return 0;
    }

    public static int getOfferInterface(int amount) {
        if (amount == 1) return 1;
        if (amount == 100000) return 2;
        else if (amount == 1000000) return 3;
        else if (amount == 10000000) return 4;
        else return 5;
    }
}
