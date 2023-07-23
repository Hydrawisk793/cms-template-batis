package kaphein.template.cmstemplatebatis.persistence;

public class EntityRelationshipInvalidException extends RuntimeException
{
    public EntityRelationshipInvalidException(String message)
    {
        super(message);
    }

    private static final long serialVersionUID = 8554113810224082372L;
}
