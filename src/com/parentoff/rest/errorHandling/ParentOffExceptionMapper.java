package com.parentoff.rest.errorHandling;

import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.NotFoundException;

@Provider
public class ParentOffExceptionMapper implements ExceptionMapper<Throwable> {
	org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(ParentOffExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception) {
		String traceId = UUID.randomUUID().toString();
		ParentOffException parentOffException = null;
		Status status = Status.BAD_REQUEST;

		if (exception
				.getClass()
				.getName()
				.equalsIgnoreCase(UnrecognizedPropertyException.class.getName())) {

			UnrecognizedPropertyException ex = (UnrecognizedPropertyException) exception;
			String unrecognizedProperty = ex.getUnrecognizedPropertyName();
			String message = "Unrecognized Property : '" + unrecognizedProperty
					+ "'";
			parentOffException = new ParentOffException(
					Status.BAD_REQUEST.getStatusCode(), message,
					ex.getMessage());
		}

		if (exception.getClass().getName()
				.equalsIgnoreCase(JsonParseException.class.getName())) {

			JsonParseException ex = (JsonParseException) exception;
			String message = "INCORRECT JSON FORMAT : Error occured while parsing input JSON";
			parentOffException = new ParentOffException(
					Status.BAD_REQUEST.getStatusCode(), message,
					ex.getMessage());
		}

		if (exception.getClass().getName()
				.equalsIgnoreCase(JsonMappingException.class.getName())) {

			JsonMappingException ex = (JsonMappingException) exception;
			String message = "Error occured while mapping fields. The data type of one or more field may not be as expected.";
			parentOffException = new ParentOffException(
					Status.BAD_REQUEST.getStatusCode(), message,
					ex.getMessage());

		}

		if (exception.getClass().getName()
				.equalsIgnoreCase(NotFoundException.class.getName())) {

			NotFoundException ex = (NotFoundException) exception;
			String message = "The requested URI: '"
					+ ex.getNotFoundUri()
					+ "' does not exist or it has moved. Make sure the request method and header parameter are correct";
			status = Status.NOT_FOUND;
			parentOffException = new ParentOffException(
					Status.NOT_FOUND.getStatusCode(), message, null);

		}

		if (exception.getClass().getName()
				.equalsIgnoreCase(WebApplicationException.class.getName())) {

			WebApplicationException ex = (WebApplicationException) exception;
			String message = "UNSUPPORTED MEDIA TYPE: Data may not have been sent in expected format. Please check the content-type and request method header params are set correctly.";
			status = Status.UNSUPPORTED_MEDIA_TYPE;
			parentOffException = new ParentOffException(
					Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), message,
					ex.getMessage());

		}

		if (parentOffException == null) {
			String message = "Unexpected error occured. Contact Ubona Technologies for further assistance with traceId - "
					+ traceId;
			status = Status.INTERNAL_SERVER_ERROR;
			parentOffException = new ParentOffException(
					Status.INTERNAL_SERVER_ERROR.getStatusCode(), message, null);
		}

		LOGGER.error("Exception occured : Trace ID={ " + traceId
				+ " },cause : ", exception);

		parentOffException.setTraceId(traceId);
		return Response.status(status).entity(parentOffException)
				.type(MediaType.APPLICATION_JSON).build();
	}
}
