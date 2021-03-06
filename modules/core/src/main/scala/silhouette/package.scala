/**
 * Licensed to the Minutemen Group under one or more contributor license
 * agreements. See the COPYRIGHT file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.circe.{ ACursor, Json }

import scala.annotation.tailrec

/**
 * A framework agnostic authentication library for Scala that supports several authentication methods,
 * including OAuth1, OAuth2, OpenID, Credentials or custom authentication schemes.
 */
package object silhouette {

  /**
   * Monkey patches the [[io.circe.ACursor]] class to get the `downAt` function back, which was removed in
   * version 0.12.0-M4.
   *
   * @see https://gitter.im/circe/circe?at=5d3f71eff0ff3e2bba8ece73
   * @param cursor The cursor to patch.
   */
  implicit class RichACursor(cursor: ACursor) {

    /**
     * If the focus is a JSON array, move to the first element that satisfies the given predicate.
     */
    def downAt(p: Json => Boolean): ACursor = {
      @tailrec
      def find(c: ACursor): ACursor = {
        if (c.succeeded) { if (c.focus.exists(p)) c else find(c.right) } else c
      }

      find(cursor.downArray)
    }
  }
}
