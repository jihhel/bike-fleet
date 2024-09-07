package backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.ConfigFactory
import stations.StationsServiceHandler

import scala.concurrent.{ExecutionContext, Future}

object Backend extends App {
    val conf = ConfigFactory.parseString("akka.http.server.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())

    implicit val system: ActorSystem = ActorSystem("HelloWorld", conf)
    implicit val ec: ExecutionContext = system.dispatcher

    val service: HttpRequest => Future[HttpResponse] = StationsServiceHandler(new StationsServiceImpl())

    val binding = Http().newServerAt("127.0.0.1", 8080).bind(service)
    binding.foreach { binding => println(s"gRPC server bound to: ${binding.localAddress}") }
}
