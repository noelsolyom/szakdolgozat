package hu.gde.ortem.user.model;

import java.util.Optional;

import hu.gde.ortem.common.model.ErrorResponse;

public class UserResponse<T> {

	private Optional<T> response = Optional.empty();
	private Optional<ErrorResponse> error = Optional.empty();

	public Optional<T> getResponse() {
		return response;
	}

	public void setResponse(Optional<T> response) {
		this.response = response;
	}

	public Optional<ErrorResponse> getError() {
		return error;
	}

	public void setError(Optional<ErrorResponse> error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "UserResponse [response=" + response + ", error=" + error + "]";
	}

}
