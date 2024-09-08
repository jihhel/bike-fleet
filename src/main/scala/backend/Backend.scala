package backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.ConfigFactory
import stations.StationsServiceHandler

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Backend extends IOApp {

    override def run(args: List[String]): IO[ExitCode] = {
        val akkaHttpConf = ConfigFactory
          .parseString("akka.http.server.enable-http2 = on")
          .withFallback(ConfigFactory.defaultApplication())

        implicit val system: ActorSystem = ActorSystem("Backend", akkaHttpConf)
        implicit val executionContext: ExecutionContext = system.dispatcher

        def startServer: IO[Unit] = IO.async_ { callback =>
            val service: HttpRequest => Future[HttpResponse] = StationsServiceHandler(new StationsServiceImpl())(system)

            val binding = Http().newServerAt("localhost", 8080).bind(service)

            // Handling server binding success or failure
            binding.onComplete {
                case Success(binding) =>
                    println(s"Server is running at ${binding.localAddress}")
                case Failure(exception) =>
                    println(s"Failed to bind the server: ${exception.getMessage}")
                    callback(Left(exception))
            }
        }

        startServer *> IO.never.as(ExitCode.Success)
    }
}
