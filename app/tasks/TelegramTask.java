package tasks;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import services.TelegramBot;
import play.libs.ws.WSClient;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


public class TelegramTask {

    private final TelegramBot telegramBot;
    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public TelegramTask(Config config, WSClient ws, ActorSystem actorSystem, ExecutionContext executionContext) {
        this.telegramBot = new TelegramBot(config,ws);
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        actorSystem.scheduler().scheduleOnce(
                Duration.create(0, TimeUnit.SECONDS), // initialDelay
                telegramBot,
                executionContext
        );

    }
}