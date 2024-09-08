package messages.bike_event

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import messages.bike_event.BikeEventType.*

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
