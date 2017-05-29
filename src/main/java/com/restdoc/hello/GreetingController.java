package com.restdoc.hello;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sumit
 *
 */
@RestController
@RequestMapping("/greeting")
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	/**
	 * @param name
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	/**
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/abc", method = RequestMethod.GET)
	public Greeting greeting2(@RequestParam(value = "id", defaultValue = "1002") Long id,
			@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println(id);
		return new Greeting(id, String.format(template, name));
	}

	/**
	 * @param greeting
	 * @return
	 */
	@RequestMapping(value = "/abcd", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public Greeting greeting3(@RequestBody Greeting greeting) {
		return new Greeting(greeting.getId(), greeting.getContent());
	}

}
