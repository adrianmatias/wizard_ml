package controllers

import io.swagger.annotations._
import javax.inject.Inject
import models.{Card, CardRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Api(value = "/cards")
class CardController @Inject()(
                                cc: ControllerComponents,
                                cardRepo: CardRepository) extends AbstractController(cc) {

  @ApiOperation(
    value = "Find all Cards",
    response = classOf[Card],
    responseContainer = "List"
  )
  def getAllCards = Action.async {
    cardRepo.getAll.map { cards =>
      Ok(Json.toJson(cards))
    }
  }


  @ApiOperation(
    value = "Get a Card",
    response = classOf[Card]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Card not found")
  )
  )
  def getCard(@ApiParam(value = "The card id of the Card to fetch") id: Long) =
    Action.async { req =>
      cardRepo.getCard(id = id).map { maybeCard =>
        maybeCard.map { card =>
          Ok(Json.toJson(card))
        }.getOrElse(NotFound)
      }
    }

  @ApiOperation(
    value = "Add a new Card to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Card format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The Card to add, in Json Format", required = true, dataType = "models.Card", paramType = "body")
  )
  )
  def createCard() = Action.async(parse.json) {
    _.body.validate[Card].map { card =>
      cardRepo.addCard(card).map { _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Card format")))
  }

  @ApiOperation(
    value = "Update a Card",
    response = classOf[Card]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Card format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The updated Card, in Json Format", required = true, dataType = "models.Card", paramType = "body")
  )
  )
  def updateCard(@ApiParam(value = "The id of the Card to update")
                 id: Long) = Action.async(parse.json) { req =>
    req.body.validate[Card].map { card =>
      cardRepo.updateCard(card).map {
        case Some(card) => Ok(Json.toJson(card))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Delete a Card",
    response = classOf[Card]
  )
  def deleteCard(@ApiParam(value = "The id of the Card to delete") id: Long) = Action.async { req =>
    cardRepo.deleteCard(id).map {
      case Some(card) => Ok(Json.toJson(card))
      case _ => NotFound
    }
  }

  @ApiOperation(
    value = "Get a recommended Card",
    response = classOf[Card]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Card not found")
  )
  )
  def getCardRecommendation(@ApiParam(value = "The Profile id to recommend the Card to fetch") id: Long) =
    Action.async { req =>
      cardRepo.getCardRecommendation(profileId = id).map { maybeCard =>
        maybeCard.map { card =>
          Ok(Json.toJson(card))
        }.getOrElse(NotFound)
      }
    }
}
