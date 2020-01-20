package com.sunragav.boardview.data.local.json.models


data class JSONFrame(
    val p:Int,
    val x:Int,
    val y:Int
)

data class FrameWrapper(
    val moves:List<JSONFrame>?
)