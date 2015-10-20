package HockeyLive.Client.Communication;

import HockeyLive.Client.Listeners.*;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Client {

    private List<GameInfoUpdateListener> gameInfoUpdateListeners;
    private List<GameListUpdateListener> gameListUpdateListeners;
    private List<BetConfirmationListener> betConfirmationListeners;
    private List<BetUpdateListener> betUpdateListeners;
    private List<GoalNotificationListener> goalNotificationListeners;
    private List<PenaltyNotificationListener> penaltyNotificationListeners;

    private ClientSocket socket = null;
    private InetAddress localhost = null;
    private InetAddress serverAddress;

    private Thread receiver;

    public Client() {
        gameInfoUpdateListeners = new ArrayList<>();
        gameListUpdateListeners = new ArrayList<>();
        betConfirmationListeners = new ArrayList<>();
        betUpdateListeners = new ArrayList<>();
        goalNotificationListeners = new ArrayList<>();
        penaltyNotificationListeners = new ArrayList<>();
        serverAddress = GetLocalhost();
    }

    public Client(InetAddress address) {
        gameInfoUpdateListeners = new ArrayList<>();
        gameListUpdateListeners = new ArrayList<>();
        betConfirmationListeners = new ArrayList<>();
        betUpdateListeners = new ArrayList<>();
        goalNotificationListeners = new ArrayList<>();
        penaltyNotificationListeners = new ArrayList<>();
        serverAddress = address;
    }

    private ClientSocket GetSocket() {
        if (socket == null) {
            try {
                socket = new ClientSocket(Constants.CLIENT_COMM_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return socket;
    }

    private InetAddress GetLocalhost() {
        if (localhost == null) {
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return localhost;
    }

    public void RequestGameList() {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatches, 1,
                serverAddress, Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                null);

        GetSocket().Send(message);
    }

    public void RequestGameInfo(Integer gameID) {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatchInfo, 2,
                serverAddress, Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                gameID);

        GetSocket().Send(message);
    }

    public void SendBet(Bet bet) {
        ClientMessage message = new ClientMessage(ClientMessageType.PlaceBet, 3,
                serverAddress, Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                bet);

        GetSocket().Send(message);
    }

    public void ReceiveBet(Bet bet) {
        for (BetUpdateListener l : betUpdateListeners)
            l.BetUpdate(bet);

        ClientMessage message = new ClientMessage(ClientMessageType.AckNotification, 4,
                serverAddress, Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                bet);

        GetSocket().Send(message);
    }

    public void ReceiveGameList(List<Game> games) {
        for (GameListUpdateListener l : gameListUpdateListeners)
            l.UpdateGameList(games);
    }

    public void ReceiveGameInfo(GameInfo info) {
        for (GameInfoUpdateListener l : gameInfoUpdateListeners)
            l.UpdateGameInfo(info);
    }

    public void ReceiveBetConfirmation(boolean confirmation) {
        for (BetConfirmationListener l : betConfirmationListeners)
            l.IsBetConfirmed(confirmation);
    }

    public void ReceiveGoalNotification(List<Object> goalInfo) {
        Game game = (Game)goalInfo.get(0);
        Side side = (Side)goalInfo.get(1);
        Goal goal = (Goal)goalInfo.get(2);

        for (GoalNotificationListener l : goalNotificationListeners)
            l.NewGoal(goal, side, game);
    }

    public void ReceivePenaltyNotification(List<Object> penaltyInfo) {
        Game game = (Game)penaltyInfo.get(0);
        Side side = (Side)penaltyInfo.get(1);
        Penalty penalty = (Penalty)penaltyInfo.get(2);

        for (PenaltyNotificationListener l : penaltyNotificationListeners)
            l.NewPenalty(penalty, side, game);
    }

    public void AddGameListUpdateListener(GameListUpdateListener listener) {
        gameListUpdateListeners.add(listener);
    }

    public void AddGameInfoUpdateListener(GameInfoUpdateListener listener) {
        gameInfoUpdateListeners.add(listener);
    }

    public void AddBetConfirmationListener(BetConfirmationListener listener) {
        betConfirmationListeners.add(listener);
    }

    public void AddBetUpdateListener(BetUpdateListener listener) {
        betUpdateListeners.add(listener);
    }

    public void AddGoalNotificationListener(GoalNotificationListener listener) {
        goalNotificationListeners.add(listener);
    }

    public void AddPenaltyNotificationListener(PenaltyNotificationListener listener) {
        penaltyNotificationListeners.add(listener);
    }

    public void UnregisterListeners() {
        gameInfoUpdateListeners.clear();
        gameListUpdateListeners.clear();
        betConfirmationListeners.clear();
        betUpdateListeners.clear();
    }

    public void Start() {
        receiver = new Thread(new MessageHandler(this, GetSocket()));
        receiver.start();
    }

    public void Close() {
        receiver.interrupt();
        socket.CloseSocket();
    }
}
