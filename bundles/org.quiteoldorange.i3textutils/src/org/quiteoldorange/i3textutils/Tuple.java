/**
 *
 */
package org.quiteoldorange.i3textutils;

/**
 * @author ozolotarev
 *
 */
public class Tuple<T, U>
{

    private final T first;

    private final U second;

    public Tuple(final T firstElement, final U secondElement)
    {
        this.first = firstElement;
        this.second = secondElement;
    }

    public T getFirst()
    {
        return first;
    }

    public U getSecond()
    {
        return second;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (this.getClass().equals(other.getClass()))
        {
            Tuple<?, ?> otherTuple = (Tuple<?, ?>)other;
            boolean isEqual = (first == null) ? otherTuple.getFirst() == null : first.equals(otherTuple.getFirst());

            if (!isEqual)
                return false;

            return (second == null) ? otherTuple.getSecond() == null : second.equals(otherTuple.getSecond());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return first == null ? 0 : first.hashCode() + 17 * (second == null ? 0 : second.hashCode());
    }

    @Override
    public String toString()
    {
        return "Tuple(" + first + ", " + second + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
