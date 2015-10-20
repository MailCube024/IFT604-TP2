package HockeyLive.Client.Listeners;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Common.Models.Side;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public interface PenaltyNotificationListener {
    void NewPenalty(Penalty penalty, Side side, Game game);
}
