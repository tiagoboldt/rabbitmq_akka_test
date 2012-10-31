import akka.actor.{Props, Actor}
import com.rabbitmq.client.{QueueingConsumer, Channel, Connection, ConnectionFactory}

class Consumer(ID: Int, queue: String) extends Actor{

  protected var factory: ConnectionFactory  = null
  protected var connection: Connection = null
  protected var channel: Channel = null

  object GetMessage

  override def preStart() {

    context.actorOf(Props(new Actor {
      protected def receive = {
        case GetMessage =>
          factory = new ConnectionFactory()
          factory.setHost("localhost")
          connection = factory.newConnection()
          channel = connection.createChannel()
          channel.queueDeclare(queue, false, false, false, null)

          val consumer = new QueueingConsumer(channel)
          channel.basicConsume(queue, true, consumer)

          while (true) {
            val delivery = consumer.nextDelivery
            val m = Message(new String(delivery.getBody))
            sender ! m
          }

      }
    })) ! GetMessage
  }

  protected def receive = {
    case Message(s) =>
      println(" \t\t\t\t[%s] Got %s".format(ID, s))
  }
}
