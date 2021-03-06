// Copyright (c) 2017-2018 Twitter, Inc.
// Licensed under the Apache License, Version 2.0 (see LICENSE.md).
package rsc.diffmjar

import java.nio.file._
import rsc.checkbase._
import rsc.checkmjar

object Main extends MainBase[Settings, Int, Path, Path] {
  def settings(args: List[String]) = {
    Settings.parse(args)
  }

  def inputs(settings: Settings) = {
    0.until(settings.nscClasspath.length).toList
  }

  def nscResult(settings: Settings, i: Int) = {
    Right(settings.nscClasspath(i))
  }

  def rscResult(settings: Settings, i: Int) = {
    Right(settings.rscClasspath(i))
  }

  def checker(settings: Settings, nscResult: Path, rscResult: Path) = {
    new checkmjar.Checker(nscResult, rscResult)
  }
}
