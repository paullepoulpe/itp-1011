package http;

public class FailureReasonExeption extends Exception {
	private String failureReason;

	public FailureReasonExeption(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getFailureReason() {
		return failureReason;
	}
}
