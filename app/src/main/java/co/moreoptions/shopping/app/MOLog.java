package co.moreoptions.shopping.app;

import com.crashlytics.android.Crashlytics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.moreoptions.shopping.Utils.ApplicationMode;
import co.moreoptions.shopping.models.AppInfo;


public class MOLog {


    private static final Log LOG_INSTANCE;

    static {
     //   if(AppInfo.sRunningMode == ApplicationMode.PROD) { // TODO : Rahul create BuildConfiguration to get Running more
        if(false){
            LOG_INSTANCE = new CrashlyticsLog();
        } else {
            LOG_INSTANCE = new DebugLog();
        }
    }

    /** Log a verbose message with optional format args. */
    public static void v(String message, Object... args) {
        LOG_INSTANCE.v(message, args);
    }

    /** Log a verbose exception and a message with optional format args. */
    public static void v(Throwable t, String message, Object... args) {
        LOG_INSTANCE.v(t, message, args);
    }

    /** Log a debug message with optional format args. */
    public static void d(String message, Object... args) {
        LOG_INSTANCE.d(message, args);
    }

    /** Log a debug exception and a message with optional format args. */
    public static void d(Throwable t, String message, Object... args) {
        LOG_INSTANCE.d(t, message, args);
    }

    /** Log an info message with optional format args. */
    public static void i(String message, Object... args) {
        LOG_INSTANCE.i(message, args);
    }

    /** Log an info exception and a message with optional format args. */
    public static void i(Throwable t, String message, Object... args) {
        LOG_INSTANCE.i(t, message, args);
    }

    /** Log a warning message with optional format args. */
    public static void w(String message, Object... args) {
        LOG_INSTANCE.w(message, args);
    }

    /** Log a warning exception and a message with optional format args. */
    public static void w(Throwable t, String message, Object... args) {
        LOG_INSTANCE.w(t, message, args);
    }

    @Deprecated
    /** Log an error message, only for backward compatibility */
    public static void e(String message, Throwable throwable) {
        LOG_INSTANCE.e(throwable, message);
    }

    /** Log an error message with optional format args. */
    public static void e(String message, Object... args) {
        LOG_INSTANCE.e(message, args);
    }

    /** Log an error exception and a message with optional format args. */
    public static void e(Throwable t, String message, Object... args) {
        LOG_INSTANCE.e(t, message, args);
    }

    /** Log a What a Terrible Failure: Report a condition that should never happen. */
    public static void wtf(String message, Object... args) {
        LOG_INSTANCE.wtf(message, args);
    }

    /** Log a What a Terrible Failure: Report a condition that should never happen. */
    public static void wtf(Throwable t, String message, Object... args) {
        LOG_INSTANCE.wtf(t, message, args);
    }

    /** Log this in Crashlytics. */
    public static void report(String message, Object... args) {
        LOG_INSTANCE.report(message, args);
    }

    /** Log this in Crashlytics. */
    public static void report(Throwable t, String message, Object... args) {
        LOG_INSTANCE.report(t, message, args);
    }

    /** A facade for attaching tags to logging calls. */
    public interface TaggedLog extends Log {
        /** Set a one-time tag for use on the next logging call. */
        void tag(String tag);
    }

    public interface Log {
        /** Log a verbose message with optional format args. */
        void v(String message, Object... args);

        /** Log a verbose exception and a message with optional format args. */
        void v(Throwable t, String message, Object... args);

        /** Log a debug message with optional format args. */
        void d(String message, Object... args);

        /** Log a debug exception and a message with optional format args. */
        void d(Throwable t, String message, Object... args);

        /** Log an info message with optional format args. */
        void i(String message, Object... args);

        /** Log an info exception and a message with optional format args. */
        void i(Throwable t, String message, Object... args);

        /** Log a warning message with optional format args. */
        void w(String message, Object... args);

        /** Log a warning exception and a message with optional format args. */
        void w(Throwable t, String message, Object... args);

        /** Log an error message with optional format args. */
        void e(String message, Object... args);

        /** Log an error exception and a message with optional format args. */
        void e(Throwable t, String message, Object... args);

        /** Log a What a Terrible Failure: Report a condition that should never happen. */
        void wtf(String message, Object... args);

        /** Log a What a Terrible Failure: Report a condition that should never happen. */
        void wtf(Throwable t, String message, Object... args);

        /** Log this in Crashlytics. */
        void report(String message, Object... args);

        /** Log this in Crashlytics. */
        void report(Throwable t, String message, Object... args);

    }

    public static class CrashlyticsLog implements Log {
        @Override public void v(String message, Object... args) {
        }

        @Override public void v(Throwable t, String message, Object... args) {
        }

        @Override public void d(String message, Object... args) {
        }

        @Override public void d(Throwable t, String message, Object... args) {
        }

