package bike_simulation.bike_event

import bike_simulation.bike_event.BikeEventType.{BIKE_RETURNED, BIKE_STATUS_UPDATE, BIKE_TAKEN}
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json
import java.time.Instant

enum BikeEventType {
  case BIKE_TAKEN
  case BIKE_RETURNED
  case BIKE_STATUS_UPDATE
}

sealed abstract class BikeEvent(val eventType: BikeEventType)

final case class BikeTakenEvent(userId: Int, bikeId: Int, stationId: Int, time: Instant) extends BikeEvent(BIKE_TAKEN)
final case class BikeReturnedEvent(bikeId: Int, stationId: Int, time: Instant) extends BikeEvent(BIKE_RETURNED)
final case class BikeStatusEvent(bikeId: Int, time: Instant) extends BikeEvent(BIKE_STATUS_UPDATE)

object BikeEvent {
  given Encoder[BikeEventType] = (eventType: BikeEventType) => eventType.toString.asJson

  given Encoder[BikeEvent] = (event: BikeEvent) => {
    val eventDataAsJson = event match
      case e: BikeTakenEvent => e.asJson
      case e: BikeReturnedEvent => e.asJson
      case e: BikeStatusEvent => e.asJson

    Json.obj(
      ("type", event.eventType.asJson),
      ("data", eventDataAsJson)
    )
  }
}
