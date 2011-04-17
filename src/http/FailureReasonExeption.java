package http;

/**
 * Exception qui est levee suite a une requete http, si la connexion n'a pu se
 * faire ou si le tracker renvoye une failure reason
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class FailureReasonExeption extends Exception {
	private String failureReason;

	public FailureReasonExeption(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getFailureReason() {
		return failureReason;
	}
}
