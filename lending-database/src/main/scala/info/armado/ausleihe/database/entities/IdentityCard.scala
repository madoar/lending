package info.armado.ausleihe.database.entities

import javax.persistence._

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * Factory for [[IdentityCard]] instances.
  */
object IdentityCard {
  /**
    * Creates a IdentityCard instance with a given barcode
    *
    * @param barcode The barcode of the identity card
    * @return The new IdentityCard instance
    */
  def apply(barcode: Barcode): IdentityCard = new IdentityCard(barcode, false)
}

/**
  * An identity card, which can be bound to an [[Envelope]]
  *
  * @author Marc Arndt
  * @param barcode   The barcode of the identity card
  * @param available The availability state of the identity card
  */
@Entity
@Table
case class IdentityCard(@Column(unique = true, nullable = false) var barcode: Barcode,
                   @Column var available: Boolean) extends HasBarcode with Serializable {

  /**
    * The unique identifier of the identity card
    */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private var id: Int = _

  /**
    * Create a new empty IdentityCard instance
    *
    * Required for JPA
    */
  def this() = this(null, false)

  override def equals(other: Any): Boolean = other match {
    case other: IdentityCard => this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + barcode.hashCode

    result
  }
}