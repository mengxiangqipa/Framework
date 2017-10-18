package com.danikula.videocache;

/**
 * Indicates any error in work of {@link ProxyCache}.
 *
 * @author Alexey Danilov
 */
public class ProxyCacheException extends Exception
{
    private static boolean showException = true;
    private static final String LIBRARY_VERSION = ". Version: " + BuildConfig.VERSION_NAME;

    public ProxyCacheException(String message)
    {
        super(showException?message + LIBRARY_VERSION:"播放异常");
    }

    public ProxyCacheException(String message, Throwable cause)
    {
        super(showException?message + LIBRARY_VERSION:"播放异常", cause);
    }

    public ProxyCacheException(Throwable cause)
    {
        super("No explanation error" + LIBRARY_VERSION, cause);
    }
}
