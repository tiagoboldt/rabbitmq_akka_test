import akka.actor.PoisonPill
import java.io.{ObjectOutputStream, ByteArrayOutputStream, Serializable}


object Start

class Sender(ID: Int, start: Int, end: Int, queue: String) extends AMQPActor(ID, queue) {


  protected def receive = {
    case Start =>
      for(i <- start to end) {

        var msg : AMQPMessage = null

        if (i%2 == 0){
          msg = StringMessage(i.toString)
        }
        else {
          msg = IntMessage(i)
        }

        val bufout = new ByteArrayOutputStream()
        val oout = new ObjectOutputStream(bufout)

        oout.writeObject(msg)
        channel.basicPublish("", queue, null, bufout.toByteArray )

        bufout.reset()
        oout.reset()
      }
      self ! PoisonPill
  }

  override def postStop(){
    channel.close()
    connection.close()
  }
}
