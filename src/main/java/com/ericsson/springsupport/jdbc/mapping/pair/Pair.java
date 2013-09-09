package com.ericsson.springsupport.jdbc.mapping.pair;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Pair implements Serializable {
    private static final long serialVersionUID = -4545221676162956602L;
    private String leftPairValue;
    private String rightPairValue;

    public Pair(String leftPairValue, String rightPairValue) {
        this.leftPairValue = leftPairValue;
        this.rightPairValue = rightPairValue;
    }

    public String getLeftPairValue() {
        return leftPairValue;
    }

    public void setLeftPairValue(String leftPairValue) {
        this.leftPairValue = leftPairValue;
    }

    public String getRightPairValue() {
        return rightPairValue;
    }

    public void setRightPairValue(String rightPairValue) {
        this.rightPairValue = rightPairValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            if (StringUtils.equals(pair.getLeftPairValue(), leftPairValue)
                    && StringUtils.equals(pair.getRightPairValue(), rightPairValue))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (new HashCodeBuilder(17, 37)).append(leftPairValue).append(rightPairValue).hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", leftPairValue, rightPairValue);
    }
}
