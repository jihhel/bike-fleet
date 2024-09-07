package bike_simulation

import lib_kafka.KRecord

final case class BikeRecord(key: Int, value: BikeEvent) extends KRecord[Int, BikeEvent]
