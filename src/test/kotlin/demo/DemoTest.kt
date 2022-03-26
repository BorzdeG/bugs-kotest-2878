package demo

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class DemoTest : DescribeSpec() {
	init {
		describe("desc - 0") {
			it("it - 0") {
				withData(
					1 to 1,
					1 to 2
				) { (x, y) ->
					x shouldBe y
				}
			}
		}
	}
}
