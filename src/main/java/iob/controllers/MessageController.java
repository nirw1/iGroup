package iob.controllers;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.MessageBoundary;

@RestController
public class MessageController {
	@RequestMapping(path = "/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageBoundary helloWorld() {
		return this.hello("World", "");
	}

	@RequestMapping(path = "/hello/{firstName}/{lastName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageBoundary hello(@PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName) {
		MessageBoundary boundary = new MessageBoundary("Hello " + firstName + " " + lastName);
		boundary.setImportant(false);
		boundary.setMessageTimestamp(new Date());
		return boundary;
	}
}
