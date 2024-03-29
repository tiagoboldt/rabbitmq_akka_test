import akka.actor.Actor
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

case class AMQPMessage()
case class StringMessage(s: String) extends AMQPMessage
case class IntMessage(i: Int) extends AMQPMessage

abstract class AMQPActor(ID: Int, queue: String) extends Actor {

  override def toString = ID.toString
  protected var factory: ConnectionFactory  = null
  protected var connection: Connection = null
  protected var channel: Channel = null

  override def preStart(){
    factory = new ConnectionFactory()
    factory.setHost("localhost")
    connection = factory.newConnection()
    channel = connection.createChannel()
    channel.queueDeclare(queue, false, false, false, null)
  }
}
