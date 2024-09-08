package bike_simulation

import lib_kafka.KProducer
import messages.bike_event.{BikeEvent, BikeRecord}

import java.util.Properties

class BikeProducer(props: Properties) extends KProducer[Int, BikeEvent, BikeRecord](
  props,
  "bike-event",
  "org.apache.kafka.common.serialization.IntegerSerializer",
  "messages.bike_event.BikeEventSerializer") {}
