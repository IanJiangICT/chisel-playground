// See README.md for license details.

package iu

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.stage.ChiselStage

class IuSpec extends FreeSpec with ChiselScalatestTester {

  "IU should xxx" in {
    test(new Iu()) { dut =>
      dut.clock.step(32)
    }
  }
}
