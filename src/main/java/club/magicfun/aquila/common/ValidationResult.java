package club.magicfun.aquila.common;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	public ValidationResult(ResultType resultType) {
		super();
		this.resultType = resultType;
	}

	private ResultType resultType;

	private String successMessage;

	private List<String> warningMessages = new ArrayList<String>();

	private List<String> errorMessages = new ArrayList<String>();

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void addWarningMessage(String warningMessage) {
		this.warningMessages.add(warningMessage);
	}

	public void addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
	}

}
