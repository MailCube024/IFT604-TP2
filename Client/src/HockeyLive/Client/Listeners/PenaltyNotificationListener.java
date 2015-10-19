package HockeyLive.Client.Listeners;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Common.Models.Side;

/**
 * Created by Benoit on 2015-10-19.
 */
public interface PenaltyNotificationListener {
    public void NewPenalty(Penalty penalty, Side side, Game game);
}
