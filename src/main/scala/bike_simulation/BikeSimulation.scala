package bike_simulation

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import bike_simulation.dto.User
import cats.effect.{ExitCode, IO, IOApp}
import stations.{GetStationsRequest, StationsServiceClient}

import scala.concurrent.ExecutionContext
import cats.implicits.*
import com.google.gson.JsonSerializer
import com.typesafe.config.ConfigFactory

import java.net.InetAddress
import java.util.Properties

object BikeSimulation extends IOApp {

  private def makeStationsServiceClient() = {
    implicit val sys: ActorSystem = ActorSystem("BikeSimulation")
    implicit val ec: ExecutionContext = sys.dispatcher

    StationsServiceClient(GrpcClientSettings.fromConfig("bike_simulation").withTls(false))
  }

  private def makeMessageProducer() = {
    ConfigFactory.defaultApplication()
    val config = new Properties();
    config.put("client.id", InetAddress.getLocalHost.getHostName);
    config.put("bootstrap.servers", "localhost:29092");

    new BikeProducer(config)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val stationsClient = makeStationsServiceClient()
    val messageProducer = makeMessageProducer()

    val simulation = for {
      response <- IO.fromFuture(IO(stationsClient.getStations(GetStationsRequest())))
      stations = response.stations
      users = List(
        User(1, "Alice"),
        User(2, "Bob"),
        User(3, "Charles")
      )
      simulation = new UserSimulation(stationsClient, messageProducer)
      _ <- users.map(user => {
        simulation.start(stations.toList, user)
      }).parSequence_
    } yield ()

    simulation.map(_ => ExitCode.Success)
  }
}
