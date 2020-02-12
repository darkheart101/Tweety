package com.tkouleris.tweety;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.tkouleris.tweety.dao.TweetRepository;
import com.tkouleris.tweety.dao.UserRepository;
import com.tkouleris.tweety.model.Tweet;
import com.tkouleris.tweety.model.User;
import com.tkouleris.tweety.service.TweetService;
import com.tkouleris.tweety.util.TimestampUtil;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
public class TweetServiceTest {
	
	@InjectMocks
	TweetService service;
	@Mock
	TweetRepository R_Tweet;
	@Mock
	UserRepository R_User;
	@Mock
	TimestampUtil timestampUtil;
	@Mock
	Authentication authentication;
	
	
	@Test
	void should_return_a_Tweet_when_many_feed_found()
	{
		User loggedInUser = new User();
		loggedInUser.setUser_id(1);
		List<Tweet> myList = new ArrayList<>();
		Tweet t1 = new Tweet();
		t1.setTweet_user_id(loggedInUser);
		Tweet t2 = new Tweet();
		t2.setTweet_user_id(loggedInUser);
		myList.add(t1);
		myList.add(t1);
		
		Mockito.when(R_Tweet.findLatestTweetByUser(loggedInUser.getUser_id())).thenReturn(myList);	

		Tweet tweet = service.getFeed(loggedInUser);

		Assert.isTrue(tweet instanceof Tweet);
	}
	
	@Test
	void should_return_null_when_no_feed_found()
	{
		User loggedInUser = new User();
		loggedInUser.setUser_id(1);
		List<Tweet> myList = new ArrayList<>();
		
		Mockito.when(R_Tweet.findLatestTweetByUser(loggedInUser.getUser_id())).thenReturn(myList);	

		Tweet tweet = service.getFeed(loggedInUser);

		Assert.isTrue(tweet == null);
	}
	
	@Test
	void createTweet_should_return_a_tweet()
	{
		User loggedInUser = new User();
		loggedInUser.setUsername("tkouleris");
		loggedInUser.setUser_id(1);
		Tweet newTweet = new Tweet();
		newTweet.setTweet_message("unit testing");
		Mockito.when(authentication.getName()).thenReturn("tkouleris");
		Mockito.when(R_User.findByUsername("tkouleris")).thenReturn(loggedInUser);
		Mockito.when(R_Tweet.save(any(Tweet.class))).thenReturn(newTweet);
		Mockito.when(timestampUtil.currentTimestamp()).thenReturn(new Timestamp(System.currentTimeMillis()));
		
		Tweet savedTweet = service.createTweet(authentication, newTweet);
		Assert.isTrue(savedTweet instanceof Tweet);
		Assert.isTrue("unit testing".equals(newTweet.getTweet_message()));
		Assert.isTrue(loggedInUser.equals(newTweet.getTweet_user_id()));		
	}
	
	@Test
	void updateTweet_should_change_the_tweet_message() throws Exception
	{
		// Prepare Data
		User loggedInUser = new User();
		loggedInUser.setUser_id(1);
		loggedInUser.setUsername("tkouleris");
		
		Tweet tweet = new Tweet();
		tweet.setTweet_id(1);
		tweet.setTweet_message("first message");
		tweet.setTweet_user_id(loggedInUser);
		
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse("23/09/2007");
		long time = date.getTime();
		Timestamp t = new Timestamp(time);
		Mockito.when(timestampUtil.currentTimestamp()).thenReturn(t);
		
		Mockito.when(R_Tweet.findById((long) 1)).thenReturn(Optional.of(tweet));
		Mockito.when(R_Tweet.save(tweet)).thenReturn(tweet);
		Mockito.when(authentication.getName()).thenReturn("tkouleris");
		Mockito.when(R_User.findByUsername("tkouleris")).thenReturn(loggedInUser);		

		
		// run
		service.updateTweet(authentication,1, "new message");
		
		// Assertions
		Assert.isTrue(tweet.getTweet_message().equals("new message"));
		assertThat(tweet.getTweet_updated_at()).isEqualTo(t);		
	}
	
	@Test
	void updateTweet_should_throw_exception_when_record_not_found() throws Exception
	{
		User loggedInUser = new User();
		loggedInUser.setUser_id(1);
		loggedInUser.setUsername("tkouleris");
		Mockito.when(authentication.getName()).thenReturn("tkouleris");
		Mockito.when(R_User.findByUsername("tkouleris")).thenReturn(loggedInUser);
		Mockito.when(R_Tweet.findById((long) 1)).thenReturn(null);				
		Assertions.assertThrows(Exception.class,()->service.updateTweet(authentication,1, "new message") );
	}	
	
	@Test
	void updateTweet_should_throw_exception_when_loggedIn_user_is_not_the_owner_of_the_tweet() throws Exception
	{
		User loggedInUser = new User();
		User OtherUser = new User();
		loggedInUser.setUser_id(1);
		loggedInUser.setUsername("tkouleris");
		Tweet tweet = new Tweet();
		tweet.setTweet_id(1);
		tweet.setTweet_message("first message");
		tweet.setTweet_user_id(OtherUser);
		Mockito.when(authentication.getName()).thenReturn("tkouleris");
		Mockito.when(R_User.findByUsername("tkouleris")).thenReturn(loggedInUser);
		Mockito.when(R_Tweet.findById((long) 1)).thenReturn(Optional.of(tweet));				
		Assertions.assertThrows(Exception.class,()->service.updateTweet(authentication,1, "new message") );
	}		
}	
