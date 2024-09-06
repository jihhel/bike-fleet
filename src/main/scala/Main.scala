import cats.effect.{ExitCode, IO, IOApp}
import lib_kafka.{BikeEventProducer, BikeEventRecord, Producer}

import java.util.Properties
import java.net.InetAddress

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val config = new Properties();
    config.put("client.id", InetAddress.getLocalHost.getHostName);
    config.put("bootstrap.servers", "localhost:29092");

    val record = BikeEventRecord(2, "Test 3")

    new BikeEventProducer(config).produce(record).map { _ =>
      ExitCode.Success
    }

  }

  def msg = "I was compiled by Scala 3. :)"
}
