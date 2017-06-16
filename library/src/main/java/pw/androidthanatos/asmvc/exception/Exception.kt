package pw.androidthanatos.asmvc.exception

/**
 * Created by liuxiongfei on 2017/6/15.
 */


class ASMVCContextException(message: String): IllegalArgumentException(message)

class ASMVCAddActivityException(message: String): IllegalArgumentException(message)

class ASMVCControllerNullException(message: String): RuntimeException(message)

class ASMVCStartIndexAPIFailerException(message: String): RuntimeException(message)

class ASMVCindexAPIparameterTypeErrorException(message: String): RuntimeException(message)