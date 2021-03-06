package org.javakontor.sherlog.ui.histogram.mock.internal;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import org.javakontor.sherlog.domain.LogEvent;
import org.javakontor.sherlog.domain.LogEventSource;
import org.javakontor.sherlog.domain.LogLevel;

/**
 * <p>
 * Implements a {@link LogEventMock}.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LogEventMock implements LogEvent {

  /**
   *
   */
  private static final long         serialVersionUID   = 1L;

  private final String              _category;

  private final long                _identifier;

  private final LogLevel            _logLevel;

  private final String              _logSource;

  private final String              _message;

  private final String              _threadName;

  private final long                _timestamp;

  private final Throwable           _throwable;

  private final Map<Object, Object> _userDefinedFields = new Hashtable<Object, Object>();

  /**
   * <p>
   * Creates a new instance of type {@link LogEventMock}.
   * </p>
   *
   * @param category
   * @param identifier
   * @param logLevel
   * @param logSource
   * @param message
   * @param threadName
   * @param throwable
   */
  public LogEventMock(String category, long identifier, LogLevel logLevel, String logSource, String message,
      String threadName, Throwable throwable) {
    super();
    this._category = category;
    this._identifier = identifier;
    this._logLevel = logLevel;
    this._logSource = logSource;
    this._message = message;
    this._threadName = threadName;
    this._throwable = throwable;
    this._timestamp = System.currentTimeMillis() + new Random().nextInt(5000000);
  }

  public String getCategory() {
    return this._category;
  }

  public long getIdentifier() {
    return this._identifier;
  }

  public LogEventSource getLogEventSource() {
    return new LogEventSource() {

      public String getSource() {
        return LogEventMock.this._logSource;
      }
    };
  }

  public LogLevel getLogLevel() {
    return this._logLevel;
  }

  public String getMessage() {
    return this._message;
  }

  public Object getNestedDiagnosticContext() {
    return null;
  }

  public String getThreadName() {
    return this._threadName;
  }

  public Object getThrowableInformation() {
    return this._throwable;
  }

  public String getThrowableInformationAsString() {

    if (this._throwable == null) {
      return null;
    }

    StringBuffer buffer = new StringBuffer();
    buffer.append(this._throwable.toString());
    for (StackTraceElement element : this._throwable.getStackTrace()) {
      buffer.append(element.toString());
    }

    return null;
  }

  public long getTimeStamp() {
    return this._timestamp;
  }

  public void setUserDefinedField(Object key, Object value) {
    if (value == null) {
      this._userDefinedFields.remove(key);
    } else {
      this._userDefinedFields.put(key, value);
    }
  }

  public Map<Object, Object> getUserDefinedFields() {
    return this._userDefinedFields;
  }

  public Object getUserDefinedField(Object key) {
    return this._userDefinedFields.get(key);
  }

  public Object getUserDefinedField(String fieldName) {
    // TODO Auto-generated method stub
    return null;
  }

  public String[] getUserDefinedFieldNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setUserDefinedField(String fieldName, Object value) {
    // TODO Auto-generated method stub

  }

  public boolean hasNestedDiagnosticContext() {
    return false;
  }

  public boolean hasThrowableInformation() {
    return this._throwable != null;
  }

  public int compareTo(LogEvent o) {
    return new Long(this._timestamp).compareTo(o.getTimeStamp());
  }
}
