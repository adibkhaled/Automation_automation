package SeleniumFrameWork.prorail.nl;

public class FoundNonExpectedValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public FoundNonExpectedValueException() {}

    //Constructor that accepts a message
    public FoundNonExpectedValueException(String message)
    {
       super(message);
    }
}
