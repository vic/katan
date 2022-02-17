package katan

import zio.prelude.Newtype

object Var extends Newtype[Int]
type Var = Var.Type
