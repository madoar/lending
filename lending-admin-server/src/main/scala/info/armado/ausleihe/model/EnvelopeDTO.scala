package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class EnvelopeDTO(var barcode: String,
                      var activated: Boolean) {
  def this() = this(null, false)
}