import zio.test.Assertion
import zio.test.{DefaultRunnableSpec, assert, suite}

object FooTest extends DefaultRunnableSpec:

  def spec =
    suite("FooTest")(
      test("Math works")(assert(3 + 2)(Assertion.equalTo(5)))
    )
