/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.sparkta.driver.service.http

import akka.pattern.ask
import com.stratio.sparkta.driver.actor._
import com.stratio.sparkta.driver.constants.HttpConstant
import com.stratio.sparkta.driver.dto.{AggregationPoliciesDto, AggregationPoliciesValidator, StreamingContextStatusDto}
import com.wordnik.swagger.annotations._
import spray.routing._

@Api(value = "/policy", description = "Operations about policies.", position = 0)
trait PolicyHttpService extends BaseHttpService {

  override def routes: Route = findAll ~ create ~ findById ~ deleteById
  case class Result(message: String, desc: Option[String] = None)

  @ApiOperation(value = "Find all policies", notes = "Returns a policies list", httpMethod = "GET",
    response = classOf[String])
  @ApiResponses(
    Array(new ApiResponse(code = HttpConstant.NotFound, message = HttpConstant.NotFoundMessage)))
  def findAll: Route = {
    path("policy") {
      get {
        complete {
          supervisor.ask(GetAllContextStatus).mapTo[List[StreamingContextStatusDto]]
        }
      }
    }
  }

  @ApiOperation(value = "post a policy", notes = "Returns the result", httpMethod = "POST", response = classOf[Result])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "policy", defaultValue = "", value = "policy json", dataType = "AggregationPoliciesDto",
      required = true, paramType = "json")))
  def create: Route = {
    post {
      entity(as[AggregationPoliciesDto]) { p =>
        val isValidAndMessageTuple = AggregationPoliciesValidator.validateDto(p)
        validate(isValidAndMessageTuple._1, isValidAndMessageTuple._2) {
          complete {
            supervisor ! new CreateContext(p)
            new Result("Creating new context with name " + p.name)
          }
        }
      }
    }
  }

  @ApiOperation(value = "get policy status by name", notes = "Returns the status of a policy", httpMethod = "GET",
    response = classOf[StreamingContextStatusDto])
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "policy name", defaultValue = "", value = "policy name",
    dataType = "String", required = true)))
  def findById: Route = {
    pathPrefix("policy" / Segment) { name =>
      get {
        complete {
          supervisor.ask(new GetContextStatus(name)).mapTo[StreamingContextStatusDto]
        }
      }
    }
  }

  @ApiOperation(value = "delete a policy by name", notes = "Returns the status of the policy", httpMethod = "DELETE",
    response = classOf[StreamingContextStatusDto])
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "policy name", defaultValue = "", value = "policy name",
    dataType = "String", required = true)))
  def deleteById: Route = {
    pathPrefix("policy" / Segment) { name =>
      delete {
        complete {
          supervisor.ask(new DeleteContext(name)).mapTo[StreamingContextStatusDto]
        }
      }
    }
  }
}
