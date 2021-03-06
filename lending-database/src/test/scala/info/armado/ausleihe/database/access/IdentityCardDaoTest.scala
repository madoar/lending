package info.armado.ausleihe.database.access

import info.armado.ausleihe.database.WebDeployment
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.entities.IdentityCard
import javax.inject.Inject
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object IdentityCardDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IdentityCardDaoTest extends JUnitSuite {
  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var gamesDao: GamesDao = _

  @Inject
  var envelopeDao: EnvelopeDao = _

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectByBarcode(): Unit = {
    identityCardDao.selectByBarcode(Barcode("33000010")) should equal(
      Some(IdentityCard(Barcode("33000010"), true))
    )

    identityCardDao.selectByBarcode(Barcode("33000043")) should equal(
      Some(IdentityCard(Barcode("33000043"), false))
    )

    identityCardDao.selectByBarcode(Barcode("44000013")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectActivatedByBarcode(): Unit = {
    identityCardDao.selectActivatedByBarcode(Barcode("33000010")) should equal(
      Some(IdentityCard(Barcode("33000010"), true))
    )

    identityCardDao.selectActivatedByBarcode(Barcode("33000043")) should equal(
      None
    )

    identityCardDao.selectActivatedByBarcode(Barcode("44000013")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectFromGame(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000047")).foreach(
      game => identityCardDao.selectFromGame(game) should equal(
        Some(IdentityCard(Barcode("33000021"), true))
      )
    )

    gamesDao.selectByBarcode(Barcode("11000058")).foreach(
      game => identityCardDao.selectFromGame(game) should equal(
        None
      )
    )

    gamesDao.selectByBarcode(Barcode("11000070")).foreach(
      game => identityCardDao.selectFromGame(game) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectFromEnvelope(): Unit = {
    envelopeDao.selectByBarcode(Barcode("44000104")).foreach(
      envelope => identityCardDao.selectFromEnvelope(envelope) should equal(
        Some(IdentityCard(Barcode("33000101"), true))
      )
    )

    envelopeDao.selectByBarcode(Barcode("44000035")).foreach(
      envelope => identityCardDao.selectFromEnvelope(envelope) should equal(
        None
      )
    )

    envelopeDao.selectByBarcode(Barcode("44000046")).foreach(
      envelope => identityCardDao.selectFromEnvelope(envelope) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllLend(): Unit = {
    identityCardDao.selectAllLend.toSet should equal(Set(
      IdentityCard(Barcode("33000010"), true),
      IdentityCard(Barcode("33000021"), true),
      IdentityCard(Barcode("33000101"), true)
    ))
  }
}
