package beelivery.misc;

public interface IIdentifiable<I> {
    I getId();
    void setId(I id);
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
