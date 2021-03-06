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

public class ExceptionCode {

  private final String _message;

  protected ExceptionCode(final String message) {
    this._message = message;
  }

  public String getMessage() {
    return this._message;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ExceptionCode:");
    buffer.append(" _message: ");
    buffer.append(this._message);
    buffer.append("]");
    return buffer.toString();
  }

}
