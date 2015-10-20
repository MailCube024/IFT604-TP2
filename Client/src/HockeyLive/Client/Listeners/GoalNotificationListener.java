package HockeyLive.Client.Listeners;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.Goal;
import HockeyLive.Common.Models.Side;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public interface GoalNotificationListener {
    void NewGoal(Goal goal, Side side, Game game);
}
