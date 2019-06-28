db.initial.insert({ some_key: "some_value" })
db.createUser(
  {
    user: "user123",
    pwd: "pass123",
    roles: [ { role: "readWrite", db: "reactivedb" } ]
  }
)