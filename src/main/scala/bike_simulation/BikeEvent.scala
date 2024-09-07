package bike_simulation

import bike_simulation.BikeEventType.{BIKE_RETURNED, BIKE_STATUS_UPDATE, BIKE_TAKEN}

import java.time.Instant

sealed abstract class BikeEvent(eType: BikeEventType, bikeId: Int, time: Instant)

case class BikeTakenEvent(userId: Int, bikeId: Int, stationId: Int, time: Instant) extends BikeEvent(BIKE_TAKEN, bikeId, time)
case class BikeReturnedEvent(bikeId: Int, stationId: Int, time: Instant) extends BikeEvent(BIKE_RETURNED, bikeId, time)
case class BikeStatusEvent(bikeId: Int, time: Instant) extends BikeEvent(BIKE_STATUS_UPDATE, bikeId, time)
