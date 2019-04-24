package tasks;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import services.TelegramBot;
import play.libs.ws.WSClient;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import services.TelegramSetupRunnable;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


public class TelegramTask {

    @Inject
    public TelegramTask(Config config, WSClient ws, ActorSystem actorSystem, ExecutionContext executionContext) {

        actorSystem.scheduler().scheduleOnce(
                Duration.create(0, TimeUnit.SECONDS), // initialDelay
                new TelegramSetupRunnable(config, ws),
                executionContext
        );

    }
}