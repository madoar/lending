package info.armado.ausleihe.remote

import info.armado.ausleihe.database.access.LendIdentityCardDao
import info.armado.ausleihe.database.barcode.{InvalidBarcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.entities.LendIdentityCard
import info.armado.ausleihe.model.transport.{ChangeOwnerRequestDTO, LendIdentityCardDTO}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/lend/identity-cards")
@RequestScoped
class LendIdentityCardService {
  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  /**
    * Select all currently issued identity cards
    *
    * @return An array containing all currently issued identity cards
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllLendIdentityCards(): Array[LendIdentityCardDTO] =
    lendIdentityCardDao.selectAllCurrentLend.map(lendIdentityCard => toLendIdentityCardDTO(lendIdentityCard)).toArray

  /**
    * Updates the owner of an issued identity card
    *
    * @param changeOwnerRequest The request containing the barcode of the issued identity card and the new owner
    * @return The updated lend identity card
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/owner")
  @Transactional
  def updateOwner(changeOwnerRequest: ChangeOwnerRequestDTO): LendIdentityCardDTO = changeOwnerRequest match {
    case ChangeOwnerRequestDTO(identityCardBarcodeString, owner) => ValidateBarcode(identityCardBarcodeString) match {
      case ValidBarcode(identityCardBarcode) => lendIdentityCardDao.selectCurrentByIdentityCardBarcode(identityCardBarcode) match {
        case Some(lendIdentityCard) => {
          lendIdentityCardDao.updateOwner(lendIdentityCard, owner)

          toLendIdentityCardDTO(lendIdentityCard)
        }
        case None => throw new NotFoundException("Non issued identity card");
      }
      case InvalidBarcode(_) => throw new BadRequestException("Invalid identity card barcode");
    }
  }

  def toLendIdentityCardDTO(lendIdentityCard: LendIdentityCard): LendIdentityCardDTO = {
    val result = new LendIdentityCardDTO()

    result.identityCardBarcode = lendIdentityCard.identityCard.barcode.toString
    result.envelopeBarcode = lendIdentityCard.envelope.barcode.toString
    result.lendTime = lendIdentityCard.lendTime.toString
    result.numberOfLendGames = lendIdentityCard.currentLendGames.length
    result.owner = lendIdentityCard.owner

    result
  }
}
