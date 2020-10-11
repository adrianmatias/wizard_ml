package controllers

import java.util.UUID

import io.swagger.annotations._
import javax.inject.Inject
import models.{Match, MatchRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Api(value = "/matchs")
class MatchController @Inject()(
                                 cc: ControllerComponents,
                                 matchRepo: MatchRepository) extends AbstractController(cc) {

  @ApiOperation(
    value = "Find all Matchs",
    response = classOf[Match],
    responseContainer = "List"
  )
  def getAllMatchs = Action.async {
    matchRepo.getAll.map { matchs =>
      Ok(Json.toJson(matchs))
    }
  }


  @ApiOperation(
    value = "Get a Match",
    response = classOf[Match]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Match not found")
  )
  )
  def getMatch(@ApiParam(value = "The card and profile ids of the Match to fetch") cardId: Long, profileId: Long) =
    Action.async { req =>
      matchRepo.getMatch(cardId = cardId, profileId = profileId).map { maybeMatch =>
        maybeMatch.map { matchWiz =>
          Ok(Json.toJson(matchWiz))
        }.getOrElse(NotFound)
      }
    }

  @ApiOperation(
    value = "Add a new Match to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Match format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The Match to add, in Json Format", required = true, dataType = "models.Match", paramType = "body")
  )
  )
  def createMatch() = Action.async(parse.json) {
    _.body.validate[Match].map { matchWiz =>
      matchRepo.addMatch(matchWiz).map { _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Match format")))
  }

  @ApiOperation(
    value = "Update a Match",
    response = classOf[Match]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Match format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The updated Match, in Json Format", required = true, dataType = "models.Match", paramType = "body")
  )
  )
  def updateMatch(@ApiParam(value = "The id of the Match to update")
                  matchId: UUID) = Action.async(parse.json) { req =>
    req.body.validate[Match].map { matchWiz =>
      matchRepo.updateMatch(matchWiz).map {
        case Some(matchWiz) => Ok(Json.toJson(matchWiz))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Delete a Match",
    response = classOf[Match]
  )
  def deleteMatch(@ApiParam(value = "The id of the Match to delete") cardId: Long, profileId: Long) = Action.async { req =>
    matchRepo.deleteMatch(cardId = cardId, profileId = profileId).map {
      case Some(matchWiz) => Ok(Json.toJson(matchWiz))
      case _ => NotFound
    }
  }
}
