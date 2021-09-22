package model;

/** Checked exception for when a validation check fails in the controller. */
public class ModelException extends Exception
{
    public ModelException(String msg)
    {
        super(msg);
    }
    
    public ModelException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
