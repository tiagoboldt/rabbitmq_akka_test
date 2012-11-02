import akka.actor.PoisonPill


case class Message(s:String)
object Start

class Sender(ID: Int, start: Int, end: Int, queue: String) extends AMQPActor(ID, queue) {

  protected def receive = {
    case Start =>
      for(i <- start to end) {
        channel.basicPublish("", queue, null, i.toString.getBytes)
      }
      self ! PoisonPill
  }

  override def postStop(){
    channel.close()
    connection.close()
  }
}
