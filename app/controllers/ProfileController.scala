package controllers

import io.swagger.annotations._
import javax.inject.Inject
import models.{Profile, ProfileRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Api(value = "/profiles")
class ProfileController @Inject()(
                                cc: ControllerComponents,
                                profileRepo: ProfileRepository) extends AbstractController(cc) {

  @ApiOperation(
    value = "Find all Profiles",
    response = classOf[Profile],
    responseContainer = "List"
  )
  def getAllProfiles = Action.async {
    profileRepo.getAll.map { profiles =>
      Ok(Json.toJson(profiles))
    }
  }


  @ApiOperation(
    value = "Get a Profile",
    response = classOf[Profile]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Profile not found")
  )
  )
  def getProfile(@ApiParam(value = "The profile id of the Profile to fetch") id: Long) =
    Action.async { req =>
      profileRepo.getProfile(id = id).map { maybeProfile =>
        maybeProfile.map { profile =>
          Ok(Json.toJson(profile))
        }.getOrElse(NotFound)
      }
    }

  @ApiOperation(
    value = "Add a new Profile to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Profile format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The Profile to add, in Json Format", required = true, dataType = "models.Profile", paramType = "body")
  )
  )
  def createProfile() = Action.async(parse.json) {
    _.body.validate[Profile].map { profile =>
      profileRepo.addProfile(profile).map { _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Profile format")))
  }

  @ApiOperation(
    value = "Update a Profile",
    response = classOf[Profile]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Profile format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The updated Profile, in Json Format", required = true, dataType = "models.Profile", paramType = "body")
  )
  )
  def updateProfile(@ApiParam(value = "The id of the Profile to update")
                 id: Long) = Action.async(parse.json) { req =>
    req.body.validate[Profile].map { profile =>
      profileRepo.updateProfile(profile).map {
        case Some(profile) => Ok(Json.toJson(profile))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Delete a Profile",
    response = classOf[Profile]
  )
  def deleteProfile(@ApiParam(value = "The id of the Profile to delete") id: Long) = Action.async { req =>
    profileRepo.deleteProfile(id).map {
      case Some(profile) => Ok(Json.toJson(profile))
      case _ => NotFound
    }
  }
}
