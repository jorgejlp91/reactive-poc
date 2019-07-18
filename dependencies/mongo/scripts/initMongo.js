//db.initial.insert({ some_key: "some_value" })
db.order.insert({_id:1,productName:"Camiseta Básica",quantity:2,cardToken:"HSDHNGBSDA86293DS",unitPrice:"35.9","_class":"com.piccolomini.reactive.domains.Order"})
db.createUser(
  {
    user: "user123",
    pwd: "pass123",
    roles: [ { role: "readWrite", db: "reactivedb" } ]
  }
)