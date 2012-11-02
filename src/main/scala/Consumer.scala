import akka.actor.{Props, Actor}
import com.rabbitmq.client.QueueingConsumer

class Consumer(ID: Int, queue: String) extends AMQPActor(ID, queue) {


  protected def startAnonymousListenner() {
    object GetMessages
    context.actorOf(Props(new Actor {
      protected def receive = {
        case GetMessages =>
          val consumer = new QueueingConsumer(channel)
          channel.basicConsume(queue, true, consumer)

          while (true) {
            val delivery = consumer.nextDelivery
            val m = Message(new String(delivery.getBody))
            sender ! m
          }
      }
    })) ! GetMessages
  }

  override def preStart() {
    super.preStart()
    startAnonymousListenner()
  }

  protected def receive = {
    case Message(s) =>
      println(" [%s] Got %s that is 1/2 %s".format(ID, s, s.toInt*2))
  }
}
