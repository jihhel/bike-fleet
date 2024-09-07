package bike_simulation

import cats.effect.IO
import stations.{Bike, GetBikesFromStationRequest, Station, StationsServiceClient}

import java.time.Instant
import scala.util.Random
import scala.concurrent.duration._

class UserSimulation(val stationsClient: StationsServiceClient, val messageProducer: BikeProducer) {

  def start(stations: List[Station], user: User): IO[Unit] = {
    val startPosition = Random.shuffle(stations).head
    println(s"Starting simulation for user $user")
    runOnce(stations, startPosition, user)
  }

  private def runOnce(stations: List[Station], currentPosition: Station, user: User): IO[Unit] = {
    for {
      maybeBike <- pickABikeFrom(currentPosition)
      _ <- maybeBike match {
        case Some(bike) =>
          val destination = pickDestination(stations, currentPosition)
          for {
            _ <- takeBikeFromStation(user, currentPosition, bike)
            _ <- travelTo(destination, bike)
            _ <- returnBike(bike, destination)
            // Recursive call, the simulation will run until the user is at a station with no available bike
            _ <- runOnce(stations, destination, user)
          } yield ()
        case None =>
          println(s"No bike was available at station $currentPosition, stop travelling")
          IO.unit
      }
    } yield ()
  }

  private def pickABikeFrom(station: Station): IO[Option[Bike]] = {
    IO.fromFuture(IO(stationsClient.getBikesFromStation(new GetBikesFromStationRequest(station.stationId)))).map { response =>
      Random.shuffle(response.availableBikes.toList).headOption
    }
  }

  private def pickDestination(stations: List[Station], currentPosition: Station): Station = {
    Random.shuffle(stations.filter(_.stationId == currentPosition.stationId)).head
  }

  private def takeBikeFromStation(user: User, station: Station, bike: Bike): IO[Unit] = {
    val eventData = BikeTakenEvent(user.userId, bike.bikeId, station.stationId, Instant.now)
    println(s"$user takes a bike on a ride $eventData")

    messageProducer.produce(BikeRecord(bike.bikeId, eventData))
  }
  private def travelTo(destination: Station, bike: Bike): IO[Unit] = {
    val nbSteps = Random().nextInt(10)

    // Action to execute at each step of the travel
    val statusUpdateIO = IO {
      val eventData = BikeStatusEvent(bike.bikeId, Instant.now)
      println(s"Bike $bike is progressing towards $destination $eventData")

      // Send report to queue and wait 2 seconds before any other action
      messageProducer.produce(BikeRecord(bike.bikeId, eventData)) *> IO.sleep(2.seconds)
    }.flatten

    // Execute the action nbSteps times
    statusUpdateIO.replicateA_(nbSteps)
  }
  private def returnBike(bike: Bike, destination: Station): IO[Unit] = {
    val eventData = BikeReturnedEvent(bike.bikeId, destination.stationId, Instant.now)
    println(s"Returning bike $bike to station $destination $eventData")

    messageProducer.produce(BikeRecord(bike.bikeId, eventData))
  }

}
