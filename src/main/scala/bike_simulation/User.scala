package bike_simulation

// User should be handled in the backend part, but for simplicity's sake I will just improvize users here
// as for the moment the only place I need users is in the simulation
final case class User(userId: Int, firstName: String)
