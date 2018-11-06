package info.armado.ausleihe.client.transport.dataobjects

import java.time.Duration

import info.armado.ausleihe.client.transport.converter.DurationAdapter
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendGameStatusDTO(var game: GameDTO,
                             var lend: Boolean,
                             @XmlJavaTypeAdapter(value = classOf[DurationAdapter]) var lendDuration: Duration) {

  def this() = this(null, false, null)
}