package org.flush.erates.errormessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.flush.erates.dto.ErrorMessage;

public class ErrorMessages {
	private static ErrorMessage message;
	
	public static Response diapasonBadDatesException() {
		message = new ErrorMessage("Bad Date", 400);
		return Response.status(Status.BAD_REQUEST)
				.entity(message)
				.build();
	}
	
	public static Response diapasonDateBeforeException() {
		message = new ErrorMessage("Start Date Is Before End Date", 400);
		return Response.status(Status.BAD_REQUEST)
				.entity(message)
				.build();
	}
}
