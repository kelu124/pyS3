package com.itextpdf.xmp.impl;

import com.itextpdf.xmp.XMPDateTime;
import com.itextpdf.xmp.XMPException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.bytedeco.javacpp.PGRFlyCapture;

public final class ISO8601Converter {
    private ISO8601Converter() {
    }

    public static XMPDateTime parse(String iso8601String) throws XMPException {
        return parse(iso8601String, new XMPDateTimeImpl());
    }

    public static XMPDateTime parse(String iso8601String, XMPDateTime binValue) throws XMPException {
        if (iso8601String == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        if (iso8601String.length() != 0) {
            ParseState input = new ParseState(iso8601String);
            if (input.ch(0) == '-') {
                input.skip();
            }
            int value = input.gatherInt("Invalid year in date string", 9999);
            if (!input.hasNext() || input.ch() == '-') {
                if (input.ch(0) == '-') {
                    value = -value;
                }
                binValue.setYear(value);
                if (input.hasNext()) {
                    input.skip();
                    value = input.gatherInt("Invalid month in date string", 12);
                    if (!input.hasNext() || input.ch() == '-') {
                        binValue.setMonth(value);
                        if (input.hasNext()) {
                            input.skip();
                            value = input.gatherInt("Invalid day in date string", 31);
                            if (!input.hasNext() || input.ch() == 'T') {
                                binValue.setDay(value);
                                if (input.hasNext()) {
                                    input.skip();
                                    binValue.setHour(input.gatherInt("Invalid hour in date string", 23));
                                    if (input.hasNext()) {
                                        if (input.ch() == ':') {
                                            input.skip();
                                            value = input.gatherInt("Invalid minute in date string", 59);
                                            if (!input.hasNext() || input.ch() == ':' || input.ch() == 'Z' || input.ch() == '+' || input.ch() == '-') {
                                                binValue.setMinute(value);
                                            } else {
                                                throw new XMPException("Invalid date string, after minute", 5);
                                            }
                                        }
                                        if (input.hasNext()) {
                                            if (input.hasNext() && input.ch() == ':') {
                                                input.skip();
                                                value = input.gatherInt("Invalid whole seconds in date string", 59);
                                                if (!input.hasNext() || input.ch() == '.' || input.ch() == 'Z' || input.ch() == '+' || input.ch() == '-') {
                                                    binValue.setSecond(value);
                                                    if (input.ch() == '.') {
                                                        input.skip();
                                                        int digits = input.pos();
                                                        value = input.gatherInt("Invalid fractional seconds in date string", PGRFlyCapture.FLYCAPTURE_BUS_MESSAGE);
                                                        if (!input.hasNext() || input.ch() == 'Z' || input.ch() == '+' || input.ch() == '-') {
                                                            digits = input.pos() - digits;
                                                            while (digits > 9) {
                                                                value /= 10;
                                                                digits--;
                                                            }
                                                            while (digits < 9) {
                                                                value *= 10;
                                                                digits++;
                                                            }
                                                            binValue.setNanoSecond(value);
                                                        } else {
                                                            throw new XMPException("Invalid date string, after fractional second", 5);
                                                        }
                                                    }
                                                }
                                                throw new XMPException("Invalid date string, after whole seconds", 5);
                                            } else if (!(input.ch() == 'Z' || input.ch() == '+' || input.ch() == '-')) {
                                                throw new XMPException("Invalid date string, after time", 5);
                                            }
                                            int tzSign = 0;
                                            int tzHour = 0;
                                            int tzMinute = 0;
                                            if (input.hasNext()) {
                                                if (input.ch() == 'Z') {
                                                    input.skip();
                                                } else if (input.hasNext()) {
                                                    if (input.ch() == '+') {
                                                        tzSign = 1;
                                                    } else if (input.ch() == '-') {
                                                        tzSign = -1;
                                                    } else {
                                                        throw new XMPException("Time zone must begin with 'Z', '+', or '-'", 5);
                                                    }
                                                    input.skip();
                                                    tzHour = input.gatherInt("Invalid time zone hour in date string", 23);
                                                    if (input.hasNext()) {
                                                        if (input.ch() == ':') {
                                                            input.skip();
                                                            tzMinute = input.gatherInt("Invalid time zone minute in date string", 59);
                                                        } else {
                                                            throw new XMPException("Invalid date string, after time zone hour", 5);
                                                        }
                                                    }
                                                }
                                                binValue.setTimeZone(new SimpleTimeZone((((tzHour * 3600) * 1000) + ((tzMinute * 60) * 1000)) * tzSign, ""));
                                                if (input.hasNext()) {
                                                    throw new XMPException("Invalid date string, extra chars at end", 5);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            throw new XMPException("Invalid date string, after day", 5);
                        }
                    }
                    throw new XMPException("Invalid date string, after month", 5);
                }
            }
            throw new XMPException("Invalid date string, after year", 5);
        }
        return binValue;
    }

    public static String render(XMPDateTime dateTime) {
        StringBuffer buffer = new StringBuffer();
        if (dateTime.hasDate()) {
            DecimalFormat df = new DecimalFormat("0000", new DecimalFormatSymbols(Locale.ENGLISH));
            buffer.append(df.format((long) dateTime.getYear()));
            if (dateTime.getMonth() == 0) {
                return buffer.toString();
            }
            df.applyPattern("'-'00");
            buffer.append(df.format((long) dateTime.getMonth()));
            if (dateTime.getDay() == 0) {
                return buffer.toString();
            }
            buffer.append(df.format((long) dateTime.getDay()));
            if (dateTime.hasTime()) {
                buffer.append('T');
                df.applyPattern("00");
                buffer.append(df.format((long) dateTime.getHour()));
                buffer.append(':');
                buffer.append(df.format((long) dateTime.getMinute()));
                if (!(dateTime.getSecond() == 0 && dateTime.getNanoSecond() == 0)) {
                    double seconds = ((double) dateTime.getSecond()) + (((double) dateTime.getNanoSecond()) / 1.0E9d);
                    df.applyPattern(":00.#########");
                    buffer.append(df.format(seconds));
                }
                if (dateTime.hasTimeZone()) {
                    int offset = dateTime.getTimeZone().getOffset(dateTime.getCalendar().getTimeInMillis());
                    if (offset == 0) {
                        buffer.append('Z');
                    } else {
                        int thours = offset / 3600000;
                        int tminutes = Math.abs((offset % 3600000) / 60000);
                        df.applyPattern("+00;-00");
                        buffer.append(df.format((long) thours));
                        df.applyPattern(":00");
                        buffer.append(df.format((long) tminutes));
                    }
                }
            }
        }
        return buffer.toString();
    }
}
