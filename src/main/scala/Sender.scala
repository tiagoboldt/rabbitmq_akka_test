import akka.actor.Actor
import com.rabbitmq.client._


case class Message(s:String)

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

    for(i <- start to end) {
      val m = i.toString
      channel.basicPublish("", queue, null, m.getBytes)
      System.out.println(" [%s] Sent '%s'".format(ID, m))
    }
  }

  protected def receive = {
    case Message(m) =>
      channel.basicPublish("", queue, null, m.getBytes)
      System.out.println(" [%s] Sent '%s'".format(ID, m))
  }

  override def postStop(){
    channel.close()
    connection.close()
  }
}
