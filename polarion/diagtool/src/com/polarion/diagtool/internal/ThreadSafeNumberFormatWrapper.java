/*
 * $Id$
 *
 * Copyright (C) 2000-2005 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2000-2005 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * Polarion Software MAKES NO REPRESENTATIONS OR WARRANTIES 
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Polarion Software
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.diagtool.internal;

import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Currency;

/**
 * ruzam is the one reponsible for missing comment
 *
 * @author  <A HREF="mailto:dev@polarion.com">Miroslav Ruza</A>, Polarion Software
 * @version $Revision$ $Date$
 */
@SuppressWarnings("nls")
public class ThreadSafeNumberFormatWrapper extends NumberFormat {
    private static final long serialVersionUID = 3144876071129054022L;

    private final NumberFormat sourceFormat;

    private ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            synchronized (sourceFormat) {
                return sourceFormat.clone();
            }
        }
    };

    public ThreadSafeNumberFormatWrapper(NumberFormat format) {
        synchronized (format) {
            sourceFormat = (NumberFormat) format.clone();
        }
    }

    private NumberFormat getTreadLocalDelegate() {
        return (NumberFormat) threadLocal.get();
    }

    /* @see java.lang.Object#clone() */
    @Override
    public Object clone() {
        return super.clone();
    }

    /* @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        return getTreadLocalDelegate().equals(obj);
    }

    /* @see java.text.NumberFormat#format(double, java.lang.StringBuffer, java.text.FieldPosition) */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return getTreadLocalDelegate().format(number, toAppendTo, pos);
    }

    /* @see java.text.NumberFormat#format(long, java.lang.StringBuffer, java.text.FieldPosition) */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return getTreadLocalDelegate().format(number, toAppendTo, pos);
    }

    /* @see java.text.NumberFormat#getCurrency() */
    @Override
    public Currency getCurrency() {
        return getTreadLocalDelegate().getCurrency();
    }

    /* @see java.text.NumberFormat#getMaximumFractionDigits() */
    @Override
    public int getMaximumFractionDigits() {
        return getTreadLocalDelegate().getMaximumFractionDigits();
    }

    /* @see java.text.NumberFormat#getMaximumIntegerDigits() */
    @Override
    public int getMaximumIntegerDigits() {
        return getTreadLocalDelegate().getMaximumIntegerDigits();
    }

    /* @see java.text.NumberFormat#getMinimumFractionDigits() */
    @Override
    public int getMinimumFractionDigits() {
        return getTreadLocalDelegate().getMinimumFractionDigits();
    }

    /* @see java.text.NumberFormat#getMinimumIntegerDigits() */
    @Override
    public int getMinimumIntegerDigits() {
        return getTreadLocalDelegate().getMinimumIntegerDigits();
    }

    /* @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return getTreadLocalDelegate().hashCode();
    }

    /* @see java.text.NumberFormat#isGroupingUsed() */
    @Override
    public boolean isGroupingUsed() {
        return getTreadLocalDelegate().isGroupingUsed();
    }

    /* @see java.text.NumberFormat#isParseIntegerOnly() */
    @Override
    public boolean isParseIntegerOnly() {
        return getTreadLocalDelegate().isParseIntegerOnly();
    }

    /* @see java.text.NumberFormat#parse(java.lang.String, java.text.ParsePosition) */
    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return getTreadLocalDelegate().parse(source, parsePosition);
    }

    /* @see java.text.NumberFormat#parse(java.lang.String) */
    @Override
    public Number parse(String source) throws ParseException {
        return getTreadLocalDelegate().parse(source);
    }

    /* @see java.text.NumberFormat#setCurrency(java.util.Currency) */
    @Override
    public void setCurrency(Currency currency) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setGroupingUsed(boolean) */
    @Override
    public void setGroupingUsed(boolean newValue) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setMaximumFractionDigits(int) */
    @Override
    public void setMaximumFractionDigits(int newValue) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setMaximumIntegerDigits(int) */
    @Override
    public void setMaximumIntegerDigits(int newValue) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setMinimumFractionDigits(int) */
    @Override
    public void setMinimumFractionDigits(int newValue) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setMinimumIntegerDigits(int) */
    @Override
    public void setMinimumIntegerDigits(int newValue) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.NumberFormat#setParseIntegerOnly(boolean) */
    @Override
    public void setParseIntegerOnly(boolean value) {
        throw new UnsupportedOperationException("Changing state not supported.");
    }

    /* @see java.text.Format#formatToCharacterIterator(java.lang.Object) */
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return getTreadLocalDelegate().formatToCharacterIterator(obj);
    }

    /* @see java.text.Format#parseObject(java.lang.String) */
    @Override
    public Object parseObject(String source) throws ParseException {
        return getTreadLocalDelegate().parseObject(source);
    }
}

/*
 * $Log$
 */