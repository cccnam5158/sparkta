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

package com.stratio.sparkta.plugin.operator.min

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class MinOperatorSpec extends WordSpec with Matchers {

  "Min operator" should {

    "processMap must be " in {
      val inputField = new MinOperator(Map())
      inputField.processMap(Map("field1" -> 1, "field2" -> 2)) should be(Some(0d))

      val inputFields2 = new MinOperator(Map("inputField" -> "field1"))
      inputFields2.processMap(Map("field3" -> 1, "field2" -> 2)) should be(Some(0d))

      val inputFields3 = new MinOperator(Map("inputField" -> "field1"))
      inputFields3.processMap(Map("field1" -> 1, "field2" -> 2)) should be(Some(1))
    }

    "processReduce must be " in {
      val inputFields = new MinOperator(Map())
      inputFields.processReduce(Seq()) should be(Some(0d))

      val inputFields2 = new MinOperator(Map())
      inputFields2.processReduce(Seq(Some(1), Some(1))) should be(Some(1d))

      val inputFields3 = new MinOperator(Map())
      inputFields3.processReduce(Seq(Some(1), Some(2), Some(3))) should be(Some(1d))
    }
  }
}