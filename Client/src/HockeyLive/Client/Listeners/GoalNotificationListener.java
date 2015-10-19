package HockeyLive.Client.Listeners;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.Goal;
import HockeyLive.Common.Models.Side;

/**
 * Created by Benoit on 2015-10-19.
 */
public interface GoalNotificationListener {
    public void NewGoal(Goal goal, Side side, Game game);
}
