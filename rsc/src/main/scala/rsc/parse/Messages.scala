// Copyright (c) 2017-2018 Twitter, Inc.
// Licensed under the Apache License, Version 2.0 (see LICENSE.md).
package rsc.parse

import rsc.lexis._
import rsc.pretty._
import rsc.report._

trait Messages {
  self: Parser =>

  def crash[T: Str: Repl](x: T): Nothing = {
    val pos = Position(input, in.offset, in.offset)
    rsc.util.crash(pos, x)
  }

  // Cf. `def syntaxError(msg: => Message, pos: Position): Unit`.
  // The Dotty counterpart emits messages unconditionally, so our method
  // only accepts fatal messages, because only those are emitted unconditionally.
  def reportPos(pos: Position, msgFn: Position => Message): Message = {
    val msg = msgFn(pos)
    if (msg.sev != FatalSeverity) {
      crash(msg.str)
    }
    reporter.append(msg)
  }

  // Cf. `syntaxErrorOrIncomplete(String)` that is implicitly taking in.offset.
  // Also cf. `syntaxError(msg: => Message, offset: Int = in.offset): Unit`.
  //
  // The former method was reporting an error and skipping until the next safe point.
  // The latter method was just reporting an error.
  //
  // This distinction is now encapsulated in Message.sev.
  // If the severity is fatal, we skip.
  // If the severity is error, we don't.
  def reportOffset(offset: Offset, msgFn: Position => Message): Message = {
    val length = if (in.token == ID) in.idValue.length else 0
    val pos = Position(input, offset, offset + length)
    val msg = msgFn(pos)
    reporter.append(msg)
    if (msg.sev == FatalSeverity) {
      crash(msg.str)
    }
    msg
  }

}