        @Override public void i(String message, Object... args) {
        }

        @Override public void i(Throwable t, String message, Object... args) {
        }

        @Override public void w(String message, Object... args) {
            Crashlytics.log(android.util.Log.WARN, "WARN", maybeFormat(message, args));
        }

        @Override public void w(Throwable t, String message, Object... args) {
            Crashlytics.log(android.util.Log.WARN, "WARN", maybeFormat(message + " -- " + t.toString(), args));
        }

        @Override public void e(String message, Object... args) {
            Crashlytics.log(android.util.Log.ERROR, "ERROR", maybeFormat(message, args));
        }

        @Override public void e(Throwable t, String message, Object... args) {
            Crashlytics.log(android.util.Log.ERROR, "ERROR",
                    maybeFormat(message + " -- " + t.toString(), args));
        }

        @Override public void wtf(String message, Object... args) {
            Crashlytics.logException(new Exception("WTF : " + maybeFormat(message, args)));
        }

        @Override public void wtf(Throwable t, String message, Object... args) {
            Crashlytics.logException(new Exception("WTF : " + maybeFormat(message, args), t));
        }

        @Override public void report(String message, Object... args) {
            Crashlytics.logException(new Exception("REPORT : " + maybeFormat(message, args)));
        }

        @Override public void report(Throwable t, String message, Object... args) {
            Crashlytics.logException(new Exception("REPORT : " + maybeFormat(message, args)));
        }

        private static String maybeFormat(String message, Object... args) {
            return args.length == 0 ? message : String.format(message, args);
        }
    }

    public static class DebugLog implements TaggedLog {
        private static final int MAX_LOG_LENGTH = 4000;
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");
        private static final ThreadLocal<String> NEXT_TAG = new ThreadLocal<String>();

        @Override public final void tag(String tag) {
            NEXT_TAG.set(tag);
        }

        protected final String nextTag() {
            String tag = NEXT_TAG.get();
            if (tag != null) {
                NEXT_TAG.remove();
            }
            return tag;
        }

        protected String createTag() {
            String tag = nextTag();
            if (tag != null) {
                return tag;
            }

            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length < 5) {
                return "TAG";
            }
            tag = stackTrace[4].getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(tag);
            if (m.find()) {
                tag = m.replaceAll("");
            }
            return tag.substring(tag.lastIndexOf('.') + 1);
        }

        private static String maybeFormat(String message, Object... args) {
            return args.length == 0 ? message : String.format(message, args);
        }

        @Override public final void v(String message, Object... args) {
            throwShade(android.util.Log.VERBOSE, maybeFormat(message, args), null);
        }

        @Override public final void v(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.VERBOSE, maybeFormat(message, args), t);
        }

        @Override public final void d(String message, Object... args) {
            throwShade(android.util.Log.DEBUG, maybeFormat(message, args), null);
        }

        @Override public final void d(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.DEBUG, maybeFormat(message, args), t);
        }

        @Override public final void i(String message, Object... args) {
            throwShade(android.util.Log.INFO, maybeFormat(message, args), null);
        }

        @Override public final void i(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.INFO, maybeFormat(message, args), t);
        }

        @Override public final void w(String message, Object... args) {
            throwShade(android.util.Log.WARN, maybeFormat(message, args), null);
        }

        @Override public final void w(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.WARN, maybeFormat(message, args), t);
        }

        @Override public final void e(String message, Object... args) {
            throwShade(android.util.Log.ERROR, maybeFormat(message, args), null);
        }

        @Override public final void e(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.ERROR, maybeFormat(message, args), t);
        }

        @Override public void wtf(String message, Object... args) {
            throwShade(android.util.Log.ASSERT, maybeFormat(message, args), null);
        }

        @Override public void wtf(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.ASSERT, maybeFormat(message, args), t);
        }

        @Override public void report(String message, Object... args) {
            throwShade(android.util.Log.DEBUG, maybeFormat(message, args), null);
        }

        @Override public void report(Throwable t, String message, Object... args) {
            throwShade(android.util.Log.DEBUG, maybeFormat(message, args), t);
        }

        private void throwShade(int priority, String message, Throwable t) {
            if (message == null || message.length() == 0) {
                if (t == null) {
                    return;
                }
                message = android.util.Log.getStackTraceString(t);
            } else if (t != null) {
                message += "\n" + android.util.Log.getStackTraceString(t);
            }

            String tag = createTag();
            logMessage(priority, tag, message);
        }

        protected void logMessage(int priority, String tag, String message) {
            if (message.length() < MAX_LOG_LENGTH) {
                android.util.Log.println(priority, tag, message);
                return;
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    android.util.Log.println(priority, tag, message.substring(i, end));
                    i = end;
                } while (i < newline);
            }
        }
    }

}
