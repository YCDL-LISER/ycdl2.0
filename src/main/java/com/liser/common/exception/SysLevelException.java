/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.liser.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SysLevelException extends RuntimeException
{

    public SysLevelException(String msg, Throwable th)
    {
        super(msg, th);
    }

    public SysLevelException(String msg)
    {
        super(msg);
    }

    public SysLevelException(Throwable th)
    {
        super(th);
    }

    public void printStackTrace(PrintStream s)
    {
        if(super.getCause() != null)
        {
            s.print((new StringBuilder(String.valueOf(getClass().getName()))).append(" Caused by: ").toString());
            super.getCause().printStackTrace(s);
        } else
        {
            super.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s)
    {
        if(super.getCause() != null)
        {
            s.print((new StringBuilder(String.valueOf(getClass().getName()))).append(" Caused by: ").toString());
            super.getCause().printStackTrace(s);
        } else
        {
            super.printStackTrace(s);
        }
    }
}
