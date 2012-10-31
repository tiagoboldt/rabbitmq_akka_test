import akka.actor._

object Test extends App {
  println("cenas")

  val QUEUE = "cenas"
  val ConsumerCount = 8
  val SenderCount = 8

  var consumers = Set[ActorRef]()
  var senders = List[ActorRef]()

  val system = ActorSystem("MySystem")

  for (i<-1 to ConsumerCount){
    consumers ++= Set(system.actorOf(Props(new Consumer(i, QUEUE)), name = "consumer%s".format(i)))
  }
  for (i<-1 to SenderCount){
    senders ++= Set(system.actorOf(Props(new Sender(QUEUE)), name = "sender%s".format(i)))
  }

  Thread.sleep(1000)

  for(i <- 1 to 2000000) {
    senders(i%SenderCount) ! Message(i.toString)
    Thread.sleep(1)
  }
//  Thread.sleep(1000)
//  consumers.foreach { _ ! PoisonPill }
//  sender ! PoisonPill
//  system.shutdown()

  println("Finished")
}
