package club.magicfun.aquila.common;

public class ResponseResult {

	public ResponseResult(ValidationResult validationResult) {
		super();

		this.resultType = validationResult.getResultType();

		if (ResultType.SUCCESS == validationResult.getResultType()) {
			this.successMessage = validationResult.getSuccessMessage();
		} else if (ResultType.WARNING == validationResult.getResultType()) {
			this.warningMessage = validationResult.getWarningMessages().get(0);
		} else if (ResultType.ERROR == validationResult.getResultType()) {
			this.errorMessage = validationResult.getErrorMessages().get(0);
		}
	}

	private ResultType resultType;

	private String successMessage;

	private String warningMessage;

	private String errorMessage;

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

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
