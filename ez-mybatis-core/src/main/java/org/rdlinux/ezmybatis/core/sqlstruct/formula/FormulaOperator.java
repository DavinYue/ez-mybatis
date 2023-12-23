package org.rdlinux.ezmybatis.core.sqlstruct.formula;

/**
 * 公式运算符
 */
public enum FormulaOperator {
    /**
     * 加
     */
    ADD("+"),
    /**
     * 减
     */
    SUBTRACT("-"),
    /**
     * 乘
     */
    MULTIPLY("*"),
    /**
     * 除
     */
    DIVIDE("/"),
    /**
     * 空操作符, 用于公式开始
     */
    EMPTY("");
    private String symbol;

    FormulaOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
