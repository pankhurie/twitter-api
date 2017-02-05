package edu.knoldus
import org.scalatest.FunSuite

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps
class TwitterFeedsTest extends FunSuite {

  test("Fetch 100 tweets with # scala in 20 seconds"){
    assert(Await.result(TwitterFeeds.getTweets("#scala", 100), 20 seconds)!= "Exception encountered in fetching tweets")
  }

  test("Find average likes on 100 tweets with # scala in 20 seconds"){
    assert(Await.result(TwitterFeeds.getAvgLikes("#scala", 100), 40 seconds)!= -1)
  }

  test("Find average retweets on 100 tweets with # scala in 20 seconds"){
    assert(Await.result(TwitterFeeds.getAvgReTweets("#scala", 100), 60 seconds)!= -1)
  }

}
