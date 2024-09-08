package backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.ConfigFactory
import stations.StationsServiceHandler

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

object Backend extends App {
    val conf = ConfigFactory.parseString("akka.http.server.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())

    implicit val system: ActorSystem = ActorSystem("HelloWorld", conf)
    implicit val ec: ExecutionContext = system.dispatcher

    val service: HttpRequest => Future[HttpResponse] = StationsServiceHandler(new StationsServiceImpl())

    val bound: Future[Http.ServerBinding] = Http()(system).newServerAt("127.0.0.1", 8080)
      .bind(service)

    bound.onComplete {
        case Success(binding) =>
            val address = binding.localAddress
            println(s"gRPC server bound to ${address.getHostString}:${address.getPort}")
        case Failure(ex) =>
            println("Failed to bind gRPC endpoint, terminating system")
            ex.printStackTrace()
            system.terminate()
    }

    bound
}
