package messages.bike_event

import io.circe.*
import io.circe.syntax.*
import org.apache.kafka.common.serialization.Serializer

class BikeEventSerializer extends Serializer[BikeEvent] {

  override def serialize(topic: String, data: BikeEvent): Array[Byte] = {
    data.asJson.toString.getBytes
  }
}
