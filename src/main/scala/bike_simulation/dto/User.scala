package bike_simulation.dto

// User should be handled in the backend part, but for simplicity's sake I will just improvise users here
// as for the moment the only place I need users is in the simulation
final case class User(userId: Int, firstName: String)
