package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public class OrderByMQLElement {
    private MQLElement element;
    private boolean asc = true;

    public OrderByMQLElement(MQLElement element, boolean asc) {
        this.element = element;
        this.asc = asc;
    }

    public OrderByMQLElement(MQLElement element) {
        this(element, true);
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public void setElement(MQLElement element) {
        this.element = element;
    }

    public MQLElement getElement() {
        return element;
    }

    public boolean getAsc() {
        return asc;
    }

}
