import akka.actor.{PoisonPill, Actor}
import com.rabbitmq.client._


case class Message(s:String)
object Start

class Sender(ID: Int, start: Int, end: Int, queue: String) extends Actor {

  protected var factory: ConnectionFactory  = null
  protected var connection: Connection = null
  protected var channel: Channel = null

  override def preStart(){
    factory = new ConnectionFactory
    factory.setHost("localhost")
    connection = factory.newConnection
    channel = connection.createChannel
    channel.queueDeclare(queue, false, false, false, null)

  }

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
