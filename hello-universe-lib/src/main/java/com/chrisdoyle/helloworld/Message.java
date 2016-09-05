package com.chrisdoyle.helloworld;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class Message implements Comparable<Message>, Serializable {

    private String message;

    public Message(final String message) {
        setMessage(message);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable<T>#compareTo(T)
     */
    @Override
    public int compareTo(final Message cmp) {
        return CompareToBuilder.reflectionCompare(this, cmp);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public String getMessage() {
        return this.message;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void setMessage(final String message) {
        Validate.notEmpty(message);
        this.message = message;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
