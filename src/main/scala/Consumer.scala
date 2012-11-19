import akka.actor.{Props, Actor}
import com.rabbitmq.client.QueueingConsumer
import java.io.{ObjectInputStream, ByteArrayInputStream}

class Consumer(ID: Int, queue: String) extends AMQPActor(ID, queue) {


  protected def startAnonymousListenner() {

    context.actorOf(Props(new Actor {
      protected def receive = {
        case 'GetMessages =>
          val consumer = new QueueingConsumer(channel)
          channel.basicConsume(queue, true, consumer)

          while (true) {
            val delivery = consumer.nextDelivery

            val bufin = new ByteArrayInputStream(delivery.getBody)
            val obin = new ObjectInputStream(bufin)

            sender ! obin.readObject()

          }
      }
    })) ! 'GetMessages
  }

  override def preStart() {
    super.preStart()
    startAnonymousListenner()
  }

  protected def receive = {
    case StringMessage(s) =>
      println(" [%s] Got string %s that is 1/2 %s".format(ID, s, s.toInt*2))
    case IntMessage(i) =>
      println(" [%s] Got int %s that is 1/2 %s".format(ID, i, i*2))
  }
}
