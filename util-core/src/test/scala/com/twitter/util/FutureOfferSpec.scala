package com.twitter.concurrent

import org.specs.SpecificationWithJUnit
import org.specs.mock.Mockito
import com.twitter.util.{Promise, Return, Try}

class FutureOfferSpec extends SpecificationWithJUnit with Mockito {
  "Future.toOffer" should {
    "activate when future is satisfied (poll)" in {
      val p = new Promise[Int]
      val o = p.toOffer
      o.prepare().poll must beNone
      p() = Return(123)
      o.prepare().poll must beLike {
         case Some(Return(tx)) =>
           tx.ack().poll must beLike {
             case Some(Return(Tx.Commit(Return(123)))) => true
           }
      }
    }
  }
}
