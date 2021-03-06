/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.javakontor.sherlog.util.exception;

/**
 * A RuntimeException that is thrown inside Sherlog.
 * 
 * <p>
 * Instead of subclassing this Exception clients should contribute own {@link ExceptionCode ExceptionCodes}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class SherlogException extends RuntimeException {

  /** serialVersionUID */
  private static final long   serialVersionUID = -2322126644590371742L;

  private final ExceptionCode _exceptionCode;

  private final Object[]      _args;

  public SherlogException(final ExceptionCode exceptionCode, final Throwable cause, final Object... args) {
    super(cause);

    this._exceptionCode = exceptionCode;
    this._args = args;

  }

  public SherlogException(final ExceptionCode exceptionCode) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = new Object[] {};
  }

  // public SherlogException(final ExceptionCode exceptionCode, final Object arg) {
  // super();
  //
  // this._exceptionCode = exceptionCode;
  // this._args = new Object[] { arg };
  // }

  public SherlogException(final ExceptionCode exceptionCode, final Object... args) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = args;
  }

  public ExceptionCode getExceptionCode() {
    return this._exceptionCode;
  }

  public Object[] getArgs() {
    return this._args;
  }

  @Override
  public String getMessage() {
    return String.format(this._exceptionCode.getMessage(), this._args);
  }

}
