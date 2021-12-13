// See README.md for license details.

package iu

import chisel3._
import chisel3.util._

class Iu(workPeriod: UInt = 6.U) extends Module {
  val io = IO(new Bundle {
    val miss          = Input(Bool())
    val pc_curr       = Input(UInt(64.W))
    val insn_curr     = Input(UInt(32.W))
    val pc_pre        = Output(UInt(64.W))
    val pc_pre_oe     = Output(Bool())
  })

  val s_idle::s_start::s_end::Nil = Enum(3)

  val state = RegInit(s_idle)
  val cnt = Reg(UInt(8.W))
  val cpc = Reg(UInt(64.W))
  val npc = Reg(UInt(64.W))

  switch (state) {
    is (s_idle) {
      printf(p"S idle cnt = $cnt\n")
      when(io.miss) {
        state := s_start
      } .otherwise {
        state := s_start
      }
    }
    is (s_start) {
      printf(p"S start cnt = $cnt\n")
      when(cnt >= workPeriod) {
        state := s_end
      } .otherwise {
        state := s_start
      }
    }
    is (s_end) {
      printf(p"S end cnt = $cnt\n")
      state := s_idle
    }
  }

  when (state === s_end) {
    io.pc_pre_oe := true.B
    io.pc_pre := npc
  } .otherwise {
    io.pc_pre_oe := false.B
    io.pc_pre := 0.U
  }

  cpc := io.pc_curr
  npc := cpc + 4.U
  when (state === s_end) {
    cnt := 0.U
  } .otherwise {
    cnt := cnt + 1.U
  }
}

import chisel3.stage.ChiselStage

object IuDriver extends App {
  val myverilog = (new ChiselStage).emitVerilog(new Iu, args)
}
