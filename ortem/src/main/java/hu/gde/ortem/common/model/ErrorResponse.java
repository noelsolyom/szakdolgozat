package hu.gde.ortem.common.model;

/*
* @author  Noel Solyom
*/
public class ErrorResponse {

	private String errorMessage;

	public ErrorResponse(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ErrorResponse [errorMessage=" + errorMessage + "]";
	}

}
