package edu.knoldus

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Query, Twitter, TwitterFactory}
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.log4j._

/**
  * TwitterFeeds Class. Demonstrates use of twitter4j API
  */
object TwitterFeeds {
  private val logger = Logger.getLogger(getClass.getName)
  private val config = ConfigFactory.load()
  val configurationBuilder: ConfigurationBuilder = new ConfigurationBuilder()
  configurationBuilder.setDebugEnabled(true)
    .setOAuthConsumerKey(config.getString("consumerKey.value"))
    .setOAuthConsumerSecret(config.getString("consumerSecretKey.value"))
    .setOAuthAccessToken(config.getString("accessToken.value"))
    .setOAuthAccessTokenSecret(config.getString("accessTokenSecret.value"))
  val twitter: Twitter = new TwitterFactory(configurationBuilder.build()).getInstance()

  /**
    * Method that takes two parameters: tag or string to be searched, and number of tweets to be fetched and returns tweets along with username, likes, and retweets
    */
  @throws
  def getTweets(searchTag: String, numOfTweets: Int): Future[String] = Future {
    try {
      val query: Query = new Query(searchTag)
      query.getMaxId
      query.setCount(numOfTweets)
      val listOfTweets = twitter.search(query).getTweets
      val listIterator = listOfTweets.iterator
      val details = for {_ <- 1 to query.getCount
                         if listIterator.hasNext
                         tweet = listIterator.next}
        yield s"${tweet.getUser.getName} tweeted: ${tweet.getText}\nLikes:${tweet.getFavoriteCount}, Retweets:${tweet.getRetweetCount}"
      logger.info("Details successfully fetched.")
      details.toList.mkString("\n\n")
    } catch {
      case ex: Exception => logger.debug(s"${ex.printStackTrace()}")
        "Exception encountered in fetching tweets"

    }
  }

  /**
    * Method that takes two parameters: tag or string to be searched, and number of tweets to be fetched and returns average number of retweets per tweet
    */
  def getAvgReTweets(searchTag: String, numOfTweets: Int): Future[Int] = Future {
    try {
      val query: Query = new Query(searchTag)
      query.getMaxId
      query.setCount(numOfTweets)
      val listOfTweets = twitter.search(query).getTweets
      val listIterator = listOfTweets.iterator
      val details = for {_ <- 1 to query.getCount
                         if listIterator.hasNext
                         tweet = listIterator.next}
        yield tweet.getRetweetCount
      details.toList.sum / numOfTweets
    } catch {
      case ex: Exception => logger.debug(s"${ex.printStackTrace()}")
        -1
    }
  }

  /**
    * Method that takes two parameters: tag or string to be searched, and number of tweets to be fetched and returns average number of likes per tweet
    */
  def getAvgLikes(searchTag: String, numOfTweets: Int): Future[Int] = Future {
    try {
      val query: Query = new Query(searchTag)
      query.getMaxId
      query.setCount(numOfTweets)
      val listOfTweets = twitter.search(query).getTweets
      val listIterator = listOfTweets.iterator
      val details = for {_ <- 1 to query.getCount
                         if listIterator.hasNext
                         tweet = listIterator.next}
        yield tweet.getFavoriteCount
      details.toList.sum / numOfTweets
    } catch {
      case ex: Exception => logger.debug(s"${ex.printStackTrace()}")
        -1

    }
  }
}
